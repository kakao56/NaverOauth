package com.oauth.naver.dao.impl;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.oauth.naver.dao.OauthDao;

@Repository("oauthDao")
public class OauthDaoImpl implements OauthDao{

	@Autowired
	private SqlSessionTemplate sst;
}
