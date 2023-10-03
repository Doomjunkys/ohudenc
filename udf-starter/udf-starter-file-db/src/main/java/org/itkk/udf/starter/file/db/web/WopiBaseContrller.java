package org.itkk.udf.starter.file.db.web;

import org.itkk.udf.starter.core.exception.SystemRuntimeException;
import org.itkk.udf.starter.file.db.dto.WopiFileInfo;
import org.itkk.udf.starter.file.db.service.DbFileService;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.security.MessageDigest;

public abstract class WopiBaseContrller {

    /**
     * request
     */
    @Autowired
    private HttpServletRequest request;

    /**
     * response
     */
    @Autowired
    private HttpServletResponse response;

    /**
     * dbFileService
     */
    @Autowired
    private DbFileService dbFileService;

    /**
     * 获取文件流
     *
     * @param name
     */
    @GetMapping("files/{name}/contents")
    public void getFile(@PathVariable String name) {
        final String utf8 = "UTF-8";
        final String iso88591 = "ISO-8859-1";
        InputStream fis = null;
        OutputStream toClient = null;
        try {
            // 获得文件
            File file = dbFileService.getFile(name);
            // 取得文件名
            String filename = file.getName();
            // 以流的形式下载文件
            fis = new BufferedInputStream(new FileInputStream(file));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes(utf8), iso88591));
            response.addHeader("Content-Length", "" + file.length());
            toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            toClient.write(buffer);
            toClient.flush();
        } catch (Exception e) {
            throw new SystemRuntimeException(e);
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                if (toClient != null) {
                    toClient.close();
                }
            } catch (Exception e) {
                throw new SystemRuntimeException(e);
            }
        }
    }

    /**
     * 获取文件信息
     *
     * @param name name
     * @return WopiFileInfo
     */
    @RequestMapping("files/{name}")
    public WopiFileInfo getFileInfo(@PathVariable String name) {
        //定义返回值
        WopiFileInfo info = null;
        //获得文件
        File file = dbFileService.getFile(name);
        //判空
        if (file.exists()) {
            //实例化
            info = new WopiFileInfo();
            //填充文件信息
            info.setBaseFileName(file.getName());
            info.setSize(file.length());
            info.setOwnerId("admin");
            info.setVersion(file.lastModified());
            info.setSha256(getHash256(file));
            info.setAllowExternalMarketplace(false);
            info.setUserCanWrite(false);
            info.setSupportsUpdate(false);
            info.setSupportsLocks(false);
            info.setDisablePrint(true);
            info.setDisableTranslation(true);
            info.setUserCanNotWriteRelative(true);
        }
        //返回
        return info;
    }

    /**
     * 获取文件的SHA-256值
     *
     * @param file file
     * @return String
     */
    private String getHash256(File file) {
        String value = "";
        // 获取hash值
        try {
            byte[] buffer = new byte[1024];
            int numRead;
            InputStream fis = new FileInputStream(file);
            //如果想使用SHA-1或SHA-256，则传入SHA-1,SHA-256
            MessageDigest complete = MessageDigest.getInstance("SHA-256");
            do {
                //从文件读到buffer
                numRead = fis.read(buffer);
                if (numRead > 0) {
                    //用读到的字节进行MD5的计算，第二个参数是偏移量
                    complete.update(buffer, 0, numRead);
                }
            } while (numRead != -1);

            fis.close();
            value = new String(Base64.encodeBase64(complete.digest()));
        } catch (Exception e) {
            throw new SystemRuntimeException(e);
        }
        return value;
    }

}
