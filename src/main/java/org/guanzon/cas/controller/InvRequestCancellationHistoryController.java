/*
* Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
* Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.cas.controller;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import java.awt.Dimension;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
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
import javafx.stage.Screen;
import javax.swing.JFrame;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.swing.JRViewer;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.cas.inventory.stock.Inv_Request;
import org.guanzon.cas.inventory.stock.request.RequestControllerFactory;
import org.guanzon.cas.inventory.stock.request.cancel.InvRequestCancel;
import org.guanzon.cas.model.ModelStockRequest;
import org.guanzon.cas.model.ReportPrinter;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author User
 */
public class InvRequestCancellationHistoryController implements Initializable, ScreenInterface {

    private final String pxeModuleName = "Inventory Request Cancellation History";
    private GRider oApp;
    private int pnEditMode;
    private InvRequestCancel oTrans;
    private boolean pbLoaded = false;
    private ObservableList<ModelStockRequest> R1data = FXCollections.observableArrayList();
    private ObservableList<ModelStockRequest> R2data = FXCollections.observableArrayList();
    private int pnRow = 0;
    
    private JasperPrint jasperPrint;
    private JRViewer jrViewer;
    private String categForm = "";
    private boolean isReportRunning = false; // Flag to track if report is running
    ReportPrinter printer = new ReportPrinter();
    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    @FXML
    private AnchorPane anchorMain,
            anchorMaster,
            anchorTable,
            anchorDetails;

    @FXML
    private HBox hbButtons;

    @FXML
    private Button btnBrowse,
            btnNew,
            btnSave,
            btnUpdate,
            btnSearch,
            btnPrint,
            btnAddItem,
            btnDelItem,
            btnCancel,
            btnClose,
            btnStatistic,
            btnConfirm;

    @FXML
    private TextField txtField01,
            txtField03,
            txtField04,
            txtField05,
            txtField06,
            txtField07,
            txtField08,
            txtField09,
            txtField10,
            txtField11,
            txtField12,
            txtField13,
            txtField14,
            txtField15;

    @FXML
    private DatePicker dpField01;

    @FXML
    private Label lblStatus;

    @FXML
    private TextArea txtArea01, txtArea02;

    @FXML
    private TableView tblDetails;

