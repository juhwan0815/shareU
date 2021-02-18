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

    /**
     * 금칙어 추가
     * 1. 동일 금칙어 존재 여부 확인
     * 2. 금칙어 저장
     */
    @Transactional
    public void saveBadWord(BadWordSaveRequest badWordSaveRequest) {

        validateSameBadWord(badWordSaveRequest);

        BadWord badWord = BadWord.createBadWord(badWordSaveRequest.getBadWord());
        badWordRepository.save(badWord);
    }

    /**
     * 금칙어 삭제
     * 1. 금칙어 조회
     * 2. 금칙어 삭제
     */
    @Transactional
    public void deleteBadWord(Long badWordId) {

        BadWord findBadWord = badWordRepository.findById(badWordId)
                .orElseThrow(() -> new BadWordException("존재하지 않는 금칙어입니다."));

        badWordRepository.delete(findBadWord);
    }

    /**
     * 족보의 금칙어 필터링
     * 1. 모든 금칙어 조회
     * 2. 금칙어 포함 여부 확인
     */
    public void validateItemForbiddenWord(String word1,String word2){

        List<String> forbiddenWords = findAllToString();

        checkForbiddenWord(word1,forbiddenWords);
        checkForbiddenWord(word2,forbiddenWords);
    }

    /**
     * 족보의 금칙어 필터링
     * 1. 모든 금칙어 조회
     * 2. 금칙어 포함 여부 확인
     */
    public void validateItemForbiddenWord(String word){

        List<String> forbiddenWords = findAllToString();

        checkForbiddenWord(word,forbiddenWords);
    }

    /**
     * 모든 금칙어 조회
     * 1. 모든 금칙어를 조회
     * 2. 모든 금칙어의 단어를 List로 변환
     * @Return List<String>
     */
    public List<String> findAllToString(){
        return badWordRepository.findAll().stream()
                .map(badWord -> badWord.getWord())
                .collect(Collectors.toList());
    }


    /**
     * 금칙어 포함 여부 확인
     */
    public void checkForbiddenWord(String word,List<String> forbiddenWords){

        Optional<String> forbiddenWord = forbiddenWords.stream()
                .filter(word::contains).findFirst();

        if(forbiddenWord.isPresent()){
            throw new BadWordException("금칙어 " + forbiddenWord.get() + " 포함");
        }
    }


    /**
     * 동일 금칙어 존재 여부 확인
     */
    private void validateSameBadWord(BadWordSaveRequest badWordSaveRequest) {
        if(badWordRepository.findByWord(badWordSaveRequest.getBadWord()).isPresent()){
            throw new BadWordException("이미 존재하는 금칙어입니다.");
        }
    }

}
