package inu.project.shareu.repository;

import inu.project.shareu.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store,Long> {

    @Query("select s from Store s join fetch s.item where s.fileStoreName =:storeName")
    Optional<Store> findWithItemByFileStoreName(@Param("storeName") String storeName);
}
