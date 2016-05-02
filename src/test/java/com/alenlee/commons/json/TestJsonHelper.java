package com.alenlee.commons.json;

import com.alibaba.fastjson.JSON;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by lc on 2016/5/1.
 */
public class TestJsonHelper {
    private static final Logger logger = LoggerFactory.getLogger(TestJsonHelper.class);
    private static final String TEST_SRC = "C:\\work\\github\\LoadTestLab\\src\\test\\java";
    private static final String DEFAULT_ENCODING = "UTF-8";

    public static <T> List<T> parseList(String classpath, Class<T> clazz){
        String json = readString(classpath);
        return JSON.parseArray(json,clazz);
    }
    public static <T> T parseObject(String classpath, Class<T> clazz){
        String json = readString(classpath);
        return JSON.parseObject(json, clazz);
    }

    public static String readString(String classpath) {
        File file = getFile(classpath);
        try {
            String string = FileUtils.readFileToString(file, DEFAULT_ENCODING);
            return string;
        } catch (IOException e) {
            logger.error("error", e);
            return null;
        }
    }

    public static void writeJson(String classpath, Object bean) {
        String json = JSON.toJSONString(bean);
        File file = getFile(classpath);
        try {
            FileUtils.writeStringToFile(file, json, "UTF-8");
        } catch (IOException e) {
            logger.error("error", e);
        }
    }


    public static File getFile(String classpath) {
        if (StringUtils.isEmpty(classpath)) {
            throw new IllegalArgumentException("classpath must not be null!");
        }
        String filepath = null;
        if (classpath.startsWith("/")||classpath.startsWith("\\")) {
            filepath = TEST_SRC + classpath.replace("/", "\\");
        } else {
            filepath = TEST_SRC + "\\" + classpath.replace("/", "\\");
        }
        File file = new File(filepath);
        return file;
    }

}
