package inu.project.shareu.repository.query;

import com.querydsl.jpa.impl.JPAQueryFactory;
import inu.project.shareu.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static inu.project.shareu.domain.QItem.*;
import static inu.project.shareu.domain.QMember.*;
import static inu.project.shareu.domain.QReview.*;

@Repository
@RequiredArgsConstructor
public class ReviewQueryRepository {

    private final JPAQueryFactory queryFactory;

    public List<Review> findReviewByMemberAndItem(Member writer, Item reviewItem){
        List<Review> reviews = queryFactory
                .selectFrom(review)
                .join(review.member, member)
                .join(review.item, item)
                .where(review.member.eq(writer)
                        .and(review.item.eq(reviewItem)))
                .fetch();

        return reviews;
    }
}
