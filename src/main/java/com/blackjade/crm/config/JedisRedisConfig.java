package com.blackjade.crm.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Redis缓存配置类
 * 
 * 
 */
@Configuration
public class JedisRedisConfig {
	@Value("${spring.redis.host}")
	private String host;
	@Value("${spring.redis.password}")
	private String password;
	@Value("${spring.redis.port}")
	private int port;
	@Value("${spring.redis.timeout}")
	private int timeout;
	@Value("${spring.redis.jedis.pool.max-idle}")
	private int maxIdle;
	@Value("${spring.redis.jedis.pool.max-wait}")
	private long maxWaitMillis;
	@Value("${spring.redis.database}")
	private int database;// 数据库索引

	@Bean
	JedisConnectionFactory jedisConnectionFactory() {
		RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(
				host, port);
		redisStandaloneConfiguration.setPassword(RedisPassword.of(password));
		redisStandaloneConfiguration.setDatabase(database);
		return new JedisConnectionFactory(redisStandaloneConfiguration);
	}

	@Bean
	public JedisPool redisPoolFactory() {
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		jedisPoolConfig.setMaxIdle(maxIdle);
		jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);

		JedisPool jedisPool = new JedisPool(jedisPoolConfig, host, port,
				timeout, password);
		return jedisPool;
	}
}
