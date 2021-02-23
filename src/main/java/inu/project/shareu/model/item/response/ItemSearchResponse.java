package inu.project.shareu.model.item.response;

import inu.project.shareu.domain.Item;
import inu.project.shareu.domain.ItemStatus;
import inu.project.shareu.domain.Lecture;
import inu.project.shareu.domain.RecommendStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ItemSearchResponse {

    private Long itemId;

    private String lectureName;

    private String title;

    private String itemContents;

    private String professor;

    private String itemStatus;

    private int reviewCount;

    private LocalDate localDate;

    public ItemSearchResponse(Item item, Lecture lecture) {
        this.itemId = item.getId();
        this.lectureName = lecture.getLectureName();
        this.title = item.getTitle();
        this.professor = lecture.getProfessor();
        this.itemContents = item.getItemContents();

        if(item.getItemstatus().equals(ItemStatus.SALE)){
            itemStatus = "판매중";
        }else{
            itemStatus = "판매정지";
        }

        this.localDate = item.getCreatedDate().toLocalDate();
    }
}
