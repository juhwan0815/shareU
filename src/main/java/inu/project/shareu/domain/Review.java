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
    private RecommendStatus recommendStatus;

    @Enumerated(EnumType.STRING)
    private ReviewStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public static Review createReview(String reviewContents, Boolean recommend,
                                      Item item, Member member){
        Review review = new Review();
        review.reviewContents =reviewContents;
        review.item = item;
        review.member = member;
        review.status = ReviewStatus.LIVE;


        if(recommend){
            review.recommendStatus = RecommendStatus.GOOD;
            item.plusRecommend();
        }else {
            review.recommendStatus = RecommendStatus.BAD;
            item.plusNotRecommend();
        }

        return review;
    }


    public void updateReview(String reviewContents, Boolean recommend) {

        if(recommend){
            if(recommendStatus.equals(RecommendStatus.BAD)){
                item.plusRecommend();
                item.minusNotRecommend();
            }
            this.recommendStatus = RecommendStatus.GOOD;
        }else{
            if(recommendStatus.equals(RecommendStatus.GOOD)){
                item.minusRecommend();
                item.plusNotRecommend();
            }
            this.recommendStatus = RecommendStatus.BAD;
        }

        this.reviewContents = reviewContents;
    }

    public void changeStatus() {
        this.status = ReviewStatus.DELETE;

        if(recommendStatus.equals(RecommendStatus.BAD)){
            item.minusNotRecommend();
        }else{
            item.minusRecommend();
        }

    }
}
