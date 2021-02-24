package inu.project.shareu.model.store.response;

import inu.project.shareu.domain.Store;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("파일 응답 모델")
public class StoreResponse {

    @ApiModelProperty(name = "storeId",value = "파일 Id",example = "1")
    private Long storeId;

    @ApiModelProperty(name = "fileOriginalName",value = "파일 오리지널 이름",example = "프로그래밍족보.hwp")
    private String fileOriginalName;

    @ApiModelProperty(name = "fileStoreName",value = "파일 저장 이름",example = "asdfasdasfd프로그래밍족보.hwp")
    private String fileStoreName;

    public StoreResponse(Store store) {
        this.storeId = store.getId();
        this.fileOriginalName = store.getFileOriginalName();
        this.fileStoreName = store.getFileStoreName();
    }
}
