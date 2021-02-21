package inu.project.shareu.service;

import inu.project.shareu.domain.College;
import inu.project.shareu.model.response.college.CollegeResponse;
import inu.project.shareu.model.response.college.CollegeWithMajorResponse;
import inu.project.shareu.repository.CollegeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CollegeService {

    private final CollegeRepository collegeRepository;

    /**
     * 단과대학 조회
     * 1. 단과대학 모두 조회
     * 2. DTO로 반환하여 변환
     * @Return List<CollegeResponse>
     */
    public List<CollegeResponse> findAll(){
        List<College> colleges = collegeRepository.findAll();
        return colleges.stream()
                .map(college -> new CollegeResponse(college))
                .collect(Collectors.toList());
    }

    public List<CollegeWithMajorResponse> findAllWithMajors(){
        List<College> colleges = collegeRepository.findALlWithMajors();
        return colleges.stream()
                .map(college -> new CollegeWithMajorResponse(college))
                .collect(Collectors.toList());
    }

}
