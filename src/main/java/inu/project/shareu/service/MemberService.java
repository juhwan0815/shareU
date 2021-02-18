package inu.project.shareu.service;

import inu.project.shareu.advice.exception.MemberException;
import inu.project.shareu.domain.Member;
import inu.project.shareu.domain.MemberStatus;
import inu.project.shareu.domain.Role;
import inu.project.shareu.model.request.member.MemberLoginRequest;
import inu.project.shareu.model.request.member.MemberSaveRequest;
import inu.project.shareu.repository.MemberRepository;
import inu.project.shareu.repository.query.MemberQueryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원 저장
     * 1. 비밀번호 일치 여부 확인
     * 2. 이미 가입한 학번이나 이름이 있는지 확인
     * 3. 회원과 역할 생성 및 저장
     */
    @Transactional
    public void saveMember(MemberSaveRequest memberSaveRequest) {

        validatePasswordSame(memberSaveRequest.getPassword1(),memberSaveRequest.getPassword2());
        validateDuplicateMember(memberSaveRequest.getStudentNumber(),memberSaveRequest.getName());

        Member member = Member.createMember(memberSaveRequest.getStudentNumber(),
                                            passwordEncoder.encode(memberSaveRequest.getPassword1()),
                                            memberSaveRequest.getName());

        Role role = Role.createRole();
        role.giveRoleToMember(member);

        memberRepository.save(member);
    }


    /**
     * 회원 로그인
     * 1. 학번을 통한 회원 조회
     * 2. 차단 회원 확인
     * 3. 비밀번호 일치 여부 확인
     * @Retrun Member
     */
    public Member loginMember(MemberLoginRequest memberLoginRequest) {

        Member findMember = memberRepository.findWithRoleByStudentNumber(memberLoginRequest.getStudentNumber())
                .orElseThrow(() -> new MemberException("존재하지 않는 회원입니다."));

        validateActivityMember(findMember);

        validateMemberPasswordSame(memberLoginRequest.getPassword(),findMember.getPassword());

        return findMember;
    }


    /**
     * 회원 탈퇴
     * 1. 회원 탈퇴
     */
    @Transactional
    public void removeMember(Member member) {
        memberRepository.delete(member);
        // TODO 회원 탈퇴시, 포인트, 장바구니, 구매내역, 게시글, 리뷰 모두 삭제
    }

    /**
     * 회원 차단 해제
     * 1. 회원 조회
     * 2. 차단 여부 확인
     * 3. 회원 상태 변경
     */
    @Transactional
    public void changeMemberStatus(Long memberId) {

        Member findMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException("존재하지 않는 회원입니다."));

        validateBlockMember(findMember);

        findMember.changeMemberStatus();
    }

    /**
     * 이미 가입한 학번 or 별명 존재 여부 확인
     */
    private void validateDuplicateMember(int studentNumber,String name) {
        List<Member> findMembers = memberRepository.findByStudentNumberOrName(studentNumber,name);

        findMembers.forEach(member -> {
            if (studentNumber == member.getStudentNumber()) {
                throw new MemberException("이미 가입한 학번입니다.");
            }
            if (name.equals(member.getName())) {
                throw new MemberException("이미 존재하는 별명입니다.");
            }
        });
    }

    /**
     * 회원 가입 요청의 비밀번호 동일 여부 확인
     */
    private void validatePasswordSame(String password1, String password2) {
        if (!password1.equals(password2)) {
            throw new MemberException("패스워드가 일치하지 않습니다.");
        }
    }

    /**
     * 회원 로그인 요청의 비밀번호 동일 여부 확인
     */
    private void validateMemberPasswordSame(String loginPassword, String memberPassword) {
        if (!passwordEncoder.matches(loginPassword, memberPassword)) {
            throw new MemberException("비밀번호가 일치하지 않습니다.");
        }
    }

    /**
     * 회원 활성화 여부 확인
     */
    private void validateActivityMember(Member member) {
        if (!member.getMemberStatus().equals(MemberStatus.ACTIVITY)) {
            throw new MemberException("차단된 회원입니다.");
        }
    }

    /**
     * 회원 차단 여부 확인
     */
    private void validateBlockMember(Member member) {
        if (!member.getMemberStatus().equals(MemberStatus.BLOCK)) {
            throw new MemberException("차단되지 않은 회원입니다.");
        }
    }
}
