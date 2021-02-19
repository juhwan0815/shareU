package inu.project.shareu.repository;

import inu.project.shareu.domain.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LectureRepository extends JpaRepository<Lecture,Long> {

    @Query("select l from Lecture l join fetch l.major where l.id = :lectureId")
    Optional<Lecture> findWithMajorById(@Param("lectureId") Long lectureId);

    Optional<Lecture> findByLectureNameAndProfessor(String lectureName,String professor);
}
