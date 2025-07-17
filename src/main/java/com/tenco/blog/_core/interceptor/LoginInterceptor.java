package com.tenco.blog._core.interceptor;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.tenco.blog._core.errors.exception.Exception401;
import com.tenco.blog._core.errors.exception.Exception500;
import com.tenco.blog._core.utils.Define;
import com.tenco.blog._core.utils.JwtUtil;
import com.tenco.blog.user.SessionUser;
import com.tenco.blog.user.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Component // Ioc 대상 (싱글톤 패턴으로 관리)
public class LoginInterceptor implements HandlerInterceptor {

    /**
     * preHandle - 컨트롤러에 들어 가지 전에 동작 하는 메서드이다.
     * 리턴타입이 boolean 이라서 true ---> 컨트롤러 안으로 들어간다, false --> 못 들어 감
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        log.debug("===== jwt 인증 인터셉터 시작 =====");
        String jwt = request.getHeader("Authorization");
        // Bearer + 공백 + 암호화된 토큰

        if (jwt == null || !jwt.startsWith("Bearer ")) {
            throw new Exception401("jwt 토큰이 없습니다");
        }
        jwt = jwt.replace("Bearer ", "");

        try {
            SessionUser sessionUser = JwtUtil.verify(jwt);

            // 구분 중요 (임시 세션 사용), 외부에선 jwt, 내부에선 세션 사용 가능
            //HttpSession session = request.getSession();
            //session.setAttribute(Define.SESSION_USER, sessionUser);

            // 근데 jwt는 stateless 개념을 지키기 위해 나왔음, 모바일 쿠키에 접근 불가능

            //request.setAttribute 는 요청 단위로 데이터를 저장하고 소멸한다
            //즉 해당 데이터는 요청이 처리된 뒤 사라지고
            //서버에 세션메모리에 저장되지 않는다
            request.setAttribute(Define.SESSION_USER, sessionUser);
            return true;


        } catch (TokenExpiredException e) {
            throw new Exception401("토큰 만료시간이 지났습니다");
        } catch (JWTDecodeException e) {
            throw new Exception401("토큰이 유효하지 않습니다");
        } catch (Exception e) {
            throw new Exception500(e.getMessage());
        }

    }//

    // 뷰가 렌터링 되기전에 콜백 되는 메서드
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);

    }//

    // 뷰가 완전 렌더링이 된 후 호출 될 수 있다.
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);

    }//

}//LoginInterceptor
