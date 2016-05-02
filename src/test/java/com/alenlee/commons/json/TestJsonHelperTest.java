package com.alenlee.commons.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by lc on 2016/5/1.
 */
public class TestJsonHelperTest {
    private static final Logger logger = LoggerFactory.getLogger(TestJsonHelperTest.class);

    @Test
    public void testRead() {
        String path = "com\\alenlee\\commons\\json\\stock.json";
        String json = TestJsonHelper.readString(path);
        JSONObject jo = JSON.parseObject(json);
        jo.put("age",22);
        TestJsonHelper.writeJson(path,jo);
        logger.info("json:{}", json);
    }
}
