package inu.project.shareu.repository;

import inu.project.shareu.domain.College;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CollegeRepository extends JpaRepository<College,Long> {

    @Query("select distinct c From College c left join fetch c.majors")
    List<College> findALlWithMajors();
}
