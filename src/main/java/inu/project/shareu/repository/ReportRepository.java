package inu.project.shareu.repository;

import inu.project.shareu.domain.Member;
import inu.project.shareu.domain.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report,Long> {

    List<Report> findByMember(Member member);
}
