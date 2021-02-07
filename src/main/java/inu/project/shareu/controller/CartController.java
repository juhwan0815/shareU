package inu.project.shareu.controller;

import inu.project.shareu.config.security.LoginMember;
import inu.project.shareu.model.request.cart.CartSaveRequest;
import inu.project.shareu.service.CartService;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Api(tags = "5.장바구니")
@Slf4j
@Controller
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @ApiOperation(value = "장바구니 등록",notes = "장바구니 등록")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization",value = "로그인 성공 후 access_token",required = true
                    ,dataType = "String", paramType = "header")
    })
    @PostMapping("/carts")
    public ResponseEntity saveCart(@ModelAttribute CartSaveRequest cartSaveRequest){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginMember loginMember = (LoginMember) authentication.getPrincipal();
        Long memberId = loginMember.getId();

        cartService.saveCart(memberId,cartSaveRequest);

        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "장바구니 삭제",notes = "장바구니 삭제")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization",value = "로그인 성공 후 access_token",required = true
                    ,dataType = "String", paramType = "header")
    })
    @DeleteMapping("/carts/{cartId}")
    public ResponseEntity deleteCart(@PathVariable Long cartId){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginMember loginMember = (LoginMember) authentication.getPrincipal();
        Long memberId = loginMember.getId();

        cartService.deleteCart(memberId,cartId);

        return ResponseEntity.ok().build();
    }
}
