/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.cas.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
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
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.cas.inventory.base.Inventory;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author User
 */
public class InventoryParamController implements Initializable,ScreenInterface {
    private final String pxeModuleName = "Inventory Parameter";
    private GRider oApp;
    private String oTransnox = "";
    private int pnEditMode;  
    private Inventory oTrans;
    
    private boolean state = false;
    private boolean pbLoaded = false;
    private int pnInventory = 0; 
    
    
    @FXML
    private AnchorPane AnchorMain,AnchorInput,AnchorTable;

    @FXML
    private TextField txtSeeks02;

    @FXML
    private TextField txtSeeks01;

    @FXML
    private HBox hbButtons;

    @FXML
    private Button btnBrowse;

    @FXML
    private Button btnNew;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnUpdate;

    @FXML
    private Button btnSearch;

    @FXML
    private Button btnCancel;

    @FXML
    private Button btnClose;

    @FXML
    private TextField txtField01;

    @FXML
    private TextField txtField02;

    @FXML
    private TextField txtField03;

    @FXML
    private TextField txtField04;

    @FXML
    private TextField txtField05;

    @FXML
    private ComboBox cmbField01;

    @FXML
    private TextField txtField06;

    @FXML
    private TextField txtField07;

    @FXML
    private TextField txtField08;

    @FXML
    private TextField txtField09;

    @FXML
    private TextField txtField10;

    @FXML
    private TextField txtField11;

    @FXML
    private TextField txtField12;

    @FXML
    private TextField txtField13;

    @FXML
    private TextField txtField14;

    @FXML
    private TextField txtField15;

    @FXML
    private TextField txtField16;

    @FXML
    private TextField txtField17;

    @FXML
    private TextField txtField18;

    @FXML
    private TextField txtField19;

    @FXML
    private ComboBox cmbField02;

    @FXML
    private CheckBox chkField01;

    @FXML
    private CheckBox chkField02;

    @FXML
    private CheckBox chkField03;

    @FXML
    private TextField txtField20;

    @FXML
    private TextField txtField21;

    @FXML
    private Label lblStatus;

    @FXML
    private CheckBox chkField04;

    @FXML
    private TextField txtField22;

    @FXML
    private TextField txtField23;

    @FXML
    private TextField txtField24;

    @FXML
    private TextField txtField25;

    @FXML
    private TextField txtField26;

    @FXML
    private TextField txtField27;
    
    @FXML
    private TableView tblSubItems;


    /**
     * Initializes the controller class.
     */
    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
        
    }  
    @FXML
    void chkFiled01_Clicked(MouseEvent event) {
        boolean isChecked = chkField01.isSelected();
        oTrans.getModel().setSerialze((isChecked ? "1": "0"));
    }
    
    @FXML
    void chkFiled02_Clicked(MouseEvent event) {
        boolean isChecked = chkField02.isSelected();
        oTrans.getModel().setComboInv((isChecked ? "1": "0"));
    }
    
    @FXML
    void chkFiled03_Clicked(MouseEvent event) {
        boolean isChecked = chkField03.isSelected();
        oTrans.getModel().setWthPromo((isChecked ? "1": "0"));
    }
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        oTrans = new Inventory(oApp, true);
        if (oTransnox == null || oTransnox.isEmpty()) { // Check if oTransnox is null or empty
            pnEditMode = EditMode.UNKNOWN;
        initButton(pnEditMode);
        }
        oTrans = new Inventory(oApp, true);
        oTrans.setRecordStatus("0123");
        
        pnEditMode = EditMode.UNKNOWN;        
        initButton(pnEditMode);
        initTabAnchor();
        ClickButton();
        pbLoaded = true;
        // TODO
    }    
    
    /*Handle button click*/
    private void ClickButton() {
        btnCancel.setOnAction(this::handleButtonAction);
        btnNew.setOnAction(this::handleButtonAction);
        btnSave.setOnAction(this::handleButtonAction);
        btnUpdate.setOnAction(this::handleButtonAction);
        btnClose.setOnAction(this::handleButtonAction);
        btnBrowse.setOnAction(this::handleButtonAction);
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
                        clearAllFields();    
                        appUnload.unloadForm(AnchorMain, oApp, pxeModuleName);
                        }
                    break;
                case "btnNew":
