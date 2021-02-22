package inu.project.shareu.model.major.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MajorSaveRequest {

    @ApiModelProperty(name = "collegeId",value = "단과대학 Id",required = true,dataType = "long")
    private Long collegeId;

    @ApiModelProperty(name = "majorName",value = "전공명",required = true,dataType = "string")
    private String majorName;

}
