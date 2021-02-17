package com.neu.video_recognize.tool;

public class Tools {

    // 生成长度为10，只包含0-9a-zA-z的随机字符串
    public static String randomString(){
        char[] chars = new char[10];
        for (int i = 0; i < chars.length; i++){
            int r = (int) (Math.random() * 1000000);
            r = r % 62;
            if (r <= 9){
                chars[i] = (char) ('0' + r);
            }else if (r <= 35){
                chars[i] = (char) (r - 10 + 'A');
            }else{
                chars[i] = (char) (r - 36 + 'a');
            }
        }
        return new String(chars);
    }
}
