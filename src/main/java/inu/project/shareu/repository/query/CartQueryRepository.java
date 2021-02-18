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

    public List<Cart> findCartByItemAndMemberId(Item item,Long memberId){
        List<Cart> carts = queryFactory
                .selectFrom(cart)
                .where(cart.member.id.eq(memberId)
                        .and(cart.item.eq(item)))
                .fetch();
        return carts;
    }

}
