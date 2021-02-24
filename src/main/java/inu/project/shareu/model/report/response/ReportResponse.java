package inu.project.shareu.model.report.response;

import inu.project.shareu.domain.Report;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

@Data
@ApiModel("신고 페이징 조회 응답 모델")
public class ReportResponse {

    @ApiModelProperty(name = "reportId",value = "신고 Id",example = "1")
    private Long reportId;

    @ApiModelProperty(name = "itemId",value = "족보 Id",example = "1")
    private Long itemId;

    @ApiModelProperty(name = "lectureName",value = "강의명",example = "프로그래밍")
    private String lectureName;

    @ApiModelProperty(name = "title",value = "족보명",example = "프로그래밍 족보입니다~")
    private String title;

    @ApiModelProperty(name = "division",value = "신고 분류 (댓글 or 족보)",example = "족보")
    private String division;

    @ApiModelProperty(name = "memberName",value = "신고 회원명",example = "지나가던 아무개")
    private String memberName;

    @ApiModelProperty(name = "createdDate",value = "신고 작성일",example = "2021-02-21")
    private LocalDate createdDate;

    public ReportResponse(Report report) {
        this.reportId = report.getId();
        this.itemId = report.getItem().getId();
        this.lectureName = report.getItem().getLecture().getLectureName();
        this.title = report.getItem().getTitle();
        this.memberName = report.getMember().getName();
        this.createdDate = report.getCreatedDate().toLocalDate();

        if(report.getReview() != null){
            this.division = "댓글";
        }else {
            this.division = "족보";
        }
    }
}
