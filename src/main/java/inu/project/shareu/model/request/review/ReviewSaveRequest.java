package inu.project.shareu.model.request.review;

import lombok.Data;

@Data
public class ReviewSaveRequest {

    private Long itemId;

    private String reviewContents;

    private Boolean recommend; // true 면 추천, false 면 비추천
}
