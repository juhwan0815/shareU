package inu.project.shareu.service;

import inu.project.shareu.advice.exception.MemberException;
import inu.project.shareu.config.security.LoginMember;
import inu.project.shareu.domain.Member;
import inu.project.shareu.domain.MemberStatus;
import inu.project.shareu.domain.Role;
import inu.project.shareu.model.request.member.MemberLoginRequest;
import inu.project.shareu.model.request.member.MemberSaveRequest;
import inu.project.shareu.model.request.member.MemberUpdateNameRequest;
import inu.project.shareu.model.request.member.MemberUpdatePasswordRequest;
import inu.project.shareu.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void saveMember(MemberSaveRequest memberSaveRequest){

        if(memberRepository.findByStudentNumber(memberSaveRequest.getStudentNumber()).isPresent()){
            throw new MemberException("이미 가입한 학번입니다.");
        }

        if(memberRepository.findByName(memberSaveRequest.getName()).isPresent()){
            throw new MemberException("이미 존재하는 닉네임입니다.");
        }

        Member member = Member.createMember(memberSaveRequest.getStudentNumber(),
                passwordEncoder.encode(memberSaveRequest.getPassword()),
                memberSaveRequest.getName());

        Role role = Role.createRole();
        role.giveRoleToMember(member);

        memberRepository.save(member);
    }

    public Member loginMember(MemberLoginRequest memberLoginRequest) {
        Member findMember = memberRepository.findWithRoleByStudentNumber(
                memberLoginRequest.getStudentNumber())
                .orElseThrow(() -> new MemberException("존재하지 않는 회원입니다."));

        if(findMember.getMemberStatus().equals(MemberStatus.BLOCK)){
            throw new MemberException("차단된 사용자입니다.");
        }

        if(!passwordEncoder.matches(memberLoginRequest.getPassword(),findMember.getPassword())){
            throw new MemberException("비밀번호가 일치하지 않습니다.");
        }

        return findMember;
    }

    @Transactional
    public void changePassword(Long memberId, MemberUpdatePasswordRequest memberUpdatePasswordrequest) {
        Member findMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException("존재하지 않는 회원입니다."));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginMember loginMember= (LoginMember) authentication.getPrincipal();

        if(!findMember.getName().equals(loginMember.getUsername())){
            throw new MemberException("다른 회원의 정보를 변경할 수 없습니다.");
        }

        if(!passwordEncoder.matches(memberUpdatePasswordrequest.getCurrentPassword(), findMember.getPassword())){
            throw new MemberException("비밀번호가 일치하지 않습니다.");
        }

        if(!memberUpdatePasswordrequest.getChangePassword1().equals(memberUpdatePasswordrequest.getChangePassword2())){
            throw new MemberException("변경될 패스워드가 일치하지 않습니다.");
        }

        findMember.changePassword(passwordEncoder.encode(memberUpdatePasswordrequest.getChangePassword1()));
    }


    @Transactional
    public void changeName(Long memberId, MemberUpdateNameRequest memberUpdateNameRequest) {
        Member findMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException("존재하지 않는 회원입니다."));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginMember loginMember  = (LoginMember) authentication.getPrincipal();

        if(!findMember.getName().equals(loginMember.getUsername())){
            throw new MemberException("다른 회원의 정보를 변경할 수 없습니다.");
        }

        if(memberRepository.findByName(memberUpdateNameRequest.getName()).isPresent()){
            throw new MemberException("이미 존재하는 닉네임입니다.");
        }

        findMember.changeName(memberUpdateNameRequest.getName());
    }


    @Transactional
    public void removeMember(Long memberId) {
        Member findMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException("존재하지 않는 회원입니다."));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginMember loginMember = (LoginMember) authentication.getPrincipal();

        if(!findMember.getName().equals(loginMember.getUsername())){
            throw new MemberException("다른 회원을 탈퇴할 수 없습니다.");
        }

        memberRepository.delete(findMember);
    }
}
