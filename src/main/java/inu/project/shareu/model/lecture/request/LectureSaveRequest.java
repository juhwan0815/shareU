package inu.project.shareu.model.lecture.request;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@ApiModel("강의 등록 요청 모델")
public class LectureSaveRequest {

    @ApiModelProperty(name = "majorId",value = "전공 Id",required = true)
    @Positive(message = "전공 Id는 양수만 가능합니다.")
    @NotNull(message = "전공 Id는 필수값 입니다.")
    private Long majorId;

    @ApiModelProperty(name = "lectureName",value = "강의명",required = true)
    @NotBlank(message = "강의명은 필수값입니다.")
    private String lectureName;

    @ApiModelProperty(name = "professor",value = "교수명",required = true)
    @NotBlank(message = "교수명은 필수값입니다.")
    private String professor;

}
