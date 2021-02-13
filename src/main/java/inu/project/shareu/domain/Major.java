package inu.project.shareu.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Major{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "major_id")
    private Long id;

    @Column(nullable = false,unique = true)
    private String majorName;

    @ManyToOne
    @JoinColumn(name = "college_id")
    private College college;

    public static Major createMajor(String majorName,College college){
        Major major = new Major();
        major.majorName = majorName;
        major.college = college;
        return major;
    }

    public void changeMajorName(String majorName) {
        this.majorName = majorName;
    }
}
