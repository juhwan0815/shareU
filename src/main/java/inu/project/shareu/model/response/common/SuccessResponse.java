package inu.project.shareu.model.response.common;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SuccessResponse <T> {

    private T data;
}
