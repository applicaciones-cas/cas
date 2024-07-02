/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.cas.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
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
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.cas.inventory.base.InvMaster;
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
    
    private double xOffset = 0;
    private double yOffset = 0;
    
    private boolean state = false;
    private boolean pbLoaded = false;
    private int pnInventory = 0; 
    
    private InvMaster oTrans;
    
    @FXML
    private AnchorPane AnchorMain,AnchorTable,AnchorInput;
    @FXML
    private GridPane gridEditable;
    @FXML
    private GridPane gridFix;

    @FXML
    private TextField txtSeeks02;

    @FXML
    private TextField txtSeeks01;

    @FXML
    private HBox hbButtons;

    @FXML
    private Button btnBrowse;

//    @FXML
//    private Button btnNew;

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
        dpField01.setValue(LocalDate.now());
        pnEditMode = EditMode.UNKNOWN;        
        initButton(pnEditMode);
        initTabAnchor();
        ClickButton();
        InitTextFields();
        pbLoaded = true;

        // TODO
    }    
    
    /*Handle button click*/
    private void ClickButton() {
        btnCancel.setOnAction(this::handleButtonAction);
//        btnNew.setOnAction(this::handleButtonAction);
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
                             pnEditMode = EditMode.UNKNOWN;     
                             initButton(pnEditMode);
                             initTabAnchor();
                             clearAllFields();
                        }
                    break;
                case "btnSave":
                    
                        JSONObject saveResult = oTrans.saveRecord();
                        if ("success".equals((String) saveResult.get("result"))){
                            System.err.println((String) saveResult.get("message"));
                            ShowMessageFX.Information((String) saveResult.get("message"), "Computerized Acounting System", pxeModuleName);
                            clearAllFields();
                            pnEditMode = EditMode.UNKNOWN;
                            initButton(pnEditMode);
                            initTabAnchor();
                            System.out.println("Record saved successfully.");
                        } else {
                            ShowMessageFX.Information((String)saveResult.get("message"), "Computerized Acounting System", pxeModuleName);
                            System.out.println("Record not saved successfully.");
                            System.out.println((String) saveResult.get("message"));
                        }
                        
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
                        System.out.print("\neditmode on browse == " + pnEditMode);
                        
                        initTabAnchor();
                        loadInventory();
                    break;
                case "btnLedger":
                        {
                            try {
                                loadLedger(oTrans.getInvModel().getStockID());
                            } catch (SQLException ex) {
                                Logger.getLogger(InventoryDetailController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }  
                    break;
                case "btnSerial":
                    if (!txtField01.getText().isEmpty()){
                        if (chkField01.isSelected()){
                            {
                                try {
                                    loadSerial(oTrans.getInvModel().getStockID());
                                } catch (SQLException ex) {
                                    Logger.getLogger(InventoryDetailController.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            } 
                        } else {
                            ShowMessageFX.Information("This Inventory is not serialize!", "Computerized Acounting System", pxeModuleName);
                        }
                    }
                    break;

        }
    }
    
    }
    private void loadSerial(String fsCode) throws SQLException {
        try {
            Stage stage = new Stage();

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/cas/views/InventorySerial.fxml"));

            InventorySerialController loControl = new InventorySerialController();
            loControl.setGRider(oApp);
            // loControl.setIncentiveObject(oTrans);
            // loControl.setIncentiveCode(fsCode);
            // loControl.setTableRow(fnRow);
            loControl.setFsCode(oTrans);

            fxmlLoader.setController(loControl);

            // Load the main interface
            Parent parent = fxmlLoader.load();
            parent.setStyle("-fx-background-color: rgba(0, 0, 0, 1);");

            // Set up dragging
            parent.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    xOffset = event.getSceneX();
                    yOffset = event.getSceneY();
                }
            });
            parent.setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    stage.setX(event.getScreenX() - xOffset);
                    stage.setY(event.getScreenY() - yOffset);
                }
            });

            // Set the main interface as the scene
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Inventory Ledger");
            stage.showAndWait();

            // loadDetail();
            // loadIncentives();
        } catch (IOException e) {
            e.printStackTrace();
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
            System.exit(1);
        }
    }
    private void loadLedger(String fsCode) throws SQLException {
        try {
            Stage stage = new Stage();

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/cas/views/InventoryLedger.fxml"));

            InventoryLedgerController loControl = new InventoryLedgerController();
            loControl.setGRider(oApp);
            loControl.setFsCode(oTrans);
            // loControl.setIncentiveObject(oTrans);
            // loControl.setIncentiveCode(fsCode);
            // loControl.setTableRow(fnRow);

            fxmlLoader.setController(loControl);

            // Load the main interface
            Parent parent = fxmlLoader.load();
            parent.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");
        //        parent.getChildrenUnmodifiable().add(parent);
            // Set up dragging
            parent.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    xOffset = event.getSceneX();
                    yOffset = event.getSceneY();
                }
            });
            parent.setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    stage.setX(event.getScreenX() - xOffset);
                    stage.setY(event.getScreenY() - yOffset);
                }
            });


            // Set the main interface as the scene  
            Scene scene = new Scene(parent);

            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.initModality(Modality.APPLICATION_MODAL);

            stage.setTitle("Inventory Ledger");
            stage.showAndWait();

            // loadDetail();
            // loadIncentives();
        } catch (IOException e) {
            e.printStackTrace();
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
            System.exit(1);
        }
    }

    /*USE TO DISABLE ANCHOR BASE ON INITMODE*/
    private void initTabAnchor(){
        
        boolean pbValue = pnEditMode == EditMode.ADDNEW || 
                pnEditMode == EditMode.UPDATE;
        System.out.println("\ninitabanchor mode == " + pbValue);
        System.out.println("\ninitabanchor mode == " + pnEditMode);
        AnchorInput.setDisable(!pbValue);
        gridFix.setDisable(!pbValue);
        gridEditable.setDisable(!pbValue);
//        AnchorTable.setDisable(pbValue);

        if (pnEditMode== 0) {
             gridFix.setDisable(pbValue);
             AnchorInput.setDisable(pbValue);
             gridEditable.setDisable(!pbValue);
        }
        
    }
    
    /*TO CONTROL BUTTONS BASE ON INITMODE*/
 private void initButton(int fnValue) {
    boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);
    
    // Set visibility and manageability for buttons based on lbShow
    btnSave.setVisible(lbShow);
    btnSave.setManaged(lbShow);
    
    btnSearch.setVisible(lbShow);
    btnSearch.setManaged(lbShow);
    
    btnCancel.setVisible(lbShow);
    btnCancel.setManaged(lbShow);
    
    btnLedger.setVisible(true);
    btnLedger.setManaged(true);
    
    btnSerial.setVisible(true);
    btnSerial.setManaged(true);
    
    btnClose.setVisible(true);
    btnClose.setManaged(true);
    
    btnUpdate.setVisible(!lbShow);
    btnUpdate.setManaged(!lbShow);
    
    btnBrowse.setVisible(!lbShow);
    btnBrowse.setManaged(!lbShow);
    
    cmbField01.setDisable(!lbShow);
    
    txtSeeks01.setDisable(!lbShow);
    txtSeeks02.setDisable(!lbShow);
    
    if (lbShow) {
        txtSeeks01.clear();
        txtSeeks02.clear();
        
        txtSeeks01.setDisable(true);
        txtSeeks02.setDisable(true);
        
        btnUpdate.setManaged(false);
        btnBrowse.setManaged(false);
        btnCancel.setManaged(true);
        btnLedger.setManaged(true);
        btnSerial.setManaged(true);
        btnClose.setManaged(true);
    } else {
        txtSeeks01.setDisable(false);
        txtSeeks02.setDisable(false);
        
        txtSeeks01.requestFocus();
        
        btnUpdate.setManaged(true);
        btnBrowse.setManaged(true);
        btnCancel.setManaged(false);
        btnLedger.setManaged(true);
        btnSerial.setManaged(true);
        btnClose.setManaged(true);
    }
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
            cmbField01.setValue(String.valueOf(oTrans.getInvModel().getCategName2()));
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
        txtField22.setOnKeyPressed(this::txtField_KeyPressed);
        txtField23.setOnKeyPressed(this::txtField_KeyPressed);

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
//                    oTrans.getModel().setStockID(lsValue);
//                   System.out.print( "STOCK ID == " + oTrans.getModel().setStockID(lsValue));
                    break;
