package com.alenlee;

import com.alenlee.packres.vo.PacResStockDetail;
import com.alenlee.packres.vo.PurchaseType;
import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Created by lc on 2016/4/30.
 */
public class FastJsonTest {
    private static final Logger logger = LoggerFactory.getLogger(FastJsonTest.class);
    @Test
    public void toJSONString() {
        PacResStockDetail detail = new PacResStockDetail();
        detail.setBatchId(1);
        detail.setDepartDate(new Date());
        detail.setResId("1");
        detail.setPurchaseType(PurchaseType.STK);
        detail.setStockLeft(10);
        detail.setPrice(120.0d);
        String detailJson = JSON.toJSONString(detail);
        logger.info("Json:{}", detailJson);

    }
}
