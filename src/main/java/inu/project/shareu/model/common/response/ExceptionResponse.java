package inu.project.shareu.model.common.response;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "예외 공통 응답",description = "예외 메세지")
public class ExceptionResponse {

    private String message;

}
