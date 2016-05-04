package com.alenlee.commons.json;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.alibaba.fastjson.JSON;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

/**
 * Created by lichen2 on 2016/4/29.
 */
public class TestJsonHelper {

  private static final String DEFAULT_ENCODING = "UTF-8";
  private static Logger logger = LoggerFactory.getLogger(TestJsonHelper.class);

  public static <T> List<T> parseList(String classpath, Class<T> clazz) {
    String json = readString(classpath);
    return JSON.parseArray(json, clazz);
  }

  public static <T> T parseObject(String classpath, Class<T> clazz) {
    String json = readString(classpath);
    return JSON.parseObject(json, clazz);
  }

  public static String readString(String classpath) {
    classpath = normalizePath(classpath);
    ClassPathResource cr = new ClassPathResource(classpath);
    try {
      File file = cr.getFile();
      String json = FileUtils.readFileToString(file, DEFAULT_ENCODING);
      return json;
    } catch (IOException e) {
      logger.error("error", e);
      return null;
    }
  }


  private static String normalizePath(String cp) {
    if (cp == null) {
      return null;
    }
    cp = cp.replace("\\", "/");
    if (!cp.startsWith("/")) {
      cp = "/" + cp;
    }
    return cp;
  }

  public static void writeJson(String classpath, Object bean) {
    String json = JSON.toJSONString(bean);
    writeJson(classpath, json);
  }

  public static void writeJson(String classpath, String content) {
    classpath = normalizePath(classpath);
    ClassPathResource cr = new ClassPathResource(classpath);
    try {
      File file = cr.getFile();
      FileUtils.writeStringToFile(file, content, DEFAULT_ENCODING);
    } catch (IOException e) {
      logger.error("error", e);
    }
  }


}
