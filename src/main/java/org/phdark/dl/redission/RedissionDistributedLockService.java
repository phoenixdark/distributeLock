package org.phdark.dl.redission;

import java.util.Properties;

import org.phdark.dl.AbstractDistributedLockService;
import org.phdark.dl.Lock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

public class RedissionDistributedLockService extends AbstractDistributedLockService
{

	private RedissonClient redisson;
	
	private RedissionDistributedLockService(Properties properties,RedisDeployType deployType) {
		RedissonClientBuilder builder =null;
		switch(deployType) {
			case SENTINEL:
				builder = new SentinelRedissonClientBuilder();
				break;
			case SINGLE:
				builder = new SingleRedissonClientBuilder();
				break;
			case CLUSTER:
				throw new IllegalArgumentException("暂不支持以cluster方式构建redis锁");
		}
		redisson = builder.build(properties);
	}
	
	@Override
	public Lock getLock(String lockKey)
	{
		final RLock lock = redisson.getLock(lockKey);
		return new RedissionLock(lock,lockKey);
	}

	@Override
	protected void doDestroyInternal()
	{
		if (redisson != null) {
			redisson.shutdown();
		}
	}

	
}

