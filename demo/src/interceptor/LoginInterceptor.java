package interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import entities.User;
import mySession.MySessionContext;

/**
 * 判断登录拦截器
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
		HttpSession session = MySessionContext.getSession(request.getHeader("sessionId"));
		User user = null;
		if (session != null) {
			user = (User) session.getAttribute("user");
		}
		if (user != null) {
			return true;
		}

		String resultString = "{\"code\":\"000001\",\"message\":\"请登录后重试\",\"data\":" + null + "}";
		response.setContentType("application/json");
		response.getWriter().write(resultString);
		return false;
	}

}
