package inu.project.shareu.service;

import inu.project.shareu.advice.exception.ItemException;
import inu.project.shareu.advice.exception.MemberException;
import inu.project.shareu.advice.exception.ReviewException;
import inu.project.shareu.domain.Item;
import inu.project.shareu.domain.Member;
import inu.project.shareu.domain.Review;
import inu.project.shareu.model.request.review.ReviewSaveRequest;
import inu.project.shareu.model.request.review.ReviewUpdateRequest;
import inu.project.shareu.repository.ItemRepository;
import inu.project.shareu.repository.MemberRepository;
import inu.project.shareu.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public void saveReview(Long memberId, ReviewSaveRequest reviewSaveRequest) {

        Member findMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException("존재하지 않는 회원입니다."));

        Item item = itemRepository.findById(reviewSaveRequest.getItemId())
                .orElseThrow(() -> new ItemException("존재하지 않는 상품입니다."));

        // TODO 한명은 상품에 한개의 리뷰만 작성 가능
        // TODO 상품 구매 여부 확인 로직 필요

        Review review = Review.createReview(reviewSaveRequest.getReviewContents(),
                reviewSaveRequest.getRecommend(),
                item, findMember);

        reviewRepository.save(review);
    }

    @Transactional
    public void updateReview(Long reviewId, Long memberId,
                             ReviewUpdateRequest reviewUpdateRequest) {

        Review findReview = reviewRepository.findWithMemberById(reviewId)
                .orElseThrow(() -> new ReviewException("존재하지 않는 리뷰입니다."));

        if(!findReview.getMember().getId().equals(memberId)){
            throw new MemberException("리뷰의 작성자가 아닙니다.");
        }

        findReview.updateReview(reviewUpdateRequest.getReviewContents(),
                reviewUpdateRequest.getRecommend());
    }

    @Transactional
    public void deleteReview(Long reviewId, Long memberId) {

        Review findReview = reviewRepository.findWithMemberById(reviewId)
                .orElseThrow(() -> new ReviewException("존재하지 않는 리뷰입니다."));

        if(!findReview.getMember().getId().equals(memberId)) {
            throw new MemberException("리뷰의 작성자가 아닙니다.");
        }

        reviewRepository.delete(findReview);
    }
}
