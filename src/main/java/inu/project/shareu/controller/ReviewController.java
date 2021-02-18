package inu.project.shareu.controller;

import inu.project.shareu.config.security.LoginMember;
import inu.project.shareu.domain.Member;
import inu.project.shareu.model.request.review.ReviewSaveRequest;
import inu.project.shareu.model.request.review.ReviewUpdateRequest;
import inu.project.shareu.service.BadWordService;
import inu.project.shareu.service.ReviewService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Api(tags = "4.리뷰")
@Slf4j
@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final BadWordService badWordService;

    @ApiOperation(value = "리뷰 등록",notes = "리뷰 등록")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization",value = "로그인 성공 후 access_token",required = true
                    ,dataType = "String", paramType = "header")
    })
    @PostMapping("/reviews")
    public ResponseEntity saveReview(@ModelAttribute ReviewSaveRequest reviewSaveRequest) {

        Member member = getLoginMember();

        badWordService.validateItemForbiddenWord(reviewSaveRequest.getReviewContents());

        reviewService.saveReview(member, reviewSaveRequest);

        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "리뷰 수정",notes = "리뷰 수정")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization",value = "로그인 성공 후 access_token",required = true
                    ,dataType = "String", paramType = "header")
    })
    @PatchMapping("/reviews/{reviewId}")
    public ResponseEntity updateReview(@PathVariable Long reviewId,
                                       @ModelAttribute ReviewUpdateRequest reviewUpdateRequest) {

        Member member = getLoginMember();

        badWordService.validateItemForbiddenWord(reviewUpdateRequest.getReviewContents());

        reviewService.updateReview(reviewId,member,reviewUpdateRequest);

        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "리뷰 삭제",notes = "리뷰 삭제")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization",value = "로그인 성공 후 access_token",required = true
                    ,dataType = "String", paramType = "header")
    })
    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity deleteReview(@PathVariable Long reviewId){

        Member member = getLoginMember();

        reviewService.deleteReview(reviewId,member);

        return ResponseEntity.ok().build();
    }

    /**
     * 현재 로그인한 사용자를 가져온다.
     */
    private Member getLoginMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginMember loginMember = (LoginMember) authentication.getPrincipal();
        return loginMember.getMember();
    }

}
