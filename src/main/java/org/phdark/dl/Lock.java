package org.phdark.dl;
public interface Lock
{

	boolean acquire(long waitTime);
	
	public void release();
}
