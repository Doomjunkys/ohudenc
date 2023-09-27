package org.itkk.udf.starter.core.util;

import org.apache.commons.lang3.ArrayUtils;

public class NumberConvertUtils {

    /**
     *  10进制整数转换为N进制整数。 10进制转换为N进制的方法是：这个10进制数除以N,求出余数，并把余数倒叙排列。 除N取余，倒叙排列
     *  @param tenRadix  十进制整数
     *  @param code 进制字符定义
     *  @return radix进制的字符串
     *  
     */
    public static String tenToN(long tenRadix, String code) {
        StringBuilder buf = new StringBuilder();
        int remainder;
        if (tenRadix == 0) {
            return String.valueOf(code.charAt(0));
        }
        while (tenRadix != 0) {
            remainder = Long.valueOf(tenRadix % code.length()).intValue();// 求余数
            tenRadix = tenRadix / code.length();// 除以基数
            buf.append(code.charAt(remainder));// 保存余数，记得要倒叙排列
        }
        buf.reverse();// 倒叙排列
        return buf.toString();
    }

    /**
     * 返回N进制对应的10进制数。
     *
     * @param num  N进制数(会自动转成大写字符)
     * @param code code
     * @return N进制数对应的10进制数
     *  
     */
    public static long nToTen(String num, String code) {
        final String CODE36 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder stringBuilder = new StringBuilder(num);
        // 反转字符，为了把权重最大的放在最右边，便于下面从左到右遍历，根据下标求权重。
        stringBuilder.reverse();
        //如果不反转，从右向左遍历(从字符串下标大的一端)也可以
        char bitCh;
        long result = 0;
        for (int i = 0; i < stringBuilder.length(); i++) {
            bitCh = stringBuilder.charAt(i);
            bitCh = CODE36.charAt(ArrayUtils.indexOf(code.toCharArray(), bitCh));
            if (bitCh >= '0' && bitCh <= '9') {
                // '0'对应的ASCII码整数：48
                result += (bitCh - '0') * pow(code.length(), i);
            } else if (bitCh >= 'A' && bitCh <= 'Z') {
                // 减去'A'的ASCII码值(65),再加上10
                result += ((bitCh - 'A') + 10) * pow(code.length(), i);
            } else if (bitCh >= 'a' && bitCh <= 'z') {
                // 减去'a'的ASCII码值(97),再加上10
                result += ((bitCh - 'a') + 10) * pow(code.length(), i);
            }
        }
        return result;
    }

    /**
     * 返回x的ex次幂。
     *
     * @param x  底数
     * @param ex 幂指数
     * @return x的ex次幂
     */
    private static int pow(int x, int ex) {
        int result = 1;
        for (int i = 0; i < ex; i++) {
            result *= x;
        }
        return result;
    }

    //public static void main(String[] args) {
    //    final String CODE34 = "0123456789ABCDEFGHJKLMNPQRSTUVWXYZ";
    //    long number = 100000;
    //    for (long i = 0; i <= number; i++) {
    //        String str = NumberConvertUtils.tenToN(i, CODE34);
    //        System.out.println(i + " -> " + str + " -> " + NumberConvertUtils.nToTen(str, CODE34));
    //    }
    //}


    /**
     * 私有化构造函数
     */
    private NumberConvertUtils() {

    }
}
