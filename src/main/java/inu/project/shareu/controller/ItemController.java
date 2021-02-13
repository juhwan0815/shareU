package inu.project.shareu.controller;

import inu.project.shareu.config.security.LoginMember;
import inu.project.shareu.model.request.item.ItemSaveRequest;
import inu.project.shareu.model.request.item.ItemUpdateRequest;
import inu.project.shareu.service.ItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@Api(tags = "2.족보")
@Slf4j
@RestController
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @ApiOperation(value = "족보 등록",notes = "족보 등록")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization",value = "로그인 성공 후 access_token",required = true
                    ,dataType = "String", paramType = "header")
    })
    @PostMapping("/items")
    public ResponseEntity saveItem(@ModelAttribute ItemSaveRequest itemSaveRequest){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginMember loginMember = (LoginMember) authentication.getPrincipal();
        Long memberId = loginMember.getId();

        itemService.saveItem(memberId,itemSaveRequest);

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

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginMember loginMember = (LoginMember) authentication.getPrincipal();
        Long memberId = loginMember.getId();

        itemService.updateItem(memberId,itemId,itemUpdateRequest);

        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "족보 삭제",notes = "족보 삭제")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization",value = "로그인 성공 후 access_token",required = true
                    ,dataType = "String", paramType = "header")
    })
    @DeleteMapping("/items/{itemId}")
    public ResponseEntity deleteItem(@PathVariable Long itemId){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginMember loginMember = (LoginMember) authentication.getPrincipal();
        Long memberId = loginMember.getId();

        itemService.deleteItem(memberId,itemId);

        return ResponseEntity.ok().build();
    }
}
