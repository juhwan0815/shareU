package inu.project.shareu.model.member.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MemberUpdateRequest {

    @ApiModelProperty(name = "name",value = "이름 (별명)",dataType = "String",required = true)
    private String name;

    @ApiModelProperty(name = "currentPassword",value = "현재 비밀번호",dataType = "String",required = true)
    private String currentPassword;

    @ApiModelProperty(name = "changePassword1",value = "변경될 비밀번호1",dataType = "String",required = true)
    private String changePassword1;

    @ApiModelProperty(name = "changePassword2",value = "변경될 비밀번호2",dataType = "String",required = true)
    private String changePassword2;

}
