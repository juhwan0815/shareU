package inu.project.shareu.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Report extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Long id;

    private String reportContents;

    @Enumerated(EnumType.STRING)
    private ReportStatus reportStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    public static Report createReport(String reportContents,Item item,
                                      Review review,Member member){
        Report report = new Report();
        report.item = item;
        report.review = review;
        report.member = member;
        report.reportStatus = ReportStatus.WAIT;
        report.reportContents = reportContents;
        return report;
    }

    public void changeReportStatus() {
        this.reportStatus = ReportStatus.FINISH;
    }
}
