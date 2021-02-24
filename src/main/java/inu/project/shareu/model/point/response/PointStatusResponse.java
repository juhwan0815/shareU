package inu.project.shareu.model.point.response;

import com.querydsl.core.annotations.QueryProjection;
import inu.project.shareu.domain.Point;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("포인트 현황 조회 응답 모델")
public class PointStatusResponse {

    @ApiModelProperty(name = "positiveSum",value = "포인트 변경 양수 합계",example = "100")
    private int positiveSum;

    @ApiModelProperty(name = "negativeSum",value = "포인트 변경 음수 합계",example = "-100")
    private int negativeSum;

    public PointStatusResponse(List<Point> points) {

        positiveSum = 0;
        negativeSum = 0;

        points.forEach(point -> {
            if(point.getChangePoint() < 0){
                negativeSum += point.getChangePoint();
            }else{
                positiveSum += point.getChangePoint();
            }
        });
    }
}
