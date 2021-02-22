package inu.project.shareu.controller;

import inu.project.shareu.config.security.LoginMember;
import inu.project.shareu.domain.Member;
import inu.project.shareu.model.common.response.ExceptionResponse;
import inu.project.shareu.model.report.request.ReportItemSaveRequest;
import inu.project.shareu.model.report.request.ReportReviewSaveRequest;
import inu.project.shareu.model.report.response.ReportResponse;
import inu.project.shareu.service.ReportService;
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

@Api(tags = "7.신고")
@Slf4j
@RestController
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @ApiOperation(value = "족보 신고 등록",notes = "족보 신고 등록")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization",value = "로그인 성공 후 access_token",required = true,
                    dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "itemId",value = "족보 Id",required = true,
                    dataType = "long", paramType = "path")
    })
    @ApiResponses({
            @ApiResponse(code = 400, message = "BAD REQUEST",response = ExceptionResponse.class),
            @ApiResponse(code = 401, message = "UNAUTHORIZED", response = ExceptionResponse.class),
            @ApiResponse(code = 403, message = "FORBIDDEN", response = ExceptionResponse.class),
            @ApiResponse(code = 500, message = "INTERNAL_SERVER_ERROR", response = ExceptionResponse.class)
    })
    @PostMapping("/reports/item/{itemId}")
    public ResponseEntity<Void> saveReportItem(@PathVariable Long itemId,
                                               @ModelAttribute ReportItemSaveRequest reportItemSaveRequest){

        Member member = getLoginMember();

        reportService.saveReportItem(member, itemId, reportItemSaveRequest);

        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "리뷰 신고 등록",notes = "리뷰 신고 등록")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization",value = "로그인 성공 후 access_token",required = true,
                    dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "reviewId",value = "리뷰 Id",required = true,
                    dataType = "long", paramType = "path")
    })
    @ApiResponses({
            @ApiResponse(code = 400, message = "BAD REQUEST",response = ExceptionResponse.class),
            @ApiResponse(code = 401, message = "UNAUTHORIZED", response = ExceptionResponse.class),
            @ApiResponse(code = 403, message = "FORBIDDEN", response = ExceptionResponse.class),
            @ApiResponse(code = 500, message = "INTERNAL_SERVER_ERROR", response = ExceptionResponse.class)
    })
    @PostMapping("/reports/review/{reviewId}")
    public ResponseEntity<Void> saveReportReview(@PathVariable Long reviewId,
                                                 @ModelAttribute ReportReviewSaveRequest reportReviewSaveRequest){

        Member member = getLoginMember();

        reportService.saveReportReview(member,reviewId,reportReviewSaveRequest);

        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "(관리자) 신고 처리 완료",notes = "(관리자) 신고 처리 완료")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization",value = "로그인 성공 후 access_token",required = true,
                    dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "reporyId",value = "신고 Id",required = true,
                    dataType = "long",paramType = "path")
    })
    @ApiResponses({
            @ApiResponse(code = 400, message = "BAD REQUEST",response = ExceptionResponse.class),
            @ApiResponse(code = 401, message = "UNAUTHORIZED", response = ExceptionResponse.class),
            @ApiResponse(code = 403, message = "FORBIDDEN", response = ExceptionResponse.class),
            @ApiResponse(code = 500, message = "INTERNAL_SERVER_ERROR", response = ExceptionResponse.class)
    })
    @DeleteMapping("/admin/reports/{reportId}")
    public ResponseEntity<Void> deleteReport(@PathVariable Long reportId){

        reportService.deleteReport(reportId);

        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "(관리자) 리뷰 페이징 조회 ",notes = "(관리자) 리뷰 페이징 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization",value = "로그인 성공 후 access_token",required = true,
                    dataType = "String", paramType = "header"),
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
    @GetMapping("/admin/reports")
    public ResponseEntity<Page<ReportResponse>> findPage(
            @PageableDefault(size = 15, sort = "id", direction = Sort.Direction.DESC)
            @ApiIgnore Pageable pageable){

        return ResponseEntity.ok(reportService.findPage(pageable));
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
