package inu.project.shareu.repository.query;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import inu.project.shareu.advice.exception.ItemException;
import inu.project.shareu.domain.*;
import inu.project.shareu.model.item.request.ItemSearchCondition;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

import static inu.project.shareu.domain.QCollege.*;
import static inu.project.shareu.domain.QItem.*;
import static inu.project.shareu.domain.QLecture.*;
import static inu.project.shareu.domain.QMajor.*;
import static inu.project.shareu.domain.QStore.*;

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

    public Item findWithMajorAndLectureAndStoreAndCollegeById(Long itemId) {
        List<Item> items = queryFactory
                .selectFrom(item).distinct()
                .join(item.lecture, lecture).fetchJoin()
                .join(item.major, major).fetchJoin()
                .join(item.storeList, store).fetchJoin()
                .join(major.college, college).fetchJoin()
                .where(item.id.eq(itemId))
                .fetch();

        if (items.isEmpty()){
            throw new ItemException("존재하지 않는 족보입니다");
        }

        return items.get(0);
    }


    public Page<Item> findWithLectureByItemSearchCondition(ItemSearchCondition condition,
                                                           Pageable pageable) {
        QueryResults<Item> results = queryFactory
                .selectFrom(item)
                .leftJoin(item.lecture, lecture).fetchJoin()
                .leftJoin(item.major, major).fetchJoin()
                .leftJoin(major.college, college).fetchJoin()
                .where(
                        itemStatus(ItemStatus.SALE),
                        titleLike(condition.getTitle()),
                        professorEq(condition.getProfessor()),
                        lectureNameEq(condition.getLectureName()),
                        majorEq(condition.getMajorId()),
                        collegeStatusEq(condition.getCollegeStatus())
                )
                .orderBy(orderByEq(condition.getOrderBy()))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetchResults();

        List<Item> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression itemStatus(ItemStatus itemStatus){
        return item.itemstatus.eq(itemStatus);
    }

    private BooleanExpression titleLike(String title) {
        return StringUtils.hasText(title) ? item.title.contains(title) : null;
    }

    private BooleanExpression professorEq(String professor) {
        return StringUtils.hasText(professor) ? lecture.professor.eq(professor) : null;
    }

    private BooleanExpression lectureNameEq(String lectureName) {
        return StringUtils.hasText(lectureName) ? lecture.lectureName.eq(lectureName) : null;
    }

    private BooleanExpression majorEq(Long majorId) {
        return majorId != null ? major.id.eq(majorId) : null;
    }

    private BooleanExpression collegeStatusEq(String collegeStatus) {
        if(StringUtils.hasText(collegeStatus)){
            if(collegeStatus.equals("전공")){
                return college.status.eq(CollegeStatus.전공);
            }else if(collegeStatus.equals("교양")){
                return college.status.eq(CollegeStatus.교양);
            }
            else return null;
        }
        return null;
    }

    private OrderSpecifier orderByEq(String orderBy) {
        if(StringUtils.hasText(orderBy)){
            if(orderBy.equals("추천순")){
                return item.recommend.desc();
            }else if(orderBy.equals("최신순")){
                return item.id.desc();
            }
            else return item.recommend.desc();
        }
        return item.recommend.desc();
    }
}
