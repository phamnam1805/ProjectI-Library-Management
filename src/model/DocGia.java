package model;

import java.sql.Date;

public class DocGia {
    private int id;
    private String tenDG;
    private int gioiTinh;
    private String diaChi;
    private Date ngaySinh;
    private String CMND;
    private String email;
    private String dienThoai;

    public DocGia() {
    }

    public DocGia(int id, String tenDG, int gioiTinh, String diaChi, Date ngaySinh, String CMND, String email, String dienThoai) {
        this.id = id;
        this.tenDG = tenDG;
        this.gioiTinh = gioiTinh;
        this.diaChi = diaChi;
        this.ngaySinh = ngaySinh;
        this.CMND = CMND;
        this.email = email;
        this.dienThoai = dienThoai;
    }

    public DocGia(String tenDG, int gioiTinh, String diaChi, Date ngaySinh, String CMND, String email, String dienThoai) {
        this.tenDG = tenDG;
        this.gioiTinh = gioiTinh;
        this.diaChi = diaChi;
        this.ngaySinh = ngaySinh;
        this.CMND = CMND;
        this.email = email;
        this.dienThoai = dienThoai;
    }

    public String toString(){
        return "DocGia{" +
                "Ma_DG_20183599 = " + id +
                ", Ten_DG_20183599 = " + tenDG +
                ", GioiTinh_20183599 = " + gioiTinh +
                ", DiaChi_20183599 = " + diaChi +
                ", NgaySinh_20183599 = " + ngaySinh +
                ", CMND_20183599 = " + CMND +
                ", Email_20183599 = " + email +
                ", DienThoai_20183599 = " + dienThoai +
                "}";
    }

    public String[] getPrint(){
        String[] data = new String[8];
        data[0] = String.valueOf(id);
        data[1] = tenDG;
        if(gioiTinh == 1){
            data[2] = "Nam";
        }else if(gioiTinh == 2){
            data[2] = "Khác";
        }else{
            data[2] = "Nữ";
        }
        data[3] = diaChi;
        if(ngaySinh != null){
            data[4] = ngaySinh.toString();
        }else{
            data[4] = "";
        }
        data[5] = CMND;
        data[6] = email;
        data[7] = dienThoai;
        return data;
    }

    public static String[] getHeader(){
        return new String[]{"Mã độc giả","Tên độc giả", "Giới tính", "Địa chỉ", "Ngày sinh", "Số chứng minh nhân dân", "Email", "Điện thoại"};
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTenDG() {
        return tenDG;
    }

    public void setTenDG(String tenDG) {
        this.tenDG = tenDG;
    }

    public int getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(int gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public Date getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(Date ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getCMND() {
        return CMND;
    }

    public void setCMND(String CMND) {
        this.CMND = CMND;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDienThoai() {
        return dienThoai;
    }

    public void setDienThoai(String dienThoai) {
        this.dienThoai = dienThoai;
    }
}
