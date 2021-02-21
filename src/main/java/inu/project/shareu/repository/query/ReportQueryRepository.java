package inu.project.shareu.repository.query;

import com.querydsl.jpa.impl.JPAQueryFactory;
import inu.project.shareu.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static inu.project.shareu.domain.QItem.*;
import static inu.project.shareu.domain.QLecture.*;
import static inu.project.shareu.domain.QReport.*;
import static inu.project.shareu.domain.QReview.*;

@Repository
@RequiredArgsConstructor
public class ReportQueryRepository {

    private final JPAQueryFactory queryFactory;

    public Page<Report> findPageWithMemberAndItemAndLectureAndReview(Pageable pageable){
        List<Report> content = queryFactory
                .selectFrom(report)
                .leftJoin(report.review, review).fetchJoin()
                .leftJoin(report.member).fetchJoin()
                .leftJoin(report.item, item).fetchJoin()
                .leftJoin(item.lecture, lecture).fetchJoin()
                .where(report.reportStatus.eq(ReportStatus.WAIT))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(report.id.asc())
                .fetch();

        long total = queryFactory
                .selectFrom(report)
                .where(report.reportStatus.eq(ReportStatus.WAIT))
                .fetchCount();

        return new PageImpl<>(content,pageable,total);
    }
}
