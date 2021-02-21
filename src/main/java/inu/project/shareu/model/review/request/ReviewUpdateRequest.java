package inu.project.shareu.model.review.request;

import lombok.Data;

@Data
public class ReviewUpdateRequest {

    private String reviewContents;

    private Boolean recommend; // 추천 true, 비추천 false
}
