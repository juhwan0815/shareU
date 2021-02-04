package inu.project.shareu.model.request.member;

import lombok.Data;

@Data
public class MemberLoginDto {

    private int studentNumber;

    private String password;
}
