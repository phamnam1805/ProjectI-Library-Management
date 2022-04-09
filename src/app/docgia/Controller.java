package app.docgia;

import DAO.DocGiaDAO;
import app.Main;
import com.opencsv.CSVWriter;
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
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import model.DocGia;
import model.Sach;

import javax.print.Doc;
import javax.xml.soap.Text;
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

    DirectoryChooser directoryChooser;

    @FXML
    private TableView<DocGia> table;

    @FXML
    private TableColumn<DocGia, Integer> idDocGiaColumn;

    @FXML
    private TableColumn<DocGia, String> tenDocGiaColumn;

    @FXML
    private TableColumn<DocGia, String> gioiTinhColumn;

    @FXML
    private TableColumn<DocGia, String> diaChiColumn;

    @FXML
    private TableColumn<DocGia, Date> ngaySinhColumn;

    @FXML
    private TableColumn<DocGia, String> CMNDColumn;

    @FXML
    private TableColumn<DocGia, String> emailColumn;

    @FXML
    private TableColumn<DocGia, String> sdtColumn;

    @FXML
    private TableColumn<DocGia, Void> buttonsColumn;

    private ObservableList<DocGia> docGiaObservableList;

    @FXML
    private TextField tenDocGiaTextField;

    @FXML
    private ComboBox gioiTinhComboBox;

    private ObservableList<String> gioiTinhObservableList;

    @FXML
    private TextField diaChiTextField;

    @FXML
    private DatePicker ngaySinhDatePicker;

    @FXML
    private TextField cmndTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField sdtTextField;

    @FXML
    private Button themDocGiaButton;

    StringConverter<LocalDate> converter;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        directoryChooser = new DirectoryChooser();
        initDAOs();
        initTable();
        formatDate();
        initThemDocGia();
    }

    private void initThemDocGia(){
        ngaySinhDatePicker.setConverter(converter);
        gioiTinhObservableList = FXCollections.observableArrayList("Nam", "Nữ", "Khác");
        gioiTinhComboBox.setItems(gioiTinhObservableList);
        gioiTinhComboBox.setValue("Chọn giới tính");

        themDocGiaButton.setDisable(true);
        tenDocGiaTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.trim().isEmpty() && !cmndTextField.getText().trim().isEmpty()){
                themDocGiaButton.setDisable(false);
            }else {
                themDocGiaButton.setDisable(true);
            }
        });

        cmndTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.trim().isEmpty() && !tenDocGiaTextField.getText().trim().isEmpty()){
                themDocGiaButton.setDisable(false);
            }else {
                themDocGiaButton.setDisable(true);
            }
        });
    }

    public void themDocGia(ActionEvent e){
        String tenDocGia = tenDocGiaTextField.getText();
        int gioiTinh = 2;
        String gt = "";
        if(gioiTinhComboBox.getValue().toString().equalsIgnoreCase("Nam")){
            gioiTinh  = 1;
            gt = "Nam";
        }else if(gioiTinhComboBox.getValue().toString().equalsIgnoreCase("Nữ")){
            gioiTinh = 0;
            gt = "Nữ";
        }
        String diaChi = diaChiTextField.getText();
        LocalDate ngaySinh = ngaySinhDatePicker.getValue();
        Date ns = null;
        if(ngaySinh != null){
            ns = Date.valueOf(ngaySinh);
        }
        String cmnd = cmndTextField.getText();
        String email = emailTextField.getText();
        String dienThoai = sdtTextField.getText();

        DocGia docGia = new DocGia(tenDocGia, gioiTinh, diaChi, ns, cmnd, email, dienThoai);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Thêm độc giả");
        alert.setHeaderText("Xác nhận thêm độc giả");

        String content = "Bạn có muốn thêm độc giả sau không? \n"+
                "Tên độc giả: "+tenDocGia+"\n"+
                "Giới tính: "+gt+"\n"+
                "Địa chỉ: "+diaChi+"\n"+
                "Ngày sinh: "+(ngaySinh == null ? "": ngaySinh)+"\n"+
                "CMND: "+cmnd+"\n"+
                "Email: "+email+"\n"+
                "Điện thoại: "+dienThoai+"\n";
        alert.setContentText(content);
        ButtonType xacNhanButtonType = new ButtonType("Xác nhận", ButtonBar.ButtonData.OK_DONE);
        ButtonType thoatButtonType = new ButtonType("Thoát", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().clear();
        alert.getButtonTypes().addAll(xacNhanButtonType, thoatButtonType);
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == xacNhanButtonType){
            try {
                docGiaDAO.insertDG(docGia);
                loadData();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                Alert alert1 = new Alert(Alert.AlertType.ERROR);
                alert1.setTitle("Lỗi");
                alert1.setHeaderText("Đã có lỗi xảy ra");
                alert1.setContentText("Đã có lỗi xảy ra khi thêm độc giả");
                alert1.show();
            }
        }
    }

    private void formatDate(){
        String pattern = "dd-MM-yyyy";
        converter = new StringConverter<LocalDate>() {
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
    }

    private void initDAOs(){
        docGiaDAO = new DocGiaDAO();
    }

    private void initTable(){
        initColumns();
        loadData();
        table.setRowFactory( RowFactory -> {
            TableRow<DocGia> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    DocGia rowData = row.getItem();
                    Dialog<ButtonType> suaDocGiaDialog = new Dialog<>();
                    suaDocGiaDialog.setTitle("Sửa thông tin");
                    suaDocGiaDialog.setHeaderText("Sửa thông tin của độc giả có id: " + rowData.getId());

                    ButtonType xacNhanButtonType = new ButtonType("Xác nhận", ButtonBar.ButtonData.OK_DONE);
                    ButtonType thoatButtonType = new ButtonType("Thoát", ButtonBar.ButtonData.CANCEL_CLOSE);
                    suaDocGiaDialog.getDialogPane().getButtonTypes().addAll(xacNhanButtonType, thoatButtonType);

                    GridPane gridPane = new GridPane();
                    gridPane.setHgap(10);
                    gridPane.setVgap(10);
                    gridPane.setPadding(new Insets(20, 150, 10, 10));

                    TextField tenDocGiaTF = new TextField();
                    tenDocGiaTF.setPromptText("Tên độc giả");
                    tenDocGiaTF.setText(rowData.getTenDG());


                    ComboBox gioiTinhCB = new ComboBox();
                    gioiTinhCB.setItems(gioiTinhObservableList);
                    if(rowData.getGioiTinh() == 2){
                        gioiTinhCB.setValue("Khác");
                    }else if(rowData.getGioiTinh() == 1){
                        gioiTinhCB.setValue("Nam");
                    }else{
                        gioiTinhCB.setValue("Nữ");
                    }

                    TextField diaChiTF = new TextField();
                    diaChiTF.setPromptText("Địa chỉ");
                    diaChiTF.setText(rowData.getDiaChi());

                    DatePicker ngaySinhDP = new DatePicker();
                    ngaySinhDP.setConverter(converter);
                    ngaySinhDP.setEditable(false);
                    if(rowData.getNgaySinh() != null){
                        ngaySinhDP.setValue(LocalDate.parse(rowData.getNgaySinh().toString()));
                    }
                    TextField cmndTF = new TextField();
                    cmndTF.setPromptText("Số chứng minh nhân dân");
                    cmndTF.setText(rowData.getCMND());

                    TextField emailTF = new TextField();
                    emailTF.setPromptText("Email");
                    emailTF.setText(rowData.getEmail());

                    TextField sdtTF = new TextField();
                    sdtTF.setPromptText("Số điện thoại");
                    sdtTF.setText(rowData.getDienThoai());

                    Node xacNhanButton = suaDocGiaDialog.getDialogPane().lookupButton(xacNhanButtonType);
                    xacNhanButton.setDisable(false);

                    tenDocGiaTF.textProperty().addListener((observable, oldValue, newValue) -> {
                        if(!newValue.trim().isEmpty() && !cmndTF.getText().trim().isEmpty()){
                            xacNhanButton.setDisable(false);
                        }else {
                            xacNhanButton.setDisable(true);
                        }
                    });

                    cmndTF.textProperty().addListener((observable, oldValue, newValue) -> {
                        if(!newValue.trim().isEmpty() && !tenDocGiaTF.getText().trim().isEmpty()){
                            xacNhanButton.setDisable(false);
                        }else {
                            xacNhanButton.setDisable(true);
                        }
                    });

                    gridPane.add(new Label("Tên độc giả "), 0, 0);
                    gridPane.add(tenDocGiaTF, 1, 0);
                    gridPane.add(new Label("Giới tính "), 0, 1);
                    gridPane.add(gioiTinhCB, 1, 1);
                    gridPane.add(new Label("Địa chỉ "), 0, 2);
                    gridPane.add(diaChiTF, 1, 2);
                    gridPane.add(new Label("Ngày sinh "), 0, 3);
                    gridPane.add(ngaySinhDP, 1, 3);
                    gridPane.add(new Label("CMND "), 0, 4);
                    gridPane.add(cmndTF, 1, 4);
                    gridPane.add(new Label("Email"), 0, 5);
                    gridPane.add(emailTF, 1, 5);
                    gridPane.add(new Label("Số điện thoại "), 0, 6);
                    gridPane.add(sdtTF, 1, 6);

                    suaDocGiaDialog.getDialogPane().setContent(gridPane);
                    Optional<ButtonType> result = suaDocGiaDialog.showAndWait();
                    if(result.get() == xacNhanButtonType){
                        String tenDocGia = tenDocGiaTF.getText();
                        int gioiTinh = 2;
                        String gt = "";
                        if(gioiTinhCB.getValue().toString().equalsIgnoreCase("Nam")){
                            gioiTinh  = 1;
                            gt = "Nam";
                        }else if(gioiTinhCB.getValue().toString().equalsIgnoreCase("Nữ")){
                            gioiTinh = 0;
                            gt = "Nữ";
                        }
                        String diaChi = diaChiTF.getText();
                        LocalDate ngaySinh = ngaySinhDP.getValue();
                        Date ns = null;
                        if(ngaySinh != null){
                            ns = Date.valueOf(ngaySinh);
                        }
                        String cmnd = cmndTF.getText();
                        String email = emailTF.getText();
                        String dienThoai = sdtTF.getText();

                        DocGia docGia = new DocGia( rowData.getId() ,tenDocGia, gioiTinh, diaChi, ns, cmnd, email, dienThoai);

                        try {
                            docGiaDAO.updateDG(docGia);
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Thông tin");
                            alert.setHeaderText("Sửa thông tin độc giả thành công");
                            String content = "ID: "+rowData.getId()+"\n"+
                                    "Tên độc giả: "+tenDocGia+"\n"+
                                    "Giới tính: "+gt+"\n"+
                                    "Địa chỉ: "+diaChi+"\n"+
                                    "Ngày sinh: "+ngaySinh+"\n"+
                                    "CMND: "+cmnd+"\n"+
                                    "Email: "+email+"\n"+
                                    "Điện thoại: "+dienThoai+"\n";
                            alert.setContentText(content);
                            alert.show();
                            loadData();
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Lỗi");
                            alert.setHeaderText("Đã có lỗi xảy ra");
                            alert.setContentText("Cập nhật thông tin độc giả thất bại");
                            alert.show();
                        }


                    }

                }
            });
            return row;
        });
    }

    private void initColumns() {
        idDocGiaColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        tenDocGiaColumn.setCellValueFactory(new PropertyValueFactory<>("tenDG"));
        gioiTinhColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<DocGia, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<DocGia, String> param) {
                DocGia docGia = param.getValue();
                int gioiTinh = docGia.getGioiTinh();
                String content = "";
                if(gioiTinh == 2){
                    content = "Khác";
                }else if(gioiTinh == 1){
                    content = "Nam";
                }else{
                    content = "Nữ";
                }
                return new ReadOnlyObjectWrapper<>(content);
            }
        });
        diaChiColumn.setCellValueFactory(new PropertyValueFactory<>("diaChi"));
        ngaySinhColumn.setCellValueFactory(new PropertyValueFactory<>("ngaySinh"));
        CMNDColumn.setCellValueFactory(new PropertyValueFactory<>("CMND"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        sdtColumn.setCellValueFactory(new PropertyValueFactory<>("dienThoai"));

        Callback<TableColumn<DocGia, Void>, TableCell<DocGia, Void>> cellFactory = new Callback<TableColumn<DocGia, Void>, TableCell<DocGia, Void>>() {
            @Override
            public TableCell<DocGia, Void> call(final TableColumn<DocGia, Void> param) {
                final TableCell<DocGia, Void> cell = new TableCell<DocGia, Void>() {
                    private final Button deleteButton = new Button("Xóa");

                    {
                        deleteButton.setOnAction((ActionEvent e)->{
                            DocGia data = getTableView().getItems().get(getIndex());
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setTitle("Xóa độc giả");
                            alert.setHeaderText("Xác nhận xóa");
                            alert.setContentText("Bạn có chắc xóa độc giả "+data.getTenDG()+" có ID là "+data.getId()+" khỏi thư viện không?");
                            ButtonType xacNhanButtonType = new ButtonType("Xác nhận", ButtonBar.ButtonData.OK_DONE);
                            ButtonType thoatButtonType = new ButtonType("Thoát", ButtonBar.ButtonData.CANCEL_CLOSE);
                            alert.getButtonTypes().clear();
                            alert.getButtonTypes().addAll(xacNhanButtonType, thoatButtonType);
                            Optional<ButtonType> result = alert.showAndWait();
                            if(result.get() == xacNhanButtonType){
                                try {
                                    docGiaDAO.deleteDG(data.getId());
                                    loadData();
                                } catch (SQLException throwables) {
                                    throwables.printStackTrace();
                                    Alert alert1 = new Alert(Alert.AlertType.ERROR);
                                    alert1.setTitle("Lỗi");
                                    alert1.setHeaderText("Có lỗi xảy ra khi xóa độc giả");
                                    alert1.setContentText("Không thể xóa độc giả");
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
        try {
            docGiaObservableList = FXCollections.observableArrayList(docGiaDAO.findAll());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        table.setItems(docGiaObservableList);
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

    public void chooseDirectory(ActionEvent e){
        Stage stage = (Stage)(((Node) e.getSource()).getScene().getWindow());
        File dir = directoryChooser.showDialog(stage);
        if(dir != null){
            Calendar calendar = Calendar.getInstance();
            //Returns current time in millis
            String fileName = "DocGia"+ calendar.getTimeInMillis()+".csv";
            String filePath = dir.getAbsolutePath() +"\\"+fileName;
            System.out.println(filePath);
            File file = new File(filePath);
            try {
                FileOutputStream fos = new FileOutputStream(file);
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
                CSVWriter writer = new CSVWriter(outputStreamWriter);
                List<String[]> data = new ArrayList<String[]>();
                data.add(DocGia.getHeader());
                for(int i = 0; i < docGiaObservableList.size(); i++){
                    data.add(docGiaObservableList.get(i).getPrint());
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
