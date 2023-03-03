package org.itkk.udf.weixin.mp;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;

/**
 * WeixinMpConsont
 */
public class WeixinMpConsont {

    /**
     * WeixinMpConsont
     */
    private WeixinMpConsont() {

    }

    /**
     * 描述 : 获得节点值
     *
     * @param root     xml文档
     * @param nodeName 节点名称
     * @return 节点值
     */
    public static String getXmlNodeValue(Element root, String nodeName) {
        NodeList toUserNameNodelist = root.getElementsByTagName(nodeName);
        return toUserNameNodelist.item(0).getTextContent();
    }

    /**
     * 描述 : 解析消息
     *
     * @param inputMsg 消息
     * @return 消息
     * @throws ParserConfigurationException 异常
     * @throws SAXException                 异常
     * @throws IOException                  异常
     */
    public static Element formatInputMessage(String inputMsg) throws ParserConfigurationException, SAXException, IOException {
        //解析消息
        StringReader sr = null;
        try { //NOSONAR
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            sr = new StringReader(inputMsg);
            InputSource is = new InputSource(sr);
            Document document = db.parse(is);
            return document.getDocumentElement();
        } finally {
            if (sr != null) {
                sr.close();
            }
        }
    }

}
