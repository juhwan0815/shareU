package inu.project.shareu.controller;

import inu.project.shareu.config.security.JwtTokenProvider;
import inu.project.shareu.config.security.LoginMember;
import inu.project.shareu.domain.Member;
import inu.project.shareu.model.request.member.MemberLoginRequest;
import inu.project.shareu.model.request.member.MemberSaveRequest;
import inu.project.shareu.model.request.member.MemberUpdateNameRequest;
import inu.project.shareu.model.request.member.MemberUpdatePasswordRequest;
import inu.project.shareu.model.response.member.MemberLoginResponse;
import inu.project.shareu.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Api(tags = {"1.회원"})
@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    @ApiOperation(value = "회원가입",notes = "회원 가입")
    @PostMapping("/members/signup")
    public ResponseEntity saveMember(@ModelAttribute MemberSaveRequest memberSaveRequest) {

        memberService.saveMember(memberSaveRequest);

        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "회원 로그인",notes = "회원 로그인")
    @PostMapping("/members/signin")
    public ResponseEntity<MemberLoginResponse> loginMember(@ModelAttribute MemberLoginRequest memberLoginRequest) {

        Member loginMember = memberService.loginMember(memberLoginRequest);

        List<String> roles = new ArrayList<>();
        loginMember.getRoles().forEach(role -> roles.add(role.getRoleName()));

        String token = "Bearer "+jwtTokenProvider.createToken(String.valueOf(loginMember.getId()), roles);

        MemberLoginResponse memberLoginResponse = new MemberLoginResponse(token);

        return ResponseEntity.ok(memberLoginResponse);
    }

    @ApiOperation(value = "비밀번호 변경",notes = "회원 비밀번호 변경")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization",value = "로그인 성공 후 access_token",required = true
                    ,dataType = "String", paramType = "header")
    })
    @PatchMapping("/members/change-password")
    public ResponseEntity changePassword(@ModelAttribute MemberUpdatePasswordRequest memberUpdatePasswordRequest) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginMember loginMember = (LoginMember) authentication.getPrincipal();
        Long memberId = loginMember.getId();

        memberService.changePassword(memberId,memberUpdatePasswordRequest);

        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "닉네임 변경",notes = "회원 닉네임 변경")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization",value = "로그인 성공 후 access_token",required = true
                    ,dataType = "String", paramType = "header")
    })
    @PatchMapping("/members/change-name")
    public ResponseEntity changeName(@ModelAttribute MemberUpdateNameRequest memberUpdateNameRequest){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginMember loginMember = (LoginMember) authentication.getPrincipal();
        Long memberId = loginMember.getId();

        memberService.changeName(memberId,memberUpdateNameRequest);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "회원 탈퇴",notes = "회원 탈퇴")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization",value = "로그인 성공 후 access_token",required = true
                    ,dataType = "String", paramType = "header")
    })
    @DeleteMapping("/members")
    public ResponseEntity removeMember(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginMember loginMember = (LoginMember) authentication.getPrincipal();
        Long memberId = loginMember.getId();

        memberService.removeMember(memberId);

        // TODO 회원 탈퇴시 족보, 구매, 장바구니 등등 모든 엔티티 삭제?

        return ResponseEntity.ok().build();
    }

    // TODO 차단 회원

    @ApiOperation(value = "회원 차단 해제",notes = "회원 차단 해제")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization",value = "로그인 성공 후 access_token",required = true
                    ,dataType = "String", paramType = "header")
    })
    @PatchMapping("/members/{memberId}")
    public ResponseEntity changeMemberStatus(@PathVariable Long memberId){

       memberService.changeMemberStatus(memberId);

        return ResponseEntity.ok().build();
    }
}

