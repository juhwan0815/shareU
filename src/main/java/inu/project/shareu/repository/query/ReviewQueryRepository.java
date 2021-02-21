package inu.project.shareu.repository.query;

import com.querydsl.jpa.impl.JPAQueryFactory;
import inu.project.shareu.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static inu.project.shareu.domain.QMember.*;
import static inu.project.shareu.domain.QReview.*;

@Repository
@RequiredArgsConstructor
public class ReviewQueryRepository {

    private final JPAQueryFactory queryFactory;

    public Page<Review> findPageWithMemberByItem(Item item, ReviewStatus reviewStatus,
                                                 Pageable pageable){
        List<Review> content = queryFactory
                .selectFrom(review)
                .join(review.member, member).fetchJoin()
                .where(review.item.eq(item)
                        .and(review.status.eq(reviewStatus)))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .orderBy(review.id.desc())
                .fetch();

        long total = queryFactory
                .selectFrom(review)
                .where(review.item.eq(item)
                        .and(review.status.eq(reviewStatus)))
                .fetchCount();

        return new PageImpl<>(content,pageable,total);
    }
}
