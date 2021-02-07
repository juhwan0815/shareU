package inu.project.shareu.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Point extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "point_id")
    private Long id;

    private String pointContents;

    private int changePoint;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public static Point createPoint(String pointContents,int changePoint,
                                    Item item,Member member){
        Point point = Point.builder()
                .pointContents(pointContents)
                .changePoint(changePoint)
                .item(item)
                .member(member)
                .build();

        member.changePoint(changePoint);

        return point;
    }

}
