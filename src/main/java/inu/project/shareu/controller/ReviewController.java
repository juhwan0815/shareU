package inu.project.shareu.controller;

import inu.project.shareu.config.security.LoginMember;
import inu.project.shareu.domain.Member;
import inu.project.shareu.model.common.response.ExceptionResponse;
import inu.project.shareu.model.review.request.ReviewSaveRequest;
import inu.project.shareu.model.review.request.ReviewUpdateRequest;
import inu.project.shareu.model.review.response.ReviewResponse;
import inu.project.shareu.service.BadWordService;
import inu.project.shareu.service.ReviewService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@Api(tags = "3.리뷰")
@Slf4j
@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final BadWordService badWordService;

    @ApiOperation(value = "리뷰 등록",notes = "리뷰 등록")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization",value = "로그인 성공 후 access_token",required = true,
                    dataType = "String", paramType = "header")
    })
    @ApiResponses({
            @ApiResponse(code = 400, message = "BAD REQUEST",response = ExceptionResponse.class),
            @ApiResponse(code = 401, message = "UNAUTHORIZED", response = ExceptionResponse.class),
            @ApiResponse(code = 403, message = "FORBIDDEN", response = ExceptionResponse.class),
            @ApiResponse(code = 500, message = "INTERNAL_SERVER_ERROR", response = ExceptionResponse.class)
    })
    @PostMapping("/reviews")
    public ResponseEntity<Void> saveReview(@ModelAttribute ReviewSaveRequest reviewSaveRequest) {

        Member member = getLoginMember();

        badWordService.validateItemForbiddenWord(reviewSaveRequest.getReviewContents());

        reviewService.saveReview(member, reviewSaveRequest);

        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "리뷰 수정",notes = "리뷰 수정")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization",value = "로그인 성공 후 access_token",required = true,
                    dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "reviewId",value = "리뷰 Id",required = true,
                    dataType = "long",paramType = "path")
    })
    @ApiResponses({
            @ApiResponse(code = 400, message = "BAD REQUEST",response = ExceptionResponse.class),
            @ApiResponse(code = 401, message = "UNAUTHORIZED", response = ExceptionResponse.class),
            @ApiResponse(code = 403, message = "FORBIDDEN", response = ExceptionResponse.class),
            @ApiResponse(code = 500, message = "INTERNAL_SERVER_ERROR", response = ExceptionResponse.class)
    })
    @PatchMapping("/reviews/{reviewId}")
    public ResponseEntity<Void> updateReview(@PathVariable Long reviewId,
                                             @ModelAttribute ReviewUpdateRequest reviewUpdateRequest) {

        Member member = getLoginMember();

        badWordService.validateItemForbiddenWord(reviewUpdateRequest.getReviewContents());

        reviewService.updateReview(reviewId,member,reviewUpdateRequest);

        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "리뷰 삭제",notes = "리뷰 삭제")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization",value = "로그인 성공 후 access_token",required = true,
                    dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "reviewId",value = "리뷰 Id",required = true,
                    dataType = "long",paramType = "path")
    })
    @ApiResponses({
            @ApiResponse(code = 400, message = "BAD REQUEST",response = ExceptionResponse.class),
            @ApiResponse(code = 401, message = "UNAUTHORIZED", response = ExceptionResponse.class),
            @ApiResponse(code = 403, message = "FORBIDDEN", response = ExceptionResponse.class),
            @ApiResponse(code = 500, message = "INTERNAL_SERVER_ERROR", response = ExceptionResponse.class)
    })
    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId){

        Member member = getLoginMember();

        reviewService.deleteReview(reviewId,member);

        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "관리자 리뷰 삭제",notes = "관리자 리뷰 삭제")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization",value = "로그인 성공 후 access_token",required = true,
                    dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "reviewId",value = "리뷰 Id",required = true,
                    dataType = "long",paramType = "path")
    })
    @ApiResponses({
            @ApiResponse(code = 400, message = "BAD REQUEST",response = ExceptionResponse.class),
            @ApiResponse(code = 401, message = "UNAUTHORIZED", response = ExceptionResponse.class),
            @ApiResponse(code = 403, message = "FORBIDDEN", response = ExceptionResponse.class),
            @ApiResponse(code = 500, message = "INTERNAL_SERVER_ERROR", response = ExceptionResponse.class)
    })
    @DeleteMapping("/admin/reviews/{reviewId}")
    public ResponseEntity<Void> deleteReviewByAdmin(@PathVariable Long reviewId){

        reviewService.deleteReviewByAdmin(reviewId);

        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "족보의 리뷰 페이징 조회",notes = "족보의 리뷰 페이징 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization",value = "로그인 성공 후 access_token",required = true,
                    dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "itemId",value = "족보 Id",required = true,
                    dataType = "long", paramType = "path"),
            @ApiImplicitParam(name = "page",value = "페이지",required = true,
                    dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "size",value = "페이징 사이즈",required = true,
                    dataType = "int", paramType = "query")
    })
    @ApiResponses({
            @ApiResponse(code = 400, message = "BAD REQUEST",response = ExceptionResponse.class),
            @ApiResponse(code = 401, message = "UNAUTHORIZED", response = ExceptionResponse.class),
            @ApiResponse(code = 403, message = "FORBIDDEN", response = ExceptionResponse.class),
            @ApiResponse(code = 500, message = "INTERNAL_SERVER_ERROR", response = ExceptionResponse.class)
    })
    @GetMapping("/items/{itemId}/reviews")
    public ResponseEntity<Page<ReviewResponse>> findReviewPage(
            @PathVariable Long itemId,
            @PageableDefault(size = 15, sort = "id", direction = Sort.Direction.DESC)
            @ApiIgnore Pageable pageable){

        Page<ReviewResponse> results = reviewService.findReviewPageByItemId(itemId, pageable);
        return ResponseEntity.ok(results);
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
