package inu.project.shareu.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    private String reviewContents;

    @Enumerated(EnumType.STRING)
    private ReviewStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public Review(String reviewContents,Boolean recommend,Item item, Member member) {
        this.reviewContents = reviewContents;
        this.item = item;
        this.member = member;

        if(recommend){
            this.status = ReviewStatus.GOOD;
        }else{
            this.status = ReviewStatus.BAD;
        }

    }

    public static Review createReview(String reviewContents, Boolean recommend,
                                      Item item, Member member){
        Review review = new Review(reviewContents,recommend,item,member);
        return review;
    }


    public void updateReview(String reviewContents, Boolean recommend) {

        if(recommend){
            this.status = ReviewStatus.GOOD;
        }else{
            this.status = ReviewStatus.BAD;
        }

        this.reviewContents = reviewContents;
    }
}
