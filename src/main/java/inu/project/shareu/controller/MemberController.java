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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/members/signup")
    public ResponseEntity saveMember(@ModelAttribute MemberSaveDto memberSaveDto) {

        memberService.saveMember(memberSaveDto);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/members/signin")
    public ResponseEntity<MemberLoginResponseDto> loginMember(@ModelAttribute MemberLoginDto memberLoginDto) {

        Member loginMember = memberService.loginMember(memberLoginDto);

        List<String> roles = new ArrayList<>();
        loginMember.getRoles().forEach(role -> roles.add(role.getRoleName()));

        String token = jwtTokenProvider.createToken(String.valueOf(loginMember.getId()), roles);

        MemberLoginResponseDto memberLoginResponseDto = new MemberLoginResponseDto(token);

        return ResponseEntity.ok(memberLoginResponseDto);
    }

    @PatchMapping("/members/changepassword")
    public ResponseEntity changePassword(@ModelAttribute MemberUpdatePasswordDto memberUpdatePasswordDto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginMember loginMember = (LoginMember) authentication.getPrincipal();
        Long id = loginMember.getId();

        memberService.changePassword(id,memberUpdatePasswordDto);

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/members/changename")
    public ResponseEntity changeName(@ModelAttribute MemberUpdateNameDto memberUpdateNameDto){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginMember loginMember = (LoginMember) authentication.getPrincipal();
        Long id = loginMember.getId();

        memberService.changeName(id,memberUpdateNameDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/members")
    public ResponseEntity removeMember(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginMember loginMember = (LoginMember) authentication.getPrincipal();
        Long id = loginMember.getId();

        memberService.removeMember(id);

        return ResponseEntity.ok().build();
    }
}

