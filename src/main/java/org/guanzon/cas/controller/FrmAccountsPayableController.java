/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.cas.controller;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import static javafx.scene.input.KeyCode.DOWN;
import static javafx.scene.input.KeyCode.ENTER;
import static javafx.scene.input.KeyCode.F3;
import static javafx.scene.input.KeyCode.UP;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.base.LogWrapper;
import org.guanzon.appdriver.base.SQLUtil;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.cas.client.account.APClients;
import org.guanzon.cas.client.account.AP_Client_Master;
import org.guanzon.cas.clients.account.GlobalVariables;
import org.guanzon.cas.inv.Inventory;
import org.guanzon.cas.model.ModelAPClientLedger;
import org.guanzon.cas.model.SharedModel;
import org.guanzon.cas.parameter.services.ParamControllers;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author User
 */
public class FrmAccountsPayableController implements Initializable,ScreenInterface {
    private final String pxeModuleName = "Accounts Payable Clients";
    private GRider oApp;
    private int pnEditMode;  
    private APClients oTrans;
    private ParamControllers oParameters;
    private boolean state = false;
    private boolean pbLoaded = false;
    private String oTransnox = "";
    private DashboardController fdController = new DashboardController();
    private String lsSearchRes = ""; 
    private SharedModel sharedModel;
    String category = System.getProperty("store.inventory.industry", "").trim().replace(";", "");

    
    
    public void initModel(SharedModel sharedModel) {
        this.sharedModel = sharedModel;
    }
    
    @FXML
    private AnchorPane AnchorMain,AnchorCompany;

    @FXML
    private HBox hbButtons;

    @FXML
    private Button btnBrowse;

    @FXML
    private Button btnNew;

    @FXML
    private Button btnSave;
    
    @FXML
    private Label lblStat;
    
    @FXML
    private Button btnUpdate;

    @FXML
    private Button btnSearch;

    @FXML
    private Button btnCancel;

    @FXML
    private Button btnClose;

    @FXML
    private TextField txtSearch02;

    @FXML
    private TextField txtSearch01;

    @FXML
    private TextField txtField01;

    @FXML
    private TextField txtField04;

    @FXML
    private TextField txtField09;

    @FXML
    private TextField txtField02;

    @FXML
    private TextField txtField08;

    @FXML
    private TextField txtField10;

    @FXML
    private TextField txtField07;

    @FXML
    private TextField txtField11;

    @FXML
    private TextField txtField12;

    @FXML
    private TextField txtField13;

    @FXML
    private CheckBox chkfield01;

    @FXML
    private TextField txtField03;

    @FXML
    private TextField txtField05;

    @FXML
    private TextField txtField06;

    @FXML
    private DatePicker cpField02;

    @FXML
    private DatePicker cpField01;

    @FXML
    private TableView tblLedger;

    @FXML
    private TableColumn indexAddress01;

    @FXML
    private TableColumn indexAddress02;

    @FXML
    private TableColumn indexAddress03;

    @FXML
    private TableColumn indexAddress04;

    @FXML
    private TableColumn indexAddress05;

    @FXML
    private TableColumn indexAddress06;
    
    @FXML
    void tblLedger_Clicked(MouseEvent event) {

    }
    public void receiveString(String message) {
        lsSearchRes = (message);
    }

