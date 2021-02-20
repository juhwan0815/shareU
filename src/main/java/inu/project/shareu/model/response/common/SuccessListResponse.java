package inu.project.shareu.model.response.common;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SuccessListResponse<T> {

    private List<T> data;

}
