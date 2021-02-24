package inu.project.shareu.model.member.response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@ApiModel("회원 로그인 응답 모델")
public class MemberAuthResponse {

    @ApiModelProperty(name = "token",value = "로그인 액세스 토큰",example = "bearer ~")
    private String token;


}
