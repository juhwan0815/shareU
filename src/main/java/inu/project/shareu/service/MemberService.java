package inu.project.shareu.service;

import com.amazonaws.services.s3.AmazonS3Client;
import inu.project.shareu.advice.exception.MemberException;
import inu.project.shareu.domain.*;
import inu.project.shareu.model.member.request.MemberLoginRequest;
import inu.project.shareu.model.member.request.MemberSaveRequest;
import inu.project.shareu.model.member.request.MemberUpdateRequest;
import inu.project.shareu.model.member.response.MemberBlockResponse;
import inu.project.shareu.repository.*;
import inu.project.shareu.repository.query.MemberQueryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberQueryRepository memberQueryRepository;

    private final AmazonS3Client amazonS3Client;


    private final ItemRepository itemRepository;
    private final ReviewRepository reviewRepository;
    private final StoreRepository storeRepository;
    private final PointRepository pointRepository;
    private final ReportRepository reportRepository;
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;

    /**
     * 매일 0 시에 족보 등록 가능 회수 초기화
     */
    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
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

            findMember.changePassword(passwordEncoder.encode(memberUpdateRequest.getChangePassword1()));
            findMember.changeName(memberUpdateRequest.getName());

        }else {

            validateMemberPasswordSame(memberUpdateRequest.getCurrentPassword(),
                                       findMember.getPassword());

            findMember.changeName(memberUpdateRequest.getName());

        }
    }

    /**
     * 회원 탈퇴
     * 1. 회원 조회 및 탈퇴로 회원상태 변경
     * 2. 회원 포인트 이력 조히 및 삭제
     * 3. 회원 신고 이력 조회 및 삭제
     * 4. 회원 리뷰 조회 및 리뷰 상태 변경 -> 리뷰 상품 추천 수 변경
     * 5. 회원 장바구니 조회 및 삭제
     * 6. 회원 구매 이력 조회 및 삭제
     * 7. 회원 족보 조회 및 상품 상태 변경 -> 족보의 파일 모두 삭제
     */
    @Transactional
    public void removeMember(Member member) {

        Member findMember = memberRepository.findById(member.getId())
                .orElseThrow(() -> new MemberException("존재하지 않는 회원입니다."));
        findMember.removeMember();

        List<Point> points = pointRepository.findByMember(findMember);
        points.stream().forEach(point -> pointRepository.delete(point));

        List<Report> reports = reportRepository.findByMember(findMember);
        reports.stream().forEach(report -> reportRepository.delete(report));

        List<Review> reviews = reviewRepository.findWithItemByMember(findMember);
        reviews.stream().forEach(review -> {
            review.changeStatus();
        });

        List<Cart> carts = cartRepository.findByMember(findMember);
        carts.stream().forEach(cart -> cartRepository.delete(cart));

        List<Order> orders = orderRepository.findByMember(findMember);
        orders.stream().forEach(order -> orderRepository.delete(order));

        List<Item> items = itemRepository.findWithStoreByMember(findMember);
        items.stream().forEach(item -> {
            item.getStoreList().stream().forEach(store -> {
                if(amazonS3Client.doesObjectExist(bucket,store.getFileStoreName())){
                    amazonS3Client.deleteObject(bucket,store.getFileOriginalName());
                }
                storeRepository.delete(store);
            });
            item.deleteItem();
        });
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
            throw new MemberException("변경할 비밀번호가 일치하지 않습니다.");
        }
    }

    /**
     * 회원 로그인 요청의 비밀번호 동일 여부 확인
     */
    private void validateMemberPasswordSame(String loginPassword, String memberPassword) {
        if (!passwordEncoder.matches(loginPassword, memberPassword)) {
            throw new MemberException("현재 비밀번호가 일치하지 않습니다.");
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
