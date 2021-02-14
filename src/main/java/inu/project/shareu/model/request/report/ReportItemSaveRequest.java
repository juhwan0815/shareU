package inu.project.shareu.model.request.report;

import lombok.Data;

@Data
public class ReportItemSaveRequest {

    private Long itemId;

    private String reportContents;

}
