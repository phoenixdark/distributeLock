package org.phdark.dl.zookeeper;

import java.util.concurrent.TimeUnit;

import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.phdark.dl.Lock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZookeeperLock implements Lock
{

	private final InterProcessMutex lock;
	
	private final String lockName;
	
	private final ZookeeperDistributedLockService service;
	
	private static Logger log = LoggerFactory.getLogger(ZookeeperLock.class);
	
	public ZookeeperLock(ZookeeperDistributedLockService service,InterProcessMutex lock,String lockName)
	{
		super();
		this.lock = lock;
		this.lockName = lockName;
		this.service = service;
	}



	@Override
	public boolean tryLock(long waitTime)
	{
		boolean result = false;
        try {
            log.info(Thread.currentThread().getId() + "正在获取锁" + ",锁名:" + lockName);
            result = lock.acquire(waitTime, TimeUnit.MILLISECONDS);

        } catch (Exception e) {
            log.warn(Thread.currentThread().getId() + "获取锁失败" + ",锁名:" + lockName, e);
        } finally {
            if (result) {
            	service.locks.put(Thread.currentThread() + lockName, lock);
            }
        }
        return result;
	}



	@Override
	public void unLock()
	{
        try {
            String lname = Thread.currentThread() + lockName;
            InterProcessMutex lock = service.locks.get(lname);
            if (lock != null) {
                lock.release();
            }
            service.locks.remove(lockName);
        } catch (Exception e) {
            log.warn("释放锁失败", e);
        }
	}



	@Override
	public void release()
	{
		try
		{
			lock.release();
		}
		catch (Exception e)
		{
			log.warn("释放锁失败", e);
		}
	}

}
