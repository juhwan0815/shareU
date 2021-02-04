package inu.project.shareu.model.request.member;

import lombok.Data;

@Data
public class MemberUpdatePasswordDto {

    private String currentPassword;

    private String changePassword1;

    private String changePassword2;
}
