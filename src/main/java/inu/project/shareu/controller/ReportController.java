package inu.project.shareu.controller;

import inu.project.shareu.config.security.LoginMember;
import inu.project.shareu.domain.Member;
import inu.project.shareu.model.request.report.ReportItemSaveRequest;
import inu.project.shareu.model.request.report.ReportReviewSaveRequest;
import inu.project.shareu.service.ReportService;
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

@Api(tags = "6. 신고")
@Slf4j
@RestController
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @ApiOperation(value = "족보 신고 등록",notes = "족보 신고 등록")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization",value = "로그인 성공 후 access_token",required = true
                    ,dataType = "String", paramType = "header")
    })
    @PostMapping("/reports/item/{itemId}")
    public ResponseEntity saveReportItem(@PathVariable Long itemId,
                                         @ModelAttribute ReportItemSaveRequest reportItemSaveRequest){

        Member member = getLoginMember();

        reportService.saveReportItem(member, itemId, reportItemSaveRequest);

        return ResponseEntity.ok().build();
    }


    @ApiOperation(value = "리뷰 신고 등록",notes = "리뷰 신고 등록")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization",value = "로그인 성공 후 access_token",required = true
                    ,dataType = "String", paramType = "header")
    })
    @PostMapping("/reports/review/{reviewId}")
    public ResponseEntity saveReportReview(@PathVariable Long reviewId,
                                           @ModelAttribute ReportReviewSaveRequest reportReviewSaveRequest){

        Member member = getLoginMember();

        reportService.saveReportReview(member,reviewId,reportReviewSaveRequest);

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
