package inu.project.shareu.model.member.response;

import inu.project.shareu.domain.Member;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("차단 회원 조회 응답 모델")
public class MemberBlockResponse {

    @ApiModelProperty(name = "memberId",value = "회원 Id",example = "1")
    private Long memberId;

    @ApiModelProperty(name = "studentNumber",value = "학번",example = "201601757")
    private int studentNumber;

    @ApiModelProperty(name = "name",value = "별명",example = "황주환")
    private String name;

    public MemberBlockResponse(Member member) {
        this.memberId = member.getId();
        this.studentNumber = member.getStudentNumber();
        this.name = member.getName();
    }
}
