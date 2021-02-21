package inu.project.shareu.service;

import inu.project.shareu.advice.exception.MemberException;
import inu.project.shareu.domain.Member;
import inu.project.shareu.domain.MemberStatus;
import inu.project.shareu.domain.Role;
import inu.project.shareu.model.request.member.MemberLoginRequest;
import inu.project.shareu.model.request.member.MemberSaveRequest;
import inu.project.shareu.model.request.member.MemberUpdateRequest;
import inu.project.shareu.model.response.member.MemberBlockResponse;
import inu.project.shareu.model.response.member.MemberResponse;
import inu.project.shareu.repository.MemberRepository;
import inu.project.shareu.repository.query.MemberQueryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.awt.color.CMMException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberQueryRepository memberQueryRepository;

    /**
     * 매일 0 시에 족보 등록 가능 회수 초기화
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void updateMemberPossibleCount(){
        long count = memberQueryRepository.updateMemberPossibleCount();
        log.info("{}명의 족보 등록 가능 회수를 업데이트 했습니다.",count);
    }


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
     * 회원 정보 수정
     * 1. 회원 조회
     * 2. 회원 수정 요청에 변경할 비밀번호가 있는지 확인
     * -> 회원 수정 요청에 변경할 비밀번호가 있다면 비밀번호 동일여부 및 현재 비밀번호 확인 후 비밀번호,이름 변경
     * -> 회원 수정 요청에 변경할 비밀번호가 없다면 이름만 변경
     */
    @Transactional
    public void updateMember(Member loginMember,MemberUpdateRequest memberUpdateRequest) {

        Member findMember = memberRepository.findById(loginMember.getId())
                .orElseThrow(() -> new MemberException("존재하지 않는 회원입니다."));

        if(checkExistUpdatePassword(memberUpdateRequest)){

            validatePasswordSame(memberUpdateRequest.getChangePassword1(),
                                 memberUpdateRequest.getChangePassword2());

            validateMemberPasswordSame(memberUpdateRequest.getCurrentPassword(),
                                       findMember.getPassword());

            findMember.changePassword(memberUpdateRequest.getChangePassword1());
            findMember.changeName(memberUpdateRequest.getName());

        }else {

            validateMemberPasswordSame(memberUpdateRequest.getCurrentPassword(),
                                       findMember.getPassword());

            findMember.changeName(memberUpdateRequest.getName());

        }


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
     * 차단된 회원 페이징 조회
     * 1. 차단된 회원 조회
     * 2. DTO로 변환하여 반환
     * @Return Page<MemberBlockResponse>
     */
    public Page<MemberBlockResponse> findBlockMembers(Pageable pageable) {
        Page<Member> blockMembers = memberRepository.findPageByMemberStatus(MemberStatus.BLOCK, pageable);
        return blockMembers.map(member -> new MemberBlockResponse(member));
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


    /**
     * 회원 수정 요청에 변경할 비밀번호 존재 여부 확인
     */
    private boolean checkExistUpdatePassword(MemberUpdateRequest memberUpdateRequest) {
        if(StringUtils.hasText(memberUpdateRequest.getChangePassword1())
                && StringUtils.hasText(memberUpdateRequest.getChangePassword2())){
            return true;
        }
        return false;
    }

}
