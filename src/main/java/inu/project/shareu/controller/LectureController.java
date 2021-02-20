package inu.project.shareu.controller;

import inu.project.shareu.model.request.lecture.LectureSaveRequest;
import inu.project.shareu.model.request.lecture.LectureUpdateRequest;
import inu.project.shareu.model.response.common.SuccessListResponse;
import inu.project.shareu.model.response.common.SuccessResponse;
import inu.project.shareu.model.response.lecture.LectureNameResponse;
import inu.project.shareu.model.response.lecture.LectureProfessorResponse;
import inu.project.shareu.service.LectureService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@Api(tags = "강의")
@Slf4j
@RestController
@RequiredArgsConstructor
public class LectureController {

    private final LectureService lectureService;

    @ApiOperation(value = "(관리자) 강의 등록",notes = "(관리자) 강의 등록")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization",value = "로그인 성공 후 access_token",required = true
                    ,dataType = "String", paramType = "header")
    })
    @PostMapping("/admin/lectures")
    public ResponseEntity saveLecture(@ModelAttribute LectureSaveRequest lectureSaveRequest){
        lectureService.saveLecture(lectureSaveRequest);

        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "(관리자) 강의 수정",notes = "(관리자) 강의 수정")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization",value = "로그인 성공 후 access_token",required = true
                    ,dataType = "String", paramType = "header")
    })
    @PatchMapping("/admin/lectures/{lectureId}")
    public ResponseEntity updateLecture(@PathVariable Long lectureId,
                                        @ModelAttribute LectureUpdateRequest lectureUpdateRequest){

        lectureService.updateLecture(lectureId,lectureUpdateRequest);

        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "(관리자) 강의 삭제",notes = "(관리자) 강의 삭제")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization",value = "로그인 성공 후 access_token",required = true
                    ,dataType = "String", paramType = "header")
    })
    @DeleteMapping("/admin/lectures/{lectureId}")
    public ResponseEntity deleteLecture(@PathVariable Long lectureId){

        lectureService.deleteLecture(lectureId);

        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "(관리자) 강의 조회",notes = "(관리자) 강의 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization",value = "로그인 성공 후 access_token",required = true
                    ,dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "page",value = "페이지",required = true
                    ,dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "size",value = "페이징 사이즈",required = true
                    ,dataType = "int", paramType = "query")
    })
    @GetMapping("/admin/lectures")
    public ResponseEntity findLectures(
            @PageableDefault(size = 15, sort = "id", direction = Sort.Direction.DESC)
            @ApiIgnore Pageable pageable){
        return ResponseEntity.ok(lectureService.findPage(pageable));
    }

    @ApiOperation(value = "강의명 조회",notes = "강의명 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization",value = "로그인 성공 후 access_token",required = true
                    ,dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "전공 & 교양 Id",value = "전공 & 교양 Id",required = true
                    ,dataType = "Long", paramType = "query")
    })
    @GetMapping("/lectures/name")
    public ResponseEntity findLectureName(@RequestParam("majorId") Long majorId){

        List<LectureNameResponse> result = lectureService.findLectureNameByMajorId(majorId);

        return ResponseEntity.ok(new SuccessListResponse<>(result));
    }

    @ApiOperation(value = "강의 교수 조회",notes = "강의 교수 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization",value = "로그인 성공 후 access_token",required = true
                    ,dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "전공 & 교양 이름",value = "전공 & 교양 이름",required = true
                    ,dataType = "String", paramType = "query")
    })
    @GetMapping("/lecture/professor")
    public ResponseEntity findLectureProfessor(@RequestParam("lectureName") String lectureName){

        List<LectureProfessorResponse> result = lectureService.findLectureProfessorByLectureName(lectureName);

        return ResponseEntity.ok(new SuccessResponse<>(result));
    }
}
