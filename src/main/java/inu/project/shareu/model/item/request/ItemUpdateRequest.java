package inu.project.shareu.model.item.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel("족보 수정 요청 모델")
public class ItemUpdateRequest {

    @ApiModelProperty(name = "title",value = "족보 제목",required = true)
    @NotBlank(message = "족보 제목은 필수값입니다.")
    private String title;

    @ApiModelProperty(name = "itemContents",value = "족보 내용",required = true)
    @NotBlank(message = "족보 내용은 필수값입니다.")
    private String itemContents;

}
