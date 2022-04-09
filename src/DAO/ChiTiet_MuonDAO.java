package DAO;

import model.ChiTiet_Muon;
import model.Sach;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ChiTiet_MuonDAO {

    private PreparedStatement prepare(String sql) throws SQLException {
        return DatabaseConnection.connection
                .prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
    }

    private ChiTiet_Muon getObject(ResultSet resultSet) throws SQLException{
        ChiTiet_Muon chiTiet_muon = new ChiTiet_Muon();
        chiTiet_muon.setMaMT(resultSet.getInt("MaMT_20183599"));
        chiTiet_muon.setMaSach(resultSet.getInt("MaSach_20183599"));
        chiTiet_muon.setNgayTra(resultSet.getDate("NgayTra_20183599"));
        chiTiet_muon.setTrangThaiSach(resultSet.getString("Trang_thai_sach"));
        chiTiet_muon.setTienPhat(resultSet.getDouble("Tien_phat"));
        chiTiet_muon.setGhiChu(resultSet.getString("Ghi_chu_20183599"));
        return chiTiet_muon;
    }

    private List<ChiTiet_Muon> getList(ResultSet resultSet) throws SQLException{
        List<ChiTiet_Muon> data = new ArrayList<>();
        while(resultSet.next()){
            data.add(getObject(resultSet));
        }
        return data;
    }
    public List<ChiTiet_Muon> findByMuonTraId(int id) throws SQLException{
        String sql = "SELECT * FROM chitiet_muon_phamvannam WHERE MaMT_20183599 = ?";
        PreparedStatement preparedStatement = prepare(sql);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        return getList(resultSet);
    }

    public boolean insertChiTietMuon(ChiTiet_Muon chiTiet_muon) throws SQLException{
        String sql = "INSERT INTO chitiet_muon_phamvannam VALUES(?, ?, null, null, null, ?)";
        PreparedStatement preparedStatement = prepare(sql);
        preparedStatement.setInt(1, chiTiet_muon.getMaMT());
        preparedStatement.setInt(2, chiTiet_muon.getMaSach());
        preparedStatement.setString(3, chiTiet_muon.getGhiChu());
        return preparedStatement.executeUpdate() > 0;
    }

    public boolean updateChiTietMuon(ChiTiet_Muon chiTiet_muon) throws SQLException{
        String sql = "UPDATE chitiet_muon_phamvannam " +
                " SET NgayTra_20183599 = ?, " +
                " Trang_thai_sach = ?, " +
                " Tien_phat = ?, " +
                " Ghi_chu_20183599 = ? \t" +
                " WHERE MaMT_20183599 = ? AND MaSach_20183599 = ?";
        PreparedStatement preparedStatement = prepare(sql);
        preparedStatement.setDate(1, chiTiet_muon.getNgayTra());
        preparedStatement.setString(2, chiTiet_muon.getTrangThaiSach());
        preparedStatement.setDouble(3, chiTiet_muon.getTienPhat());
        preparedStatement.setString(4, chiTiet_muon.getGhiChu());
        preparedStatement.setInt(5, chiTiet_muon.getMaMT());
        preparedStatement.setInt(6, chiTiet_muon.getMaSach());
        return preparedStatement.executeUpdate() > 0;
    }
}
