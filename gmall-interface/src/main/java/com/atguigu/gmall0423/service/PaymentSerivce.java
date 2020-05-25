package com.atguigu.gmall0423.service;

import com.atguigu.gmall0423.bean.PaymentInfo;

import java.util.Map;

public interface PaymentSerivce  {

    /**
     * 保存交易记录
     * @param paymentInfo
     */
    void savePaymentInfo(PaymentInfo paymentInfo);

    /**
     * 根据out_trade_no 查询
     * @param paymentInfoQuery
     * @return
     */
    PaymentInfo getPaymentInfo(PaymentInfo paymentInfoQuery);

    /**
     *
     * @param out_trade_no
     * @param paymentInfo
     */
    void updatePaymentInfo(String out_trade_no, PaymentInfo paymentInfo);

    /**
     * 退款接口
     * @param orderId
     * @return
     */
    boolean refund(String orderId);

    /**
     *
     * @param orderId
     * @param s
     * @return
     */
    Map createNative(String orderId, String s);

    /**
     * 发送消息给订单
     * @param paymentInfo
     * @param result
     */
    void sendPaymentResult(PaymentInfo paymentInfo,String result);

    /**
     * 根据out_trade_no 查询交易记录
     * @param paymentInfoQuery
     * @return
     */
    boolean checkPayment(PaymentInfo paymentInfoQuery);

    /**
     * 每隔15秒主动去支付宝询问该笔订单是否支付成功！
     * @param outTradeNo 第三方交易编号
     * @param delaySec 每隔多长时间查询一次
     * @param checkCount 查询的次数
     */
    void sendDelayPaymentResult(String outTradeNo,int delaySec ,int checkCount);

    /**
     *  根据订单Id 关闭交易记录状态
     * @param orderId
     */
    void closePayment(String orderId);
}
