package inu.project.shareu.model.lecture.response;

import inu.project.shareu.domain.Lecture;
import lombok.Data;

@Data
public class LectureResponse {

    private Long id;

    private String lectureName;

    private String professor;

    public LectureResponse(Lecture lecture) {
        this.id = lecture.getId();
        this.lectureName = lecture.getLectureName();
        this.professor = lecture.getProfessor();
    }
}
