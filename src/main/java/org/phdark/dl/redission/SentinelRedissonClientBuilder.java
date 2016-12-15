package org.phdark.dl.redission;

import java.util.Objects;
import java.util.Properties;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

public class SentinelRedissonClientBuilder implements RedissonClientBuilder
{

	@Override
	public RedissonClient build(Properties properties)
	{
		Config config = new Config();
        config.useSentinelServers()//
              .setMasterName(getMasterName(properties))//
              .addSentinelAddress(getAddresses(properties))//
              .setTimeout(Integer.parseInt(Objects.toString(properties.get("timeout"), "5000")))//
              .setDatabase(Integer.parseInt(Objects.toString(properties.get("database"), "0")))//
              .setPassword(properties.getProperty("password"));
        return Redisson.create(config);
	}

	
	String getMasterName(Properties properties) {
        final String masterName = properties.getProperty("masterName");
        if (masterName==null || "".equals(masterName.trim())) {
            throw new IllegalArgumentException("masterName can not be blank");
        }
        return masterName;
    }
    String[] getAddresses(Properties properties) {
        final String address = properties.getProperty("address");
        if (address==null || "".equals(address.trim())) {
            throw new IllegalArgumentException("address can not be blank");
        }
        return address.split("[;,\\s\\t\\n]+");
    }
}
