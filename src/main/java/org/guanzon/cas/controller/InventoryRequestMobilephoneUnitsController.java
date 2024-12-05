package org.guanzon.cas.controller;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import static javafx.scene.input.KeyCode.DOWN;
import static javafx.scene.input.KeyCode.ENTER;
import static javafx.scene.input.KeyCode.F3;
import static javafx.scene.input.KeyCode.UP;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.swing.JRViewer;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.cas.inventory.stock.Inv_Request;
import org.guanzon.cas.inventory.stock.request.RequestControllerFactory;
import org.guanzon.cas.model.ModelMobile;
import org.guanzon.cas.model.ModelStockRequest;
import org.guanzon.cas.model.ReportPrinter;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author User
 */
public class InventoryRequestMobilephoneUnitsController implements Initializable, ScreenInterface {

    private final String pxeModuleName = "Inventory Request MC";
    private GRider oApp;
    private int pnEditMode;
    private Inv_Request oTrans;
    private boolean pbLoaded = false;
    private ObservableList<ModelStockRequest> R1data = FXCollections.observableArrayList();
    private ObservableList<ModelStockRequest> R2data = FXCollections.observableArrayList();
    private int pnRow = 0;
    private JasperPrint jasperPrint;
    private JRViewer jrViewer;
    private String categForm = "";
    private boolean isReportRunning = false; // Flag to track if report is running
    private int pnROQ = 0;
    ReportPrinter printer = new ReportPrinter();

    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    @FXML
    private TextField txtField01, txtField02, txtField03, txtField04, txtField05, txtField06,
            txtField07, txtField08, txtField09, txtField10, txtField11, txtField12,
            txtField13, txtField14, txtField15, txtField16, txtField17, txtField18,
            txtField19, txtField20, txtField21, txtField22, txtField23, txtField24,
            txtField25;
    @FXML
        private AnchorPane anchorMain,
            anchorMaster,
            anchorTable,
            anchorDetails;
    @FXML
    private DatePicker dpField01;

    @FXML
    private TextArea txtArea01;

    @FXML
    private Label lblStatus;

    @FXML
    private TableView tblDetails,tblDetailsROQ;

    @FXML
    private TableColumn index01, index02, index03, index04, index05,
            index06, index07, index08, index09, index10, index11;
    @FXML
    private TableColumn indexROQ01, indexROQ02, indexROQ03, indexROQ04, indexROQ05,
            indexROQ06, indexROQ07, indexROQ08, indexROQ09;

    @FXML
    private HBox hbButtons;

