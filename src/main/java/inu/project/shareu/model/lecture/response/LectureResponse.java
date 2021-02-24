package inu.project.shareu.model.lecture.response;

import inu.project.shareu.domain.Lecture;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("강의 조회 응답 모델")
public class LectureResponse {

    @ApiModelProperty(name = "lectureId",value = "강의 Id",example = "1")
    private Long lectureId;

    @ApiModelProperty(name = "lectureName",value = "강의명",example = "프로그래밍")
    private String lectureName;

    @ApiModelProperty(name = "professor",value = "교수명",example = "오승호")
    private String professor;

    public LectureResponse(Lecture lecture) {
        this.lectureId = lecture.getId();
        this.lectureName = lecture.getLectureName();
        this.professor = lecture.getProfessor();
    }
}
