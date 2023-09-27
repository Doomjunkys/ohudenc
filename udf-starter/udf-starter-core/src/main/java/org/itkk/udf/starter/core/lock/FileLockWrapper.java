package org.itkk.udf.starter.core.lock;

import org.itkk.udf.starter.core.CoreConstant;
import org.itkk.udf.starter.core.CoreProperties;
import org.itkk.udf.starter.core.CoreUtil;
import org.itkk.udf.starter.core.exception.SystemRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.Closeable;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 基于NIO的文件锁包装类
 */
@Slf4j
public class FileLockWrapper implements Closeable {

    /**
     * 锁名称
     */
    private String name;

    /**
     * 文件通道
     */
    private FileChannel fileChannel;

    /**
     * 文件锁
     */
    private FileLock lock;

    /**
     * 构造函数
     *
     * @param name 锁文件名称
     */
    public FileLockWrapper(String name) {
        this.initFileLock(name, false);
    }

    /**
     * 构造函数
     *
     * @param name 锁文件名称
     * @param wait 是否等待
     */
    public FileLockWrapper(String name, boolean wait) {
        this.initFileLock(name, wait);
    }

    /**
     * 锁状态
     *
     * @return boolean
     */
    public boolean isValid() {
        return this.lock != null && this.lock.isValid();
    }

    @Override
    public void close() {
        this.unlock();
    }

    /**
     * 初始化文件锁
     *
     * @param name name
     * @param wait wait
     */
    private void initFileLock(String name, boolean wait) {
        //获得配置
        CoreProperties coreProperties = CoreConstant.applicationContext.getBean(CoreProperties.class);
        //判空
        if (StringUtils.isBlank(coreProperties.getFileLockRootDir())) {
            throw new SystemRuntimeException("未配置文件锁根目录");
        }
        //设置属性
        this.name = coreProperties.getFileLockRootDir() + name;
        //初始化锁目录
        this.initDir();
        //判断是否等待
        if (wait) {
            //获得文件通道
            do {
                this.initFileChannel();
            } while (this.fileChannel == null);
            //获得锁
            do {
                this.initTryLock();
            } while (this.lock == null);
        } else {
            //获得文件通道
            this.initFileChannel();
            //获得锁
            this.initTryLock();
        }
    }

    /**
     * 初始化锁目录
     */
    private void initDir() {
        try {
            Files.createDirectories(Paths.get(this.name).getParent());
        } catch (Exception e) {
            log.debug("{} --> {} 创建文件锁根目录失败,错误类型为 : {}", CoreUtil.getTraceId(), Paths.get(this.name).getParent(), e.getClass().getName());
        }
    }

    /**
     * 初始化锁文件通道
     */
    private void initFileChannel() {
        //构造文件通道(读,写,必须新创建,关闭后删除)
        try {
            this.fileChannel = FileChannel.open(Paths.get(this.name), StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.CREATE_NEW, StandardOpenOption.DELETE_ON_CLOSE);
            log.debug("{} --> {} 获得文件通道成功", CoreUtil.getTraceId(), this.name);
        } catch (Exception e) {
            log.debug("{} --> {} 获得文件通道失败,错误类型为 : {}", CoreUtil.getTraceId(), this.name, e.getClass().getName());
        }
    }

    /**
     * 初始化文件锁
     */
    private void initTryLock() {
        //通道判空
        if (this.fileChannel != null) {
            //获得锁
            try {
                this.lock = this.fileChannel.tryLock();
                log.debug("{} --> {} 获得文件锁成功", CoreUtil.getTraceId(), this.name);
            } catch (OverlappingFileLockException | IOException e) {
                log.debug("{} --> {} 获得文件锁失败,错误类型为 : {}", CoreUtil.getTraceId(), this.name, e.getClass().getName());
            }
        }
    }

    /**
     * 释放文件锁
     */
    private void unlock() {
        //释放锁
        if (this.isValid()) {
            try {
                this.lock.release();
                log.debug("{} --> {} 释放文件锁成功", CoreUtil.getTraceId(), this.name);
            } catch (Exception e) {
                log.debug("{} --> {} 释放文件锁失败,错误类型为 : {}", CoreUtil.getTraceId(), this.name, e.getClass().getName());
            } finally {
                this.lock = null;
            }
        }
        //释放文件通道
        if (this.fileChannel != null) {
            try {
                this.fileChannel.close();
                log.debug("{} --> {} 释放文件通道锁成功", CoreUtil.getTraceId(), this.name);
            } catch (Exception e) {
                log.debug("{} --> {} 释放文件通道锁失败,错误类型为 : {}", CoreUtil.getTraceId(), this.name, e.getClass().getName());
            } finally {
                this.fileChannel = null;
            }
        }
    }

    /**
     * 锁文件列表
     *
     * @return List<String>
     * @throws IOException
     */
    public static List<Path> listAllLockFileName() throws IOException {
        //获得配置
        CoreProperties coreProperties = CoreConstant.applicationContext.getBean(CoreProperties.class);
        //判空
        if (StringUtils.isNotBlank(coreProperties.getFileLockRootDir())) {
            return Files.list(Paths.get(coreProperties.getFileLockRootDir())).collect(Collectors.toList());
        }
        //默认返回
        return new ArrayList<>();
    }

}
