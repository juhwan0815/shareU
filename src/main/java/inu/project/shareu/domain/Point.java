package inu.project.shareu.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    public static Point
    createPoint(String pointContents,int changePoint,
                                    Item item,Member member){
        Point point = new Point();
        point.pointContents = pointContents;
        point.changePoint = changePoint;
        point.item = item;
        point.member = member;

        member.changePoint(changePoint);

        return point;
    }

}
