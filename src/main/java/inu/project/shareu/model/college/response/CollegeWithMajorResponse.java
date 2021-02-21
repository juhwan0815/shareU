package inu.project.shareu.model.college.response;

import inu.project.shareu.domain.College;
import inu.project.shareu.model.major.response.MajorResponse;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class CollegeWithMajorResponse {

    private Long collegeId;

    private String collegeName;

    private List<MajorResponse> majors;

    public CollegeWithMajorResponse(College college) {
        this.collegeId = college.getId();
        this.collegeName = college.getName();
        this.majors = college.getMajors().stream()
                    .map(major -> new MajorResponse(major))
                    .collect(Collectors.toList());
    }
}