//                     pnEditMode = EditMode.ADDNEW;
//                     initButton(pnEditMode);
//                     initTabAnchor();
//                     txtField02.requestFocus();
                    poJSON = oTrans.newRecord();
                    if ("success".equals((String) poJSON.get("result"))){
                            pnEditMode = EditMode.ADDNEW;
                            initButton(pnEditMode);
                            txtField01.setText(oTrans.getModel().getStockID());
                            System.out.print("(Stock ID === " + (String) oTrans.getModel().getStockID() + "\n");
                            
                            System.out.println("ADDNEW = " + EditMode.ADDNEW);
                            System.out.println("EDITMODE = " + pnEditMode);
//                            loadComapany();
                            initTabAnchor();
                            InitTextFields();
                        }else{
                            ShowMessageFX.Information((String)poJSON.get("message"), "Computerized Acounting System", pxeModuleName);
//                            System.out.println("Record not saved successfully.");
                            System.out.println((String) poJSON.get("message"));
                            initTabAnchor();
                        }
                        
                    break;
                case "btnUpdate":
                    
                     poJSON = oTrans.updateRecord();
                        if ("error".equals((String) poJSON.get("result"))){
                            ShowMessageFX.Information((String)poJSON.get("message"), "Computerized Acounting System", pxeModuleName);
                        }
                        pnEditMode =  oTrans.getEditMode();
                        
                        initButton(pnEditMode);
                        initTabAnchor();
                    break;
                case "btnCancel":
                        if (ShowMessageFX.YesNo("Do you really want to cancel this record? \nAny data collected will not be kept.", "Computerized Acounting System", pxeModuleName)){                            
                             clearAllFields();
                             pnEditMode = EditMode.UNKNOWN;     
                             initButton(pnEditMode);
                             initTabAnchor();
                        }
                    break;
                case "btnSave":
                        JSONObject saveResult = oTrans.saveRecord();
                        if ("success".equals((String) saveResult.get("result"))){
                            System.err.println((String) saveResult.get("message"));
                            ShowMessageFX.Information((String) saveResult.get("message"), "Computerized Acounting System", pxeModuleName);
                            
                            pnEditMode = EditMode.UNKNOWN;
                            initButton(pnEditMode);
                            clearAllFields();
                            initTabAnchor();
//                            clearCompanyFields();
//                            data.clear();
                            System.out.println("Record saved successfully.");
                        } else {
                            ShowMessageFX.Information((String)saveResult.get("message"), "Computerized Acounting System", pxeModuleName);
                            System.out.println("Record not saved successfully.");
                            System.out.println((String) saveResult.get("message"));
                        }
                        
                     break;

                case "btnAdd":
//                    JSONObject addDetails = oTrans.addDetail();
//                         System.out.println((String) addDetails.get("message"));
//                        if ("error".equals((String) addDetails.get("result"))){
//                            ShowMessageFX.Information((String) addDetails.get("message"), "Computerized Acounting System", pxeModuleName);
//                            break;
//                        } 
////                        clearMobileFields();
//                        loadComapany();
//                        pnCompany = oTrans.getAccount().size()-1;
//                        tblAccreditation.getSelectionModel().select(pnCompany + 1);
//                        clearCompanyFields();
                break;
                case "btnDelete":
//                    if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Do you want to remove ?") == true){  
//                     
//                        oTrans.getAccount().remove(pnCompany);
//                        if(oTrans.getAccount().size() <= 0){
//                            oTrans.addDetail();
//                            
//                        }
//                        
//                        pnCompany = oTrans.getAccount().size()-1;
//                        loadComapany();
//                        clearCompanyFields();
//                        txtField02.requestFocus();
//                    }
                    break;
                case "btnBrowse": 
//                    
                        poJSON = oTrans.searchRecord(txtSeeks01.getText().toString(), false);
                        if ("error".equals((String) poJSON.get("result"))){
                            ShowMessageFX.Information((String) poJSON.get("message"), "Computerized Acounting System", pxeModuleName);
                           
                            txtSeeks01.clear();
                            break;
                        }
                        pnEditMode = oTrans.getEditMode();
                        
                        System.out.print("\neditmode on browse == " +pnEditMode);
                        loadInventory();
                        
                       
                    break;
        }
    }
    
    }
    
    /*USE TO DISABLE ANCHOR BASE ON INITMODE*/
    private void initTabAnchor(){
        boolean pbValue = pnEditMode == EditMode.ADDNEW || 
                pnEditMode == EditMode.UPDATE;
        AnchorInput.setDisable(!pbValue);
        AnchorTable.setDisable(!pbValue);
    }
    
    /*TO CONTROL BUTTONS BASE ON INITMODE*/
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
        cmbField01.setDisable(!lbShow);

        txtSeeks01.setDisable(!lbShow);
        txtSeeks02.setDisable(!lbShow);
        
        if (lbShow){
            txtSeeks01.setDisable(lbShow);
            txtSeeks01.clear();
            txtSeeks02.setDisable(lbShow);
            txtSeeks02.clear();
            
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
            txtSeeks01.setDisable(lbShow);
            txtSeeks01.requestFocus();
            txtSeeks02.setDisable(lbShow);  
        }
