package inu.project.shareu.model.response.item;

import inu.project.shareu.domain.Item;
import inu.project.shareu.model.response.college.CollegeDetailResponse;
import inu.project.shareu.model.response.lecture.LectureResponse;
import inu.project.shareu.model.response.major.MajorResponse;
import inu.project.shareu.model.response.store.StoreResponse;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ItemDetailResponse {

    private Long itemId;

    private String title;

    private String itemContents;

    private int recommend;

    private int notRecommend;

    private LocalDate localDate;

    private MajorResponse major;

    private LectureResponse lecture;

    private CollegeDetailResponse college;

    private List<StoreResponse> storeList;

    public ItemDetailResponse(Item item) {
        this.itemId = item.getId();
        this.title = item.getTitle();
        this.itemContents = item.getItemContents();
        this.recommend = item.getRecommend();
        this.notRecommend = item.getNotRecommend();
        this.localDate = item.getLastModifiedDate().toLocalDate();
        this.major = new MajorResponse(item.getMajor());
        this.college = new CollegeDetailResponse(item.getMajor().getCollege());
        this.lecture = new LectureResponse(item.getLecture());
        this.storeList = item.getStoreList().stream()
                        .map(store -> new StoreResponse(store))
                        .collect(Collectors.toList());
    }
}
