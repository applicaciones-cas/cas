/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.cas.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.base.LogWrapper;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.cas.inv.Inventory;
import org.guanzon.cas.model.ModelInvSubUnit;
import org.guanzon.cas.parameter.Category;
import org.guanzon.cas.parameter.CategoryLevel2;
import org.guanzon.cas.parameter.CategoryLevel3;
import org.guanzon.cas.parameter.CategoryLevel4;
import org.guanzon.cas.parameter.Parameters;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author User
 */
public class InventoryParamController implements Initializable, ScreenInterface {

    private final String pxeModuleName = "Inventory Parameter";
//    LogWrapper logwrapr = new LogWrapper("cas", "cas-err.log");

    private GRider oApp;
    private String oTransnox = "";
    private int pnEditMode;
    private Inventory oTrans;
    private Parameters oParameters;
    private boolean state = false;
    private boolean pbLoaded = false;
    private int pnInventory = 0;
    private int pnRow = 0;

    private ObservableList<ModelInvSubUnit> data = FXCollections.observableArrayList();
    @FXML
    private AnchorPane AnchorInput, AnchorTable, AnchorMain;

    @FXML
    private GridPane subItemFields;

    @FXML
    private HBox hbButtons;

    @FXML
    private Label lblStatus;

    @FXML
    private Text lblMeasure, lblShelf;

    @FXML
    private TableView tblSubItems;

    @FXML
    private TableColumn index01, index02, index03, index04, index05;

// Buttons
    @FXML
    private Button btnBrowse, btnNew, btnAddSubItem, btnDelSubUnit,
            btnSave, btnUpdate, btnSearch, btnCancel, btnClose;

// Text Fields
    @FXML
    private TextField txtSeeks01, txtSeeks02,
            txtField01, txtField02, txtField03, txtField04, txtField05,
            txtField06, txtField07, txtField08, txtField09, txtField10,
            txtField11, txtField12, txtField13, txtField14, txtField15,
            txtField16, txtField17, txtField18, txtField19, txtField20,
            txtField21, txtField22, txtField23, txtField24, txtField25,
            txtField26, txtField27;

// Combo Boxes
    @FXML
    private ComboBox cmbField01, cmbField02;

// Check Boxes
    @FXML
    private CheckBox chkField01, chkField02, chkField03, chkFiled04;

    ObservableList<String> unitType = FXCollections.observableArrayList(
            "LDU",
            "Regular",
            "Free",
            "Live",
            "Service",
            "RDU",
            "Others"
    );

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
//        oTrans.getModel().setSerialze((isChecked ? "1": "0"));
    }

    @FXML
    void chkFiled02_Clicked(MouseEvent event) {
        boolean isChecked = chkField02.isSelected();
//        oTrans.getModel().setComboInv((isChecked ? "1": "0"));
    }

    @FXML
    void chkFiled03_Clicked(MouseEvent event) {
        boolean isChecked = chkField03.isSelected();
//        oTrans.getModel().setWthPromo((isChecked ? "1": "0"));
    }

    @FXML
    void chkFiled04_Clicked(MouseEvent event) {
        boolean isChecked = chkFiled04.isSelected();
//        oTrans.getModel().setWthPromo((isChecked ? "1": "0"));
    }

    @FXML
    void tblSubUnit_Clicked(MouseEvent event) {
//        boolean isChecked = chkFiled04.isSelected();
////        oTrans.getModel().setWthPromo((isChecked ? "1": "0"));
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        LogWrapper logwrapr = new LogWrapper("CAS", System.getProperty("sys.default.path.temp") + "cas-error.log");
        oTrans = new Inventory();
        oTrans.setApplicationDriver(oApp);
        oTrans.setWithParentClass(false);
        oTrans.setLogWrapper(logwrapr);
        oTrans.initialize();

        oParameters = new Parameters(oApp, logwrapr);
        InitTextFields();
        pnEditMode = EditMode.UNKNOWN;
        ClickButton();
        initButton(pnEditMode);

        pbLoaded = true;
    }

