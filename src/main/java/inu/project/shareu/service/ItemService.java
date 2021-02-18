package inu.project.shareu.service;

import inu.project.shareu.advice.exception.ItemException;
import inu.project.shareu.advice.exception.LectureException;
import inu.project.shareu.advice.exception.MemberException;
import inu.project.shareu.domain.*;
import inu.project.shareu.model.request.item.ItemSaveRequest;
import inu.project.shareu.model.request.item.ItemUpdateRequest;
import inu.project.shareu.repository.*;
import inu.project.shareu.repository.query.CartQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final PointRepository pointRepository;
    private final LectureRepository lectureRepository;
    private final CartQueryRepository cartQueryRepository;

    /**
     * 족보 저장
     * 1. 강의 조회
     * 2. 족보 생성 및 저장
     * 3. 포인트 이력 생성 및 저장
     * 4. 회원 저장 ( merge ) why? 준영속 상태이기 때문
     * @Return Item
     */
    @Transactional
    public Item saveItem(Member loginMember, ItemSaveRequest itemSaveRequest) {

        Lecture findLecture = lectureRepository.findWithMajorById(itemSaveRequest.getLectureId())
                .orElseThrow(() -> new LectureException("존재하지 않는 강의입니다."));

        Item item = Item.createItem(itemSaveRequest.getTitle(),
                                    itemSaveRequest.getItemContents(),
                                    findLecture,
                                    loginMember,
                                    findLecture.getMajor());

        Item saveItem = itemRepository.save(item);

        Point point = Point.createPoint("족보 등록", 5, item, loginMember);
        pointRepository.save(point);

        memberRepository.save(loginMember); // merge

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

        // TODO 아래 코드 지우기
//        if (adminRole.isEmpty()) {
//
//            if (!member.getId().equals(item.getMember().getId())) {
//                throw new MemberException("상품의 판매자가 아닙니다.");
//            }
//
//            item.deleteItem();
//            item.getStoreList().forEach(store -> storeService.deleteStore(store));
//
//        } else {
//
//            int changePoint = item.deleteItemByAdmin();
//            item.getStoreList().forEach(store -> storeService.deleteStore(store));
//
//            if(item.getMember().getCurrentPoint() > 0) {
//                Point point = Point.createPoint("족보 신고 처리로 인한 포인트 초기화", changePoint,
//                        item, item.getMember());
//                pointRepository.save(point);
//            }
//
//            List<Cart> carts = cartQueryRepository.findCartWithMemberByItemAndCartStatus(item);
//
//            if(!carts.isEmpty()){
//                carts.forEach(cart -> {
//                    int orderPrice = cart.cancel();
//                    Point refundPoint = Point.createPoint("족보 신고 처리로 인한 환불", orderPrice,
//                            item, cart.getMember());
//                    pointRepository.save(refundPoint);
//                });
//            }
//        }


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
