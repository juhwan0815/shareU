package inu.project.shareu.controller;

import inu.project.shareu.advice.exception.ItemException;
import inu.project.shareu.advice.exception.StoreException;
import inu.project.shareu.config.security.LoginMember;
import inu.project.shareu.domain.Item;
import inu.project.shareu.domain.Member;
import inu.project.shareu.model.common.response.ExceptionResponse;
import inu.project.shareu.model.item.request.ItemSaveRequest;
import inu.project.shareu.model.item.request.ItemSearchCondition;
import inu.project.shareu.model.item.request.ItemUpdateRequest;
import inu.project.shareu.model.item.response.ItemDetailResponse;
import inu.project.shareu.model.item.response.ItemResponse;
import inu.project.shareu.model.item.response.ItemSearchResponse;
import inu.project.shareu.service.BadWordService;
import inu.project.shareu.service.ItemService;
import inu.project.shareu.service.StoreService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Api(tags = "2.족보")
@Slf4j
@RestController
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final StoreService storeService;
    private final BadWordService badWordService;

    @ApiOperation(value = "족보 등록",notes = "족보 등록 \n 스웨거 다중 파일 업로드르 지원하지 않아 포스트맨으로 테스트")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization",value = "로그인 성공 후 access_token",required = true,
                    dataType = "String", paramType = "header")
    })
    @ApiResponses({
            @ApiResponse(code = 200,message = "OK"),
            @ApiResponse(code = 400, message = "BAD REQUEST",response = ExceptionResponse.class),
            @ApiResponse(code = 401, message = "UNAUTHORIZED", response = ExceptionResponse.class),
            @ApiResponse(code = 403, message = "FORBIDDEN", response = ExceptionResponse.class),
            @ApiResponse(code = 500, message = "INTERNAL_SERVER_ERROR", response = ExceptionResponse.class)
    })
    @PostMapping(value = "/items",consumes = MediaType.MULTIPART_FORM_DATA_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> saveItem(@Valid ItemSaveRequest itemSaveRequest){

        validateFilesExistAndSize(itemSaveRequest.getFiles());

        validateFileType(itemSaveRequest);

        Member member = getLoginMember();

        badWordService.validateItemForbiddenWord(itemSaveRequest.getTitle(),
                                                 itemSaveRequest.getItemContents());

        Item saveItem = itemService.saveItem(member, itemSaveRequest);

        storeService.saveFile(itemSaveRequest.getFiles(),saveItem);

        return ResponseEntity.ok().build();
    }


    @ApiOperation(value = "족보 수정",notes = "족보 수정")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization",value = "로그인 성공 후 access_token",required = true,
                    dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "itemId",value = "족보 Id",required = true,
                    dataType = "long", paramType = "path")
    })
    @ApiResponses({
            @ApiResponse(code = 400, message = "BAD REQUEST",response = ExceptionResponse.class),
            @ApiResponse(code = 401, message = "UNAUTHORIZED", response = ExceptionResponse.class),
            @ApiResponse(code = 403, message = "FORBIDDEN", response = ExceptionResponse.class),
            @ApiResponse(code = 500, message = "INTERNAL_SERVER_ERROR", response = ExceptionResponse.class)
    })
    @PatchMapping(value = "/items/{itemId}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateItem(
            @PathVariable Long itemId,
            @ApiParam(name = "족보 수정 요청 모델",value = "족보 수정 요청 모델",required = true,type = "body")
            @RequestBody @Valid ItemUpdateRequest itemUpdateRequest){

        Member member = getLoginMember();

        badWordService.validateItemForbiddenWord(itemUpdateRequest.getTitle(),
                                                 itemUpdateRequest.getItemContents());

        itemService.updateItem(member,itemId,itemUpdateRequest);

        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "족보 삭제",notes = "족보 삭제")
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
    @DeleteMapping(value = "/items/{itemId}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteItem(@PathVariable Long itemId){

        Member member = getLoginMember();

        Item item = itemService.deleteItem(member, itemId);
        storeService.deleteStores(item);

        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "관리자 족보 삭제",notes = "관리자 족보 삭제")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization",value = "로그인 성공 후 access_token",required = true,
                    dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "itemId",value = "족보 Id",required = true,
                    dataType = "long", paramType = "path")
    })
    @ApiResponses({
            @ApiResponse(code = 400, message = "BAD REQUEST",response = ExceptionResponse.class),
            @ApiResponse(code = 401, message = "UNAUTHORIZED", response = ExceptionResponse.class),
            @ApiResponse(code = 403, message = "FORBIDDEN", response = ExceptionResponse.class),
            @ApiResponse(code = 500, message = "INTERNAL_SERVER_ERROR", response = ExceptionResponse.class)
    })
    @DeleteMapping(value = "/admin/items/{itemId}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteItemByAdmin(@PathVariable Long itemId){

        Item item = itemService.deleteItemByAdmin(itemId);
        storeService.deleteStores(item);

        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "내가 등록한 족보 조회",notes = "내가 등록한 족보 조회")
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
    @GetMapping(value = "/members/items",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<ItemResponse>> findMyItems(
            @PageableDefault(size = 15, sort = "id", direction = Sort.Direction.DESC)
            @ApiIgnore Pageable pageable){

        Member loginMember = getLoginMember();
        return ResponseEntity.ok(itemService.findMyItemPage(loginMember,pageable));
    }

    @ApiOperation(value = "족보 상세 조회",notes = "족보 상세 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization",value = "로그인 성공 후 access_token",required = true,
                    dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "itemId",value = "족보 Id",required = true,
                    dataType = "long", paramType = "path")
    })
    @ApiResponses({
            @ApiResponse(code = 400, message = "BAD REQUEST",response = ExceptionResponse.class),
            @ApiResponse(code = 401, message = "UNAUTHORIZED", response = ExceptionResponse.class),
            @ApiResponse(code = 403, message = "FORBIDDEN", response = ExceptionResponse.class),
            @ApiResponse(code = 500, message = "INTERNAL_SERVER_ERROR", response = ExceptionResponse.class)
    })
    @GetMapping(value = "/items/{itemId}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemDetailResponse> findItemById(@PathVariable Long itemId){

        ItemDetailResponse itemDetail = itemService.findItemById(itemId);

        return ResponseEntity.ok(itemDetail);
    }

    @ApiOperation(value = "족보 페이징 조회",notes = "족보 페이징 조회")
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
    @GetMapping(value = "/items",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<ItemSearchResponse>> findItemPage(
            ItemSearchCondition itemSearchCondition,
            @ApiIgnore Pageable pageable){

        Page<ItemSearchResponse> results = itemService.findItemByItemSearchCondition(itemSearchCondition, pageable);

        return ResponseEntity.ok(results);
    }

    /**
     * 현재 로그인한 사용자를 가져온다.
     */
    private Member getLoginMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginMember loginMember = (LoginMember) authentication.getPrincipal();
        return loginMember.getMember();
    }

    /**
     * 족보 등록시 파일 존재 여부 확인
     */
    private void validateFilesExistAndSize(List<MultipartFile> files) {
        if(files.size() == 0){
            throw new StoreException("파일이 존재하지 않습니다.");
        }
        if(files.size() > 3){
            throw new StoreException("파일은 3개까지만 가능합니다.");
        }
    }

    /**
     * 파일 타입 체크
     * Tika를 사용하여 MiME-TYPE을 체크한다.
     * application으로 시작하는 것이 아니면 모두 체크
     */
    private void validateFileType(ItemSaveRequest itemSaveRequest) {
        Tika tika = new Tika();
        itemSaveRequest.getFiles().forEach(file -> {
            try {
                String mimeType = tika.detect(file.getInputStream());
                if(!mimeType.startsWith("application")){
                    throw new StoreException(file.getOriginalFilename() + "은 업로드할 수 없는 파일타입입니다.");
                }
            } catch (IOException e) {
                throw new StoreException("파일 타입 체크 오류");
            }
        });
    }


}
