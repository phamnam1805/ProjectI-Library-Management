package DAO;

import model.DocGia;
import model.Sach;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class DocGiaDAO {

    private PreparedStatement prepare(String sql) throws SQLException {
        return DatabaseConnection.connection
                .prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
    }

    private DocGia getObject(ResultSet resultSet) throws  SQLException{
        DocGia docGia = new DocGia();
        docGia.setId(resultSet.getInt("Ma_DG_20183599"));
        docGia.setTenDG(resultSet.getString("Ten_DG_20183599"));
        docGia.setGioiTinh(resultSet.getInt("GioiTinh_20183599"));
        docGia.setDiaChi(resultSet.getString("DiaChi_20183599"));
        docGia.setNgaySinh(resultSet.getDate("NgaySinh_20183599"));
        docGia.setCMND(resultSet.getString("CMND_20183599"));
        docGia.setEmail(resultSet.getString("Email_20183599"));
        docGia.setDienThoai(resultSet.getString("DienThoai_20183599"));
        return docGia;
    }

    private List<DocGia> getList(ResultSet resultSet) throws SQLException{
        List<DocGia> data = new ArrayList<>();
        while(resultSet.next()){
            data.add(getObject(resultSet));
        }
        return data;
    }

    public DocGia findById(int id) throws SQLException{
        String sql = "SELECT * FROM docgia_phamvannam WHERE Ma_DG_20183599 = ?";
        PreparedStatement preparedStatement = prepare(sql);
        preparedStatement.setInt(1,id);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.first();
        return getObject(resultSet);
    }

    public DocGia findByCMND(String CMND) throws SQLException{
        String sql = "SELECT * FROM docgia_phamvannam WHERE CMND_20183599 = ?";
        PreparedStatement preparedStatement = prepare(sql);
        preparedStatement.setString(1,CMND);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.first();
        return getObject(resultSet);
    }

    public List<DocGia> findByName(String name) throws SQLException{
        name = "\'%"+name+"%\'";
        String sql = "SELECT * FROM docgia_phamvannam WHERE Ten_DG_20183599 LIKE"+name;
        PreparedStatement preparedStatement = prepare(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        return getList(resultSet);
    }

    public DocGia findByPhoneNumber(String phoneNumber) throws SQLException{
        String sql = "SELECT * FROM docgia_phamvannam WHERE DienThoai_20183599 = ?";
        PreparedStatement preparedStatement = prepare(sql);
        preparedStatement.setString(1,phoneNumber);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.first();
        return getObject(resultSet);
    }

    public List<DocGia> findAll() throws SQLException {
        String sql = "SELECT * FROM docgia_phamvannam";
        PreparedStatement preparedStatement = prepare(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        return getList(resultSet);
    }

    public boolean insertDG(DocGia docGia) throws SQLException{
        String sql = "INSERT INTO docgia_phamvannam VALUES(null, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = prepare(sql);
        preparedStatement.setString(1, docGia.getTenDG());
        preparedStatement.setInt(2, docGia.getGioiTinh());
        preparedStatement.setString(3, docGia.getDiaChi());
        preparedStatement.setDate(4, docGia.getNgaySinh());
        preparedStatement.setString(5, docGia.getCMND());
        preparedStatement.setString(6, docGia.getEmail());
        preparedStatement.setString(7, docGia.getDienThoai());
        return preparedStatement.executeUpdate() > 0;
    }

    public boolean updateDG(DocGia docGia) throws SQLException {
        String sql = "UPDATE docgia_phamvannam SET "+
                " Ten_DG_20183599 = ?, "+
                " GioiTinh_20183599 = ?, "+
                " DiaChi_20183599 = ?, "+
                " NgaySinh_20183599 = ?, "+
                " CMND_20183599 = ?, "+
                " Email_20183599 = ?, "+
                " DienThoai_20183599 = ? "+
                " WHERE Ma_DG_20183599 = ?";
        PreparedStatement preparedStatement = prepare(sql);
        preparedStatement.setString(1, docGia.getTenDG());
        preparedStatement.setInt(2, docGia.getGioiTinh());
        preparedStatement.setString(3, docGia.getDiaChi());
        preparedStatement.setDate(4, docGia.getNgaySinh());
        preparedStatement.setString(5, docGia.getCMND());
        preparedStatement.setString(6, docGia.getEmail());
        preparedStatement.setString(7, docGia.getDienThoai());
        preparedStatement.setInt(8, docGia.getId());
        return preparedStatement.executeUpdate() > 0 ;
    }

    public boolean deleteDG(int id) throws SQLException {
        String sql = "DELETE FROM docgia_phamvannam WHERE Ma_DG_20183599 = ?";
        PreparedStatement preparedStatement = prepare(sql);
        preparedStatement.setInt(1, id);
        return preparedStatement.executeUpdate() > 0;
    }
}
