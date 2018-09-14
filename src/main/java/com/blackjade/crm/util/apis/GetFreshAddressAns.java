package com.blackjade.crm.util.apis;


import java.util.UUID;

/**
 * Created by Administrator on 2018/8/30.
 */
public class GetFreshAddressAns {

    private UUID requestid;
    private String messageid;
    private int customerId;

    private ComStatus.GetFreshAddressEnum status;

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

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public ComStatus.GetFreshAddressEnum getStatus() {
        return status;
    }

    public void setStatus(ComStatus.GetFreshAddressEnum status) {
        this.status = status;
    }
}
