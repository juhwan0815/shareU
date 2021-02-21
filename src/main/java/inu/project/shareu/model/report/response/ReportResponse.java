package inu.project.shareu.model.report.response;

import inu.project.shareu.domain.Report;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ReportResponse {

    private Long reportId;

    private String lectureName;

    private String title;

    private String division;

    private String memberName;

    private LocalDate localDate;

    public ReportResponse(Report report) {
        this.reportId = report.getId();
        this.lectureName = report.getItem().getLecture().getLectureName();
        this.title = report.getItem().getTitle();
        this.memberName = report.getMember().getName();
        this.localDate = report.getCreatedDate().toLocalDate();

        if(report.getReview() != null){
            this.division = "댓글";
        }else {
            this.division = "족보";
        }
    }
}