//    /*Handle button click*/
    private void ClickButton() {
        btnCancel.setOnAction(this::handleButtonAction);
        btnNew.setOnAction(this::handleButtonAction);
        btnSave.setOnAction(this::handleButtonAction);
        btnUpdate.setOnAction(this::handleButtonAction);
        btnClose.setOnAction(this::handleButtonAction);
        btnBrowse.setOnAction(this::handleButtonAction);
        btnAddSubItem.setOnAction(this::handleButtonAction);
        btnDelSubUnit.setOnAction(this::handleButtonAction);
    }

    private void handleButtonAction(ActionEvent event) {
        Object source = event.getSource();

        if (source instanceof Button) {
            Button clickedButton = (Button) source;
            unloadForm appUnload = new unloadForm();
            switch (clickedButton.getId()) {
                case "btnClose":
                    if (ShowMessageFX.YesNo("Do you really want to cancel this record? \nAny data collected will not be kept.", "Computerized Acounting System", pxeModuleName)) {
//                        clearAllFields();    
                        appUnload.unloadForm(AnchorMain, oApp, pxeModuleName);
                    }
                    break;
                case "btnNew":
//                    clearAllFields();
                    txtField02.requestFocus();
                    JSONObject poJSON;
                    poJSON = oTrans.newRecord();
                    pnEditMode= EditMode.READY;
                    if ("success".equals((String) poJSON.get("result"))) {
                        pnEditMode = EditMode.ADDNEW;
                        initButton(pnEditMode);
                        loadInventory();
//                        initSubItemForm();
                                txtField01.setText(oTrans.getModel().getStockId());
                                txtField02.setText(oTrans.getModel().getBarCode());
//                            txtField06.setText((String) oTrans.getModel().Category().getDescription()); 
                            initTabAnchor();
                    } else {
                        ShowMessageFX.Information((String) poJSON.get("message"), "Computerized Acounting System", pxeModuleName);
                        System.out.println((String) poJSON.get("message"));
                            initTabAnchor();
                    }
                    System.out.println("EDITMODE sa new= " + pnEditMode);
                    break;
                case "btnBrowse":
                    String lsValue = (txtSeeks01.getText() == null) ? "" : txtSeeks01.getText();
                    poJSON = oTrans.searchRecord(lsValue, false);
                    if ("error".equals((String) poJSON.get("result"))) {
                        ShowMessageFX.Information((String) poJSON.get("message"), "Computerized Acounting System", pxeModuleName);

                        txtSeeks01.clear();
                        break;
                    }

//                    txtSeeks01.setText(oTrans.getModel().getBarCode());
//                    txtSeeks02.setText(oTrans.getModel().getDescription());
                    pnEditMode = EditMode.READY;
                    data.clear();
                    System.out.print("\neditmode on browse == " + pnEditMode);

//                        initSubItemForm();
                    loadInventory();
//                        loadSubUnitData();
//                        loadSubUnit();
//                        initTable();
                    System.out.println("EDITMODE sa cancel= " + pnEditMode);
                    break;
                case "btnUpdate":
                     poJSON = oTrans.updateRecord();
                        if ("error".equals((String) poJSON.get("result"))){
                            ShowMessageFX.Information((String)poJSON.get("message"), "Computerized Acounting System", pxeModuleName);
                            break;
                        }
                         pnEditMode =  oTrans.getEditMode();
                        System.out.println("EDITMODE sa update= " + pnEditMode);
                        initButton(pnEditMode);
//                        initTable();
                        initTabAnchor();
                    break;
                case "btnCancel":
                        if (ShowMessageFX.YesNo("Do you really want to cancel this record? \nAny data collected will not be kept.", "Computerized Acounting System", pxeModuleName)){                            
//                            clearAllFields();
//                            oTrans = new Inventory(oApp, true);
//                            oTrans.setRecordStatus("0123");
//                            oTrans.setWithUI(true);
//                            pnEditMode = EditMode.UNKNOWN;     
//                            initButton(pnEditMode);
//                            initTabAnchor();
                        }
                        System.out.println("EDITMODE sa cancel= " + pnEditMode);
                    break;
                case "btnSave":
                        JSONObject saveResult = oTrans.saveRecord();
                        System.out.println(saveResult.toJSONString());
                        if ("success".equals((String) saveResult.get("result"))){
                            System.err.println((String) saveResult.get("message"));
                            ShowMessageFX.Information((String) saveResult.get("message"), "Computerized Acounting System", pxeModuleName);
                            
                            pnEditMode = EditMode.UNKNOWN;
                            initButton(pnEditMode);
//                            clearAllFields();
//                            initTabAnchor();
                            
                            System.out.println("Record saved successfully.");
                        } else {
                            ShowMessageFX.Information((String)saveResult.get("message"), "Computerized Acounting System", pxeModuleName);
                            System.out.println("Record not saved successfully.");
                            System.out.println((String) saveResult.get("message"));
                        }
                        
                     break;

                
                case "btnAddSubItem":
//                        clearSubItemTxtField();
//                        poJSON = oTrans.addSubUnit();
//                        System.out.println(poJSON.toJSONString());
//                        if ("error".equals((String) poJSON.get("result"))){
//                            ShowMessageFX.Information((String) poJSON.get("message"), "Computerized Acounting System", pxeModuleName);
//                           
////                            txtSeeks01.clear();
//                            break;
//                        }
//                        pnRow = oTrans.getSubUnit().getMaster().size() - 1;
//                        loadSubUnitData();
                    break;
                case "btnDelSubUnit":

//                    if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Do you want to remove this sub item?") == true){  
//                        oTrans.getSubUnit().getMaster().remove(pnRow);
//                        if(oTrans.getSubUnit().getMaster().size() <= 0){
//                            oTrans.getSubUnit().addSubUnit();
//                        }
//                        
//                        pnRow = oTrans.getSubUnit().getMaster().size()-1;
//                        loadSubUnitData();
//                        clearSubItemTxtField();
//                        txtField22.requestFocus();
//                    }
                    break;
            }
        }
    }

