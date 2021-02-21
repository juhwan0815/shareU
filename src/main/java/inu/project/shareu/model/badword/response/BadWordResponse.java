package inu.project.shareu.model.badword.response;

import inu.project.shareu.domain.BadWord;
import lombok.Data;

@Data
public class BadWordResponse {

    private Long badWordId;

    private String word;

    public BadWordResponse(BadWord badWord) {
        this.badWordId = badWord.getId();
        this.word = badWord.getWord();
    }
}
