/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.cas.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.cas.inventory.base.InvMaster;
import org.guanzon.cas.inventory.base.Inventory;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author User
 */
public class InventoryDetailController implements  Initializable,ScreenInterface {
    
    private final String pxeModuleName = "Inventory Details";
    private GRider oApp;
    private String oTransnox = "";
    private int pnEditMode;  
    
    private boolean state = false;
    private boolean pbLoaded = false;
    private int pnInventory = 0; 
    
    private InvMaster oTrans;
    
    @FXML
    private AnchorPane AnchorMain,AnchorTable,AnchorInput;

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
    private Button btnLedger;

    @FXML
    private Button btnSerial;

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
    private TextField txtField261;

    @FXML
    private TextField txtField271;

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
    private DatePicker dpField01;

    @FXML
    private TextField txtField26;

    @FXML
    private TextField txtField27;

    @FXML
    private TextField txtField28;

    @FXML
    private TextField txtField29;

    @FXML
    private TextField txtField30;

    @FXML
    private TextField txtField31;

    @FXML
    private TextField txtField32;
    
    @FXML
    private TextField txtField33;

    @FXML
    private TextField txtField34;

    @FXML
    void chkFiled01_Clicked(MouseEvent event) {

    }

    @FXML
    void chkFiled02_Clicked(MouseEvent event) {

    }

    @FXML
    void chkFiled03_Clicked(MouseEvent event) {

    }

    @FXML
    void chkFiled04_Clicked(MouseEvent event) {

    }


    /**
     * Initializes the controller class.
     */
    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
        
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        oTrans = new InvMaster(oApp, true);
        if (oTransnox == null || oTransnox.isEmpty()) { // Check if oTransnox is null or empty
            pnEditMode = EditMode.UNKNOWN;
        initButton(pnEditMode);
        }
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
        btnLedger.setOnAction(this::handleButtonAction);        
        btnSerial.setOnAction(this::handleButtonAction);
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
                            appUnload.unloadForm(AnchorMain, oApp, pxeModuleName);
                        }
                    break;
                case "btnNew":
                     pnEditMode = EditMode.ADDNEW;
                     initButton(pnEditMode);
                     initTabAnchor();
                     txtField02.requestFocus();
//                    poJSON = oTrans.newTransaction();
//                    if ("success".equals((String) poJSON.get("result"))){
//                            pnEditMode = EditMode.ADDNEW;
//                            initButton(pnEditMode);
//                            txtField01.setText(oTrans.getTransNox());
//                            System.out.print("(transnox === " + (String) oTrans.getTransNox());
//                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
////                            System.out.println("dTransact = " + oTrans.getAccount(pnCompany, "dTransact"));
//                            String dateStr = oTrans.getAccount(pnCompany, "dTransact").toString();
//                            if (!dateStr.isEmpty()) {
//                                // Parse the date string
//                                LocalDateTime dateTime = LocalDateTime.parse(dateStr, formatter);
//                                LocalDate localbdate = dateTime.toLocalDate();
//
//                                // Set the value of the DatePicker to the parsed LocalDate
//                                cpField02.setValue(localbdate);
//                            } else {
//                                // Handle empty or null date string
//                                // For example, set the DatePicker value to null or display an error message
//                                cpField02.setValue(null); // Set to null or handle appropriately
//                            }
//                            System.out.println("ADDNEW = " + EditMode.ADDNEW);
//                            System.out.println("EDITMODE = " + pnEditMode);
//                            loadComapany();
//                            initTabAnchor();
//                        }else{
//                            ShowMessageFX.Information((String)poJSON.get("message"), "Computerized Acounting System", pxeModuleName);
//                            System.out.println("Record not saved successfully.");
//                            System.out.println((String) poJSON.get("message"));
//                            
//                            initTabAnchor();
//                        }
                        
                    break;
                case "btnUpdate":
//                     poJSON = oTrans.updateTransaction();
//                        if ("error".equals((String) poJSON.get("result"))){
//                            ShowMessageFX.Information((String)poJSON.get("message"), "Computerized Acounting System", pxeModuleName);
//                        }
//                        pnEditMode =  oTrans.getEditMode();
//                        
//                        initButton(pnEditMode);
//                        initTabAnchor();
//                    
//                    
//                        poJSON = oTrans.updateTransaction();
//                        if ("error".equals((String) poJSON.get("result"))){
//                            ShowMessageFX.Information((String)poJSON.get("message"), "Computerized Acounting System", pxeModuleName);
//                        }
//                        pnEditMode =  oTrans.getEditMode();
//                        
//                        initButton(pnEditMode);
//                        initTabAnchor();
//                        
                    break;
                case "btnCancel":
                        if (ShowMessageFX.YesNo("Do you really want to cancel this record? \nAny data collected will not be kept.", "Computerized Acounting System", pxeModuleName)){
                            
//                            clearCompanyFields();
                             pnEditMode = EditMode.UNKNOWN;     
                             initButton(pnEditMode);
                             initTabAnchor();
                        }
                    break;
                case "btnSave":
                    
