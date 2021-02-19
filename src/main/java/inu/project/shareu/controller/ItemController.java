package inu.project.shareu.controller;

import inu.project.shareu.config.security.LoginMember;
import inu.project.shareu.domain.Item;
import inu.project.shareu.domain.Member;
import inu.project.shareu.model.request.item.ItemSaveRequest;
import inu.project.shareu.model.request.item.ItemUpdateRequest;
import inu.project.shareu.service.BadWordService;
import inu.project.shareu.service.ItemService;
import inu.project.shareu.service.StoreService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Api(tags = "2. 족보")
@Slf4j
@RestController
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final StoreService storeService;
    private final BadWordService badWordService;

    @ApiOperation(value = "족보 등록",notes = "족보 등록")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization",value = "로그인 성공 후 access_token",required = true
                    ,dataType = "String", paramType = "header")
    })
    @PostMapping("/items")
    public ResponseEntity saveItem(@ModelAttribute ItemSaveRequest itemSaveRequest){

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
            @ApiImplicitParam(name = "Authorization",value = "로그인 성공 후 access_token",required = true
                    ,dataType = "String", paramType = "header")
    })
    @PatchMapping("/items/{itemId}")
    public ResponseEntity updateItem(@PathVariable Long itemId,
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
            @ApiImplicitParam(name = "Authorization",value = "로그인 성공 후 access_token",required = true
                    ,dataType = "String", paramType = "header")
    })
    @DeleteMapping("/items/{itemId}")
    public ResponseEntity deleteItem(@PathVariable Long itemId){

        Member member = getLoginMember();

        Item item = itemService.deleteItem(member, itemId);
        storeService.deleteStores(item);

        return ResponseEntity.ok().build();
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
