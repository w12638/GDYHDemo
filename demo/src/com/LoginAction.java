package com;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

















import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import db.DBUtil;
@Controller
public class LoginAction {
 
	@RequestMapping("login.do")
	public String login(String loginname, String password, HttpServletRequest request) {
		System.out.println("用户名："+loginname+"\t密码："+password); 
		try{
			HttpSession session = request.getSession();
			String sessionid = session.getId();
			
			String sql = "SELECT * FROM user where name = '"+loginname+"';";
	        String pd  = DBUtil.selectSql(sql, "password");  
	        
	        request.setAttribute("username", pd);
	        request.setAttribute("sessionid", sessionid);
		}catch(Exception e){
			return "error";
		}
		
		return "NewFile";
	}

	
}
