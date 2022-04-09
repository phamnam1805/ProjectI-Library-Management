package model;

public class Sach {
    private int id;
    private String tenSach;
    private String tacGia;
    private String nhaXB;
    private int namXB;
    private double donGia;
    private String gioiThieu;

    public Sach() {
    }

    public Sach(int id, String tenSach, String tacGia, String nhaXB, int namXB, double donGia, String gioiThieu) {
        this.id = id;
        this.tenSach = tenSach;
        this.tacGia = tacGia;
        this.nhaXB = nhaXB;
        this.namXB = namXB;
        this.donGia = donGia;
        this.gioiThieu = gioiThieu;
    }

    public Sach(String tenSach, String tacGia, String nhaXB, int namXB, double donGia, String gioiThieu) {
        this.tenSach = tenSach;
        this.tacGia = tacGia;
        this.nhaXB = nhaXB;
        this.namXB = namXB;
        this.donGia = donGia;
        this.gioiThieu = gioiThieu;
    }

    public String toString(){
        return "Sach{" +
                "MaSach_20183599 = " + id +
                ", TenSach_20183599 = \'" + tenSach + "\'" +
                ", NhaXB_20183599 = \'" + nhaXB + "\'" +
                ", TacGia_20183599 = \'" + tacGia + "\'" +
                ", NamXB_20183599 = \'" + namXB + "\'" +
                ", DonGia_20183599 = " + donGia +
                ", GioiThieu_20183599 = \'" + gioiThieu + "\'" +
                "}";
    }

    public String[] getPrint(){
        String[] data = new String[7];
        data[0] = String.valueOf(id);
        data[1] = tenSach;
        data[2] = tacGia;
        data[3] = nhaXB;
        data[4] = String.valueOf(namXB);
        data[5] = String.valueOf(donGia);
        data[6] = gioiThieu;
        return data;
    }

    public static String[] getHeader(){
        return new String[]{"Mã sách","Tên sách", "Tác giả", "Nhà xuất bản", "Năm xuất bản", "Đơn giá", "Giới thiệu"};
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTenSach() {
        return tenSach;
    }

    public void setTenSach(String tenSach) {
        this.tenSach = tenSach;
    }

    public String getTacGia() {
        return tacGia;
    }

    public void setTacGia(String tacGia) {
        this.tacGia = tacGia;
    }

    public String getNhaXB() {
        return nhaXB;
    }

    public void setNhaXB(String nhaXB) {
        this.nhaXB = nhaXB;
    }

    public int getNamXB() {
        return namXB;
    }

    public void setNamXB(int namXB) {
        this.namXB = namXB;
    }

    public double getDonGia() {
        return donGia;
    }

    public void setDonGia(double donGia) {
        this.donGia = donGia;
    }

    public String getGioiThieu() {
        return gioiThieu;
    }

    public void setGioiThieu(String gioiThieu) {
        this.gioiThieu = gioiThieu;
    }
}
