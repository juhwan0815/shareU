package inu.project.shareu.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class College {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "college_id")
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private CollegeStatus status;

    @OneToMany(mappedBy = "college")
    private List<Major> majors = new ArrayList<>();

    public static College createCollege(String name,CollegeStatus status){
        College college = new College();
        college.name = name;
        college.status = status;
        return college;
    }

}
