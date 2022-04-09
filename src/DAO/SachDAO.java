package DAO;

import model.Sach;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SachDAO {

    private PreparedStatement prepare(String sql) throws SQLException {
        return DatabaseConnection.connection
                .prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
    }

    private Sach getObject(ResultSet resultSet) throws SQLException{
        Sach sach = new Sach();
        sach.setId(resultSet.getInt("MaSach_20183599"));
        sach.setTenSach(resultSet.getString("TenSach_20183599"));
        sach.setTacGia(resultSet.getString("TacGia_20183599"));
        sach.setNhaXB(resultSet.getString("NhaXB_20183599"));
        sach.setNamXB(resultSet.getInt("NamXB_20183599"));
        sach.setDonGia(resultSet.getDouble("DonGia_20183599"));
        sach.setGioiThieu(resultSet.getString("GioiThieu_20183599"));
        return sach;
    }

    private List<Sach> getList(ResultSet resultSet) throws SQLException{
        List<Sach> data = new ArrayList<>();
        while(resultSet.next()){
            data.add(getObject(resultSet));
        }
        return data;
    }

    public Sach findById(int id) throws SQLException{
        String sql = "SELECT * FROM sach_phamvannam WHERE MaSach_20183599 = ?";
        PreparedStatement preparedStatement = prepare(sql);
        preparedStatement.setInt(1,id);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.first();
        return getObject(resultSet);
    }

    public List<Sach> findByName(String name) throws SQLException{
        name = "\'%"+name+"%\'";
        String sql = "SELECT * FROM sach_phamvannam WHERE TenSach_20183599 LIKE "+name;
        PreparedStatement preparedStatement = prepare(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        return getList(resultSet);
    }

    public List<Sach> findAll() throws SQLException{
        String sql = "SELECT * FROM sach_phamvannam";
        PreparedStatement preparedStatement = prepare(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        return getList(resultSet);
    }

    public List<Sach> findByTacGia(String tacGia) throws SQLException{
        tacGia = "\'%"+tacGia+"%\'";
        String sql = "SELECT * FROM sach_phamvannam WHERE TacGia_20183599 LIKE "+tacGia;
        PreparedStatement preparedStatement = prepare(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        return getList(resultSet);
    }

    public List<Sach> findByNhaXB(String nhaXB) throws SQLException{
        nhaXB = "\'%"+nhaXB+"%\'";
        String sql = "SELECT * FROM sach_phamvannam WHERE NhaXB_20183599 LIKE "+nhaXB;
        PreparedStatement preparedStatement = prepare(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        return getList(resultSet);
    }

    public List<Sach> findByNamXB(int namXB) throws SQLException{
        String sql = "SELECT * FROM sach_phamvannam WHERE namXB_20183599 = "+namXB;
        PreparedStatement preparedStatement = prepare(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        return getList(resultSet);
    }

    public boolean insertSach(Sach sach) throws SQLException{
        String sql = "INSERT INTO sach_phamvannam VALUES(null, ?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = prepare(sql);
        preparedStatement.setString(1, sach.getTenSach());
        preparedStatement.setString(2, sach.getTacGia());
        preparedStatement.setString(3, sach.getNhaXB());
        preparedStatement.setInt(4, sach.getNamXB());
        preparedStatement.setDouble(5, sach.getDonGia());
        preparedStatement.setString(6, sach.getGioiThieu());
        return preparedStatement.executeUpdate() > 0;
    }

    public boolean updateSach(Sach sach) throws  SQLException{
        String sql = "UPDATE sach_phamvannam SET " +
                "TenSach_20183599 = ?, " +
                "TacGia_20183599 = ?, " +
                "NhaXB_20183599 = ?, " +
                "NamXB_20183599 = ?, " +
                "DonGia_20183599 = ?, " +
                "GioiThieu_20183599 = ? " +
                "WHERE MaSach_20183599 = ? ";
        PreparedStatement preparedStatement = prepare(sql);
        preparedStatement.setString(1, sach.getTenSach());
        preparedStatement.setString(2, sach.getTacGia());
        preparedStatement.setString(3, sach.getNhaXB());
        preparedStatement.setInt(4, sach.getNamXB());
        preparedStatement.setDouble(5, sach.getDonGia());
        preparedStatement.setString(6, sach.getGioiThieu());
        preparedStatement.setInt(7, sach.getId());
        return preparedStatement.executeUpdate() > 0;
    }

    public boolean deleteSach(int id) throws SQLException{
        String sql = "DELETE FROM sach_phamvannam WHERE MaSach_20183599 = ?";
        PreparedStatement preparedStatement = prepare(sql);
        preparedStatement.setInt(1, id);
        return preparedStatement.executeUpdate() > 0;
    }
}
