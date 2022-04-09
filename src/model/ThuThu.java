package model;


import java.sql.Date;

public class ThuThu {
    private int id;
    private String tenTT;
    private int gioiTinh;
    private java.sql.Date ngaySinh;
    private String CMND;
    private String email;
    private String dienThoai;

    public ThuThu() {
    }

    public ThuThu(int id, String tenTT, int gioiTinh, Date ngaySinh, String CMND, String email, String dienThoai) {
        this.id = id;
        this.tenTT = tenTT;
        this.gioiTinh = gioiTinh;
        this.ngaySinh = ngaySinh;
        this.CMND = CMND;
        this.email = email;
        this.dienThoai = dienThoai;
    }

    public ThuThu(String tenTT, int gioiTinh, Date ngaySinh, String CMND, String email, String dienThoai) {
        this.tenTT = tenTT;
        this.gioiTinh = gioiTinh;
        this.ngaySinh = ngaySinh;
        this.CMND = CMND;
        this.email = email;
        this.dienThoai = dienThoai;
    }



    public String toString(){
        return "ThuThu{" +
                "Ma_TT_20183599 = " + id +
                ", Ten_TT_20183599 = " + tenTT +
                ", GioiTinh_20183599 = " + gioiTinh +
                ", NgaySinh_20183599 = " + ngaySinh +
                ", CMND_20183599 = " + CMND +
                ", Email_20183599 = " + email +
                ", DienThoai_20183599 = " + dienThoai +
                "}";
    }

    public String[] getPrint(){
        String[] data = new String[7];
        data[0] = String.valueOf(id);
        data[1] = tenTT;
        if(gioiTinh == 1){
            data[2] = "Nam";
        }else if(gioiTinh == 2){
            data[2] = "Khác";
        }else{
            data[2] = "Nữ";
        }
        if(ngaySinh != null){
            data[3] = ngaySinh.toString();
        }else{
            data[3] = "";
        }
        data[4] = CMND;
        data[5] = email;
        data[6] = dienThoai;
        return data;
    }

    public static String[] getHeader(){
        return new String[]{"Mã độc giả","Tên độc giả", "Giới tính", "Ngày sinh", "Số chứng minh nhân dân", "Email", "Điện thoại"};
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTenTT() {
        return tenTT;
    }

    public void setTenTT(String tenTT) {
        this.tenTT = tenTT;
    }

    public int getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(int gioiTinh) {
        this.gioiTinh = gioiTinh;
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
