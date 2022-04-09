package model;

import java.sql.Date;

public class MuonTra {
    private int id;
    private int id_DG;
    private int id_TT;
    private Date ngayMuon;
    private Date ngayHenTra;
    private double tienCoc;
    private boolean daTra;

    public MuonTra() {
    }


    public MuonTra(int id, int id_DG, int id_TT, Date ngayMuon, Date ngayHenTra, double tienCoc, boolean daTra) {
        this.id = id;
        this.id_DG = id_DG;
        this.id_TT = id_TT;
        this.ngayMuon = ngayMuon;
        this.ngayHenTra = ngayHenTra;
        this.tienCoc = tienCoc;
        this.daTra = daTra;
    }

    public boolean isDaTra() {
        return daTra;
    }

    public void setDaTra(boolean daTra) {
        this.daTra = daTra;
    }

    public String toString(){
        return "MuonTra{" +
                "MaMT_20183599 = " + id +
                ", Ma_DG_20183599 = " + id_DG +
                ", Ma_TT_20183599 = " + id_TT +
                ", Ngay_muon = " + ngayMuon +
                ", Ngay_hentra = " + ngayHenTra +
                ", DaTra_20183599 = " +daTra+
                "}";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_DG() {
        return id_DG;
    }

    public void setId_DG(int id_DG) {
        this.id_DG = id_DG;
    }

    public int getId_TT() {
        return id_TT;
    }

    public void setId_TT(int id_TT) {
        this.id_TT = id_TT;
    }

    public Date getNgayMuon() {
        return ngayMuon;
    }

    public void setNgayMuon(Date ngayMuon) {
        this.ngayMuon = ngayMuon;
    }

    public Date getNgayHenTra() {
        return ngayHenTra;
    }

    public void setNgayHenTra(Date ngayHenTra) {
        this.ngayHenTra = ngayHenTra;
    }

    public double getTienCoc() {
        return tienCoc;
    }

    public void setTienCoc(double tienCoc) {
        this.tienCoc = tienCoc;
    }
}
