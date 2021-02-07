package inu.project.shareu.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(unique = true,updatable = false,nullable = false)
    private int studentNumber;

    @Column(nullable = false)
    private String password;

    @Column(unique = true,nullable = false)
    private String name;

    @Column(nullable = false)
    private int currentPoint;

    @OneToMany(mappedBy = "member",cascade = CascadeType.ALL)
    private List<Role> roles = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private MemberStatus memberStatus;

    @OneToMany(mappedBy = "member")
    private List<Cart> carts = new ArrayList<>();


    public Member(int studentNumber, String password, String name) {
        this.studentNumber = studentNumber;
        this.password = password;
        this.name = name;
        this.currentPoint = 10;
        this.memberStatus = MemberStatus.ACTIVITY;
    }

    public static Member createMember(int studentNumber, String password, String name){
        Member member = new Member(studentNumber,password,name);
        return member;
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public void changeName(String name){
        this.name = name;
    }

    public void changePoint(int changePoint) {
        this.currentPoint += changePoint;
    }
}
