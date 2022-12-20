package org.itkk.udf.file.aliyun.oss.api;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.PolicyConditions;
import org.apache.commons.lang3.StringUtils;
import org.itkk.udf.core.ApplicationConfig;
import org.itkk.udf.core.exception.ParameterValidException;
import org.itkk.udf.file.aliyun.oss.api.domain.PolicyResult;
import org.itkk.udf.file.aliyun.oss.api.meta.AliyunOssAccessMeta;
import org.itkk.udf.file.aliyun.oss.api.meta.AliyunOssPathMeta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.sql.Date;

/**
 * OssWarpper
 */
@Component
public class OssWarpper {

    /**
     * NUM_1000
     */
    private static final long NUM_1000 = 1000;

    /**
     * NUM_1048576000
     */
    private static final long NUM_1048576000 = 1048576000;

    /**
     * applicationConfig
     */
    @Autowired
    private ApplicationConfig applicationConfig;

    /**
     * aliyunOssProperties
     */
    @Autowired
    private AliyunOssProperties aliyunOssProperties;

    /**
     * getPolicy
     *
     * @param code code
     * @return PolicyResult
     * @throws UnsupportedEncodingException UnsupportedEncodingException
     */
    public PolicyResult getPolicy(String code) throws UnsupportedEncodingException {
        //判空
        if (!aliyunOssProperties.getAuth().containsKey(code) || !aliyunOssProperties.getPath().containsKey(code)) {
            throw new ParameterValidException("aliyun oss code:" + code + "未定义", null); //NOSONAR
        }
        //获得认证信息 和 路径信息
        AliyunOssAccessMeta auth = aliyunOssProperties.getAuth().get(code);
        AliyunOssPathMeta path = aliyunOssProperties.getPath().get(code);
        //实例化oss对象
        OSSClient client = new OSSClient(auth.getEndPoint(), auth.getAccessId(), auth.getAccessKey());
        //获得Policy
        try {
            //构造超时时间
            long expireEndTime = System.currentTimeMillis() + auth.getExpireTime() * NUM_1000;
            Date expiration = new Date(expireEndTime);
            PolicyConditions policyConds = new PolicyConditions();
            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, NUM_1048576000);
            String postPolicy = client.generatePostPolicy(expiration, policyConds);
            byte[] binaryData = postPolicy.getBytes(applicationConfig.getEncoding());
            String encodedPolicy = BinaryUtil.toBase64String(binaryData);
            String postSignature = client.calculatePostSignature(postPolicy);
            //定义返回参数
            PolicyResult policyResult = new PolicyResult();
            policyResult.setAccessId(auth.getAccessId());
            policyResult.setPolicy(encodedPolicy);
            policyResult.setSignature(postSignature);
            policyResult.setExpire(String.valueOf(expireEndTime / NUM_1000));
            policyResult.setAliyunOssPathMeta(path);
            //返回
            return policyResult;
        } finally {
            client.shutdown();
        }
    }

    /**
     * 返回签名URL
     *
     * @param code      code
     * @param objectKey objectKey
     * @return String
     */
    public String getPresignedUrl(String code, String objectKey) {
        //判空
        if (!aliyunOssProperties.getAuth().containsKey(code) || !aliyunOssProperties.getPath().containsKey(code)) {
            throw new ParameterValidException("aliyun oss code:" + code + "未定义", null);
        }
        //获得认证信息 和 路径信息
        AliyunOssAccessMeta auth = aliyunOssProperties.getAuth().get(code);
        AliyunOssPathMeta path = aliyunOssProperties.getPath().get(code);
        //实例化oss对象
        OSSClient client = new OSSClient(auth.getEndPoint(), auth.getAccessId(), auth.getAccessKey());
        try {
            //判断文件是否存在
            if (!client.doesObjectExist(path.getBucketName(), objectKey)) {
                throw new ParameterValidException(objectKey + "不存在", null);
            }
            //构造超时时间
            long expireEndTime = System.currentTimeMillis() + auth.getExpireTime() * NUM_1000;
            Date expiration = new Date(expireEndTime);
            // 生成URL
            URL url = client.generatePresignedUrl(path.getBucketName(), objectKey, expiration);
            //返回地址判断
            if (StringUtils.isNotBlank(path.getCdnHost())) {
                //返回CDN地址
                return path.getCdnHost() + url.getFile();
            } else {
                //返回OSS公网地址
                return url.getFile();
            }
        } finally {
            client.shutdown();
        }
    }

    /**
     * 删除文件
     *
     * @param code      code
     * @param objectKey objectKey
     */
    public void delete(String code, String objectKey) {
        //判空
        if (!aliyunOssProperties.getAuth().containsKey(code) || !aliyunOssProperties.getPath().containsKey(code)) {
            throw new ParameterValidException("aliyun oss code:" + code + "未定义", null);
        }
        //获得认证信息 和 路径信息
        AliyunOssAccessMeta auth = aliyunOssProperties.getAuth().get(code);
        AliyunOssPathMeta path = aliyunOssProperties.getPath().get(code);
        //实例化oss对象
        OSSClient client = new OSSClient(auth.getEndPoint(), auth.getAccessId(), auth.getAccessKey());
        try {
            //判断是否存在,如果存在,则删除
            if (client.doesObjectExist(path.getBucketName(), objectKey)) {
                client.deleteObject(path.getBucketName(), objectKey);
            }
        } finally {
            client.shutdown();
        }
    }

    /**
     * 判断文件是否存在
     *
     * @param code      code
     * @param objectKey objectKey
     * @return boolean
     */
    public boolean checkExist(String code, String objectKey) {
        //判空
        if (!aliyunOssProperties.getAuth().containsKey(code) || !aliyunOssProperties.getPath().containsKey(code)) {
            throw new ParameterValidException("aliyun oss code:" + code + "未定义", null);
        }
        //获得认证信息 和 路径信息
        AliyunOssAccessMeta auth = aliyunOssProperties.getAuth().get(code);
        AliyunOssPathMeta path = aliyunOssProperties.getPath().get(code);
        //实例化oss对象
        OSSClient client = new OSSClient(auth.getEndPoint(), auth.getAccessId(), auth.getAccessKey());
        try {
            return client.doesObjectExist(path.getBucketName(), objectKey);
        } finally {
            client.shutdown();
        }
    }

    /**
     * 文件上传
     *
     * @param code code
     * @param file file
     * @return String
     */
    public String uploadFile(String code, File file) {
        //判空
        if (!aliyunOssProperties.getAuth().containsKey(code) || !aliyunOssProperties.getPath().containsKey(code)) {
            throw new ParameterValidException("aliyun oss code:" + code + "未定义", null);
        }
        //获得认证信息 和 路径信息
        AliyunOssAccessMeta auth = aliyunOssProperties.getAuth().get(code);
        AliyunOssPathMeta path = aliyunOssProperties.getPath().get(code);
        //实例化oss对象
        OSSClient client = new OSSClient(auth.getEndPoint(), auth.getAccessId(), auth.getAccessKey());
        try {
            String objectKey = "upload/" + file.getName();
            client.putObject(path.getBucketName(), objectKey, file);
            return objectKey;
        } finally {
            client.shutdown();
        }
    }

}
