package DAO;

import model.ChiTiet_Muon;
import model.MuonTra;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MuonTraDAO {

    private PreparedStatement prepare(String sql) throws SQLException {
        return DatabaseConnection.connection
                .prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
    }

    private MuonTra getObject(ResultSet resultSet) throws SQLException{
        MuonTra muonTra = new MuonTra();
        muonTra.setId(resultSet.getInt("MaMT_20183599"));
        muonTra.setId_DG(resultSet.getInt("Ma_DG_20183599"));
        muonTra.setId_TT(resultSet.getInt("Ma_TT_20183599"));
        muonTra.setNgayMuon(resultSet.getDate("Ngay_muon"));
        muonTra.setNgayHenTra(resultSet.getDate("Ngay_hentra"));
        muonTra.setTienCoc(resultSet.getDouble("Tiencoc"));
        muonTra.setDaTra(resultSet.getBoolean("DaTra_20183599"));
        return muonTra;
    }

    private List<MuonTra> getList(ResultSet resultSet) throws SQLException {
        List<MuonTra> data = new ArrayList<>();
        while (resultSet.next()) {
            data.add(getObject(resultSet));
        }
        return data;
    }


    public MuonTra findById(int id) throws SQLException {
        String sql = "SELECT * FROM muontra_phamvannam WHERE MaMT_20183599 = ? AND DaTra_20183599 = False";
        PreparedStatement preparedStatement = prepare(sql);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.first();
        return getObject(resultSet);
    }

    public List<MuonTra> findAll() throws SQLException {
        String sql = "SELECT * FROM muontra_phamvannam";
        PreparedStatement preparedStatement = prepare(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        return getList(resultSet);
    }

    public int insertMuonTra(MuonTra muonTra) throws SQLException{
        String sql = "INSERT INTO muontra_phamvannam VALUES(null, ?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = prepare(sql);
        preparedStatement.setInt(1, muonTra.getId_DG());
        preparedStatement.setInt(2, muonTra.getId_TT());
        preparedStatement.setDate(3, muonTra.getNgayMuon());
        preparedStatement.setDate(4, muonTra.getNgayHenTra());
        preparedStatement.setDouble(5, muonTra.getTienCoc());
        preparedStatement.setBoolean(6, muonTra.isDaTra());
        if(preparedStatement.executeUpdate() > 0){
            sql = "SELECT LAST_INSERT_ID() as id";
            preparedStatement = prepare(sql);
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
                return rs.getInt("id");
            }else{
                return -1;
            }
        }
        return -1;
    }

    public boolean updateDatra(int id, boolean daTra) throws SQLException{
        String sql = "UPDATE muontra_phamvannam " +
                " SET DaTra_20183599 = ? " +
                " WHERE MaMT_20183599 = ? ";
        PreparedStatement preparedStatement = prepare(sql);
        preparedStatement.setInt(2, id);
        preparedStatement.setBoolean(1, daTra);
        return preparedStatement.executeUpdate() > 0;
    }
}
