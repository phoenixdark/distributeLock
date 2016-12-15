package org.phdark.dl.redission;

import java.util.Properties;

import org.redisson.api.RedissonClient;

public interface RedissonClientBuilder
{

	RedissonClient build(Properties properties);
	
}