//        initClientType();

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
        txtField09.focusedProperty().addListener(txtField_Focus);
        txtField10.focusedProperty().addListener(txtField_Focus);
        txtField11.focusedProperty().addListener(txtField_Focus);
        txtField12.focusedProperty().addListener(txtField_Focus);
        txtField13.focusedProperty().addListener(txtField_Focus);
        txtField14.focusedProperty().addListener(txtField_Focus);
        txtField15.focusedProperty().addListener(txtField_Focus);
        txtField16.focusedProperty().addListener(txtField_Focus);
        txtField17.focusedProperty().addListener(txtField_Focus);
        txtField18.focusedProperty().addListener(txtField_Focus);
        txtField19.focusedProperty().addListener(txtField_Focus);
        txtField20.focusedProperty().addListener(txtField_Focus);
        txtField21.focusedProperty().addListener(txtField_Focus);
        txtField22.focusedProperty().addListener(txtField_Focus);
        txtField23.focusedProperty().addListener(txtField_Focus);
        txtField24.focusedProperty().addListener(txtField_Focus);
        txtField25.focusedProperty().addListener(txtField_Focus);
        txtField26.focusedProperty().addListener(txtField_Focus);
        txtField27.focusedProperty().addListener(txtField_Focus);
        
        txtField06.setOnKeyPressed(this::txtField_KeyPressed);
        txtField07.setOnKeyPressed(this::txtField_KeyPressed);
        txtField08.setOnKeyPressed(this::txtField_KeyPressed);
        txtField09.setOnKeyPressed(this::txtField_KeyPressed);
        txtField10.setOnKeyPressed(this::txtField_KeyPressed);
        txtField11.setOnKeyPressed(this::txtField_KeyPressed);
        txtField12.setOnKeyPressed(this::txtField_KeyPressed);

    }
    /*textfield lost focus*/
    final ChangeListener<? super Boolean> txtField_Focus = (o,ov,nv)->{ 
        if (!pbLoaded) return;
       
        TextField txtField = (TextField)((ReadOnlyBooleanPropertyBase)o).getBean();
        int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
        String lsValue = txtField.getText();
        JSONObject jsonObject = new JSONObject();
        if (lsValue == null) return;         
        if(!nv){ /*Lost Focus*/
            switch (lnIndex){
                case 1: /*Stock ID*/
                    oTrans.getModel().setStockID(lsValue);
                   System.out.print( "STOCK ID == " + oTrans.getModel().setStockID(lsValue));
                    break;
                case 2:/*barrcode*/
                   oTrans.getModel().setBarcode(lsValue);
                   System.out.print( "BARRCODE == " + oTrans.getModel().setBarcode(lsValue));
                    break;
                case 3:/*ALT barrcode*/   
                   oTrans.getModel().setAltBarcode (lsValue);
                   System.out.print( "ALT BARRCODE == " + oTrans.getModel().setAltBarcode (lsValue));
                    break;
                case 4:/*BRIEF DESC*/
                   oTrans.getModel().setBriefDescription(lsValue);
                   System.out.print( "BRIEF DESC == " + oTrans.getModel().setBriefDescription (lsValue));
                    break;
                case 5:/*DESC*/
                   oTrans.getModel().setDescription(lsValue);
                   System.out.print( "DESC == " + oTrans.getModel().setDescription(lsValue));
                    break;
                case 6:/*CATEGORY CODE 1*/
                   oTrans.getModel().setCategCd1(lsValue);
                   System.out.print( "CATEGORY 1 == " + oTrans.getModel().setCategCd1(lsValue));
                    break;
                case 7:/*CATEGORY CODE 2*/
                  oTrans.getModel().setCategCd2(lsValue);
                   System.out.print( "CATEGORY 2 == " + oTrans.getModel().setCategCd2(lsValue));
                    break;
                case 8:/*CATEGORY CODE 3*/
                   oTrans.getModel().setCategCd3(lsValue);
                   System.out.print( "CATEGORY 3 == " + oTrans.getModel().setCategCd3(lsValue));
                    break;
                case 9:/*CATEGORY CODE 4*/
                   oTrans.getModel().setCategCd4(lsValue);
                   System.out.print( "CATEGORY 4 == " + oTrans.getModel().setCategCd4(lsValue));
                    break;
                case 10:/*Brand*/
                   oTrans.getModel().setBrandCode(lsValue);
                   System.out.print( "BRAND == " + oTrans.getModel().setBrandCode(lsValue));
                    break;
                case 11:/*Model*/
                   oTrans.getModel().setModelCode(lsValue);
                   System.out.print( "Model == " + oTrans.getModel().setModelCode(lsValue));
                    break;
                case 12:/*Color*/
                   oTrans.getModel().setColorCode(lsValue);
                   System.out.print( "color == " + oTrans.getModel().setColorCode(lsValue));
                    break;
                case 13:/*Measure*/
                   oTrans.getModel().setMeasureID(lsValue);
                   System.out.print( "Measure == " + oTrans.getModel().setMeasureID(lsValue));
                    break;
                case 14:/*discount 1*/
                   oTrans.getModel().setDiscountLvl1((Double.parseDouble(lsValue)));
                   System.out.print( "discount 1 == " + oTrans.getModel().setDiscountLvl1((Double.parseDouble(lsValue))));
                   if ("error".equals(jsonObject.get("result"))) {
                        System.err.println((String) jsonObject.get("message"));
                        return;
                    }
                    break;
                case 15:/*discount 2*/
                    oTrans.getModel().setDiscountLvl2(Double.parseDouble(lsValue));
                   System.out.print( "discount 2 == " + oTrans.getModel().setDiscountLvl2(Double.parseDouble(lsValue)));
                   if ("error".equals((String) jsonObject.get("result"))) {
                        System.err.println((String) jsonObject.get("message"));
                        return;
                    }
                    break;
                case 16:/*discount 3*/
                   oTrans.getModel().setDiscountLevel3((Double.parseDouble(lsValue)));
                   System.out.print( "discount 3 == " + oTrans.getModel().setDiscountLevel3(Double.parseDouble(lsValue)));
                   if ("error".equals((String) jsonObject.get("result"))) {
                        System.err.println((String) jsonObject.get("message"));
                        return;
                    }
                    break;
                case 17:/*dealer discount or discount 4*/
                   oTrans.getModel().setDealerDiscount((Double.parseDouble(lsValue)));
                   System.out.print( "discount 3 == " + oTrans.getModel().setDealerDiscount(Double.parseDouble(lsValue)));
                   if ("error".equals((String) jsonObject.get("result"))) {
                        System.err.println((String) jsonObject.get("message"));
                        return;
                    }
                    break;
                case 26: /*MINIMUM LEVEL*/
                   
                   oTrans.getModel().setMinLevel((Integer.parseInt(lsValue)));
                   System.out.print( "MINIMUM LEVEL == " + oTrans.getModel().setMinLevel((Integer.parseInt(lsValue))));
                   if ("error".equals((String) jsonObject.get("result"))) {
                        System.err.println((String) jsonObject.get("message"));
                        return;
                    }
                    break;
                    
                case 27: /*MAXIMUM LEVEL*/
                   
                   oTrans.getModel().setMaxLevel((Integer.parseInt(lsValue)));
                   System.out.print( "MAXIMUM LEVEL == " + oTrans.getModel().setMaxLevel((Integer.parseInt(lsValue))));
                   if ("error".equals((String) jsonObject.get("result"))) {
                        System.err.println((String) jsonObject.get("message"));
                        return;
                    }
                    break;    
                case 18:/*cost*/
                   oTrans.getModel().setUnitPrice((Double.parseDouble(lsValue)));
                   System.out.print( "discount 3 == " + oTrans.getModel().setUnitPrice(Double.parseDouble(lsValue)));
                   if ("error".equals((String) jsonObject.get("result"))) {
                        System.err.println((String) jsonObject.get("message"));
                        return;
                    }
                    break;
                case 19:/*SRP*/
                   oTrans.getModel().setSelPrice((Double.parseDouble(lsValue)));
                   System.out.print( "discount 3 == " + oTrans.getModel().setSelPrice(Double.parseDouble(lsValue)));
                   if ("error".equals((String) jsonObject.get("result"))) {
                        System.err.println((String) jsonObject.get("message"));
                        return;
                    }
                    break;
                case 20:/*superseded*/
                   oTrans.getModel().setSupersed(lsValue);
                   System.out.print( "Model == " + oTrans.getModel().setSupersed(lsValue));
                    break;
                case 21: /*shelf life*/
                   if(lsValue.isEmpty()) lsValue = "0";
                   oTrans.getModel().setShlfLife((Integer.parseInt(lsValue)));
                   System.out.print( "MINIMUM LEVEL == " + oTrans.getModel().setShlfLife((Integer.parseInt(lsValue))));
                   if ("error".equals((String) jsonObject.get("result"))) {
                        System.err.println((String) jsonObject.get("message"));
                        return;
                    }
                    break;
            }                  
        } else
            txtField.selectAll();
    };
    
    /*Text Field with search*/
    private void txtField_KeyPressed(KeyEvent event){
        TextField txtField = (TextField)event.getSource();
        int lnIndex = Integer.parseInt(((TextField)event.getSource()).getId().substring(8,10));
        String lsValue = txtField.getText();
        JSONObject poJson;
        switch (event.getCode()) {
            case F3:
                switch (lnIndex){
                    case 06: /*search category 1*/
                        poJson = new JSONObject();
                        poJson =  oTrans.SearchMaster(6,lsValue, false);
                           System.out.println("poJson = " + poJson.toJSONString());
                           if("error".equalsIgnoreCase(poJson.get("result").toString())){
                               ShowMessageFX.Information((String) poJson.get("message"), "Computerized Acounting System", pxeModuleName);                              
                           }
                            System.out.print( "category 1 == " + oTrans.getMaster(32));
                           txtField06.setText((String) oTrans.getModel().getCategName1()); 
                           cmbField01.setValue((String) oTrans.getMaster(32));
                        break;
                    case 7: /*search category 2*/
                        poJson = new JSONObject();
                        poJson =  oTrans.SearchMaster(7,lsValue, false);
                           System.out.println("poJson = " + poJson.toJSONString());
                           if("error".equalsIgnoreCase(poJson.get("result").toString())){
                               ShowMessageFX.Information((String) poJson.get("message"), "Computerized Acounting System", pxeModuleName);                              
                           }
                            System.out.print( "category 2 == " + oTrans.getMaster(33));
                           txtField07.setText((String) oTrans.getMaster(33));      
                        break;
                    case 8: /*search category 3*/
                        poJson = new JSONObject();
                        poJson =  oTrans.SearchMaster(8,lsValue, false);
                           System.out.println("poJson = " + poJson.toJSONString());
                           if("error".equalsIgnoreCase(poJson.get("result").toString())){
                               ShowMessageFX.Information((String) poJson.get("message"), "Computerized Acounting System", pxeModuleName);                              
                           }
                            System.out.print( "category 3 == " + oTrans.getMaster(34));
                           txtField08.setText((String) oTrans.getMaster(34));      
                        break;
                    case 9: /*search category 4*/
                        poJson = new JSONObject();
                        poJson =  oTrans.SearchMaster(9,lsValue, false);
                           System.out.println("poJson = " + poJson.toJSONString());
                           if("error".equalsIgnoreCase(poJson.get("result").toString())){
                               ShowMessageFX.Information((String) poJson.get("message"), "Computerized Acounting System", pxeModuleName);                              
                           }
                            System.out.print( "category 4 == " + oTrans.getMaster(35));
                           txtField09.setText((String) oTrans.getMaster(35));      
                        break;
                    case 10: /*search Brand*/
                        poJson = new JSONObject();
                        poJson =  oTrans.SearchMaster(10,lsValue, false);
                           System.out.println("poJson = " + poJson.toJSONString());
                           if("error".equalsIgnoreCase(poJson.get("result").toString())){
                               ShowMessageFX.Information((String) poJson.get("message"), "Computerized Acounting System", pxeModuleName);                              
                           }
                            System.out.print( "brand == " + oTrans.getMaster(36));
                           txtField10.setText((String) oTrans.getMaster(36));      
                        break;  
                    case 11: /*search Model*/
                        poJson = new JSONObject();
                        poJson =  oTrans.SearchMaster(11,lsValue, false);
                           System.out.println("poJson = " + poJson.toJSONString());
                           if("error".equalsIgnoreCase(poJson.get("result").toString())){
                               ShowMessageFX.Information((String) poJson.get("message"), "Computerized Acounting System", pxeModuleName);                              
                           }
                            System.out.print( "Model == " + oTrans.getMaster(37));
                           txtField11.setText((String) oTrans.getMaster(37));      
                        break;
                    case 12: /*search color*/
                        poJson = new JSONObject();
                        poJson =  oTrans.SearchMaster(12,lsValue, false);
                           System.out.println("poJson = " + poJson.toJSONString());
                           if("error".equalsIgnoreCase(poJson.get("result").toString())){
                               ShowMessageFX.Information((String) poJson.get("message"), "Computerized Acounting System", pxeModuleName);                              
                           }
                            System.out.print( "Color == " + oTrans.getMaster(39));
                           txtField12.setText((String) oTrans.getMaster(39));      
                        break;
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
    private void clearAllFields() {
        // Arrays of TextFields grouped by sections
        TextField[][] allFields = {
            // Text fields related to specific sections
            {txtSeeks01, txtSeeks02, txtField01, txtField02, txtField03, txtField04,
             txtField05, txtField06, txtField07, txtField08, txtField09,txtField10, 
             txtField11, txtField12, txtField13, txtField14, txtField15,txtField16, 
             txtField17, txtField18, txtField19, txtField20, txtField21,txtField22, 
             txtField23, txtField24, txtField25, txtField26, txtField27},

        };
        chkField01.setSelected(false);
        chkField02.setSelected(false);
        chkField03.setSelected(false);
        chkField04.setSelected(false);
        cmbField01.setValue(null);
        cmbField01.setValue(null);
        
        // Loop through each array of TextFields and clear them
        for (TextField[] fields : allFields) {
            for (TextField field : fields) {
                field.clear();
            }
        }
    }
    private void loadInventory(){
     if(pnEditMode == EditMode.READY || 
                pnEditMode == EditMode.ADDNEW || 
                pnEditMode == EditMode.UPDATE){
            txtField01.setText((String) oTrans.getModel().getStockID());
            txtField02.setText((String) oTrans.getModel().getBarcode());
            txtField03.setText((String) oTrans.getModel().getAltBarcode());
            txtField04.setText((String) oTrans.getModel().getBriefDescription());
            txtField05.setText((String) oTrans.getModel().getDescription());
            
            txtField06.setText((String) oTrans.getModel().getCategName1());
            txtField07.setText((String) oTrans.getModel().getCategName2());
            txtField08.setText((String) oTrans.getModel().getCategName3());
            txtField09.setText((String) oTrans.getModel().getCategName4());
            
            txtField10.setText((String) oTrans.getModel().getBrandName());
            txtField11.setText((String) oTrans.getModel().getModelName());
            txtField12.setText((String) oTrans.getModel().getColorName());
            txtField13.setText((String) oTrans.getModel().getMeasureName());
            
            txtField14.setText(CommonUtils.NumberFormat(oTrans.getModel().getDiscountLevel1(), "#,##0.00"));
            txtField15.setText( CommonUtils.NumberFormat(oTrans.getModel().getDiscountLevel2(), "#,##0.00"));
            txtField16.setText( CommonUtils.NumberFormat(oTrans.getModel().getDiscountLevel3(), "#,##0.00"));
            txtField17.setText( CommonUtils.NumberFormat(oTrans.getModel().getDealerDiscount(), "#,##0.00"));
            
            txtField26.setText(String.valueOf(oTrans.getModel().getMinLevel()));
            txtField27.setText(String.valueOf(oTrans.getModel().getMaxLevel()));
            txtField18.setText( CommonUtils.NumberFormat(oTrans.getModel().getUnitPrice(), "#,##0.00"));
            txtField19.setText( CommonUtils.NumberFormat(oTrans.getModel().getSelPrice(), "#,##0.00"));
            
            
            txtField20.setText((String) oTrans.getModel().getSupersed());
            txtField21.setText(String.valueOf(oTrans.getModel().getShlfLife()));
            
            chkField01.setSelected("1".equals(oTrans.getModel().getSerialze()));
            chkField02.setSelected("1".equals(oTrans.getModel().getComboInv()));
            chkField03.setSelected("1".equals(oTrans.getModel().getWthPromo()));
            chkField04.setSelected((oTrans.getModel().isActive()));
            
            if (chkField04.isSelected()){
                lblStatus.setText("ACTIVE");
            }else{
                lblStatus.setText("INACTIVE");
            }
     }
    }
    @FXML
    void chkFiled04_Clicked(MouseEvent event) {
        boolean isChecked = chkField04.isSelected();
        oTrans.getModel().setActive((isChecked));
        if (chkField04.isSelected()){
                lblStatus.setText("ACTIVE");
            }else{
                lblStatus.setText("INACTIVE");
        }
    }
}
