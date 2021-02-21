package inu.project.shareu.model.item.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class ItemSaveRequest {

    private Long lectureId;

    private String title;

    private String itemContents;

    private List<MultipartFile> files;

}
