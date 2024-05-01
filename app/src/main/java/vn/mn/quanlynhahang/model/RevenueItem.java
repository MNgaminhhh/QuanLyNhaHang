package vn.mn.quanlynhahang.model;

public class RevenueItem {
    private String date;
    private long revenue;

    public RevenueItem(String date, long revenue) {
        this.date = date;
        this.revenue = revenue;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getRevenue() {
        return revenue;
    }

    public void setRevenue(long revenue) {
        this.revenue = revenue;
    }
}
