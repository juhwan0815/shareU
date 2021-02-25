package inu.project.shareu.controller;

import inu.project.shareu.model.common.response.ExceptionResponse;
import inu.project.shareu.model.major.request.MajorSaveRequest;
import inu.project.shareu.model.major.request.MajorUpdateRequest;
import inu.project.shareu.model.major.response.MajorResponse;
import inu.project.shareu.service.MajorService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "전공,교양")
@Slf4j
@RestController
@RequiredArgsConstructor
public class MajorController {

    private final MajorService majorService;

    @ApiOperation(value = "(관리자) 전공 & 교양 등록",notes = "(관리자) 전공 & 교양 등록")
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
    @PostMapping(value = "/admin/majors",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> saveMajor(
            @ApiParam(name = "전공 & 교양 등록 요청 모델",value = "전공 & 교양 등록 요청 모델",required = true,type = "body")
            @RequestBody @Valid MajorSaveRequest majorSaveRequest){

        majorService.saveMajor(majorSaveRequest);

        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "(관리자) 전공 & 교양 수정",notes = "(관리자) 전공 & 교양 수정")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization",value = "로그인 성공 후 access_token",required = true,
                    dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "majorId",value = "전공 Id",required = true,
                    dataType = "long",paramType = "path")
    })
    @ApiResponses({
            @ApiResponse(code = 400, message = "BAD REQUEST",response = ExceptionResponse.class),
            @ApiResponse(code = 401, message = "UNAUTHORIZED", response = ExceptionResponse.class),
            @ApiResponse(code = 403, message = "FORBIDDEN", response = ExceptionResponse.class),
            @ApiResponse(code = 500, message = "INTERNAL_SERVER_ERROR", response = ExceptionResponse.class)
    })
    @PutMapping(value = "/admin/majors/{majorId}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateMajor(
            @PathVariable Long majorId,
            @ApiParam(name = "전공 & 교양 수정 요청 모델",value = "전공 & 교양 수정 요청 모델",required = true,type = "body")
            @RequestBody @Valid MajorUpdateRequest majorUpdateRequest){

        majorService.updateMajor(majorId,majorUpdateRequest);

        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "(관리자) 전공 & 교양 삭제",notes = "(관리자) 전공 & 교양 삭제")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization",value = "로그인 성공 후 access_token",required = true,
                    dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "majorId",value = "전공 Id",required = true,
                    dataType = "long", paramType = "path")
    })
    @ApiResponses({
            @ApiResponse(code = 400, message = "BAD REQUEST",response = ExceptionResponse.class),
            @ApiResponse(code = 401, message = "UNAUTHORIZED", response = ExceptionResponse.class),
            @ApiResponse(code = 403, message = "FORBIDDEN", response = ExceptionResponse.class),
            @ApiResponse(code = 500, message = "INTERNAL_SERVER_ERROR", response = ExceptionResponse.class)
    })
    @DeleteMapping(value = "/admin/majors/{majorId}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteMajor(@PathVariable Long majorId){
        majorService.deleteMajor(majorId);

        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "(관리자) 전공 & 교양 조회",notes = "(관리자) 전공 & 교양 조회")
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
    @GetMapping(value = "/admin/majors",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<MajorResponse>> findMajors(
            @PageableDefault(size = 15, sort = "id", direction = Sort.Direction.DESC)
            @ApiIgnore Pageable pageable){
        return ResponseEntity.ok(majorService.findMajors(pageable));
    }

    @ApiOperation(value = "전공 & 교양 조회",notes = "전공 & 교양 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization",value = "로그인 성공 후 access_token",required = true,
                    dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "collegeId",value = "단과 대학 Id",required = true,
                    dataType = "long", paramType = "path")
    })
    @ApiResponses({
            @ApiResponse(code = 400, message = "BAD REQUEST",response = ExceptionResponse.class),
            @ApiResponse(code = 401, message = "UNAUTHORIZED", response = ExceptionResponse.class),
            @ApiResponse(code = 403, message = "FORBIDDEN", response = ExceptionResponse.class),
            @ApiResponse(code = 500, message = "INTERNAL_SERVER_ERROR", response = ExceptionResponse.class)
    })
    @GetMapping(value = "/college/{collegeId}/majors",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MajorResponse>> findMajorsByCollegeId(
            @ApiIgnore @PathVariable("collegeId") Long collegeId){

        List<MajorResponse> result = majorService.findMajorsByCollegeId(collegeId);

        return ResponseEntity.ok(result);
    }


}
