package com;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import entities.DataReturn;
import mySession.MySessionContext;

@RestController
@RequestMapping("/kaifapingtai")
public class LogOut {
	@GetMapping("/logout.do")
	public DataReturn logout(HttpServletRequest request) {
		MySessionContext.DelSession(MySessionContext.getSession(request.getHeader("sessionId")));
		return new DataReturn("退出成功！", null);

	}

}
