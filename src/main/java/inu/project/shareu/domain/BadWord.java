package inu.project.shareu.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BadWord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bad_word_id")
    private Long id;

    @Column(unique = true,nullable = false)
    private String word;

    public static BadWord createBadWord(String word){
        BadWord badWord = new BadWord();
        badWord.word=word;
        return badWord;
    }
}
