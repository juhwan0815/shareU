package inu.project.shareu.model.member.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel("회원 수정 요청 모델")
public class MemberUpdateRequest {

    @ApiModelProperty(name = "name",value = "이름 (별명)",dataType = "String",required = true)
    @NotBlank(message = "별명은 필수값입니다.")
    private String name;

    @ApiModelProperty(name = "currentPassword",value = "현재 비밀번호",dataType = "String",required = true)
    @NotBlank(message = "현재 비밀번호는 필수값입니다.")
    private String currentPassword;

    @ApiModelProperty(name = "changePassword1",value = "변경될 비밀번호1",dataType = "String",required = true)
    private String changePassword1;

    @ApiModelProperty(name = "changePassword2",value = "변경될 비밀번호2",dataType = "String",required = true)
    private String changePassword2;

}
