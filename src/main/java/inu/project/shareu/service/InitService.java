package inu.project.shareu.service;

import inu.project.shareu.domain.College;
import inu.project.shareu.domain.CollegeStatus;
import inu.project.shareu.domain.Member;
import inu.project.shareu.domain.Role;
import inu.project.shareu.repository.CollegeRepository;
import inu.project.shareu.repository.MemberRepository;
import inu.project.shareu.repository.query.MemberQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class InitService {

    private final CollegeRepository collegeRepository;
    private final MemberRepository memberRepository;
    private final MemberQueryRepository memberQueryRepository;
    private final PasswordEncoder passwordEncoder;

    public void initCollege() {
        List<College> colleges = collegeRepository.findAll();

        if(colleges.isEmpty()){
            College college1 = College.createCollege("인문대학", CollegeStatus.전공);
            collegeRepository.save(college1);

            College college2 = College.createCollege("자연과학대학", CollegeStatus.전공);
            collegeRepository.save(college2);

            College college3 = College.createCollege("사회과학대학", CollegeStatus.전공);
            collegeRepository.save(college3);

            College college4 = College.createCollege("글로벌법정경대학", CollegeStatus.전공);
            collegeRepository.save(college4);

            College college5 = College.createCollege("공과대학", CollegeStatus.전공);
            collegeRepository.save(college5);

            College college6 = College.createCollege("정보기술대학", CollegeStatus.전공);
            collegeRepository.save(college6);

            College college7 = College.createCollege("경영대학", CollegeStatus.전공);
            collegeRepository.save(college7);

            College college8 = College.createCollege("예술체육대학", CollegeStatus.전공);
            collegeRepository.save(college8);

            College college9 = College.createCollege("사범대학", CollegeStatus.전공);
            collegeRepository.save(college9);

            College college10 = College.createCollege("도시과학대학", CollegeStatus.전공);
            collegeRepository.save(college10);

            College college11 = College.createCollege("동북아국제통상대학", CollegeStatus.전공);
            collegeRepository.save(college11);

            College college12 = College.createCollege("생명과학기술대학",CollegeStatus.전공);
            collegeRepository.save(college12);

            College college13 = College.createCollege("교양필수",CollegeStatus.교양);
            collegeRepository.save(college13);

            College college14 = College.createCollege("교양선택",CollegeStatus.교양);
            collegeRepository.save(college14);

            College college15 = College.createCollege("연계전공",CollegeStatus.교양);
            collegeRepository.save(college15);

            College college16 = College.createCollege("교직",CollegeStatus.교양);
            collegeRepository.save(college16);

            College college17 = College.createCollege("일반선택",CollegeStatus.교양);
            collegeRepository.save(college17);

            College college18 = College.createCollege("군사학",CollegeStatus.교양);
            collegeRepository.save(college18);


        }
    }

    public void initAdmin(){

        List<Member> adminMembers = memberQueryRepository.findMemberWithAdminRole();

        if(adminMembers.isEmpty()) {

            // TODO 기본 관리자 아이디 설정
            Member admin = Member.createMember(201601757, passwordEncoder.encode("rjatjd0815*"),
                    "황주환");

            Role role = Role.createRole();
            role.giveRoleToMember(admin);
            Role adminRole = Role.createAdminRole();
            adminRole.giveRoleToMember(admin);

            memberRepository.save(admin);
        }
    }
}
