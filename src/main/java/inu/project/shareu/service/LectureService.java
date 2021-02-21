package inu.project.shareu.service;

import inu.project.shareu.advice.exception.LectureException;
import inu.project.shareu.advice.exception.MajorException;
import inu.project.shareu.domain.Lecture;
import inu.project.shareu.domain.Major;
import inu.project.shareu.model.lecture.request.LectureSaveRequest;
import inu.project.shareu.model.lecture.request.LectureUpdateRequest;
import inu.project.shareu.model.lecture.response.LectureNameResponse;
import inu.project.shareu.model.lecture.response.LectureProfessorResponse;
import inu.project.shareu.model.lecture.response.LectureResponse;
import inu.project.shareu.repository.LectureRepository;
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
public class LectureService {

    private final LectureRepository lectureRepository;
    private final MajorRepository majorRepository;

    /**
     * 강의 저장
     * 1. 전공 조회
     * 2. 동일 강의 존재 여부 확인
     * 3. 강의 생성 및 저장
     */
    @Transactional
    public void saveLecture(LectureSaveRequest lectureSaveRequest) {

        Major major = majorRepository.findById(lectureSaveRequest.getMajorId())
                .orElseThrow(() -> new MajorException("존재하지 않는 전공(교양)입니다."));

        validateSameLecture(lectureSaveRequest.getLectureName(),
                            lectureSaveRequest.getProfessor());

        Lecture lecture = Lecture.createLecture(lectureSaveRequest.getLectureName(),
                                                lectureSaveRequest.getProfessor(),
                                                major);
        lectureRepository.save(lecture);
    }


    /**
     * 강의 수정
     * 1. 강의 조회
     * 2. 동일 강의 존재 여부 확인
     * 3. 강의 수정
     */
    @Transactional
    public void updateLecture(Long lectureId, LectureUpdateRequest lectureUpdateRequest) {

        Lecture findLecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new LectureException("존재하지 않는 강의입니다."));

        validateSameLecture(lectureUpdateRequest.getLectureName(),
                            lectureUpdateRequest.getProfessor());

        findLecture.updateLecture(lectureUpdateRequest.getLectureName(),
                                  lectureUpdateRequest.getProfessor());
    }

    /**
     * 강의 삭제
     * 1. 강의 조회
     * 2. 강의 삭제
     */
    @Transactional
    public void deleteLecture(Long lectureId) {
        Lecture findLecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new LectureException("존재하지 않는 강의입니다"));

        lectureRepository.delete(findLecture);
    }

    /**
     * 전공으로 강의명 조회
     * 1. 전공 조회
     * 2. 강의 조회
     * 3. DTO로 변환
     * @Return List<LectureNameResponse>
     */
    public List<LectureNameResponse> findLectureNameByMajorId(Long majorId) {

        Major findMajor = majorRepository.findById(majorId)
                .orElseThrow(() -> new MajorException("존재하지 않는 전공입니다."));

        List<Lecture> findLectures = lectureRepository.findByMajor(findMajor);
        return findLectures.stream()
                .map(lecture -> new LectureNameResponse(lecture))
                .collect(Collectors.toList());
    }

    /**
     * 강의명으로 교수 조회
     * 1. 강의조회
     * 2. DTO로 변환
     * @return List<LectureProfessorResponse>
     */
    public List<LectureProfessorResponse> findLectureProfessorByLectureName(String lectureName) {

        List<Lecture> findLectures = lectureRepository.findByLectureName(lectureName);

        return findLectures.stream()
                .map(lecture -> new LectureProfessorResponse(lecture))
                .collect(Collectors.toList());
    }

    /**
     * 강의 페이징 조회
     * 1. 강의 조회
     * 2. DTO로 변환
     * @Return Page<LectureResponse>
     */
    public Page<LectureResponse> findPage(Pageable pageable) {
        Page<Lecture> lectures = lectureRepository.findAll(pageable);
        return lectures.map(lecture -> new LectureResponse(lecture));
    }

    /**
     * 동일 강의 존재 여부 확인
     */
    private void validateSameLecture(String lectureName,String professor) {
        if(lectureRepository.findByLectureNameAndProfessor(lectureName, professor).isPresent()){
            throw new LectureException("이미 존재하는 강의입니다.");
        }
    }


}
