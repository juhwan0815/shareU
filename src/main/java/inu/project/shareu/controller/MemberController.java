package inu.project.shareu.controller;

import inu.project.shareu.config.security.JwtTokenProvider;
import inu.project.shareu.config.security.LoginMember;
import inu.project.shareu.domain.Member;
import inu.project.shareu.model.request.member.MemberLoginDto;
import inu.project.shareu.model.request.member.MemberSaveDto;
import inu.project.shareu.model.request.member.MemberUpdateNameDto;
import inu.project.shareu.model.request.member.MemberUpdatePasswordDto;
import inu.project.shareu.model.response.member.MemberLoginResponseDto;
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

@Api(tags = {"1.회원"})
@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    @ApiOperation(value = "회원가입",notes = "회원 가입")
    @PostMapping("/members/signup")
    public ResponseEntity saveMember(@ModelAttribute MemberSaveDto memberSaveDto) {

        memberService.saveMember(memberSaveDto);

        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "회원 로그인",notes = "회원 로그인")
    @PostMapping("/members/signin")
    public ResponseEntity<MemberLoginResponseDto> loginMember(@ModelAttribute MemberLoginDto memberLoginDto) {

        Member loginMember = memberService.loginMember(memberLoginDto);

        List<String> roles = new ArrayList<>();
        loginMember.getRoles().forEach(role -> roles.add(role.getRoleName()));

        String token = "Bearer "+jwtTokenProvider.createToken(String.valueOf(loginMember.getId()), roles);

        MemberLoginResponseDto memberLoginResponseDto = new MemberLoginResponseDto(token);

        return ResponseEntity.ok(memberLoginResponseDto);
    }

    @ApiOperation(value = "비밀번호 변경",notes = "회원 비밀번호 변경")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization",value = "로그인 성공 후 access_token",required = true
                    ,dataType = "String", paramType = "header")
    })
    @PatchMapping("/members/change-password")
    public ResponseEntity changePassword(@ModelAttribute MemberUpdatePasswordDto memberUpdatePasswordDto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginMember loginMember = (LoginMember) authentication.getPrincipal();
        Long id = loginMember.getId();

        memberService.changePassword(id,memberUpdatePasswordDto);

        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "닉네임 변경",notes = "회원 닉네임 변경")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization",value = "로그인 성공 후 access_token",required = true
                    ,dataType = "String", paramType = "header")
    })
    @PatchMapping("/members/change-name")
    public ResponseEntity changeName(@ModelAttribute MemberUpdateNameDto memberUpdateNameDto){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginMember loginMember = (LoginMember) authentication.getPrincipal();
        Long id = loginMember.getId();

        memberService.changeName(id,memberUpdateNameDto);
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
        Long id = loginMember.getId();

        memberService.removeMember(id);

        // TODO 회원 탈퇴시 족보, 구매, 장바구니 등등 모든 엔티티 삭제?

        return ResponseEntity.ok().build();
    }
}

