package DAO;

import model.DocGia;
import model.ThuThu;
import model.ThuThu;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ThuThuDAO {

    private PreparedStatement prepare(String sql) throws SQLException {
        return DatabaseConnection.connection
                .prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
    }

    private ThuThu getObject(ResultSet resultSet) throws SQLException{
        ThuThu thuThu = new ThuThu();
        thuThu.setId(resultSet.getInt("Ma_TT_20183599"));
        thuThu.setTenTT(resultSet.getString("Ten_TT_20183599"));
        thuThu.setGioiTinh(resultSet.getInt("GioiTinh_20183599"));
        thuThu.setNgaySinh(resultSet.getDate("NgaySinh_20183599"));
        thuThu.setCMND(resultSet.getString("CMND_20183599"));
        thuThu.setEmail(resultSet.getString("Email_20183599"));
        thuThu.setDienThoai(resultSet.getString("Dienthoai_20183599"));
        return thuThu;
    }

    private List<ThuThu> getList(ResultSet resultSet) throws SQLException{
        List<ThuThu> data = new ArrayList<>();
        while(resultSet.next()){
            data.add(getObject(resultSet));
        }
        return data;
    }

    public ThuThu findById(int id) throws SQLException{
        String sql = "SELECT * FROM thuthu_phamvannam WHERE Ma_TT_20183599 = ?";
        PreparedStatement preparedStatement = prepare(sql);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.first();
        return getObject(resultSet);
    }
    
    public List<ThuThu> findAll() throws SQLException {
        String sql = "SELECT * FROM thuthu_phamvannam";
        PreparedStatement preparedStatement = prepare(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        return getList(resultSet);
    }

    public boolean insertTT(ThuThu thuThu) throws SQLException{
        String sql = "INSERT INTO thuthu_phamvannam VALUES(null, ?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = prepare(sql);
        preparedStatement.setString(1, thuThu.getTenTT());
        preparedStatement.setInt(2, thuThu.getGioiTinh());
        preparedStatement.setDate(3, thuThu.getNgaySinh());
        preparedStatement.setString(4, thuThu.getCMND());
        preparedStatement.setString(5, thuThu.getEmail());
        preparedStatement.setString(6, thuThu.getDienThoai());
        return preparedStatement.executeUpdate() > 0;
    }
    
    public boolean updateTT(ThuThu thuThu) throws SQLException {
        String sql = "UPDATE thuthu_phamvannam SET "+
                " Ten_TT_20183599 = ?, "+
                " GioiTinh_20183599 = ?, "+
                " NgaySinh_20183599 = ?, "+
                " CMND_20183599 = ?, "+
                " Email_20183599 = ?, "+
                " DienThoai_20183599 = ? "+
                " WHERE Ma_TT_20183599 = ?";
        PreparedStatement preparedStatement = prepare(sql);
        preparedStatement.setString(1, thuThu.getTenTT());
        preparedStatement.setInt(2, thuThu.getGioiTinh());
        preparedStatement.setDate(3, thuThu.getNgaySinh());
        preparedStatement.setString(4, thuThu.getCMND());
        preparedStatement.setString(5, thuThu.getEmail());
        preparedStatement.setString(6, thuThu.getDienThoai());
        preparedStatement.setInt(7, thuThu.getId());
        return preparedStatement.executeUpdate() > 0 ;
    }

    public boolean deleteTT(int id) throws SQLException {
        String sql = "DELETE FROM thuthu_phamvannam WHERE Ma_TT_20183599 = ?";
        PreparedStatement preparedStatement = prepare(sql);
        preparedStatement.setInt(1, id);
        return preparedStatement.executeUpdate() > 0;
    }
}
