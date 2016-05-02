package com.alenlee.pacres.json;

import com.alenlee.commons.json.TestJsonHelper;
import com.alenlee.packres.vo.PacResStockDetail;
import com.alenlee.packres.vo.PurchaseType;
import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by lc on 2016/5/1.
 */
public class PacResTestData {
    private static final Logger logger = LoggerFactory.getLogger(PacResTestData.class);

    @Test
    public void refreshData() {
        int idSize = 100;
        Date date = new Date();
        List<PacResStockDetail> list = new ArrayList<PacResStockDetail>();
        for (int i = 1; i <= idSize; i++) {
            PacResStockDetail base = new PacResStockDetail();
            base.setDepartDate(new Date());
            base.setResId(i + "");
            //不同的批次
            int module = i % 2;
            if (module == 0) {
                //不满足库存
                int totalStock = 2;
                PacResStockDetail b1 = newStock(base, 1, totalStock, 1, PurchaseType.STK);
                PacResStockDetail b2 = newStock(base, 2, totalStock, 1, PurchaseType.STK);
                PacResStockDetail b3 = newStock(base, 3, totalStock, 999, PurchaseType.FS);
                list.add(b1);
                list.add(b2);
                list.add(b3);
            } else if (module == 1) {
                //部分满足库存,各批次按价格递增排序
                int totalStock = 12;
                PacResStockDetail b1 = newStock(base, 1, totalStock, 4, PurchaseType.STK);
                PacResStockDetail b2 = newStock(base, 2, totalStock, 4, PurchaseType.STK);
                PacResStockDetail b3 = newStock(base, 3, totalStock, 4, PurchaseType.STK);
                PacResStockDetail b4 = newStock(base, 4, totalStock, 4, PurchaseType.STK);
                b2.setPrice(b1.getPrice() + 10);
                b3.setPrice(b2.getPrice() + 10);
                b4.setPrice(b3.getPrice() + 10);
                list.add(b1);
                list.add(b2);
                list.add(b3);
                list.add(b4);
            }
        }

        //写入文件
        TestJsonHelper.writeJson("com\\alenlee\\pacres\\json\\stock.json", list);
    }


    public List<PacResStockDetail> getStockList() {
        List<PacResStockDetail> list = TestJsonHelper.parseList("com\\alenlee\\pacres\\json\\stock.json", PacResStockDetail.class);
        return list;
    }

    public Map<String, List<PacResStockDetail>> calculatePrice(List<PacResStockDetail> stockList, int minStock) {
        long start = System.currentTimeMillis();
        Map<String, List<PacResStockDetail>> details = new HashMap<String, List<PacResStockDetail>>();
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        //按ID,团期聚合
        for (PacResStockDetail stock : stockList) {
            String key = stock.getResId() + "_" + df.format(stock.getDepartDate());
            List<PacResStockDetail> list = details.get(key);
            if (list == null) {
                list = new ArrayList<PacResStockDetail>();
                details.put(key, list);
            }
            list.add(stock);
        }

        List<PacResStockDetail> byStock = new ArrayList<PacResStockDetail>();
        List<PacResStockDetail> byFS = new ArrayList<PacResStockDetail>();
        List<PacResStockDetail> byIQ = new ArrayList<PacResStockDetail>();
        //选择批次，计算均价
        for (List<PacResStockDetail> list : details.values()) {
            if (list == null || list.size() <= 0) {
                continue;
            }
            PacResStockDetail d0 = list.get(0);
            //库存
            if (d0.getPurchaseType() == PurchaseType.STK && d0.getTotalStock() >= minStock) {
                Double total = 0d;
                int stockCollected = 0;

                for (PacResStockDetail d : list) {
                    if (stockCollected + d.getStockLeft() >= minStock) {
                        int size = minStock - stockCollected;
                        stockCollected = minStock;
                        total += size * d.getPrice();
                        break;
                    } else {
                        stockCollected += d.getStockLeft();
                        total = d.getStockLeft() * d.getPrice() + total;
                    }
                }
                Double avg = total / stockCollected;
                PacResStockDetail newStock = newStock(d0, 0, 0, 0, d0.getPurchaseType());
                newStock.setPrice(avg);
                byStock.add(newStock);
                continue;
            } else {
                //FS或现询
                for (PacResStockDetail d : list) {
                    if (d.getPurchaseType() == PurchaseType.FS) {
                        PacResStockDetail newStock = newStock(d, 0, 0, 0, d.getPurchaseType());
                        newStock.setPrice(d.getPrice());
                        byFS.add(newStock);
                    } else if (d.getPurchaseType() == PurchaseType.IQ) {
                        PacResStockDetail newStock = newStock(d, 0, 0, 0, d.getPurchaseType());
                        newStock.setPrice(d.getPrice());
                        byIQ.add(newStock);
                    }
                }
                continue;
            }
        }
        long end = System.currentTimeMillis();
        logger.info("stock size:{}, using {} ms", stockList.size(), (end - start));
        logger.info("map:{}", JSON.toJSONString(details));
        logger.info("bystock:{}", JSON.toJSONString(byStock));
        // System.out.println(JSON.toJSONString(byStock));
        // System.out.println(JSON.toJSONString(byFS));
        // System.out.println(JSON.toJSONString(byIQ));
        return details;
    }


    @Test
    public void testCalulate() {
        List<PacResStockDetail> stockDetails = getStockList();
        Map<String, List<PacResStockDetail>> byIdDate = calculatePrice(stockDetails, 7);
        //System.out.println(JSON.toJSONString(byIdDate));
    }

    private PacResStockDetail newStock(PacResStockDetail base, int batchId, int totalStock, int stockLeft, PurchaseType purchaseType) {
        Random rand = new Random();
        PacResStockDetail d = new PacResStockDetail();
        d.setResId(base.getResId());
        d.setDepartDate(base.getDepartDate());
        d.setPrice(new Double(rand.nextInt(10000)));
        d.setBatchId(batchId);
        d.setStockLeft(stockLeft);
        d.setTotalStock(totalStock);
        d.setPurchaseType(purchaseType);
        return d;
    }

}
