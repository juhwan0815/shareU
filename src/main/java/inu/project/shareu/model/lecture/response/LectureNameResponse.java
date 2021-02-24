package inu.project.shareu.model.lecture.response;

import inu.project.shareu.domain.Lecture;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("강의명 조회 응답 모델")
public class LectureNameResponse {

    @ApiModelProperty(name = "lectureName",value = "강의명",example = "프로그래밍")
    private String lectureName;

    public LectureNameResponse(Lecture lecture) {
        this.lectureName = lecture.getLectureName();
    }
}
