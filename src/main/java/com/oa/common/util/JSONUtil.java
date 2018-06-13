package com.oa.common.util;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class JSONUtil {
    /**
     * 这个方法被servlet调用，通过流的方式读取前端传入的数据，输出为JSONObject
     * @param request
     * @return
     * @throws Exception
     */
    public static JSONObject getJsonStr(HttpServletRequest request)throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(),"utf-8"));
        StringBuilder sb = new StringBuilder();
        String temp="";
        while( (temp=br.readLine())!=null){
            sb.append(temp);
        }
        br.close();
        return (JSONObject) JSONObject.parse(sb.toString());
    }


}
