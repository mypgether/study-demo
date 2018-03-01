package com.gether.bigdata.flink.bean;

public class OrderUserItem {
    private String userName;
    private String itemName;
    private long transactionDate;
    private int quantity;
    private String userAddress;
    private String gender;
    private int age;
    private String itemAddress;
    private String itemType;
    private double itemPrice;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public long getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(long transactionDate) {
        this.transactionDate = transactionDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getItemAddress() {
        return itemAddress;
    }

    public void setItemAddress(String itemAddress) {
        this.itemAddress = itemAddress;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public static OrderUserItem fromOrderUser(OrderUser orderUser) {
        OrderUserItem orderUserItem = new OrderUserItem();
        if (orderUser == null) {
            return orderUserItem;
        }
        orderUserItem.userName = orderUser.getUserName();
        orderUserItem.itemName = orderUser.getItemName();
        orderUserItem.transactionDate = orderUser.getTransactionDate();
        orderUserItem.quantity = orderUser.getQuantity();
        orderUserItem.userAddress = orderUser.getUserAddress();
        orderUserItem.gender = orderUser.getGender();
        orderUserItem.age = orderUser.getAge();
        return orderUserItem;
    }

    public static OrderUserItem fromOrderUser(OrderUser orderUser, Item item) {
        OrderUserItem orderUserItem = fromOrderUser(orderUser);
        if (item == null) {
            return orderUserItem;
        }
        orderUserItem.itemAddress = item.getAddress();
        orderUserItem.itemType = item.getType();
        orderUserItem.itemPrice = item.getPrice();
        return orderUserItem;
    }
}