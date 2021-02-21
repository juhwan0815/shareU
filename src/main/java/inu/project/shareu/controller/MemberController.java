package inu.project.shareu.controller;

import inu.project.shareu.config.security.JwtTokenProvider;
import inu.project.shareu.config.security.LoginMember;
import inu.project.shareu.domain.Member;
import inu.project.shareu.model.request.member.MemberLoginRequest;
import inu.project.shareu.model.request.member.MemberSaveRequest;
import inu.project.shareu.model.request.member.MemberUpdateRequest;
import inu.project.shareu.model.response.common.SuccessResponse;
import inu.project.shareu.model.response.member.MemberLoginResponse;
import inu.project.shareu.model.response.member.MemberResponse;
import inu.project.shareu.service.BadWordService;
import inu.project.shareu.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.Response;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@Api(tags = {"1.회원"})
@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final BadWordService badWordService;
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    @ApiOperation(value = "회원가입",notes = "회원 가입")
    @PostMapping("/members")
    public ResponseEntity saveMember(@ModelAttribute MemberSaveRequest memberSaveRequest) {

        badWordService.validateItemForbiddenWord(memberSaveRequest.getName());
        memberService.saveMember(memberSaveRequest);

        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "회원 로그인",notes = "회원 로그인")
    @PostMapping("/members/login")
    public ResponseEntity loginMember(@ModelAttribute MemberLoginRequest memberLoginRequest) {

        Member loginMember = memberService.loginMember(memberLoginRequest);

        String token = "Bearer "+ jwtTokenProvider.createToken(String.valueOf(loginMember.getId()));

        return ResponseEntity.ok(new SuccessResponse<>(token));
    }


    @ApiOperation(value = "회원 수정",notes = "회원 수정")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization",value = "로그인 성공 후 access_token",required = true
                    ,dataType = "String", paramType = "header")
    })
    @PatchMapping("/members")
    public ResponseEntity updateMember(@ModelAttribute MemberUpdateRequest memberUpdateRequest){

        Member loginMember = getLoginMember();

        badWordService.validateItemForbiddenWord(memberUpdateRequest.getName());
        memberService.updateMember(loginMember,memberUpdateRequest);

        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "회원 탈퇴",notes = "회원 탈퇴")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization",value = "로그인 성공 후 access_token",required = true
                    ,dataType = "String", paramType = "header")
    })
    @DeleteMapping("/members")
    public ResponseEntity removeMember(){

        Member loginMember = getLoginMember();
        memberService.removeMember(loginMember);

        // TODO 회원 탈퇴시 족보, 구매, 장바구니 등등 모든 엔티티 삭제?

        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "회원 정보 조회",notes = "회원 정보 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization",value = "로그인 성공 후 access_token",required = true
                    ,dataType = "String", paramType = "header")
    })
    @GetMapping("/members")
    public ResponseEntity findLoginMember(){

        Member loginMember = getLoginMember();

        return ResponseEntity.ok(new MemberResponse(loginMember));
    }

    @ApiOperation(value = "(관리자) 차단 회원 조회",notes = "(관리자) 차단 회원 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization",value = "로그인 성공 후 access_token",required = true
                    ,dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "page",value = "페이지",required = true
                    ,dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "size",value = "페이징 사이즈",required = true
                    ,dataType = "int", paramType = "query")
    })
    @GetMapping("/admin/members")
    public ResponseEntity findBlockMember(
            @PageableDefault(size = 15,sort = "id",direction = Sort.Direction.DESC)
            @ApiIgnore Pageable pageable){
        return ResponseEntity.ok(memberService.findBlockMembers(pageable));
    }

    @ApiOperation(value = "관리자 회원 차단 해제",notes = "관리자 회원 차단 해제")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization",value = "로그인 성공 후 access_token",required = true
                    ,dataType = "String", paramType = "header")
    })
    @PatchMapping("/admin/members/{memberId}")
    public ResponseEntity changeMemberStatus(@PathVariable Long memberId){

        memberService.changeMemberStatus(memberId);

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

