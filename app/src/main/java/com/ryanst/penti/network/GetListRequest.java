package com.ryanst.penti.network;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zhengjuntong on 7/11/16.
 */
public class GetListRequest extends BaseRequest {
    @SerializedName("operation")
    private String operation;

    @SerializedName("id")
    private String id;

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
