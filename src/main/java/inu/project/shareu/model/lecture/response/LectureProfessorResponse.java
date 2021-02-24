package inu.project.shareu.model.lecture.response;

import inu.project.shareu.domain.Lecture;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("강의 교수 조회 응답 모델")
public class LectureProfessorResponse {

    @ApiModelProperty(name = "lectureId",value = "강의 Id",example = "1")
    private Long lectureId;

    @ApiModelProperty(name = "professor",value = "교수명",example = "오승호")
    private String professor;

    public LectureProfessorResponse(Lecture lecture) {
        this.lectureId = lecture.getId();
        this.professor = lecture.getProfessor();
    }
}
