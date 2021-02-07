package inu.project.shareu.repository.query;

import com.querydsl.jpa.impl.JPAQueryFactory;
import inu.project.shareu.domain.CartStatus;
import inu.project.shareu.domain.Member;
import inu.project.shareu.domain.QMember;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static inu.project.shareu.domain.QCart.cart;
import static inu.project.shareu.domain.QItem.item;
import static inu.project.shareu.domain.QMember.*;
import static inu.project.shareu.domain.QMember.member;

@Repository
@RequiredArgsConstructor
public class MemberQueryRepository {

    private final JPAQueryFactory queryFactory;

    public Member findMemberWithCurrentCartsAndItemById(Long memberId) {
        // TODO 쿼리 수정 예정
        Member loginMember = queryFactory
                .selectFrom(member).distinct()
                .join(member.carts, cart).fetchJoin()
                .join(cart.item, item).fetchJoin()
                .where(member.id.eq(memberId)
                        .and(cart.cartStatus.eq(CartStatus.CART)))
                .fetchOne();

        return loginMember;
    }
}
