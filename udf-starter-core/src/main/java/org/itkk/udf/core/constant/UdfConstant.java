package org.itkk.udf.core.constant;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * UdfConstant
 */
@Slf4j
public class UdfConstant {

    /**
     * UdfConstant
     */
    private UdfConstant() {

    }

    /**
     * 读取返回的信息
     *
     * @param in           输入流
     * @param characterSet 字符集
     * @return 数据
     * @throws IOException 异常
     */
    public static String getData(InputStream in, String characterSet) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = null;
        String line;
        try { //NOSONAR
            br = new BufferedReader(new InputStreamReader(in, characterSet));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    log.warn("close时遇到错误", e);
                }
            }
        }
        return sb.toString();
    }

}
