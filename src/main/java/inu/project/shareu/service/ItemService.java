package inu.project.shareu.service;

import inu.project.shareu.advice.exception.ItemException;
import inu.project.shareu.advice.exception.LectureException;
import inu.project.shareu.advice.exception.MemberException;
import inu.project.shareu.config.security.LoginMember;
import inu.project.shareu.domain.*;
import inu.project.shareu.model.request.item.ItemSaveRequest;
import inu.project.shareu.model.request.item.ItemUpdateRequest;
import inu.project.shareu.repository.*;
import inu.project.shareu.repository.query.CartQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
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
    private final BadWordService badWordService;
    private final BadWordRepository badWordRepository;
    private final CartQueryRepository cartQueryRepository;

    @Transactional
    public void saveItem(Long memberId, ItemSaveRequest itemSaveRequest) {

        Member findMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException("존재하지 않는 회원입니다."));

        Lecture findLecture = lectureRepository.findWithMajorById(itemSaveRequest.getLectureId())
                .orElseThrow(() -> new LectureException("존재하지 않는 강의입니다."));

        List<String> forbiddenWords = badWordRepository.findAll().stream()
                .map(badWord -> badWord.getWord())
                .collect(Collectors.toList());

        badWordService.checkForbiddenWord(itemSaveRequest.getTitle(), forbiddenWords);
        badWordService.checkForbiddenWord(itemSaveRequest.getItemContents(), forbiddenWords);

        Item item = Item.createItem(itemSaveRequest.getTitle(),
                itemSaveRequest.getItemContents(), findLecture,
                findMember, findLecture.getMajor());

        Point point = Point.createPoint("족보 등록", 5, item, findMember);

        itemRepository.save(item);
        pointRepository.save(point);
    }


    @Transactional
    public void updateItem(Long memberId, Long itemId, ItemUpdateRequest itemUpdateRequest) {

        Item item = itemRepository.findWithMemberById(itemId)
                .orElseThrow(() -> new ItemException("존재하지 않는 상품입니다."));

        if (!memberId.equals(item.getMember().getId())) {
            throw new MemberException("상품의 판매자가 아닙니다.");
        }

        List<String> forbiddenWords = badWordRepository.findAll().stream()
                .map(badWord -> badWord.getWord())
                .collect(Collectors.toList());

        badWordService.checkForbiddenWord(itemUpdateRequest.getTitle(), forbiddenWords);
        badWordService.checkForbiddenWord(itemUpdateRequest.getItemContents(), forbiddenWords);

        item.updateItem(itemUpdateRequest.getTitle(),
                itemUpdateRequest.getItemContents());
    }

    @Transactional
    public void deleteItem(LoginMember loginMember, Long itemId) {

        Item item = itemRepository.findWithMemberById(itemId)
                .orElseThrow(() -> new ItemException("존재하지 않는 상품입니다."));

        List<GrantedAuthority> adminRole = loginMember.getAuthorities().stream()
                .filter(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))
                .collect(Collectors.toList());

        if (adminRole.isEmpty()) {
            if (!loginMember.getId().equals(item.getMember().getId())) {
                throw new MemberException("상품의 판매자가 아닙니다.");
            }
            item.deleteItem();

        } else {

            int changePoint = item.deleteItemByAdmin();
            Point point = Point.createPoint("족보 신고 처리로 인한 포인트 초기화", changePoint,
                    item, item.getMember());
            pointRepository.save(point);

            // TODO 환불 검증 필요
            List<Cart> carts = cartQueryRepository.findCartWithMemberByItemAndCartStatus(item);

            if(!carts.isEmpty()){
                carts.forEach(cart -> {
                    int orderPrice = cart.cancel();
                    cart.getMember().changePoint(orderPrice);
                    Point refundPoint = Point.createPoint("족보 신고 처리로 인한 환불", orderPrice,
                            item, cart.getMember());
                    pointRepository.save(refundPoint);
                });

            }
        }


    }
}
