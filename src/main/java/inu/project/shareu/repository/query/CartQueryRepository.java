package inu.project.shareu.repository.query;

import com.querydsl.jpa.impl.JPAQueryFactory;
import inu.project.shareu.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static inu.project.shareu.domain.QCart.*;
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
}
