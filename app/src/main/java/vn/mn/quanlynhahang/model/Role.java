package vn.mn.quanlynhahang.model;

import java.util.List;

public class Role {
    private String tenChucVu;
    private List<String> danhSach;

    public Role() {
    }

    public Role(String tenChucVu, List<String> danhSach) {
        this.tenChucVu = tenChucVu;
        this.danhSach = danhSach;
    }

    public String getTenChucVu() {
        return tenChucVu;
    }

    public void setTenChucVu(String tenChucVu) {
        this.tenChucVu = tenChucVu;
    }

    public List<String> getDanhSach() {
        return danhSach;
    }

    public void setDanhSach(List<String> danhSach) {
        this.danhSach = danhSach;
    }
}

