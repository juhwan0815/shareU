package inu.project.shareu.repository.query;

import com.querydsl.jpa.impl.JPAQueryFactory;
import inu.project.shareu.advice.exception.ItemException;
import inu.project.shareu.domain.Item;
import inu.project.shareu.domain.QItem;
import inu.project.shareu.domain.QMember;
import inu.project.shareu.domain.QStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static inu.project.shareu.domain.QItem.*;
import static inu.project.shareu.domain.QMember.*;
import static inu.project.shareu.domain.QStore.*;

@Repository
@RequiredArgsConstructor
public class ItemQueryRepository {

    private final JPAQueryFactory queryFactory;

    public Item findItemWithStoreAndMemberByItemId(Long itemId){
        List<Item> result = queryFactory
                .selectFrom(item).distinct()
                .join(item.member, member).fetchJoin()
                .join(item.storeList, store).fetchJoin()
                .where(item.id.eq(itemId))
                .fetch();

        if(result.isEmpty()){
            throw new ItemException("존재하지 않는 족보입니다.");
        }

        return result.get(0);
    }



}
