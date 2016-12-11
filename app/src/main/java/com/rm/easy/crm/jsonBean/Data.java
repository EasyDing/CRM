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
    //Warehouse

    private String warehouseId;
    private String warehouseName;

    //warehouseItem
    private String warehouseItemId;
    private String warehouseItemName;
    private String warehouseItemWeight;
    private String warehouseItemWarehouseID;

    //warehouseDetail
    private String warehouseDetailId;
    private String warehouseDetailDirection;
    private String warehouseDetailWarehouseId;
    private String warehouseDetailItemId;
    private String warehouseDetailWeight;
    private String warehouseOperator;

    public String getWarehouseOperator() {
        return warehouseOperator;
    }

    public void setWarehouseOperator(String warehouseOperator) {
        this.warehouseOperator = warehouseOperator;
    }

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

    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public String getWarehouseItemId() {
        return warehouseItemId;
    }

    public void setWarehouseItemId(String warehouseItemId) {
        this.warehouseItemId = warehouseItemId;
    }

    public String getWarehouseItemName() {
        return warehouseItemName;
    }

    public void setWarehouseItemName(String warehouseItemName) {
        this.warehouseItemName = warehouseItemName;
    }

    public String getWarehouseItemWeight() {
        return warehouseItemWeight;
    }

    public void setWarehouseItemWeight(String warehouseItemWeight) {
        this.warehouseItemWeight = warehouseItemWeight;
    }

    public String getWarehouseItemWarehouseID() {
        return warehouseItemWarehouseID;
    }

    public void setWarehouseItemWarehouseID(String warehouseItemWarehouseID) {
        this.warehouseItemWarehouseID = warehouseItemWarehouseID;
    }

    public String getWarehouseDetailId() {
        return warehouseDetailId;
    }

    public void setWarehouseDetailId(String warehouseDetailId) {
        this.warehouseDetailId = warehouseDetailId;
    }

    public String getWarehouseDetailDirection() {
        return warehouseDetailDirection;
    }

    public void setWarehouseDetailDirection(String warehouseDetailDirection) {
        this.warehouseDetailDirection = warehouseDetailDirection;
    }

    public String getWarehouseDetailWarehouseId() {
        return warehouseDetailWarehouseId;
    }

    public void setWarehouseDetailWarehouseId(String warehouseDetailWarehouseId) {
        this.warehouseDetailWarehouseId = warehouseDetailWarehouseId;
    }

    public String getWarehouseDetailItemId() {
        return warehouseDetailItemId;
    }

    public void setWarehouseDetailItemId(String warehouseDetailItemId) {
        this.warehouseDetailItemId = warehouseDetailItemId;
    }

    public String getWarehouseDetailWeight() {
        return warehouseDetailWeight;
    }

    public void setWarehouseDetailWeight(String warehouseDetailWeight) {
        this.warehouseDetailWeight = warehouseDetailWeight;
    }
}
