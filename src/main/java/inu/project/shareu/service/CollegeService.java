package inu.project.shareu.service;

import inu.project.shareu.domain.College;
import inu.project.shareu.model.response.college.CollegeResponse;
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

    public List<CollegeResponse> findAll(){
        List<College> colleges = collegeRepository.findAll();
        return colleges.stream()
                .map(college -> new CollegeResponse(college))
                .collect(Collectors.toList());
    }
}