    @FXML
    private TableColumn index01,
            index02,
            index03,
            index04,
            index05,
            index06,
            index07,
            index08,
            index09,
            index10,
            index11,
            index12;

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
        btnConfirm.setOnAction(this::handleButtonAction);
        btnPrint.setOnAction(this::handleButtonAction);
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
                        loadItemData();
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
                        ShowMessageFX.Information("Record saved successfully.", "Computerized Accounting System", pxeModuleName);
                        initTrans();
                        clearAllFields();
                        initTabAnchor();
                    } else {
                        ShowMessageFX.Information((String) poJSON.get("message"), "Computerized Accounting System", pxeModuleName);
                        loadItemData();
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
                    loadItemData();
                    initButton(pnEditMode);
                    initTblDetails();
                    initTabAnchor();
                    txtField03.requestFocus();
                    tblDetails.getSelectionModel().select(pnRow + 1);
                    break;

                case "btnBrowse":
                    poJSON = oTrans.searchTransaction("sTransNox", "", false);
                    if ("error".equals((String) poJSON.get("result"))) {
                        ShowMessageFX.Information((String) poJSON.get("message"), "Computerized Accounting System", pxeModuleName);
                        break;
                    }
                    pnEditMode = oTrans.getEditMode();
                    R1data.clear();                    
                    
                    loadTransaction();
                    initTblDetails();
                    loadItemData();
                    initTabAnchor();
                    tblDetails.getSelectionModel().select(0);
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
                    tblDetails.getSelectionModel().select(pnRow + 1);

                    break;

                case "btnDelItem":
                    if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Do you want to remove this item?") == true) {
                        oTrans.RemoveModelDetail(pnRow);
                        pnRow = oTrans.getDetailModel().size() - 1;
                        clearItem();
                        loadItemData();
                        txtField04.requestFocus();
                    }
                    break;
                case "btnCancel":
                    if (ShowMessageFX.YesNo("Do you really want to cancel this record? \nAny data collected will not be kept.", "Computerized Accounting System", pxeModuleName)) {

                        if (pnEditMode == EditMode.UPDATE) {
//                            oTrans.cancelUpdate();
                        }
                        initTrans();
                        initTabAnchor();

                    }
                    break;
                case "btnConfirm":
                    if (ShowMessageFX.YesNo("Are you sure you want to confirm this  record? Please verify the details before proceeding.", "Computerized Accounting System", pxeModuleName)) {

                        poJSON = oTrans.postTransaction(oTrans.getMasterModel().getTransactionNumber());
                            System.out.println(poJSON.toJSONString());
                            if ("error".equals((String) poJSON.get("result"))) {
                                ShowMessageFX.Information((String) poJSON.get("message"), "Computerized Accounting System", pxeModuleName);
                                break;
                            }
                            ShowMessageFX.Information("Transaction successfully confirm.", "Computerized Accounting System", pxeModuleName);
                            clearAllFields();
                            initTrans();
                            initTabAnchor();
                    }
                    break;
                    
                case "btnPrint":
                    if (pnEditMode == 1 && ShowMessageFX.YesNo("Do you want to print this record?", "Computerized Accounting System", pxeModuleName)) {
                        // Check if the report is already running
                        if (!isReportRunning) {
                            loadPrint(); // Call loadPrint only if the report is not running
                        } else {
                            ShowMessageFX.Warning("Report already running.", "Warning", "Please close the existing report before opening a new one.");
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
            {txtField01, txtField03, txtField04,
                txtField05, txtField06, txtField07, txtField08, txtField09,
                txtField10, txtField11, txtField12, txtField13,
                txtField14, txtField15},};

// Loop through each array of TextFields and clear them
        for (TextField[] fields : allFields) {
            for (TextField field : fields) {
                field.clear();
            }
        }
        R1data.clear();
        txtArea01.clear();
        txtArea02.clear();
    }


    /*initialize fields*/
    private void InitTextFields() {
        txtArea01.focusedProperty().addListener(txtArea_Focus);
        txtArea02.focusedProperty().addListener(txtArea_Focus);
// Define arrays for text fields with focusedProperty listeners
        TextField[] focusTextFields = {
            txtField01,
            txtField03,
            txtField04,
            txtField05,
            txtField06,
            txtField07,
            txtField08,
            txtField09,
            txtField10,
            txtField11,
            txtField12,
            txtField13,
            txtField14,
            txtField15};

// Add the listener to each text field in the focusTextFields array
        for (TextField textField : focusTextFields) {
            textField.focusedProperty().addListener(txtField_Focus);
        }

// Define arrays for text fields with setOnKeyPressed handlers
        TextField[] keyPressedTextFields = {txtField03, txtField04, txtField05, txtField08, txtField09,
            txtField11, txtField15
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
                case 2:
                    oTrans.getDetailModel().get(pnRow).setNotes(lsValue);
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
                case 4:/*ITEM DESCRIPTION*/
//                   oTrans.getModel().setBriefDescription(lsValue);
                    System.out.print("ITEM DESCRIPTION == ");
                    break;
                case 5:/*CLASSIFY*/
//                   oTrans.getModel().setDescription(lsValue);
                    System.out.print("CLASSIFY == ");
                    break;
                case 6:/*RSVD ORDER*/
//                   oTrans.getModel().setDescription(lsValue);
                    System.out.print("RSVD ORDER == ");
                    break;

                case 7:/*ON TRANSIT*/
//                   oTrans.getModel().setDescription(lsValue);
                    System.out.print("ON TRANSIT == ");
                    break;

                case 8:/*AMC*/
//                   oTrans.getModel().setDescription(lsValue);
                    System.out.print("AMC == ");
                    break;

                case 9:/*BACK ORDER*/
//                   oTrans.getModel().setDescription(lsValue);
                    System.out.print("BACK ORDER == ");
                    break;

                case 10:/*ON HAND*/
//                   oTrans.getModel().setDescription(lsValue);
                    System.out.print("ON HAND == ");
                    break;

                case 11:/*SERIES ROQ*/
//                   oTrans.getModel().setDescription(lsValue);
                    System.out.print("SERIES ROQ == ");
                    break;

                case 12:/*COLORE ROQ*/
//                   oTrans.getModel().setDescription(lsValue);
                    System.out.print("COLORE ROQ == ");
                    break;

                case 13:/*MODEL ROQ*/
//                   oTrans.getModel().setDescription(lsValue);
                    System.out.print("MODEL ROQ == ");
                    break;

                case 15:/*QTY Cancel*/
                    System.out.println("case 11 == " + lsValue);
                    int qty = (lsValue.isEmpty()) ? 0 : Integer.parseInt(lsValue);
                    oTrans.setDetail(pnRow, "nQuantity", qty);
                    System.out.println("QTY Request == " + oTrans.getDetailModel(pnRow).getQuantity() + "\n");
//                    loadItemData();
                    break;

            }
            loadItemData();
            loadDetails();
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
//                        /*search order no*/
                        poJson = new JSONObject();
//                        poJson =  oTrans.BrowseRequest("sTransNox",lsValue, true);
                        poJson = oTrans.searchMaster(5, lsValue, true);
                        if ("error".equalsIgnoreCase(poJson.get("result").toString())) {
                            ShowMessageFX.Information((String) poJson.get("message"), "Computerized Accounting System", pxeModuleName);
                            break;
                        }
                        System.out.println("order no.x == " + oTrans.getMasterModel().getOrderNumber());
                        txtField.setText(oTrans.getMasterModel().getOrderNumber());
                        txtArea01.requestFocus();
                        break;
                    case 04:/*search barrcode*/
                        poJson = new JSONObject();
                        poJson = oTrans.searchDetail(pnRow, 3, lsValue, true);
                        System.out.println("poJson = " + poJson.toJSONString());
                        if ("error".equalsIgnoreCase(poJson.get("result").toString())) {
                            ShowMessageFX.Information((String) poJson.get("message"), "Computerized Accounting System", pxeModuleName);
                            break;
                        }
                        txtField15.requestFocus();
                        break;
                    case 05:/*search desciption*/
                        poJson = new JSONObject();
                        poJson = oTrans.searchDetail(pnRow, 3, lsValue, false);
                        System.out.println("poJson = " + poJson.toJSONString());
                        if ("error".equalsIgnoreCase(poJson.get("result").toString())) {
                            ShowMessageFX.Information((String) poJson.get("message"), "Computerized Accounting System", pxeModuleName);
                            break;
                        }
                        txtField15.requestFocus();
                        break;
                }
