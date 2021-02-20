package inu.project.shareu.model.response.college;

import inu.project.shareu.domain.College;
import lombok.Data;

@Data
public class CollegeResponse {

    private Long collegeId;

    private String collegeName;

    public CollegeResponse(College college) {
        this.collegeId = college.getId();
        this.collegeName = college.getName();
    }
}
