package com;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import db.DBUtil;
@Controller
public class QueryAppsList {
	@RequestMapping("queryAppList.do")
	public String query(HttpServletRequest request) {
		try {
			
			String sql = "select APP_ID,USER_ID,name,version,plateform,packType,desc from App;";
		    DBUtil.operationSql(sql);
		    List list = DBUtil.selectSql(sql);
		    System.out.println(list.toString());
		    request.setAttribute("list", list);
		    
		} catch (Exception e) {
			return "error";
		}
		return "queryAppSuccess";
	}

}
