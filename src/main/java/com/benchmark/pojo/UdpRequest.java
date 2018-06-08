package com.benchmark.pojo;

import java.util.Arrays;

/**
 * @author mofei@yixia.com
 * @date 2018/6/8 上午9:38
 */
public class UdpRequest {
    private long id;
    private long sendTime;
    private byte[] body;

    public UdpRequest(long sendTime, byte[] body) {
        this.sendTime = sendTime;
        this.body = body;
    }

    public UdpRequest() {
    }

    @Override
    public String toString() {
        return "UdpRequest{" + "id=" + id + ", sendTime=" + sendTime + ", body=" + Arrays.toString(body) + '}';
    }

    public long getSendTime() {
        return sendTime;
    }

    public void setSendTime(long sendTime) {
        this.sendTime = sendTime;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}