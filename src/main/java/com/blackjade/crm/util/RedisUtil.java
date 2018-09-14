package com.blackjade.crm.util;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisUtil {
	
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	
	// string key , string value
	public void saveStringValue(String key, String value, long timeout, TimeUnit unit){
		stringRedisTemplate.opsForValue().set(key, value, 180,
				TimeUnit.SECONDS);
	}
	
	// string key , string value
	public String getValue(Object key){
		String value = stringRedisTemplate.opsForValue().get(key);
		
		return value;
	}
	
	public void deleteKey (String key){
		stringRedisTemplate.delete(key);
	}
	// TODO other mothods
}