//    /*USE TO DISABLE ANCHOR BASE ON INITMODE*/
    private void initTabAnchor(){
        boolean pbValue = (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE);
        AnchorInput.setDisable(!pbValue);
        subItemFields.setDisable(!pbValue);
        AnchorTable.setDisable(!pbValue);
        if (pnEditMode == EditMode.READY || pnEditMode == EditMode.UNKNOWN){
            AnchorTable.setDisable(pbValue);
        }
    }
//    
//    /*TO CONTROL BUTTONS BASE ON INITMODE*/
private void initButton(int fnValue) {
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);

// Manage visibility and managed state of buttons
        btnCancel.setVisible(lbShow);
        btnSearch.setVisible(lbShow);
        btnSave.setVisible(lbShow);
//        btnAddItem.setVisible(lbShow);
//        btnDelItem.setVisible(lbShow);

        btnCancel.setManaged(lbShow);
        btnSearch.setManaged(lbShow);
        btnSave.setManaged(lbShow);
//        btnAddItem.setManaged(lbShow);
//        btnDelItem.setManaged(lbShow);

// Manage visibility and managed state of other buttons
        btnBrowse.setVisible(!lbShow);
        btnNew.setVisible(!lbShow);
        btnClose.setVisible(lbShow);
        btnBrowse.setManaged(!lbShow);
        btnNew.setManaged(!lbShow);
        btnClose.setManaged(lbShow);

//        btnCancelTrans.setVisible(false);
//        btnCancelTrans.setManaged(false);
//        btnApprove.setVisible(false);
//        btnApprove.setManaged(false);
        btnUpdate.setVisible(lbShow);
        btnUpdate.setManaged(lbShow);
//        btnAddItem.setVisible(lbShow);
//        btnAddItem.setManaged(lbShow);
//        btnDelItem.setVisible(false);
//        btnDelItem.setManaged(false);
//        System.out.println("THIS IS YOUR INITIALIZE " + fnValue);
//        boolean isVisible = (fnValue == 1);
//        btnCancelTrans.setVisible(isVisible);
//        btnCancelTrans.setManaged(isVisible);
//        btnApprove.setVisible(isVisible);
//        btnApprove.setManaged(isVisible);

    }
    private void initButtonx(int fnValue) {

        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);
        btnCancel.setVisible(lbShow);
        btnSearch.setVisible(lbShow);
        btnSave.setVisible(lbShow);

        btnSave.setManaged(lbShow);
        btnCancel.setManaged(lbShow);
        btnSearch.setManaged(lbShow);
        btnBrowse.setVisible(!lbShow);
//        if("success".equals(oTrans.getmodel().is.get("result"))){
//            btnNew.setVisible(!lbShow);
//            btnUpdate.setVisible(!lbShow);
//        }else{
//            btnNew.setVisible(false);
//            btnNew.setManaged(false);
//            btnUpdate.setVisible(false);
//            btnUpdate.setManaged(false);
//        }

        cmbField01.setDisable(!lbShow);

        txtSeeks01.setDisable(!lbShow);
        txtSeeks02.setDisable(!lbShow);

        if (lbShow) {
            txtSeeks01.setDisable(lbShow);
            txtSeeks01.clear();
            txtSeeks02.setDisable(lbShow);
            txtSeeks02.clear();

            btnCancel.setVisible(lbShow);
            btnSearch.setVisible(lbShow);
            btnSave.setVisible(lbShow);
            btnBrowse.setVisible(!lbShow);

//            if("success".equals(oTrans.isWareHose().get("result"))){
//                btnNew.setVisible(!lbShow);
//                btnNew.setManaged(false);
//                btnUpdate.setVisible(!lbShow);
//                btnUpdate.setManaged(false);
//            }
//            btnBrowse.setManaged(false);
//            btnClose.setManaged(false);
        } else {
            txtSeeks01.setDisable(lbShow);
            txtSeeks01.requestFocus();
            txtSeeks02.setDisable(lbShow);
        }
    }

    private void initSubItemForm() {
        if (!oTrans.getModel().getCategoryFirstLevelId().isEmpty()) { // Ensure the string is not empty
            switch (oTrans.getModel().getCategoryFirstLevelId()) {
                case "0001":
                case "0002":
                case "0003":
                    AnchorTable.setVisible(false);
                    lblMeasure.setVisible(false);
                    lblShelf.setVisible(false);
                    txtField13.setVisible(false);
                    txtField21.setVisible(false);
                    break;
                case "0004":
                    AnchorTable.setVisible(true);
                    lblMeasure.setVisible(true);
                    lblShelf.setVisible(true);
                    txtField13.setVisible(true);
                    txtField21.setVisible(true);
                    break;
            }
        }
    }
