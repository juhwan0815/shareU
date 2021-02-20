package inu.project.shareu.repository;

import inu.project.shareu.domain.BadWord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BadWordRepository extends JpaRepository<BadWord,Long> {

    Optional<BadWord> findByWord(String word);

    Page<BadWord> findAll(Pageable pageable);

}
