package org.itkk.udf.starter.core.id;

import org.itkk.udf.starter.core.exception.SystemRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.NetworkInterface;

/**
 * 描述 : 分布式id生成器,基于twiter的snowflake
 *
 * @author wangkang
 */
@Slf4j
public class IdWorker {

    /**
     * 描述 : 机器ID( 0 - 31 )
     */
    private long workerId;

    /**
     * 描述 : 数据中心ID( 0 - 31 )
     */
    private long datacenterId;

    /**
     * 描述 : 序列号( 0 - 4095)
     */
    private long sequence = 0L;

    /**
     * 描述 : 上次生产id时间戳
     */
    private long lastTimestamp = -1L;

    public IdWorker() {
        this.datacenterId = getDatacenterId(IdWorkerConstant.MAX_DATACENTER_ID);
        this.workerId = getMaxWorkerId(this.datacenterId, IdWorkerConstant.MAX_WORKER_ID);
        log.info("IdWorker load datacenterId:{} , workerId:{}", this.datacenterId, this.workerId);
    }

    /**
     * 描述 : 构造函数
     *
     * @param workerId     workerId
     * @param datacenterId datacenterId
     */
    public IdWorker(long workerId, long datacenterId) {
        // sanity check for workerId
        if (workerId > IdWorkerConstant.MAX_WORKER_ID || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", IdWorkerConstant.MAX_WORKER_ID));
        }
        if (datacenterId > IdWorkerConstant.MAX_DATACENTER_ID || datacenterId < 0) {
            throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", IdWorkerConstant.MAX_DATACENTER_ID));
        }
        this.workerId = workerId;
        this.datacenterId = datacenterId;
    }

    /**
     * 描述 : 下一个ID
     *
     * @return ID
     */
    public synchronized long nextId() {
        long timestamp = timeGen();
        if (timestamp < lastTimestamp) {
            throw new SystemRuntimeException(String.format("Clock moved backwards. Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & IdWorkerConstant.SEQUENCE_MASK;
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0L;
        }
        lastTimestamp = timestamp;
        return ((timestamp - IdWorkerConstant.TWEPOCH) << IdWorkerConstant.TIMESTAMP_LEFT_SHIFT) | (datacenterId << IdWorkerConstant.DATACENTER_ID_SHIFT) | (workerId << IdWorkerConstant.WORKER_ID_SHIFT) | sequence;
    }

    /**
     * 描述 : 获得下一个毫秒数
     *
     * @param lastTimestampParam lastTimestampParam
     * @return 下一个毫秒数
     */
    protected long tilNextMillis(long lastTimestampParam) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestampParam) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    /**
     * 描述 : 获得当前时间毫秒数
     *
     * @return 当前时间毫秒数
     */
    protected long timeGen() {
        return System.currentTimeMillis();
    }

    /**
     * 获得WorkerId
     *
     * @param datacenterId datacenterId
     * @param maxWorkerId  maxWorkerId
     * @return
     */
    protected long getMaxWorkerId(long datacenterId, long maxWorkerId) {
        StringBuilder mpid = new StringBuilder();
        mpid.append(datacenterId);
        String name = ManagementFactory.getRuntimeMXBean().getName();
        if (StringUtils.isNotEmpty(name)) {
            //GET jvmPid
            mpid.append(name.split("@")[0]);
        }
        //MAC + PID 的 hashcode 获取16个低位
        return (mpid.toString().hashCode() & 0xffff) % (maxWorkerId + 1);
    }

    /**
     * 获得DatacenterId
     *
     * @param maxDatacenterId maxDatacenterId
     * @return long
     */
    protected long getDatacenterId(long maxDatacenterId) {
        long id = 0L;
        try {
            InetAddress ip = InetAddress.getLocalHost();
            NetworkInterface network = NetworkInterface.getByInetAddress(ip);
            if (network == null) {
                id = 1L;
            } else {
                byte[] mac = network.getHardwareAddress();
                if (null != mac) {
                    id = ((0x000000FF & (long) mac[mac.length - 1]) | (0x0000FF00 & (((long) mac[mac.length - 2]) << 8))) >> 6;
                    id = id % (maxDatacenterId + 1);
                }
            }
        } catch (Exception e) {
            throw new SystemRuntimeException(e);
        }
        return id;
    }

}
