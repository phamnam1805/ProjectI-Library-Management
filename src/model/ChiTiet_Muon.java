package model;

import java.sql.Date;

public class ChiTiet_Muon {
    private int maMT;
    private int maSach;
    private Date ngayTra;
    private String trangThaiSach;
    private double tienPhat;
    private String ghiChu;

    public ChiTiet_Muon() {
    }

    public ChiTiet_Muon(int id_MaMT, int id_MaSach, Date ngayTra, String trangThaiSach, double tienPhat, String ghiChu) {
        this.maMT = id_MaMT;
        this.maSach = id_MaSach;
        this.ngayTra = ngayTra;
        this.trangThaiSach = trangThaiSach;
        this.tienPhat = tienPhat;
        this.ghiChu = ghiChu;
    }

    public String toString(){
        return "ChiTiet_Muon{" +
                "MaMT_20183599 = " + maMT +
                ", MaSach_20183599 = " + maSach +
                ", NgayTra_20183599 = " + ngayTra +
                ", Trang_thai_sach = " + trangThaiSach +
                ", Tien_phat = " + tienPhat +
                ", Ghi_chu_20183599 = " + ghiChu +
                "}";
    }

    public int getMaMT() {
        return maMT;
    }

    public void setMaMT(int maMT) {
        this.maMT = maMT;
    }

    public int getMaSach() {
        return maSach;
    }

    public void setMaSach(int maSach) {
        this.maSach = maSach;
    }

    public Date getNgayTra() {
        return ngayTra;
    }

    public void setNgayTra(Date ngayTra) {
        this.ngayTra = ngayTra;
    }

    public String getTrangThaiSach() {
        return trangThaiSach;
    }

    public void setTrangThaiSach(String trangThaiSach) {
        this.trangThaiSach = trangThaiSach;
    }

    public double getTienPhat() {
        return tienPhat;
    }

    public void setTienPhat(double tienPhat) {
        this.tienPhat = tienPhat;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }
}
