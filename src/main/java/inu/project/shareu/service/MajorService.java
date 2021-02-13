package inu.project.shareu.service;

import inu.project.shareu.advice.exception.CollegeException;
import inu.project.shareu.advice.exception.MajorException;
import inu.project.shareu.domain.College;
import inu.project.shareu.domain.Major;
import inu.project.shareu.model.request.major.MajorSaveRequest;
import inu.project.shareu.model.request.major.MajorUpdateRequest;
import inu.project.shareu.repository.CollegeRepository;
import inu.project.shareu.repository.MajorRepository;
import javassist.compiler.CompileError;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MajorService {

    private final MajorRepository majorRepository;
    private final CollegeRepository collegeRepository;

    @Transactional
    public void saveMajor(MajorSaveRequest majorSaveRequest) {

        College college = collegeRepository.findById(majorSaveRequest.getCollegeId())
                .orElseThrow(() -> new CollegeException("존재하지 않는 카테고리입니다."));

        if(majorRepository.findByMajorName(majorSaveRequest.getMajorName()).isPresent()){
            throw new MajorException("이미 존재하는 학과입니다.");
        }

        Major major = Major.createMajor(majorSaveRequest.getMajorName(),college);

        majorRepository.save(major);
    }

    @Transactional
    public void updateMajor(Long majorId, MajorUpdateRequest majorUpdateRequest) {
        Major major = majorRepository.findById(majorId)
                .orElseThrow(() -> new MajorException("존재하지 않는 학과입니다."));

        if(majorRepository.findByMajorName(majorUpdateRequest.getMajorName()).isPresent()){
            throw new MajorException("이미 존재하는 학과입니다.");
        }

        major.changeMajorName(majorUpdateRequest.getMajorName());
    }

    @Transactional
    public void deleteMajor(Long majorId) {
        Major major = majorRepository.findById(majorId)
                .orElseThrow(() -> new MajorException("존재하지 않는 학과입니다."));

        majorRepository.delete(major);
    }
}
