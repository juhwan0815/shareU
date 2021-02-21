package inu.project.shareu.controller;

import inu.project.shareu.model.college.response.CollegeResponse;
import inu.project.shareu.model.college.response.CollegeWithMajorResponse;
import inu.project.shareu.service.CollegeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "단과대학")
@RestController
@RequiredArgsConstructor
public class CollegeController {

    private final CollegeService collegeService;

    @ApiOperation(value = "단과대학 조회",notes = "단과대학 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization",value = "로그인 성공 후 access_token",required = true
                    ,dataType = "String", paramType = "header")
    })
    @GetMapping("/colleges")
    public ResponseEntity<List<CollegeResponse>> findColleges(){

        List<CollegeResponse> result = collegeService.findAll();

        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "단과대학 및 소속 학과 조회",notes = "단과대학 및 소속 학과 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization",value = "로그인 성공 후 access_token",required = true
                    ,dataType = "String", paramType = "header")
    })
    @GetMapping("/colleges/majors")
    public ResponseEntity<List<CollegeWithMajorResponse>> findCollegesWithMajors(){

        List<CollegeWithMajorResponse> result = collegeService.findAllWithMajors();

        return ResponseEntity.ok(result);
    }


}
