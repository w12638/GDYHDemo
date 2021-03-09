package com;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import entities.DataReturn;
import util.CosStsClient;

@RestController
@RequestMapping("/kaifapingtai")
public class GetUpLoadFileAuthorization {
	@GetMapping("/getUpLoadFileAuthorization")
	public DataReturn getCredential() {
		TreeMap<String, Object> config = new TreeMap<String, Object>();

		try {
			// 云 api 密钥 SecretId
			config.put("secretId", "AKIDVhrqY9ImZCcqx0vTqKXXMxLrDLCuJg9P");
			// 云 api 密钥 SecretKey
			config.put("secretKey", "PZllOtUpkuBNLEWedhlHYZQ1OXotiGXb");

//            if (properties.containsKey("https.proxyHost")) {
//                System.setProperty("https.proxyHost", properties.getProperty("https.proxyHost"));
//                System.setProperty("https.proxyPort", properties.getProperty("https.proxyPort"));
//            }

			// 设置域名
			// config.put("host", "sts.internal.tencentcloudapi.com");

			// 临时密钥有效时长，单位是秒
			config.put("durationSeconds", 1800);

			// 换成你的 bucket
			config.put("bucket", "cebfir-1301179215");
			// 换成 bucket 所在地区
			config.put("region", "ap-beijing");

			// 这里改成允许的路径前缀，可以根据自己网站的用户登录态判断允许上传的具体路径，例子： a.jpg 或者 a/* 或者 * (使用通配符*存在重大安全风险,
			// 请谨慎评估使用)
			config.put("allowPrefix", "*");
			// 可以通过 allowPrefixes 指定前缀数组
//            config.put("allowPrefixes", new String[] {
//                    "exampleobject",
//                    "exampleobject2"
//            });

			// 密钥的权限列表。简单上传和分片需要以下的权限，其他权限列表请看
			// https://cloud.tencent.com/document/product/436/31923
			String[] allowActions = new String[] {
					// 简单上传
					"name/cos:PutObject", "name/cos:PostObject",
					// 分片上传
					"name/cos:InitiateMultipartUpload", "name/cos:ListMultipartUploads", "name/cos:ListParts",
					"name/cos:UploadPart", "name/cos:CompleteMultipartUpload" };
			config.put("allowActions", allowActions);

			JSONObject credential = CosStsClient.getCredential(config);
			System.out.println(credential.toString(4));
			Map<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
			Map<String, Object> map = new LinkedHashMap<String, Object>();
			JSONObject jSONObject = credential.getJSONObject("credentials");
			map.put("tmpSecretId", jSONObject.getString("tmpSecretId"));
			map.put("tmpSecretKey", jSONObject.getString("tmpSecretKey"));
			map.put("sessionToken", jSONObject.getString("sessionToken"));
			linkedHashMap.put("credentials", map);
			linkedHashMap.put("requestId", credential.getString("requestId"));
			linkedHashMap.put("expiration", credential.getString("expiration"));
			linkedHashMap.put("startTime", credential.getInt("startTime"));
			linkedHashMap.put("expiredTime", credential.getInt("expiredTime"));

			return new DataReturn("临时密钥", linkedHashMap);
		} catch (Exception e) {
			e.printStackTrace();
			return new DataReturn("1111", "获取临时密钥失败", null);

		}
	}

}
