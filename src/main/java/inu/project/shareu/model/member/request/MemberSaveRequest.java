package inu.project.shareu.model.member.request;

import io.swagger.annotations.ApiParam;
import lombok.Data;

@Data
public class MemberSaveRequest {

    private int studentNumber;

    private String password1;

    private String password2;

    private String name;

}