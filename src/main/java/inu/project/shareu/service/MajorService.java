package inu.project.shareu.service;

import inu.project.shareu.advice.exception.MajorException;
import inu.project.shareu.domain.Major;
import inu.project.shareu.model.request.major.MajorSaveRequest;
import inu.project.shareu.model.request.major.MajorUpdateRequest;
import inu.project.shareu.repository.MajorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MajorService {

    private final MajorRepository majorRepository;

    @Transactional
    public void saveMajor(MajorSaveRequest majorSaveRequest) {
        if(majorRepository.findByMajorName(majorSaveRequest.getMajorName()).isPresent()){
            throw new MajorException("이미 존재하는 학과입니다.");
        }

        Major major = Major.createMajor(majorSaveRequest.getMajorName());

        majorRepository.save(major);
    }

    @Transactional
    public void updateMajor(Long id, MajorUpdateRequest majorUpdateRequest) {
        Major major = majorRepository.findById(id)
                .orElseThrow(() -> new MajorException("존재하지 않는 학과입니다."));

        if(majorRepository.findByMajorName(majorUpdateRequest.getMajorName()).isPresent()){
            throw new MajorException("이미 존재하는 학과입니다.");
        }

        major.changeMajorName(majorUpdateRequest.getMajorName());
    }


    @Transactional
    public void deleteMajor(Long id) {
        Major major = majorRepository.findById(id)
                .orElseThrow(() -> new MajorException("존재하지 않는 학과입니다."));

        majorRepository.delete(major);
    }
}
