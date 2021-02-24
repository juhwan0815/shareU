package inu.project.shareu.controller;

import inu.project.shareu.config.security.JwtTokenProvider;
import inu.project.shareu.config.security.LoginMember;
import inu.project.shareu.domain.Member;
import inu.project.shareu.model.common.response.ExceptionResponse;
import inu.project.shareu.model.member.request.MemberLoginRequest;
import inu.project.shareu.model.member.request.MemberSaveRequest;
import inu.project.shareu.model.member.request.MemberUpdateRequest;
import inu.project.shareu.model.member.response.MemberAuthResponse;
import inu.project.shareu.model.member.response.MemberBlockResponse;
import inu.project.shareu.model.member.response.MemberResponse;
import inu.project.shareu.service.BadWordService;
import inu.project.shareu.service.MemberService;
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

import javax.validation.Valid;

@Api(tags = {"1.회원"})
@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final BadWordService badWordService;
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    @ApiOperation(value = "회원가입",notes = "회원 가입")
    @ApiResponses({
            @ApiResponse(code = 400, message = "BAD REQUEST",response = ExceptionResponse.class),
            @ApiResponse(code = 500, message = "INTERNAL_SERVER_ERROR", response = ExceptionResponse.class)
    })
    @PostMapping(value = "/members",produces = "application/json")
    public ResponseEntity<Void> saveMember(
            @ApiParam(name = "회원가입 요청 모델",value = "회원가입 요청 모델",required = true,type = "body")
            @RequestBody @Valid MemberSaveRequest memberSaveRequest) {

        badWordService.validateItemForbiddenWord(memberSaveRequest.getName());
        memberService.saveMember(memberSaveRequest);

        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "회원 로그인",notes = "회원 로그인")
    @ApiResponses({
            @ApiResponse(code = 400, message = "BAD REQUEST",response = ExceptionResponse.class),
            @ApiResponse(code = 500, message = "INTERNAL_SERVER_ERROR", response = ExceptionResponse.class)
    })
    @PostMapping(value = "/members/login",produces = "application/json")
    public ResponseEntity<MemberAuthResponse> loginMember(
            @ApiParam(name = "회원 로그인 요청 모델",value = "회원 로그인 요청 모델",required = true,type = "body")
            @RequestBody @Valid MemberLoginRequest memberLoginRequest) {

        Member loginMember = memberService.loginMember(memberLoginRequest);

        String token = "Bearer "+ jwtTokenProvider.createToken(String.valueOf(loginMember.getId()));

        return ResponseEntity.ok(new MemberAuthResponse(token));
    }

    @ApiOperation(value = "회원 수정",notes = "회원 수정")
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
    @PatchMapping(value = "/members",produces = "application/json")
    public ResponseEntity<Void> updateMember(
            @ApiParam(name = "회원 수정 요청 모델",value = "회원 수정 요청 모델",required = true,type = "body")
            @RequestBody @Valid MemberUpdateRequest memberUpdateRequest){

        Member loginMember = getLoginMember();

        badWordService.validateItemForbiddenWord(memberUpdateRequest.getName());
        memberService.updateMember(loginMember,memberUpdateRequest);

        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "회원 탈퇴",notes = "회원 탈퇴")
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
    @DeleteMapping(value = "/members",produces = "application/json")
    public ResponseEntity<Void> removeMember(){

        Member loginMember = getLoginMember();
        memberService.removeMember(loginMember);

        // TODO 회원 탈퇴시 족보, 구매, 장바구니 등등 모든 엔티티 삭제?

        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "회원 정보 조회",notes = "회원 정보 조회")
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
    @GetMapping(value = "/members",produces = "application/json")
    public ResponseEntity<MemberResponse> findLoginMember(){

        Member loginMember = getLoginMember();

        return ResponseEntity.ok(new MemberResponse(loginMember));
    }

    @ApiOperation(value = "(관리자) 차단 회원 조회",notes = "(관리자) 차단 회원 조회")
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
    @GetMapping(value = "/admin/members",produces = "application/json")
    public ResponseEntity<Page<MemberBlockResponse>> findBlockMember(
            @PageableDefault(size = 15,sort = "id",direction = Sort.Direction.DESC)
            @ApiIgnore Pageable pageable){
        return ResponseEntity.ok(memberService.findBlockMembers(pageable));
    }

    @ApiOperation(value = "관리자 회원 차단 해제",notes = "관리자 회원 차단 해제")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization",value = "로그인 성공 후 access_token",required = true,
                    dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "memberId",value = "회원 Id",required = true,
                    dataType = "long",paramType = "path")
    })
    @ApiResponses({
            @ApiResponse(code = 400, message = "BAD REQUEST",response = ExceptionResponse.class),
            @ApiResponse(code = 401, message = "UNAUTHORIZED", response = ExceptionResponse.class),
            @ApiResponse(code = 403, message = "FORBIDDEN", response = ExceptionResponse.class),
            @ApiResponse(code = 500, message = "INTERNAL_SERVER_ERROR", response = ExceptionResponse.class)
    })
    @PatchMapping(value = "/admin/members/{memberId}",produces = "application/json")
    public ResponseEntity<Void> changeMemberStatus(@PathVariable Long memberId){

        memberService.changeMemberStatus(memberId);

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

