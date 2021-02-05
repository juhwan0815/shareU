package inu.project.shareu.repository;

import inu.project.shareu.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item,Long> {

    @Query("select i from Item i join fetch i.member where i.id = :itemId")
    Optional<Item> findWithMemberById(@Param("itemId") Long itemId);
}
