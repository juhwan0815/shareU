package inu.project.shareu.model.major.response;

import inu.project.shareu.domain.Major;
import lombok.Data;

@Data
public class MajorResponse {

    private Long majorId;

    private String majorName;

    public MajorResponse(Major major) {
        this.majorId = major.getId();
        this.majorName = major.getMajorName();
    }
}
