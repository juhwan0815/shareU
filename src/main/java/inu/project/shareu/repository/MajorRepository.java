package inu.project.shareu.repository;

import inu.project.shareu.domain.Major;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MajorRepository extends JpaRepository<Major,Long> {

    Optional<Major> findByMajorName(String majorName);
}