//                case 2:/*barrcode*/
//                   oTrans.getModel().setBarcode(lsValue);
//                   System.out.print( "BARRCODE == " + oTrans.getModel().setBarcode(lsValue));
//                    break;
//                case 3:/*ALT barrcode*/   
//                   oTrans.getModel().setAltBarcode (lsValue);
//                   System.out.print( "ALT BARRCODE == " + oTrans.getModel().setAltBarcode (lsValue));
//                    break;
//                case 4:/*BRIEF DESC*/
//                   oTrans.getModel().setBriefDescription(lsValue);
//                   System.out.print( "BRIEF DESC == " + oTrans.getModel().setBriefDescription (lsValue));
//                    break;
//                case 5:/*DESC*/
//                   oTrans.getModel().setDescription(lsValue);
//                   System.out.print( "DESC == " + oTrans.getModel().setDescription(lsValue));
//                    break;
//                case 6:/*CATEGORY CODE 1*/
//                   oTrans.getModel().setCategCd1(lsValue);
//                   System.out.print( "CATEGORY 1 == " + oTrans.getModel().setCategCd1(lsValue));
//                    break;
//                case 7:/*CATEGORY CODE 2*/
//                  oTrans.getModel().setCategCd2(lsValue);
//                   System.out.print( "CATEGORY 2 == " + oTrans.getModel().setCategCd2(lsValue));
//                    break;
//                case 8:/*CATEGORY CODE 3*/
//                   oTrans.getModel().setCategCd3(lsValue);
//                   System.out.print( "CATEGORY 3 == " + oTrans.getModel().setCategCd3(lsValue));
//                    break;
//                case 9:/*CATEGORY CODE 4*/
//                   oTrans.getModel().setCategCd4(lsValue);
//                   System.out.print( "CATEGORY 4 == " + oTrans.getModel().setCategCd4(lsValue));
//                    break;
//                case 10:/*Brand*/
////                   oTrans.getModel().setBrandCode(lsValue);
////                   System.out.print( "BRAND == " + oTrans.getModel().setBrandCode(lsValue));
//                    break;
//                case 11:/*Model*/
////                   oTrans.getModel().setModelCode(lsValue);
////                   System.out.print( "Model == " + oTrans.getModel().setModelCode(lsValue));
//                    break;
//                case 12:/*Color*/
////                   oTrans.getModel().setColorCode(lsValue);
////                   System.out.print( "color == " + oTrans.getModel().setColorCode(lsValue));
//                    break;
//                case 13:/*Measure*/
////                   oTrans.getModel().setMeasureID(lsValue);
////                   System.out.print( "Measure == " + oTrans.getModel().setMeasureID(lsValue));
//                    break;
//                case 14:/*discount 1*/
//                   oTrans.getModel().setDiscountLvl1((Double.parseDouble(lsValue)));
//                   System.out.print( "discount 1 == " + oTrans.getModel().setDiscountLvl1((Double.parseDouble(lsValue))));
//                   if ("error".equals(jsonObject.get("result"))) {
//                        System.err.println((String) jsonObject.get("message"));
//                        return;
//                    }
//                    break;
//                case 15:/*discount 2*/
//                    oTrans.getModel().setDiscountLvl2(Double.parseDouble(lsValue));
//                   System.out.print( "discount 2 == " + oTrans.getModel().setDiscountLvl2(Double.parseDouble(lsValue)));
//                   if ("error".equals((String) jsonObject.get("result"))) {
//                        System.err.println((String) jsonObject.get("message"));
//                        return;
//                    }
//                    break;
//                case 16:/*discount 3*/
//                   oTrans.getModel().setDiscountLevel3((Double.parseDouble(lsValue)));
//                   System.out.print( "discount 3 == " + oTrans.getModel().setDiscountLevel3(Double.parseDouble(lsValue)));
//                   if ("error".equals((String) jsonObject.get("result"))) {
//                        System.err.println((String) jsonObject.get("message"));
//                        return;
//                    }
//                    break;
//                case 17:/*dealer discount or discount 4*/
//                   oTrans.getModel().setDealerDiscount((Double.parseDouble(lsValue)));
//                   System.out.print( "discount 3 == " + oTrans.getModel().setDealerDiscount(Double.parseDouble(lsValue)));
//                   if ("error".equals((String) jsonObject.get("result"))) {
//                        System.err.println((String) jsonObject.get("message"));
//                        return;
//                    }
//                    break;
//                case 26: /*MINIMUM LEVEL*/
//                   
//                   oTrans.getModel().setMinLevel((Integer.parseInt(lsValue)));
//                   System.out.print( "MINIMUM LEVEL == " + oTrans.getModel().setMinLevel((Integer.parseInt(lsValue))));
//                   if ("error".equals((String) jsonObject.get("result"))) {
//                        System.err.println((String) jsonObject.get("message"));
//                        return;
//                    }
//                    break;
//                    
//                case 27: /*MAXIMUM LEVEL*/
//                   
//                   oTrans.getModel().setMaxLevel((Integer.parseInt(lsValue)));
//                   System.out.print( "MAXIMUM LEVEL == " + oTrans.getModel().setMaxLevel((Integer.parseInt(lsValue))));
//                   if ("error".equals((String) jsonObject.get("result"))) {
//                        System.err.println((String) jsonObject.get("message"));
//                        return;
//                    }
//                    break;    
//                case 18:/*cost*/
//                   oTrans.getModel().setUnitPrice((Double.parseDouble(lsValue)));
//                   System.out.print( "discount 3 == " + oTrans.getModel().setUnitPrice(Double.parseDouble(lsValue)));
//                   if ("error".equals((String) jsonObject.get("result"))) {
//                        System.err.println((String) jsonObject.get("message"));
//                        return;
//                    }
//                    break;
//                case 19:/*SRP*/
//                   oTrans.getModel().setSelPrice((Double.parseDouble(lsValue)));
//                   System.out.print( "discount 3 == " + oTrans.getModel().setSelPrice(Double.parseDouble(lsValue)));
//                   if ("error".equals((String) jsonObject.get("result"))) {
//                        System.err.println((String) jsonObject.get("message"));
//                        return;
//                    }
//                    break;
//                case 20:/*superseded*/
//                   oTrans.getModel().setSupersed(lsValue);
//                   System.out.print( "supersed == " + oTrans.getModel().setSupersed(lsValue));
//                    break;
//                    
//                case 21: /*shelf life*/
//                   if(lsValue.isEmpty()) lsValue = "0";
//                   oTrans.getModel().setShlfLife((Integer.parseInt(lsValue)));
//                   System.out.print( "shelflife == " + oTrans.getModel().setShlfLife((Integer.parseInt(lsValue))));
//                   if ("error".equals((String) jsonObject.get("result"))) {
//                        System.err.println((String) jsonObject.get("message"));
//                        return;
//                    }
//                    break;
                
            }                  
        } else
            txtField.selectAll();
    };
    
    private void txtField_KeyPressed(KeyEvent event){
        TextField txtField = (TextField)event.getSource();
        int lnIndex = Integer.parseInt(((TextField)event.getSource()).getId().substring(8,10));
        String lsValue = (txtField.getText() == null ?"": txtField.getText());
        JSONObject poJson;
        switch (event.getCode()) {
            case F3:
                switch (lnIndex){
//                    case 06: /*search category 1*/
//                        poJson = new JSONObject();
//                        poJson =  oTrans.SearchMaster(6,lsValue, false);
//                           System.out.println("poJson = " + poJson.toJSONString());
//                           if("error".equalsIgnoreCase(poJson.get("result").toString())){
//                               ShowMessageFX.Information((String) poJson.get("message"), "Computerized Acounting System", pxeModuleName);                              
//                           }
//                            System.out.print( "category 1 == " + oTrans.getMaster(32));
////                           txtField06.setText((String) oTrans.getModel().getCategName1()); 
//                           cmbField01.setValue((String) oTrans.getMaster(32));
//                        break;
//                    case 7: /*search category 2*/
//                        poJson = new JSONObject();
//                        poJson =  oTrans.SearchMaster(7,lsValue, false);
//                           System.out.println("poJson = " + poJson.toJSONString());
//                           if("error".equalsIgnoreCase(poJson.get("result").toString())){
//                               ShowMessageFX.Information((String) poJson.get("message"), "Computerized Acounting System", pxeModuleName);                              
//                           }
//                            System.out.print( "category 2 == " + oTrans.getMaster(33));
//                           txtField07.setText((String) oTrans.getMaster(33));      
//                        break;
//                    case 8: /*search category 3*/
//                        poJson = new JSONObject();
//                        poJson =  oTrans.SearchMaster(8,lsValue, false);
//                           System.out.println("poJson = " + poJson.toJSONString());
//                           if("error".equalsIgnoreCase(poJson.get("result").toString())){
//                               ShowMessageFX.Information((String) poJson.get("message"), "Computerized Acounting System", pxeModuleName);                              
//                           }
//                            System.out.print( "category 3 == " + oTrans.getMaster(34));
//                           txtField08.setText((String) oTrans.getMaster(34));      
//                        break;
//                    case 9: /*search category 4*/
//                        poJson = new JSONObject();
//                        poJson =  oTrans.SearchMaster(9,lsValue, false);
//                           System.out.println("poJson = " + poJson.toJSONString());
//                           if("error".equalsIgnoreCase(poJson.get("result").toString())){
//                               ShowMessageFX.Information((String) poJson.get("message"), "Computerized Acounting System", pxeModuleName);                              
//                           }
//                            System.out.print( "category 4 == " + oTrans.getMaster(35));
//                           txtField09.setText((String) oTrans.getMaster(35));      
//                        break;
//                    case 10: /*search Brand*/
//                        poJson = new JSONObject();
//                        poJson =  oTrans.SearchMaster(10,lsValue, false);
//                           System.out.println("poJson = " + poJson.toJSONString());
//                           if("error".equalsIgnoreCase(poJson.get("result").toString())){
//                               ShowMessageFX.Information((String) poJson.get("message"), "Computerized Acounting System", pxeModuleName);                              
//                           }
//                            System.out.print( "brand == " + oTrans.getMaster(36));
//                           txtField10.setText((String) oTrans.getMaster(36));      
//                        break;  
//                    case 11: /*search Model*/
//                        poJson = new JSONObject();
//                        poJson =  oTrans.SearchMaster(11,lsValue, false);
//                           System.out.println("poJson = " + poJson.toJSONString());
//                           if("error".equalsIgnoreCase(poJson.get("result").toString())){
//                               ShowMessageFX.Information((String) poJson.get("message"), "Computerized Acounting System", pxeModuleName);                              
//                           }
//                            System.out.print( "Model == " + oTrans.getMaster(37));
//                           txtField11.setText((String) oTrans.getMaster(37));      
//                        break;
//                    case 12: /*search color*/
//                        poJson = new JSONObject();
//                        poJson =  oTrans.SearchMaster(12,lsValue, false);
//                           System.out.println("poJson = " + poJson.toJSONString());
//                           if("error".equalsIgnoreCase(poJson.get("result").toString())){
//                               ShowMessageFX.Information((String) poJson.get("message"), "Computerized Acounting System", pxeModuleName);                              
//                           }
//                            System.out.print( "Color == " + oTrans.getMaster(39));
//                           txtField12.setText((String) oTrans.getMaster(39));      
//                        break;
                    
                    case 22:
                        poJson = new JSONObject();
                        poJson = oTrans.SearchMaster(4,lsValue, false);
                        System.out.println("poJson = " + poJson.toJSONString());
                        if("error".equalsIgnoreCase(poJson.get("result").toString())){
                            ShowMessageFX.Information((String) poJson.get("message"), "Computerized Acounting System", pxeModuleName);                              
                        }
                        System.out.print( "Location == " + oTrans.getMaster(27));
                        txtField22.setText((String) oTrans.getMaster(27));  
                        break;
                    case 23:
                        poJson = new JSONObject();
                        poJson = oTrans.SearchMaster(3,lsValue, false);
                        System.out.println("poJson = " + poJson.toJSONString());
                        if("error".equalsIgnoreCase(poJson.get("result").toString())){
                            ShowMessageFX.Information((String) poJson.get("message"), "Computerized Acounting System", pxeModuleName);                              
                        }
                        System.out.print( "Warehouse == " + oTrans.getMaster(26));
                        txtField23.setText((String) oTrans.getMaster(26));  
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
             txtField23, txtField24, txtField25, txtField26, txtField27,txtField28,txtField29, 
             txtField30, txtField31, txtField32, txtField33, txtField34},

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

    private Stage getStage(){
	return (Stage) txtField01.getScene().getWindow();
    }
}
