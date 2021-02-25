package inu.project.shareu.model.review.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel("리뷰 수정 요청 모델")
public class ReviewUpdateRequest {

    @ApiModelProperty(name = "reviewContents",value = "리뷰 내용",required = true,dataType = "string")
    @NotBlank(message = "리뷰 내용은 필수값입니다.")
    private String reviewContents;

    @ApiModelProperty(name = "recommend",value = "추천 or 비추천",required = true,dataType = "boolean")
    @NotNull(message = "추천 여부는 필수값입니다.")
    private Boolean recommend; // 추천 true, 비추천 false
}
