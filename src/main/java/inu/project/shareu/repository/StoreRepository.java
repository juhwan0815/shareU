package inu.project.shareu.repository;

import inu.project.shareu.domain.Item;
import inu.project.shareu.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store,Long> {

    List<Store> findByItem(Item item);

    @Query("select s from Store s join fetch s.item where s.id =:storeId")
    Optional<Store> findWithItemById(@Param("storeId") Long storeId);
}
