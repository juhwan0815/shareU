package inu.project.shareu.model.report.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel("족보 신고 등록 요청 모델")
public class ReportItemSaveRequest {

    @ApiModelProperty(name = "reportContents",value = "신고 내용",required = true)
    @NotBlank(message = "신고 내용은 필수 값입니다.")
    private String reportContents;

}
