package com.alenlee.packres.vo;

/**
 * Created by lc on 2016/4/30.
 */
public enum PurchaseType {
    STK(1), FS(2), IQ(3);

    PurchaseType(int value) {
        this.value = value;
    }

    private final int value;

    public int getValue() {
        return value;
    }
}