    private ObservableList<ModelAPClientLedger> data = FXCollections.observableArrayList();
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        pnEditMode = EditMode.UNKNOWN;        
        initButton(pnEditMode);
        clearAllFields();
        initializeObject();
        InitTextFields();
        ClickButton();
        initSearchFields();
//        InitSearchFields();
        initTabAnchor();
        pbLoaded = true;
    }  
    
    private void initializeObject() {
//        String category = System.getProperty("store.inventory.industry", "").trim().replace(";", "");
//
//        System.out.println("category == " + category);
        LogWrapper logwrapr = new LogWrapper("CAS", System.getProperty("sys.default.path.temp") + "cas-error.log");
        oTrans = new APClients(oApp, "", logwrapr);
        oParameters = new ParamControllers(oApp, logwrapr);

    }
    
    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
        
    }
    public void setTransaction(String fsValue){
        oTransnox = fsValue;
    }
    
    public void setState(boolean fsValue){
        state = fsValue;
    }
    
    private void ClickButton() {
        btnCancel.setOnAction(this::handleButtonAction);
        btnNew.setOnAction(this::handleButtonAction);
        btnSave.setOnAction(this::handleButtonAction);
        btnUpdate.setOnAction(this::handleButtonAction);
        btnClose.setOnAction(this::handleButtonAction);
        btnBrowse.setOnAction(this::handleButtonAction);
    }
    private void initSearchFields(){
        /*textFields FOCUSED PROPERTY*/
        txtSearch01.setOnKeyPressed(this::searchinfo_KeyPressed);
        txtSearch02.setOnKeyPressed(this::searchinfo_KeyPressed);
    }
    private void searchinfo_KeyPressed(KeyEvent event){
        TextField txtSearch = (TextField)event.getSource();
        int lnIndex = Integer.parseInt(((TextField)event.getSource()).getId().substring(9,11));
        String lsValue = txtSearch.getText();
        JSONObject poJson;
        switch (event.getCode()) {
            case F3:
                switch (lnIndex){
                    case 01: /*search company id*/
//                        poJson = oTrans.(lsValue, true);
//                        if ("error".equals((String) poJson.get("result"))){
//                            ShowMessageFX.Information((String) poJson.get("message"), "Computerized Acounting System", pxeModuleName);
//                            txtSearch02.clear();
//                            break;
//                        }
//                        pnEditMode = oTrans.getEditMode();                        
//                        retrieveDetails();
                    break;
                    case 02: /*search company name*/
//                        poJson = oTrans.searchRecord(lsValue, false);
//                        if ("error".equals((String) poJson.get("result"))){
//                            ShowMessageFX.Information((String) poJson.get("message"), "Computerized Acounting System", pxeModuleName);
//                            txtSearch02.clear();
//                            break;
//                        }
//                        pnEditMode = oTrans.getEditMode();
//                        retrieveDetails();
                    break;
                }
            case ENTER:
        }
        
        switch (event.getCode()){
        case ENTER:
            CommonUtils.SetNextFocus(txtSearch);
        case DOWN:
            CommonUtils.SetNextFocus(txtSearch);
            break;
        case UP:
            CommonUtils.SetPreviousFocus(txtSearch);
        }
    }
    private void handleButtonAction(ActionEvent event) {
        Object source = event.getSource();
        JSONObject poJSON;
        if (source instanceof Button) {
            Button clickedButton = (Button) source;
            unloadForm appUnload = new unloadForm();
            switch (clickedButton.getId()) {
                case"btnClose":
                    if (ShowMessageFX.YesNo("Do you really want to cancel this record? \nAny data collected will not be kept.", "Computerized Acounting System", pxeModuleName)){
                            appUnload.unloadForm(AnchorMain, oApp, pxeModuleName);
                        }
                    break;
                case "btnNew":
                    
                    clearAllFields();
//                    txtField02.requestFocus();
                    poJSON = oTrans.APClientMaster().newRecord();
                    if ("success".equals((String) poJSON.get("result"))) {
                        pnEditMode = EditMode.ADDNEW;
                        initButton(pnEditMode);
                        poJSON = oParameters.Category().searchRecord(category, true);
                        if("success".equals((String)poJSON.get("result"))){
                            txtField13.setText((String) oParameters.Category().getModel().getDescription());
                        }
//                        retrieveDetails();
                        initTabAnchor();
                        cpField01.setValue(LocalDate.now());
                        cpField02.setValue(LocalDate.now());
                        System.out.println("pnEditMode == " + pnEditMode);
                    } else {
                        ShowMessageFX.Information((String) poJSON.get("message"), "Computerized Acounting System", pxeModuleName);
                        System.out.println((String) poJSON.get("message"));
                        initTabAnchor();
                    }
                    
                    
//                    poJSON = oTrans.newRecord();
//                    if ("success".equals((String) poJSON.get("result"))){
//                        
//                        clearAllFields();
//                        pnEditMode = EditMode.ADDNEW;
//                        initButton(pnEditMode);
//
//                        System.out.println("ADDNEW = " + EditMode.ADDNEW);
//                        System.out.println("EDITMODE = " + pnEditMode);
//
//                        txtField02.requestFocus();
//                        txtField07.setText((String) oTrans.getModel().getCreditLimit());
//                        txtField10.setText((String) oTrans.getModel().getBeginBal());
//                        txtField08.setText((String) oTrans.getModel().getDiscount());
//                        txtField11.setText((String) oTrans.getModel().getABalance());
//                        txtField12.setText((String) oTrans.getModel().getOBalance());
//                        txtField13.setText((String) oTrans.getModel().getCategoryName());
//                        System.out.println("getCategoryName = "  + oTrans.getModel().getCategoryName());
//                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//
//                        // Parse the formatted date string into a LocalDate object
//                        if(oTrans.getMaster(5) != null && !oTrans.getMaster(5).toString().trim().isEmpty()){
//                            LocalDate localbdate = LocalDate.parse(CommonUtils.dateFormat(oTrans.getMaster(5), "yyyy-MM-dd"), formatter);
//                            // Set the value of the DatePicker to the parsed LocalDate
//                                cpField01.setValue(localbdate);
//                        }
//                        // Parse the formatted date string into a LocalDate object
//                        if(oTrans.getMaster(6) != null && !oTrans.getMaster(6).toString().trim().isEmpty()){
//                            LocalDate localbdate = LocalDate.parse(CommonUtils.dateFormat(oTrans.getMaster(6), "yyyy-MM-dd"), formatter);
//
//                            // Set the value of the DatePicker to the parsed LocalDate
//                            cpField02.setValue(localbdate);
//                        }
//                        initTabAnchor();
//                    }else{
//                        ShowMessageFX.Information((String)poJSON.get("message"), "Computerized Acounting System", pxeModuleName);
//                        System.out.println("Record not saved successfully.");
//                        System.out.println((String) poJSON.get("message"));
//                        initTabAnchor();
//                    }
                    break;
                case "btnUpdate":
//                         poJSON = oTrans.updateRecord();
//                        if ("error".equals((String) poJSON.get("result"))){
//                            ShowMessageFX.Information((String)poJSON.get("message"), "Computerized Acounting System", pxeModuleName);
//                        }
//                        pnEditMode =  oTrans.getEditMode();
//                        
//                        initButton(pnEditMode);
//                        initTabAnchor();
                    break;
                case "btnCancel":
                        if (ShowMessageFX.YesNo("Do you really want to cancel this record? \nAny data collected will not be kept.", "Computerized Acounting System", pxeModuleName)){
                            clearAllFields();
                            initializeObject();
                            pnEditMode = EditMode.UNKNOWN;
                            initButton(pnEditMode);
                            initTabAnchor();
                        }
                    break;
                case "btnBrowse":  
                            String lsValue = (txtSearch01.getText()==null?"":txtSearch01.getText());
                            poJSON =  oTrans.APClientMaster().searchRecord(lsValue, false);
                           System.out.println("poJson = " + poJSON.toJSONString());
                           if("error".equalsIgnoreCase(poJSON.get("result").toString())){
                                ShowMessageFX.Information((String) poJSON.get("message"), "Computerized Acounting System", pxeModuleName);
                                break;
                           }
                           pnEditMode = EditMode.READY;
//                           txtSearch02.setText(oTrans.getModel().getClientName());
//                           txtSearch01.setText(oTrans.getModel().getClientID());
                           
                            retrieveDetails();
                    
                    break;
                case "btnSave":
                    System.out.println("pnEditModesave == " + pnEditMode);
                    oTrans.APClientMaster().getModel().setModifyingId(oApp.getUserID());
                    oTrans.APClientMaster().getModel().setModifiedDate(oApp.getServerDate());
                    JSONObject saveResult = oTrans.APClientMaster().saveRecord();
                    if ("success".equals((String) saveResult.get("result"))) {
                        ShowMessageFX.Information((String) saveResult.get("message"), "Computerized Acounting System", pxeModuleName);
                        pnEditMode = EditMode.UNKNOWN;
                        initButton(pnEditMode);
                        clearAllFields();
                        System.out.println("Record saved successfully.");
                    } else {
                        ShowMessageFX.Information((String) saveResult.get("message"), "Computerized Acounting System", pxeModuleName);
                        System.out.println("Record not saved successfully.");
                        System.out.println((String) saveResult.get("message"));
                    }
                    
                    
                    
                    
//                    System.err.println("EDITMODE BEFORE SAVE == " + EditMode.ADDNEW);
//                    System.err.println("EDITMODE BEFORE SAVE == " + pnEditMode);
//                    oTrans.APClientMaster().getModel().setModifyingId(oApp.getUserID());
//                    oTrans.APClientMaster().getModel().setModifiedDate(oApp.getServerDate());
//                    JSONObject saveResult = oTrans.APClientMaster().saveRecord();
//                    if ("success".equals((String) saveResult.get("result"))) {
//                        ShowMessageFX.Information((String) saveResult.get("message"), "Computerized Acounting System", pxeModuleName);
//                        pnEditMode = EditMode.UNKNOWN;
//                        initButton(pnEditMode);
//                        clearAllFields();
//                        System.out.println("Record saved successfully.");
//                    } else {
//                        ShowMessageFX.Information((String) saveResult.get("message"), "Computerized Acounting System", pxeModuleName);
//                        System.out.println("Record not saved successfully.");
//                        System.out.println((String) saveResult.get("message"));
//                    }

                    
                    
                    
                    
//                        JSONObject saveResult = oTrans.saveRecord();
//                        if ("success".equals((String) saveResult.get("result"))){
//                            System.err.println((String) saveResult.get("message"));
//                            ShowMessageFX.Information((String) saveResult.get("message"), "Computerized Acounting System", pxeModuleName);
//                            clearAllFields();
//                            pnEditMode = EditMode.UNKNOWN;
//                            initButton(pnEditMode);
//                            initTabAnchor();
//                            System.out.println("Record saved successfully.");
//                        } else {
//                            ShowMessageFX.Information((String)saveResult.get("message"), "Computerized Acounting System", pxeModuleName);
//                            System.out.println("Record not saved successfully.");
//                            System.out.println((String) saveResult.get("message"));
//                        }
                        
                     break;

                case "btnAddSocMed":
                break;
        }
    }
}
    private void InitSearchFields(){
        txtSearch01.setOnKeyPressed(this::txtSearch_KeyPressed);
        txtSearch02.setOnKeyPressed(this::txtSearch_KeyPressed);
    }
    
    private void InitTextFields(){
        
        txtField01.focusedProperty().addListener(txtField_Focus);
        txtField02.focusedProperty().addListener(txtField_Focus);
        txtField03.focusedProperty().addListener(txtField_Focus);
        txtField04.focusedProperty().addListener(txtField_Focus);
        txtField05.focusedProperty().addListener(txtField_Focus);
        txtField06.focusedProperty().addListener(txtField_Focus);
        txtField07.focusedProperty().addListener(txtField_Focus);
        txtField08.focusedProperty().addListener(txtField_Focus);
//        txtField09.focusedProperty().addListener(txtField_Focus);
        txtField10.focusedProperty().addListener(txtField_Focus);
        txtField11.focusedProperty().addListener(txtField_Focus);
        txtField12.focusedProperty().addListener(txtField_Focus);
        
        txtField02.setOnKeyPressed(this::txtField_KeyPressed);
        txtField09.setOnKeyPressed(this::txtField_KeyPressed);
        txtField13.setOnKeyPressed(this::txtField_KeyPressed);
        
        // Set a custom StringConverter to format date
          DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
       
//        initdatepicker();
    }
    private void initTabAnchor(){
        boolean pbValue = pnEditMode == EditMode.ADDNEW || 
                pnEditMode == EditMode.UPDATE;
        AnchorCompany.setDisable(!pbValue);
    
    }
    
    private void txtField_KeyPressed(KeyEvent event){
        TextField txtField = (TextField)event.getSource();
        int lnIndex = Integer.parseInt(((TextField)event.getSource()).getId().substring(8,10));        
        String lsValue = (txtField.getText() == null ?"": txtField.getText());
        JSONObject poJson;
        switch (event.getCode()) {
            case F3:
                switch (lnIndex){
                    case 02: /*search branch*/
                        poJson = new JSONObject();
                        String input = "";
                        input = lsValue;
                           poJson =  oTrans.APClientMaster().searchRecord(lsValue, false);
                           System.out.println("poJson = " + poJson.toJSONString());
                           if("error".equalsIgnoreCase(poJson.get("result").toString())){
                               loadCompanyTransaction();
                               String receivedData = SharedModel.sharedString;
                               System.out.println("receivedData = " + GlobalVariables.sClientID);
//                               poJson = oTrans.SearchClient(receivedData, tr);
                           }
                           retrieveDetails();
//                           txtField01.setText(oTrans.APClientMaster().getModel().ClientMaster().getClientId());
//                           txtField02.setText(oTrans.APClientMaster().getModel().ClientMaster().getCompanyName());
//                           txtField03.setText(oTrans.APClientMaster().getModel().ClientAddress().getAddress());
//                           txtField04.setText(oTrans.APClientMaster().getModel().ClientAddress().getAddress());
//                           txtField05.setText(oTrans.APClientMaster().getModel().ClientAddress().getAddress());
                           
//                           txtField01.setText((String) poJson.get("sClientID"));                    
//                           txtField02.setText((String) poJson.get("sCompnyNm"));                           
//                           txtField03.setText((String) poJson.get("xAddressx"));
//                           txtField04.setText((String) poJson.get("sCPerson1"));                         
//                           txtField05.setText((String) poJson.get("sMobileNo"));
//                           txtField06.setText((String) poJson.get("sTaxIDNox"));
                        break;
//                        
//                    case 9: /*search branch*/
////                        receivedDataLabel.setText(receivedData);
//                        poJson = new JSONObject();
//                        poJson =  oTrans.SearchTerm(lsValue, false);
//                       System.out.println("poJson = " + poJson.toJSONString());
//                       if("error".equalsIgnoreCase(poJson.get("result").toString())){
//                            ShowMessageFX.Information((String) poJson.get("message"), "Computerized Acounting System", pxeModuleName);
//                            txtField09.clear();
//                       }
//                       txtField09.setText((String) poJson.get("sDescript"));        
//                        break;
//                    case 13: /*search branch*/
////                        receivedDataLabel.setText(receivedData);
//                        poJson = new JSONObject();
//                        poJson =  oTrans.SearchCategory(lsValue, false);
//                       System.out.println("poJson = " + poJson.toJSONString());
//                       if("error".equalsIgnoreCase(poJson.get("result").toString())){
//                            ShowMessageFX.Information((String) poJson.get("message"), "Computerized Acounting System", pxeModuleName);
//                            txtField13.clear();
//                       }
//                       txtField13.setText((String) poJson.get("xCategrNm"));        
//                        break;
                }
            case ENTER:
                
        }
        switch (event.getCode()){
        case ENTER:
            CommonUtils.SetNextFocus(txtField);
        case DOWN:
            CommonUtils.SetNextFocus(txtField);
            break;
        case UP:
            CommonUtils.SetPreviousFocus(txtField);
        }
    }
    
