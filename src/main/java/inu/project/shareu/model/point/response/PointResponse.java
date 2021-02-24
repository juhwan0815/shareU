package inu.project.shareu.model.point.response;

import inu.project.shareu.domain.Item;
import inu.project.shareu.domain.Point;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

@Data
@ApiModel("포인트 이력 조회 응답 모델")
public class PointResponse {

    @ApiModelProperty(name = "pointId",value = "포인트 Id",example = "1")
    private Long pointId;

    @ApiModelProperty(name = "itemId",value = "족보 Id",example = "1")
    private Long itemId;

    @ApiModelProperty(name = "title",value = "족보 제목",example = "프로그래밍 족보입니다.")
    private String title;

    @ApiModelProperty(name = "pointContent",value = "포인트 변경 내용",example = "족보 등록")
    private String pointContents;

    @ApiModelProperty(name = "changePoint",value = "변경 포인트",example = "3")
    private int changePoint;

    @ApiModelProperty(name = "createdDate",value = "포인트 이력 생성일",example = "2021-02-21")
    private LocalDate createdDate;

    public PointResponse(Point point, Item item) {
        this.pointId = point.getId();
        this.itemId = item.getId();
        this.title = item.getTitle();
        this.pointContents = point.getPointContents();
        this.changePoint = point.getChangePoint();
        this.createdDate = point.getCreatedDate().toLocalDate();
    }
}
