package com.xinhua.language.wanbang.utils;

public class StringUtils {
    public static String transStr(String str){
       return str.replaceAll("%(?![0-9a-fA-F]{2})", "%25");
//       return str.replaceAll("%(?![0-9a-fA-F]{2})", "%25").replaceAll("\"&quot;","\"");
    }
}
