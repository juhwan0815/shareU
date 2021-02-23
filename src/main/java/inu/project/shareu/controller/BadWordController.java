package inu.project.shareu.controller;

import inu.project.shareu.model.badword.request.BadWordSaveRequest;
import inu.project.shareu.model.badword.response.BadWordResponse;
import inu.project.shareu.model.common.response.ExceptionResponse;
import inu.project.shareu.service.BadWordService;
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

import javax.validation.Valid;

@Api(tags = "(관리자 전용) 금칙어")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class BadWordController {

    private final BadWordService badWordService;

    @ApiOperation(value = "(관리자) 금칙어 등록",notes = "(관리자) 금칙어 등록")
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

    @PostMapping("/badwords")
    public ResponseEntity<Void> saveBadWord(@Valid @RequestBody BadWordSaveRequest badWordSaveRequest){

        badWordService.saveBadWord(badWordSaveRequest);

        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "(관리자) 금칙어 삭제",notes = "(관리자) 금칙어 삭제")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "badWordId",value = "금칙어 Id",required = true,
                    dataType = "long", paramType = "path"),
            @ApiImplicitParam(name = "Authorization",value = "로그인 성공 후 access_token",required = true,
                    dataType = "String", paramType = "header")
    })
    @ApiResponses({
            @ApiResponse(code = 400, message = "BAD REQUEST",response = ExceptionResponse.class),
            @ApiResponse(code = 401, message = "UNAUTHORIZED", response = ExceptionResponse.class),
            @ApiResponse(code = 403, message = "FORBIDDEN", response = ExceptionResponse.class),
            @ApiResponse(code = 500, message = "INTERNAL_SERVER_ERROR", response = ExceptionResponse.class)
    })
    @DeleteMapping("/badwords/{badWordId}")
    public ResponseEntity<Void> deleteBadWord(@PathVariable Long badWordId){

        badWordService.deleteBadWord(badWordId);

        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "(관리자) 금칙어 조회",notes = "(관리자) 금칙어 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page",value = "페이지",required = true,
                    dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "size",value = "페이징 사이즈",required = true,
                    dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "Authorization",value = "로그인 성공 후 access_token",required = true,
                    dataType = "String", paramType = "header")
    })
    @ApiResponses({
            @ApiResponse(code = 400, message = "BAD REQUEST",response = ExceptionResponse.class),
            @ApiResponse(code = 401, message = "UNAUTHORIZED", response = ExceptionResponse.class),
            @ApiResponse(code = 403, message = "FORBIDDEN", response = ExceptionResponse.class),
            @ApiResponse(code = 500, message = "INTERNAL_SERVER_ERROR", response = ExceptionResponse.class)
    })
    @GetMapping("/badwords")
    public ResponseEntity<Page<BadWordResponse>> findBadWords(
            @PageableDefault(size = 15, sort = "id", direction = Sort.Direction.DESC)
            @ApiIgnore Pageable pageable){
        return ResponseEntity.ok(badWordService.findPage(pageable));
    }


}
