package inu.project.shareu.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Item extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    private String title;

    private String itemContents;

    @Enumerated(EnumType.STRING)
    private ItemStatus itemstatus;

    private String className;

    private String professor;

    private int price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "major_id")
    private Major major;

    @OneToMany(mappedBy = "item")
    private List<Store> storeList = new ArrayList<>();

    public static Item createItem(String title,String itemContents,
                                  String className,String professor,
                                  Member member,Major major){
        Item item = Item.builder()
                .title(title)
                .itemContents(itemContents)
                .className(className)
                .professor(professor)
                .itemstatus(ItemStatus.SALE)
                .price(3)
                .member(member)
                .major(major)
                .build();
        return item;
    }

    public void updateItem(String title,String itemContents,
                           String className,String professor){
        this.title = title;
        this.itemContents = itemContents;
        this.className = className;
        this.professor = professor;
    }

    public void deleteItem(){
        this.itemstatus = ItemStatus.STOP;
    }

}
