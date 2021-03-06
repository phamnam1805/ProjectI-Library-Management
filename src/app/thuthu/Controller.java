package app.thuthu;

import DAO.ThuThuDAO;
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
import model.ThuThu;

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

    DirectoryChooser directoryChooser;

    ThuThuDAO thuThuDAO;

    @FXML
    private TableView<ThuThu> table;

    @FXML
    private TableColumn<ThuThu, Integer> idThuThuColumn;

    @FXML
    private TableColumn<ThuThu, String> tenThuThuColumn;

    @FXML
    private TableColumn<ThuThu, String> gioiTinhColumn;

    @FXML
    private TableColumn<ThuThu, Date> ngaySinhColumn;

    @FXML
    private TableColumn<ThuThu, String> CMNDColumn;

    @FXML
    private TableColumn<ThuThu, String> emailColumn;

    @FXML
    private TableColumn<ThuThu, String> sdtColumn;

    @FXML
    private TableColumn<ThuThu, Void> buttonsColumn;

    private ObservableList<ThuThu> thuThuObservableList;

    @FXML
    private TextField tenThuThuTextField;

    @FXML
    private ComboBox gioiTinhComboBox;

    private ObservableList<String> gioiTinhObservableList;

    @FXML
    private DatePicker ngaySinhDatePicker;

    @FXML
    private TextField cmndTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField sdtTextField;

    @FXML
    private Button themThuThuButton;

    StringConverter<LocalDate> converter;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        directoryChooser = new DirectoryChooser();
        initDAOs();
        initTable();
        formatDate();
        initThemThuThu();
    }

    private void initThemThuThu(){
        ngaySinhDatePicker.setConverter(converter);
        gioiTinhObservableList = FXCollections.observableArrayList("Nam", "N???", "Kh??c");
        gioiTinhComboBox.setItems(gioiTinhObservableList);
        gioiTinhComboBox.setValue("Ch???n gi???i t??nh");

        themThuThuButton.setDisable(true);
        tenThuThuTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.trim().isEmpty() && !cmndTextField.getText().trim().isEmpty()){
                themThuThuButton.setDisable(false);
            }else {
                themThuThuButton.setDisable(true);
            }
        });

        cmndTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.trim().isEmpty() && !tenThuThuTextField.getText().trim().isEmpty()){
                themThuThuButton.setDisable(false);
            }else {
                themThuThuButton.setDisable(true);
            }
        });
    }

    public void themThuThu(ActionEvent e){
        String tenThuThu = tenThuThuTextField.getText();
        int gioiTinh = 2;
        String gt = "";
        if(gioiTinhComboBox.getValue().toString().equalsIgnoreCase("Nam")){
            gioiTinh  = 1;
            gt = "Nam";
        }else if(gioiTinhComboBox.getValue().toString().equalsIgnoreCase("N???")){
            gioiTinh = 0;
            gt = "N???";
        }
        LocalDate ngaySinh = ngaySinhDatePicker.getValue();
        Date ns = null;
        if(ngaySinh != null){
            ns = Date.valueOf(ngaySinh);
        }
        String cmnd = cmndTextField.getText();
        String email = emailTextField.getText();
        String dienThoai = sdtTextField.getText();

        ThuThu ThuThu = new ThuThu(tenThuThu, gioiTinh,  ns, cmnd, email, dienThoai);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Th??m th??? th??");
        alert.setHeaderText("X??c nh???n th??m th??? th??");

        String content = "B???n c?? mu???n th??m th??? th?? sau kh??ng? \n"+
                "T??n th??? th??: "+tenThuThu+"\n"+
                "Gi???i t??nh: "+gt+"\n"+
                "Ng??y sinh: "+(ngaySinh == null ? "": ngaySinh)+"\n"+
                "CMND: "+cmnd+"\n"+
                "Email: "+email+"\n"+
                "??i???n tho???i: "+dienThoai+"\n";
        alert.setContentText(content);
        ButtonType xacNhanButtonType = new ButtonType("X??c nh???n", ButtonBar.ButtonData.OK_DONE);
        ButtonType thoatButtonType = new ButtonType("Tho??t", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().clear();
        alert.getButtonTypes().addAll(xacNhanButtonType, thoatButtonType);
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == xacNhanButtonType){
            try {
                thuThuDAO.insertTT(ThuThu);
                loadData();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                Alert alert1 = new Alert(Alert.AlertType.ERROR);
                alert1.setTitle("L???i");
                alert1.setHeaderText("???? c?? l???i x???y ra");
                alert1.setContentText("???? c?? l???i x???y ra khi th??m th??? th??");
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
        thuThuDAO = new ThuThuDAO();
    }

    private void initTable(){
        initColumns();
        loadData();
        table.setRowFactory( RowFactory -> {
            TableRow<ThuThu> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    ThuThu rowData = row.getItem();
                    Dialog<ButtonType> suaThuThuDialog = new Dialog<>();
                    suaThuThuDialog.setTitle("S???a th??ng tin");
                    suaThuThuDialog.setHeaderText("S???a th??ng tin c???a th??? th?? c?? id: " + rowData.getId());

                    ButtonType xacNhanButtonType = new ButtonType("X??c nh???n", ButtonBar.ButtonData.OK_DONE);
                    ButtonType thoatButtonType = new ButtonType("Tho??t", ButtonBar.ButtonData.CANCEL_CLOSE);
                    suaThuThuDialog.getDialogPane().getButtonTypes().addAll(xacNhanButtonType, thoatButtonType);

                    GridPane gridPane = new GridPane();
                    gridPane.setHgap(10);
                    gridPane.setVgap(10);
                    gridPane.setPadding(new Insets(20, 150, 10, 10));

                    TextField tenThuThuTF = new TextField();
                    tenThuThuTF.setPromptText("T??n th??? th??");
                    tenThuThuTF.setText(rowData.getTenTT());


                    ComboBox gioiTinhCB = new ComboBox();
                    gioiTinhCB.setItems(gioiTinhObservableList);
                    if(rowData.getGioiTinh() == 2){
                        gioiTinhCB.setValue("Kh??c");
                    }else if(rowData.getGioiTinh() == 1){
                        gioiTinhCB.setValue("Nam");
                    }else{
                        gioiTinhCB.setValue("N???");
                    }

                    DatePicker ngaySinhDP = new DatePicker();
                    ngaySinhDP.setConverter(converter);
                    ngaySinhDP.setEditable(false);
                    if(rowData.getNgaySinh() != null){
                        ngaySinhDP.setValue(LocalDate.parse(rowData.getNgaySinh().toString()));
                    }
                    TextField cmndTF = new TextField();
                    cmndTF.setPromptText("S??? ch???ng minh nh??n d??n");
                    cmndTF.setText(rowData.getCMND());

                    TextField emailTF = new TextField();
                    emailTF.setPromptText("Email");
                    emailTF.setText(rowData.getEmail());

                    TextField sdtTF = new TextField();
                    sdtTF.setPromptText("S??? ??i???n tho???i");
                    sdtTF.setText(rowData.getDienThoai());

                    Node xacNhanButton = suaThuThuDialog.getDialogPane().lookupButton(xacNhanButtonType);
                    xacNhanButton.setDisable(false);

                    tenThuThuTF.textProperty().addListener((observable, oldValue, newValue) -> {
                        if(!newValue.trim().isEmpty() && !cmndTF.getText().trim().isEmpty()){
                            xacNhanButton.setDisable(false);
                        }else {
                            xacNhanButton.setDisable(true);
                        }
                    });

                    cmndTF.textProperty().addListener((observable, oldValue, newValue) -> {
                        if(!newValue.trim().isEmpty() && !tenThuThuTF.getText().trim().isEmpty()){
                            xacNhanButton.setDisable(false);
                        }else {
                            xacNhanButton.setDisable(true);
                        }
                    });

                    gridPane.add(new Label("T??n th??? th?? "), 0, 0);
                    gridPane.add(tenThuThuTF, 1, 0);
                    gridPane.add(new Label("Gi???i t??nh "), 0, 1);
                    gridPane.add(gioiTinhCB, 1, 1);
                    gridPane.add(new Label("Ng??y sinh "), 0, 2);
                    gridPane.add(ngaySinhDP, 1, 2);
                    gridPane.add(new Label("CMND "), 0, 3);
                    gridPane.add(cmndTF, 1, 3);
                    gridPane.add(new Label("Email"), 0, 4);
                    gridPane.add(emailTF, 1, 4);
                    gridPane.add(new Label("S??? ??i???n tho???i "), 0, 5);
                    gridPane.add(sdtTF, 1, 5);

                    suaThuThuDialog.getDialogPane().setContent(gridPane);
                    Optional<ButtonType> result = suaThuThuDialog.showAndWait();
                    if(result.get() == xacNhanButtonType){
                        String tenThuThu = tenThuThuTF.getText();
                        int gioiTinh = 2;
                        String gt = "";
                        if(gioiTinhCB.getValue().toString().equalsIgnoreCase("Nam")){
                            gioiTinh  = 1;
                            gt = "Nam";
                        }else if(gioiTinhCB.getValue().toString().equalsIgnoreCase("N???")){
                            gioiTinh = 0;
                            gt = "N???";
                        }
                        LocalDate ngaySinh = ngaySinhDP.getValue();
                        Date ns = null;
                        if(ngaySinh != null){
                            ns = Date.valueOf(ngaySinh);
                        }
                        String cmnd = cmndTF.getText();
                        String email = emailTF.getText();
                        String dienThoai = sdtTF.getText();

                        ThuThu ThuThu = new ThuThu( rowData.getId() ,tenThuThu, gioiTinh, ns, cmnd, email, dienThoai);

                        try {
                            thuThuDAO.updateTT(ThuThu);
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Th??ng tin");
                            alert.setHeaderText("S???a th??ng tin th??? th?? th??nh c??ng");
                            String content = "ID: "+rowData.getId()+"\n"+
                                    "T??n th??? th??: "+tenThuThu+"\n"+
                                    "Gi???i t??nh: "+gt+"\n"+
                                    "Ng??y sinh: "+ngaySinh+"\n"+
                                    "CMND: "+cmnd+"\n"+
                                    "Email: "+email+"\n"+
                                    "??i???n tho???i: "+dienThoai+"\n";
                            alert.setContentText(content);
                            alert.show();
                            loadData();
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("L???i");
                            alert.setHeaderText("???? c?? l???i x???y ra");
                            alert.setContentText("C???p nh???t th??ng tin th??? th?? th???t b???i");
                            alert.show();
                        }


                    }

                }
            });
            return row;
        });
    }

    private void initColumns() {
        idThuThuColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        tenThuThuColumn.setCellValueFactory(new PropertyValueFactory<>("tenTT"));
        gioiTinhColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ThuThu, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ThuThu, String> param) {
                ThuThu ThuThu = param.getValue();
                int gioiTinh = ThuThu.getGioiTinh();
                String content = "";
                if(gioiTinh == 2){
                    content = "Kh??c";
                }else if(gioiTinh == 1){
                    content = "Nam";
                }else{
                    content = "N???";
                }
                return new ReadOnlyObjectWrapper<>(content);
            }
        });
        ngaySinhColumn.setCellValueFactory(new PropertyValueFactory<>("ngaySinh"));
        CMNDColumn.setCellValueFactory(new PropertyValueFactory<>("CMND"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        sdtColumn.setCellValueFactory(new PropertyValueFactory<>("dienThoai"));

        Callback<TableColumn<ThuThu, Void>, TableCell<ThuThu, Void>> cellFactory = new Callback<TableColumn<ThuThu, Void>, TableCell<ThuThu, Void>>() {
            @Override
            public TableCell<ThuThu, Void> call(final TableColumn<ThuThu, Void> param) {
                final TableCell<ThuThu, Void> cell = new TableCell<ThuThu, Void>() {
                    private final Button deleteButton = new Button("X??a");

                    {
                        deleteButton.setOnAction((ActionEvent e)->{
                            ThuThu data = getTableView().getItems().get(getIndex());
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setTitle("X??a th??? th??");
                            alert.setHeaderText("X??c nh???n x??a");
                            alert.setContentText("B???n c?? ch???c x??a th??? th?? "+data.getTenTT()+" c?? ID l?? "+data.getId()+" kh???i th?? vi???n kh??ng?");
                            ButtonType xacNhanButtonType = new ButtonType("X??c nh???n", ButtonBar.ButtonData.OK_DONE);
                            ButtonType thoatButtonType = new ButtonType("Tho??t", ButtonBar.ButtonData.CANCEL_CLOSE);
                            alert.getButtonTypes().clear();
                            alert.getButtonTypes().addAll(xacNhanButtonType, thoatButtonType);
                            Optional<ButtonType> result = alert.showAndWait();
                            if(result.get() == xacNhanButtonType){
                                try {
                                    thuThuDAO.deleteTT(data.getId());
                                    loadData();
                                } catch (SQLException throwables) {
                                    throwables.printStackTrace();
                                    Alert alert1 = new Alert(Alert.AlertType.ERROR);
                                    alert1.setTitle("L???i");
                                    alert1.setHeaderText("C?? l???i x???y ra khi x??a th??? th??");
                                    alert1.setContentText("Kh??ng th??? x??a th??? th??");
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
            thuThuObservableList = FXCollections.observableArrayList(thuThuDAO.findAll());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        table.setItems(thuThuObservableList);
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
            String fileName = "ThuThu"+ calendar.getTimeInMillis()+".csv";
            String filePath = dir.getAbsolutePath() +"\\"+fileName;
            System.out.println(filePath);
            File file = new File(filePath);
            try {
                FileOutputStream fos = new FileOutputStream(file);
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
                CSVWriter writer = new CSVWriter(outputStreamWriter);
                List<String[]> data = new ArrayList<String[]>();
                data.add(ThuThu.getHeader());
                for(int i = 0; i < thuThuObservableList.size(); i++){
                    data.add(thuThuObservableList.get(i).getPrint());
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
