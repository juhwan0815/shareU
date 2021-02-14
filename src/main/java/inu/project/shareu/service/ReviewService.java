package inu.project.shareu.service;

import inu.project.shareu.advice.exception.ItemException;
import inu.project.shareu.advice.exception.MemberException;
import inu.project.shareu.advice.exception.OrderException;
import inu.project.shareu.advice.exception.ReviewException;
import inu.project.shareu.config.security.LoginMember;
import inu.project.shareu.domain.*;
import inu.project.shareu.model.request.review.ReviewSaveRequest;
import inu.project.shareu.model.request.review.ReviewUpdateRequest;
import inu.project.shareu.repository.*;
import inu.project.shareu.repository.query.OrderQueryRepository;
import inu.project.shareu.repository.query.ReviewQueryRepository;
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
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final PointRepository pointRepository;
    private final OrderQueryRepository orderQueryRepository;
    private final ReviewQueryRepository reviewQueryRepository;
    private final BadWordRepository badWordRepository;
    private final BadWordService badWordService;

    @Transactional
    public void saveReview(Long memberId, ReviewSaveRequest reviewSaveRequest) {

        Member findMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException("존재하지 않는 회원입니다."));

        Item item = itemRepository.findById(reviewSaveRequest.getItemId())
                .orElseThrow(() -> new ItemException("존재하지 않는 족보입니다."));

        List<Order> orders = orderQueryRepository.findOrderWithCartByMemberAndItem(findMember, item);
        if(orders.isEmpty()){
            throw new OrderException("족보에 대한 리뷰를 작성할 수 없습니다.");
        }

        List<Review> reviews= reviewQueryRepository.findReviewByMemberAndItem(findMember, item);
        if(!reviews.isEmpty()){
            throw new ReviewException("이미 족보에 대한 리뷰를 작성하였습니다.");
        }

        List<String> forbiddenWords = badWordRepository.findAll().stream()
                .map(badWord -> badWord.getWord())
                .collect(Collectors.toList());

        badWordService.checkForbiddenWord(reviewSaveRequest.getReviewContents(),forbiddenWords);

        Review review = Review.createReview(reviewSaveRequest.getReviewContents(),
                reviewSaveRequest.getRecommend(),
                item, findMember);

        Point point = Point.createPoint("리뷰 등록", 3, item, findMember);

        reviewRepository.save(review);
        pointRepository.save(point);
    }

    @Transactional
    public void updateReview(Long reviewId, Long memberId,
                             ReviewUpdateRequest reviewUpdateRequest) {

        Review findReview = reviewRepository.findWithMemberById(reviewId)
                .orElseThrow(() -> new ReviewException("존재하지 않는 리뷰입니다."));

        if(!findReview.getMember().getId().equals(memberId)){
            throw new MemberException("리뷰의 작성자가 아닙니다.");
        }

        List<String> forbiddenWords = badWordRepository.findAll().stream()
                .map(badWord -> badWord.getWord())
                .collect(Collectors.toList());

        badWordService.checkForbiddenWord(reviewUpdateRequest.getReviewContents(),forbiddenWords);


        findReview.updateReview(reviewUpdateRequest.getReviewContents(),
                reviewUpdateRequest.getRecommend());
    }

    @Transactional
    public void deleteReview(Long reviewId, LoginMember loginMember) {

        Review findReview = reviewRepository.findWithMemberAndItemById(reviewId)
                .orElseThrow(() -> new ReviewException("존재하지 않는 리뷰입니다."));

        List<GrantedAuthority> adminRole = loginMember.getAuthorities().stream()
                .filter(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))
                .collect(Collectors.toList());

        if(adminRole.isEmpty()){

            if(!findReview.getMember().getId().equals(loginMember.getId())) {
                throw new MemberException("리뷰의 작성자가 아닙니다.");
            }
            reviewRepository.delete(findReview);

        }else {

            int changePoint = findReview.getMember().changeCountAndPoint();
            Point point = Point.createPoint("리뷰 신고로 인한 포인트 초기화 처리", changePoint,
                    findReview.getItem(), findReview.getMember());

            pointRepository.save(point);
            reviewRepository.delete(findReview);
        }


    }
}
