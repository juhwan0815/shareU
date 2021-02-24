package inu.project.shareu.model.item.response;

import inu.project.shareu.domain.Item;
import inu.project.shareu.domain.ItemStatus;
import inu.project.shareu.domain.Lecture;
import inu.project.shareu.domain.RecommendStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

@Data
@ApiModel("족보 검색 응답 모델")
public class ItemSearchResponse {

    @ApiModelProperty(name = "itemId",value = "족보 Id",example = "1")
    private Long itemId;

    @ApiModelProperty(name = "lectureName",value = "강의명",example = "프로그래밍")
    private String lectureName;

    @ApiModelProperty(name = "title",value = "족보 제목",example = "프로그래밍 족보입니다.")
    private String title;

    @ApiModelProperty(name = "itemContents",value = "족보 내용",example = "프로그래밍 족보입니다~!~!")
    private String itemContents;

    @ApiModelProperty(name = "professor",value = "교수명",example = "오승호")
    private String professor;

    @ApiModelProperty(name = "createdDate",value = "족보 생성일",example = "2021-02-21")
    private LocalDate createdDate;

    public ItemSearchResponse(Item item, Lecture lecture) {
        this.itemId = item.getId();
        this.lectureName = lecture.getLectureName();
        this.title = item.getTitle();
        this.professor = lecture.getProfessor();
        this.itemContents = item.getItemContents();
        this.createdDate = item.getCreatedDate().toLocalDate();
    }
}
