package inu.project.shareu.service;

import inu.project.shareu.advice.exception.MemberException;
import inu.project.shareu.config.security.JwtTokenProvider;
import inu.project.shareu.domain.Member;
import inu.project.shareu.domain.MemberStatus;
import inu.project.shareu.domain.Role;
import inu.project.shareu.model.request.member.MemberLoginDto;
import inu.project.shareu.model.request.member.MemberSaveDto;
import inu.project.shareu.model.request.member.MemberUpdateNameDto;
import inu.project.shareu.model.request.member.MemberUpdatePasswordDto;
import inu.project.shareu.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void saveMember(MemberSaveDto memberSaveDto){

        if(memberRepository.findByStudentNumber(memberSaveDto.getStudentNumber()).isPresent()){
            throw new MemberException("이미 가입한 학번입니다.");
        }

        if(memberRepository.findByName(memberSaveDto.getName()).isPresent()){
            throw new MemberException("이미 존재하는 닉네임입니다.");
        }


        Member member = Member.createMember(memberSaveDto.getStudentNumber(),
                passwordEncoder.encode(memberSaveDto.getPassword()),
                memberSaveDto.getName());

        Role role = Role.createRole();
        role.giveRoleToMember(member);

        memberRepository.save(member);
    }

    public Member loginMember(MemberLoginDto memberLoginDto) {
        Member findMember = memberRepository.findWithRoleByStudentNumber(
                memberLoginDto.getStudentNumber())
                .orElseThrow(() -> new MemberException("존재하지 않는 회원입니다."));

        if(findMember.getMemberStatus().equals(MemberStatus.BLOCK)){
            throw new MemberException("차단된 사용자입니다.");
        }

        if(!passwordEncoder.matches(memberLoginDto.getPassword(),findMember.getPassword())){
            throw new MemberException("비밀번호가 일치하지 않습니다.");
        }

        return findMember;
    }

    @Transactional
    public void changePassword(Long id, MemberUpdatePasswordDto memberUpdatePasswordDto) {
        Member findMember = memberRepository.findById(id)
                .orElseThrow(() -> new MemberException("존재하지 않는 회원입니다."));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        if(!findMember.getName().equals(user.getUsername())){
            throw new MemberException("다른 회원의 정보를 변경할 수 없습니다.");
        }

        if(!passwordEncoder.matches(memberUpdatePasswordDto.getCurrentPassword(), findMember.getPassword())){
            throw new MemberException("비밀번호가 일치하지 않습니다.");
        }

        if(!memberUpdatePasswordDto.getChangePassword1().equals(memberUpdatePasswordDto.getChangePassword2())){
            throw new MemberException("변경될 패스워드가 일치하지 않습니다.");
        }

        findMember.changePassword(passwordEncoder.encode(memberUpdatePasswordDto.getChangePassword1()));
    }


    @Transactional
    public void changeName(Long id, MemberUpdateNameDto memberUpdateNameDto) {
        Member findMember = memberRepository.findById(id)
                .orElseThrow(() -> new MemberException("존재하지 않는 회원입니다."));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        if(!findMember.getName().equals(user.getUsername())){
            throw new MemberException("다른 회원의 정보를 변경할 수 없습니다.");
        }

        if(memberRepository.findByName(memberUpdateNameDto.getName()).isPresent()){
            throw new MemberException("이미 존재하는 닉네임입니다.");
        }

        findMember.changeName(memberUpdateNameDto.getName());
    }


    @Transactional
    public void removeMember(Long id) {
        Member findMember = memberRepository.findById(id)
                .orElseThrow(() -> new MemberException("존재하지 않는 회원입니다."));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        if(!findMember.getName().equals(user.getUsername())){
            throw new MemberException("다른 회원을 탈퇴할 수 없습니다.");
        }

        memberRepository.delete(findMember);
    }
}
