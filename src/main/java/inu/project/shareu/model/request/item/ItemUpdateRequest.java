package inu.project.shareu.model.request.item;

import lombok.Data;

@Data
public class ItemUpdateRequest {

    private String title;

    private String itemContents;

    private String className;

    private String professor;
}
