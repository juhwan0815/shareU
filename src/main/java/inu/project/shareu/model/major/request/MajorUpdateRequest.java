package inu.project.shareu.model.major.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("전공 & 교양 수정 요청 모델")
public class MajorUpdateRequest {

    @ApiModelProperty(name = "majorName",value = "전공 이름",required = true)
    private String majorName;
}
