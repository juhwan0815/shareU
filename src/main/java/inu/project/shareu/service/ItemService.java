package inu.project.shareu.service;

import inu.project.shareu.advice.exception.ItemException;
import inu.project.shareu.advice.exception.MajorException;
import inu.project.shareu.advice.exception.MemberException;
import inu.project.shareu.domain.Item;
import inu.project.shareu.domain.Major;
import inu.project.shareu.domain.Member;
import inu.project.shareu.domain.Point;
import inu.project.shareu.model.request.item.ItemSaveRequest;
import inu.project.shareu.model.request.item.ItemUpdateRequest;
import inu.project.shareu.repository.ItemRepository;
import inu.project.shareu.repository.MajorRepository;
import inu.project.shareu.repository.MemberRepository;
import inu.project.shareu.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final MajorRepository majorRepository;
    private final PointRepository pointRepository;

    @Transactional
    public void saveItem(Long memberId, ItemSaveRequest itemSaveRequest) {

        Member findMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException("존재하지 않는 회원입니다."));

        Major major = majorRepository.findById(itemSaveRequest.getMajorId())
                .orElseThrow(() -> new MajorException("존재하지 않는 학과입니다."));

        // TODO 금칙어 처리 -> 이 부분은 DB연동

        Item item = Item.createItem(itemSaveRequest.getTitle(),
                itemSaveRequest.getItemContents(),
                itemSaveRequest.getClassName(),
                itemSaveRequest.getProfessor(),
                findMember,
                major);

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

        item.updateItem(itemUpdateRequest.getTitle(),
                itemUpdateRequest.getItemContents(),
                itemUpdateRequest.getClassName(),
                itemUpdateRequest.getProfessor());
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
