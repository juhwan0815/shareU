package inu.project.shareu.controller;

import inu.project.shareu.config.security.LoginMember;
import inu.project.shareu.domain.Store;
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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Api(tags = "10.파일")
@Slf4j
@Controller
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    @ApiOperation(value = "파일 삭제",notes = "파일 삭제")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization",value = "로그인 성공 후 access_token",required = true
                    ,dataType = "String", paramType = "header")
    })
    @DeleteMapping("/store/{storeId}")
    @ResponseBody
    public ResponseEntity removeStore(@PathVariable Long storeId){

        storeService.deleteStore(storeId);

        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "파일 다운",notes = "파일 다운")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization",value = "로그인 성공 후 access_token",required = true
                    ,dataType = "String", paramType = "header")
    })
    @GetMapping("/store")
    public String getResourcePath(@RequestParam("storeName") String storeName){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginMember loginMember = (LoginMember) authentication.getPrincipal();
        Long memberId = loginMember.getId();

        Store store = storeService.findStore(memberId, storeName);
        return "redirect:"+store.getResourcePath();
    }


}
