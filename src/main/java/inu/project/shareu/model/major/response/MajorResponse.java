package inu.project.shareu.model.major.response;

import inu.project.shareu.domain.Major;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("전공 조회 응답 모델")
public class MajorResponse {

    @ApiModelProperty(name = "majorId",value = "전공 Id",example = "1")
    private Long majorId;

    @ApiModelProperty(name = "majorName",value = "전공 이름",example = "정보통신공학과")
    private String majorName;

    public MajorResponse(Major major) {
        this.majorId = major.getId();
        this.majorName = major.getMajorName();
    }
}
