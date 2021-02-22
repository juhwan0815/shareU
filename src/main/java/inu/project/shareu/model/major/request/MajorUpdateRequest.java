package inu.project.shareu.model.major.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MajorUpdateRequest {

    @ApiModelProperty(name = "majorName",value = "전공명",required = true,dataType = "string")
    private String majorName;
}
