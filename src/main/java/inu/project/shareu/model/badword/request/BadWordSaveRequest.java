package inu.project.shareu.model.badword.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BadWordSaveRequest {

    @ApiModelProperty(name = "badWord",value = "금칙어 단어",dataType = "String", required = true)
    private String badWord;

}
