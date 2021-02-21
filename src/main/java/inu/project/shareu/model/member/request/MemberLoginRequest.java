package inu.project.shareu.model.member.request;

import lombok.Data;

@Data
public class MemberLoginRequest {

    private int studentNumber;

    private String password;
}
