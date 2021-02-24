package inu.project.shareu.model.college.response;

import inu.project.shareu.domain.College;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "단과대학 조회 응답 모델")
public class CollegeResponse {

    @ApiModelProperty(name = "collegeId",value = "단과대학 Id",example = "1")
    private Long collegeId;

    @ApiModelProperty(name = "collegeName",value = "단과대학 이름",example = "정보기술대학")
    private String collegeName;

    public CollegeResponse(College college) {
        this.collegeId = college.getId();
        this.collegeName = college.getName();
    }
}