    @FXML
    private Button btnStatistic, btnBrowse, btnNew, btnSave, btnUpdate, btnSearch,
            btnPrint, btnAddItem, btnDelItem, btnApprove, btnCancelTrans,
            btnCancel, btnClose;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        initTrans();
        ClickButton();
        clearAllFields();
        InitTextFields();
        initButton(pnEditMode);
        initTblDetails();
        initTblDetailsROQ();
        initTabAnchor();
        lblStatus.setText("UNKNOWN");
        oTrans.setWithUI(true);
        pbLoaded = true;
    }



    /*Handle button click*/
    private void ClickButton() {
        btnCancel.setOnAction(this::handleButtonAction);
        btnNew.setOnAction(this::handleButtonAction);
        btnSave.setOnAction(this::handleButtonAction);
        btnUpdate.setOnAction(this::handleButtonAction);
        btnClose.setOnAction(this::handleButtonAction);
        btnBrowse.setOnAction(this::handleButtonAction);
        btnAddItem.setOnAction(this::handleButtonAction);
        btnDelItem.setOnAction(this::handleButtonAction);
        btnPrint.setOnAction(this::handleButtonAction);
        btnCancelTrans.setOnAction(this::handleButtonAction);
        btnApprove.setOnAction(this::handleButtonAction);
    }

    private void handleButtonAction(ActionEvent event) {
        Object source = event.getSource();
        JSONObject poJSON;
        if (source instanceof Button) {
            Button clickedButton = (Button) source;
            unloadForm appUnload = new unloadForm();
            switch (clickedButton.getId()) {
                case "btnClose":
                    if (ShowMessageFX.YesNo("Do you really want to cancel this record? \nAny data collected will not be kept.", "Computerized Accounting System", pxeModuleName)) {
                        clearAllFields();
                        appUnload.unloadForm(anchorMain, oApp, pxeModuleName);
                    }
                    break;

                case "btnNew":
                    poJSON = oTrans.newTransaction();
                    if ("success".equals((String) poJSON.get("result"))) {
                        pnEditMode = oTrans.getEditMode();
                        pnRow = 0;
                        initButton(pnEditMode);
                        clearAllFields();
                        initTabAnchor();
//                        loadItemData();
                        loadItemDataROQ();
                        loadDetails();
                        txtField01.setText((String) oTrans.getMasterModel().getTransactionNumber());
                        LocalDate currentDate = LocalDate.now();

// Optionally format the date (not necessary for setting value in DatePicker)
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        String formattedDate = currentDate.format(formatter);
                        dpField01.setValue(currentDate);
                    }
                    break;

                case "btnSave":
                    poJSON = oTrans.saveTransaction();
                    System.out.println(poJSON.toJSONString());
                    if ("success".equals((String) poJSON.get("result"))) {
                        System.err.println((String) poJSON.get("message"));
                        ShowMessageFX.Information((String) poJSON.get("message"), "Computerized Accounting System", pxeModuleName);
                        initTrans();
                        clearAllFields();
                        initTabAnchor();
                    } else {
                        ShowMessageFX.Information((String) poJSON.get("message"), "Computerized Accounting System", pxeModuleName);
                        loadItemData();
                        loadItemDataROQ();
                    }
                    break;
                case "btnUpdate":
                    poJSON = oTrans.updateTransaction();
                    if ("error".equals((String) poJSON.get("result"))) {
                        ShowMessageFX.Information((String) poJSON.get("message"), "Computerized Accounting System", pxeModuleName);
                        break;
                    }

                    pnEditMode = oTrans.getEditMode();
                    pnRow = oTrans.getDetailModel().size() - 1;
//                        
//                        System.out.println("pnRow sa update= " + pnRow);
                    loadItemData();
                    loadItemDataROQ();
                    initButton(pnEditMode);
                    initTblDetails();
                    initTabAnchor();
                    txtField03.requestFocus();
                    tblDetailsROQ.getSelectionModel().select(pnRow + 1);
                    break;

                case "btnBrowse":
                    poJSON = oTrans.searchTransaction("sTransNox", "", pbLoaded);
                    if ("error".equals((String) poJSON.get("result"))) {
                        ShowMessageFX.Information((String) poJSON.get("message"), "Computerized Accounting System", pxeModuleName);
                        break;
                    }
                    pnEditMode = oTrans.getEditMode();
                    R1data.clear();
                    loadTransaction();
                    initTblDetails();
                    loadItemData();
                    loadItemDataROQ();
                    initTabAnchor();
                    tblDetailsROQ.getSelectionModel().select(0);
                        loadDetails();
                    break;

                case "btnAddItem":

                    poJSON = oTrans.AddModelDetail();
                    System.out.println(poJSON.toJSONString());
                    if ("error".equals((String) poJSON.get("result"))) {
                        ShowMessageFX.Information((String) poJSON.get("message"), "Computerized Accounting System", pxeModuleName);
                        break;
                    }
                    pnRow = oTrans.getDetailModel().size() - 1;
                    clearItem();
                    pnEditMode = oTrans.getEditMode();
                    loadItemData();
                    loadItemDataROQ();
                    tblDetailsROQ.getSelectionModel().select(pnRow + 1);

                    break;

                case "btnDelItem":
                    if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Do you want to remove this item?") == true) {
                        oTrans.RemoveModelDetail(pnRow);
                        pnRow = oTrans.getDetailModel().size() - 1;
                        clearItem();
                        loadItemData();
                        loadItemDataROQ();
                        txtField04.requestFocus();
                    }
                    break;
                case "btnCancel":
                    if (ShowMessageFX.YesNo("Do you really want to cancel this record? \nAny data collected will not be kept.", "Computerized Accounting System", pxeModuleName)) {

                        if (pnEditMode == EditMode.UPDATE) {
                            oTrans.cancelUpdate();
                        }
                        initTrans();
                        initTabAnchor();

                    }
                    break;
                case "btnPrint":
                    if (pnEditMode == 1 && ShowMessageFX.YesNo("Do you want to print this record?", "Computerized Accounting System", pxeModuleName)) {
                        loadPrint();
                    }
                    break;
                case "btnCancelTrans":
                    if (pnEditMode == 1) {
                        if (ShowMessageFX.YesNo("Do you really want to cancel this transaction?", "Computerized Accounting System", pxeModuleName)) {
                            poJSON = oTrans.cancelTransaction(oTrans.getMasterModel().getTransactionNumber());
                            System.out.println(poJSON.toJSONString());
                            if ("error".equals((String) poJSON.get("result"))) {
                                ShowMessageFX.Information((String) poJSON.get("message"), "Computerized Accounting System", pxeModuleName);
                                break;
                            }
                            ShowMessageFX.Information("Transaction cancelled succesfully.", "Computerized Accounting System", pxeModuleName);
                            clearAllFields();
                            initTrans();
                            initTabAnchor();
                        }
                    }
                    break;
                case "btnApprove":
                    if (pnEditMode == 1) {
                        if (ShowMessageFX.YesNo("Do you really want to post this transaction?", "Computerized Accounting System", pxeModuleName)) {
                            poJSON = oTrans.postTransaction(oTrans.getMasterModel().getTransactionNumber());
                            System.out.println(poJSON.toJSONString());
                            if ("error".equals((String) poJSON.get("result"))) {
                                ShowMessageFX.Information((String) poJSON.get("message"), "Computerized Accounting System", pxeModuleName);
                                break;
                            }
                            ShowMessageFX.Information("Transaction successfully approve.", "Computerized Accounting System", pxeModuleName);
                            clearAllFields();
                            initTrans();
                            initTabAnchor();
                        }
                    }
                    break;
            }
        }
    }

    private void clearAllFields() {
// Arrays of TextFields grouped by sections
        TextField[][] allFields = {
            // Text fields related to specific sections
            {txtField01, txtField02, txtField03, txtField04, txtField05, txtField06,
            txtField07, txtField08, txtField09, txtField10, txtField11, txtField12,
            txtField13, txtField14, txtField15, txtField16, txtField17, txtField18,
            txtField19, txtField20, txtField21, txtField22, txtField23, txtField24,
            txtField25},};

// Loop through each array of TextFields and clear them
        for (TextField[] fields : allFields) {
            for (TextField field : fields) {
                field.clear();
            }
        }
        R1data.clear();
         R2data.clear();
        txtArea01.clear();
        lblStatus.setText("UNKNOWN");
    }


    /*initialize fields*/
    private void InitTextFields() {
        txtArea01.focusedProperty().addListener(txtArea_Focus);
// Define arrays for text fields with focusedProperty listeners
        TextField[] focusTextFields = {
            txtField01, txtField02, txtField03, txtField04, txtField05, txtField06,
            txtField07, txtField08, txtField09, txtField10, txtField11, txtField12,
            txtField13, txtField14, txtField15, txtField16, txtField17, txtField18,
            txtField19, txtField20, txtField21, txtField22, txtField23, txtField24,
            txtField25};

// Add the listener to each text field in the focusTextFields array
        for (TextField textField : focusTextFields) {
            textField.focusedProperty().addListener(txtField_Focus);
        }

// Define arrays for text fields with setOnKeyPressed handlers
        TextField[] keyPressedTextFields = {
            txtField05, txtField09, txtField11,txtField14,txtField08
        };

// Set the same key pressed event handler for each text field in the keyPressedTextFields array
        for (TextField textField : keyPressedTextFields) {
            textField.setOnKeyPressed(this::txtField_KeyPressed);
        }

//        lblStatus.setText(chkField04.isSelected() ? "ACTIVE" : "INACTIVE");        
    }
    final ChangeListener<? super Boolean> txtArea_Focus = (o, ov, nv) -> {
        if (!pbLoaded) {
            return;
        }

        TextArea txtArea = (TextArea) ((ReadOnlyBooleanPropertyBase) o).getBean();
        int lnIndex = Integer.parseInt(txtArea.getId().substring(7, 9));
        String lsValue = (txtArea.getText() == null ? "" : txtArea.getText());
        JSONObject jsonObject = new JSONObject();
        if (lsValue == null) {
            return;
        }
        if (!nv) {
            /*Lost Focus*/
            switch (lnIndex) {
                case 1:
                    oTrans.getMasterModel().setRemarks(lsValue);
                    break;
            }
        }
    };

    final ChangeListener<? super Boolean> txtField_Focus = (o, ov, nv) -> {
        if (!pbLoaded) {
            return;
        }

        TextField txtField = (TextField) ((ReadOnlyBooleanPropertyBase) o).getBean();
        int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
        String lsValue = (txtField.getText() == null ? "" : txtField.getText());
        JSONObject jsonObject = new JSONObject();
        if (lsValue == null) {
            return;
        }
        if (!nv) {
            /*Lost Focus*/
            switch (lnIndex) {
                case 1:
                    /*TRANSACTION NO*/
//                   oTrans.getModel().setStockID(lsValue);
                    System.out.print("TRANSACTION NO == ");
                    break;
                case 2:/*Reference No*/
//                   oTrans.getModel().setBarcode(lsValue);
                    System.out.println("Reference No == " + lsValue);
                    break;
                case 3:/*BARRCODE*/
//                   oTrans.getModel().setAltBarcode (lsValue);
                    System.out.print("BARRCODE == ");
                    break;
                case 4:/*ITEM DESCRIPTION*/
//                   oTrans.getModel().setBriefDescription(lsValue);
                    System.out.print("ITEM DESCRIPTION == ");
                    break;
                case 5:/*ROQ*/
//                   oTrans.getModel().setDescription(lsValue);
                    System.out.print("CLASSIFY == ");
                    break;
                case 8:/*ORDER QTY*/
                    if (lsValue.matches("\\d*")) {
                        int qty = (lsValue.isEmpty()) ? 0 : Integer.parseInt(lsValue);
                        oTrans.getDetailModel().get(pnRow).setQuantity(qty);
                        if(qty>0){
                        
                            JSONObject addObj = oTrans.AddDetailOthers(pnRow);
                                System.out.println((String) addObj.get("message"));
                                if ("error".equals((String) addObj.get("result"))){
                                    ShowMessageFX.Information((String) addObj.get("message"), "Computerized Acounting System", pxeModuleName);
                                    break;
                                } 
    //                         pnROQ = oTrans.getDetailModelOthers().size()-1;     
                            loadItemDataROQ();
                            loadItemData();  
                            tblDetails.getSelectionModel().select(pnRow + 1);
                            tblDetailsROQ.getSelectionModel().select(pnROQ + 1);

                        }
                        break;
                    } else {
                        ShowMessageFX.Information("Invalid Input", "Computerized Accounting System", pxeModuleName);
                    }
//                    txtField.setText("");
                    txtField.requestFocus();
                    break;
            }
//            loadItemData();
            loadItemDataROQ();
        } else {
            txtField.selectAll();
        }
    };

    private void txtField_KeyPressed(KeyEvent event) {
        TextField txtField = (TextField) event.getSource();
        int lnIndex = Integer.parseInt(((TextField) event.getSource()).getId().substring(8, 10));
        String lsValue = (txtField.getText() == null ? "" : txtField.getText());
        JSONObject poJson;
        switch (event.getCode()) {
            case ENTER:
            case F3:
                switch (lnIndex) {
                    case 03:
                        /*search barcode*/
                        poJson = new JSONObject();
                        poJson = oTrans.searchDetail(pnRow, 3, lsValue, true);
                        System.out.println("poJson = " + poJson.toJSONString());
                        if ("error".equalsIgnoreCase(poJson.get("result").toString())) {
                            ShowMessageFX.Information((String) poJson.get("message"), "Computerized Accounting System", pxeModuleName);
                            break;
                        }
                        break;
                    case 04:/*search desciption*/
                        poJson = new JSONObject();
                        poJson = oTrans.searchDetail(pnRow, 3, lsValue, false);
                        System.out.println("poJson = " + poJson.toJSONString());
                        if ("error".equalsIgnoreCase(poJson.get("result").toString())) {
                            ShowMessageFX.Information((String) poJson.get("message"), "Computerized Accounting System", pxeModuleName);
                            break;
                        }
                        break;
                    case 8:/*QUANTITY ORDER*/
                        tblDetailsROQ.requestFocus();
                        Platform.runLater(() -> {
                            if (pnRow < tblDetailsROQ.getItems().size() - 1) {
                                pnRow++;

                                tblDetailsROQ.getSelectionModel().select(pnRow);
                                tblDetailsROQ.scrollTo(pnRow); // Scroll to ensure the row is visible
                                loadDetails();

                                // Update focus to txtField14 after loading details
                                Platform.runLater(() -> txtField08.requestFocus());

                                // Optionally refresh the table to make sure selection is visually updated
                                tblDetailsROQ.refresh();
                            }
                        });
                        break;

                }
                loadDetails();
                txtField08.requestFocus();
        }
        switch (event.getCode()) {
            case DOWN:
                CommonUtils.SetNextFocus(txtField);
                break;
            case UP:
                CommonUtils.SetPreviousFocus(txtField);
        }
    }

    /*USE TO DISABLE ANCHOR BASE ON INITMODE*/
    private void initTabAnchor() {
        System.out.print("EDIT MODE == " + pnEditMode);
        boolean pbValue = pnEditMode == EditMode.ADDNEW
                || pnEditMode == EditMode.UPDATE;

        System.out.print("pbValue == " + pbValue);
        anchorMaster.setDisable(!pbValue);
        anchorDetails.setDisable(!pbValue);
        anchorTable.setDisable(!pbValue);
        if (pnEditMode == EditMode.READY) {
            anchorTable.setDisable(false);
            btnStatistic.setDisable(false);
        }
    }

    /*Text seek/search*/
    private void txtSeeks_KeyPressed(KeyEvent event) {
        TextField txtSeeks = (TextField) event.getSource();
        int lnIndex = Integer.parseInt(((TextField) event.getSource()).getId().substring(8, 10));
        String lsValue = (txtSeeks.getText() == null ? "" : txtSeeks.getText());
        JSONObject poJSON;
        switch (event.getCode()) {
            case F3:
                switch (lnIndex) {
                    case 1:
                        /*transaction no*/
                        System.out.print("search transaction == " + lsValue);
                        break;
                }
            case ENTER:
        }
        switch (event.getCode()) {
            case ENTER:
                CommonUtils.SetNextFocus(txtSeeks);
            case DOWN:
                CommonUtils.SetNextFocus(txtSeeks);
                break;
            case UP:
                CommonUtils.SetPreviousFocus(txtSeeks);
        }
    }

    private void initButton(int fnValue) {
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);

