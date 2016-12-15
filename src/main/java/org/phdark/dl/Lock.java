package org.phdark.dl;
public interface Lock
{

	boolean tryLock(long waitTime);
	
	public void unLock();
	
	public void release();
}
