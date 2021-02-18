package inu.project.shareu.repository;

import inu.project.shareu.domain.Cart;
import inu.project.shareu.domain.CartStatus;
import inu.project.shareu.domain.Item;
import inu.project.shareu.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart,Long> {

    @Query("select c from Cart c join fetch c.member where c.id = :cartId")
    Optional<Cart> findWithMemberById(@Param("cartId") Long cartId);

    List<Cart> findByMemberAndItem(Member member, Item item);

    List<Cart> findByMemberAndItemAndCartStatus(Member member, Item item,CartStatus cartStatus);
}
