package inu.project.shareu.controller;

import inu.project.shareu.model.request.major.MajorSaveRequest;
import inu.project.shareu.model.request.major.MajorUpdateRequest;
import inu.project.shareu.model.response.common.SuccessListResponse;
import inu.project.shareu.model.response.major.MajorResponse;
import inu.project.shareu.service.MajorService;
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

@Api(tags = "전공,교양")
@Slf4j
@RestController
@RequiredArgsConstructor
public class MajorController {

    private final MajorService majorService;

    @ApiOperation(value = "(관리자) 전공 & 교양 등록",notes = "(관리자) 전공 & 교양 등록")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization",value = "로그인 성공 후 access_token",required = true
                    ,dataType = "String", paramType = "header")
    })
    @PostMapping("/admin/majors")
    public ResponseEntity saveMajor(@ModelAttribute MajorSaveRequest majorSaveRequest){

        majorService.saveMajor(majorSaveRequest);

        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "(관리자) 전공 & 교양 수정",notes = "(관리자) 전공 & 교양 수정")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization",value = "로그인 성공 후 access_token",required = true
                    ,dataType = "String", paramType = "header")
    })
    @PutMapping("/admin/majors/{majorId}")
    public ResponseEntity updateMajor(@PathVariable Long majorId,
                                      @ModelAttribute MajorUpdateRequest majorUpdateRequest){

        majorService.updateMajor(majorId,majorUpdateRequest);

        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "(관리자) 전공 & 교양 삭제",notes = "(관리자) 전공 & 교양 삭제")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization",value = "로그인 성공 후 access_token",required = true
                    ,dataType = "String", paramType = "header")
    })
    @DeleteMapping("/admin/majors/{majorId}")
    public ResponseEntity deleteMajor(@PathVariable Long majorId){
        majorService.deleteMajor(majorId);

        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "(관리자) 전공 & 교양 조회",notes = "(관리자) 전공 & 교양 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization",value = "로그인 성공 후 access_token",required = true
                    ,dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "page",value = "페이지",required = true
                    ,dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "size",value = "페이징 사이즈",required = true
                    ,dataType = "int", paramType = "query")
    })
    @GetMapping("/admin/majors")
    public ResponseEntity findMajors(
            @PageableDefault(size = 15, sort = "id", direction = Sort.Direction.DESC)
            @ApiIgnore Pageable pageable){
        return ResponseEntity.ok(majorService.findMajors(pageable));
    }

    @ApiOperation(value = "전공 & 교양 조회",notes = "전공 & 교양 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization",value = "로그인 성공 후 access_token",required = true
                    ,dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "collegeId",value = "단과 대학 Id",required = true
                    ,dataType = "Long", paramType = "query")
    })
    @GetMapping("/majors")
    public ResponseEntity findMajorsByCollegeId(
            @ApiIgnore @RequestParam("collegeId") Long collegeId){

        List<MajorResponse> result = majorService.findMajorsByCollegeId(collegeId);

        return ResponseEntity.ok(new SuccessListResponse<>(result));
    }


}
