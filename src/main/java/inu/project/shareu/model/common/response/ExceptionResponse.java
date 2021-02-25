package inu.project.shareu.model.common.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "예외 공통 응답")
public class ExceptionResponse {

    @ApiModelProperty(name = "message",value = "에외 메세지")
    private String message;

    @ApiModelProperty(name = "attribute",value = "예외 속성",example = "{}")
    private Map<String,String> attributes;

    public ExceptionResponse(String message) {
        this.message = message;
    }
}
