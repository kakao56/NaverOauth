package com.oauth.naver.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.oauth.naver.dao.OauthDao;
import com.oauth.naver.service.OauthService;

@Service("oauthService")
public class OauthServiceImpl implements OauthService{

	@Resource(name="oauthDao")
	private OauthDao oauthDao;
}
