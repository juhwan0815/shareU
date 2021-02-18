package inu.project.shareu.service;

import inu.project.shareu.advice.exception.*;
import inu.project.shareu.config.security.LoginMember;
import inu.project.shareu.domain.*;
import inu.project.shareu.model.request.review.ReviewSaveRequest;
import inu.project.shareu.model.request.review.ReviewUpdateRequest;
import inu.project.shareu.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final CartRepository cartRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final PointRepository pointRepository;

    /**
     * 리뷰 등록
     * 1. 족보 조회
     * 2. 이미 족보에 대해 회원이 리뷰를 작성한 적이 있는지 확인
     * 3. 회원이 족보를 구매했는지 확인
     * 4. 리뷰 생성 및 저장
     * 5. 포인트 이력 생성 및 저장
     * 6. 회원 저장 (merge)
     */
    @Transactional
    public void saveReview(Member member, ReviewSaveRequest reviewSaveRequest) {

        Item item = itemRepository.findById(reviewSaveRequest.getItemId())
                .orElseThrow(() -> new ItemException("존재하지 않는 족보입니다."));

        validateDuplicatedReview(member, item);

        validateItemPurchase(member, item);

        Review review = Review.createReview(reviewSaveRequest.getReviewContents(),
                                            reviewSaveRequest.getRecommend(),
                                            item, member);
        reviewRepository.save(review);

        Point point = Point.createPoint("리뷰 등록", 3, item, member);
        pointRepository.save(point);

        memberRepository.save(member); // merge
    }

    /**
     * 리뷰 수정
     * 1. 리뷰 조회
     * 2. 현재 로그인한 사용자가 리뷰의 작성자인지 확인
     * 3. 리뷰 수정
     */
    @Transactional
    public void updateReview(Long reviewId, Member member,
                             ReviewUpdateRequest reviewUpdateRequest) {

        Review findReview = reviewRepository.findWithMemberById(reviewId)
                .orElseThrow(() -> new ReviewException("존재하지 않는 리뷰입니다."));

        validateReviewOwner(member, findReview);

        findReview.updateReview(reviewUpdateRequest.getReviewContents(),
                                reviewUpdateRequest.getRecommend());
    }

    /**
     * 리뷰 삭제
     * 1. 리뷰 조회
     * 2. 현재 로그인한 사용자가 리뷰의 작성자인지 확인
     * 3. 리뷰 삭제
     */
    @Transactional
    public void deleteReview(Long reviewId, Member member) {

        Review findReview = reviewRepository.findWithMemberAndItemById(reviewId)
                .orElseThrow(() -> new ReviewException("존재하지 않는 리뷰입니다."));

        validateReviewOwner(member,findReview);

        reviewRepository.delete(findReview);

        // TODO 리뷰 어드민 api
//        if (adminRole.isEmpty()) {
//
//            if (!findReview.getMember().getId().equals(loginMember.getId())) {
//                throw new MemberException("리뷰의 작성자가 아닙니다.");
//            }
//            reviewRepository.delete(findReview);
//
//        } else {
//
//            int changePoint = findReview.getMember().getChangePoint();
//
//            if (findReview.getMember().getCurrentPoint() > 0) {
//                Point point = Point.createPoint("리뷰 신고로 인한 포인트 초기화 처리", changePoint,
//                        findReview.getItem(), findReview.getMember());
//                pointRepository.save(point);
//            }
//
//            reviewRepository.delete(findReview);
//        }
    }

    /**
     * 현재 로그인한 사용자가 리뷰의 작성자인지 확인
     */
    private void validateReviewOwner(Member member, Review findReview) {
        if (!findReview.getMember().getId().equals(member.getId())) {
            throw new MemberException("리뷰의 작성자가 아닙니다.");
        }
    }

    /**
     * 회원이 족보를 구매한 적이 있는지 확인
     */

    private void validateItemPurchase(Member member, Item item) {
        List<Cart> carts = cartRepository.findByMemberAndItemAndCartStatus(member, item, CartStatus.ORDER);
        if(carts.isEmpty()){
            throw new CartException("리뷰를 작성할 권한이 없습니다.");
        }
    }

    /**
     *  이미 족보에 대해 회원이 리뷰를 작성한 적이 있는지 확인
     */
    private void validateDuplicatedReview(Member member, Item item) {
        List<Review> reviews = reviewRepository.findByMemberAndItem(member, item);
        if (!reviews.isEmpty()) {
            throw new ReviewException("이미 족보에 대한 리뷰를 작성하였습니다.");
        }
    }
}
