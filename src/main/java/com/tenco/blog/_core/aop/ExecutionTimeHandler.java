package com.tenco.blog._core.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect //AOP
@Component //Ioc 대상, Bean으로 처리됨
@Slf4j
public class ExecutionTimeHandler {

    /*
     1.요청부터 응답까지 실행시간을 측정하여 로그에 기록하는 Advice로 지정한다
     2.JoinPoint 지정 ( 특정 시점 )
      @Around : 메서드의 실행 전과 후에 동작하라
     3.PointCut.. Advice가 들어갈 메서드 할당
     execution(* com.tenco.blog..*(..))
     4.Advice가 실행할 작업을 명시
     */

    //@Around("execution(* com.tenco.blog..*(..))")
    @Around("@annotation(org.springframework.web.bind.annotation.GetMapping) || @annotation(org.springframework.web.bind.annotation.PostMapping)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable { //Exeption 상위호환
        //ProceedingJoinPoint = 중요한 통로 관리자
        //행사진행담당자.. 행사준비와 마무리정리를 하는 녀석이다
        //"이제 행사 시작" 신호를 주면 메서드가 실행된다

        //1.요청시작시간기록
        long startTime = System.currentTimeMillis();

        //2.진행하고자 하는 메서드 진행 신호
        Object result;
        try {
            result = joinPoint.proceed();

        } catch (Throwable e) {
            log.error("Advice 실행중 오류발생:{}", e.getMessage());
            throw e;
        }

        //3. 응답완료 시간기록 및 실행시간 기록
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;

        //4. 로그에 실행시간 출력
        log.info("메서드:{},실행시간:{}ms", joinPoint.getSignature().getName(), executionTime);

        //원래 메서드의 결과 반환
        return result;
    }

}
