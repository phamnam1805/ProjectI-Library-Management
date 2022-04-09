package model.form;

public class FormChiTietMuon {
    private int MaMT;
    private int MaSach;
    private String tenSach;
    private String ghiChu;

    public FormChiTietMuon() {
    }

    public FormChiTietMuon(int id_MaMT, int id_MaSach, String tenSach, String ghiChu) {
        this.MaMT = id_MaMT;
        this.MaSach = id_MaSach;
        this.tenSach = tenSach;
        this.ghiChu = ghiChu;
    }

    public String[] getPrint(){
        String[] data = new String[3];
        data[0] = String.valueOf(getMaSach());
        data[1] = tenSach;
        data[2] = ghiChu;
        return data;
    }

    public static String[] getHeader(){
        return new String[]{"Mã sách", "Tên sách", "Ghi chú"};
    }

    public int getMaMT() {
        return MaMT;
    }

    public void setMaMT(int maMT) {
        this.MaMT = maMT;
    }

    public int getMaSach() {
        return MaSach;
    }

    public void setMaSach(int maSach) {
        this.MaSach = maSach;
    }

    public String getTenSach() {
        return tenSach;
    }

    public void setTenSach(String tenSach) {
        this.tenSach = tenSach;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }
}
