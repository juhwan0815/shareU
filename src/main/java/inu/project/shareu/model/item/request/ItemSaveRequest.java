package inu.project.shareu.model.item.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@Data
@ApiModel("족보 등록 요청 모델")
public class ItemSaveRequest {

    @ApiModelProperty(name = "lectureId",value = "강의 Id",required = true)
    @NotNull(message = "강의 Id는 필수값입니다.")
    @Positive(message = "강의 Id는 양수만 가능합니다.")
    private Long lectureId;

    @ApiModelProperty(name = "title",value = "족보명",required = true)
    @NotBlank(message = "족보 제목은 필수입니다.")
    private String title;

    @ApiModelProperty(name = "itemContents",value = "족보 내용",required = true)
    @NotBlank(message = "족보 내용은 필수입니다.")
    private String itemContents;

    @ApiModelProperty(name = "files",value = "족보 파일 (포스트맨으로 테스트)",required = true)
    private List<MultipartFile> files;


}
