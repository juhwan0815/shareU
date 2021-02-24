package inu.project.shareu.model.common.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "예외 공통 응답",description = "예외 메세지")
public class ExceptionResponse {

    @ApiModelProperty(name = "message",value = "에외 응답")
    private String message;

}
