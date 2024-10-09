package org.guanzon.cas.controller;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import java.math.BigDecimal;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import static javafx.scene.input.KeyCode.DOWN;
import static javafx.scene.input.KeyCode.ENTER;
import static javafx.scene.input.KeyCode.F3;
import static javafx.scene.input.KeyCode.UP;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.base.SQLUtil;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.cas.inventory.base.Inventory;
import org.guanzon.cas.model.ModelPOQuotation;
import org.guanzon.cas.inventory.base.PO_Quotation;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author Luke
 */
public class PO_QuotationController implements Initializable, ScreenInterface {
    
    private final String pxeModuleName = "Purchase Quotation Request";
    private GRider oApp;
    private PO_Quotation oTrans;
    private JSONObject poJSON;
    private int pnEditMode;

    private String psPrimary = "";

    private boolean pbLoaded = false;
    private int pnIndex;
    private int pnDetailRow;
    
    
    
    @FXML
    private AnchorPane MainAnchorPane, apBrowse, apButton, apMaster, apDetail, apTable;
    @FXML
    private HBox hbButtons;
    @FXML
    private Button btnBrowse, btnSearch, btnNew, btnUpdate, btnConfirm, btnVoid, 
                   btnSave, btnDelDetail, btnCancel, btnClose;
    @FXML
    private Label lblStatus;
    @FXML
    private CheckBox cVatAdd;
    @FXML
    private TextField txtField01, txtField02, txtField03, txtField04, txtField05, 
                      txtField06, txtField07, txtField08, txtField09, txtField10, 
                      txtField12, txtField13, txtField14, txtField15, txtField16, 
                      txtField17, txtField18, txtField19, txtField98, txtField99,

                      txtDetail01, txtDetail02, txtDetail03, txtDetail04, txtDetail05, 
                      txtDetail06, txtDetail07, txtDetail08, txtDetail09, txtDetail10, 
                      txtDetail11;
    @FXML
    private TextArea txtField11;
    @FXML
    private TableView tblDetails;
    @FXML
    private TableColumn index01, index02, index03, index04, index05, index06, index07, index08;
    
    private ObservableList<ModelPOQuotation> data = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        oTrans = new PO_Quotation(oApp, false);
        oTrans.setTransactionStatus("12340");

        initTextFields();
        initDetailsGrid();
        clearFields();

