package inu.project.shareu.controller;

import inu.project.shareu.config.security.LoginMember;
import inu.project.shareu.domain.Member;
import inu.project.shareu.model.cart.request.CartSaveRequest;
import inu.project.shareu.model.cart.response.CartResponse;
import inu.project.shareu.model.common.response.ExceptionResponse;
import inu.project.shareu.service.CartService;
import inu.project.shareu.service.query.CartQueryService;
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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@Api(tags = "4.장바구니")
@Slf4j
@Controller
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final CartQueryService cartQueryService;

    @ApiOperation(value = "장바구니 등록",notes = "장바구니 등록")
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
    @PostMapping("/carts")
    public ResponseEntity<Void> saveCart(@ModelAttribute CartSaveRequest cartSaveRequest){

        Member member = getLoginMember();

        cartService.saveCart(member,cartSaveRequest);

        return ResponseEntity.ok().build();
    }


    @ApiOperation(value = "장바구니 삭제",notes = "장바구니 삭제")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization",value = "로그인 성공 후 access_token",required = true,
                    dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "cartId",value = "장바구니 Id",required = true,
                    dataType = "long",paramType = "path")
    })
    @ApiResponses({
            @ApiResponse(code = 400, message = "BAD REQUEST",response = ExceptionResponse.class),
            @ApiResponse(code = 401, message = "UNAUTHORIZED", response = ExceptionResponse.class),
            @ApiResponse(code = 403, message = "FORBIDDEN", response = ExceptionResponse.class),
            @ApiResponse(code = 500, message = "INTERNAL_SERVER_ERROR", response = ExceptionResponse.class)
    })
    @DeleteMapping("/carts/{cartId}")
    public ResponseEntity<Void> deleteCart(@PathVariable Long cartId){

        Member member = getLoginMember();

        cartService.deleteCart(member,cartId);

        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "장바구니 족보 조회",notes = "장바구니 족보 조회")
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
    @GetMapping("/carts")
    public ResponseEntity<Page<CartResponse>> findCarts(
            @PageableDefault(size = 15, sort = "id", direction = Sort.Direction.DESC)
            @ApiIgnore Pageable pageable){

        Member loginMember = getLoginMember();
        return ResponseEntity.ok(cartQueryService.findPage(loginMember,pageable));
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
