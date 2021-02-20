package inu.project.shareu.model.response.point;

import com.querydsl.core.annotations.QueryProjection;
import inu.project.shareu.domain.Point;
import lombok.Data;

import java.util.List;

@Data
public class PointStatusResponse {

    private int positiveSum;

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
