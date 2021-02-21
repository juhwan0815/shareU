package inu.project.shareu.service;

import inu.project.shareu.advice.exception.CollegeException;
import inu.project.shareu.advice.exception.MajorException;
import inu.project.shareu.domain.College;
import inu.project.shareu.domain.Major;
import inu.project.shareu.model.major.request.MajorSaveRequest;
import inu.project.shareu.model.major.request.MajorUpdateRequest;
import inu.project.shareu.model.major.response.MajorResponse;
import inu.project.shareu.repository.CollegeRepository;
import inu.project.shareu.repository.MajorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MajorService {

    private final MajorRepository majorRepository;
    private final CollegeRepository collegeRepository;

    /**
     * 전공 저장
     * 1. 단과대학 조회
     * 2. 동일 전공 존재 여부 확인
     * 3. 전공 생성 및 저장
     */
    @Transactional
    public void saveMajor(MajorSaveRequest majorSaveRequest) {

        College college = collegeRepository.findById(majorSaveRequest.getCollegeId())
                .orElseThrow(() -> new CollegeException("존재하지 않는 카테고리입니다."));

        validateDuplicateMajor(majorSaveRequest.getMajorName());

        Major major = Major.createMajor(majorSaveRequest.getMajorName(), college);
        majorRepository.save(major);
    }

    /**
     * 전공 수정
     * 1. 전공 조회
     * 2. 변경 예정 전공 존재 여부 확인
     * 3. 전공 수정
     */
    @Transactional
    public void updateMajor(Long majorId, MajorUpdateRequest majorUpdateRequest) {

        Major major = majorRepository.findById(majorId)
                .orElseThrow(() -> new MajorException("존재하지 않는 학과입니다."));

        validateDuplicateMajor(majorUpdateRequest.getMajorName());

        major.changeMajorName(majorUpdateRequest.getMajorName());
    }

    /**
     * 전공 삭제
     * 1. 전공 조회
     * 2. 전공 삭제
     */
    @Transactional
    public void deleteMajor(Long majorId) {

        Major major = majorRepository.findById(majorId)
                .orElseThrow(() -> new MajorException("존재하지 않는 학과입니다."));

        majorRepository.delete(major);
    }

    /**
     * 단과대학 소속 전공 조회
     * 1. 단과대학 조회
     * 2. 단과대학 소속 전공 조회
     * 3. DTO로 변환
     * @Return List<MajorResponse>
     */
    public List<MajorResponse> findMajorsByCollegeId(Long collegeId) {

        College findCollege = collegeRepository.findById(collegeId)
                .orElseThrow(() -> new CollegeException("존재하지 않는 단과대학입니다."));

        List<Major> findMajors = majorRepository.findByCollege(findCollege);

        return findMajors.stream()
                .map(major -> new MajorResponse(major))
                .collect(Collectors.toList());

    }

    /**
     * 전공 페이징 조회
     * 1. 전공 페이징 조회
     * 2. DTO로 변환하여 반환
     * @Return Page<MajorResponse>
     */
    public Page<MajorResponse> findMajors(Pageable pageable) {
        Page<Major> majors = majorRepository.findAll(pageable);
        return majors.map(major -> new MajorResponse(major));
    }

    /**
     * 동일 전공 존재 여부 확인
     */
    private void validateDuplicateMajor(String majorName) {

        if (majorRepository.findByMajorName(majorName).isPresent()) {
            throw new MajorException("이미 존재하는 전공입니다.");
        }
    }
}