// Manage visibility and managed state of buttons
        btnCancel.setVisible(lbShow);
        btnSearch.setVisible(lbShow);
        btnSave.setVisible(lbShow);
        btnAddItem.setVisible(lbShow);
        btnDelItem.setVisible(lbShow);

        btnCancel.setManaged(lbShow);
        btnSearch.setManaged(lbShow);
        btnSave.setManaged(lbShow);
        btnAddItem.setManaged(lbShow);
        btnDelItem.setManaged(lbShow);

// Manage visibility and managed state of other buttons
        
        btnNew.setVisible(!lbShow);
        btnClose.setVisible(!lbShow);
        btnBrowse.setManaged(!lbShow);
        btnNew.setManaged(!lbShow);
        btnClose.setManaged(!lbShow);
        
        btnBrowse.setVisible(false);
        btnBrowse.setManaged(false);
        
        btnUpdate.setVisible(false);
        btnUpdate.setManaged(false);
        
        btnApprove.setVisible(false);
        btnApprove.setManaged(false);
        
        btnCancelTrans.setVisible(false);
        btnCancelTrans.setManaged(false);
        
        btnAddItem.setVisible(false);
        btnAddItem.setManaged(false);
        btnDelItem.setVisible(false);
        btnDelItem.setManaged(false);

    }

    private void initTblDetails() {
        index01.setStyle("-fx-alignment: CENTER;");
        index02.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        index03.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        index04.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        index05.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        index06.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        index07.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        index08.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        index09.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        index10.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        index11.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");

        index01.setCellValueFactory(new PropertyValueFactory<>("index01"));
        index02.setCellValueFactory(new PropertyValueFactory<>("index02"));
        index03.setCellValueFactory(new PropertyValueFactory<>("index03"));
        index04.setCellValueFactory(new PropertyValueFactory<>("index04"));
        index05.setCellValueFactory(new PropertyValueFactory<>("index05"));
        index06.setCellValueFactory(new PropertyValueFactory<>("index06"));
        index07.setCellValueFactory(new PropertyValueFactory<>("index07"));
        index08.setCellValueFactory(new PropertyValueFactory<>("index08"));
        index09.setCellValueFactory(new PropertyValueFactory<>("index09"));
        index10.setCellValueFactory(new PropertyValueFactory<>("index10"));
        index11.setCellValueFactory(new PropertyValueFactory<>("index11"));

        tblDetails.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblDetails.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });
        tblDetails.setItems(R1data);
        tblDetails.autosize();
    }
    
    private void initTblDetailsROQ() {
        indexROQ01.setStyle("-fx-alignment: CENTER;");
        indexROQ02.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        indexROQ03.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        indexROQ04.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        indexROQ05.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        indexROQ06.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        indexROQ07.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        indexROQ08.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        indexROQ09.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");

        indexROQ01.setCellValueFactory(new PropertyValueFactory<>("index01"));
        indexROQ02.setCellValueFactory(new PropertyValueFactory<>("index02"));
        indexROQ03.setCellValueFactory(new PropertyValueFactory<>("index03"));
        indexROQ04.setCellValueFactory(new PropertyValueFactory<>("index04"));
        indexROQ05.setCellValueFactory(new PropertyValueFactory<>("index05"));
        indexROQ06.setCellValueFactory(new PropertyValueFactory<>("index06"));
        indexROQ07.setCellValueFactory(new PropertyValueFactory<>("index07"));
        indexROQ08.setCellValueFactory(new PropertyValueFactory<>("index08"));
        indexROQ09.setCellValueFactory(new PropertyValueFactory<>("index09"));

        tblDetailsROQ.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblDetailsROQ.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });
        tblDetailsROQ.setItems(R2data);
        tblDetailsROQ.autosize();
    }

    private void loadTransaction() {
        if (pnEditMode == EditMode.READY
                || pnEditMode == EditMode.ADDNEW
                || pnEditMode == EditMode.UPDATE) {

            txtField01.setText((String) oTrans.getMasterModel().getTransactionNumber());
            txtArea01.setText((String) oTrans.getMasterModel().getRemarks());
//            dpField01.setValue((Date) oTrans.getMasterModel().getTransaction());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

// Parse the formatted date string into a LocalDate object
            if (oTrans.getMasterModel().getTransaction() != null && !oTrans.getMasterModel().getTransaction().toString().trim().isEmpty()) {
                LocalDate localbdate = LocalDate.parse(oTrans.getMasterModel().getTransaction().toString(), formatter);

// Set the value of the DatePicker to the parsed LocalDate
                dpField01.setValue(localbdate);
            }
// Parse the formatted date string into a LocalDate object
            if (oTrans.getMasterModel().getTransaction() != null && !oTrans.getMasterModel().getTransaction().toString().trim().isEmpty()) {
                LocalDate localbdate = LocalDate.parse(oTrans.getMasterModel().getTransaction().toString(), formatter);

// Set the value of the DatePicker to the parsed LocalDate
                dpField01.setValue(localbdate);
            }

            switch (oTrans.getMasterModel().getTransactionStatus()) {
                case "0":
                    lblStatus.setText("OPEN");
                    break;
                case "1":
                    lblStatus.setText("CLOSED");
                    break;
                case "2":
                    lblStatus.setText("POSTED");
                    break;
                case "3":
                    lblStatus.setText("CANCELLED");
                    break;
                case "5":
                    lblStatus.setText("VOID");
                    break;
                default:
                    lblStatus.setText("UNKNOWN");
                    break;
            }
        }
    }

    private void loadDetails() {
        if (!oTrans.getDetailModel().isEmpty()) {
            txtField03.setText((String) oTrans.getDetailModel().get(pnRow).getBrandName());
            txtField04.setText((String) oTrans.getDetailModel().get(pnRow).getModelName()); 
            txtField05.setText(String.valueOf(oTrans.getDetailModel().get(pnRow).getQuantityOnHand()));
            txtField06.setText("");
            txtField07.setText(String.valueOf(oTrans.getDetailModel().get(pnRow).getOnTransit()));
            txtField08.setText(String.valueOf(oTrans.getDetailModel().get(pnRow).getQuantity()));
            txtField10.setText(String.valueOf(oTrans.getDetailModel().get(pnRow).getQuantityOnHand()));
            txtField11.setText((String)oTrans.getDetailModel().get(pnRow).getColorName());
            txtField12.setText(String.valueOf(oTrans.getDetailModel().get(pnRow).getMinimumLevel()));
            txtField13.setText(String.valueOf(oTrans.getDetailModel().get(pnRow).getMaximumLevel()));
            txtField14.setText(String.valueOf(oTrans.getDetailModel().get(pnRow).getQuantityOnHand()));
            txtField15.setText((String) oTrans.getDetailModel().get(pnRow).getClassify());
            txtField16.setText(String.valueOf(oTrans.getDetailModel().get(pnRow).getOnTransit()));
            txtField17.setText(String.valueOf(oTrans.getDetailModel().get(pnRow).getRecordOrder()));
            txtField18.setText(String.valueOf(oTrans.getDetailModel().get(pnRow).getBackOrder()));
        }
    }
    private void loadDetailsOthers() {
        if (!oTrans.getDetailModelOthers().isEmpty()) {
            txtField03.setText((String) oTrans.getDetailModelOthers().get(pnROQ).getBrandName());
            txtField04.setText((String) oTrans.getDetailModelOthers().get(pnROQ).getModelName()); 
            txtField05.setText(String.valueOf(oTrans.getDetailModelOthers().get(pnROQ).getQuantityOnHand()));
            txtField06.setText("");
            txtField07.setText(String.valueOf(oTrans.getDetailModelOthers().get(pnROQ).getOnTransit()));
            txtField08.setText(String.valueOf(oTrans.getDetailModelOthers().get(pnROQ).getQuantity()));
            txtField10.setText(String.valueOf(oTrans.getDetailModelOthers().get(pnROQ).getQuantityOnHand()));
            txtField11.setText((String)oTrans.getDetailModelOthers().get(pnROQ).getColorName());
            txtField12.setText(String.valueOf(oTrans.getDetailModelOthers().get(pnROQ).getMinimumLevel()));
            txtField13.setText(String.valueOf(oTrans.getDetailModelOthers().get(pnROQ).getMaximumLevel()));
            txtField14.setText(String.valueOf(oTrans.getDetailModelOthers().get(pnROQ).getQuantityOnHand()));
            txtField15.setText((String) oTrans.getDetailModelOthers().get(pnROQ).getClassify());
            txtField16.setText(String.valueOf(oTrans.getDetailModelOthers().get(pnROQ).getOnTransit()));
            txtField17.setText(String.valueOf(oTrans.getDetailModelOthers().get(pnROQ).getRecordOrder()));
            txtField18.setText(String.valueOf(oTrans.getDetailModelOthers().get(pnROQ).getBackOrder()));
        }
    }
    private void loadItemData() {
        int lnCtr;
        R1data.clear();
        if (oTrans.getDetailModelOthers()!= null) {
            for  (lnCtr = 0; lnCtr < oTrans.getDetailModelOthers().size(); lnCtr++){
            R1data.add(new ModelStockRequest(String.valueOf(lnCtr + 1),
                
            
            (String) oTrans.getDetailModelOthers().get(lnCtr).getBrandName(),
                (String) oTrans.getDetailModelOthers().get(lnCtr).getModelName(),
                        "",                
                            "",
                        (oTrans.getDetailModelOthers().get(lnCtr).getColorName() == null ? "" : oTrans.getDetailModelOthers().get(lnCtr).getColorName()),
                        (oTrans.getDetailModelOthers().get(lnCtr).getClassify() == null ? "" : oTrans.getDetailModelOthers().get(lnCtr).getClassify()),
                        oTrans.getDetailModelOthers().get(lnCtr).getQuantityOnHand().toString(),
                        "",
                        oTrans.getDetailModelOthers().get(lnCtr).getRecordOrder().toString(),
                        oTrans.getDetailModelOthers().get(lnCtr).getQuantity().toString()));
            }
        }
    }
        private void loadItemDataROQ() {
        int lnCtr;
        R2data.clear();
        if (oTrans.getDetailModel() != null) {
            for (lnCtr = 0; lnCtr < oTrans.getDetailModel().size(); lnCtr++) {
                ;
//            oTrans.getDetailModel().get(lnCtr).list();
                R2data.add(new ModelStockRequest(String.valueOf(lnCtr + 1),
                        (String) oTrans.getDetailModel().get(lnCtr).getModelName(),
                        "",
                        (oTrans.getDetailModel().get(lnCtr).getColorName() == null ? "" : oTrans.getDetailModel().get(lnCtr).getColorName()),
                        (oTrans.getDetailModel().get(lnCtr).getClassify() == null ? "" : oTrans.getDetailModel().get(lnCtr).getClassify()),
                        oTrans.getDetailModel().get(lnCtr).getQuantityOnHand().toString(),
                        "",
                        oTrans.getDetailModel().get(lnCtr).getRecordOrder().toString(),
                        oTrans.getDetailModel().get(lnCtr).getQuantity().toString()));
            }
            System.out.println("list " + oTrans.getItemCount());
        }
    }

    @FXML
    private void tblDetails_Clicked(MouseEvent event) {
        if (tblDetails.getSelectionModel().getSelectedIndex() >= 0) {
            pnROQ = tblDetails.getSelectionModel().getSelectedIndex();
            System.out.println("pnROQ = " + pnROQ);
            loadDetailsOthers();
            txtField08.requestFocus();
            txtField08.selectAll();
        }
        tblDetails.setOnKeyReleased((KeyEvent t) -> {
            KeyCode key = t.getCode();
            switch (key) {
                case DOWN:
                    pnROQ = tblDetails.getSelectionModel().getSelectedIndex();
                    if (pnROQ == tblDetails.getItems().size()) {
                        pnROQ = tblDetails.getItems().size();
                        loadDetailsOthers();
                    } else {
                        loadDetailsOthers();
                    }
                    break;
                case UP:
//                    int pnROQ = 0;
                    int x = 1;
                    pnROQ = tblDetails.getSelectionModel().getSelectedIndex();

                    loadDetailsOthers();
                    break;
                default:
                    break;
            }
        });
    }
        @FXML
    private void tblDetailsROQ_Clicked(MouseEvent event) {
        if (tblDetailsROQ.getSelectionModel().getSelectedIndex() >= 0) {
            pnRow = tblDetailsROQ.getSelectionModel().getSelectedIndex();
            System.out.println("pnRow = " + pnRow);
            loadDetails();
            txtField08.requestFocus();
            txtField08.selectAll();
        }
        tblDetails.setOnKeyReleased((KeyEvent t) -> {
            KeyCode key = t.getCode();
            switch (key) {
                case DOWN:
                    pnRow = tblDetailsROQ.getSelectionModel().getSelectedIndex();
                    if (pnRow == tblDetailsROQ.getItems().size()) {
                        pnRow = tblDetailsROQ.getItems().size();
                        loadDetails();
                    } else {
                        loadDetails();
                    }
                    break;
                case UP:
                    int pnRows = 0;
                    int x = 1;
                    pnRow = tblDetailsROQ.getSelectionModel().getSelectedIndex();

                    loadDetails();
                    break;
                default:
                    break;
            }
        });
    }

    private void clearItem() {
        TextField[][] allFields = {
            // Text fields related to specific sections
            {txtField05, txtField06,txtField07, txtField08, txtField09, txtField10, txtField11, txtField12,
            txtField13, txtField14, txtField15, txtField16, txtField17, txtField18,
            txtField19, txtField20, txtField21, txtField22, txtField23, txtField24,
            txtField25},};

        for (TextField[] fields : allFields) {
            for (TextField field : fields) {
                field.clear();
            }
        }
    }

    private void initTrans() {
        Properties po_props = new Properties();
        clearAllFields();
        oTrans = new Inv_Request(oApp, true);
        String industry = System.getProperty("store.inventory.industry");

        RequestControllerFactory.RequestType types = null;
        String[] category = industry.split(";");
// Print the resulting array
        for (String type : category) {
            if (types == null) {
                if ("0001".equals(type)) {
                    types = RequestControllerFactory.RequestType.MC;
                    oTrans.setType(types);
                } else if ("0002".equals(type)) {
                    types = RequestControllerFactory.RequestType.MP;
                    oTrans.setType(types);
                } else if ("0003".equals(type)) {
                    types = RequestControllerFactory.RequestType.AUTO;
                    oTrans.setType(types);
                }

                System.out.println("type value = " + types);
            }
        }
        oTrans.setCategoryType(RequestControllerFactory.RequestCategoryType.WITH_ROQ);
        oTrans.setTransactionStatus("0123");
        oTrans.isHistory(false);
        oTrans.setWithUI(true);
        pnEditMode = EditMode.UNKNOWN;
        initButton(pnEditMode);
    }

    private boolean loadPrint() {
        JSONObject loJSON = new JSONObject();
        if (oTrans.getMasterModel().getTransactionNumber() == null) {
            ShowMessageFX.Warning("Unable to print transaction.", "Warning", "No record loaded.");
            loJSON.put("result", "error");
            loJSON.put("message", "Model Master is null");
            return false;
        }

        // Prepare report parameters
        Map<String, Object> params = new HashMap<>();
        params.put("sPrintdBy", "Printed By: " + oApp.getLogName());
//      params.put("sReportDt", CommonUtils.xsDateLong(oApp.getServerDate()));
        params.put("sReportNm", pxeModuleName);
        params.put("sReportDt", CommonUtils.xsDateMedium((Date) oApp.getServerDate()));
        params.put("sBranchNm", oApp.getBranchName());
        params.put("sAddressx", oApp.getAddress());
        params.put("sTransNox", oTrans.getMasterModel().getTransactionNumber());
        params.put("sTranDte", CommonUtils.xsDateMedium((Date) oTrans.getMasterModel().getTransaction()));
        params.put("sRemarks", oTrans.getMasterModel().getRemarks());
        params.put("status", oTrans.getMasterModel().getTransactionStatus());
//      params.put("sTranType", "Unprcd Qty");
//      params.put("sTranQty", "Cancel");

        // Define report file paths
        String sourceFileName = oApp.getReportPath() + "InventoryRequestROQ.jasper";
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(R1data);

        return printer.loadAndShowReport(sourceFileName, params, R1data, pxeModuleName);
    }
}
