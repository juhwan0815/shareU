package inu.project.shareu.model.member.request;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;

@Data
public class MemberSaveRequest {

    @ApiModelProperty(name = "studentNumber",value = "학번",dataType = "integer",required = true)
    private int studentNumber;

    @ApiModelProperty(name = "password1",value = "비밀번호1",dataType = "String", required = true)
    private String password1;

    @ApiModelProperty(name = "password2",value = "비밀번호2",dataType = "String", required = true)
    private String password2;

    @ApiModelProperty(name = "name",value = "이름 (별명) ",dataType = "String", required = true)
    private String name;

}
