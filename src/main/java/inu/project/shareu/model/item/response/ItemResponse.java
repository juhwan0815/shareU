package inu.project.shareu.model.item.response;

import inu.project.shareu.domain.Cart;
import inu.project.shareu.domain.Item;
import inu.project.shareu.domain.ItemStatus;
import inu.project.shareu.domain.Lecture;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

@Data
@ApiModel("족보 조회 응답 모델")
public class ItemResponse {

    @ApiModelProperty(name = "itemId",value = "족보 Id",example = "1")
    private Long itemId;

    @ApiModelProperty(name = "lectureName",value = "강의명",example = "프로그래밍")
    private String lectureName;

    @ApiModelProperty(name = "title",value = "족보 제목",example = "프로그래밍 족보입니다.")
    private String title;

    @ApiModelProperty(name = "professor",value = "교수명",example = "오승호")
    private String professor;

    @ApiModelProperty(name = "itemStatus",value = "족보 상태 판매중 or 판매정지",example = "판매중")
    private String itemStatus;

    @ApiModelProperty(name = "createdDate",value = "족보 생성일자",example = "2021-02-21")
    private LocalDate createdDate;

    public ItemResponse(Item item, Lecture lecture) {
        this.lectureName = lecture.getLectureName();
        this.title = item.getTitle();
        this.professor = lecture.getProfessor();

        if(item.getItemstatus().equals(ItemStatus.SALE)){
            itemStatus = "판매중";
        }else{
            itemStatus = "판매정지";
        }

        this.createdDate = item.getCreatedDate().toLocalDate();
    }
}
