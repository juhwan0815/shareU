package inu.project.shareu.model.member.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel("회원 로그인 요청 모델")
public class MemberLoginRequest {

    @ApiModelProperty(name = "studentNumber",value = "학번",dataType = "integer",required = true)
    @NotNull(message = "학번은 필수값입니다.")
    @Max(value = 210000000,message = "정확한 학번을 입력해주세요")
    @Min(value = 200000000,message = "정확한 학번을 입력해주세요")
    private int studentNumber;

    @ApiModelProperty(name = "password",value = "비밀번호",dataType = "String", required = true)
    @NotBlank(message = "비밀번호는 필수값입니다.")
    private String password;
}