//    

    private void InitTextFields() {
        // Define arrays for text fields with focusedProperty listeners
        TextField[] focusTextFields = {
            txtField01, txtField02, txtField03, txtField04, txtField05,
            txtField06, txtField07, txtField08, txtField09, txtField10,
            txtField11, txtField12, txtField13, txtField14, txtField15,
            txtField16, txtField17, txtField18, txtField19, txtField20,
            txtField21, txtField22, txtField23, txtField24, txtField25,
            txtField26, txtField27
        };

        // Add the listener to each text field in the focusTextFields array
        for (TextField textField : focusTextFields) {
            textField.focusedProperty().addListener(txtField_Focus);
        }

        // Define arrays for text fields with setOnKeyPressed handlers
        TextField[] keyPressedTextFields = {
            txtField06, txtField07, txtField08, txtField09, txtField10,
            txtField11, txtField12, txtField13, txtField22
        };

        // Set the same key pressed event handler for each text field in the keyPressedTextFields array
        for (TextField textField : keyPressedTextFields) {
            textField.setOnKeyPressed(this::txtField_KeyPressed);
        }

//        txtSeeks01.setOnKeyPressed(this::txtSeeks_KeyPressed);
//        txtSeeks02.setOnKeyPressed(this::txtSeeks_KeyPressed);
//        lblStatus.setText(chkField04.isSelected() ? "ACTIVE" : "INACTIVE");
    }
