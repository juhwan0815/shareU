package inu.project.shareu.model.badword.response;

import inu.project.shareu.domain.BadWord;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "금칙어 조회 응답 모델")
public class BadWordResponse {

    @ApiModelProperty(name = "badWordId",value = "금칙어 Id",example = "1")
    private Long badWordId;

    @ApiModelProperty(name = "word",value = "금칙어",example = "병신")
    private String badWord;

    public BadWordResponse(BadWord badWord) {
        this.badWordId = badWord.getId();
        this.badWord = badWord.getWord();
    }
}
