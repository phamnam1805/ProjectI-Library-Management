package app.trasach;

import DAO.*;
import app.Main;
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
import javafx.util.StringConverter;
import model.ChiTiet_Muon;
import model.MuonTra;
import model.Sach;
import model.form.FormChiTietMuon;
import model.form.FormChiTietTra;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    private MuonTra muonTra;

    DocGiaDAO docGiaDAO;

    ThuThuDAO thuThuDAO;

    SachDAO sachDAO;

    MuonTraDAO muonTraDAO;

    ChiTiet_MuonDAO chiTiet_muonDAO;

    @FXML
    private TextField ngayMuon;

    @FXML
    private TextField ngayHenTra;

    @FXML
    private TextField idDocGia;

    @FXML
    private TextField idThuThu;

    @FXML
    private TextField tienCoc;

    @FXML
    private TextField tongTienPhat;

    @FXML
    private TextField conPhaiDong;

    @FXML
    private TableView<FormChiTietTra> table;

    @FXML
    private TableColumn<FormChiTietTra, Integer> maSachColumn;

    @FXML
    private TableColumn<FormChiTietTra, String> tenSachColumn;

    @FXML
    private TableColumn<FormChiTietTra, Date> ngayTraColumn;

    @FXML
    private TableColumn<FormChiTietTra, String> trangThaiSachColumn;

    @FXML
    private TableColumn<FormChiTietTra, Double> tienPhatColumn;

    @FXML
    private TableColumn<FormChiTietTra, String> ghiChuColumn;

    @FXML
    private TableColumn<FormChiTietTra, Button> suaColumn;


    private ObservableList<FormChiTietTra> chiTietTraList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // khoi tao DAO
        initDAOs();
        // table
    }

    public void constructor(MuonTra muonTra) {
        this.muonTra = muonTra;
        initDateValue();
        initTable();
        initTextFieldValue();
    }

    private void initTextFieldValue(){
        idDocGia.setText(String.valueOf(muonTra.getId_DG()));
        idThuThu.setText(String.valueOf(muonTra.getId_TT()));
        tienCoc.setText(String.valueOf(muonTra.getTienCoc()));
        tongTienPhat.setText(String.valueOf(tinhTongTienPhat()));
        conPhaiDong.setText(String.valueOf(tinhConPhaiDong()));
    }


    private void initDateValue(){
        ngayMuon.setText(muonTra.getNgayMuon().toString());
        ngayHenTra.setText(muonTra.getNgayHenTra().toString());
    }


    private void initDAOs(){
        docGiaDAO = new DocGiaDAO();
        thuThuDAO = new ThuThuDAO();
        sachDAO = new SachDAO();
        muonTraDAO = new MuonTraDAO();
        chiTiet_muonDAO = new ChiTiet_MuonDAO();
    }
    private void initTable(){
        initCols();
        chiTietTraList = FXCollections.observableArrayList(new ArrayList<>());
        try{
            List<ChiTiet_Muon> chiTietMuonList = new ArrayList<>(chiTiet_muonDAO.findByMuonTraId(muonTra.getId()));
            for (int i = 0; i < chiTietMuonList.size(); i++) {
                int maSach = chiTietMuonList.get(i).getMaSach();
                String tenSach = sachDAO.findById(maSach).getTenSach();
                FormChiTietTra formChiTietTra = new FormChiTietTra();
                formChiTietTra.setMaMT(muonTra.getId());
                formChiTietTra.setTenSach(tenSach);
                formChiTietTra.setMaSach(maSach);
                formChiTietTra.setGhiChu(chiTietMuonList.get(i).getGhiChu());
                formChiTietTra.setTrangThaiSach(chiTietMuonList.get(i).getTrangThaiSach());
                formChiTietTra.setTienPhat(chiTietMuonList.get(i).getTienPhat());
                formChiTietTra.setNgayTra(Date.valueOf(LocalDate.now()));
                Button suaButton = new Button("S???a");
                suaButton.setOnAction(e -> {
                    int index = chiTietTraList.indexOf(formChiTietTra);
                    sua(formChiTietTra);
                    chiTietTraList.set(index, formChiTietTra);
                });
                suaButton.setMaxHeight(200);
                suaButton.setMaxWidth(200);
                formChiTietTra.setSua(suaButton);
                chiTietTraList.add(formChiTietTra);
            }
        }catch (SQLException ex){
            ex.printStackTrace();
        }
        table.setItems(chiTietTraList);
    }

    private void initCols(){
        maSachColumn.setCellValueFactory(new PropertyValueFactory<>("MaSach"));
        tenSachColumn.setCellValueFactory(new PropertyValueFactory<>("tenSach"));
        ghiChuColumn.setCellValueFactory(new PropertyValueFactory<>("ghiChu"));
        ngayTraColumn.setCellValueFactory(new PropertyValueFactory<>("ngayTra"));
        trangThaiSachColumn.setCellValueFactory(new PropertyValueFactory<>("trangThaiSach"));
        tienPhatColumn.setCellValueFactory(new PropertyValueFactory<>("tienPhat"));
        suaColumn.setCellValueFactory(new PropertyValueFactory<>("sua"));

    }

    private double tinhTongTienPhat(){
        double sum = 0;
        for (int i = 0; i < chiTietTraList.size(); i++) {
            sum += chiTietTraList.get(i).getTienPhat();
        }
        return  sum;
    }

    private double tinhConPhaiDong(){
        return tinhTongTienPhat() - muonTra.getTienCoc();
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

    public void sua(FormChiTietTra selected){
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("S???a th??ng tin");
        dialog.setHeaderText("S???a th??ng tin tr??? s??ch");

        ButtonType xacNhanButtonType = new ButtonType("X??c nh???n", ButtonBar.ButtonData.OK_DONE);
        ButtonType thoatButtonType = new ButtonType("Tho??t", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(xacNhanButtonType, thoatButtonType);

        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setPadding(new Insets(20,150,10,10));
        grid.setPrefSize(600,200);


        TextField idSach = new TextField();
        idSach.setPromptText("M?? s??ch");
        idSach.setText(String.valueOf(selected.getMaSach()));
        idSach.setEditable(false);

        TextField ghiChu = new TextField();
        ghiChu.setPromptText("Ghi ch??");
        ghiChu.setText(selected.getGhiChu());
        ghiChu.setPrefSize(300,30);

        TextField tienPhat = new TextField();
        tienPhat.setPromptText("Ghi ch??");
        tienPhat.setText(String.valueOf(selected.getTienPhat()));
        tienPhat.setPrefSize(300,30);

        TextField trangThai = new TextField();
        trangThai.setPromptText("Tr???ng th??i");
        trangThai.setText(selected.getTrangThaiSach());
        trangThai.setPrefSize(300,30);

        grid.add(new Label("M?? s??ch"), 0 ,0);
        grid.add(idSach, 1,0);
        grid.add(new Label("Ghi ch??"), 0, 1);
        grid.add(ghiChu, 1, 1);
        grid.add(new Label("Ti???n ph???t"),0,2);
        grid.add(tienPhat,1,2);
        grid.add(new Label("Tr???ng th??i s??ch"),0,3);
        grid.add(trangThai, 1,3);

        dialog.getDialogPane().setContent(grid);

        Optional<ButtonType> result = dialog.showAndWait();
        if(result.get() == xacNhanButtonType){
            try{
                selected.setTienPhat(Double.parseDouble(tienPhat.getText()));
                selected.setTrangThaiSach(trangThai.getText());
                selected.setGhiChu(ghiChu.getText());
                tongTienPhat.setText(String.valueOf(tinhTongTienPhat()));
                conPhaiDong.setText(String.valueOf(tinhConPhaiDong()));
            }catch (NumberFormatException ex){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Ti???n ph???t kh??ng h???p l???");
                alert.setTitle("L???I");
                alert.setHeaderText("???? c?? l???i x???y ra!!!");
                alert.show();
            }
        }
    }

    public void xacNhan(ActionEvent e){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("X??c nh???n");
        alert.setHeaderText("X??c nh???n tr??? s??ch?");
        ButtonType xacNhanButtonType = new ButtonType("X??c nh???n", ButtonBar.ButtonData.OK_DONE);
        ButtonType thoatButtonType = new ButtonType("Tho??t", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().clear();
        alert.getButtonTypes().addAll(xacNhanButtonType, thoatButtonType);
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == xacNhanButtonType){
            try{
                for (int i = 0; i < chiTietTraList.size() ; i++) {
                    chiTiet_muonDAO.updateChiTietMuon(chiTietTraList.get(i).toChiTietMuon());
                }
                muonTraDAO.updateDatra(muonTra.getId(), true);
                Alert alertSuccess = new Alert(Alert.AlertType.INFORMATION);
                alertSuccess.setTitle("Th??ng b??o");
                alertSuccess.setHeaderText("Tr??? s??ch th??nh c??ng");
                String content = "ID m?????n tr???: "+muonTra.getId()+ "\n" +
                        "ID ?????c gi???: "+muonTra.getId_DG()+"\n" +
                        "ID th??? th??: "+muonTra.getId_TT()+"\n" +
                        "Ti???n c???c: "+muonTra.getTienCoc()+"\n" +
                        "T???ng ti???n ph???t: "+tongTienPhat.getText()+"\n" +
                        "C??n ph???i ????ng th??m: "+conPhaiDong.getText()+"\n"+
                        "Ng??y tr???: "+chiTietTraList.get(0).getNgayTra().toString();
                alertSuccess.setContentText(content);
                alertSuccess.show();
                quayLai(e);
            }catch (SQLException ex){
                ex.printStackTrace();
                Alert alertError = new Alert(Alert.AlertType.ERROR);
                alertError.setContentText("???? c?? l???i x???y ra !!!");
                alertError.setTitle("L???I");
                alertError.setHeaderText("!!!");
                alertError.show();
            }
        }
    }

}
