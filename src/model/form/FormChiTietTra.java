package model.form;

import javafx.scene.control.Button;
import model.ChiTiet_Muon;

import java.sql.Date;

public class FormChiTietTra {
    private int MaMT;
    private int MaSach;
    private String tenSach;
    private Date ngayTra;
    private String trangThaiSach;
    private double tienPhat;
    private String ghiChu;
    private Button sua;

    public FormChiTietTra() {
    }

    public FormChiTietTra(int maMT, int maSach, String tenSach, Date ngayTra, String trangThaiSach, double tienPhat, String ghiChu, Button sua) {
        MaMT = maMT;
        MaSach = maSach;
        this.tenSach = tenSach;
        this.ngayTra = ngayTra;
        this.trangThaiSach = trangThaiSach;
        this.tienPhat = tienPhat;
        this.ghiChu = ghiChu;
        this.sua = sua;
    }

    public ChiTiet_Muon toChiTietMuon(){
        ChiTiet_Muon chiTiet_muon = new ChiTiet_Muon();
        chiTiet_muon.setMaMT(getMaMT());
        chiTiet_muon.setMaSach(getMaSach());
        chiTiet_muon.setNgayTra(getNgayTra());
        chiTiet_muon.setTienPhat(getTienPhat());
        chiTiet_muon.setTrangThaiSach(getTrangThaiSach());
        chiTiet_muon.setGhiChu(getGhiChu());
        return chiTiet_muon;
    }

    public Button getSua() {
        return sua;
    }

    public void setSua(Button sua) {
        this.sua = sua;
    }

    public int getMaMT() {
        return MaMT;
    }

    public void setMaMT(int maMT) {
        MaMT = maMT;
    }

    public int getMaSach() {
        return MaSach;
    }

    public void setMaSach(int maSach) {
        MaSach = maSach;
    }

    public String getTenSach() {
        return tenSach;
    }

    public void setTenSach(String tenSach) {
        this.tenSach = tenSach;
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