//                loadDetails();
        }
        switch (event.getCode()) {
            case ENTER:
                CommonUtils.SetNextFocus(txtField);
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
        btnCancel.setVisible(!lbShow);
        btnSearch.setVisible(lbShow);
        btnSave.setVisible(lbShow);
        btnAddItem.setVisible(lbShow);
        btnDelItem.setVisible(lbShow);

        btnCancel.setManaged(!lbShow);
        btnSearch.setManaged(lbShow);
        btnSave.setManaged(lbShow);
        btnAddItem.setManaged(lbShow);
        btnDelItem.setManaged(lbShow);

// Manage visibility and managed state of other buttons
        btnBrowse.setVisible(!lbShow);
        btnNew.setVisible(!lbShow);
        btnUpdate.setVisible(!lbShow);
        btnClose.setVisible(!lbShow);
        btnBrowse.setManaged(!lbShow);
        btnNew.setManaged(!lbShow);
        btnUpdate.setManaged(!lbShow);
        btnClose.setManaged(!lbShow);
//        btnVoid.setVisible(!lbShow);
//        btnVoid.setManaged(!lbShow);
        btnConfirm.setVisible(!lbShow);
        btnConfirm.setManaged(!lbShow);
        
        btnNew.setVisible(false);
        btnNew.setManaged(false);
        btnUpdate.setVisible(false);
        btnUpdate.setManaged(false);
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
        index12.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");

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
        index12.setCellValueFactory(new PropertyValueFactory<>("index12"));

        tblDetails.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblDetails.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });
        tblDetails.setItems(R1data);
        tblDetails.autosize();
    }

    private void loadTransaction() {
        if (pnEditMode == EditMode.READY
                || pnEditMode == EditMode.ADDNEW
                || pnEditMode == EditMode.UPDATE) {

            txtField01.setText((String) oTrans.getMasterModel().getTransactionNumber());
            txtArea01.setText((String) oTrans.getMasterModel().getRemarks());
            txtField03.setText((String) oTrans.getMasterModel().getOrderNumber());
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
            System.out.println("getClassify = " + String.valueOf(oTrans.getDetailModel().get(pnRow).getClassify()));
//            txtField03.setText((String) oTrans.getDetailModel().get(pnRow).getOrderNumber());
            txtField04.setText((String) oTrans.getDetailModel().get(pnRow).getBarcode());
            txtField05.setText((String) oTrans.getDetailModel().get(pnRow).getDescription());
            txtField06.setText(oTrans.getDetailModel().get(pnRow).getClassify());
            txtField07.setText(String.valueOf(oTrans.getDetailModel().get(pnRow).getReservedOrder()));
            txtField08.setText(String.valueOf(oTrans.getDetailModel().get(pnRow).getOnTransit()));
            txtField09.setText(String.valueOf(oTrans.getDetailModel().get(pnRow).getAverageMonthlySalary()));
            txtField10.setText(String.valueOf(oTrans.getDetailModel().get(pnRow).getBackOrder()));
            txtField11.setText(String.valueOf(oTrans.getDetailModel().get(pnRow).getQuantityOnHand()));
            txtField12.setText(String.valueOf(oTrans.getDetailModel().get(pnRow).getQuantityRequest()));
            txtField13.setText(String.valueOf(oTrans.getDetailModel().get(pnRow).getIssueQuantity()));
            txtField14.setText(String.valueOf(oTrans.getDetailModel().get(pnRow).getOrderQuantity()));
            txtField15.setText(String.valueOf(oTrans.getDetailModel().get(pnRow).getQuantity()));
            txtArea02.setText((String) oTrans.getDetailModel().get(pnRow).getNotes());
//            txtField15.setText(String.valueOf(oTrans.getDetailModel().get(pnRow).getQuantity()));
        }
    }

    private void loadItemData() {
        int lnCtr;
        R1data.clear();
        if (oTrans.getDetailModel() != null) {
            for (lnCtr = 0; lnCtr < oTrans.getDetailModel().size(); lnCtr++) {
//            oTrans.getDetailModel().get(lnCtr).list();
                R1data.add(new ModelStockRequest(String.valueOf(lnCtr + 1),
                        (String) oTrans.getDetailModel().get(lnCtr).getOrderNumber(),
                        (String) oTrans.getDetailModel().get(lnCtr).getBarcode(),
                        (String) oTrans.getDetailModel().get(lnCtr).getDescription(),
                        (oTrans.getDetailModel().get(lnCtr).getBrandName() == null ? "" : oTrans.getDetailModel().get(lnCtr).getBrandName()),
                        (oTrans.getDetailModel().get(lnCtr).getModelName() == null ? "" : oTrans.getDetailModel().get(lnCtr).getModelName()),
                        (oTrans.getDetailModel().get(lnCtr).getModelDescription() == null ? "" : oTrans.getDetailModel().get(lnCtr).getModelDescription()),
                        "",
                        "",
                        (oTrans.getDetailModel().get(lnCtr).getColorName() == null ? "" : oTrans.getDetailModel().get(lnCtr).getColorName()),
                        String.valueOf(oTrans.getDetailModel().get(lnCtr).getUnserve()),
                        String.valueOf(oTrans.getDetailModel().get(lnCtr).getQuantity())));
            }
        }
    }

    @FXML
    private void tblDetails_Clicked(MouseEvent event) {
        if (tblDetails.getSelectionModel().getSelectedIndex() >= 0) {
            pnRow = tblDetails.getSelectionModel().getSelectedIndex();
            System.out.println("pnRow = " + pnRow);
            loadDetails();
            txtField15.requestFocus();
        }
        tblDetails.setOnKeyReleased((KeyEvent t) -> {
            KeyCode key = t.getCode();
            switch (key) {
                case DOWN:
                    pnRow = tblDetails.getSelectionModel().getSelectedIndex();
                    if (pnRow == tblDetails.getItems().size()) {
                        pnRow = tblDetails.getItems().size();
                        loadDetails();
                    } else {
                        loadDetails();
                    }
                    break;
                case UP:
                    int pnRows = 0;
                    int x = 1;
                    pnRow = tblDetails.getSelectionModel().getSelectedIndex();

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
            {txtField04, txtField04, txtField05, txtField06, txtField07,
                txtField08, txtField09, txtField10, txtField11, txtField12,
                txtField13, txtField14, txtField15},};

        for (TextField[] fields : allFields) {
            for (TextField field : fields) {
                field.clear();
            }
        }
    }
    private void initTrans() {
        Properties po_props = new Properties();
        clearAllFields();
        oTrans = new InvRequestCancel(oApp, false);
        String industry = System.getProperty("store.inventory.industry");

        RequestControllerFactory.RequestType types = null;
        String[] category = industry.split(";");
// Print the resulting array
        for (String type : category) {
            if (types == null) {
                if ("0001".equals(type)) {
                    types = RequestControllerFactory.RequestType.MC;
                    categForm = " MC" ;
                    oTrans.setType(types);
                } else if ("0002".equals(type)) {
                    types = RequestControllerFactory.RequestType.MPUnits;
                    categForm = " MP" ;
                    oTrans.setType(types);
                }else if ("0003".equals(type)) {
                    types = RequestControllerFactory.RequestType.AUTO;
                    categForm = " AUTO";
                    oTrans.setType(types);
                }

                System.out.println("type value = " + types);
            }
        }
        oTrans.setCategoryType(RequestControllerFactory.RequestCategoryType.WITHOUT_ROQ);
        oTrans.setTransactionStatus("0123");
        oTrans.setWithUI(true);
        oTrans.isHistory(true);
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
//        params.put("sReportDt", CommonUtils.xsDateLong(oApp.getServerDate()));
        params.put("sReportNm", pxeModuleName);
        params.put("sReportDt", CommonUtils.xsDateMedium((Date) oApp.getServerDate()));
        params.put("sBranchNm", oApp.getBranchName());
        params.put("sAddressx", oApp.getAddress());
        params.put("sTransNox", oTrans.getMasterModel().getTransactionNumber());
        params.put("sTranDte", CommonUtils.xsDateMedium((Date) oTrans.getMasterModel().getTransaction()));
        params.put("sRemarks", oTrans.getMasterModel().getRemarks());
        params.put("status", oTrans.getMasterModel().getTransactionStatus());
//        params.put("sTranType", "Unprcd Qty");
//        params.put("sTranQty", "Cancel");

        // Define report file paths
        String sourceFileName = "D://GGC_Maven_Systems/Reports/InventoryRequestCancellation.jasper";
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(R1data);

        return printer.loadAndShowReport(sourceFileName, params, R1data, pxeModuleName);
    }
}
