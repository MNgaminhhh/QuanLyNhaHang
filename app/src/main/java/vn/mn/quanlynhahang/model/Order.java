package vn.mn.quanlynhahang.model;

import com.google.type.DateTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class Order {
    int id;
    Date timeOrder;
    Map<String, OrderQuantity> order;
    int idTable;
    String orderEmp;
    boolean paymentStatus;
    String paymentMethod;

    public Order() {
        this.paymentStatus=true;
    }

    public Order(int id, Date timeOrder, Map<String, OrderQuantity> order, int idTable, String orderEmp, boolean paymentStatus, String paymentMethod) {
        this.id = id;
        this.timeOrder = timeOrder;
        this.order = order;
        this.idTable = idTable;
        this.orderEmp = orderEmp;
        this.paymentStatus = paymentStatus;
        this.paymentMethod = paymentMethod;
    }

    public int totalPrice(){
        int sum = 0;
        for (OrderQuantity orderQuantity:this.order.values()){
            sum+=orderQuantity.calcPrice();
        }
        return sum;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getTimeOrder() {
        return timeOrder;
    }

    public void setTimeOrder(Date timeOrder) {
        this.timeOrder = timeOrder;
    }

    public String getOrderEmp() {
        return orderEmp;
    }

    public void setOrderEmp(String orderEmp) {
        this.orderEmp = orderEmp;
    }

    public Map<String, OrderQuantity> getOrder() {
        return order;
    }

    public void setOrder(Map<String, OrderQuantity> order) {
        this.order = order;
    }

    public int getIdTable() {
        return idTable;
    }

    public void setIdTable(int idTable) {
        this.idTable = idTable;
    }

    public boolean isPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(boolean paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
