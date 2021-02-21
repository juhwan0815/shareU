package inu.project.shareu.model.member.request;

import lombok.Data;

@Data
public class MemberUpdateRequest {

    private String name;

    private String currentPassword;

    private String changePassword1;

    private String changePassword2;

}
