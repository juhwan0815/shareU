package inu.project.shareu.model.badword.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel(value = "금칙어 등록 요청 모델",description = "금칙어 등록 요청 모델")
public class BadWordSaveRequest {

    @ApiModelProperty(name = "badWord",value = "금칙어 단어", required = true)
    @NotBlank(message = "금칙어는 필수값입니다.")
    private String badWord;

}
