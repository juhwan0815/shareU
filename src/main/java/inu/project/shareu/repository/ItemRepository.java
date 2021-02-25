package inu.project.shareu.repository;

import inu.project.shareu.domain.Item;
import inu.project.shareu.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item,Long> {

    @Query("select i from Item i join fetch i.member where i.id = :itemId")
    Optional<Item> findWithMemberById(@Param("itemId") Long itemId);

    @Query("select distinct i from Item i left join fetch i.storeList where i.member =:member")
    List<Item> findWithStoreByMember(@Param("member") Member member);
}
