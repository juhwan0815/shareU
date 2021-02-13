package inu.project.shareu.model.request.lecture;

import lombok.Data;

@Data
public class LectureSaveRequest {

    private Long majorId;

    private String lectureName;

    private String professor;

}
