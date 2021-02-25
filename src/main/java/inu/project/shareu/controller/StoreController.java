package inu.project.shareu.controller;

import inu.project.shareu.advice.exception.StoreException;
import inu.project.shareu.config.security.LoginMember;
import inu.project.shareu.domain.Member;
import inu.project.shareu.domain.Store;
import inu.project.shareu.model.common.response.ExceptionResponse;
import inu.project.shareu.model.store.StoreDto;
import inu.project.shareu.service.StoreService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Api(tags = "8.파일")
@Slf4j
@RestController
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    @ApiOperation(value = "파일 삭제",notes = "파일 삭제")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization",value = "로그인 성공 후 access_token",required = true,
                    dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "storeId",value = "파일 Id",required = true,
                    dataType = "long", paramType = "path")
    })
    @ApiResponses({
            @ApiResponse(code = 400, message = "BAD REQUEST",response = ExceptionResponse.class),
            @ApiResponse(code = 401, message = "UNAUTHORIZED", response = ExceptionResponse.class),
            @ApiResponse(code = 403, message = "FORBIDDEN", response = ExceptionResponse.class),
            @ApiResponse(code = 500, message = "INTERNAL_SERVER_ERROR", response = ExceptionResponse.class)
    })
    @DeleteMapping(value = "/store/{storeId}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> removeStore(@PathVariable Long storeId){

        Member member = getLoginMember();
        storeService.deleteStore(member,storeId);

        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "파일 다운",notes = "파일 다운")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization",value = "로그인 성공 후 access_token",required = true,
                    dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "storeId",value = "파일 Id",required = true,
                    dataType = "long",paramType = "path")
    })
    @ApiResponses({
            @ApiResponse(code = 400, message = "BAD REQUEST",response = ExceptionResponse.class),
            @ApiResponse(code = 401, message = "UNAUTHORIZED", response = ExceptionResponse.class),
            @ApiResponse(code = 403, message = "FORBIDDEN", response = ExceptionResponse.class),
            @ApiResponse(code = 500, message = "INTERNAL_SERVER_ERROR", response = ExceptionResponse.class)
    })
    @GetMapping(value = "/store/{storeId}",produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<ByteArrayResource> getResourcePath(@PathVariable Long storeId){

        Member member = getLoginMember();

        StoreDto storeDto = storeService.downloadFile(member,storeId);
        Store store = storeDto.getStore();
        byte[] data = storeDto.getData();

        String fileOriginalName = store.getFileOriginalName();
        String encordedFilename = getEncodedFileOriginalName(fileOriginalName);

        ByteArrayResource resource = new ByteArrayResource(data);

        return ResponseEntity
                .ok()
                .contentLength(data.length)
                .header("Content-type","application/octet-stream")
                .header("Content-disposition","attachment; filename=\"" + encordedFilename + "\"")
                .body(resource);
    }

    @ApiOperation(value = "(관리자) 파일 다운",notes = "(관리자) 파일 다운")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization",value = "로그인 성공 후 access_token",required = true,
                    dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "storeId",value = "파일 Id",required = true,
                    dataType = "long",paramType = "path")
    })
    @ApiResponses({
            @ApiResponse(code = 400, message = "BAD REQUEST",response = ExceptionResponse.class),
            @ApiResponse(code = 401, message = "UNAUTHORIZED", response = ExceptionResponse.class),
            @ApiResponse(code = 403, message = "FORBIDDEN", response = ExceptionResponse.class),
            @ApiResponse(code = 500, message = "INTERNAL_SERVER_ERROR", response = ExceptionResponse.class)
    })
    @GetMapping(value = "/admin/store/{storeId}",produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<ByteArrayResource> getResourcePathByAdmin(@PathVariable Long storeId){

        StoreDto storeDto = storeService.downloadFileByAdmin(storeId);
        Store store = storeDto.getStore();
        byte[] data = storeDto.getData();

        String fileOriginalName = store.getFileOriginalName();
        String encordedFilename = getEncodedFileOriginalName(fileOriginalName);

        ByteArrayResource resource = new ByteArrayResource(data);

        return ResponseEntity
                .ok()
                .contentLength(data.length)
                .header("Content-type","application/octet-stream")
                .header("Content-disposition","attachment; filename=\"" + encordedFilename + "\"")
                .body(resource);
    }

    /**
     * 파일명 변경 (UTF8)
     */
    private String getEncodedFileOriginalName(String fileOriginalName) {
        String encordedFilename;
        try {
            encordedFilename = URLEncoder.encode(fileOriginalName,"UTF-8").replace("+", "%20");
        } catch (UnsupportedEncodingException e) {
            throw new StoreException("파일명 변경 중 오류");
        }
        return encordedFilename;
    }

    /**
     * 현재 로그인한 사용자를 가져온다.
     */
    private Member getLoginMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginMember loginMember = (LoginMember) authentication.getPrincipal();
        return loginMember.getMember();
    }

}
