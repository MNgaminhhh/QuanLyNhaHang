package vn.mn.quanlynhahang.model;

public class Table {
    int id;
    int numberOfDiner;

    public Table() {
    }

    public Table(int id, int numberOfDiner) {
        this.id = id;
        this.numberOfDiner = numberOfDiner;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumberOfDiner() {
        return numberOfDiner;
    }

    public void setNumberOfDiner(int numberOfDiner) {
        this.numberOfDiner = numberOfDiner;
    }
}
