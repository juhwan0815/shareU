package inu.project.shareu.model.lecture.request;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class LectureSaveRequest {

    @ApiModelProperty(name = "majorId",value = "전공 Id",required = true,dataType = "long")
    private Long majorId;

    @ApiModelProperty(name = "lectureName",value = "강의명",required = true,dataType = "string")
    private String lectureName;

    @ApiModelProperty(name = "professor",value = "교수명",required = true,dataType = "string")
    private String professor;

}