//    public void initdatepicker(){
//        // Set a custom StringConverter to format date
//          DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        cpField01.setConverter(new StringConverter<LocalDate>() {
//            @Override
//            public String toString(LocalDate date) {
//                if (date != null) {
////                    oTrans.APClientMaster().getModel().setBeginningDate(dateFormatter.format(date).toString());
////                    oTrans.setMaster(5, dateFormatter.format(date).toString());
//                    System.out.println("dCltSince = " + date);
//                    
////                    cpField01.setValue(LocalDate.parse(date.format(dateFormatter), dateFormatter));
//                    return dateFormatter.format(date);
//                } else {
//                    return "";
//                }
//            }
//
//            @Override
//            public LocalDate fromString(String string) {
//                if (string != null && !string.isEmpty()) {
//                    oTrans.setMaster(5,LocalDate.parse(string, dateFormatter).toString());
//                    return LocalDate.parse(string, dateFormatter);
//                } else {
//                    return null;
//                }
//            }
//        });
//        cpField02.setConverter(new StringConverter<LocalDate>() {
//            @Override
//            public String toString(LocalDate date) {
//                if (date != null) {
//                    oTrans.setMaster(6, dateFormatter.format(date).toString());
//                    System.out.println("dBegDatex = " + date);
//                    
//                    cpField02.setValue(LocalDate.parse(date.format(dateFormatter), dateFormatter));
//                    return dateFormatter.format(date);
//                } else {
//                    return "";
//                }
//            }
//
//            @Override
//            public LocalDate fromString(String string) {
//                if (string != null && !string.isEmpty()) {
//                    oTrans.setMaster(6,LocalDate.parse(string, dateFormatter).toString());
//                    return LocalDate.parse(string, dateFormatter);
//                } else {
//                    return null;
//                }
//            }
//        });
//
//    }
    
    final ChangeListener<? super Boolean> txtField_Focus = (ObservableValue<? extends Boolean> o,Boolean ov,Boolean nv)->{ 
        if (!pbLoaded) return;
       
        TextField txtField = (TextField)((ReadOnlyBooleanPropertyBase)o).getBean();
        int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
        String lsValue = txtField.getText();
        JSONObject jsonObject = new JSONObject();
        if (lsValue == null) return;         
        if(!nv){ /*Lost Focus*/
            switch (lnIndex){
                case 1: /*company id*/
//                    jsonObject = oTrans.setMaster( 8,lsValue);
//                    jsonObject =  oTrans.APClientMaster().getModel().(lsValue);
                    break;
                case 2:/*company name*/
                    break;
                case 3:/*address*/   
                    break;
                case 4:/*Contact person*/
//                    jsonObject =oTrans.setMaster(3,jsonObject.get("sContctID"));
                    break;
                case 5:/*contact no*/
                    break;  
                case 6:/*tin No */
                    break;
                case 7:/*Credit limit*/
                case 8:
                case 10:
                case 11:
                case 12:
                    double discount = Double.parseDouble(lsValue);
                        if (lnIndex == 7) {
                            oTrans.APClientMaster().getModel().setCreditLimit(discount);
                        }
                        if (lnIndex == 8) {
                            oTrans.APClientMaster().getModel().setDiscount(discount);
                        }
                        if (lnIndex == 10) {
                            oTrans.APClientMaster().getModel().setBeginningBalance(discount);
                        }
                        if (lnIndex == 11) {
                            oTrans.APClientMaster().getModel().setAccountBalance(discount);
                        }
                        if (lnIndex == 12) {
                            oTrans.APClientMaster().getModel().setOBalance(discount);
                        }
                        txtField.setText(CommonUtils.NumberFormat(discount, "0.00"));
                        break;
//                    txtField.setText( (CommonUtils.NumberFormat(Double.parseDouble(lsValue), "#,##0.00")));
//                    jsonObject = oTrans.setMaster(10,(Double.parseDouble(lsValue.replace(",", ""))));
//                    break;
//                case 8:/*discount*/     
////                    txtField.setText(CommonUtils.NumberFormat(Double.parseDouble(lsValue), "0.00"));
////                    jsonObject = oTrans.setMaster(9,(Double.parseDouble(lsValue)));
//                    break;
                case 9:/*term */
                    break;
//                case 10 :/*beginning balance*/
//                    
////                    txtField.setText( (CommonUtils.NumberFormat(Double.parseDouble(lsValue), "#,##0.00")));
////                    jsonObject = oTrans.setMaster(7,(Double.parseDouble(lsValue.replace(",", ""))));
//                    break;
//                case 11 :/*available balance*/
////                    txtField.setText( (CommonUtils.NumberFormat(Double.parseDouble(lsValue), "#,##0.00")));
////                    jsonObject = oTrans.setMaster(11,(Double.parseDouble(lsValue.replace(",", ""))));
//                    break;
//                case 12 :/*outstanding balance*/
////                    txtField.setText( (CommonUtils.NumberFormat(Double.parseDouble(lsValue), "#,##0.00")));
//                    jsonObject = oTrans.setMaster(12,(Double.parseDouble(lsValue.replace(",", ""))));
//                    break;
            }                    
        } else
            txtField.selectAll();
    };
    
    @FXML
    void ChkVATReg_Clicked(MouseEvent event) {
        boolean isChecked = chkfield01.isSelected();
//        oTrans.setMaster(14,(isChecked) ? "1" : "0");
        String val = (isChecked) ? "1" : "0";
    }
    /*OPEN WINDOW FOR */
    private void loadCompanyTransaction() {
        try {
            String sFormName = "Client Transactions Company";
            FXMLLoader fxmlLoader = new FXMLLoader();
            unloadForm unload = new unloadForm();
            ClientMasterTransactionCompanyController loControl = new ClientMasterTransactionCompanyController();
            loControl.setGRider(oApp);
            loControl.setParentController(this);
            loControl.setCompanyName((txtField02.getText()==null?"":txtField02.getText().toString()));
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/cas/views/ClientMasterTransactionCompany.fxml"));
            fxmlLoader.setController(loControl);
            Parent parent = fxmlLoader.load();
            AnchorPane otherAnchorPane = loControl.AnchorMain;

            // Get the parent of the TabContent node
            Node tabContent = AnchorMain.getParent();
            Parent tabContentParent = tabContent.getParent();

            // If the parent is a TabPane, you can work with it directly
            if (tabContentParent instanceof TabPane) {
                TabPane tabpane = (TabPane) tabContentParent;

                for (Tab tab : tabpane.getTabs()) {
                    if (tab.getText().equals(sFormName)) {
                        if (ShowMessageFX.OkayCancel(null, pxeModuleName, "You have unsaved data on " + pxeModuleName + ". Are you sure you want to create new client for account payable  record?") == true) {
                        
                            System.out.println("globalvariable = " + GlobalVariables.sClientID);
                        } else {
                            System.out.println("globalvariable = " + GlobalVariables.sClientID);
                            return;
                        }

                        if (ShowMessageFX.OkayCancel(null, pxeModuleName, "You have opened " + pxeModuleName + ".  Are you sure you want to create new client for account payable record?") == true) {
                            
                            System.out.println("globalvariable = " + GlobalVariables.sClientID);
                        } else {
                            
                            System.out.println("globalvariable = " + GlobalVariables.sClientID);
                            return;
                        }
                        tabpane.getSelectionModel().select(tab);
                        unload.unloadForm(AnchorMain, oApp, sFormName);
                        unload.unloadForm(otherAnchorPane, oApp, sFormName);
                        
                        System.out.println("globalvariable = " + GlobalVariables.sClientID);
                        loadCompanyTransaction();
                        return;
                    }
                }

                Tab newTab = new Tab(sFormName, parent);
                newTab.setStyle("-fx-font-weight: bold; -fx-pref-width: 180; -fx-font-size: 10.5px; -fx-font-family: arial;");
                newTab.setContextMenu(fdController.createContextMenu(tabpane, newTab, oApp));
                tabpane.getTabs().add(newTab);
                tabpane.getSelectionModel().select(newTab);
                newTab.setOnCloseRequest(event -> {
                    if (ShowMessageFX.YesNo(null, "Close Tab", "Are you sure, do you want to close tab?") == true) {
                        if (unload != null) {
                            unload.unloadForm(otherAnchorPane, oApp, sFormName);
                            
                            System.out.println("globalvariable = " + GlobalVariables.sClientID);
                        } else {
//                            ShowMessageFX.Warning(getStage(), "Please notify the system administrator to configure the null value at the close button.", "Warning", pxeModuleName);
                        }
                    } else {
                        // Cancel the close request
                        event.consume();
                    }

                });

                List<String> tabName = new ArrayList<>();
                
                tabName.remove(sFormName);
                tabName.add(sFormName);
                // Save the list of tab IDs to the JSON file
                
            }

        } catch (IOException e) {
            e.printStackTrace();
//            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
            System.exit(1);

         }
    }
    
    private void initButton(int fnValue){
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);
         btnCancel.setVisible(lbShow);
        btnSearch.setVisible(lbShow);
        btnSave.setVisible(lbShow);
        
        btnSave.setManaged(lbShow);
        btnCancel.setManaged(lbShow);
        btnSearch.setManaged(lbShow);
        btnUpdate.setVisible(!lbShow);
        btnBrowse.setVisible(!lbShow);
        btnNew.setVisible(!lbShow);
        

        txtSearch01.setDisable(!lbShow);
        txtSearch02.setDisable(!lbShow);
        
        if (lbShow){
            txtSearch01.setDisable(lbShow);
            txtSearch01.clear();
            txtSearch02.setDisable(lbShow);
            
            btnCancel.setVisible(lbShow);
            btnSearch.setVisible(lbShow);
            btnSave.setVisible(lbShow);
            btnUpdate.setVisible(!lbShow);
            btnBrowse.setVisible(!lbShow);
            btnNew.setVisible(!lbShow);
            btnBrowse.setManaged(false);
            btnNew.setManaged(false);
            btnUpdate.setManaged(false);
            btnClose.setManaged(false);
        }
        else{
            txtSearch01.setDisable(lbShow);
            txtSearch01.requestFocus();
            txtSearch02.setDisable(lbShow);  
        }
        
    }
    private void txtSearch_KeyPressed(KeyEvent event){
        TextField txtSearch = (TextField)event.getSource();
        int lnIndex = Integer.parseInt(((TextField)event.getSource()).getId().substring(9,11));
        String lsValue = txtSearch.getText();
        JSONObject poJson;
        switch (event.getCode()) {
            case F3:
                switch (lnIndex){
                    case 01: /*search company*/
//                        poJson = new JSONObject();
//                            poJson =  oTrans.searchRecord(lsValue, true);
//                           System.out.println("poJson = " + poJson.toJSONString());
//                           if("error".equalsIgnoreCase(poJson.get("result").toString())){
//                                ShowMessageFX.Information((String) poJson.get("message"), "Computerized Acounting System", pxeModuleName);
//                           }
//                           pnEditMode = oTrans.getEditMode();
//                           txtSearch02.setText(oTrans.getModel().getClientName());
//                           txtSearch01.setText(oTrans.getModel().getClientID());
//                           
//                        break;
//                    case 02: /*search company*/
//                        poJson = new JSONObject();
//                            poJson =  oTrans.searchRecord(lsValue, false);
//                           System.out.println("poJson = " + poJson.toJSONString());
//                           if("error".equalsIgnoreCase(poJson.get("result").toString())){
//                                ShowMessageFX.Information((String) poJson.get("message"), "Computerized Acounting System", pxeModuleName);
//                           }
//                           pnEditMode = oTrans.getEditMode();
//                           txtSearch02.setText(oTrans.getModel().getClientName());
//                           txtSearch01.setText(oTrans.getModel().getClientID());
                           
                        break;
                }
            case ENTER:
                
        }
        switch (event.getCode()){
        case ENTER:
            CommonUtils.SetNextFocus(txtSearch);
        case DOWN:
            CommonUtils.SetNextFocus(txtSearch);
            break;
        case UP:
            CommonUtils.SetPreviousFocus(txtSearch);
        }
        
        retrieveDetails();
    }
    private void retrieveDetails(){
        if (pnEditMode == EditMode.READY
                || pnEditMode == EditMode.ADDNEW
                || pnEditMode == EditMode.UPDATE) {
            
            txtField01.setText((String) oTrans.APClientMaster().getModel().getClientId());
            txtField02.setText((String) oTrans.APClientMaster().getModel().ClientMaster().getCompanyName());
            txtField03.setText((String) oTrans.APClientMaster().getModel().ClientAddress().getAddress());
            txtField04.setText((String) oTrans.APClientMaster().getModel().ClientInstitutionContact().getContactPersonName());
            txtField05.setText((String) oTrans.APClientMaster().getModel().ClientInstitutionContact().getMobileNo());
            txtField06.setText((String) oTrans.APClientMaster().getModel().ClientMaster().getTaxIdNumber());
            txtField07.setText(CommonUtils.NumberFormat(oTrans.APClientMaster().getModel().getCreditLimit(), "#,##0.00"));
            txtField08.setText(CommonUtils.NumberFormat(oTrans.APClientMaster().getModel().getDiscount(), "#,##0.00"));
            txtField09.setText((String) oTrans.APClientMaster().getModel().Term().getDescription());
            txtField10.setText(CommonUtils.NumberFormat(oTrans.APClientMaster().getModel().getBeginningBalance(), "#,##0.00"));
            txtField11.setText(CommonUtils.NumberFormat(oTrans.APClientMaster().getModel().getAccountBalance(), "#,##0.00"));            
            txtField12.setText(CommonUtils.NumberFormat(oTrans.APClientMaster().getModel().getOBalance(), "#,##0.00"));
            txtField13.setText((String) oTrans.APClientMaster().getModel().Category().getDescription());
            

            if (oTrans.APClientMaster().getModel().getdateClientSince() != null && !oTrans.APClientMaster().getModel().getdateClientSince().toString().isEmpty()) {
                cpField01.setValue(strToDate(SQLUtil.dateFormat(oTrans.APClientMaster().getModel().getdateClientSince(), SQLUtil.FORMAT_SHORT_DATE)));
            }
            
            
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//            if(oTrans.APClientMaster().getModel().getdateClientSince() != null && !oTrans.APClientMaster().getModel().getdateClientSince().toString().trim().isEmpty()){
//                LocalDate clientSinceDate = LocalDate.parse(oTrans.APClientMaster().getModel().getdateClientSince().toString(), formatter);
//                cpField01.setValue(clientSinceDate);
//            }
//            if(oTrans.APClientMaster().getModel().getBeginningDate()!= null && !oTrans.APClientMaster().getModel().getBeginningDate().toString().trim().isEmpty()){
//                LocalDate beginningDate = LocalDate.parse(oTrans.APClientMaster().getModel().getBeginningDate().toString(), formatter);
//                cpField02.setValue(beginningDate);
//            }
 
            chkfield01.setSelected(("1".equals((String) oTrans.APClientMaster().getModel().getVatable())));
            String lsValue = oTrans.APClientMaster().getModel().getRecordStatus();

            // Use a Map to store the status mappings
            Map<String, String> statusMap = new HashMap<>();
            statusMap.put("0", "OPEN");
            statusMap.put("1", "APPROVED");
            statusMap.put("3", "DISAPPROVED");
            statusMap.put("4", "BLACKLISTED");

            // Set the label text based on the status
            lblStat.setText(statusMap.getOrDefault(lsValue, "UNKNOWN"));

            
//              loadLedger();
////              txtSearch01.clear();
////              txtSearch02.clear();
        }
    
    }
    
    public static LocalDate strToDate(String val) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(val, dateFormatter);
    }
