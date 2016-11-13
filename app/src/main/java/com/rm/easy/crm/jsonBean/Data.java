package com.rm.easy.crm.jsonBean;

/**
 * Created by Easy.D on 2016/11/6.
 */
public class Data {


    //User
    private String id;
    private String username;
    private String userpwd;
    private String identity;
    //Client

    private String clientId;
    private String clientName;
    private String clientAdd;
    private String clientSex;
    private String clientPhone;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientAdd() {
        return clientAdd;
    }

    public void setClientAdd(String clientAdd) {
        this.clientAdd = clientAdd;
    }

    public String getClientSex() {
        return clientSex;
    }

    public void setClientSex(String clientSex) {
        this.clientSex = clientSex;
    }

    public String getClientPhone() {
        return clientPhone;
    }

    public void setClientPhone(String clientPhone) {
        this.clientPhone = clientPhone;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUserpwd(String userpwd) {
        this.userpwd = userpwd;
    }

    public String getUserpwd() {
        return userpwd;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getIdentity() {
        return identity;
    }
}
