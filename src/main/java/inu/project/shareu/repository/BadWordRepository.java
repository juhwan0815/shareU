package inu.project.shareu.repository;

import inu.project.shareu.domain.BadWord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BadWordRepository extends JpaRepository<BadWord,Long> {

    Optional<BadWord> findByWord(String word);
}
