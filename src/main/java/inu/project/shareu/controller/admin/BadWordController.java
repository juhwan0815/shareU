package inu.project.shareu.controller.admin;

import inu.project.shareu.model.request.badword.BadWordSaveRequest;
import inu.project.shareu.service.BadWordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(tags = "금칙어")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class BadWordController {

    private final BadWordService badWordService;

    @ApiOperation(value = "금칙어 등록",notes = "금칙어 등록")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization",value = "로그인 성공 후 access_token",required = true
                    ,dataType = "String", paramType = "header")
    })
    @PostMapping("/badwords")
    public ResponseEntity saveBadWord(@ModelAttribute BadWordSaveRequest badWordSaveRequest){

        badWordService.saveBadWord(badWordSaveRequest);

        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "금칙어 삭제",notes = "금칙어 삭제")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization",value = "로그인 성공 후 access_token",required = true
                    ,dataType = "String", paramType = "header")
    })
    @DeleteMapping("/badwords/{badWordId}")
    public ResponseEntity deleteBadWord(@PathVariable Long badWordId){

        badWordService.deleteBadWord(badWordId);

        return ResponseEntity.ok().build();
    }
}
