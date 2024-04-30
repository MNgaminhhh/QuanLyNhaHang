package vn.mn.quanlynhahang.model;

public class OrderEmp {
    private String orderEmpName;
    private String orderTime;
    private int orderCount;

    public OrderEmp(String orderEmpName, String orderTime, int orderCount) {
        this.orderEmpName = orderEmpName;
        this.orderTime = orderTime;
        this.orderCount = orderCount;
    }

    public String getOrderEmpName() {
        return orderEmpName;
    }

    public void setOrderEmpName(String orderEmpName) {
        this.orderEmpName = orderEmpName;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public int getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(int orderCount) {
        this.orderCount = orderCount;
    }
}
