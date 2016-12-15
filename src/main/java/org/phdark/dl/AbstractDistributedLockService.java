package org.phdark.dl;

import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PreDestroy;

import org.apache.log4j.Logger;

public abstract class AbstractDistributedLockService implements DistributedLockService
{
	protected ConcurrentHashMap<String, Lock> locks    = new ConcurrentHashMap<String, Lock>();
	
	private static Logger log      = Logger.getLogger(AbstractDistributedLockService.class);
	
	@PreDestroy
    public void destroy() {
        for (String lockName : locks.keySet()) {
        	Lock lock = locks.get(lockName);
            if (lock != null) {
                try {
                    lock.release();
                } catch (Exception e) {
                    log.warn("释放锁失败", e);
                }
            }
            locks.remove(lockName);
        }
    }
	
	protected void doDestroyInternal() {
		
	}
}
