package inu.project.shareu.model.member.response;

import inu.project.shareu.domain.Member;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class MemberResponse {

    private int studentNumber;

    private String name;

    private int currentPoint;

    private List<String> roles;

    public MemberResponse(Member member) {
        this.studentNumber = member.getStudentNumber();
        this.name = member.getName();
        this.currentPoint = member.getCurrentPoint();
        this.roles = member.getRoles().stream()
                    .map(role -> role.getRoleName())
                    .collect(Collectors.toList());
    }
}
