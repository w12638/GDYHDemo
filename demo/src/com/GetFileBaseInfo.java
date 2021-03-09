package com;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import db.DBUtil;
import entities.DataReturn;

@RestController
public class GetFileBaseInfo {
	@PostMapping("/getFileBaseInfo")
	public DataReturn getFileBaseInfo(@RequestBody Map<String, Object> map) {
		try {
			
			String sql = "select FIELDNAME as file_Name, APP_VERSION as version,BUILDVERSION as build_Version,icon,APP_TYPE as file_Type from T_APPINFO where APP_ID = '"+map.get("FileId")+"'";
			List appList = DBUtil.selectSql(sql);
			if (appList == null) {
				return new DataReturn("11111111","查询文件下载信息失败！",null);
			}
			
			return new DataReturn("获取文件下载信息成功！",appList.get(0));
			
		} catch (Exception e) {
			e.printStackTrace();
			return new DataReturn("11111111","获取文件下载信息失败！",null);
		}
		
	

	}

}
