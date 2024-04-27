package vn.mn.quanlynhahang.model;

public class OrderQuantity {
    Dish dish;
    int quantity;

    public OrderQuantity() {
    }

    public OrderQuantity(Dish dish, int quantity) {
        this.dish = dish;
        this.quantity = quantity;
    }
    public int calcPrice(){
        return this.dish.getPrice()*quantity;
    }
    public Dish getDish() {
        return dish;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
