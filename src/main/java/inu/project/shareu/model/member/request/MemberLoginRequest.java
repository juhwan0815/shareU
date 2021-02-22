package inu.project.shareu.model.member.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MemberLoginRequest {

    @ApiModelProperty(name = "studentNumber",value = "학번",dataType = "integer",required = true)
    private int studentNumber;

    @ApiModelProperty(name = "password",value = "비밀번호",dataType = "String", required = true)
    private String password;
}
