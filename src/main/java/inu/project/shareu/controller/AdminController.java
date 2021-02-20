package inu.project.shareu.controller;

import inu.project.shareu.advice.exception.StoreException;
import inu.project.shareu.config.security.LoginMember;
import inu.project.shareu.domain.Item;
import inu.project.shareu.domain.Member;
import inu.project.shareu.domain.Store;
import inu.project.shareu.model.common.store.StoreDto;
import inu.project.shareu.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Api(tags = "관리자")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final ItemService itemService;
    private final ReviewService reviewService;
    private final MemberService memberService;
    private final StoreService storeService;
    private final ReportService reportService;

    @ApiOperation(value = "관리자 족보 삭제",notes = "관리자 족보 삭제")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization",value = "로그인 성공 후 access_token",required = true
                    ,dataType = "String", paramType = "header")
    })
    @DeleteMapping("/items/{itemId}")
    public ResponseEntity deleteItemByAdmin(@PathVariable Long itemId){

        Item item = itemService.deleteItemByAdmin(itemId);
        storeService.deleteStores(item);

        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "관리자 리뷰 삭제",notes = "관리자 리뷰 삭제")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization",value = "로그인 성공 후 access_token",required = true
                    ,dataType = "String", paramType = "header")
    })
    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity deleteReviewByAdmin(@PathVariable Long reviewId){

        reviewService.deleteReviewByAdmin(reviewId);

        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "관리자 회원 차단 해제",notes = "관리자 회원 차단 해제")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization",value = "로그인 성공 후 access_token",required = true
                    ,dataType = "String", paramType = "header")
    })
    @PatchMapping("/members/{memberId}")
    public ResponseEntity changeMemberStatus(@PathVariable Long memberId){

        memberService.changeMemberStatus(memberId);

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

    @ApiOperation(value = "파일 다운",notes = "파일 다운")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization",value = "로그인 성공 후 access_token",required = true
                    ,dataType = "String", paramType = "header")
    })
    @GetMapping("/store")
    public ResponseEntity<ByteArrayResource> getResourcePathByAdmin(@RequestParam("storeName") String storeName){


        StoreDto storeDto = storeService.downloadFileByAdmin(storeName);
        Store store = storeDto.getStore();
        byte[] data = storeDto.getData();

        String fileOriginalName = store.getFileOriginalName();
        String encordedFilename = getEncodedFileOriginalName(fileOriginalName);

        ByteArrayResource resource = new ByteArrayResource(data);

        return ResponseEntity
                .ok()
                .contentLength(data.length)
                .header("Content-type","application/octet-stream")
                .header("Content-disposition","attachment; filename=\"" + encordedFilename + "\"")
                .body(resource);
    }

    /**
     * 파일명 변경 (UTF8)
     */
    private String getEncodedFileOriginalName(String fileOriginalName) {
        String encordedFilename;
        try {
            encordedFilename = URLEncoder.encode(fileOriginalName,"UTF-8").replace("+", "%20");
        } catch (UnsupportedEncodingException e) {
            throw new StoreException("파일명 변경 중 오류");
        }
        return encordedFilename;
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
