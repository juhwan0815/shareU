package inu.project.shareu.model.response.item;

import inu.project.shareu.domain.Cart;
import inu.project.shareu.domain.Item;
import inu.project.shareu.domain.ItemStatus;
import inu.project.shareu.domain.Lecture;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ItemResponse {

    private Long itemId;

    private String lectureName;

    private String title;

    private String professor;

    private String itemStatus;

    private LocalDate localDate;

    public ItemResponse(Item item, Lecture lecture) {
        this.lectureName = lecture.getLectureName();
        this.title = item.getTitle();
        this.professor = lecture.getProfessor();

        if(item.getItemstatus().equals(ItemStatus.SALE)){
            itemStatus = "판매중";
        }else{
            itemStatus = "판매정지";
        }

        this.localDate = item.getCreatedDate().toLocalDate();
    }
}
