package vn.mn.quanlynhahang.model;

public class Dish {
    int id;
    String dishName;
    int price;
    String urlImage;
    Boolean inStocks;

    public Dish() {
        this.inStocks =true;
    }

    public Dish(int id, String dishName, int price, String urlImage) {
        this.id = id;
        this.dishName = dishName;
        this.price = price;
        this.urlImage = urlImage;
    }
    public void changStocksStatus(){
        this.inStocks = !(this.inStocks);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public Boolean getInStocks() {
        return inStocks;
    }

    public void setInStocks(Boolean inStocks) {
        this.inStocks = inStocks;
    }
}
