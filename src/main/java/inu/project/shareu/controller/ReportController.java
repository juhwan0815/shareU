package inu.project.shareu.controller;

import inu.project.shareu.config.security.LoginMember;
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

@Api(tags = "9.신고")
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
    @PostMapping("/reports/item")
    public ResponseEntity saveReportItem(@ModelAttribute ReportItemSaveRequest reportItemSaveRequest){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginMember loginMember = (LoginMember) authentication.getPrincipal();
        Long memberId = loginMember.getId();

        reportService.saveReportItem(memberId,reportItemSaveRequest);

        return ResponseEntity.ok().build();
    }


    @ApiOperation(value = "리뷰 신고 등록",notes = "리뷰 신고 등록")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization",value = "로그인 성공 후 access_token",required = true
                    ,dataType = "String", paramType = "header")
    })
    @PostMapping("/reports/review")
    public ResponseEntity saveReportReview(@ModelAttribute ReportReviewSaveRequest reportReviewSaveRequest){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginMember loginMember = (LoginMember) authentication.getPrincipal();
        Long memberId = loginMember.getId();

        reportService.saveReportReview(memberId,reportReviewSaveRequest);

        return ResponseEntity.ok().build();
    }


    @ApiOperation(value = "신고 처리 완료",notes = "신고 처리 완료")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization",value = "로그인 성공 후 access_token",required = true
                    ,dataType = "String", paramType = "header")
    })
    @DeleteMapping("/reports/{reportId}")
    public ResponseEntity finishReport(@PathVariable Long reportId){

        reportService.finishReport(reportId);

        return ResponseEntity.ok().build();
    }
}
