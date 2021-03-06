package inu.project.shareu.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    private String title;

    private String itemContents;

    private int recommend;

    private int notRecommend;

    @Enumerated(EnumType.STRING)
    private ItemStatus itemstatus;

    private int price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "major_id")
    private Major major;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id")
    private Lecture lecture;

    @OneToMany(mappedBy = "item")
    public List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "item")
    private List<Store> storeList = new ArrayList<>();

    public static Item createItem(String title,String itemContents,Lecture lecture,
                                  Member member,Major major){
        Item item = new Item();
        item.title = title;
        item.itemContents = itemContents;
        item.lecture = lecture ;
        item.itemstatus = ItemStatus.SALE;
        item.recommend = 0;
        item.notRecommend = 0;
        item.price = 3;
        item.major = major;
        item.member = member;

        member.changePossibleCount();

        return item;
    }

    public void updateItem(String title,String itemContents){
        this.title = title;
        this.itemContents = itemContents;
    }

    public void deleteItem(){
        this.itemstatus = ItemStatus.STOP;
    }

    public int deleteItemByAdmin() {
        this.itemstatus = ItemStatus.STOP;
        member.changeCount();
        int changePoint = member.getChangePoint();
        return changePoint;
    }

    public void plusRecommend() {
        this.recommend += 1;
    }

    public void plusNotRecommend() {
        this.notRecommend += 1;
    }

    public void minusNotRecommend() {
        this.notRecommend -= 1;
    }

    public void minusRecommend() {
        this.recommend -= 1;
    }
}