        pnEditMode = EditMode.UNKNOWN;
        initButton(pnEditMode);
        pbLoaded = true;
        
    }   
    
    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }
    
    
    @FXML
    void cmdButton_Click(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();
        boolean isVatAdded = cVatAdd.isSelected();
        switch (lsButton) {
            
            case "btnNew":
                poJSON = oTrans.newTransaction();
                loadRecord();
                pnEditMode = oTrans.getMasterModel().getEditMode();
                oTrans.setTransactionStatus("12340");
                if ("error".equals((String) poJSON.get("result"))) {
                    System.err.println((String) poJSON.get("message"));

                    pnEditMode = EditMode.UNKNOWN;
                    return;
                }
                break;

            case "btnSave":
                
                poJSON = oTrans.getMasterModel().setRemarks(txtField11.getText());
                if ("error".equals((String) poJSON.get("result"))) {
                    System.err.println((String) poJSON.get("message"));

                    pnEditMode = EditMode.UNKNOWN;
                    return;
                }
                
                
                if (isVatAdded = true){
                    poJSON = oTrans.getMasterModel().setVATAdded("1");
                } else{
                    poJSON = oTrans.getMasterModel().setVATAdded("0");
                }
                if ("error".equals((String) poJSON.get("result"))) {
                    System.err.println((String) poJSON.get("message"));

                    pnEditMode = EditMode.UNKNOWN;
                    return;
                }
                
                
                String formattedDate = LocalDate.parse(txtField04.getText(), 
                DateTimeFormatter.ofPattern("MMMM dd, yyyy"))
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                
                Date date = java.sql.Date.valueOf(formattedDate);
                
                poJSON = oTrans.getMasterModel().setReferenceDate(date);
                if ("error".equals((String) poJSON.get("result"))) {
                    System.err.println((String) poJSON.get("message"));
                    ShowMessageFX.Information(null, pxeModuleName, (String) poJSON.get("message"));
                    pnEditMode = EditMode.UNKNOWN;
                    return;
                }
                
                poJSON = oTrans.getMasterModel().setGrossAmount(Double.parseDouble(txtField15.getText()));
                if ("error".equals((String) poJSON.get("result"))) {
                    System.err.println((String) poJSON.get("message"));
                    ShowMessageFX.Information(null, pxeModuleName, (String) poJSON.get("message"));
                    pnEditMode = EditMode.UNKNOWN;
                    return;
                }
                
                poJSON = oTrans.getMasterModel().setDiscount(Double.parseDouble(txtField17.getText()));
                if ("error".equals((String) poJSON.get("result"))) {
                    System.err.println((String) poJSON.get("message"));
                    ShowMessageFX.Information(null, pxeModuleName, (String) poJSON.get("message"));
                    pnEditMode = EditMode.UNKNOWN;
                    return;
                }
                
                poJSON = oTrans.getMasterModel().setAddDiscx(Double.parseDouble(txtField18.getText()));
                if ("error".equals((String) poJSON.get("result"))) {
                    System.err.println((String) poJSON.get("message"));
                    ShowMessageFX.Information(null, pxeModuleName, (String) poJSON.get("message"));
                    pnEditMode = EditMode.UNKNOWN;
                    return;
                }
                
                poJSON = oTrans.getMasterModel().setVatAmtxx(Double.parseDouble(txtField12.getText()));
                if ("error".equals((String) poJSON.get("result"))) {
                    System.err.println((String) poJSON.get("message"));
                    ShowMessageFX.Information(null, pxeModuleName, (String) poJSON.get("message"));
                    pnEditMode = EditMode.UNKNOWN;
                    return;
                }
                
                poJSON = oTrans.getMasterModel().setVatRatex(Double.parseDouble(txtField13.getText()));
                if ("error".equals((String) poJSON.get("result"))) {
                    System.err.println((String) poJSON.get("message"));
                    ShowMessageFX.Information(null, pxeModuleName, (String) poJSON.get("message"));
                    pnEditMode = EditMode.UNKNOWN;
                    return;
                }
                
                poJSON = oTrans.getMasterModel().setFreightx(Double.parseDouble(txtField19.getText()));
                if ("error".equals((String) poJSON.get("result"))) {
                    System.err.println((String) poJSON.get("message"));
                    ShowMessageFX.Information(null, pxeModuleName, (String) poJSON.get("message"));
                    pnEditMode = EditMode.UNKNOWN;
                    return;
                }
                
                poJSON = oTrans.getMasterModel().setTransactionTotal(Double.parseDouble(txtField14.getText()));
                if ("error".equals((String) poJSON.get("result"))) {
                    System.err.println((String) poJSON.get("message"));
                    ShowMessageFX.Information(null, pxeModuleName, (String) poJSON.get("message"));
                    pnEditMode = EditMode.UNKNOWN;
                    return;
                }
                
                
                poJSON = oTrans.saveTransaction(); 

                pnEditMode = oTrans.getMasterModel().getEditMode();
                if ("error".equals((String) poJSON.get("result"))) {
                    System.err.println((String) poJSON.get("message"));
                    ShowMessageFX.Information(null, pxeModuleName, (String) poJSON.get("message"));
                    pnEditMode = EditMode.UNKNOWN;
                    return;

                } else {
                    oTrans = new PO_Quotation(oApp, true);
                    pbLoaded = true;
                    oTrans.setTransactionStatus("12340");
                    pnEditMode = EditMode.UNKNOWN;
                    clearFields();
                    ShowMessageFX.Information(null, pxeModuleName, "Transaction successful Saved!");
                }
                break;

            case "btnUpdate":
                poJSON = oTrans.updateTransaction();

                pnEditMode = oTrans.getMasterModel().getEditMode();
                if ("error".equals((String) poJSON.get("result"))) {
                    System.err.println((String) poJSON.get("message"));
                    ShowMessageFX.Information(null, pxeModuleName, (String) poJSON.get("message"));
                    pnEditMode = EditMode.UNKNOWN;
                    return;
                }
                break;

            case "btnCancel":
                if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Do you want to disregard changes?") == true) {
                    oTrans = new PO_Quotation(oApp, true);
                    oTrans.setTransactionStatus("12340");
                    pbLoaded = true;
                    pnEditMode = EditMode.UNKNOWN;
                    clearFields();
                    break;
                } else {
                    return;
                }

            case "btnConfirm":
                if (!psPrimary.isEmpty()) {
                    if (btnConfirm.getText().equals("Activate")) {
                        if (ShowMessageFX.YesNo(null, pxeModuleName, "Do you want to Activate this Parameter?") == true) {
                            poJSON = oTrans.postTransaction(psPrimary);
                            if ("error".equals((String) poJSON.get("result"))) {
                                System.err.println((String) poJSON.get("message"));
                                
                                return;
                            } else {
                                clearFields();
                                pnEditMode = EditMode.UNKNOWN;
                                initButton(pnEditMode);
                                oTrans = new PO_Quotation(oApp, false);
                                oTrans.setTransactionStatus("12340");
                                pbLoaded = true;

                            }
                        } else {
                            return;
                        }
                    } else {
                        if (ShowMessageFX.YesNo(null, pxeModuleName, "Do you want to Deactivate this Parameter?") == true) {
                            poJSON = oTrans.cancelTransaction(psPrimary);
                            if ("error".equals((String) poJSON.get("result"))) {
                                System.err.println((String) poJSON.get("message"));
                                ShowMessageFX.Information(null, pxeModuleName, (String) poJSON.get("message"));
                                return;
                            } else {
                                clearFields();
                                pnEditMode = EditMode.UNKNOWN;
                                initButton(pnEditMode);
                                oTrans = new PO_Quotation(oApp, false);
                                oTrans.setTransactionStatus("12340");
                                pbLoaded = true;

                            }
                        } else {
                            return;
                        }
                    }
                } else {
                    ShowMessageFX.Warning(null, pxeModuleName, "Please select a record to confirm!");
                }
                break;

            case "btnClose":
                unloadForm appUnload = new unloadForm();
                if (ShowMessageFX.OkayCancel(null, "Close Tab", "Are you sure you want to close this Tab?") == true) {
                    appUnload.unloadForm(MainAnchorPane, oApp, pxeModuleName);

                } else {
                    return;
                }

                break;

            case "btnBrowse":
                if (pnIndex < 98) {
                    pnIndex = 99;
                }
                poJSON = oTrans.searchTransaction("sTransNox", txtField01.getText(), pnIndex == 99);
                pnEditMode = EditMode.READY;
                if ("error".equalsIgnoreCase(poJSON.get("result").toString())) {

                    ShowMessageFX.Information((String) poJSON.get("message"), "Computerized Acounting System", pxeModuleName);
                    txtField01.requestFocus();
                    pnEditMode = EditMode.UNKNOWN;
                    return;
                } else {
                    loadRecord();
                }
                break;

            case "btnSearch":
                if (pnIndex > 3 || pnIndex < 1) {
                    pnIndex = 1;
                }
                switch (pnIndex) {
                    case 1:
                    case 2:
                        /* Barcode & Description */
                        poJSON = oTrans.searchDetail(pnDetailRow, 3, (pnIndex == 1) ? txtDetail01.getText() : "", pnIndex == 1);
//                        System.out.println("poJson Result = " + poJSON.toJSONString());
                        if ("error".equalsIgnoreCase(poJSON.get("result").toString())) {
                            ShowMessageFX.Information((String) poJSON.get("message"), "Computerized Acounting System", pxeModuleName);
                        }

                        loadTableDetail();
                        break;
                }
                break;

            case "btnDelDetail":
                oTrans.RemoveModelDetail(pnDetailRow);
                loadTableDetail();

                break;

            default:
                ShowMessageFX.Warning(null, pxeModuleName, "Button with name " + lsButton + " not registered.");
                return;
        }

        initButton(pnEditMode);

    }

    @FXML
    void tblDetails_Clicked(MouseEvent event) {
        pnDetailRow = tblDetails.getSelectionModel().getSelectedIndex();
        if (pnDetailRow >= 0) {
            setSelectedDetail();
        }
    }
    
    public void initDetailsGrid() {
        index01.setStyle("-fx-alignment: CENTER;");
        index02.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        index03.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        index04.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        index05.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        index06.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        index07.setStyle("-fx-alignment: CENTER-RIGHT;-fx-padding: 0 5 0 0;");
        index08.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");

        index01.setCellValueFactory(new PropertyValueFactory<ModelPOQuotation, String>("index01"));
        index02.setCellValueFactory(new PropertyValueFactory<ModelPOQuotation, String>("index02"));
        index03.setCellValueFactory(new PropertyValueFactory<ModelPOQuotation, String>("index03"));
        index04.setCellValueFactory(new PropertyValueFactory<ModelPOQuotation, String>("index04"));
        index05.setCellValueFactory(new PropertyValueFactory<ModelPOQuotation, String>("index05"));
        index06.setCellValueFactory(new PropertyValueFactory<ModelPOQuotation, String>("index06"));
        index07.setCellValueFactory(new PropertyValueFactory<ModelPOQuotation, String>("index07"));
        index08.setCellValueFactory(new PropertyValueFactory<ModelPOQuotation, String>("index08"));

        tblDetails.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblDetails.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });

        tblDetails.setItems(data);

    }
    
    private void initButton(int fnValue) {
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);

        btnSearch.setVisible(lbShow);
        btnSave.setVisible(lbShow);
        btnDelDetail.setVisible(lbShow);
        btnCancel.setVisible(lbShow);

        btnSearch.setManaged(lbShow);
        btnSave.setManaged(lbShow);
        btnDelDetail.setManaged(lbShow);
        btnCancel.setManaged(lbShow);

        btnBrowse.setManaged(!lbShow);
        btnNew.setManaged(!lbShow);
        btnUpdate.setManaged(!lbShow);
        btnConfirm.setManaged(!lbShow);
        btnVoid.setManaged(!lbShow);
        btnClose.setManaged(!lbShow);

        btnBrowse.setVisible(!lbShow);
        btnNew.setVisible(!lbShow);
        btnUpdate.setVisible(!lbShow);
        btnConfirm.setVisible(!lbShow);
        btnVoid.setVisible(!lbShow);
        btnClose.setVisible(!lbShow);

        apBrowse.setDisable(lbShow);
        apMaster.setDisable(!lbShow);
        apDetail.setDisable(!lbShow);
