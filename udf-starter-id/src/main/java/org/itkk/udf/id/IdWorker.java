/**
 * IdWorker.java
 * Created at 2016-11-01
 * Created by wangkang
 * Copyright (C) 2016 itkk.org, All rights reserved.
 */
package org.itkk.udf.id;

import lombok.extern.slf4j.Slf4j;
import org.itkk.udf.core.exception.SystemRuntimeException;
import org.itkk.udf.id.domain.Id;

/**
 * 描述 : 分布式id生成器,基于twiter的snowflake
 *
 * @author wangkang
 */
@Slf4j
public class IdWorker {

    /**
     * 描述 : 时间起始标记点，作为基准，一般取系统的最近时间（一旦确定不能变动）
     */
    private long twepoch = 1288834974657L;

    /**
     * 描述 : 机器标识位数
     */
    private long workerIdBits = 5L;

    /**
     * 描述 : 数据中心标识位数
     */
    private long datacenterIdBits = 5L;

    /**
     * 描述 : 机器ID最大值
     */
    private long maxWorkerId = -1L ^ (-1L << workerIdBits);

    /**
     * 描述 : 数据中心ID最大值
     */
    private long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);

    /**
     * 描述 : 毫秒内自增位
     */
    private long sequenceBits = 12L;

    /**
     * 描述 : 机器ID偏左移12位
     */
    private long workerIdShift = sequenceBits;

    /**
     * 描述 : 数据中心ID左移17位
     */
    private long datacenterIdShift = sequenceBits + workerIdBits;

    /**
     * 描述 : 时间毫秒左移22位
     */
    private long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;

    /**
     * 描述 : 序列号最大值 , 一微秒能产生的ID个数
     */
    private long sequenceMask = -1L ^ (-1L << sequenceBits);

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

    /**
     * 描述 : 构造函数
     *
     * @param workerId     workerId
     * @param datacenterId datacenterId
     */
    public IdWorker(long workerId, long datacenterId) {
        // sanity check for workerId
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
        }
        if (datacenterId > maxDatacenterId || datacenterId < 0) {
            throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", maxDatacenterId));
        }
        this.workerId = workerId;
        this.datacenterId = datacenterId;
        log.info("datacenterId:{},workerId:{}", this.datacenterId, this.workerId);
    }

    /**
     * 反解析ID
     *
     * @param id id
     * @return id信息
     */
    public Id reverse(long id) {
        Id reverseId = new Id();
        reverseId.setSequence((id) & ~(-1L << sequenceBits));
        reverseId.setDwId((id >> (workerIdShift)) & ~(-1L << (datacenterIdBits + workerIdBits)));
        reverseId.setWorkerId((id >> workerIdShift) & ~(-1L << workerIdBits));
        reverseId.setDatacenterId((id >> datacenterIdShift) & ~(-1L << datacenterIdBits));
        reverseId.setTimestamp((id >> timestampLeftShift) + twepoch);
        return reverseId;
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
            sequence = (sequence + 1) & sequenceMask;
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0L;
        }
        lastTimestamp = timestamp;
        return ((timestamp - twepoch) << timestampLeftShift) | (datacenterId << datacenterIdShift) | (workerId << workerIdShift) | sequence;
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

}
