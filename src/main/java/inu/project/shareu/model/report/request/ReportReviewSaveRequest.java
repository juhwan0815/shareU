package inu.project.shareu.model.report.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ReportReviewSaveRequest {

    @ApiModelProperty(name = "reportContents",value = "리뷰 내용",required = true,dataType = "string")
    private String reportContents;
}
