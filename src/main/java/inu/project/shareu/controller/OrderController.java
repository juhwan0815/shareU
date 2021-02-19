package inu.project.shareu.controller;

import inu.project.shareu.config.security.LoginMember;
import inu.project.shareu.domain.Member;
import inu.project.shareu.service.OrderService;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Api(tags = "5. 구매")
@Slf4j
@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @ApiOperation(value = "장바구니 일괄 구매",notes = "장바구니 일괄 구매")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization",value = "로그인 성공 후 access_token",required = true
                    ,dataType = "String", paramType = "header")
    })
    @PostMapping("/orders")
    public ResponseEntity saveBulkOrder(){

        Member member = getLoginMember();

        orderService.saveBulkOrder(member);

        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "족보 단품 구매",notes = "족보 단품 구매")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization",value = "로그인 성공 후 access_token",required = true
                    ,dataType = "String", paramType = "header")
    })
    @PostMapping("/orders/items/{itemId}")
    public ResponseEntity saveSingleOrder(@PathVariable Long itemId){

        Member member = getLoginMember();

        orderService.saveSingleOrder(member,itemId);

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
