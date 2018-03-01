package com.gether.bigdata.flink.bean;

public class OrderUser {
    private String userName;
    private String itemName;
    private long transactionDate;
    private int quantity;
    private String userAddress;
    private String gender;
    private int age;

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

    public static OrderUser fromOrder(Order order) {
        OrderUser orderUser = new OrderUser();
        if (order == null) {
            return orderUser;
        }
        orderUser.userName = order.getUserName();
        orderUser.itemName = order.getItemName();
        orderUser.transactionDate = order.getTransactionDate();
        orderUser.quantity = order.getQuantity();
        return orderUser;
    }

    public static OrderUser fromOrderUser(Order order, User user) {
        OrderUser orderUser = fromOrder(order);
        if (user == null) {
            return orderUser;
        }
        orderUser.gender = user.getGender();
        orderUser.age = user.getAge();
        orderUser.userAddress = user.getAddress();
        return orderUser;
    }
}