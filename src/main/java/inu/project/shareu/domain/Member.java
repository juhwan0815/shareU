package inu.project.shareu.domain;

import inu.project.shareu.advice.exception.MemberException;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(unique = true, updatable = false, nullable = false)
    private int studentNumber;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private int count;

    @Column(nullable = false)
    private int currentPoint;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Role> roles = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private MemberStatus memberStatus;

    @OneToMany(mappedBy = "member")
    private List<Cart> carts = new ArrayList<>();

    public static Member createMember(int studentNumber, String password, String name) {
        Member member = new Member();
        member.studentNumber = studentNumber;
        member.password = password;
        member.name = name;
        member.count = 3;
        member.currentPoint = 10;
        member.memberStatus = MemberStatus.ACTIVITY;
        return member;
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public void changeName(String name) {
        this.name = name;
    }

    public void changePoint(int changePoint) {

        this.currentPoint += changePoint;

        if(currentPoint < 0){
            throw new MemberException("포인트가 부족합니다.");
        }
    }

    public int getChangePoint() {
        int changePoint = 0;
        changePoint -= currentPoint;
        return changePoint;
    }

    public void changeMemberStatus() {
        this.memberStatus = MemberStatus.ACTIVITY;
        this.count = 3;
    }

    public void changeCount() {
        if (count > 0) {
            count -= 1;
        }
        if (count == 0) {
            memberStatus = MemberStatus.BLOCK;
        }
    }
}