//    
//    /*textfield lost focus*/
    final ChangeListener<? super Boolean> txtField_Focus = (o, ov, nv) -> {
        if (!pbLoaded) {
            return;
        }

        TextField txtField = (TextField) ((ReadOnlyBooleanPropertyBase) o).getBean();
        int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
        String lsValue = txtField.getText();
        JSONObject jsonObject = new JSONObject();
        if (lsValue == null) {
            return;
        }
        if (!nv) {
            /*Lost Focus*/
            switch (lnIndex) {
                case 1:
                    /*Stock ID*/
                    oTrans.getModel().setStockId(lsValue);
//                   System.out.print( "STOCK ID == " + oTrans.getModel().setStockID(lsValue));
                    break;
                case 2:/*barrcode*/
                    oTrans.getModel().setBarCode(lsValue);
//                   System.out.print( "BARRCODE == " + oTrans.getModel().setBarcode(lsValue));
                    break;
                case 3:/*ALT barrcode*/
                    oTrans.getModel().setAlternateBarCode(lsValue);
//                   System.out.print( "ALT BARRCODE == " + oTrans.getModel().setAltBarcode (lsValue));
                    break;
                case 4:/*BRIEF DESC*/
                    oTrans.getModel().setBriefDescription(lsValue);
//                   System.out.print( "BRIEF DESC == " + oTrans.getModel().setBriefDescription (lsValue));
                    break;
                case 5:/*DESC*/
                    oTrans.getModel().setDescription(lsValue);
//                   System.out.print( "DESC == " + oTrans.getModel().setDescription(lsValue));
                    break;
                case 6:/*CATEGORY CODE 1*/
//                   System.out.print( "CATEGORY 1 == " + oTrans.getModel().setCategCd1(lsValue));
                    break;
                case 7:/*CATEGORY CODE 2*/
//                   System.out.print( "CATEGORY 2 == " + oTrans.getModel().setCategCd2(lsValue));
                    break;
                case 8:/*CATEGORY CODE 3*/
//                   System.out.print( "CATEGORY 3 == " + oTrans.getModel().setCategCd3(lsValue));
                    break;
                case 9:/*CATEGORY CODE 4*/
//                   System.out.print( "CATEGORY 4 == " + oTrans.getModel().setCategCd4(lsValue));
                    break;
                case 10:/*Brand*/
                    break;
                case 11:/*Model*/
                    break;
                case 12:/*Color*/
                    break;
                case 13:/*Measure*/
                    break;
                case 14:/*discount 1*/
                    oTrans.getModel().setDiscountRateLevel1((Double.parseDouble(lsValue)));
                    System.out.print("discount 1 == " + oTrans.getModel().setDiscountRateLevel1((Double.parseDouble(lsValue))));
                    if ("error".equals(jsonObject.get("result"))) {
                        System.err.println((String) jsonObject.get("message"));
                        return;
                    }
                    txtField.setText(CommonUtils.NumberFormat(Double.parseDouble(lsValue), "0.00"));
                    break;
                case 15:/*discount 2*/
                    oTrans.getModel().setDiscountRateLevel2(Double.parseDouble(lsValue));
                    System.out.print("discount 2 == " + oTrans.getModel().setDiscountRateLevel2(Double.parseDouble(lsValue)));
                    if ("error".equals((String) jsonObject.get("result"))) {
                        System.err.println((String) jsonObject.get("message"));
                        return;
                    }
                    txtField.setText(CommonUtils.NumberFormat(Double.parseDouble(lsValue), "0.00"));
                    break;
                case 16:/*discount 3*/
                    oTrans.getModel().setDiscountRateLevel3((Double.parseDouble(lsValue)));
                    System.out.print("discount 3 == " + oTrans.getModel().setDiscountRateLevel3(Double.parseDouble(lsValue)));
                    if ("error".equals((String) jsonObject.get("result"))) {
                        System.err.println((String) jsonObject.get("message"));
                        return;
                    }
                    txtField.setText(CommonUtils.NumberFormat(Double.parseDouble(lsValue), "0.00"));
//                    txtField.setText(CommonUtils.NumberFormat((Number)oTrans.getIncentiveInfo(tbl_row, "nAmtGoalx"), "#,##0.00"));    
                    break;
                case 17:/*dealer discount or discount 4*/
                    oTrans.getModel().setDealerDiscountRate((Double.parseDouble(lsValue)));
                    System.out.print("discount 3 == " + oTrans.getModel().setDealerDiscountRate(Double.parseDouble(lsValue)));
                    if ("error".equals((String) jsonObject.get("result"))) {
                        System.err.println((String) jsonObject.get("message"));
                        return;
                    }
                    txtField.setText(CommonUtils.NumberFormat(Double.parseDouble(lsValue), "0.00"));
                    break;
                case 26:
                    /*MINIMUM LEVEL*/
                    oTrans.getModel().setMinimumInventoryLevel((Integer.parseInt(lsValue)));
                    System.out.print("MINIMUM LEVEL == " + oTrans.getModel().setMinimumInventoryLevel((Integer.parseInt(lsValue))));
                    if ("error".equals((String) jsonObject.get("result"))) {
                        System.err.println((String) jsonObject.get("message"));
                        return;
                    }
                    break;

                case 27:
                    /*MAXIMUM LEVEL*/
                    oTrans.getModel().setMaximumInventoryLevel((Integer.parseInt(lsValue)));
                    System.out.print("MAXIMUM LEVEL == " + oTrans.getModel().setMaximumInventoryLevel((Integer.parseInt(lsValue))));
                    if ("error".equals((String) jsonObject.get("result"))) {
                        System.err.println((String) jsonObject.get("message"));
                        return;
                    }
                    break;
                case 18:/*cost*/
//                   oTrans.getModel().set(Double.parseDouble(lsValue.replace(",", lsValue))));
//                   System.out.print( "discount 3 == " + oTrans.getModel().setUnitPrice(Double.parseDouble(lsValue.replace(",", lsValue))));
//                   if ("error".equals((String) jsonObject.get("result"))) {
//                        System.err.println((String) jsonObject.get("message"));
//                        return;
//                    }
//                   txtField.setText(CommonUtils.NumberFormat(Double.parseDouble(lsValue), "#,##0.00"));
                    break;
                case 19:/*SRP*/
                    oTrans.getModel().setSellingPrice((Double.parseDouble(lsValue.replace(",", lsValue))));
                    System.out.print("discount 3 == " + oTrans.getModel().setSellingPrice(Double.parseDouble(lsValue.replace(",", lsValue))));
                    if ("error".equals((String) jsonObject.get("result"))) {
                        System.err.println((String) jsonObject.get("message"));
                        return;
                    }
                    txtField.setText(CommonUtils.NumberFormat(Double.parseDouble(lsValue), "#,##0.00"));
                    break;
                case 20:/*superseded*/
                    oTrans.getModel().setSupersededId(lsValue);
                    System.out.print("supersed == " + oTrans.getModel().setSupersededId(lsValue));
                    break;

                case 21:
                    /*shelf life*/
                    if (lsValue == "") {
                        lsValue = "0";
                    }
                    System.out.print("shelflife == " + lsValue);
//                   if(lsValue.isEmpty()) lsValue = "0";
                    oTrans.getModel().setShelfLife((Integer.parseInt(lsValue)));
                    System.out.print("shelflife == " + oTrans.getModel().setShelfLife((Integer.parseInt(lsValue))));
                    if ("error".equals((String) jsonObject.get("result"))) {
                        System.err.println((String) jsonObject.get("message"));
                        return;
                    }
                    break;

                case 25:
                    /*Quantity*/
//                    oTrans.getModel  getSubUnit().getMaster().get(pnRow).setQuantity(Integer.parseInt(lsValue));
//                    loadSubUnitData();
                    break;

            }
        } else {
            txtField.selectAll();
        }
    };
