package com.oauth.naver.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.oauth.naver.service.OauthService;

@Controller
public class OauthoController {

	private String clientId = "JjYgNeLqXGD4cKjASZH_";
	private String clientSecret = "4kbGANrWc_";
	private String callBackUrl = "http://192.168.219.190/api/member/oauth2c/naverlogincallback";

	@Resource(name = "oauthService")
	private OauthService oauthService;

	@RequestMapping("api/member/oauth2c/naverlogin")
	public String naverLogin(HttpServletRequest request) throws Exception {

		String redirectURI = URLEncoder.encode(callBackUrl, "UTF-8");

		SecureRandom random = new SecureRandom();
		String state = new BigInteger(130, random).toString(32);

		request.getSession().setAttribute("state", state);
		
		String apiURL = "redirect: https://nid.naver.com/oauth2.0/authorize?response_type=code";
		apiURL += "&client_id=" + clientId;
		apiURL += "&redirect_uri=" + redirectURI;
		apiURL += "&state=" + state;
		

		return apiURL;
	}

	@RequestMapping("api/member/oauth2c/naverlogincallback")
	public ModelAndView naverLoginCallback(@RequestParam Map<String, Object> map, HttpServletRequest request, ModelAndView mv) throws Exception {

		String code = request.getParameter("code");
		String state = request.getParameter("state");
		String redirectURI = URLEncoder.encode(callBackUrl, "UTF-8");
		
		String apiURL = "https://nid.naver.com/oauth2.0/token?grant_type=authorization_code&";
		apiURL += "client_id=" + clientId;
		apiURL += "&client_secret=" + clientSecret;
		apiURL += "&redirect_uri=" + redirectURI;
		apiURL += "&code=" + code;
		apiURL += "&state=" + state;

		try {
			URL url = new URL(apiURL);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			int responseCode = con.getResponseCode();
			BufferedReader br;
			
			if (responseCode != 200) { // 에러 발생
				mv.setViewName("error");
				return mv;
			}
			
			br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			
			ObjectMapper objMapper = new ObjectMapper();
			
			Map<String, String> token = objMapper.readValue(br, new TypeReference<Map<String, String>>() {});
			
			br.close();
			
			mv.addObject("access_token", token.get("access_token"));
			mv.addObject("refresh_token", token.get("refresh_token"));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		mv.setViewName("naverlogin");
		return mv;
	}
}
