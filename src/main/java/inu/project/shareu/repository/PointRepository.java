package inu.project.shareu.repository;

import inu.project.shareu.domain.Member;
import inu.project.shareu.domain.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PointRepository extends JpaRepository<Point,Long> {

    List<Point> findByMember(Member member);
}