//                        JSONObject saveResult = oTrans.saveTransaction();
//                        if ("success".equals((String) saveResult.get("result"))){
//                            System.err.println((String) saveResult.get("message"));
//                            ShowMessageFX.Information((String) saveResult.get("message"), "Computerized Acounting System", pxeModuleName);
////                            clearAllFields();
//                            pnEditMode = EditMode.UNKNOWN;
//                            initButton(pnEditMode);
//                            initTabAnchor();
//                            clearCompanyFields();
//                            data.clear();
//                            System.out.println("Record saved successfully.");
//                        } else {
//                            ShowMessageFX.Information((String)saveResult.get("message"), "Computerized Acounting System", pxeModuleName);
//                            System.out.println("Record not saved successfully.");
//                            System.out.println((String) saveResult.get("message"));
//                        }
                        
                     break;

                
                case "btnBrowse": 
                       poJSON = oTrans.SearchInventory(txtSeeks01.getText().toString(), false);
                        if ("error".equals((String) poJSON.get("result"))){
                            ShowMessageFX.Information((String) poJSON.get("message"), "Computerized Acounting System", pxeModuleName);
                           
                            txtSeeks01.clear();
                            break;
                        }
                        pnEditMode = oTrans.getEditMode();
                        
                        initButton(pnEditMode);
                        System.out.print("\neditmode on browse == " +poJSON);
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
//        btnLedger.setVisible(!lbShow);
//        btnSerial.setVisible(!lbShow);
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
//            btnLedger.setVisible(!lbShow);
//            btnSerial.setVisible(!lbShow);
            btnBrowse.setVisible(!lbShow);
            btnNew.setVisible(!lbShow);
            
            btnBrowse.setManaged(false);
            btnNew.setManaged(false);
            btnUpdate.setManaged(false);
//            btnLedger.setManaged(false);
//            btnSerial.setManaged(false);
            btnClose.setManaged(false);
        }
        else{
            txtSeeks01.setDisable(lbShow);
            txtSeeks01.requestFocus();
            txtSeeks02.setDisable(lbShow);  
        }
//        initClientType();

    }
    
    private void loadInventory(){
     if(pnEditMode == EditMode.READY || 
                pnEditMode == EditMode.ADDNEW || 
                pnEditMode == EditMode.UPDATE){
            txtField01.setText((String) oTrans.getInvModel().getStockID());
            txtField02.setText((String) oTrans.getInvModel().getBarcode());
            txtField03.setText((String) oTrans.getInvModel().getAltBarcode());
            txtField04.setText((String) oTrans.getInvModel().getBriefDescription());
            txtField05.setText((String) oTrans.getInvModel().getDescription());
            
            txtField06.setText((String) oTrans.getInvModel().getCategName1());
            txtField07.setText((String) oTrans.getInvModel().getCategName2());
            txtField08.setText((String) oTrans.getInvModel().getCategName3());
            txtField09.setText((String) oTrans.getInvModel().getCategName4());
            
            txtField10.setText((String) oTrans.getInvModel().getBrandName());
            txtField11.setText((String) oTrans.getInvModel().getModelName());
            txtField12.setText((String) oTrans.getInvModel().getColorName());
            txtField13.setText((String) oTrans.getInvModel().getMeasureName());
            
            txtField14.setText(CommonUtils.NumberFormat(oTrans.getInvModel().getDiscountLevel1(), "#,##0.00"));
            txtField15.setText( CommonUtils.NumberFormat(oTrans.getInvModel().getDiscountLevel2(), "#,##0.00"));
            txtField16.setText( CommonUtils.NumberFormat(oTrans.getInvModel().getDiscountLevel3(), "#,##0.00"));
            txtField17.setText( CommonUtils.NumberFormat(oTrans.getInvModel().getDealerDiscount(), "#,##0.00"));
            
            txtField26.setText(String.valueOf(oTrans.getInvModel().getMinLevel()));
            txtField27.setText(String.valueOf(oTrans.getInvModel().getMaxLevel()));
            txtField29.setText(String.valueOf(oTrans.getInvModel().getMinLevel()));
            txtField30.setText(String.valueOf(oTrans.getInvModel().getMaxLevel()));
            
            txtField18.setText( CommonUtils.NumberFormat(oTrans.getInvModel().getUnitPrice(), "#,##0.00"));
            txtField19.setText( CommonUtils.NumberFormat(oTrans.getInvModel().getSelPrice(), "#,##0.00"));
            
            txtField20.setText((String) oTrans.getInvModel().getSupersed());
            txtField21.setText(String.valueOf(oTrans.getInvModel().getShlfLife()));
            
            chkField01.setSelected("1".equals(oTrans.getInvModel().getSerialze()));
            chkField02.setSelected("1".equals(oTrans.getInvModel().getComboInv()));
            chkField03.setSelected("1".equals(oTrans.getInvModel().getWthPromo()));
            chkField04.setSelected((oTrans.getInvModel().isActive()));
            
            if (chkField04.isSelected()){
                lblStatus.setText("ACTIVE");
            }else{
                lblStatus.setText("INACTIVE");
            }
     }
    }
}
