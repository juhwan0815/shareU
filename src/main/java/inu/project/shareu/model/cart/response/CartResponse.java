package inu.project.shareu.model.cart.response;

import com.querydsl.core.annotations.QueryProjection;
import inu.project.shareu.domain.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@ApiModel(value = "장바구니 족보 조회 모델")
public class CartResponse {

    @ApiModelProperty(name = "cartId",value = "장바구니 Id",example = "1")
    private Long cartId;

    @ApiModelProperty(name = "itemId",value = "족보 Id",example = "1")
    private Long itemId;

    @ApiModelProperty(name = "lectureName",value = "강의명",example = "프로그래밍")
    private String lectureName;

    @ApiModelProperty(name = "title",value = "족보 제목",example = "프로그래밍 족보입니다~")
    private String title;

    @ApiModelProperty(name = "professor",value = "교수",example = "오승호")
    private String professor;

    @ApiModelProperty(name = "itemStatus",value = "족보 상태",example = "판매중 or 판매정지")
    private String itemStatus;

    @ApiModelProperty(name = "createdDate",value = "장바구니 등록일",example = "2021-02-21")
    private LocalDate createdDate;

    public CartResponse(Cart cart,Item item,Lecture lecture) {
        this.cartId = cart.getId();
        this.itemId = item.getId();
        this.lectureName = lecture.getLectureName();
        this.title = item.getTitle();
        this.professor = lecture.getProfessor();

        if(item.getItemstatus().equals(ItemStatus.SALE)){
            itemStatus = "판매중";
        }else{
            itemStatus = "판매정지";
        }
        this.createdDate = cart.getCreatedDate().toLocalDate();
    }
}
