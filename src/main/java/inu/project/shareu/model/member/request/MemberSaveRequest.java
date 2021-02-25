package inu.project.shareu.model.member.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel("회원 가입 요청 모델")
public class MemberSaveRequest {

    @ApiModelProperty(name = "studentNumber",value = "학번",required = true)
    @Max(value = 210000000,message = "정확한 학번을 입력해주세요")
    @Min(value = 200000000,message = "정확한 학번을 입력해주세요")
    @NotNull(message = "학번은 필수값입니다.")
    private int studentNumber;

    @ApiModelProperty(name = "password1",value = "비밀번호1", required = true)
    @NotBlank(message = "비밀번호는 필수값입니다.")
    private String password1;

    @ApiModelProperty(name = "password2",value = "비밀번호2",required = true)
    @NotBlank(message = "비밀번호는 필수값입니다.")
    private String password2;

    @ApiModelProperty(name = "name",value = "이름 (별명) ",required = true)
    @NotBlank(message = "별명은 필수값입니다.")
    private String name;

}
