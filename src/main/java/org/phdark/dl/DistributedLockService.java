package org.phdark.dl;
public interface DistributedLockService
{

	Lock getLock(String lockKey);
	
}
