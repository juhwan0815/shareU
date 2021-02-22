package inu.project.shareu.controller;

import inu.project.shareu.model.common.response.ExceptionResponse;
import inu.project.shareu.model.lecture.request.LectureSaveRequest;
import inu.project.shareu.model.lecture.request.LectureUpdateRequest;
import inu.project.shareu.model.lecture.response.LectureNameResponse;
import inu.project.shareu.model.lecture.response.LectureProfessorResponse;
import inu.project.shareu.model.lecture.response.LectureResponse;
import inu.project.shareu.service.LectureService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
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
            @ApiImplicitParam(name = "Authorization",value = "로그인 성공 후 access_token",required = true,
                    dataType = "String", paramType = "header")
    })
    @ApiResponses({
            @ApiResponse(code = 400, message = "BAD REQUEST",response = ExceptionResponse.class),
            @ApiResponse(code = 401, message = "UNAUTHORIZED", response = ExceptionResponse.class),
            @ApiResponse(code = 403, message = "FORBIDDEN", response = ExceptionResponse.class),
            @ApiResponse(code = 500, message = "INTERNAL_SERVER_ERROR", response = ExceptionResponse.class)
    })
    @PostMapping("/admin/lectures")
    public ResponseEntity<Void> saveLecture(@ModelAttribute LectureSaveRequest lectureSaveRequest){

        lectureService.saveLecture(lectureSaveRequest);

        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "(관리자) 강의 수정",notes = "(관리자) 강의 수정")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization",value = "로그인 성공 후 access_token",required = true,
                    dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "lectureId",value = "강의 Id",required = true,
                    dataType = "long",paramType = "path")
    })
    @ApiResponses({
            @ApiResponse(code = 400, message = "BAD REQUEST",response = ExceptionResponse.class),
            @ApiResponse(code = 401, message = "UNAUTHORIZED", response = ExceptionResponse.class),
            @ApiResponse(code = 403, message = "FORBIDDEN", response = ExceptionResponse.class),
            @ApiResponse(code = 500, message = "INTERNAL_SERVER_ERROR", response = ExceptionResponse.class)
    })
    @PatchMapping("/admin/lectures/{lectureId}")
    public ResponseEntity<Void> updateLecture(@PathVariable Long lectureId,
                                              @ModelAttribute LectureUpdateRequest lectureUpdateRequest){

        lectureService.updateLecture(lectureId,lectureUpdateRequest);

        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "(관리자) 강의 삭제",notes = "(관리자) 강의 삭제")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization",value = "로그인 성공 후 access_token",required = true,
                    dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "lectureId",value = "강의 Id",required = true,
                    dataType = "long",paramType = "path")
    })
    @ApiResponses({
            @ApiResponse(code = 400, message = "BAD REQUEST",response = ExceptionResponse.class),
            @ApiResponse(code = 401, message = "UNAUTHORIZED", response = ExceptionResponse.class),
            @ApiResponse(code = 403, message = "FORBIDDEN", response = ExceptionResponse.class),
            @ApiResponse(code = 500, message = "INTERNAL_SERVER_ERROR", response = ExceptionResponse.class)
    })
    @DeleteMapping("/admin/lectures/{lectureId}")
    public ResponseEntity<Void> deleteLecture(@PathVariable Long lectureId){

        lectureService.deleteLecture(lectureId);

        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "(관리자) 강의 조회",notes = "(관리자) 강의 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization",value = "로그인 성공 후 access_token",required = true,
                    dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "page",value = "페이지",required = true,
                    dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "size",value = "페이징 사이즈",required = true,
                    dataType = "int", paramType = "query")
    })
    @ApiResponses({
            @ApiResponse(code = 400, message = "BAD REQUEST",response = ExceptionResponse.class),
            @ApiResponse(code = 401, message = "UNAUTHORIZED", response = ExceptionResponse.class),
            @ApiResponse(code = 403, message = "FORBIDDEN", response = ExceptionResponse.class),
            @ApiResponse(code = 500, message = "INTERNAL_SERVER_ERROR", response = ExceptionResponse.class)
    })
    @GetMapping("/admin/lectures")
    public ResponseEntity<Page<LectureResponse>> findLectures(
            @PageableDefault(size = 15, sort = "id", direction = Sort.Direction.DESC)
            @ApiIgnore Pageable pageable){
        return ResponseEntity.ok(lectureService.findPage(pageable));
    }

    @ApiOperation(value = "강의명 조회",notes = "강의명 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization",value = "로그인 성공 후 access_token",required = true,
                    dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "majorId",value = "전공 & 교양 Id",required = true,
                    dataType = "long", paramType = "query")
    })
    @ApiResponses({
            @ApiResponse(code = 400, message = "BAD REQUEST",response = ExceptionResponse.class),
            @ApiResponse(code = 401, message = "UNAUTHORIZED", response = ExceptionResponse.class),
            @ApiResponse(code = 403, message = "FORBIDDEN", response = ExceptionResponse.class),
            @ApiResponse(code = 500, message = "INTERNAL_SERVER_ERROR", response = ExceptionResponse.class)
    })
    @GetMapping("/lectures/name")
    public ResponseEntity<List<LectureNameResponse>> findLectureName(
            @ApiIgnore @RequestParam("majorId") Long majorId){

        List<LectureNameResponse> result = lectureService.findLectureNameByMajorId(majorId);

        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "강의 교수 조회",notes = "강의 교수 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization",value = "로그인 성공 후 access_token",required = true,
                    dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "lectureName",value = "전공 & 교양 이름",required = true,
                    dataType = "String", paramType = "query")
    })
    @ApiResponses({
            @ApiResponse(code = 400, message = "BAD REQUEST",response = ExceptionResponse.class),
            @ApiResponse(code = 401, message = "UNAUTHORIZED", response = ExceptionResponse.class),
            @ApiResponse(code = 403, message = "FORBIDDEN", response = ExceptionResponse.class),
            @ApiResponse(code = 500, message = "INTERNAL_SERVER_ERROR", response = ExceptionResponse.class)
    })
    @GetMapping("/lecture/professor")
    public ResponseEntity<List<LectureProfessorResponse>> findLectureProfessor(
            @ApiIgnore @RequestParam("lectureName") String lectureName){

        List<LectureProfessorResponse> result = lectureService.findLectureProfessorByLectureName(lectureName);

        return ResponseEntity.ok(result);
    }
}
