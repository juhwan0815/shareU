package inu.project.shareu.model.member.response;

import inu.project.shareu.domain.Member;
import lombok.Data;

@Data
public class MemberBlockResponse {

    private Long memberId;

    private int studentNumber;

    private String name;

    public MemberBlockResponse(Member member) {
        this.memberId = member.getId();
        this.studentNumber = member.getStudentNumber();
        this.name = member.getName();
    }
}
