package inu.project.shareu.controller;

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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@Api(tags = "2.족보")
@Slf4j
@RestController
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final StoreService storeService;
    private final BadWordService badWordService;

    @ApiOperation(value = "족보 등록",notes = "족보 등록")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization",value = "로그인 성공 후 access_token",required = true,
                    dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "lectureId",value = "강의 Id",required = true,
                    dataType = "long", paramType = "query"),
            @ApiImplicitParam(name = "title",value = "족보 제목",required = true,
                    dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "itemContents",value = "족보 설명",required = true,
                    dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "files",value = "족보 파일 (다중 선택 가능)",required = true,
                    dataType = "file",paramType = "query")
    })
    @ApiResponses({
            @ApiResponse(code = 400, message = "BAD REQUEST",response = ExceptionResponse.class),
            @ApiResponse(code = 401, message = "UNAUTHORIZED", response = ExceptionResponse.class),
            @ApiResponse(code = 403, message = "FORBIDDEN", response = ExceptionResponse.class),
            @ApiResponse(code = 500, message = "INTERNAL_SERVER_ERROR", response = ExceptionResponse.class)
    })
    @PostMapping("/items")
    public ResponseEntity<Void> saveItem(
            @ApiIgnore @ModelAttribute ItemSaveRequest itemSaveRequest){

        // TODO 파일 타입 체크

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
    @PatchMapping("/items/{itemId}")
    public ResponseEntity<Void> updateItem(@PathVariable Long itemId,
                                           @ModelAttribute ItemUpdateRequest itemUpdateRequest){

        // TODO 파일 수정은 어떻게?
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
    @DeleteMapping("/items/{itemId}")
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
    @DeleteMapping("/admin/items/{itemId}")
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
    @GetMapping("/members/items")
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
    @GetMapping("/items/{itemId}")
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
    @GetMapping("/items")
    public ResponseEntity<Page<ItemSearchResponse>> findItemPage(
            ItemSearchCondition itemSearchCondition,
            Pageable pageable){

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
}
