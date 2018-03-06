package com.gph.libr.utils;

import static android.text.TextUtils.isEmpty;

/**
 * Created by guopenghui-961057759@qq.com on 2018/3/1 0001.
 */

public class EncodeUtils {
    /**
     * 字符串转换为url编码格式
     *
     * @param s 等待转换的
     * @return url编码字符串
     */
    public static String toUrlEncode(String s) {
        if (isEmpty(s))
            return s;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c >= 0 && c <= 255) {
                sb.append(c);
            } else {
                byte[] b;
                try {
                    b = String.valueOf(c).getBytes("utf-8");
                } catch (Exception ex) {
                    System.out.println(ex);
                    b = new byte[0];
                }
                for (int j = 0; j < b.length; j++) {
                    int k = b[j];
                    if (k < 0)
                        k += 256;
                    sb.append("%" + Integer.toHexString(k).toUpperCase());
                }
            }
        }
        return sb.toString().replaceAll(" ", "%20");
    }
}
