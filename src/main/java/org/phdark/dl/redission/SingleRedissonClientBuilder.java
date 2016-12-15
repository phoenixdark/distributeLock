package org.phdark.dl.redission;

import java.util.Objects;
import java.util.Properties;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

public class SingleRedissonClientBuilder implements RedissonClientBuilder
{

	@Override
	public RedissonClient build(Properties properties)
	{
		Config config = new Config();
        config.useSingleServer()
              .setAddress(properties.getProperty("address", "localhost:6379"))
              .setDatabase(Integer.parseInt(Objects.toString(properties.get("database"), "0")))
              .setTimeout(Integer.parseInt(Objects.toString(properties.get("timeout"), "5000"))) 
              .setPassword(properties.getProperty("password"));
        return Redisson.create(config);
	}
	
}
