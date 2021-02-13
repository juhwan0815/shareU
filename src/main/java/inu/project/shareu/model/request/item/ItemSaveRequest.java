package inu.project.shareu.model.request.item;

import lombok.Data;

@Data
public class ItemSaveRequest {

    private Long lectureId;

    private String title;

    private String itemContents;

}
