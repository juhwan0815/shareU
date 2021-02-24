package inu.project.shareu.model.college.response;

import inu.project.shareu.domain.College;
import inu.project.shareu.model.major.response.MajorResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@ApiModel(value = "단과대학 및 소속학과 조회 응답 모델")
public class CollegeWithMajorResponse {

    @ApiModelProperty(name = "collegeId",value = "단과대학 Id",example = "1")
    private Long collegeId;

    @ApiModelProperty(name = "collegeName",value = "단과대학 이름",example = "정보기술대학")
    private String collegeName;

    @ApiModelProperty(name = "majors",value = "소속 학과")
    private List<MajorResponse> majors;

    public CollegeWithMajorResponse(College college) {
        this.collegeId = college.getId();
        this.collegeName = college.getName();
        this.majors = college.getMajors().stream()
                    .map(major -> new MajorResponse(major))
                    .collect(Collectors.toList());
    }
}
