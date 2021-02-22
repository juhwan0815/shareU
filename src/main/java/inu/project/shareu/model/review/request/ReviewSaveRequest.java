package inu.project.shareu.model.review.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ReviewSaveRequest {

    @ApiModelProperty(name = "itemId",value = "족보 Id",required = true,dataType = "long")
    private Long itemId;

    @ApiModelProperty(name = "reviewContents",value = "리뷰 내용",required = true,dataType = "string")
    private String reviewContents;

    @ApiModelProperty(name = "recommend",value = "추천 or 비추천",required = true,dataType = "boolean")
    private Boolean recommend; // true 면 추천, false 면 비추천
}
