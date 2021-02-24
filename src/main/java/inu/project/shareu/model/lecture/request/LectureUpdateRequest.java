package inu.project.shareu.model.lecture.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel("강의 수정 요청 모델")
public class LectureUpdateRequest {

    @ApiModelProperty(name = "lectureName",value = "강의명",required = true)
    @NotBlank(message = "강의명은 필수값입니다.")
    private String lectureName;

    @ApiModelProperty(name = "professor",value = "교수명",required = true)
    @NotBlank(message = "교수명은 필수값입니다.")
    private String professor;
}
