package inu.project.shareu.service;

import inu.project.shareu.advice.exception.BadWordException;
import inu.project.shareu.advice.exception.ItemException;
import inu.project.shareu.advice.exception.LectureException;
import inu.project.shareu.advice.exception.MemberException;
import inu.project.shareu.domain.*;
import inu.project.shareu.model.request.item.ItemSaveRequest;
import inu.project.shareu.model.request.item.ItemUpdateRequest;
import inu.project.shareu.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final PointRepository pointRepository;
    private final LectureRepository lectureRepository;
    private final BadWordService badWordService;
    private final BadWordRepository badWordRepository;

    @Transactional
    public void saveItem(Long memberId, ItemSaveRequest itemSaveRequest) {

        Member findMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException("존재하지 않는 회원입니다."));

        Lecture findLecture = lectureRepository.findWithMajorById(itemSaveRequest.getLectureId())
                .orElseThrow(() -> new LectureException("존재하지 않는 강의입니다."));

        List<String> forbiddenWords = badWordRepository.findAll().stream()
                .map(badWord -> badWord.getWord())
                .collect(Collectors.toList());

        badWordService.checkForbiddenWord(itemSaveRequest.getTitle(),forbiddenWords);
        badWordService.checkForbiddenWord(itemSaveRequest.getItemContents(),forbiddenWords);

        Item item = Item.createItem(itemSaveRequest.getTitle(),
                itemSaveRequest.getItemContents(),findLecture,
                findMember,findLecture.getMajor());

        Point point = Point.createPoint("족보 등록", 5, item, findMember);

        itemRepository.save(item);
        pointRepository.save(point);
    }


    @Transactional
    public void updateItem(Long memberId, Long itemId, ItemUpdateRequest itemUpdateRequest) {

        Item item = itemRepository.findWithMemberById(itemId)
                .orElseThrow(() -> new ItemException("존재하지 않는 상품입니다."));

        if(!memberId.equals(item.getMember().getId())){
            throw new MemberException("상품의 판매자가 아닙니다.");
        }

        List<String> forbiddenWords = badWordRepository.findAll().stream()
                .map(badWord -> badWord.getWord())
                .collect(Collectors.toList());

        badWordService.checkForbiddenWord(itemUpdateRequest.getTitle(),forbiddenWords);
        badWordService.checkForbiddenWord(itemUpdateRequest.getItemContents(),forbiddenWords);

        item.updateItem(itemUpdateRequest.getTitle(),
                itemUpdateRequest.getItemContents());
    }

    @Transactional
    public void deleteItem(Long memberId, Long itemId) {

        Item item = itemRepository.findWithMemberById(itemId)
                .orElseThrow(() -> new ItemException("존재하지 않는 상품입니다."));

        if(!memberId.equals(item.getMember().getId())){
            throw new MemberException("상품의 판매자가 아닙니다.");
        }

        item.deleteItem();
    }

}
