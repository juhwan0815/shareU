package inu.project.shareu.service;

import inu.project.shareu.advice.exception.*;
import inu.project.shareu.config.security.LoginMember;
import inu.project.shareu.domain.*;
import inu.project.shareu.model.request.review.ReviewSaveRequest;
import inu.project.shareu.model.request.review.ReviewUpdateRequest;
import inu.project.shareu.model.response.review.ReviewResponse;
import inu.project.shareu.repository.*;
import inu.project.shareu.repository.query.ReviewQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final ReviewQueryRepository reviewQueryRepository;

    /**
     * 리뷰 등록
     * 1. 회원 조회
     * 2. 족보 조회
     * 3. 이미 족보에 대해 회원이 리뷰를 작성한 적이 있는지 확인
     * 4. 회원이 족보를 구매했는지 확인
     * 5. 리뷰 생성 및 저장
     * 6. 포인트 이력 생성 및 저장
     */
    @Transactional
    public void saveReview(Member loginMember, ReviewSaveRequest reviewSaveRequest) {

        Member member = memberRepository.findById(loginMember.getId())
                .orElseThrow(() -> new MemberException("존재하지 않는 회원입니다."));

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

        Review findReview = reviewRepository.findWithItemById(reviewId)
                .orElseThrow(() -> new ReviewException("존재하지 않는 리뷰입니다."));

        validateReviewOwner(member, findReview);

        findReview.updateReview(reviewUpdateRequest.getReviewContents(),
                                reviewUpdateRequest.getRecommend());
    }

    /**
     * 관리자 리뷰 삭제
     * 1. 리뷰 조회
     * 2. 리뷰 작성자 포인트 초기화
     * 3. 포인트 초기화 이력 생성 및 저장
     * 4. 리뷰 삭제
     */
    @Transactional
    public void deleteReviewByAdmin(Long reviewId){

        Review findReview = reviewRepository.findWithMemberAndItemById(reviewId)
                .orElseThrow(() -> new ReviewException("존재하지 않는 리뷰입니다."));

        int changePoint = findReview.getMember().getChangePoint();

        Point point = Point.createPoint("리뷰 신고로 인한 포인트 초기화 처리", changePoint,
                findReview.getItem(), findReview.getMember());
        pointRepository.save(point);

        findReview.changeStatus();
    }


    /**
     * 리뷰 삭제
     * 1. 리뷰 조회
     * 2. 현재 로그인한 사용자가 리뷰의 작성자인지 확인
     * 3. 리뷰 삭제
     */
    @Transactional
    public void deleteReview(Long reviewId, Member member) {

        Review findReview = reviewRepository.findWithItemById(reviewId)
                .orElseThrow(() -> new ReviewException("존재하지 않는 리뷰입니다."));

        validateReviewOwner(member,findReview);

        findReview.changeStatus();
    }

    /**
     * 족보로 리뷰 페이징 조회
     * 1. 족보 조회
     * 2. 리뷰 페이징 조회
     * 3. Dto로 변환하여 반환
     * @Return Page<ReviewResponse>
     */
    public Page<ReviewResponse> findReviewPageByItemId(Long itemId, Pageable pageable) {

        Item findItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemException("존재하지 않는 족보입니다."));

        Page<Review> reviews = reviewQueryRepository
                .findPageWithMemberByItem(findItem,ReviewStatus.LIVE,pageable);
        return reviews.map(review -> new ReviewResponse(review));
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
