package inu.project.shareu.service;

import inu.project.shareu.advice.exception.ItemException;
import inu.project.shareu.advice.exception.LectureException;
import inu.project.shareu.advice.exception.MemberException;
import inu.project.shareu.domain.*;
import inu.project.shareu.model.request.item.ItemSaveRequest;
import inu.project.shareu.model.request.item.ItemUpdateRequest;
import inu.project.shareu.model.response.item.ItemResponse;
import inu.project.shareu.repository.*;
import inu.project.shareu.repository.query.CartQueryRepository;
import inu.project.shareu.repository.query.ItemQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final PointRepository pointRepository;
    private final LectureRepository lectureRepository;
    private final CartQueryRepository cartQueryRepository;
    private final ItemQueryRepository itemQueryRepository;

    /**
     * 족보 저장
     * 1. 회원 조회
     * 2. 강의 조회
     * 3. 족보 생성 및 저장
     * 4. 포인트 이력 생성 및 저장
     *
     * @Return Item
     */
    @Transactional
    public Item saveItem(Member loginMember, ItemSaveRequest itemSaveRequest) {

        Member findMember = memberRepository.findById(loginMember.getId())
                .orElseThrow(() -> new MemberException("존재하지 않는 회원입니다."));

        Lecture findLecture = lectureRepository.findWithMajorById(itemSaveRequest.getLectureId())
                .orElseThrow(() -> new LectureException("존재하지 않는 강의입니다."));

        Item item = Item.createItem(itemSaveRequest.getTitle(),
                                    itemSaveRequest.getItemContents(),
                                    findLecture,
                                    findMember,
                                    findLecture.getMajor());

        Item saveItem = itemRepository.save(item);

        Point point = Point.createPoint("족보 등록", 5, item, findMember);
        pointRepository.save(point);

        return saveItem;
    }

    /**
     * 족보 수정
     * 1. 족보 조회
     * 2. 현재 로그인한 사용자가 족보의 판매자인지 확인
     * 3. 족보 수정
     */
    @Transactional
    public void updateItem(Member member, Long itemId, ItemUpdateRequest itemUpdateRequest) {

        Item item = itemRepository.findWithMemberById(itemId)
                .orElseThrow(() -> new ItemException("존재하지 않는 상품입니다."));

        validateItemOwner(member, item);

        item.updateItem(itemUpdateRequest.getTitle(),
                        itemUpdateRequest.getItemContents());
    }

    /**
     * 관리자에 의한 족보 삭제
     * 1. 족보 조회
     * 2. 족보를 구매한 사람들에게 포인트 환불 처리
     * 3. 족보 상태 변경
     * 4. 족보 작성자의 포인트 초기화 및 포인트 이력 생성 및 저장
     * @Return Item
     */
    @Transactional
    public Item deleteItemByAdmin(Long itemId){

        Item item = itemRepository.findWithMemberById(itemId)
                .orElseThrow(() -> new ItemException("존재하지 않는 족보입니다."));

        refundPointToOrderMember(item);

        int changePoint = item.deleteItemByAdmin();

        Point point = Point.createPoint("족보 신고 처리로 인한 포인트 초기화",
                                        changePoint, item, item.getMember());
        pointRepository.save(point);

        return item;
    }


    /**
     * 족보 삭제
     * 1. 족보 조회
     * 2. 현재 로그인한 사용자가 족보의 판매자인지 확인
     * 3. 족보 삭제
     * @Return item
     */
    @Transactional
    public Item deleteItem(Member member, Long itemId) {

        Item item = itemRepository.findWithMemberById(itemId)
                .orElseThrow(() -> new ItemException("존재하지 않는 족보입니다."));

        validateItemOwner(member,item);

        item.deleteItem();

        return item;
    }

    /**
     * 내가 올린 족보 페이징 조회
     * 1. 내가 올린 족보 조회
     * 2. DTO로 변환해서 반환
     * @return Page<ItemResponse>
     */
    public Page<ItemResponse> findMyItemPage(Member member, Pageable pageable) {
        Page<Item> items = itemQueryRepository.findPageByMember(member, pageable);

        return items.map(item -> new ItemResponse(item,item.getLecture()));
    }

    /**
     * 족보를 구매한 사람들 모두 환불 처리
     */
    private void refundPointToOrderMember(Item item) {
        List<Cart> carts = cartQueryRepository.findWithMemberByItemAndCartStatus(item,CartStatus.ORDER);

        if(!carts.isEmpty()){
            carts.forEach(cart -> {
                cart.cancel();
                Point refundPoint = Point.createPoint("족보 신고 처리로 인한 환불",
                        cart.getOrderPrice(),
                        item, cart.getMember());
                pointRepository.save(refundPoint);
            });
        }
    }

    /**
     * 사용자가 족보의 사용자와 일치하는 지 여부 확인
     * why? id 비교
     * member가 준영속 상태이기 때문이다.
     */
    private void validateItemOwner(Member member, Item item) {
        if (!member.getId().equals(item.getMember().getId())) {
            throw new MemberException("족보의 판매자가 아닙니다.");
        }
    }


}
