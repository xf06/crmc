package com.blackjade.crm.util.apis;


import java.util.UUID;

/**
 * Created by Administrator on 2018/8/29.
 */
public class SendBitcoin {
    private UUID requestid;
    private String messageid;
    private String amount;
    private String address;
    private UUID orderid;
    private int clientid;
    private int pnsid;
    private int pnsgid;
    private int operateType;
    private long platformFee;

    public ComStatus.SendBitcoinEnum reviewData(){

        return ComStatus.SendBitcoinEnum.SUCCESS;
    }

    public UUID getRequestid() {
        return requestid;
    }

    public void setRequestid(UUID requestid) {
        this.requestid = requestid;
    }

    public String getMessageid() {
        return messageid;
    }

    public void setMessageid(String messageid) {
        this.messageid = messageid;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public UUID getOrderid() {
        return orderid;
    }

    public void setOrderid(UUID orderid) {
        this.orderid = orderid;
    }

    public int getClientid() {
        return clientid;
    }

    public void setClientid(int clientid) {
        this.clientid = clientid;
    }

    public int getPnsid() {
        return pnsid;
    }

    public void setPnsid(int pnsid) {
        this.pnsid = pnsid;
    }

    public int getPnsgid() {
        return pnsgid;
    }

    public void setPnsgid(int pnsgid) {
        this.pnsgid = pnsgid;
    }

    public int getOperateType() {
        return operateType;
    }

    public void setOperateType(int operateType) {
        this.operateType = operateType;
    }

    public long getPlatformFee() {
        return platformFee;
    }

    public void setPlatformFee(long platformFee) {
        this.platformFee = platformFee;
    }
}
