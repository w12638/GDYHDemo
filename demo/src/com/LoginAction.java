package com;




import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import db.DBUtil;
import entities.DataReturn;
import entities.User;
//@CrossOrigin(origins = "*", maxAge = 3600)
@RestController

public class LoginAction {

	@RequestMapping(value="/login.do", method=RequestMethod.POST)
	public DataReturn login(@RequestBody Map map, HttpServletRequest request,HttpServletResponse response) throws IOException {
		System.out.println(map.toString());
		try{
			String name = (String) map.get("name");
			String password = (String) map.get("password");
			HttpSession session = request.getSession();
			String sessionid = session.getId();
			String sql = "SELECT PASSWORD FROM T_user where USER_NAME = '"+name+"';";
	        String pd  = DBUtil.selectSql(sql, "PASSWORD");  
	        
	        if(!pd.isEmpty()&&password.equals(pd)){
	        	User user= new User(name, password);
		        session.setAttribute("user", user);
		        Map<String,Object> linkedHashMap = new LinkedHashMap<String, Object>();
		        linkedHashMap.put("sessionid", sessionid);
		        return new DataReturn("登录成功!",linkedHashMap);
	        }else{
	        	return new DataReturn("111111","请输入用户名和密码!",null);
	        }
	        
		}catch(Exception e){
            return new DataReturn("111111","请登录失败!",null);
		}
		
	}
	
	
}
