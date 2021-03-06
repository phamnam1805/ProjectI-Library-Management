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
        // ki???m tra id, s??? ti???n
        initCheckValue();
        // kh???i t???o ng??y
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
                alert.setContentText("ID ?????c gi??? kh??ng h???p l???");
                alert.setTitle("L???I");
                alert.setHeaderText("???? c?? l???i x???y ra!!!");
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
                alert.setContentText("ID th??? th?? kh??ng h???p l???");
                alert.setTitle("L???I");
                alert.setHeaderText("???? c?? l???i x???y ra!!!");
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
                alert.setContentText("Ti???n c???c kh??ng h???p l???");
                alert.setTitle("L???I");
                alert.setHeaderText("???? c?? l???i x???y ra!!!");
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
            alert.setTitle("Th??ng tin");
            alert.setHeaderText("Th??ng tin ?????c gi???");
            alert.setHeight(600);
            alert.setWidth(600);
            String info = "ID: "+docGia.getId()+"\n" +
                    "T??n: "+docGia.getTenDG()+"\n" +
                    "CMND: "+docGia.getCMND()+"\n" +
                    "Email: "+docGia.getEmail()+"\n" +
                    "??i???n tho???i: "+docGia.getDienThoai();
            alert.setContentText(info);
            alert.show();
        }catch (Exception ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("ID kh??ng h???p l??? ho???c kh??ng ch??nh x??c");
            alert.setTitle("L???I");
            alert.setHeaderText("???? c?? l???i x???y ra!!!");
            alert.show();
        }
    }

    public void chiTietThuThu(ActionEvent e){
        try{
            int id = Integer.parseInt(idThuThu.getText());
            ThuThu thuThu = thuThuDAO.findById(id);
            System.out.println(thuThu);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Th??ng tin");
            alert.setHeaderText("Th??ng tin th??? th??");
            alert.setHeight(600);
            alert.setWidth(600);
            String info = "ID: "+thuThu.getId()+"\n" +
                    "T??n: "+thuThu.getTenTT()+"\n" +
                    "CMND: "+thuThu.getCMND()+"\n" +
                    "Email: "+thuThu.getEmail()+"\n" +
                    "??i???n tho???i: "+thuThu.getDienThoai();
            alert.setContentText(info);
            alert.show();
        }catch (Exception ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("ID kh??ng h???p l??? ho???c kh??ng ch??nh x??c");
            alert.setTitle("L???I");
            alert.setHeaderText("???? c?? l???i x???y ra!!!");
            alert.show();
        }
    }

    public void themSach(ActionEvent e){
        Dialog<FormChiTietMuon> dialog = new Dialog<>();
        dialog.setTitle("Th??m s??ch");
        dialog.setHeaderText("Nh???p th??ng tin s??ch");

        ButtonType xacNhanButtonType = new ButtonType("X??c nh???n", ButtonBar.ButtonData.OK_DONE);
        ButtonType thoatButtonType = new ButtonType("Tho??t", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(xacNhanButtonType, thoatButtonType);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20,150,10,10));

        TextField idSach = new TextField();
        idSach.setPromptText("M?? s??ch");

        TextField ghiChu = new TextField();
        ghiChu.setPromptText("Ghi ch??");

        grid.add(new Label("M?? s??ch"), 0 ,0);
        grid.add(idSach, 1,0);
        grid.add(new Label("Ghi ch??"), 0, 1);
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
                    alert.setContentText("M?? s??ch kh??ng h???p l??? ho???c kh??ng ch??nh x??c");
                    alert.setTitle("L???I");
                    alert.setHeaderText("???? c?? l???i x???y ra!!!");
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
                    alert.setContentText("M?? s??ch "+id+" ???? t???n t???i");
                    alert.setTitle("L???I");
                    alert.setHeaderText("???? c?? l???i x???y ra!!!");
                    alert.show();
                }else {
                    chiTietMuonList.add(formChiTietMuon);
                }
            }catch (SQLException ex){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("M?? s??ch kh??ng h???p l??? ho???c kh??ng ch??nh x??c");
                alert.setTitle("L???I");
                alert.setHeaderText("???? c?? l???i x???y ra!!!");
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
        dialog.setTitle("S???a s??ch");
        dialog.setHeaderText("S???a th??ng tin s??ch");

        ButtonType xacNhanButtonType = new ButtonType("X??c nh???n", ButtonBar.ButtonData.OK_DONE);
        ButtonType thoatButtonType = new ButtonType("Tho??t", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(xacNhanButtonType, thoatButtonType);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20,150,10,10));


        TextField idSach = new TextField();
        idSach.setPromptText("M?? s??ch");
        idSach.setText(String.valueOf(formChiTietMuonOld.getMaSach()));

        TextField ghiChu = new TextField();
        ghiChu.setPromptText("Ghi ch??");
        ghiChu.setText(formChiTietMuonOld.getGhiChu());

        grid.add(new Label("M?? s??ch"), 0 ,0);
        grid.add(idSach, 1,0);
        grid.add(new Label("Ghi ch??"), 0, 1);
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
                    alert.setContentText("M?? s??ch kh??ng h???p l??? ho???c kh??ng ch??nh x??c");
                    alert.setTitle("L???I");
                    alert.setHeaderText("???? c?? l???i x???y ra!!!");
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
                        alert.setContentText("M?? s??ch "+id+" ???? t???n t???i");
                        alert.setTitle("L???I");
                        alert.setHeaderText("???? c?? l???i x???y ra!!!");
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
                alert.setContentText("M?? s??ch kh??ng h???p l??? ho???c kh??ng ch??nh x??c");
                alert.setTitle("L???I");
                alert.setHeaderText("???? c?? l???i x???y ra!!!");
                alert.show();
            }
        });
    }

    public void xoa(ActionEvent e){
        FormChiTietMuon selected = table.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("B???n c?? mu???n x??a cu???n s??ch "+selected.getTenSach()+" kh??ng?");
        alert.setTitle("X??c nh???n");
        alert.setHeaderText("X??c nh???n thao t??c");
        ButtonType xacNhanButtonType = new ButtonType("X??c nh???n", ButtonBar.ButtonData.OK_DONE);
        ButtonType thoatButtonType = new ButtonType("Tho??t", ButtonBar.ButtonData.CANCEL_CLOSE);
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
            alert.setContentText("Ng??y m?????n v?? ng??y h???n tr??? kh??ng h???p l???");
            alert.setTitle("L???I");
            alert.setHeaderText("???? c?? l???i x???y ra!!!");
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
                    alert.setContentText("B???n ch??a m?????n cu???n s??ch n??o !!!");
                    alert.setTitle("L???I");
                    alert.setHeaderText("???? c?? l???i x???y ra!!!");
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
                                data.add(new String[]{"ID m?????n tr???: " +idMT, "ID ?????c gi???: "+idDG, "ID th??? th??: "+idTT});
                                data.add(new String[]{null});
                                data.add(new String[]{"Ng??y m?????n: "+ngay_muon, "Ng??y h???n tr???: "+ngay_hen_tra});
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
                                data.add(new String[]{null, null, "Ti???n c???c: "+muonTra.getTienCoc()});
                                data.add(new String[]{null});
                                data.add(new String[]{null, "Ch??? k?? th??? th??", "Ch??? k?? ng?????i m?????n"});
                                writer.writeAll(data);
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("Th??ng b??o");
                                alert.setHeaderText("M?????n s??ch th??nh c??ng");
                                String info = "ID m?????n tr???: " +idMT+"\n" +
                                        "ID ?????c gi???: "+idDG+"\n" +
                                        "ID th??? th??: "+idTT+"\n" +
                                        "Ng??y m?????n: "+ngay_muon+"\n" +
                                        "Ng??y h???n tr???: "+ngay_hen_tra+"\n";
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
                        alert.setContentText("C?? l???i khi m?????n s??ch");
                        alert.setTitle("L???I");
                        alert.setHeaderText("???? c?? l???i x???y ra!!!");
                        alert.show();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            }catch (NumberFormatException ex){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("ID ?????c gi??? v?? th??? th?? kh??ng th??? ????? tr???ng");
                alert.setTitle("L???I");
                alert.setHeaderText("???? c?? l???i x???y ra!!!");
                alert.show();
            }catch (SQLException ex){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("ID ?????c gi??? ho???c th??? th?? kh??ng t???n t???i");
                alert.setTitle("L???I");
                alert.setHeaderText("???? c?? l???i x???y ra!!!");
                alert.show();
            }
        }
    }
}
