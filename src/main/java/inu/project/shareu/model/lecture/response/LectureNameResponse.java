package inu.project.shareu.model.lecture.response;

import inu.project.shareu.domain.Lecture;
import lombok.Data;

@Data
public class LectureNameResponse {

    private String lectureName;

    public LectureNameResponse(Lecture lecture) {
        this.lectureName = lecture.getLectureName();
    }
}
