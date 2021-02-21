package inu.project.shareu.model.lecture.request;

import lombok.Data;

@Data
public class LectureSaveRequest {

    private Long majorId;

    private String lectureName;

    private String professor;

}
