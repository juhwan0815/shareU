package inu.project.shareu.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long id;

    private String roleName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public static Role createRole(){
        Role role = new Role();
        role.roleName = "ROLE_MEMBER";
        return role;
    }

    public static Role createAdminRole(){
        Role role = new Role();
        role.roleName = "ROLE_ADMIN";
        return role;
    }

    public void giveRoleToMember(Member member){
        member.getRoles().add(this);
        this.member = member;
    }


}
