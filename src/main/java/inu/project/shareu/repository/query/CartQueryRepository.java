package inu.project.shareu.repository.query;

import com.querydsl.jpa.impl.JPAQueryFactory;
import inu.project.shareu.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static inu.project.shareu.domain.QCart.*;
import static inu.project.shareu.domain.QItem.*;
import static inu.project.shareu.domain.QLecture.*;
import static inu.project.shareu.domain.QMember.*;

@Repository
@RequiredArgsConstructor
public class CartQueryRepository {

    private final JPAQueryFactory queryFactory;

    public List<Cart> findWithMemberByItemAndCartStatus(Item item, CartStatus cartStatus) {
        return queryFactory
                .selectFrom(cart)
                .join(cart.member, member).fetchJoin()
                .where(cart.item.eq(item)
                        .and(cart.cartStatus.eq(cartStatus)))
                .fetch();
    }

    public Page<Cart> findWithItemAndLectureByMemberAndCartStatus(Member member, CartStatus cartStatus,
                                                                  Pageable pageable) {
        List<Cart> content = queryFactory
                .selectFrom(cart)
                .join(cart.item, item).fetchJoin()
                .join(item.lecture, lecture).fetchJoin()
                .where(cart.member.eq(member)
                        .and(cart.cartStatus.eq(cartStatus)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(cart.id.desc())
                .fetch();

        long total = queryFactory
                .selectFrom(cart)
                .where(cart.member.eq(member)
                        .and(cart.cartStatus.eq(cartStatus)))
                .fetchCount();

        return new PageImpl<>(content,pageable,total);
    }

}
