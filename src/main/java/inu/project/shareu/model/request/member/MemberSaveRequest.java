package inu.project.shareu.model.request.member;

import io.swagger.annotations.ApiParam;
import lombok.Data;

@Data
public class MemberSaveRequest {

    private int studentNumber;

    private String password;

    private String name;

}
