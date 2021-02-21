package inu.project.shareu.model.response.college;

import inu.project.shareu.domain.College;
import inu.project.shareu.domain.CollegeStatus;
import lombok.Data;

@Data
public class CollegeDetailResponse {

    private Long collegeId;

    private String collegeName;

    private String collegeStatus;

    public CollegeDetailResponse(College college) {
        this.collegeId = college.getId();
        this.collegeName = college.getName();
        if(college.getStatus().equals(CollegeStatus.전공)){
            collegeStatus = "전공";
        }else{
            collegeStatus = "교양";
        }

    }
}
