package inu.project.shareu.model.item.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ItemUpdateRequest {

    @ApiModelProperty(name = "title",value = "족보 제목",required = true,dataType = "String")
    private String title;

    @ApiModelProperty(name = "itemContents",value = "족보 내용",required = true,dataType = "String")
    private String itemContents;

}
