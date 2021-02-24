package inu.project.shareu.model.item.request;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ItemSearchCondition {

    @ApiModelProperty(name = "title",value = "족보 제목")
    private String title;

    @ApiModelProperty(name = "professor",value = "교수명")
    private String professor;

    @ApiModelProperty(name = "lectureName",value = "강의명")
    private String lectureName;

    @ApiModelProperty(name = "collegeStatus",value = "전공 or 교양")
    private String collegeStatus;

    @ApiModelProperty(name = "majorId",value = "전공 Id")
    private Long majorId;

    @ApiModelProperty(name = "orderBy",value = "정렬 기준 (추천순 or 최신순)")
    private String orderBy;

}
