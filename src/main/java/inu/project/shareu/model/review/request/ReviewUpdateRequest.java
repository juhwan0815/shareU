package inu.project.shareu.model.review.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ReviewUpdateRequest {

    @ApiModelProperty(name = "reviewContents",value = "리뷰 내용",required = true,dataType = "string")
    private String reviewContents;

    @ApiModelProperty(name = "recommend",value = "추천 or 비추천",required = true,dataType = "boolean")
    private Boolean recommend; // 추천 true, 비추천 false
}
