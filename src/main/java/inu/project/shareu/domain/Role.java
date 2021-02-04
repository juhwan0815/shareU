package inu.project.shareu.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
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
        Role role = Role.builder()
                .roleName("ROLE_MEMBER")
                .build();
        return role;
    }

    public void giveRoleToMember(Member member){
        member.getRoles().add(this);
        this.member = member;
    }


}
