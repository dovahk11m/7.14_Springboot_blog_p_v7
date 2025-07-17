package com.tenco.blog._core.aop;

import com.tenco.blog._core.errors.exception.Exception400;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

@Slf4j
@Aspect
@Component
public class MyValidationHandler {
    /*
    POST, PUT 요청에서 발생하는 유효성 검사 자동화

    동작원리
    1.POST/PUT 요청이 Controller 메서드에 도달하기 전에 가로챈다
    2.메서드의 매개변수 중 Errors 객체를 찾는다
    3.유효성 문제가 있으면 Exception400을 던진다
     */

    @Before("@annotation(org.springframework.web.bind.annotation.PutMapping) || @annotation(org.springframework.web.bind.annotation.PostMapping)")
    public void validationCheck(JoinPoint joinPoint) {

        log.debug("===== AOP 유효성검사 시작 =====");
        log.debug("실행메서드:{}", joinPoint.getSignature().getName());

        //JoinPoint 에서 메서드의 모든 매개변수 가져오기
        Object[] args = joinPoint.getArgs();

        //매개변수를 하나씩 검사해서 Errors 객체를 찾는다
        for(Object arg : args) {
            if (arg instanceof Errors) { //이 arg가 Errors의 인스턴스라면?
                //instanceof 객체가 특정 타입인지 확인하는 연산자다

                log.debug("Errors 객체 발견 - 유효성 검사 진행");

                Errors errors = (Errors)arg;

                if (errors.hasErrors()) {//오류가 있다면
                    log.warn("유효성검사 오류발견, 오류개수:{}",errors.getErrorCount());
                    FieldError firstError = errors.getFieldErrors().get(0);
                    String errorMessage = firstError.getDefaultMessage() + ":" + firstError.getField();
                    throw new Exception400(errorMessage);
                }
                break;

            }//if

        }//for
        log.debug("유효성 검사 통과");

        log.debug("===== AOP 유효성검사 종료 =====");

    }//validationCheck

}
