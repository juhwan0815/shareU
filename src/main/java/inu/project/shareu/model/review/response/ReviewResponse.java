package inu.project.shareu.model.review.response;

import inu.project.shareu.domain.RecommendStatus;
import inu.project.shareu.domain.Review;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ReviewResponse {

    private Long reviewId;

    private String reviewContents;

    private boolean recommendStatus;

    private LocalDate localDate;

    private String memberName;

    public ReviewResponse(Review review) {
        this.reviewId = review.getId();
        this.reviewContents = review.getReviewContents();
        this.localDate = review.getCreatedDate().toLocalDate();
        this.memberName = review.getMember().getName();

        if(review.getRecommendStatus().equals(RecommendStatus.GOOD)){
            recommendStatus = true;
        }else{
            recommendStatus = false;
        }

    }
}
