package inu.project.shareu.controller;

import inu.project.shareu.model.request.major.MajorSaveRequest;
import inu.project.shareu.model.request.major.MajorUpdateRequest;
import inu.project.shareu.service.MajorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(tags = "3.전공,교양")
@Slf4j
@RestController
@RequiredArgsConstructor
public class MajorController {

    private final MajorService majorService;

    @ApiOperation(value = "전공 & 교양 등록",notes = "전공 & 교양 등록")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization",value = "로그인 성공 후 access_token",required = true
                    ,dataType = "String", paramType = "header")
    })
    @PostMapping("/majors")
    public ResponseEntity saveMajor(@ModelAttribute MajorSaveRequest majorSaveRequest){

        majorService.saveMajor(majorSaveRequest);

        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "전공 & 교양 수정",notes = "전공 & 교양 수정")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization",value = "로그인 성공 후 access_token",required = true
                    ,dataType = "String", paramType = "header")
    })
    @PutMapping("/majors/{majorId}")
    public ResponseEntity updateMajor(@PathVariable Long majorId,
                                      @ModelAttribute MajorUpdateRequest majorUpdateRequest){

        majorService.updateMajor(majorId,majorUpdateRequest);

        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "전공 & 교양 삭제",notes = "전공 & 교양 삭제")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization",value = "로그인 성공 후 access_token",required = true
                    ,dataType = "String", paramType = "header")
    })
    @DeleteMapping("/majors/{majorId}")
    public ResponseEntity deleteMajor(@PathVariable Long majorId){
        majorService.deleteMajor(majorId);

        return ResponseEntity.ok().build();
    }
}
