package inu.project.shareu.repository.query;

import com.querydsl.jpa.impl.JPAQueryFactory;
import inu.project.shareu.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static inu.project.shareu.domain.QCart.*;
import static inu.project.shareu.domain.QOrder.*;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {

    private final JPAQueryFactory queryFactory;

    public List<Order> findOrderWithCartByMemberAndItem(Member member, Item item) {
        List<Order> orders = queryFactory
                .selectFrom(order).distinct()
                .join(order.carts, cart).fetchJoin()
                .where(order.member.eq(member)
                        .and(cart.item.eq(item)))
                .fetch();
        return orders;
    }


}
