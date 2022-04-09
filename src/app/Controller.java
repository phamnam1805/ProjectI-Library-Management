package app;

import DAO.MuonTraDAO;
import DAO.SachDAO;
import com.opencsv.CSVWriter;
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
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.MuonTra;
import model.Sach;
import model.form.FormChiTietMuon;

import javax.xml.soap.Text;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class Controller implements Initializable {
    SachDAO sachDAO;

    MuonTraDAO muonTraDAO;

    DirectoryChooser directoryChooser;

    @FXML
    private TableView<Sach> table;

    @FXML
    private TableColumn<Sach, Integer> idColumn;

    @FXML
    private TableColumn<Sach, String> tenColumn;

    @FXML
    private TableColumn<Sach, String> tacGiaColumn;

    @FXML
    private TableColumn<Sach, String> nhaXBColumn;

    @FXML
    private TableColumn<Sach, String> namXBColumn;

    @FXML
    private TableColumn<Sach, Double> donGiaColumn;

    @FXML
    private TableColumn<Sach, String> gioiThieuColumn;

    @FXML
    private TableColumn<Sach, Void> buttonsColumn;

    private ObservableList<Sach> sachList;

    @FXML
    private TextField timKiemTextField;

    @FXML
    private Button timKiemButton;

    @FXML
    private Button tatCaSachButton;

    @FXML
    private Button muonSachButton;

    @FXML
    ComboBox<String> comboBox;

    @FXML
    ObservableList<String> comboBoxList;

    int methodSearch;
    //1. theo tên sách
    //2. theo tác giả
    //3. theo nhà xuất bản
    //4. theo năm xuất bản

    @FXML
    private TextField tenSachTextField;

    @FXML
    private TextField tacGiaTextField;

    @FXML
    private TextField nhaXuatBanTextField;

    @FXML
    private TextField namXuatBanTextField;

    @FXML
    private TextField donGiaTextField;

    @FXML
    private TextField gioiThieuTextField;

    @FXML
    private Button themSachButton;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        comboBoxList = FXCollections.observableArrayList("Tên sách", "Tác giả", "Nhà xuất bản", "Năm xuất bản");
        comboBox.setItems(comboBoxList);
        methodSearch = 1;
        comboBox.setValue("Tên sách");

        directoryChooser = new DirectoryChooser();
        initDAOs();
        initTable();
        initThemSach();

    }

    private void initDAOs(){
        sachDAO = new SachDAO();
        muonTraDAO = new MuonTraDAO();
    }

    private void initTable(){
        initColumns();
        loadData();
        table.setRowFactory( RowFactory -> {
            TableRow<Sach> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    Sach rowData = row.getItem();
                    Dialog<ButtonType> suaSachDialog = new Dialog<>();
                    suaSachDialog.setTitle("Sửa thông tin");
                    suaSachDialog.setHeaderText("Sửa thông tin của cuốn sách có id: "+rowData.getId());

                    ButtonType xacNhanButtonType = new ButtonType("Xác nhận", ButtonBar.ButtonData.OK_DONE);
                    ButtonType thoatButtonType = new ButtonType("Thoát", ButtonBar.ButtonData.CANCEL_CLOSE);
                    suaSachDialog.getDialogPane().getButtonTypes().addAll(xacNhanButtonType, thoatButtonType);

                    GridPane gridPane = new GridPane();
                    gridPane.setHgap(10);
                    gridPane.setVgap(10);
                    gridPane.setPadding(new Insets(20,150,10,10));

                    TextField tenSachTF = new TextField();
                    tenSachTF.setPromptText("Tên sách");
                    tenSachTF.setText(rowData.getTenSach());

                    TextField tacGiaTF = new TextField();
                    tacGiaTF.setPromptText("Tác giả");
                    tacGiaTF.setText(rowData.getTacGia());

                    TextField nhaXBTF = new TextField();
                    nhaXBTF.setPromptText("Nhà xuất bản");
                    nhaXBTF.setText(rowData.getNhaXB());

                    TextField namXBTF = new TextField();
                    namXBTF.setPromptText("Năm xuất bản");
                    namXBTF.setText(String.valueOf(rowData.getNamXB()));
                    namXBTF.textProperty().addListener((observable, oldValue, newValue) -> {
                        try{
                            if(!newValue.isEmpty()){
                                Integer.parseInt(newValue);
                            }
                        }catch (NumberFormatException ex){
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setContentText("Năm xuất bản không hợp lệ");
                            alert.setTitle("LỖI");
                            alert.setHeaderText("Đã có lỗi xảy ra!!!");
                            alert.show();
                            namXBTF.setText("");
                        }
                    });


                    TextField donGiaTF = new TextField();
                    donGiaTF.setPromptText("Đơn giá");
                    donGiaTF.setText(String.valueOf(rowData.getDonGia()));
                    donGiaTF.textProperty().addListener((observable, oldValue, newValue) -> {
                        try{
                            if(!newValue.isEmpty()){
                                Integer.parseInt(newValue);
                            }
                        }catch (NumberFormatException ex){
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setContentText("Đơn giá không hợp lệ");
                            alert.setTitle("LỖI");
                            alert.setHeaderText("Đã có lỗi xảy ra!!!");
                            alert.show();
                            donGiaTF.setText("");
                        }
                    });

                    Node xacNhanButton = suaSachDialog.getDialogPane().lookupButton(xacNhanButtonType);
                    xacNhanButton.setDisable(false);
                    tenSachTF.textProperty().addListener((observable, oldValue, newValue) -> {
                        if(!newValue.trim().isEmpty() &&
                                !tacGiaTF.getText().trim().isEmpty() &&
                                !nhaXBTF.getText().trim().isEmpty() &&
                                !namXBTF.getText().trim().isEmpty()){
                            xacNhanButton.setDisable(false);
                        }else{
                            xacNhanButton.setDisable(true);
                        }
                    });
                    tacGiaTF.textProperty().addListener((observable, oldValue, newValue) -> {
                        if(!newValue.trim().isEmpty() &&
                                !tenSachTF.getText().trim().isEmpty() &&
                                !nhaXBTF.getText().trim().isEmpty() &&
                                !namXBTF.getText().trim().isEmpty()){
                            xacNhanButton.setDisable(false);
                        }else{
                            xacNhanButton.setDisable(true);
                        }
                    });
                    nhaXBTF.textProperty().addListener((observable, oldValue, newValue) -> {
                        if(!newValue.trim().isEmpty() &&
                                !tacGiaTF.getText().trim().isEmpty() &&
                                !tenSachTF.getText().trim().isEmpty() &&
                                !namXBTF.getText().trim().isEmpty()){
                            xacNhanButton.setDisable(false);
                        }else{
                            xacNhanButton.setDisable(true);
                        }
                    });
                    namXBTF.textProperty().addListener((observable, oldValue, newValue) -> {
                        if(!newValue.trim().isEmpty() &&
                                !tacGiaTF.getText().trim().isEmpty() &&
                                !nhaXBTF.getText().trim().isEmpty() &&
                                !tenSachTF.getText().trim().isEmpty()){
                            xacNhanButton.setDisable(false);
                        }else{
                            xacNhanButton.setDisable(true);
                        }
                    });

                    TextField gioiThieuTF = new TextField();
                    gioiThieuTF.setPromptText("Giới thiệu");
                    gioiThieuTF.setText(rowData.getGioiThieu());

                    gridPane.add(new Label("Tên sách "), 0 , 0);
                    gridPane.add(tenSachTF, 1, 0);
                    gridPane.add(new Label("Tác giả "), 0, 1);
                    gridPane.add(tacGiaTF, 1,1);
                    gridPane.add(new Label("Nhà xuất bản "), 0,2);
                    gridPane.add(nhaXBTF, 1,2);
                    gridPane.add(new Label("Năm xuất bản "),0, 3);
                    gridPane.add(namXBTF, 1,3);
                    gridPane.add(new Label("Đơn giá "), 0, 4);
                    gridPane.add(donGiaTF, 1, 4);
                    gridPane.add(new Label("Giới thiệu"), 0, 5);
                    gridPane.add(gioiThieuTF, 1,5);

                    suaSachDialog.getDialogPane().setContent(gridPane);
                    Optional<ButtonType> result = suaSachDialog.showAndWait();
                    if(result.get() == xacNhanButtonType){
                        String tenSach = tenSachTF.getText();
                        String tacGia = tacGiaTF.getText();
                        String nhaXB = nhaXBTF.getText();
                        int namXB = Integer.parseInt(namXBTF.getText());
                        double donGia = 0;
                        if(!donGiaTF.getText().trim().isEmpty()){
                            donGia = Double.parseDouble(donGiaTF.getText().trim());
                        }
                        String gioiThieu = gioiThieuTF.getText();
                        Sach sach = new Sach(rowData.getId(), tenSach, tacGia, nhaXB, namXB, donGia, gioiThieu);
                        try {
                            sachDAO.updateSach(sach);
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Thông báo");
                            alert.setHeaderText("Cập nhật thông tin cuốn sách có ID "+rowData.getId()+" thành công");
                            String content = "Tên sách: "+tenSach+"\n"+
                                    "Tác giả: "+tacGia+"\n"+
                                    "Nhà xuất bản: "+nhaXB+"\n"+
                                    "Năm xuất bản: "+namXB+"\n"+
                                    "Đơn giá: "+donGia+"\n"+
                                    "Giới thiệu: "+gioiThieu+"\n";;
                            alert.setContentText(content);
                            alert.show();
                            loadData();
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Lỗi");
                            alert.setHeaderText("Đã có lỗi xảy ra");
                            alert.setContentText("Cập nhật thông tin sách thất bại");
                            alert.show();
                        }
                    }
                }
            });
            return row;
        });
    }

    private void initColumns(){
        idColumn.setCellValueFactory(new PropertyValueFactory<Sach, Integer>("id"));
        tenColumn.setCellValueFactory(new PropertyValueFactory<Sach, String>("tenSach"));
        tacGiaColumn.setCellValueFactory(new PropertyValueFactory<Sach, String>("tacGia"));
        nhaXBColumn.setCellValueFactory(new PropertyValueFactory<Sach, String>("nhaXB"));
        namXBColumn.setCellValueFactory(new PropertyValueFactory<Sach, String>("namXB"));
        donGiaColumn.setCellValueFactory(new PropertyValueFactory<Sach, Double>("donGia"));
        gioiThieuColumn.setCellValueFactory(new PropertyValueFactory<Sach, String>("gioiThieu"));
        Callback<TableColumn<Sach, Void>, TableCell<Sach, Void>> cellFactory = new Callback<TableColumn<Sach, Void>, TableCell<Sach, Void>>() {
            @Override
            public TableCell<Sach, Void> call(final TableColumn<Sach, Void> param) {
                final TableCell<Sach, Void> cell = new TableCell<Sach, Void>() {
                    private final Button deleteButton = new Button("Xóa");
                    {
                        deleteButton.setOnAction((ActionEvent e)->{
                            Sach sach = getTableView().getItems().get(getIndex());

                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setTitle("Xóa cuốn sách");
                            alert.setHeaderText("Xác nhận xóa");
                            alert.setContentText("Bạn có chắc xóa "+sach.getTenSach()+" khỏi thư viện không?");
                            ButtonType xacNhanButtonType = new ButtonType("Xác nhận", ButtonBar.ButtonData.OK_DONE);
                            ButtonType thoatButtonType = new ButtonType("Thoát", ButtonBar.ButtonData.CANCEL_CLOSE);
                            alert.getButtonTypes().clear();
                            alert.getButtonTypes().addAll(xacNhanButtonType, thoatButtonType);
                            Optional<ButtonType> result = alert.showAndWait();
                            if(result.get() == xacNhanButtonType) {
                                try {
                                    sachDAO.deleteSach(sach.getId());
                                    loadData();
                                } catch (SQLException throwables) {
                                    throwables.printStackTrace();
                                    Alert alert1 = new Alert(Alert.AlertType.ERROR);
                                    alert1.setTitle("Lỗi");
                                    alert1.setHeaderText("Có lỗi xảy ra khi xóa cuốn sách");
                                    alert1.setContentText("Không thể xóa cuốn sách");
                                    alert1.show();
                                }
                            }
                        });
                        deleteButton.setMaxSize(1000,1000);
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(deleteButton);
                        }
                    }
                };
                return cell;
            }
        };

        buttonsColumn.setCellFactory(cellFactory);
        buttonsColumn.setSortable(false);
    }

    private void loadData(){
        try{
            sachList = FXCollections.observableArrayList(sachDAO.findAll());
        }catch (SQLException e){
            e.printStackTrace();
        }
        table.setItems(sachList);
    }

    private void initThemSach(){
        namXuatBanTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            try{
                if(!newValue.isEmpty()){
                    Integer.parseInt(newValue);
                }
            }catch (NumberFormatException ex){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Năm xuất bản không hợp lệ");
                alert.setTitle("LỖI");
                alert.setHeaderText("Đã có lỗi xảy ra!!!");
                alert.show();
                namXuatBanTextField.setText("");
            }
        });
        donGiaTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            try{
                if(!newValue.isEmpty()){
                    Integer.parseInt(newValue);
                }
            }catch (NumberFormatException ex){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Đơn giá không hợp lệ");
                alert.setTitle("LỖI");
                alert.setHeaderText("Đã có lỗi xảy ra!!!");
                alert.show();
                donGiaTextField.setText("");
            }
        });
        themSachButton.setDisable(true);
        tenSachTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.trim().isEmpty() &&
                    !tacGiaTextField.getText().trim().isEmpty() &&
                    !nhaXuatBanTextField.getText().trim().isEmpty() &&
                    !namXuatBanTextField.getText().trim().isEmpty()){
                themSachButton.setDisable(false);
            }else{
                themSachButton.setDisable(true);
            }
        });
        tacGiaTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.trim().isEmpty() &&
                    !tenSachTextField.getText().trim().isEmpty() &&
                    !nhaXuatBanTextField.getText().trim().isEmpty() &&
                    !namXuatBanTextField.getText().trim().isEmpty()){
                themSachButton.setDisable(false);
            }else{
                themSachButton.setDisable(true);
            }
        });
        nhaXuatBanTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.trim().isEmpty() &&
                    !tacGiaTextField.getText().trim().isEmpty() &&
                    !tenSachTextField.getText().trim().isEmpty() &&
                    !namXuatBanTextField.getText().trim().isEmpty()){
                themSachButton.setDisable(false);
            }else{
                themSachButton.setDisable(true);
            }
        });
        namXuatBanTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.trim().isEmpty() &&
                    !tacGiaTextField.getText().trim().isEmpty() &&
                    !nhaXuatBanTextField.getText().trim().isEmpty() &&
                    !tenSachTextField.getText().trim().isEmpty()){
                themSachButton.setDisable(false);
            }else{
                themSachButton.setDisable(true);
            }
        });
    }

    public void themSach(ActionEvent e){
        String tenSach = tenSachTextField.getText();
        String tacGia = tacGiaTextField.getText();
        String nhaXuatBan = nhaXuatBanTextField.getText();
        int namXuatBan = Integer.parseInt(namXuatBanTextField.getText());
        double donGia = 0;
        if(!donGiaTextField.getText().trim().isEmpty()){
            donGia = Double.parseDouble(donGiaTextField.getText());
        }
        String gioiThieu = gioiThieuTextField.getText();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        String content = "Bạn có muốn thêm cuốn sách sau không? \n"+
                "Tên sách: "+tenSach+"\n"+
                "Tác giả: "+tacGia+"\n"+
                "Nhà xuất bản: "+nhaXuatBan+"\n"+
                "Năm xuất bản: "+namXuatBan+"\n"+
                "Đơn giá: "+donGia+"\n"+
                "Giới thiệu: "+gioiThieu+"\n";
        alert.setContentText(content);
        alert.setTitle("Xác nhận");
        alert.setHeaderText("Xác nhận thao tác");
        ButtonType xacNhanButtonType = new ButtonType("Xác nhận", ButtonBar.ButtonData.OK_DONE);
        ButtonType thoatButtonType = new ButtonType("Thoát", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().clear();
        alert.getButtonTypes().addAll(xacNhanButtonType, thoatButtonType);
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == xacNhanButtonType){
            try {
                Sach sach = new Sach(tenSach, tacGia, nhaXuatBan, namXuatBan, donGia, gioiThieu);
                sachDAO.insertSach(sach);
                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert1.setTitle("Thông báo");
                alert1.setHeaderText("Thêm sách thành công");
                String content1 = "Tên sách: "+tenSach+"\n"+
                        "Tác giả: "+tacGia+"\n"+
                        "Nhà xuất bản: "+nhaXuatBan+"\n"+
                        "Năm xuất bản: "+namXuatBan+"\n"+
                        "Đơn giá: "+donGia+"\n"+
                        "Giới thiệu: "+gioiThieu+"\n";
                alert1.setContentText(content1);
                alert1.show();
                loadData();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                Alert alert1 = new Alert(Alert.AlertType.ERROR);
                alert1.setTitle("Lỗi");
                alert1.setHeaderText("Đã có lỗi xảy ra");
                alert1.setContentText("Đã có lỗi xảy ra khi thêm sách");
                alert1.show();
            }
        }
    }

    public void setComboBox(ActionEvent e){
        String promptText = "Nhập "+ comboBox.getValue().toLowerCase();
        timKiemTextField.setPromptText(promptText);
        if(comboBox.getValue().equalsIgnoreCase("tên sách")){
            methodSearch = 1;
        }else if(comboBox.getValue().equalsIgnoreCase("tác giả")){
            methodSearch = 2;
        }else if(comboBox.getValue().equalsIgnoreCase("nhà xuất bản")){
            methodSearch = 3;
        }else if(comboBox.getValue().equalsIgnoreCase("năm xuất bản")){
            methodSearch = 4;
        }
        System.out.println(methodSearch);
    }

    public void timKiem(ActionEvent e){
        String noiDung = timKiemTextField.getText();
        try{
            if(methodSearch == 1){
                sachList = FXCollections.observableArrayList(sachDAO.findByName(noiDung));
            }else if(methodSearch == 2){
                sachList = FXCollections.observableArrayList(sachDAO.findByTacGia(noiDung));
            }else if(methodSearch == 3){
                sachList = FXCollections.observableArrayList(sachDAO.findByNhaXB(noiDung));
            }else if(methodSearch == 4){
                sachList = FXCollections.observableArrayList(sachDAO.findByNamXB(Integer.parseInt(noiDung)));
            }

        }catch (SQLException ex){
            ex.printStackTrace();
        }catch (NumberFormatException ex){
        }
        table.setItems(sachList);
    }

    public void tatCaSach(ActionEvent e){
        try{
            sachList = FXCollections.observableArrayList(sachDAO.findAll());
        }catch (SQLException ex){
            ex.printStackTrace();
        }
        table.setItems(sachList);
    }

    public void muonSach(ActionEvent e){
        Stage stage = (Stage)(((Node) e.getSource()).getScene().getWindow());
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("muonsach\\sample.fxml"));
        try {
            Parent root = loader.load();
            Scene scene = new Scene(root,stage.getWidth(),stage.getHeight());
            stage.setScene(scene);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void docGia(ActionEvent e){
        Stage stage = (Stage)(((Node) e.getSource()).getScene().getWindow());
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("docgia\\sample.fxml"));
        try {
            Parent root = loader.load();
            Scene scene = new Scene(root,stage.getWidth(),stage.getHeight());
            stage.setScene(scene);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void thuThu(ActionEvent e){
        Stage stage = (Stage)(((Node) e.getSource()).getScene().getWindow());
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("thuthu\\sample.fxml"));
        try {
            Parent root = loader.load();
            Scene scene = new Scene(root,stage.getWidth(),stage.getHeight());
            stage.setScene(scene);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void danhSachMuonTra(ActionEvent e){
        Stage stage = (Stage)(((Node) e.getSource()).getScene().getWindow());
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("danhsachmuontra\\sample.fxml"));
        try {
            Parent root = loader.load();
            Scene scene = new Scene(root,stage.getWidth(),stage.getHeight());
            stage.setScene(scene);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void traSach(ActionEvent e){
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Trả sách");
        dialog.setHeaderText("Nhập mã mượn trả");

        ButtonType xacNhanButtonType = new ButtonType("Xác nhận", ButtonBar.ButtonData.OK_DONE);
        ButtonType thoatButtonType = new ButtonType("Thoát", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(xacNhanButtonType, thoatButtonType);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20,150,10,10));

        TextField idMT = new TextField();
        idMT.setPromptText("Mã mượn trả");

        grid.add(new Label("Mã mượn trả"), 0 ,0);
        grid.add(idMT, 1,0);

        Node xacNhanButton = dialog.getDialogPane().lookupButton(xacNhanButtonType);
        xacNhanButton.setDisable(true);
        idMT.textProperty().addListener((observable, oldValue, newValue) -> {
            xacNhanButton.setDisable(newValue.trim().isEmpty());
        });

        dialog.getDialogPane().setContent(grid);
        Optional<ButtonType> result = dialog.showAndWait();
        if(result.get() == xacNhanButtonType){
            int id;
            try{
                id = Integer.parseInt(idMT.getText());
                MuonTra muonTra = muonTraDAO.findById(id);
                System.out.println(muonTra);
                Stage stage = (Stage)(((Node) e.getSource()).getScene().getWindow());
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("trasach\\sample.fxml"));
                try {
                    Parent root = loader.load();
                    app.trasach.Controller traSachController = loader.getController();
                    traSachController.constructor(muonTra);
                    Scene scene = new Scene(root,stage.getWidth(),stage.getHeight());
                    stage.setScene(scene);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }catch (NumberFormatException ex){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Mã mượn trả không hợp lệ");
                alert.setTitle("LỖI");
                alert.setHeaderText("Đã có lỗi xảy ra!!!");
                alert.show();
            }catch (SQLException ex){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Mã mượn trả không chính xác");
                alert.setTitle("LỖI");
                alert.setHeaderText("Đã có lỗi xảy ra!!!");
                alert.show();
            }
        }
    }

    public void chooseDirectory(ActionEvent e){
        Stage stage = (Stage)(((Node) e.getSource()).getScene().getWindow());
        File dir = directoryChooser.showDialog(stage);
        if(dir != null){
            Calendar calendar = Calendar.getInstance();
            //Returns current time in millis
            String fileName = "Sach"+ calendar.getTimeInMillis()+".csv";
            String filePath = dir.getAbsolutePath() +"\\"+fileName;
            System.out.println(filePath);
            File file = new File(filePath);
            try {
                FileOutputStream fos = new FileOutputStream(file);
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
                CSVWriter writer = new CSVWriter(outputStreamWriter);
                List<String[]> data = new ArrayList<String[]>();
                data.add(Sach.getHeader());
                for(int i = 0; i < sachList.size(); i++){
                    data.add(sachList.get(i).getPrint());
                }
                writer.writeAll(data);
                writer.close();
                outputStreamWriter.close();
                fos.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
}
