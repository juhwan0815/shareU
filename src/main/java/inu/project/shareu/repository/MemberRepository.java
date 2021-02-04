package inu.project.shareu.repository;

import inu.project.shareu.domain.Member;
import inu.project.shareu.domain.MemberStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long> {

    Optional<Member> findByStudentNumber(int studentNumber);

    Optional<Member> findByName(String Name);

    @Query("select distinct m from Member m join fetch m.roles where m.id = :id")
    Optional<Member> findWithRoleById(@Param("id") Long id);

    @Query("select distinct m from Member m join fetch m.roles where m.studentNumber = :studentNumber")
    Optional<Member> findWithRoleByStudentNumber(@Param("studentNumber") int studentNumber);
}
