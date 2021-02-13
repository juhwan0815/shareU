package inu.project.shareu.repository.query;

import com.querydsl.jpa.impl.JPAQueryFactory;
import inu.project.shareu.domain.CartStatus;
import inu.project.shareu.domain.Member;
import inu.project.shareu.domain.QMember;
import inu.project.shareu.domain.QRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static inu.project.shareu.domain.QCart.cart;
import static inu.project.shareu.domain.QItem.item;
import static inu.project.shareu.domain.QMember.*;
import static inu.project.shareu.domain.QMember.member;
import static inu.project.shareu.domain.QRole.*;

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

    public List<Member> findMemberWithAdminRole(){

        List<Member> adminMembers = queryFactory
                .selectFrom(member).distinct()
                .join(member.roles, role).fetchJoin()
                .where(role.roleName.eq("ROLE_ADMIN"))
                .fetch();

        return adminMembers;
    }
}
