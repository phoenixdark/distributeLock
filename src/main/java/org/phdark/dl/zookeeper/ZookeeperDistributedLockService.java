package org.phdark.dl.zookeeper;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.phdark.dl.AbstractDistributedLockService;
import org.phdark.dl.Lock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 分布式锁ZK实现 客户端需对connectionString赋值
 * 
 * @version 2016年5月23日 上午9:14:52
 */
public class ZookeeperDistributedLockService extends AbstractDistributedLockService{

    /** 连接地址 */
    private String                                       connectionString;

    private CuratorFramework                             client;

    /** zk根节点 */
    private String                                       rootNode = "/locks/";

    protected ConcurrentHashMap<String, InterProcessMutex> locks    = new ConcurrentHashMap<String, InterProcessMutex>();

    private static Logger                                log      = LoggerFactory.getLogger(ZookeeperDistributedLockService.class);

    /**
     * 
     * TODO 初始化信息
     * 初始化zk节点
     */
    private void getZkClient() {
        ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(1000, 3);
        this.client = CuratorFrameworkFactory.newClient(connectionString, retryPolicy);
        this.client.start();
    }

    public void setConnectionString(String connectionString) {
        this.connectionString = connectionString;
    }

    public void doDestroyInternal() {
        if (client != null) {
            client.close();
        }
    }

	@Override
	public Lock getLock(String lockKey)
	{
		if (client == null) {
            getZkClient();
        }
        InterProcessMutex lock = new InterProcessMutex(client, rootNode + lockKey);
        return new ZookeeperLock(this,lock,lockKey);
	}

}
