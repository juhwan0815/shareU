package inu.project.shareu.model.review.response;

import inu.project.shareu.domain.RecommendStatus;
import inu.project.shareu.domain.Review;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

@Data
@ApiModel("리뷰 조회 응답 모델")
public class ReviewResponse {

    @ApiModelProperty(name = "reviewId",value = "리뷰 Id",example = "1")
    private Long reviewId;

    @ApiModelProperty(name = "reviewContents",value = "리뷰 내용",example = "좋은 자료입니다")
    private String reviewContents;

    @ApiModelProperty(name = "recommendStatus",value = "추천 여부",example = "true")
    private boolean recommendStatus;

    @ApiModelProperty(name = "createdDate",value = "리뷰 작성일",example = "2021-02-21")
    private LocalDate createdDate;

    @ApiModelProperty(name = "memberName",value = "회원명",example = "황주환")
    private String memberName;

    public ReviewResponse(Review review) {
        this.reviewId = review.getId();
        this.reviewContents = review.getReviewContents();
        this.createdDate = review.getCreatedDate().toLocalDate();
        this.memberName = review.getMember().getName();

        if(review.getRecommendStatus().equals(RecommendStatus.GOOD)){
            recommendStatus = true;
        }else{
            recommendStatus = false;
        }

    }
}
