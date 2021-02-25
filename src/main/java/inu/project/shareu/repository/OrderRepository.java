package inu.project.shareu.repository;

import inu.project.shareu.domain.Member;
import inu.project.shareu.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {

    List<Order> findByMember(Member member);
}
