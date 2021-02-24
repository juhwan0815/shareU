package inu.project.shareu.model.member.response;

import inu.project.shareu.domain.Member;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@ApiModel("회원 정보 조회 응답 모델")
public class MemberResponse {

    @ApiModelProperty(name = "studentNumber",value = "학번",example = "201601757")
    private int studentNumber;

    @ApiModelProperty(name = "name",value = "별명",example = "황주환")
    private String name;

    @ApiModelProperty(name = "currentPoint",value = "현재 포인트",example = "10")
    private int currentPoint;

    @ApiModelProperty(name = "roles",value = "권한",example = "ROLE_MEMBER")
    private List<String> roles;

    public MemberResponse(Member member) {
        this.studentNumber = member.getStudentNumber();
        this.name = member.getName();
        this.currentPoint = member.getCurrentPoint();
        this.roles = member.getRoles().stream()
                    .map(role -> role.getRoleName())
                    .collect(Collectors.toList());
    }
}
