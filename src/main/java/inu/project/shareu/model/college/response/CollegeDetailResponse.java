package inu.project.shareu.model.college.response;

import inu.project.shareu.domain.College;
import inu.project.shareu.domain.CollegeStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("단과대학 상세 응답 모델")
public class CollegeDetailResponse {

    @ApiModelProperty(name = "collegeId",value = "단과대학 Id",example = "1")
    private Long collegeId;

    @ApiModelProperty(name = "collegeName",value = "단과대학 이름",example = "정보기술대학")
    private String collegeName;

    @ApiModelProperty(name = "collegeStatus",value = "단과대학 분류 전공 or 교양",example = "전공")
    private String collegeStatus;

    public CollegeDetailResponse(College college) {
        this.collegeId = college.getId();
        this.collegeName = college.getName();
        if(college.getStatus().equals(CollegeStatus.전공)){
            collegeStatus = "전공";
        }else{
            collegeStatus = "교양";
        }

    }
}
