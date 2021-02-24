package inu.project.shareu.model.major.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@ApiModel("전공 & 교양 등록 요청 모델")
public class MajorSaveRequest {

    @ApiModelProperty(name = "collegeId",value = "단과대학 Id",required = true)
    @Positive(message = "단과대학 Id는 양수만 가능합니다.")
    @NotNull(message = "단과대학 Id는 필수값입니다.")
    private Long collegeId;

    @ApiModelProperty(name = "majorName",value = "전공명",required = true,dataType = "string")
    @NotBlank(message = "전공 이름은 필수값입니다.")
    private String majorName;

}
