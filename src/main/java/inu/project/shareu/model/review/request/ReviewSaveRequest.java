package inu.project.shareu.model.review.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("리뷰 등록 요청 모델")
public class ReviewSaveRequest {

    @ApiModelProperty(name = "itemId",value = "족보 Id",required = true)
    private Long itemId;

    @ApiModelProperty(name = "reviewContents",value = "리뷰 내용",required = true)
    private String reviewContents;

    @ApiModelProperty(name = "recommend",value = "추천 or 비추천",required = true)
    private Boolean recommend; // true 면 추천, false 면 비추천
}
