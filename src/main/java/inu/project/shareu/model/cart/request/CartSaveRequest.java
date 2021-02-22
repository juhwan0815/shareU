package inu.project.shareu.model.cart.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CartSaveRequest {

    @ApiModelProperty(name = "itemId",value = "족보 Id",dataType = "long",required = true)
    private Long itemId;
}
