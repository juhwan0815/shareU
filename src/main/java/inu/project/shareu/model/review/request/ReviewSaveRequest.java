package inu.project.shareu.model.review.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@ApiModel("리뷰 등록 요청 모델")
public class ReviewSaveRequest {

    @ApiModelProperty(name = "itemId",value = "족보 Id",required = true)
    @Positive(message = "itemId는 양수만 가능합니다.")
    @NotNull(message = "itemId는 필수값입니다.")
    private Long itemId;

    @ApiModelProperty(name = "reviewContents",value = "리뷰 내용",required = true)
    @NotBlank(message = "리뷰 내용은 필수값입니다.")
    private String reviewContents;

    @ApiModelProperty(name = "recommend",value = "추천 or 비추천",required = true)
    @NotNull(message = "추천여부는 필수값입니다.")
    private Boolean recommend; // true 면 추천, false 면 비추천
}
