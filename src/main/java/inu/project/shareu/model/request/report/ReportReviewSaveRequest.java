package inu.project.shareu.model.request.report;

import lombok.Data;

@Data
public class ReportReviewSaveRequest {

    private Long reviewId;

    private String reportContents;
}
