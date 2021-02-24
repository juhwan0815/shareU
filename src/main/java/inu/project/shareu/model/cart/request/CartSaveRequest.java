package inu.project.shareu.model.cart.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@ApiModel(value = "장바구니 등록 요청 모델")
public class CartSaveRequest {

    @ApiModelProperty(name = "itemId",value = "족보 Id",required = true)
    @NotNull(message = "아이템 Id는 필수값입니다.")
    @Positive(message = "아이템 Id는 양수만 가능합니다.")
    private Long itemId;
}
