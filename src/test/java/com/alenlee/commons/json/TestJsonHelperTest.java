package com.alenlee.commons.json;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * Created by lichen2 on 2016/5/3.
 */
public class TestJsonHelperTest {

  @Test
  public void testRead() {
    String json = TestJsonHelper.readString("data\\pkgres\\stk.json");
    System.out.print(json);
  }

  @Test
  public void testWrite(){
    Map<String,String> a = new HashMap<>();
    a.put("name","張三1");
    TestJsonHelper.writeJson("data\\pkgres\\stk.json",a);
  }
}
