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

@Api(tags = {"1. 회원"})
@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    @ApiOperation(value = "회원가입",notes = "회원 가입")
    @PostMapping("/members")
    public ResponseEntity saveMember(@ModelAttribute MemberSaveRequest memberSaveRequest) {

        memberService.saveMember(memberSaveRequest);

        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "회원 로그인",notes = "회원 로그인")
    @PostMapping("/members/login")
    public ResponseEntity<MemberLoginResponse> loginMember(@ModelAttribute MemberLoginRequest memberLoginRequest) {

        Member loginMember = memberService.loginMember(memberLoginRequest);

        String token = "Bearer "+ jwtTokenProvider.createToken(String.valueOf(loginMember.getId()));
        MemberLoginResponse memberLoginResponse = new MemberLoginResponse(token);

        return ResponseEntity.ok(memberLoginResponse);
    }

    // TODO 회원 수정

    @ApiOperation(value = "회원 탈퇴",notes = "회원 탈퇴")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization",value = "로그인 성공 후 access_token",required = true
                    ,dataType = "String", paramType = "header")
    })
    @DeleteMapping("/members")
    public ResponseEntity removeMember(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginMember loginMember = (LoginMember) authentication.getPrincipal();
        Member member = loginMember.getMember();

        memberService.removeMember(member);

        // TODO 회원 탈퇴시 족보, 구매, 장바구니 등등 모든 엔티티 삭제?

        return ResponseEntity.ok().build();
    }

}

