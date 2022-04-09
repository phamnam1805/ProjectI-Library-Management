package app.danhsachmuontra;

import DAO.ChiTiet_MuonDAO;
import DAO.MuonTraDAO;
import app.Main;
import com.mysql.cj.xdevapi.Table;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.ChiTiet_Muon;
import model.MuonTra;
import model.ThuThu;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    MuonTraDAO muonTraDAO;

    ChiTiet_MuonDAO chiTiet_muonDAO;

    @FXML
    private TableView<MuonTra> muonTraTableView;

    @FXML
    private TableColumn<MuonTra, Integer> maMTColumn_MT;

    @FXML
    private TableColumn<MuonTra, Integer> maDGColumn;

    @FXML
    private TableColumn<MuonTra, Integer> maTTColumn;

    @FXML
    private TableColumn<MuonTra, Date> ngayMuonColumn;

    @FXML
    private TableColumn<MuonTra, Date> ngayHenTraColumn;

    @FXML
    private TableColumn<MuonTra, Double> tienCocColumn;

    @FXML
    private TableColumn<MuonTra, String> daTraColumn;

    private ObservableList<MuonTra> muonTraObservableList;

    @FXML
    private TableView<ChiTiet_Muon> chiTietMuonTableView;

    @FXML
    private TableColumn<ChiTiet_Muon, Integer> maMTColumn_CTM;

    @FXML
    private TableColumn<ChiTiet_Muon, Integer> maSachColumn;

    @FXML
    private TableColumn<ChiTiet_Muon, Date> ngayTraColumn;

    @FXML
    private TableColumn<ChiTiet_Muon, String> trangThaiColumn;

    @FXML
    private TableColumn<ChiTiet_Muon, Double> tienPhatColumn;

    @FXML
    private TableColumn<ChiTiet_Muon, String> ghiChuColumn;

    private ObservableList<ChiTiet_Muon> chiTietMuonObservableList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initDAOs();
        initTableMuonTra();
        initTableCTM();
        loadDataTableMuonTra();
    }

    private void initDAOs(){
        muonTraDAO = new MuonTraDAO();
        chiTiet_muonDAO = new ChiTiet_MuonDAO();
    }

    private void initTableMuonTra(){
        initColumnsTableMuonTra();
        loadDataTableMuonTra();
        muonTraTableView.setRowFactory( RowFactory -> {
            TableRow<MuonTra> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    MuonTra rowData = row.getItem();
                    loadDataTableCTM(rowData.getId());
                }
            });
            return row;
        });
    }

    private void initColumnsTableMuonTra(){
        maMTColumn_MT.setCellValueFactory(new PropertyValueFactory<>("id"));
        maDGColumn.setCellValueFactory(new PropertyValueFactory<>("id_DG"));
        maTTColumn.setCellValueFactory(new PropertyValueFactory<>("id_TT"));
        ngayMuonColumn.setCellValueFactory(new PropertyValueFactory<>("ngayMuon"));
        ngayHenTraColumn.setCellValueFactory(new PropertyValueFactory<>("ngayHenTra"));
        tienCocColumn.setCellValueFactory(new PropertyValueFactory<>("tienCoc"));
        daTraColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<MuonTra, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<MuonTra, String> param) {
                MuonTra muonTra = param.getValue();
                String daTra = "";
                if(muonTra.isDaTra()){
                    daTra = "Đã trả";
                }else{
                    daTra = "Chưa trả";
                }
                return new ReadOnlyObjectWrapper<>(daTra);
            }
        });
    }

    private void loadDataTableMuonTra(){
        try {
            muonTraObservableList = FXCollections.observableArrayList(muonTraDAO.findAll());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        muonTraTableView.setItems(muonTraObservableList);
    }

    private void initTableCTM(){
        initColumnsTableCTM();
    }

    private void initColumnsTableCTM(){
        maMTColumn_CTM.setCellValueFactory(new PropertyValueFactory<>("maMT"));
        maSachColumn.setCellValueFactory(new PropertyValueFactory<>("maSach"));
        ngayTraColumn.setCellValueFactory(new PropertyValueFactory<>("ngayTra"));
        trangThaiColumn.setCellValueFactory(new PropertyValueFactory<>("trangThaiSach"));
        tienPhatColumn.setCellValueFactory(new PropertyValueFactory<>("tienPhat"));
        ghiChuColumn.setCellValueFactory(new PropertyValueFactory<>("ghiChu"));
    }

    private void loadDataTableCTM(int id){
        try {
            chiTietMuonObservableList = FXCollections.observableArrayList(chiTiet_muonDAO.findByMuonTraId(id));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        chiTietMuonTableView.setItems(chiTietMuonObservableList);
    }

    public void quayLai(ActionEvent e){
        Stage stage = (Stage)(((Node) e.getSource()).getScene().getWindow());
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("sample.fxml"));
        try {
            Parent root = loader.load();
//            Scene scene = new Scene(root,stage.getWidth(),stage.getHeight());
            Scene scene = new Scene(root);
            stage.setScene(scene);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
