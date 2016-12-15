package org.phdark.dl.redission;

import java.util.concurrent.TimeUnit;

import org.phdark.dl.Lock;
import org.redisson.api.RLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RedissionLock implements Lock
{

	private final RLock lock;
	
	private final String lockName;
	
	private static Logger log = LoggerFactory.getLogger(RedissionLock.class);
	
	public RedissionLock(RLock lock,String lockName)
	{
		super();
		this.lock = lock;
		this.lockName = lockName;
	}

	@Override
	public boolean tryLock(long waitTime)
	{
		try
		{
			lock.tryLock(waitTime, TimeUnit.MILLISECONDS);
		}
		catch (InterruptedException e)
		{
			log.warn(Thread.currentThread().getId() + "获取锁失败" + ",锁名:" + lockName, e);
		}
		return false;
	}

	@Override
	public void unLock()
	{
		lock.unlock();
	}

	@Override
	public void release()
	{
		lock.delete();
	}

}
