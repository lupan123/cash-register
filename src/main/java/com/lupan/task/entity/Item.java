package com.lupan.task.entity;

/**
 * TODO 商品pojo
 *
 * @author lupan
 * @version 2016/2/29 0029
 */
public class Item {
    //商品名
    private String name;
    //类别
    private String category;
    //数量
    private double amount;
    //单位
    private String unit;
    //单价
    private double price;
    //条形码
    private String barCode;
    //优惠活动类型，含多个优惠活动用","分开
    private String promotionType;


    public Item(){

    }

    public Item(String name,String category,double amount,String unit,double price,String barCode,String promotionType){
        this.name = name;
        this.category = category;
        this.amount = amount;
        this.unit = unit;
        this.price = price;
        this.barCode = barCode;
        this.promotionType = promotionType;
    }

    public Item(Item item) {
        this.name = item.getName();
        this.category = item.getCategory();
        this.amount = item.getAmount();
        this.unit = item.getUnit();
        this.price = item.getPrice();
        this.barCode = item.getBarCode();
        this.promotionType = item.getPromotionType();
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getPromotionType() {
        return promotionType;
    }

    public void setPromotionType(String promotionType) {
        this.promotionType = promotionType;
    }
}