//        FieldsManualEdit(lbShow);
        apTable.setDisable(!lbShow);

    }
    
    private void initTextFields() {
        /*textFields FOCUSED PROPERTY*/
        txtField01.focusedProperty().addListener(txtField_Focus);
        txtField02.focusedProperty().addListener(txtField_Focus);
        txtField03.focusedProperty().addListener(txtField_Focus);
        txtField04.focusedProperty().addListener(txtField_Focus);
        txtField05.focusedProperty().addListener(txtField_Focus);
        txtField06.focusedProperty().addListener(txtField_Focus);
        txtField07.focusedProperty().addListener(txtField_Focus);
        txtField08.focusedProperty().addListener(txtField_Focus);
        txtField09.focusedProperty().addListener(txtField_Focus);
        txtField10.focusedProperty().addListener(txtField_Focus);
        txtField12.focusedProperty().addListener(txtField_Focus);
        txtField13.focusedProperty().addListener(txtField_Focus);
        txtField99.focusedProperty().addListener(txtField_Focus);
        txtField98.focusedProperty().addListener(txtField_Focus);
        txtField11.focusedProperty().addListener(txtArea_Focus);
        txtField14.focusedProperty().addListener(txtField_Focus);
        txtField15.focusedProperty().addListener(txtField_Focus);
        txtField16.focusedProperty().addListener(txtField_Focus);
        txtField17.focusedProperty().addListener(txtField_Focus);
        txtField18.focusedProperty().addListener(txtField_Focus);
        txtField19.focusedProperty().addListener(txtField_Focus);

        txtDetail01.focusedProperty().addListener(txtDetail_Focus);
        txtDetail02.focusedProperty().addListener(txtDetail_Focus);
        txtDetail03.focusedProperty().addListener(txtDetail_Focus);
        txtDetail04.focusedProperty().addListener(txtDetail_Focus);
        txtDetail05.focusedProperty().addListener(txtDetail_Focus);
        txtDetail06.focusedProperty().addListener(txtDetail_Focus);
        txtDetail07.focusedProperty().addListener(txtDetail_Focus);
//        txtDetail08.focusedProperty().addListener(txtDetail_Focus);
//        txtDetail09.focusedProperty().addListener(txtDetail_Focus);
//        txtDetail10.focusedProperty().addListener(txtDetail_Focus);
//        txtDetail11.focusedProperty().addListener(txtDetail_Focus);

        /*textFields KeyPressed PROPERTY*/
        txtField01.setOnKeyPressed(this::txtField_KeyPressed);
        txtField03.setOnKeyPressed(this::txtField_KeyPressed);//supplier
        txtField06.setOnKeyPressed(this::txtField_KeyPressed);//Categ
        txtField10.setOnKeyPressed(this::txtField_KeyPressed);//term

        txtField99.setOnKeyPressed(this::txtField_KeyPressed);
        txtField98.setOnKeyPressed(this::txtField_KeyPressed);

        txtDetail01.setOnKeyPressed(this::txtDetail_KeyPressed);//barcode
        txtDetail02.setOnKeyPressed(this::txtDetail_KeyPressed);
//        txtDetail02.setOnKeyPressed(this::txtDetail_KeyPressed);//description for clarification
//        txtField10.setOnKeyPressed(this::txtField_KeyPressed);

    }
    
    private void txtField_KeyPressed(KeyEvent event) {

        TextField textField = (TextField) event.getSource();
        int lnIndex = Integer.parseInt(((TextField) event.getSource()).getId().substring(8, 10));
        String lsValue = textField.getText();
        switch (event.getCode()) {
            case F3:
                switch (lnIndex) {
                    case 98:/*Browse Destination*/
                    case 99:/*Browse Primary*/
                        poJSON = oTrans.searchTransaction("sTransNox", lsValue, lnIndex == 99);
                        if ("error".equalsIgnoreCase(poJSON.get("result").toString())) {

                            ShowMessageFX.Information((String) poJSON.get("message"), "Computerized Acounting System", pxeModuleName);
                            txtField01.requestFocus();
                        } else {
                            loadRecord();
                        }
                        break;

                    case 3:
                        /*sSupplier*/
                        poJSON = oTrans.searchMaster(3, lsValue, false);
                        if ("error".equalsIgnoreCase(poJSON.get("result").toString())) {

                            ShowMessageFX.Information((String) poJSON.get("message"), "Computerized Acounting System", pxeModuleName);
                            ShowMessageFX.Information(null, pxeModuleName, (String) poJSON.get("message"));
                            txtField01.requestFocus();
                        } else {
                            loadRecord();
                        }
                        break;
                        
                    case 6:/*sCategCd*/
                        poJSON = oTrans.searchMaster(21, lsValue, false);
                        if ("error".equalsIgnoreCase(poJSON.get("result").toString())) {

                            ShowMessageFX.Information((String) poJSON.get("message"), "Computerized Acounting System", pxeModuleName);
                            ShowMessageFX.Information(null, pxeModuleName, (String) poJSON.get("message"));
                            txtField01.requestFocus();
                        } else {
                            loadRecord();
                        }
                        break;
                        
                    case 10:/*sTermCode*/
                        poJSON = oTrans.searchMaster(8, lsValue, false);
                        if ("error".equalsIgnoreCase(poJSON.get("result").toString())) {

                            ShowMessageFX.Information((String) poJSON.get("message"), "Computerized Acounting System", pxeModuleName);
                            ShowMessageFX.Information(null, pxeModuleName, (String) poJSON.get("message"));
                            txtField01.requestFocus();
                        } else {
                            loadRecord();
                        }
                        break;

                }
                break;
        }
        switch (event.getCode()) {
            case ENTER:
                break;
            case DOWN:
                CommonUtils.SetNextFocus(textField);
                break;
            case UP:
                CommonUtils.SetPreviousFocus(textField);
        }

        pnIndex = lnIndex;
    }

    private void txtDetail_KeyPressed(KeyEvent event) {

        TextField textField = (TextField) event.getSource();
        int lnIndex = Integer.parseInt(((TextField) event.getSource()).getId().substring(9, 11));
        String lsValue = textField.getText();
        switch (event.getCode()) {
            case F3:
                switch (lnIndex) {
                    case 1:
                    case 2:
                        /* Barcode & Description */
                        poJSON = oTrans.searchDetail(pnDetailRow, 3, lsValue, lnIndex == 1);
//                        System.out.println("poJson Result = " + poJSON.toJSONString());
                        if ("error".equalsIgnoreCase(poJSON.get("result").toString())) {
                            ShowMessageFX.Information((String) poJSON.get("message"), "Computerized Acounting System", pxeModuleName);
                        }
                        loadTableDetail();
                        break;

                }
                loadTableDetail();
                break;
        }
        switch (event.getCode()) {
            case ENTER:
                break;
            case DOWN:
                CommonUtils.SetNextFocus(textField);
                break;
            case UP:
                CommonUtils.SetPreviousFocus(textField);
        }

        pnIndex = lnIndex;
    }
    
    private void loadRecord() {
        String lsActive = oTrans.getMasterModel().getTransactionStatus();
        
        switch (lsActive) {
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
            default:
                lblStatus.setText("UNKNOWN");
                break;

        }
        
        
        
        psPrimary = oTrans.getMasterModel().getTransactionNumber();
        txtField01.setText(psPrimary);
        
        
//        String test = CommonUtils.xsDateLong(oTrans.getMasterModel().getTransaction()) + "";
        txtField02.setText(CommonUtils.xsDateLong(oTrans.getMasterModel().getTransaction()));

        

        txtField03.setText(oTrans.getMasterModel().getSupplierName());
//        txtField05.setText(oTrans.getMasterModel().getAddress());
//        txtField07.setText(oTrans.getMasterModel().getContactNo());
        
        
        txtField04.setText(CommonUtils.xsDateLong(oTrans.getMasterModel().getModifiedDate()));

        txtField09.setText(CommonUtils.xsDateLong(oTrans.getMasterModel().getValidity()));

        
        
        txtField08.setText(oTrans.getMasterModel().getReferenceNumber());
        txtField06.setText(oTrans.getMasterModel().getCategoryName());
        txtField10.setText(oTrans.getMasterModel().getTermName());
        txtField11.setText(oTrans.getMasterModel().getRemarks());
        
        
        txtField12.setText(oTrans.getMasterModel().getVatAmtxx().toString());

        txtField13.setText(oTrans.getMasterModel().getVatRatex().toString());
        txtField17.setText(oTrans.getMasterModel().getDiscount().toString());
        txtField18.setText(oTrans.getMasterModel().getAddDiscx().toString());
        txtField19.setText(oTrans.getMasterModel().getFreightx().toString());
        txtField16.setText(oTrans.getMasterModel().getTWithHld().toString());
        txtField14.setText(oTrans.getMasterModel().getTransactionTotal().toString());
        txtField15.setText(oTrans.getMasterModel().getGrossAmount().toString());
        
        int cvataddedx = Integer.parseInt(oTrans.getMasterModel().getVATAdded());
        if (cvataddedx == 1){
            cVatAdd.setSelected(true);
        } else if(cvataddedx == 0){
            cVatAdd.setSelected(false);
        }

        loadTableDetail();

    }
    
    private void clearFields() {
        txtField01.clear();
        txtField02.clear();
        txtField03.clear();
        txtField04.clear();
        txtField05.clear();
        txtField06.clear();
        txtField07.clear();
        txtField08.clear();
        txtField09.clear();
        txtField10.clear();
        txtField11.clear();
        txtField12.clear();
        txtField13.clear();
        txtField14.clear();
        txtField15.clear();
        txtField16.clear();
        txtField17.clear();
        txtField18.clear();
        txtField19.clear();

        txtField99.clear();
        txtField98.clear();

        txtDetail01.clear();
        txtDetail02.clear();
        txtDetail03.clear();
        txtDetail04.clear();
        txtDetail05.clear();
        txtDetail06.clear();
        txtDetail07.clear();

        psPrimary = "";
        lblStatus.setText("UNKNOWN");

        pnDetailRow = -1;
        pnIndex = -1;

        data.clear();

    }
    
    private void loadTableDetail() {
        int lnCtr;
        data.clear();

        int lnItem = oTrans.getItemCount();
        if (lnItem < 0) {
            return;
        }
        Inventory loInventory;
        for (lnCtr = 0; lnCtr <= lnItem - 1; lnCtr++) {
            String lsStockIDx = (String) oTrans.getDetailModel(lnCtr).getValue("sStockIDx");

            if (lsStockIDx != null && !lsStockIDx.equals("")) {
                loInventory = oTrans.GetInventory(lsStockIDx, true);
                BigDecimal nUnitPrce = (BigDecimal) loInventory.getMaster("nUnitPrce");
                data.add(new ModelPOQuotation(String.valueOf(lnCtr + 1),
                        (String) loInventory.getMaster("sBarCodex"),
                        (String) loInventory.getMaster("sDescript"),
                        oTrans.getDetailModel(lnCtr).getValue("nDiscAmtx").toString(),
                        oTrans.getDetailModel(lnCtr).getValue("nDiscRate").toString(),
                        (String) loInventory.getMaster("xInvTypNm"),
                        oTrans.getDetailModel(lnCtr).getValue("nQuantity").toString(),
                        nUnitPrce.toPlainString()));

                //display fetched detail on console
                System.out.println("\nNo == " + String.valueOf(lnCtr + 1));
                System.out.println("\nsBarCodex == " + (String) loInventory.getMaster("sBarCodex"));
                System.out.println("\nsDescript == " + (String) loInventory.getMaster("sDescript"));
                System.out.println("\nnDiscAmtx == " + oTrans.getDetailModel(lnCtr).getValue("nDiscAmtx").toString());
                System.out.println("\nnDiscRate == " + oTrans.getDetailModel(lnCtr).getValue("nDiscRate").toString());
                System.out.println("\nxInvTypNm == " + (String) loInventory.getMaster("xInvTypNm"));
                System.out.println("\nQuantity == " + oTrans.getDetailModel(lnCtr).getValue("nQuantity").toString());
                System.out.println("\nnUnitPrce == " + oTrans.getDetailModel(lnCtr).getValue("nUnitPrce").toString());
                
            } else {
                data.add(new ModelPOQuotation(String.valueOf(lnCtr + 1),
                        "",
                        (String) oTrans.getDetailModel(lnCtr).getValue("sDescript"),
                        oTrans.getDetailModel(lnCtr).getValue("nDiscAmtx").toString(),
                        oTrans.getDetailModel(lnCtr).getValue("nDiscRate").toString(),
                        "",
                        oTrans.getDetailModel(lnCtr).getValue("nQuantity").toString(),
                        oTrans.getDetailModel(lnCtr).getValue("nUnitPrce").toString() ));
                
            }

        }
        /*FOCUS ON FIRST ROW*/
        if (pnDetailRow < 0 || pnDetailRow >= data.size()) {
            if (!data.isEmpty()) {
                /* FOCUS ON FIRST ROW */
                tblDetails.getSelectionModel().select(0);
                tblDetails.getFocusModel().focus(0);
                pnDetailRow = tblDetails.getSelectionModel().getSelectedIndex();
            }
            setSelectedDetail();
        } else {
            /* FOCUS ON THE ROW THAT pnRowDetail POINTS TO */
            tblDetails.getSelectionModel().select(pnDetailRow);
            tblDetails.getFocusModel().focus(pnDetailRow);
            setSelectedDetail();
        }
        initDetailsGrid();
    }
    
    private void setSelectedDetail() {
        txtDetail01.setText((String) data.get(pnDetailRow).getIndex02());
        txtDetail02.setText((String) data.get(pnDetailRow).getIndex03());
//        txtDetail05.setText((String) data.get(pnDetailRow).getIndex04());
//        txtDetail09.setText((String) oTrans.getDetailModel(pnDetailRow).getValue("xCategrNm"));
//        txtDetail10.setText((String) oTrans.getDetailModel(pnDetailRow).getValue("xCategrNm"));
        txtDetail05.setText((String) data.get(pnDetailRow).getIndex04());
        txtDetail06.setText((String) data.get(pnDetailRow).getIndex05());
        txtDetail04.setText((String) data.get(pnDetailRow).getIndex07());
        txtDetail07.setText((String) data.get(pnDetailRow).getIndex08());

        txtDetail03.setText((String) oTrans.getDetailModel(pnDetailRow).getValue("xCategrNm"));

    }

    public void loadResult(String fsValue, boolean fbVal) {
        JSONObject poJson = new JSONObject();
//        overlay.setVisible(fbVal);
        poJson = oTrans.openTransaction(fsValue);
        if ("error".equalsIgnoreCase(poJson.get("result").toString())) {
            ShowMessageFX.Information((String) poJson.get("message"), "Computerized Acounting System", pxeModuleName);
        }
        initButton(pnEditMode);
        loadRecord();

    }
    
    public void saveUnitPrice(String stockid) {
        String nUnitPrice = (String) oTrans.getDetailModel(pnDetailRow).getUnitPrice();
        String lsValue = txtDetail07.getText();
        if (nUnitPrice == null || nUnitPrice.toString() == "") {
            poJSON = oTrans.setDetail(pnDetailRow, "nUnitPrce", (Double.parseDouble(lsValue)));
            System.out.println( "wat " + nUnitPrice);
            if ("error".equals((String) poJSON.get("result"))) {
                System.err.println((String) poJSON.get("message"));
            }
        }
    }
    
    ///////////////////////////////////////////////////////////////////
    
    final ChangeListener<? super Boolean> txtField_Focus = (o, ov, nv) -> {
        if (!pbLoaded) {
            return;
        }

        TextField txtField = (TextField) ((ReadOnlyBooleanPropertyBase) o).getBean();
        int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
        String lsValue = txtField.getText();

        if (lsValue == null) {
            return;
        }

        if (!nv) {
            /*Lost Focus*/
            switch (lnIndex) {
                case 2://dtransact
                    poJSON = oTrans.getMasterModel().setTransaction(SQLUtil.toDate(lsValue, "yyyy-MM-dd"));
                    if ("error".equals((String) poJSON.get("result"))) {
                        System.err.println((String) poJSON.get("message"));
                        ShowMessageFX.Information(null, pxeModuleName, (String) poJSON.get("message"));
                        return;
                    }
                    txtField.setText(CommonUtils.xsDateLong(oTrans.getMasterModel().getTransaction()));
                    break;
                    
                case 4://dReferDte
                    poJSON = oTrans.getMasterModel().setReferenceDate(SQLUtil.toDate(lsValue, "yyyy-MM-dd"));
                    if ("error".equals((String) poJSON.get("result"))) {
                        System.err.println((String) poJSON.get("message"));
                        ShowMessageFX.Information(null, pxeModuleName, (String) poJSON.get("message"));
                        return;
                    }
                    txtField.setText(CommonUtils.xsDateLong(oTrans.getMasterModel().getTransaction()));
                    break;
                
                case 8://referno
                    poJSON = oTrans.getMasterModel().setReferenceNumber(lsValue);
                    if ("error".equals((String) poJSON.get("result"))) {
                        System.err.println((String) poJSON.get("message"));
                        ShowMessageFX.Information(null, pxeModuleName, (String) poJSON.get("message"));
                        return;
                    }
                    break;
                
                case 9://dValidity
                    poJSON = oTrans.getMasterModel().setValidity(SQLUtil.toDate(lsValue, "yyyy-MM-dd"));
                    if ("error".equals((String) poJSON.get("result"))) {
                        System.err.println((String) poJSON.get("message"));
                        ShowMessageFX.Information(null, pxeModuleName, (String) poJSON.get("message"));
                        return;
                    }
                    txtField.setText(CommonUtils.xsDateLong(oTrans.getMasterModel().getTransaction()));
                    break;
                    
                case 14://nTWithHld
                    poJSON = oTrans.getMasterModel().setTransactionTotal(Double.parseDouble(lsValue));

                    if ("error".equals((String) poJSON.get("result"))) {
                        System.err.println((String) poJSON.get("message"));
                        ShowMessageFX.Information(null, pxeModuleName, (String) poJSON.get("message"));
                        return;
                    }
                    txtField.setText(oTrans.getMasterModel().getTransactionTotal().toString());
                    break;
                   
                case 16://nTWithHld
                    poJSON = oTrans.getMasterModel().setTWithHld(Double.parseDouble(lsValue));

                    if ("error".equals((String) poJSON.get("result"))) {
                        System.err.println((String) poJSON.get("message"));
                        ShowMessageFX.Information(null, pxeModuleName, (String) poJSON.get("message"));
                        return;
                    }
                    txtField.setText(oTrans.getMasterModel().getTWithHld().toString());
                    break;
                
                case 17://nDiscount
                    poJSON = oTrans.getMasterModel().setDiscount(Double.parseDouble(lsValue));

                    if ("error".equals((String) poJSON.get("result"))) {
                        System.err.println((String) poJSON.get("message"));
                        ShowMessageFX.Information(null, pxeModuleName, (String) poJSON.get("message"));
                        return;
                    }
                    txtField.setText(oTrans.getMasterModel().getDiscount().toString());
                    break;
                
                case 18://nAddDiscx
                    poJSON = oTrans.getMasterModel().setAddDiscx(Double.parseDouble(lsValue));

                    if ("error".equals((String) poJSON.get("result"))) {
                        System.err.println((String) poJSON.get("message"));
                        ShowMessageFX.Information(null, pxeModuleName, (String) poJSON.get("message"));
                        return;
                    }
                    txtField.setText(oTrans.getMasterModel().getAddDiscx().toString());
                    break;
                    
                case 19://
                    poJSON = oTrans.getMasterModel().setFreightx(Double.parseDouble(lsValue));

                    if ("error".equals((String) poJSON.get("result"))) {
                        System.err.println((String) poJSON.get("message"));
                        ShowMessageFX.Information(null, pxeModuleName, (String) poJSON.get("message"));
                        return;
                    }
                    txtField.setText(oTrans.getMasterModel().getFreightx().toString());
                    break;
//                case 6://expecteddate
//                    poJSON = oTrans.getMasterModel().setExpectedPurchaseDate(SQLUtil.toDate(lsValue, "yyyy-MM-dd"));
//                    if ("error".equals((String) poJSON.get("result"))) {
//                        System.err.println((String) poJSON.get("message"));
//                        ShowMessageFX.Information(null, pxeModuleName, (String) poJSON.get("message"));
//                        return;
//                    }
//                    txtField.setText(CommonUtils.xsDateLong(oTrans.getMasterModel().getExpectedPurchaseDate()));
//                    break;

            }
        } else {

            switch (lnIndex) {
                case 2:
                    txtField.setText(CommonUtils.dateFormat(oTrans.getMasterModel().getTransaction(), "yyyy-MM-dd"));
                    if ("error".equals((String) poJSON.get("result"))) {
                        System.err.println((String) poJSON.get("message"));
                        ShowMessageFX.Information(null, pxeModuleName, (String) poJSON.get("message"));
                        return;
                    }
                    break;
//                case 6:
//                    txtField.setText(CommonUtils.dateFormat(oTrans.getMasterModel().getExpectedPurchaseDate(), "yyyy-MM-dd"));
//                    if ("error".equals((String) poJSON.get("result"))) {
//                        System.err.println((String) poJSON.get("message"));
//                        ShowMessageFX.Information(null, pxeModuleName, (String) poJSON.get("message"));
//                        return;
//                    }
//                    break;
            }

        }

        txtField.selectAll();
        pnIndex = lnIndex;
    };

    final ChangeListener<? super Boolean> txtArea_Focus = (o, ov, nv) -> {
        if (!pbLoaded) {
            return;
        }

        TextArea txtField = (TextArea) ((ReadOnlyBooleanPropertyBase) o).getBean();
        int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
        String lsValue = txtField.getText();

        if (lsValue == null) {
            return;
        }

        if (!nv) {
            /*Lost Focus*/
            switch (lnIndex) {
                case 7:
                    poJSON = oTrans.getMasterModel().setRemarks(lsValue);
                    if ("error".equals((String) poJSON.get("result"))) {
                        System.err.println((String) poJSON.get("message"));
                        return;
                    }
                    break;
            }
        } else {
            txtField.selectAll();
        }
        pnIndex = lnIndex;
    };

    final ChangeListener<? super Boolean> txtDetail_Focus = (o, ov, nv) -> {
        if (!pbLoaded) {
            return;
        }

        TextField txtField = (TextField) ((ReadOnlyBooleanPropertyBase) o).getBean();
        int lnIndex = Integer.parseInt(txtField.getId().substring(9, 11));
        String lsValue = txtField.getText();
        pnIndex = lnIndex;
        if (lsValue == null) {
            return;
        }

        if (!nv) {
            /*Lost Focus*/
            switch (lnIndex) {
                case 1:
                    String lsStockID = (String) oTrans.getDetailModel(pnDetailRow).getValue("sStockIDx");
                    if (lsStockID == null || lsStockID.isEmpty()) {
                        if (txtField.getText().length() > 128) {
                            ShowMessageFX.Warning("Max characters for `Descript` exceeds the limit.", pxeModuleName, "Please verify your entry.");
                            txtField.requestFocus();
                            return;
                        }
                        poJSON = oTrans.setDetail(pnDetailRow, "sDescript", lsValue);
                        
                        if ("error".equals((String) poJSON.get("result"))) {
                            System.err.println((String) poJSON.get("message"));
                            return;
                        }
                    }
                    
                    loadTableDetail();
                    break;
                case 2:
                    String lsStockID2 = (String) oTrans.getDetailModel(pnDetailRow).getValue("sStockIDx");
                    if (lsStockID2 == null || lsStockID2.isEmpty()) {
                        if (txtField.getText().length() > 128) {
                            ShowMessageFX.Warning("Max characters for `Descript` exceeds the limit.", pxeModuleName, "Please verify your entry.");
                            txtField.requestFocus();
                            return;
                        }
                        poJSON = oTrans.setDetail(pnDetailRow, "sDescript", lsValue);
                        
                        if ("error".equals((String) poJSON.get("result"))) {
                            System.err.println((String) poJSON.get("message"));
                            return;
                        }
                    }
                    loadTableDetail();
                    break;

                case 4:
                    /*this must be numeric*/
                    int x = 0;
                    try {

                        x = Integer.valueOf(lsValue);
                        if (x > 999999) {
                            x = 0;

                            ShowMessageFX.Warning("Please input not greater than 999999", pxeModuleName, "");
                            txtField.requestFocus();
                        }
                    } catch (Exception e) {
                        ShowMessageFX.Warning("Please input numbers only.", pxeModuleName, e.getMessage());
                        txtField.requestFocus();
                    }

                    poJSON = oTrans.setDetail(pnDetailRow, "nQuantity", x);
                    if ("error".equals((String) poJSON.get("result"))) {
                        System.err.println((String) poJSON.get("message"));
                        ShowMessageFX.Information(null, pxeModuleName, (String) poJSON.get("message"));
                        return;
                    }

                    loadTableDetail();
                    break;
                    
                case 5:
                    /*this must be numeric*/
                    
                    Double yy = 0.00;
                    try {

                        yy = Double.valueOf(lsValue);
                        if (yy > 99999999) {
                            yy = 0.00;

                            ShowMessageFX.Warning("Please input not greater than 99999999", pxeModuleName, "");
                            txtField.requestFocus();
                        }
                    } catch (Exception e) {
                        ShowMessageFX.Warning("Please input numbers only.", pxeModuleName, e.getMessage());
                        txtField.requestFocus();
                    }

                    poJSON = oTrans.setDetail(pnDetailRow, "nDiscAmtx", yy);
                    if ("error".equals((String) poJSON.get("result"))) {
                        System.err.println((String) poJSON.get("message"));
                        ShowMessageFX.Information(null, pxeModuleName, (String) poJSON.get("message"));
                        return;
                    }
                    
                    loadTableDetail();
                    break;
                    
                case 6:
                    /*this must be numeric*/
                    Double zz = 0.00;
                    try {

                        zz = Double.valueOf(lsValue);
                        if (zz > 99999999) {
                            zz = 0.00;

                            ShowMessageFX.Warning("Please input not greater than 99999999", pxeModuleName, "");
                            txtField.requestFocus();
                        }
                    } catch (Exception e) {
                        ShowMessageFX.Warning("Please input numbers only.", pxeModuleName, e.getMessage());
                        txtField.requestFocus();
                    }

                    poJSON = oTrans.setDetail(pnDetailRow, "nDiscRate", zz);
                    
                    
                    if ("error".equals((String) poJSON.get("result"))) {
                        System.err.println((String) poJSON.get("message"));
                        ShowMessageFX.Information(null, pxeModuleName, (String) poJSON.get("message"));
                        return;
                    }

                    loadTableDetail();
                    break;
                    
                      case 7:
                    /*this must be numeric*/
                    Double z = 0.00;
                    try {

                        z = Double.valueOf(lsValue);
                        if (z > 99999999) {
                            z = 0.00;

                            ShowMessageFX.Warning("Please input not greater than 99999999", pxeModuleName, "");
                            txtField.requestFocus();
                        }
                    } catch (Exception e) {
                        ShowMessageFX.Warning("Please input numbers only.", pxeModuleName, e.getMessage());
                        txtField.requestFocus();
                    }

                    poJSON = oTrans.setDetail(pnDetailRow, "nUnitPrce", z);
                    
                    
                    if ("error".equals((String) poJSON.get("result"))) {
                        System.err.println((String) poJSON.get("message"));
                        ShowMessageFX.Information(null, pxeModuleName, (String) poJSON.get("message"));
                        return;
                    }

                    loadTableDetail();
                    break;
                
                
                    
            }
        } else {
            txtField.selectAll();
        }

    };
    

    
    
    
}
