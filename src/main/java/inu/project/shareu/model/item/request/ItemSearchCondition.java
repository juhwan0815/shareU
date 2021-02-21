package inu.project.shareu.model.item.request;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ItemSearchCondition {

    private String title;

    private String professor;

    private String lectureName;

    @ApiModelProperty(value = "전공 교양 여부")
    private String collegeStatus;

    private Long majorId;

    private String orderBy;

}
