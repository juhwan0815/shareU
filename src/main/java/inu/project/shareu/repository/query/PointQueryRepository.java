package inu.project.shareu.repository.query;

import com.querydsl.jpa.impl.JPAQueryFactory;
import inu.project.shareu.domain.Member;
import inu.project.shareu.domain.Point;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static inu.project.shareu.domain.QItem.*;
import static inu.project.shareu.domain.QPoint.*;

@Repository
@RequiredArgsConstructor
public class PointQueryRepository {

    private final JPAQueryFactory queryFactory;

    public Page<Point> findPageWithItemByMember(Member member, Pageable pageable) {

        List<Point> content = queryFactory
                .selectFrom(point)
                .join(point.item, item).fetchJoin()
                .where(point.member.eq(member))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .selectFrom(point)
                .where(point.member.eq(member))
                .fetchCount();

        return new PageImpl<>(content,pageable,total);
    }

}
