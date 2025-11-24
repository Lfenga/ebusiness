package com.ch.ebusiness.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * 金额格式化工具类
 * 用于处理金额显示和计算的精度问题
 */
public class MoneyUtil {

    private static final DecimalFormat MONEY_FORMAT = new DecimalFormat("#0.00");

    /**
     * 格式化金额为字符串，保留两位小数
     * 
     * @param amount 金额
     * @return 格式化后的金额字符串
     */
    public static String format(BigDecimal amount) {
        if (amount == null) {
            return "0.00";
        }
        return MONEY_FORMAT.format(amount);
    }

    /**
     * 格式化金额为字符串，保留两位小数
     * 
     * @param amount 金额
     * @return 格式化后的金额字符串
     */
    public static String format(Double amount) {
        if (amount == null) {
            return "0.00";
        }
        return MONEY_FORMAT.format(amount);
    }

    /**
     * 将BigDecimal金额四舍五入到两位小数
     * 
     * @param amount 金额
     * @return 四舍五入后的金额
     */
    public static BigDecimal round(BigDecimal amount) {
        if (amount == null) {
            return BigDecimal.ZERO;
        }
        return amount.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Double转BigDecimal并四舍五入到两位小数
     * 
     * @param amount 金额
     * @return BigDecimal金额
     */
    public static BigDecimal toBigDecimal(Double amount) {
        if (amount == null) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(amount).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * 计算两个金额的乘积，保留两位小数
     * 
     * @param price    单价
     * @param quantity 数量
     * @return 总金额
     */
    public static BigDecimal multiply(BigDecimal price, int quantity) {
        if (price == null) {
            return BigDecimal.ZERO;
        }
        return price.multiply(BigDecimal.valueOf(quantity)).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * 计算多个金额的总和，保留两位小数
     * 
     * @param amounts 金额数组
     * @return 总金额
     */
    public static BigDecimal sum(BigDecimal... amounts) {
        BigDecimal total = BigDecimal.ZERO;
        for (BigDecimal amount : amounts) {
            if (amount != null) {
                total = total.add(amount);
            }
        }
        return total.setScale(2, RoundingMode.HALF_UP);
    }
}
