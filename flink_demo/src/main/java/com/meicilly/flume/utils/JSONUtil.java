package com.meicilly.flume.utils;

import com.alibaba.fastjson.JSONObject;

public class JSONUtil {
    // TODO: 2023/6/4 校验json是不是一个合法的json 通过异常捕捉来校验
    public static boolean isJSONValidate(String log) {
        try {
            JSONObject.parseObject(log);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static void main(String[] args) {

    }
}
