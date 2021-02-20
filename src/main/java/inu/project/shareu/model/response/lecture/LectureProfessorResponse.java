package inu.project.shareu.model.response.lecture;

import inu.project.shareu.domain.Lecture;
import lombok.Data;

@Data
public class LectureProfessorResponse {

    private Long lectureId;

    private String professor;

    public LectureProfessorResponse(Lecture lecture) {
        this.lectureId = lecture.getId();
        this.professor = lecture.getProfessor();
    }
}
