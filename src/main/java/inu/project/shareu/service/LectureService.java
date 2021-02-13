package inu.project.shareu.service;

import inu.project.shareu.advice.exception.LectureException;
import inu.project.shareu.advice.exception.MajorException;
import inu.project.shareu.domain.Lecture;
import inu.project.shareu.domain.Major;
import inu.project.shareu.model.request.lecture.LectureSaveRequest;
import inu.project.shareu.model.request.lecture.LectureUpdateRequest;
import inu.project.shareu.repository.LectureRepository;
import inu.project.shareu.repository.MajorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LectureService {

    private final LectureRepository lectureRepository;
    private final MajorRepository majorRepository;

    @Transactional
    public void saveLecture(LectureSaveRequest lectureSaveRequest) {

        Major major = majorRepository.findById(lectureSaveRequest.getMajorId())
                .orElseThrow(() -> new MajorException("존재하지 않는 전공(교양)입니다."));

        if(lectureRepository.findByLectureNameAndProfessor(lectureSaveRequest.getLectureName(),
                lectureSaveRequest.getProfessor()).isPresent()){
            throw new LectureException("이미 존재하는 강의입니다.");
        }

        Lecture lecture = Lecture.createLecture(lectureSaveRequest.getLectureName(),
                lectureSaveRequest.getProfessor(),
                major);

        lectureRepository.save(lecture);
    }

    @Transactional
    public void updateLecture(Long lectureId, LectureUpdateRequest lectureUpdateRequest) {
        Lecture findLecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new LectureException("존재하지 않는 강의입니다."));

        findLecture.updateLecture(lectureUpdateRequest.getLectureName(),
                lectureUpdateRequest.getProfessor());
    }

    @Transactional
    public void deleteLecture(Long lectureId) {
        Lecture findLecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new LectureException("존재하지 않는 강의입니다"));

        lectureRepository.delete(findLecture);
    }


}