//    

    /*Text Field with search*/
    private void txtField_KeyPressed(KeyEvent event) {
        TextField txtField = (TextField) event.getSource();
        int lnIndex = Integer.parseInt(((TextField) event.getSource()).getId().substring(8, 10));
        String lsValue = (txtField.getText() == null ? "" : txtField.getText());
        JSONObject poJson;
        switch (event.getCode()) {
            case F3:
                switch (lnIndex) {
                    case 06:

                        poJson = new JSONObject();

                        poJson = oParameters.Category().searchRecord(lsValue, false);

//                        poJson =  oTrans.SearchMaster(6,lsValue, false);
//                           System.out.println("poJson = " + poJson.toJSONString());
                        if ("error".equalsIgnoreCase(poJson.get("result").toString())) {
                            ShowMessageFX.Information((String) poJson.get("message"), "Computerized Acounting System", pxeModuleName);
                        }
//                            System.out.print( "category 1 == " + oTrans.getMaster(33));
//                           if(!oTrans.getModel().get().equals(oTrans.getModel().getMainCategoryCode())){
//                               txtField07.clear();
//                           }
//                        txtField06.setText((String) oParameters.Category().getModel().getDescription());
                        
//                           System.out.println(oTrans.getModel().getCategCd1());
//                           initSubItemForm();
                        break;
                    case 7:
//                        poJson = new JSONObject();
//                        poJson =  oTrans.SearchMaster(7,lsValue, false);
//                           System.out.println("poJson = " + poJson.toJSONString());
//                           if("error".equalsIgnoreCase(poJson.get("result").toString())){
//                               ShowMessageFX.Information((String) poJson.get("message"), "Computerized Acounting System", pxeModuleName);                              
//                           }
//                            System.out.print( "category 2 == " + oTrans.getMaster(34));
//                           txtField06.setText((String) oTrans.getModel().getCategName1()); 
//                           txtField07.setText((String) oTrans.getMaster(34));    
//                           cmbField01.setValue((String) oTrans.getMaster(42));  
                        break;
                    case 8:
//                        poJson = new JSONObject();
//                        poJson =  oTrans.SearchMaster(8,lsValue, false);
//                           System.out.println("poJson = " + poJson.toJSONString());
//                           if("error".equalsIgnoreCase(poJson.get("result").toString())){
//                               ShowMessageFX.Information((String) poJson.get("message"), "Computerized Acounting System", pxeModuleName);                              
//                           }
//                            System.out.print( "category 3 == " + oTrans.getMaster(35));
//                           txtField08.setText((String) oTrans.getMaster(35));      
                        break;
                    case 9:
//                        poJson = new JSONObject();
//                        poJson =  oTrans.SearchMaster(9,lsValue, false);
//                           System.out.println("poJson = " + poJson.toJSONString());
//                           if("error".equalsIgnoreCase(poJson.get("result").toString())){
//                               ShowMessageFX.Information((String) poJson.get("message"), "Computerized Acounting System", pxeModuleName);                              
//                           }
//                            System.out.print( "category 4 == " + oTrans.getMaster(36));
//                           txtField09.setText((String) oTrans.getMaster(36));      
                        break;
                    case 10:
                        /*search Brand*/
//                        poJson = new JSONObject();
//                        poJson =  oTrans.SearchMaster(10,lsValue, false);
//                           System.out.println("poJson = " + poJson.toJSONString());
//                           if("error".equalsIgnoreCase(poJson.get("result").toString())){
//                               ShowMessageFX.Information((String) poJson.get("message"), "Computerized Acounting System", pxeModuleName);                              
//                           }
//                            System.out.print( "brand == " + oTrans.getMaster(37));
//                           txtField10.setText((String) oTrans.getMaster(37));      
                        break;
                    case 11:
                        /*search Model*/
                        poJson = new JSONObject();
//                        poJson =  oTrans.SearchMaster(11,lsValue, false);
//                           System.out.println("poJson = " + poJson.toJSONString());
//                           if("error".equalsIgnoreCase(poJson.get("result").toString())){
//                               ShowMessageFX.Information((String) poJson.get("message"), "Computerized Acounting System", pxeModuleName);                              
//                           }
//                            System.out.print( "Model == " + oTrans.getMaster(38));
//                           txtField11.setText((String) oTrans.getMaster(38));      
                        break;
                    case 12:
                        /*search color*/
                        poJson = new JSONObject();
//                        poJson =  oTrans.SearchMaster(12,lsValue, false);
//                           System.out.println("poJson = " + poJson.toJSONString());
//                           if("error".equalsIgnoreCase(poJson.get("result").toString())){
//                               ShowMessageFX.Information((String) poJson.get("message"), "Computerized Acounting System", pxeModuleName);                              
//                           }
//                            System.out.print( "Color == " + oTrans.getMaster(40));
//                           txtField12.setText((String) oTrans.getMaster(40));      
                        break;

                    case 13:
                        /*search measure*/
                        poJson = new JSONObject();
//                        poJson =  oTrans.SearchMaster(13,lsValue, false);
//                           System.out.println("poJson = " + poJson.toJSONString());
//                           if("error".equalsIgnoreCase(poJson.get("result").toString())){
//                               ShowMessageFX.Information((String) poJson.get("message"), "Computerized Acounting System", pxeModuleName);                              
//                           }
//                            System.out.print( "measure == " + oTrans.getMaster(40));
//                           txtField13.setText((String) oTrans.getMaster(41));      
                        break;
                    case 22:
                        /*search Barrcode for sub unit*/
                        poJson = new JSONObject();
//                        poJson = oTrans.SearchSubUnit(pnRow, 3, lsValue, true);
//                            if ("error".equals((String) poJson.get("result"))){
//                                ShowMessageFX.Information((String) poJson.get("message"), "Computerized Acounting System", pxeModuleName);
//
//                                txtField22.clear();
//                                break;
//                            }
//                        System.out.print("\neditmode on browse == " +pnEditMode);
//                        loadSubUnit();
//                        loadSubUnitData();
//                        txtField25.requestFocus();
                        break;
                }
            case ENTER:
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
//    /*Text seek/search*/
//    private void txtSeeks_KeyPressed(KeyEvent event){
//        TextField txtSeeks = (TextField)event.getSource();
//        int lnIndex = Integer.parseInt(((TextField)event.getSource()).getId().substring(8,10));
//        String lsValue = (txtSeeks.getText() == null ?"": txtSeeks.getText());
//        JSONObject poJSON;
//        switch (event.getCode()) {
//            case F3:
//                switch (lnIndex){
//                    
//                    case 1: /*search Barrcode*/
//                        System.out.print("LSVALUE OF SEARCH 1 ==== " + lsValue);
//                        poJSON = oTrans.searchRecord(lsValue, true);
//                        if ("error".equals((String) poJSON.get("result"))){
//                            ShowMessageFX.Information((String) poJSON.get("message"), "Computerized Acounting System", pxeModuleName);
//                           
//                            txtSeeks01.clear();
//                            break;
//                        }
//                        txtSeeks01.setText(oTrans.getModel().getBarcode());
//                        txtSeeks02.setText(oTrans.getModel().getDescription());
//                        pnEditMode = oTrans.getEditMode();
//                        loadInventory();
//                        loadSubUnitData();
//                        loadSubUnit();
//                        initTable();
//                        break;
//                    case 2 :
//                         poJSON = oTrans.searchRecord(lsValue, false);
//                        if ("error".equals((String) poJSON.get("result"))){
//                            ShowMessageFX.Information((String) poJSON.get("message"), "Computerized Acounting System", pxeModuleName);
//                           
//                            txtSeeks01.clear();
//                            break;
//                        }
//                        pnEditMode = oTrans.getEditMode();
//                        
//                        txtSeeks01.setText(oTrans.getModel().getBarcode());
//                        txtSeeks02.setText(oTrans.getModel().getDescription());
//                        System.out.print("\neditmode on browse == " +pnEditMode);
//                        loadInventory();
//                        loadSubUnitData();
//                        loadSubUnit();
//                        initTable();
//                        System.out.println("EDITMODE sa cancel= " + pnEditMode);
//                        break;
//                }
//            case ENTER:
//                
//        }
//        switch (event.getCode()){
//        case ENTER:
//            CommonUtils.SetNextFocus(txtSeeks);
//        case DOWN:
//            CommonUtils.SetNextFocus(txtSeeks);
//            break;
//        case UP:
//            CommonUtils.SetPreviousFocus(txtSeeks);
//        }
//    }
//    private void clearAllFields() {
//        // Arrays of TextFields grouped by sections
//        TextField[][] allFields = {
//            // Text fields related to specific sections
//            {txtSeeks01, txtSeeks02, txtField01, txtField02, txtField03, txtField04,
//             txtField05, txtField06, txtField07, txtField08, txtField09,txtField10, 
//             txtField11, txtField12, txtField13, txtField14, txtField15,txtField16, 
//             txtField17, txtField18, txtField19, txtField20, txtField21,txtField22, 
//             txtField23, txtField24, txtField25, txtField26, txtField27},
//
//        };
//        chkField01.setSelected(false);
//        chkField02.setSelected(false);
//        chkField03.setSelected(false);
//        chkField04.setSelected(false);
//        cmbField01.setValue(null);
//        cmbField01.setValue(null);
//        
//        // Loop through each array of TextFields and clear them
//        for (TextField[] fields : allFields) {
//            for (TextField field : fields) {
//                field.clear();
//            }
//        }
//        data.clear();
//    }
//    private void loadSubUnit(){
//        if(oTrans.getSubUnit()!= null){
//            txtField22.setText((String) oTrans.getSubUnit().getMaster(pnRow, "xBarCodeU"));
//            txtField23.setText((String) oTrans.getSubUnit().getMaster(pnRow, "xDescripU"));        
//            txtField25.setText(oTrans.getSubUnit().getMaster(pnRow, "nQuantity").toString());
//            txtField24.setText((String) oTrans.getSubUnit().getMaster(pnRow, "xMeasurNm"));
//            System.out.println("sub stock id = " + oTrans.getSubUnit().getMaster(pnRow, "sStockIDx"));
//        }
//    
//    }
//    
//    private void loadSubUnitData(){
//        int lnCtr;
//        data.clear();
//        if(oTrans.getSubUnit()!= null){
//            for (lnCtr = 0; lnCtr < oTrans.getSubUnit().getMaster().size(); lnCtr++){
//                data.add(new ModelInvSubUnit(String.valueOf(lnCtr + 1),
//                     oTrans.getSubUnit(lnCtr, "xBarCodeU").toString(), 
//                    (String)oTrans.getSubUnit(lnCtr, "xDescripU"),
//                    oTrans.getSubUnit(lnCtr, "nQuantity").toString(), 
//                    (String)oTrans.getSubUnit(lnCtr, "xMeasurNm")));  
//            }
//        }
//    }

    private void loadInventory() {
        if (pnEditMode == EditMode.READY
                || pnEditMode == EditMode.ADDNEW
                || pnEditMode == EditMode.UPDATE) {

            txtField01.setText((String) oTrans.getModel().getStockId());
            txtField02.setText((String) oTrans.getModel().getBarCode());
            txtField03.setText((String) oTrans.getModel().getAlternateBarCode());
            txtField04.setText((String) oTrans.getModel().getBriefDescription());
            txtField05.setText((String) oTrans.getModel().getDescription());
            txtField06.setText((String) oTrans.getModel().getDescription());
            txtField07.setText((String) oTrans.getModel().CategoryLevel2().getDescription());
//            txtField08.setText((String) oTrans.getModel().CategoryLevel3().getDescription());
//            txtField09.setText((String) oTrans.getModel().CategoryLevel4().getDescription());

//            txtField10.setText((String) oTrans.getModel().Brand().getDescription());
//            txtField11.setText((String) oTrans.getModel().Model().getDescription());
//            txtField12.setText((String) oTrans.getModel().Color().getDescription());
//            txtField13.setText((String) oTrans.getModel().Measure().getMeasureName());
//
//            txtField14.setText(CommonUtils.NumberFormat(oTrans.getModel().getDiscountRateLevel1(), "#,##0.00"));
//            txtField15.setText(CommonUtils.NumberFormat(oTrans.getModel().getDiscountRateLevel2(), "#,##0.00"));
//            txtField16.setText(CommonUtils.NumberFormat(oTrans.getModel().getDiscountRateLevel3(), "#,##0.00"));
//            txtField17.setText(CommonUtils.NumberFormat(oTrans.getModel().getDealerDiscountRate(), "#,##0.00"));
//
//            txtField26.setText(String.valueOf(oTrans.getModel().getMinimumInventoryLevel()));
//            txtField27.setText(String.valueOf(oTrans.getModel().getMaximumInventoryLevel()));
////            txtField18.setText(CommonUtils.NumberFormat(Double.parseDouble(oTrans.getModel().getUnitPrice().toString()), "#,##0.00"));
//            txtField19.setText(CommonUtils.NumberFormat(oTrans.getModel().getSellingPrice(), "#,##0.00"));
//
//            txtField20.setText((String) oTrans.getModel().getSupersededId());
//            txtField21.setText(String.valueOf(oTrans.getModel().getShelfLife()));
//            cmbField01.setValue(String.valueOf(oTrans.getModel().InventoryType().getDescription()));
//            chkField01.setSelected("1".equals(oTrans.getModel().isSerialized()));
//            chkField02.setSelected("1".equals(oTrans.getModel().isComboInventory()));
//            chkField03.setSelected("1".equals(oTrans.getModel().isWithPromo()));
//            chkField04.setSelected((oTrans.getModel().isActive()));

//            lblStatus.setText(chkField04.isSelected() ? "ACTIVE" : "INACTIVE");

        }
    }
//    @FXML
//    void chkFiled04_Clicked(MouseEvent event) {
//        boolean isChecked = chkField04.isSelected();
////        oTrans.getModel().setActive((isChecked));
//        lblStatus.setText(chkField04.isSelected() ? "ACTIVE" : "INACTIVE");
//    }
//    
//    private void initTable() {
//        index01.setStyle("-fx-alignment: CENTER;");
//        index02.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
//        index03.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
//        index04.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
//        index05.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
////        
//        index01.setCellValueFactory(new PropertyValueFactory<>("index01"));
//        index02.setCellValueFactory(new PropertyValueFactory<>("index02"));
//        index03.setCellValueFactory(new PropertyValueFactory<>("index03"));
//        index04.setCellValueFactory(new PropertyValueFactory<>("index04"));
//        index05.setCellValueFactory(new PropertyValueFactory<>("index05"));
//        
//        tblSubItems.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
//            TableHeaderRow header = (TableHeaderRow) tblSubItems.lookup("TableHeaderRow");
//            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
//                header.setReordering(false);
//            });
//        });
//        tblSubItems.setItems(data);
//        tblSubItems.autosize();
//    }
//    
//    @FXML
//    private void tblSubUnit_Clicked (MouseEvent event) {
//        pnRow = tblSubItems.getSelectionModel().getSelectedIndex();
//        loadSubUnitData();
//        if (pnRow >= 0){
//            loadSubUnit();
//            txtField22.requestFocus();
//        }
//    }
//    
//    private void clearSubItemTxtField(){
//        txtField22.clear();
//        txtField23.clear();
//        txtField24.clear();
//        txtField25.clear();
//    }
}
// 
