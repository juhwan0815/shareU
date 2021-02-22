package inu.project.shareu.model.report.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ReportItemSaveRequest {

    @ApiModelProperty(name = "reportContents",value = "신고 내용",dataType = "string",required = true)
    private String reportContents;

}
