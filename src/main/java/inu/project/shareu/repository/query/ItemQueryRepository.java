package inu.project.shareu.repository.query;

import com.querydsl.jpa.impl.JPAQueryFactory;
import inu.project.shareu.domain.Item;
import inu.project.shareu.domain.Member;
import inu.project.shareu.domain.QItem;
import inu.project.shareu.domain.QLecture;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static inu.project.shareu.domain.QCart.cart;
import static inu.project.shareu.domain.QItem.*;
import static inu.project.shareu.domain.QLecture.*;

@Repository
@RequiredArgsConstructor
public class ItemQueryRepository {

    private final JPAQueryFactory queryFactory;

    public Page<Item> findPageByMember(Member member, Pageable pageable) {
        List<Item> content = queryFactory
                .selectFrom(item)
                .join(item.lecture, lecture).fetchJoin()
                .where(item.member.eq(member))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .selectFrom(item)
                .where(item.member.eq(member))
                .fetchCount();

        return new PageImpl<>(content,pageable,total);
    }
}
