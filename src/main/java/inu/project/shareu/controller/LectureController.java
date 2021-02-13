package inu.project.shareu.controller;

import inu.project.shareu.model.request.lecture.LectureSaveRequest;
import inu.project.shareu.model.request.lecture.LectureUpdateRequest;
import inu.project.shareu.service.LectureService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(tags = "7.강의")
@Slf4j
@RestController
@RequiredArgsConstructor
public class LectureController {

    private final LectureService lectureService;

    @ApiOperation(value = "강의 등록",notes = "강의 등록")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization",value = "로그인 성공 후 access_token",required = true
                    ,dataType = "String", paramType = "header")
    })
    @PostMapping("/lectures")
    public ResponseEntity saveLecture(@ModelAttribute LectureSaveRequest lectureSaveRequest){
        lectureService.saveLecture(lectureSaveRequest);

        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "강의 수정",notes = "강의 수정")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization",value = "로그인 성공 후 access_token",required = true
                    ,dataType = "String", paramType = "header")
    })
    @PatchMapping("/lectures/{lectureId}")
    public ResponseEntity updateLecture(@PathVariable Long lectureId,
                                        @ModelAttribute LectureUpdateRequest lectureUpdateRequest){

        lectureService.updateLecture(lectureId,lectureUpdateRequest);

        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "강의 삭제",notes = "강의 삭제")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization",value = "로그인 성공 후 access_token",required = true
                    ,dataType = "String", paramType = "header")
    })
    @DeleteMapping("/lectures/{lectureId}")
    public ResponseEntity deleteLecture(@PathVariable Long lectureId){

        lectureService.deleteLecture(lectureId);

        return ResponseEntity.ok().build();
    }


}
