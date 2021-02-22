package com;




import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;






import org.springframework.web.bind.annotation.RequestMethod;

import db.DBUtil;
@CrossOrigin(origins = "*", maxAge = 3600)
@Controller

public class LoginAction {

	@RequestMapping(value="/login.do", method=RequestMethod.POST)
	public void login(@RequestBody Map map, HttpServletRequest request,HttpServletResponse response) throws IOException {
		String resultString="";
		String errorres="{\"code\":\"err0000\",\"message\":\"Fail\",\"data\":\"\"}";
		System.out.println(map.toString());
		try{
			String name = (String) map.get("name");
			String password = (String) map.get("password");
			HttpSession session = request.getSession();
			String sessionid = session.getId();
			String sql = "SELECT PASSWORD FROM T_user where USER_NAME = '"+name+"';";
	        String pd  = DBUtil.selectSql(sql, "PASSWORD");  
	        resultString="{\"code\":\"000000\",\"message\":\"SUCCESS\",\"data\":{\"sessionid\":\""+sessionid+"\"}}";
	        
	        if(!pd.isEmpty()&&password.equals(pd)){
	        	User user= new User(name, password);
		        session.setAttribute("user", user);
	        	response.setContentType("application/json");
	            response.getWriter().write(resultString);
	        }else{
	        	response.setContentType("application/json");
	            response.getWriter().write(errorres);
	        }
	        
		}catch(Exception e){
			response.setContentType("application/json");
            response.getWriter().write(errorres);
		}
		
	}

	
}
