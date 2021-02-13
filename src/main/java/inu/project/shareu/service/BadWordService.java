package inu.project.shareu.service;

import inu.project.shareu.advice.exception.BadWordException;
import inu.project.shareu.domain.BadWord;
import inu.project.shareu.model.request.badword.BadWordSaveRequest;
import inu.project.shareu.repository.BadWordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BadWordService {

    private final BadWordRepository badWordRepository;

    @Transactional
    public void saveBadWord(BadWordSaveRequest badWordSaveRequest) {
        if(badWordRepository.findByWord(badWordSaveRequest.getBadWord()).isPresent()){
            throw new BadWordException("이미 존재하는 금칙어입니다.");
        }

        BadWord badWord = BadWord.createBadWord(badWordSaveRequest.getBadWord());

        badWordRepository.save(badWord);
    }

    @Transactional
    public void deleteBadWord(Long badWordId) {
        BadWord findBadWord = badWordRepository.findById(badWordId)
                .orElseThrow(() -> new BadWordException("존재하지 않는 금칙어입니다."));

        badWordRepository.delete(findBadWord);
    }

    public void checkForbiddenWord(String word,List<String> forbiddenWords){

        Optional<String> forbiddenWord = forbiddenWords.stream()
                .filter(word::contains).findFirst();

        if(forbiddenWord.isPresent()){
            throw new BadWordException("금칙어 " + forbiddenWord.get() + "가 포함되어 있습니다.");
        }
    }

}
