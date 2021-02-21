package inu.project.shareu.model.item.response;

import inu.project.shareu.domain.Item;
import inu.project.shareu.model.college.response.CollegeDetailResponse;
import inu.project.shareu.model.lecture.response.LectureResponse;
import inu.project.shareu.model.major.response.MajorResponse;
import inu.project.shareu.model.store.response.StoreResponse;
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