//    private void loadLedger(){
//        int lnCtr;
//        data.clear();
////        oTrans.getAddress(pnAddress).list();
//        if(oTrans.getLedger()!= null){
//            for (lnCtr = 0; lnCtr < oTrans.getLedger().getMaster().size(); lnCtr++){
//                data.add(new ModelAPClientLedger(String.valueOf(lnCtr + 1),
//                    (String)oTrans.getLedger(lnCtr, "dTransact"), 
//                    (String)oTrans.getLedger(lnCtr, "sSourceCd"), 
//                    (String)oTrans.getLedger(lnCtr, "sSourceNo"), 
//                    (String)oTrans.getLedger(lnCtr,  "nAmountIn"),
//                    (String)oTrans.getLedger(lnCtr,  "nAmountOt")));  
//
//            }
//        }
//        
//         
//    }
    private void clearAllFields() {
        // Arrays of TextFields grouped by sections
        TextField[][] allFields = {
            // Text fields related to specific sections
            {txtSearch01, txtSearch02, txtField01, txtField02, txtField03, txtField04,
             txtField05, txtField06, txtField07, txtField08, txtField09,txtField10, txtField11, txtField12, txtField13},

            
        };
        cpField01.setValue(null);
        cpField02.setValue(null);
        chkfield01.setSelected(false);
        

        // Loop through each array of TextFields and clear them
        for (TextField[] fields : allFields) {
            for (TextField field : fields) {
                field.clear();
            }
        }   
    }
 
//    public void loadReturn(String lsValue) {
//        System.out.println("loadReturn lsValue = " + lsValue);
//        JSONObject poJson = new JSONObject();
//         poJson =  oTrans.SearchClient(lsValue, true);
//        System.out.println("poJson = " + poJson.toJSONString());
//        if("error".equalsIgnoreCase(poJson.get("result").toString())){
//            ShowMessageFX.Information((String)poJson.get("message"), "Computerized Acounting System", pxeModuleName);
//        }
//        txtField01.setText((String) poJson.get("sClientID"));                    
//        txtField02.setText((String) poJson.get("sCompnyNm"));                           
//        txtField03.setText((String) poJson.get("xAddressx"));
//        txtField04.setText((String) poJson.get("sCPerson1"));                         
//        txtField05.setText((String) poJson.get("sMobileNo"));
//        txtField06.setText((String) poJson.get("sTaxIDNox"));
//    }
    
//    private void StatusLabel(){
    
//    }

    
}