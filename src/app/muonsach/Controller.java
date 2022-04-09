package app.muonsach;

import DAO.*;
import app.Main;
import com.opencsv.CSVWriter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import model.*;
import model.form.FormChiTietMuon;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Controller implements Initializable {
    DocGiaDAO docGiaDAO;

    ThuThuDAO thuThuDAO;

    SachDAO sachDAO;

    MuonTraDAO muonTraDAO;

    ChiTiet_MuonDAO chiTiet_muonDAO;

    @FXML
    private DatePicker ngayMuon;

    @FXML
    private DatePicker ngayHenTra;

    @FXML
    private TextField idDocGia;

    @FXML
    private TextField idThuThu;

    @FXML
    private TextField tienCoc;

    @FXML
    private TableView<FormChiTietMuon> table;

    @FXML
    private TableColumn<FormChiTietMuon, Integer> maSachColumn;

    @FXML
    private TableColumn<FormChiTietMuon, String> ghiChuColumn;

    @FXML
    private TableColumn<FormChiTietMuon, String> tenSachColumn;

    private ObservableList<FormChiTietMuon> chiTietMuonList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // khoi tao DAO
        initDAOs();
        // kiểm tra id, số tiền
        initCheckValue();
        // khởi tạo ngày
        initDate();
        // table
        initTable();
        chiTietMuonList = FXCollections.observableArrayList(new ArrayList<FormChiTietMuon>());
        table.setItems(chiTietMuonList);

    }

    private void initDate(){
        ngayHenTra.setValue(LocalDate.now());
        ngayMuon.setValue(LocalDate.now());
        // converter
        String pattern = "dd-MM-yyyy";
        StringConverter<LocalDate> converter = new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter =
                    DateTimeFormatter.ofPattern(pattern);

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        };
        ngayHenTra.setConverter(converter);
        ngayMuon.setConverter(converter);
    }

    private void initCheckValue(){
        idDocGia.textProperty().addListener((observable, oldValue, newValue) -> {
            try{
                if(!newValue.isEmpty()){
                    int id = Integer.parseInt(newValue);
                }
            }catch (NumberFormatException ex){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("ID độc giả không hợp lệ");
                alert.setTitle("LỖI");
                alert.setHeaderText("Đã có lỗi xảy ra!!!");
                alert.show();
                idDocGia.setText("");
            }
        });

        idThuThu.textProperty().addListener((observable, oldValue, newValue) -> {
            try{
                if(!newValue.isEmpty()){
                    int id = Integer.parseInt(newValue);
                }
            }catch (NumberFormatException ex){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("ID thủ thư không hợp lệ");
                alert.setTitle("LỖI");
                alert.setHeaderText("Đã có lỗi xảy ra!!!");
                alert.show();
                idThuThu.setText("");
            }
        });

        tienCoc.textProperty().addListener((observable, oldValue, newValue) -> {
            try{
                if(!newValue.isEmpty()){
                    double tienCoc = Double.parseDouble(newValue);
                }
            }catch (NumberFormatException ex){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Tiền cọc không hợp lệ");
                alert.setTitle("LỖI");
                alert.setHeaderText("Đã có lỗi xảy ra!!!");
                alert.show();
                tienCoc.setText("");
            }
        });
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
    }

    private void initCols(){
        maSachColumn.setCellValueFactory(new PropertyValueFactory<>("MaSach"));
        tenSachColumn.setCellValueFactory(new PropertyValueFactory<>("tenSach"));
        ghiChuColumn.setCellValueFactory(new PropertyValueFactory<>("ghiChu"));

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

    public void chiTietDocGia(ActionEvent e){
        try{
            int id = Integer.parseInt(idDocGia.getText());
            DocGia docGia = docGiaDAO.findById(id);
            System.out.println(docGia);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thông tin");
            alert.setHeaderText("Thông tin độc giả");
            alert.setHeight(600);
            alert.setWidth(600);
            String info = "ID: "+docGia.getId()+"\n" +
                    "Tên: "+docGia.getTenDG()+"\n" +
                    "CMND: "+docGia.getCMND()+"\n" +
                    "Email: "+docGia.getEmail()+"\n" +
                    "Điện thoại: "+docGia.getDienThoai();
            alert.setContentText(info);
            alert.show();
        }catch (Exception ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("ID không hợp lệ hoặc không chính xác");
            alert.setTitle("LỖI");
            alert.setHeaderText("Đã có lỗi xảy ra!!!");
            alert.show();
        }
    }

    public void chiTietThuThu(ActionEvent e){
        try{
            int id = Integer.parseInt(idThuThu.getText());
            ThuThu thuThu = thuThuDAO.findById(id);
            System.out.println(thuThu);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thông tin");
            alert.setHeaderText("Thông tin thủ thư");
            alert.setHeight(600);
            alert.setWidth(600);
            String info = "ID: "+thuThu.getId()+"\n" +
                    "Tên: "+thuThu.getTenTT()+"\n" +
                    "CMND: "+thuThu.getCMND()+"\n" +
                    "Email: "+thuThu.getEmail()+"\n" +
                    "Điện thoại: "+thuThu.getDienThoai();
            alert.setContentText(info);
            alert.show();
        }catch (Exception ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("ID không hợp lệ hoặc không chính xác");
            alert.setTitle("LỖI");
            alert.setHeaderText("Đã có lỗi xảy ra!!!");
            alert.show();
        }
    }

    public void themSach(ActionEvent e){
        Dialog<FormChiTietMuon> dialog = new Dialog<>();
        dialog.setTitle("Thêm sách");
        dialog.setHeaderText("Nhập thông tin sách");

        ButtonType xacNhanButtonType = new ButtonType("Xác nhận", ButtonBar.ButtonData.OK_DONE);
        ButtonType thoatButtonType = new ButtonType("Thoát", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(xacNhanButtonType, thoatButtonType);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20,150,10,10));

        TextField idSach = new TextField();
        idSach.setPromptText("Mã sách");

        TextField ghiChu = new TextField();
        ghiChu.setPromptText("Ghi chú");

        grid.add(new Label("Mã sách"), 0 ,0);
        grid.add(idSach, 1,0);
        grid.add(new Label("Ghi chú"), 0, 1);
        grid.add(ghiChu, 1, 1);

        Node xacNhanButton = dialog.getDialogPane().lookupButton(xacNhanButtonType);
        xacNhanButton.setDisable(true);
        idSach.textProperty().addListener((observable, oldValue, newValue) -> {
            xacNhanButton.setDisable(newValue.trim().isEmpty());
        });

        dialog.getDialogPane().setContent(grid);
        dialog.setResultConverter(dialogButton ->{
            if(dialogButton == xacNhanButtonType){
                FormChiTietMuon formChiTietMuon = new FormChiTietMuon();
                try{
                    formChiTietMuon.setMaSach(Integer.parseInt(idSach.getText()));
                }catch (NumberFormatException ex){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Mã sách không hợp lệ hoặc không chính xác");
                    alert.setTitle("LỖI");
                    alert.setHeaderText("Đã có lỗi xảy ra!!!");
                    alert.show();
                }
                formChiTietMuon.setGhiChu(ghiChu.getText());
                return formChiTietMuon;
            }
            return null;
        });

        Optional<FormChiTietMuon> result = dialog.showAndWait();
        result.ifPresent(formChiTietMuon -> {
            int id = formChiTietMuon.getMaSach();
            try{
                Sach sach = sachDAO.findById(id);
                String tenSach = sach.getTenSach();
                formChiTietMuon.setTenSach(tenSach);
                if(checkTrungSach(id)){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Mã sách "+id+" đã tồn tại");
                    alert.setTitle("LỖI");
                    alert.setHeaderText("Đã có lỗi xảy ra!!!");
                    alert.show();
                }else {
                    chiTietMuonList.add(formChiTietMuon);
                }
            }catch (SQLException ex){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Mã sách không hợp lệ hoặc không chính xác");
                alert.setTitle("LỖI");
                alert.setHeaderText("Đã có lỗi xảy ra!!!");
                alert.show();
            }
        });
    }

    public boolean checkTrungSach(int id){
        for (int i = 0; i < chiTietMuonList.size(); i++) {
            if(chiTietMuonList.get(i).getMaSach() == id){
                return true;
            }
        }
        return false;
    }

    public void sua(ActionEvent e){
        FormChiTietMuon formChiTietMuonOld = table.getSelectionModel().getSelectedItem();
        Dialog<FormChiTietMuon> dialog = new Dialog<>();
        dialog.setTitle("Sửa sách");
        dialog.setHeaderText("Sửa thông tin sách");

        ButtonType xacNhanButtonType = new ButtonType("Xác nhận", ButtonBar.ButtonData.OK_DONE);
        ButtonType thoatButtonType = new ButtonType("Thoát", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(xacNhanButtonType, thoatButtonType);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20,150,10,10));


        TextField idSach = new TextField();
        idSach.setPromptText("Mã sách");
        idSach.setText(String.valueOf(formChiTietMuonOld.getMaSach()));

        TextField ghiChu = new TextField();
        ghiChu.setPromptText("Ghi chú");
        ghiChu.setText(formChiTietMuonOld.getGhiChu());

        grid.add(new Label("Mã sách"), 0 ,0);
        grid.add(idSach, 1,0);
        grid.add(new Label("Ghi chú"), 0, 1);
        grid.add(ghiChu, 1, 1);

        Node xacNhanButton = dialog.getDialogPane().lookupButton(xacNhanButtonType);
        xacNhanButton.setDisable(true);
        idSach.textProperty().addListener((observable, oldValue, newValue) -> {
            xacNhanButton.setDisable(newValue.trim().isEmpty());
        });

        dialog.getDialogPane().setContent(grid);
        dialog.setResultConverter(dialogButton ->{
            if(dialogButton == xacNhanButtonType){
                FormChiTietMuon formChiTietMuonNew = new FormChiTietMuon();
                try{
                    formChiTietMuonNew.setMaSach(Integer.parseInt(idSach.getText()));
                }catch (NumberFormatException ex){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Mã sách không hợp lệ hoặc không chính xác");
                    alert.setTitle("LỖI");
                    alert.setHeaderText("Đã có lỗi xảy ra!!!");
                    alert.show();
                }
                formChiTietMuonNew.setGhiChu(ghiChu.getText());
                return formChiTietMuonNew;
            }
            return formChiTietMuonOld;
        });

        Optional<FormChiTietMuon> result = dialog.showAndWait();
        result.ifPresent(formChiTietMuon -> {
            int id = formChiTietMuon.getMaSach();
            try{
                Sach sach = sachDAO.findById(id);
                String tenSach = sach.getTenSach();
                formChiTietMuon.setTenSach(tenSach);
                if(formChiTietMuonOld.getMaSach() != formChiTietMuon.getMaSach()){
                    if(checkTrungSach(id)){
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Mã sách "+id+" đã tồn tại");
                        alert.setTitle("LỖI");
                        alert.setHeaderText("Đã có lỗi xảy ra!!!");
                        alert.show();
                    }else {
                        int index = chiTietMuonList.indexOf(formChiTietMuonOld);
                        chiTietMuonList.set(index, formChiTietMuon);
                    }
                }else {
                    int index = chiTietMuonList.indexOf(formChiTietMuonOld);
                    chiTietMuonList.set(index, formChiTietMuon);
                }

            }catch (SQLException ex){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Mã sách không hợp lệ hoặc không chính xác");
                alert.setTitle("LỖI");
                alert.setHeaderText("Đã có lỗi xảy ra!!!");
                alert.show();
            }
        });
    }

    public void xoa(ActionEvent e){
        FormChiTietMuon selected = table.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Bạn có muốn xóa cuốn sách "+selected.getTenSach()+" không?");
        alert.setTitle("Xác nhận");
        alert.setHeaderText("Xác nhận thao tác");
        ButtonType xacNhanButtonType = new ButtonType("Xác nhận", ButtonBar.ButtonData.OK_DONE);
        ButtonType thoatButtonType = new ButtonType("Thoát", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().clear();
        alert.getButtonTypes().addAll(xacNhanButtonType, thoatButtonType);
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == xacNhanButtonType){
            chiTietMuonList.remove(selected);
        }
    }

    public void xacNhan(ActionEvent e){
        MuonTra muonTra = new MuonTra();
        LocalDate ngay_muon = ngayMuon.getValue();
        LocalDate ngay_hen_tra = ngayHenTra.getValue();
        if(ngay_hen_tra.isBefore(ngay_muon)){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Ngày mượn và ngày hẹn trả không hợp lệ");
            alert.setTitle("LỖI");
            alert.setHeaderText("Đã có lỗi xảy ra!!!");
            alert.show();
        }else{
            try{
                int idTT = Integer.parseInt(idThuThu.getText());
                int idDG = Integer.parseInt(idDocGia.getText());
                thuThuDAO.findById(idTT);
                docGiaDAO.findById(idDG);
                muonTra.setId_DG(idDG);
                muonTra.setId_TT(idTT);
                if(tienCoc.getText().isEmpty()){
                    muonTra.setTienCoc(0);
                }else{
                    muonTra.setTienCoc(Double.parseDouble(tienCoc.getText()));
                }
                muonTra.setNgayHenTra(Date.valueOf(ngay_hen_tra));
                muonTra.setNgayMuon(Date.valueOf(ngay_muon));

                if(chiTietMuonList.size() == 0){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Bạn chưa mượn cuốn sách nào !!!");
                    alert.setTitle("LỖI");
                    alert.setHeaderText("Đã có lỗi xảy ra!!!");
                    alert.show();
                }else{
                    try{
                        int idMT = muonTraDAO.insertMuonTra(muonTra);
                        muonTra.setId(idMT);
                        System.out.println(muonTra);
                        if(idMT != -1){
                            DirectoryChooser directoryChooser = new DirectoryChooser();
                            Stage stage = (Stage)(((Node) e.getSource()).getScene().getWindow());
                            File dir = directoryChooser.showDialog(stage);
                            if(dir!=null){
                                String fileName = "MuonSach_"+idMT+"_"+idDG+"_"+idTT+".csv";
                                String filePath = dir.getAbsolutePath()+"\\"+fileName;
                                File file = new File(filePath);
                                FileOutputStream fos = new FileOutputStream(file);
                                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
                                CSVWriter writer = new CSVWriter(outputStreamWriter);
                                List<String[]> data = new ArrayList<String[]>();
                                data.add(new String[]{"ID mượn trả: " +idMT, "ID độc giả: "+idDG, "ID thủ thư: "+idTT});
                                data.add(new String[]{null});
                                data.add(new String[]{"Ngày mượn: "+ngay_muon, "Ngày hẹn trả: "+ngay_hen_tra});
                                data.add(new String[]{null});
                                data.add(FormChiTietMuon.getHeader());
                                for(int i = 0; i < chiTietMuonList.size(); i++){
                                    ChiTiet_Muon chiTiet_muon = new ChiTiet_Muon();
                                    chiTiet_muon.setMaSach(chiTietMuonList.get(i).getMaSach());
                                    chiTiet_muon.setGhiChu(chiTietMuonList.get(i).getGhiChu());
                                    chiTiet_muon.setMaMT(idMT);
                                    chiTiet_muonDAO.insertChiTietMuon(chiTiet_muon);
                                    System.out.println(chiTiet_muon);
                                    data.add(chiTietMuonList.get(i).getPrint());
                                }
                                data.add(new String[]{null});
                                data.add(new String[]{null, null, "Tiền cọc: "+muonTra.getTienCoc()});
                                data.add(new String[]{null});
                                data.add(new String[]{null, "Chữ kí thủ thư", "Chữ kí người mượn"});
                                writer.writeAll(data);
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("Thông báo");
                                alert.setHeaderText("Mượn sách thành công");
                                String info = "ID mượn trả: " +idMT+"\n" +
                                        "ID độc giả: "+idDG+"\n" +
                                        "ID thủ thư: "+idTT+"\n" +
                                        "Ngày mượn: "+ngay_muon+"\n" +
                                        "Ngày hẹn trả: "+ngay_hen_tra+"\n";
                                alert.setContentText(info);
                                alert.show();
                                FXMLLoader loader = new FXMLLoader();
                                loader.setLocation(Main.class.getResource("sample.fxml"));
                                try {
                                    Parent root = loader.load();
                                    Scene scene = new Scene(root,stage.getWidth(),stage.getHeight());
                                    stage.setScene(scene);
                                } catch (IOException ioException) {
                                    ioException.printStackTrace();
                                }finally {
                                    writer.close();
                                    outputStreamWriter.close();
                                    fos.close();
                                }
                            }
                        }
                    }catch (SQLException ex){
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Có lỗi khi mượn sách");
                        alert.setTitle("LỖI");
                        alert.setHeaderText("Đã có lỗi xảy ra!!!");
                        alert.show();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            }catch (NumberFormatException ex){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("ID độc giả và thủ thư không thể để trống");
                alert.setTitle("LỖI");
                alert.setHeaderText("Đã có lỗi xảy ra!!!");
                alert.show();
            }catch (SQLException ex){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("ID độc giả hoặc thủ thư không tồn tại");
                alert.setTitle("LỖI");
                alert.setHeaderText("Đã có lỗi xảy ra!!!");
                alert.show();
            }
        }
    }
}
