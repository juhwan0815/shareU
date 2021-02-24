package inu.project.shareu.model.item.response;

import inu.project.shareu.domain.Item;
import inu.project.shareu.model.college.response.CollegeDetailResponse;
import inu.project.shareu.model.lecture.response.LectureResponse;
import inu.project.shareu.model.major.response.MajorResponse;
import inu.project.shareu.model.store.response.StoreResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Data
@ApiModel("족보 상세 조회 응답 모델")
public class ItemDetailResponse {

    @ApiModelProperty(name = "itemId",value = "족보 Id",example = "1")
    private Long itemId;

    @ApiModelProperty(name = "title",value = "족보 제목",example = "프로그래밍 족보입니다~")
    private String title;

    @ApiModelProperty(name = "itemContents",value = "족보 내용",example = "프로그래밍 족보입니다!")
    private String itemContents;

    @ApiModelProperty(name = "recommned",value = "추천 수",example = "100")
    private int recommend;

    @ApiModelProperty(name = "notRecommend",value = "비추천 수",example = "100")
    private int notRecommend;

    @ApiModelProperty(name = "createdDate",value = "족보 생성일",example = "2021-02-21")
    private LocalDate createdDate;

    @ApiModelProperty(name = "major",value = "전공")
    private MajorResponse major;

    @ApiModelProperty(name = "lecture",value = "강의")
    private LectureResponse lecture;

    @ApiModelProperty(name = "college",value = "단과대학")
    private CollegeDetailResponse college;

    @ApiModelProperty(name = "storeList",value = "족보 파일")
    private List<StoreResponse> storeList;

    public ItemDetailResponse(Item item) {
        this.itemId = item.getId();
        this.title = item.getTitle();
        this.itemContents = item.getItemContents();
        this.recommend = item.getRecommend();
        this.notRecommend = item.getNotRecommend();
        this.createdDate = item.getLastModifiedDate().toLocalDate();
        this.major = new MajorResponse(item.getMajor());
        this.college = new CollegeDetailResponse(item.getMajor().getCollege());
        this.lecture = new LectureResponse(item.getLecture());
        this.storeList = item.getStoreList().stream()
                        .map(store -> new StoreResponse(store))
                        .collect(Collectors.toList());
    }
}
