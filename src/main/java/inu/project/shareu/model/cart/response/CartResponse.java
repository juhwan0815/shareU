package inu.project.shareu.model.cart.response;

import com.querydsl.core.annotations.QueryProjection;
import inu.project.shareu.domain.*;
import lombok.Data;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class CartResponse {

    private Long cartId;

    private Long itemId;

    private String lectureName;

    private String title;

    private String professor;

    private String itemStatus;

    private LocalDate localDate;

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
        this.localDate = cart.getCreatedDate().toLocalDate();
    }
}
