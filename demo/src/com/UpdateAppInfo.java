package com;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import db.DBUtil;

@Controller
public class UpdateAppInfo {
	@RequestMapping("updateAppInfo.do")
	public String login(String appId, String name, String desc,HttpServletRequest request) {
		try {
			System.out.println("appId:"+appId+",name:"+name+",desc:"+desc);
			
			String sql = "UPDATE app set name ='"+ name+"',desc='"+desc+"' where app_id = '"+appId+"';";
		    DBUtil.operationSql(sql);
		    String selectsql =  "select APP_ID,USER_ID,name,version,plateform,packType,desc from App;" ;
		    List list = DBUtil.selectSql(selectsql);
		    System.out.println(list.toString());
		    request.setAttribute("list", list);
		    
		} catch (Exception e) {
			return "error";
		}
		return "queryAppSuccess";
	}
}
