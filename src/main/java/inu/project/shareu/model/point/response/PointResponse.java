package inu.project.shareu.model.point.response;

import inu.project.shareu.domain.Item;
import inu.project.shareu.domain.Point;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PointResponse {

    private Long pointId;

    private Long itemId;

    private String title;

    private String pointContents;

    private int changePoint;

    private LocalDate localDate;

    public PointResponse(Point point, Item item) {
        this.pointId = point.getId();
        this.itemId = item.getId();
        this.title = item.getTitle();
        this.pointContents = point.getPointContents();
        this.changePoint = point.getChangePoint();
        this.localDate = point.getCreatedDate().toLocalDate();
    }
}
