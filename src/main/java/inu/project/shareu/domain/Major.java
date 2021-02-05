package inu.project.shareu.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Major extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "major_id")
    private Long id;

    @Column(nullable = false,unique = true)
    private String majorName;

    public static Major createMajor(String majorName){
        Major major = Major.builder()
                .majorName(majorName)
                .build();
        return major;
    }

    public void changeMajorName(String majorName) {
        this.majorName = majorName;
    }
}
