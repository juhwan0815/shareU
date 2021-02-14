package inu.project.shareu.service;

import inu.project.shareu.advice.exception.ItemException;
import inu.project.shareu.advice.exception.MemberException;
import inu.project.shareu.advice.exception.ReportException;
import inu.project.shareu.advice.exception.ReviewException;
import inu.project.shareu.domain.Item;
import inu.project.shareu.domain.Member;
import inu.project.shareu.domain.Report;
import inu.project.shareu.domain.Review;
import inu.project.shareu.model.request.report.ReportItemSaveRequest;
import inu.project.shareu.model.request.report.ReportReviewSaveRequest;
import inu.project.shareu.repository.ItemRepository;
import inu.project.shareu.repository.MemberRepository;
import inu.project.shareu.repository.ReportRepository;
import inu.project.shareu.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportService {

    private final ReportRepository reportRepository;
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final ReviewRepository reviewRepository;

    @Transactional
    public void saveReportItem(Long memberId,ReportItemSaveRequest reportItemSaveRequest){
        // TODO 신고 사유 검증필요
        // TODO 한 족보에 대하여 신고를 한번만 할 수 있을지?
        Member findMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException("존재하지 않는 회원입니다."));

        Item item = itemRepository.findById(reportItemSaveRequest.getItemId())
                .orElseThrow(() -> new ItemException("존재하지 않는 족보입니다."));

        Report report = Report.createReport(reportItemSaveRequest.getReportContents(), item,
                null, findMember);

        reportRepository.save(report);
    }

    @Transactional
    public void saveReportReview(Long memberId, ReportReviewSaveRequest reportReviewSaveRequest) {
        // TODO 신고 사유 검증필요
        // TODO 한 족보에 대하여 신고를 한번만 할 수 있을지?
        Member findMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException("존재하지 않는 회원입니다."));

        Review review = reviewRepository.findWithItemById(reportReviewSaveRequest.getReviewId())
                .orElseThrow(() -> new ReviewException("존재하지 않는 리뷰입니다."));

        Report report = Report.createReport(reportReviewSaveRequest.getReportContents(),
                review.getItem(), review, findMember);

        reportRepository.save(report);
    }

    @Transactional
    public void finishReport(Long reportId) {

        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ReportException("존재하지 않는 신고입니다."));

        report.changeReportStatus();
    }
}
