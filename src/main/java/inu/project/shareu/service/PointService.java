package inu.project.shareu.service;

import inu.project.shareu.domain.Member;
import inu.project.shareu.domain.Point;
import inu.project.shareu.model.point.response.PointResponse;
import inu.project.shareu.model.point.response.PointStatusResponse;
import inu.project.shareu.repository.PointRepository;
import inu.project.shareu.repository.query.PointQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PointService {

    private final PointQueryRepository pointQueryRepository;
    private final PointRepository pointRepository;

    /**
     * 나의 포인트이력 페이징 조회
     * 1. 포인트이력 페이징 조회
     * 2. DTO로 변환하여 반환
     * @return Page<PointResponse>
     */
    public Page<PointResponse> findMyPoints(Member member, Pageable pageable) {
        Page<Point> points = pointQueryRepository.findPageWithItemByMember(member, pageable);
        return points.map(point -> new PointResponse(point,point.getItem()));
    }

    public PointStatusResponse findMyPointStatus(Member member) {
        List<Point> points = pointRepository.findByMember(member);
        return new PointStatusResponse(points);
    }
}
