package inu.project.shareu.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Lecture extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lecture_id")
    private Long id;

    @Column(nullable = false)
    private String lectureName;

    @Column(nullable = false)
    private String professor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "major_id")
    private Major major;
    
    public static Lecture createLecture(String lectureName,String professor,Major major){
        Lecture lecture = new Lecture();
        lecture.lectureName = lectureName;
        lecture.professor = professor;
        lecture.major = major;
        return lecture;
    }

    public void updateLecture(String lectureName, String professor) {
        this.lectureName = lectureName;
        this.professor = professor;
    }
}
