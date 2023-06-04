package com.meicilly.flume.interceptor;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;

import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;
import com.meicilly.flume.utils.JSONUtil;
public class ETLInterceptor implements Interceptor {
    @Override
    public void initialize() {

    }

    @Override
    public Event intercept(Event event) {
        //1 获取body中的数据
        byte[] body = event.getBody();
        String log = new String(body, StandardCharsets.UTF_8);
        // TODO: 2023/6/4 判断是不是合法的json
        if(JSONUtil.isJSONValidate(log)){
            return event;
        }else {
            return null;
        }
        // TODO: 2023/6/4 是 return event  不是 return null
    }

    @Override
    public List<Event> intercept(List<Event> list) {
        Iterator<Event> iterator = list.iterator();
        while (iterator.hasNext()){
            Event event = iterator.next();
            if(intercept(event) == null){
                iterator.remove();
            }
        }
        return list;
    }

    @Override
    public void close() {

    }
    public static class Builder implements Interceptor.Builder{

        @Override
        public Interceptor build() {
            return new ETLInterceptor();
        }

        @Override
        public void configure(Context context) {

        }
    }
}
