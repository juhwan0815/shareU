package inu.project.shareu.repository;

import inu.project.shareu.domain.Item;
import inu.project.shareu.domain.Member;
import inu.project.shareu.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review,Long> {

    List<Review> findByMemberAndItem(Member member, Item item);

    @Query("select r from Review r join fetch r.member where r.id =:reviewId")
    Optional<Review> findWithMemberById(@Param("reviewId") Long reviewId);

    @Query("select r from Review r join fetch r.item where r.id =:reviewId")
    Optional<Review> findWithItemById(@Param("reviewId") Long reviewId);

}
