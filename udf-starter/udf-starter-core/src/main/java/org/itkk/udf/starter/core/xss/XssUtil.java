package org.itkk.udf.starter.core.xss;

import java.util.regex.Pattern;

public class XssUtil {

    /**
     * 被替换的字符
     */
    private static final String REPLACE_STRING = "*";

    /**
     * 将容易引起xss & sql漏洞的半角字符直接替换成全角字符
     *
     * @param s s
     * @return String
     */
    public static String xssEncode(String s) {
        if (s == null || s.isEmpty()) {
            return s;
        } else {
            s = stripXSSAndSql(s);
        }
        StringBuilder sb = new StringBuilder(s.length() + 16);
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '>':
                    sb.append("＞");// 转义大于号
                    break;
                case '<':
                    sb.append("＜");// 转义小于号
                    break;
                case '\'':
                    sb.append("＇");// 转义单引号
                    break;
                case '\"':
                    sb.append("＂");// 转义双引号
                    break;
                /*case '&':
                    sb.append("＆");// 转义&
                    break;
                case '#':
                    sb.append("＃");// 转义#
                    break;*/
                default:
                    sb.append(c);
                    break;
            }
        }
        return sb.toString();
    }

    /**
     * 防止xss跨脚本攻击（替换，根据实际情况调整）
     */
    private static final Pattern
            scriptPattern = Pattern.compile("<[\r\n| | ]*script[\r\n| | ]*>(.*?)</[\r\n| | ]*script[\r\n| | ]*>", Pattern.CASE_INSENSITIVE),
            scriptPattern2 = Pattern.compile("src[\r\n| | ]*=[\r\n| | ]*[\\\"|\\\'](.*?)[\\\"|\\\']", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            scriptPattern3 = Pattern.compile("</[\r\n| | ]*script[\r\n| | ]*>", Pattern.CASE_INSENSITIVE),
            scriptPattern4 = Pattern.compile("<[\r\n| | ]*script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            scriptPattern5 = Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            scriptPattern6 = Pattern.compile("e-xpression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            scriptPattern7 = Pattern.compile("javascript[\r\n| | ]*:[\r\n| | ]*", Pattern.CASE_INSENSITIVE),
            scriptPattern8 = Pattern.compile("vbscript[\r\n| | ]*:[\r\n| | ]*", Pattern.CASE_INSENSITIVE),
            scriptPattern9 = Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            scriptPattern10 = Pattern.compile("(?:')|(?:--)|(/\\*(?:.|[\\n\\r])*?\\*/)|(\\b(select|and|or|insert|trancate|char|into|substr|ascii|declare|exec|count|master|into|drop|execute|union|where|limit|group by|hex|set)\\b)", Pattern.CASE_INSENSITIVE),
            scriptPattern11 = Pattern.compile("\\*\\/(\\s|.)*?\\/\\*", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);

    private static String stripXSSAndSql(String value) {
        if (value != null) {
            // Avoid anything between script tags
            value = scriptPattern.matcher(value).replaceAll(REPLACE_STRING);
            // Avoid anything in a src="http://www.yihaomen.com/article/java/..." type of e-xpression
            value = scriptPattern2.matcher(value).replaceAll(REPLACE_STRING);
            // Remove any lonesome </script> tag
            value = scriptPattern3.matcher(value).replaceAll(REPLACE_STRING);
            // Remove any lonesome <script ...> tag
            value = scriptPattern4.matcher(value).replaceAll(REPLACE_STRING);
            // Avoid eval(...) expressions
            value = scriptPattern5.matcher(value).replaceAll(REPLACE_STRING);
            // Avoid e-xpression(...) expressions
            value = scriptPattern6.matcher(value).replaceAll(REPLACE_STRING);
            // Avoid javascript:... expressions
            value = scriptPattern7.matcher(value).replaceAll(REPLACE_STRING);
            // Avoid vbscript:... expressions
            value = scriptPattern8.matcher(value).replaceAll(REPLACE_STRING);
            // Avoid onload= expressions
            value = scriptPattern9.matcher(value).replaceAll(REPLACE_STRING);
            // Avoid sql= expressions
            value = scriptPattern10.matcher(value).replaceAll(REPLACE_STRING);
            // Avoid */xxx/*(...) expressions
            value = scriptPattern11.matcher(value).replaceAll(REPLACE_STRING);
        }
        return value;
    }

    /**
     * 私有化构造函数
     */
    private XssUtil() {

    }
}
