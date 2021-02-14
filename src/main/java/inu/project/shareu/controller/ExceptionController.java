package inu.project.shareu.controller;

import inu.project.shareu.advice.exception.AuthenticationEntryPointException;
import io.swagger.annotations.Api;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "예외처리")
@RestController
@RequestMapping("/exception")
public class ExceptionController {

    // TODO 알수없다 머냐?
    @RequestMapping("/entrypoint")
    public void entryPointException(){
        throw new AuthenticationEntryPointException("로그인이 필요한 서비스입니다.");
    }

    @RequestMapping("/accessdenied")
    public void accessDenied(){
        throw new AccessDeniedException("보유한 권한으로 접근할 수 없습니다.");
    }
}
