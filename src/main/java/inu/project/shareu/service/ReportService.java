package inu.project.shareu.service;

import inu.project.shareu.advice.exception.ItemException;
import inu.project.shareu.advice.exception.ReportException;
import inu.project.shareu.advice.exception.ReviewException;
import inu.project.shareu.domain.*;
import inu.project.shareu.model.request.report.ReportItemSaveRequest;
import inu.project.shareu.model.request.report.ReportReviewSaveRequest;
import inu.project.shareu.model.response.report.ReportResponse;
import inu.project.shareu.repository.ItemRepository;
import inu.project.shareu.repository.ReportRepository;
import inu.project.shareu.repository.ReviewRepository;
import inu.project.shareu.repository.query.ReportQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportService {

    private final ReportQueryRepository reportQueryRepository;
    private final ReportRepository reportRepository;
    private final ItemRepository itemRepository;
    private final ReviewRepository reviewRepository;

    /**
     * 족보 신고 등록
     * 1. 족보 조회
     * 2. 신고 생성 및 저장
     */
    @Transactional
    public void saveReportItem(Member member,Long itemId,
                               ReportItemSaveRequest reportItemSaveRequest){

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemException("존재하지 않는 족보입니다."));

        Report report = Report.createReport(reportItemSaveRequest.getReportContents(),
                                            item, null, member);
        reportRepository.save(report);
    }

    /**
     * 리뷰 신고 저장
     * 1. 리뷰 조회
     * 2. 신고 생성 및 저장
     */
    @Transactional
    public void saveReportReview(Member member,Long reviewId,
                                 ReportReviewSaveRequest reportReviewSaveRequest) {

        Review review = reviewRepository.findWithItemById(reviewId)
                .orElseThrow(() -> new ReviewException("존재하지 않는 리뷰입니다."));

        Report report = Report.createReport(reportReviewSaveRequest.getReportContents(),
                                            review.getItem(), review, member);
        reportRepository.save(report);
    }

    /**
     * 신고 처리 완료
     * 1. 신고 조회
     * 2. 신고 처리 완료
     */
    @Transactional
    public void deleteReport(Long reportId) {

        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ReportException("존재하지 않는 신고입니다."));

        report.changeReportStatus();
    }

    public Page<ReportResponse> findPage(Pageable pageable) {
        Page<Report> reports = reportQueryRepository
                .findPageWithMemberAndItemAndLectureAndReview(pageable);
        return reports.map(report -> new ReportResponse(report));
    }
}
