/**
 * IdWorker.java
 * Created at 2016-11-01
 * Created by wangkang
 * Copyright (C) 2016 egridcloud.com, All rights reserved.
 */
package com.egridcloud.udf.core;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.egridcloud.udf.core.exception.SystemRuntimeException;

/**
 * 描述 : 分布式id生成器,基于twiter的snowflake
 * 
 * @author wangkang
 *
 */
public class IdWorker {

  /**
   * 描述 : 日志
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(IdWorker.class);

  /**
   * 描述 : 机器ID
   */
  private long workerId;

  /**
   * 描述 : 数据中心ID
   */
  private long datacenterId;

  /**
   * 描述 : 并发控制
   */
  private long sequence = 0L;

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
   * 描述 : sequenceMask
   */
  private long sequenceMask = -1L ^ (-1L << sequenceBits);

  /**
   * 描述 : 上次生产id时间戳
   */
  private long lastTimestamp = -1L;

  /**
   * 描述 : 构造函数
   *
   */
  public IdWorker() {
    this.datacenterId = getDatacenterId(maxDatacenterId);
    this.workerId = getWorkerId(maxWorkerId);
    LOGGER.info("datacenterId:{},workerId:{}", this.datacenterId, this.workerId);
  }

  /**
   * 描述 : 构造函数
   *
   * @param workerId workerId
   * @param datacenterId datacenterId
   */
  public IdWorker(long workerId, long datacenterId) {
    // sanity check for workerId
    if (workerId > maxWorkerId || workerId < 0) {
      throw new IllegalArgumentException(
          String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
    }
    if (datacenterId > maxDatacenterId || datacenterId < 0) {
      throw new IllegalArgumentException(
          String.format("datacenter Id can't be greater than %d or less than 0", maxDatacenterId));
    }
    this.workerId = workerId;
    this.datacenterId = datacenterId;
    LOGGER.info("datacenterId:{},workerId:{}", maxDatacenterId, maxWorkerId);
  }

  /**
   * 描述 : 下一个ID
   *
   * @return ID
   */
  public synchronized long nextId() {
    long timestamp = timeGen();
    if (timestamp < lastTimestamp) {
      throw new SystemRuntimeException(
          String.format("Clock moved backwards. Refusing to generate id for %d milliseconds",
              lastTimestamp - timestamp));
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
    return ((timestamp - twepoch) << timestampLeftShift) | (datacenterId << datacenterIdShift)
        | (workerId << workerIdShift) | sequence;
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
   * 描述 : 获取机器ID
   *
   * @param maxWorkerIdparam maxWorkerIdparam
   * @return 机器ID
   */
  protected long getWorkerId(long maxWorkerIdparam) {
    return Math.abs(UUID.randomUUID().toString().hashCode() % maxWorkerIdparam);
  }

  /**
   * 描述 : 获取数据中心ID
   *
   * @param maxDatacenterIdparam maxDatacenterIdparam
   * @return 数据中心ID
   */
  protected long getDatacenterId(long maxDatacenterIdparam) {
    return Math.abs(UUID.randomUUID().toString().hashCode() % maxDatacenterIdparam);
  }

  /**
   * 描述 : 获取机器ID(备用,docker容器中有问题)
   *
   * @param datacenterIdparam datacenterIdparam
   * @param maxWorkerIdparam maxWorkerIdparam
   * @return 机器ID
   */
  protected long getWorkerIdBack(long datacenterIdparam, long maxWorkerIdparam) {
    final int numbera = 0xffff;
    StringBuilder mpid = new StringBuilder();
    mpid.append(datacenterIdparam);
    String name = ManagementFactory.getRuntimeMXBean().getName();
    if (!name.isEmpty()) {
      //GET jvmPid
      mpid.append(name.split("@")[0]);
    }
    //MAC + PID 的 hashcode 获取16个低位
    return (mpid.toString().hashCode() & numbera) % (maxWorkerIdparam + 1);
  }

  /**
   * 描述 : 获取数据中心ID(备用,docker容器中有问题)
   *
   * @param maxDatacenterIdparam maxDatacenterIdparam
   * @return 数据中心ID
   */
  protected long getDatacenterIdBack(long maxDatacenterIdparam) {
    final int numbera = 0x000000FF;
    final int numberb = 0x0000FF00;
    final int number2 = 2;
    final int number6 = 6;
    final int number8 = 8;
    long id = 0L;
    try {
      InetAddress ip = InetAddress.getLocalHost();
      NetworkInterface network = NetworkInterface.getByInetAddress(ip);
      if (network == null) {
        id = 1L;
      } else {
        byte[] mac = network.getHardwareAddress();
        id = ((numbera & (long) mac[mac.length - 1])
            | (numberb & (((long) mac[mac.length - number2]) << number8))) >> number6;
        id = id % (maxDatacenterIdparam + 1);
      }
    } catch (IOException e) {
      throw new SystemRuntimeException(e);
    }
    return id;
  }

}
