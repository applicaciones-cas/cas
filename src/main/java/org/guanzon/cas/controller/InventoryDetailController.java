/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.cas.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
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
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Screen;
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
    public StackPane overlay;
    
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
    private Text lblShelf, lblMeasure;

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
//
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        oTrans = new InvMaster(oApp, true);
        if (oTransnox == null || oTransnox.isEmpty()) { // Check if oTransnox is null or empty
            pnEditMode = EditMode.UNKNOWN;
        initButton(pnEditMode);
        }
        oTrans.setRecordStatus("0123");
//        dpField01.setValue(LocalDate.now());
        pnEditMode = EditMode.UNKNOWN;        
        initButton(pnEditMode);
        initTabAnchor();
        ClickButton();
        InitTextFields();
        pbLoaded = true;
        overlay.setVisible(false);
        
    }    
    
    /*Handle button click*/
    private void ClickButton() {
        btnCancel.setOnAction(this::handleButtonAction);
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
                            break;
                        }
                        pnEditMode =  oTrans.getEditMode();
                        
                        System.err.println("update btn editmode ==" + pnEditMode);
                        initButton(pnEditMode);
                        initTabAnchor();
                    break;
                case "btnCancel":
                        if (ShowMessageFX.YesNo("Do you really want to cancel this record? \nAny data collected will not be kept.", "Computerized Acounting System", pxeModuleName)){
                            oTrans = new InvMaster(oApp, true);
                            oTrans.setRecordStatus("0123"); 
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
                    String lsValue = (txtSeeks01.getText().toString().isEmpty() ?"": txtSeeks01.getText().toString());
                       poJSON = oTrans.SearchInventory(lsValue, true);
                        if ("error".equals((String) poJSON.get("result"))){
                            ShowMessageFX.Information((String) poJSON.get("message"), "Computerized Acounting System", pxeModuleName);
                            txtSeeks01.clear();
                            break;
                        }
                        pnEditMode = oTrans.getEditMode();
                        
                        if(pnEditMode==EditMode.READY){
                            txtSeeks01.setText(oTrans.getModel().getBarCodex());
                            txtSeeks02.setText(oTrans.getModel().getDescript());
                        }else{
                            txtSeeks01.clear();
                            txtSeeks02.clear();
                        }
                        initButton(pnEditMode);
                        System.out.print("\neditmode on browse == " + pnEditMode);
                        initTabAnchor();
                        loadInventory();
                    break;
                case "btnLedger":
                        {
                            try {
                                if(pnEditMode == EditMode.READY ||
                                    pnEditMode == EditMode.ADDNEW ||
                                    pnEditMode == EditMode.UPDATE){
                                    loadLedger(oTrans.getInvModel().getStockID());
                                }
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

        overlay.setVisible(true);

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/org/guanzon/cas/views/InventorySerial.fxml"));

        InventorySerialController loControl = new InventorySerialController();
        loControl.setGRider(oApp);
        loControl.setFsCode(oTrans);

        fxmlLoader.setController(loControl);

        // Load the main interface
        Parent parent = fxmlLoader.load();
        parent.setStyle("-fx-background-color: rgba(0, 0, 0, 1);");

        // Set up dragging
        final double[] xOffset = new double[1];
        final double[] yOffset = new double[1];

        parent.setOnMousePressed(event -> {
            xOffset[0] = event.getSceneX();
            yOffset[0] = event.getSceneY();
        });

        parent.setOnMouseDragged(event -> {
            double newX = event.getScreenX() - xOffset[0];
            double newY = event.getScreenY() - yOffset[0];

            // Get the screen bounds
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

            // Calculate the window bounds
            double stageWidth = stage.getWidth();
            double stageHeight = stage.getHeight();

            // Constrain the stage position to the screen bounds
            if (newX < 0) newX = 0;
            if (newY < 0) newY = 0;
            if (newX + stageWidth > screenBounds.getWidth()) newX = screenBounds.getWidth() - stageWidth;
            if (newY + stageHeight > screenBounds.getHeight()) newY = screenBounds.getHeight() - stageHeight;

            stage.setX(newX);
            stage.setY(newY);
        });

        // Set the main interface as the scene
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Inventory Serial");

        // Add close request handler
        stage.setOnCloseRequest(event -> {
            System.out.println("Stage is closing");
            overlay.setVisible(false);
        });

        stage.setOnHidden(e -> overlay.setVisible(false));
        stage.showAndWait();
    } catch (IOException e) {
        e.printStackTrace();
        ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
        System.exit(1);
    }
}

    private void loadLedger(String fsCode) throws SQLException {
    try {
        Stage stage = new Stage();

        overlay.setVisible(true);

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/org/guanzon/cas/views/InventoryLedger.fxml"));

        InventoryLedgerController loControl = new InventoryLedgerController();
        loControl.setGRider(oApp);
        loControl.setFsCode(oTrans);
        fxmlLoader.setController(loControl);

        // Load the main interface
        Parent parent = fxmlLoader.load();

        // Set up dragging
        final double[] xOffset = new double[1];
        final double[] yOffset = new double[1];
        
        parent.setOnMousePressed(event -> {
            xOffset[0] = event.getSceneX();
            yOffset[0] = event.getSceneY();
        });

        parent.setOnMouseDragged(event -> {
            double newX = event.getScreenX() - xOffset[0];
            double newY = event.getScreenY() - yOffset[0];
            
            // Get the screen bounds
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            
            // Calculate the window bounds
            double stageWidth = stage.getWidth();
            double stageHeight = stage.getHeight();
            
            // Constrain the stage position to the screen bounds
            if (newX < 0) newX = 0;
            if (newY < 0) newY = 0;
            if (newX + stageWidth > screenBounds.getWidth()) newX = screenBounds.getWidth() - stageWidth;
            if (newY + stageHeight > screenBounds.getHeight()) newY = screenBounds.getHeight() - stageHeight;

            stage.setX(newX);
            stage.setY(newY);
        });

        Scene scene = new Scene(parent);
        
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.initModality(Modality.APPLICATION_MODAL);

        stage.setTitle("Inventory Ledger");
        
        // Add close request handler
        stage.setOnCloseRequest(event -> {
            System.out.println("Stage is closing");
            overlay.setVisible(false);
        });
        
        stage.setOnHidden(e -> overlay.setVisible(false));
        stage.showAndWait();

    } catch (IOException e) {
        e.printStackTrace();
        ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
        System.exit(1);
    }
}


    /*USE TO DISABLE ANCHOR BASE ON INITMODE*/    
    private void initTabAnchor(){
        System.out.print("EDIT MODE == " + pnEditMode);
        boolean pbValue = pnEditMode == EditMode.ADDNEW || 
                pnEditMode == EditMode.UPDATE;
        
        System.out.print("pbValue == " + pbValue);
        AnchorInput.setDisable(pbValue);
        gridFix.setDisable(pbValue);
        gridEditable.setDisable(!pbValue);
        
            if(pnEditMode == EditMode.READY ||pnEditMode == EditMode.UNKNOWN){
                AnchorInput.setDisable(!pbValue);
                gridFix.setDisable(!pbValue);
                gridEditable.setDisable(!pbValue);
            }
            System.out.println("EDIT MODE STAT == " + pnEditMode);
            if (pnEditMode == EditMode.UPDATE || pnEditMode == 2) { 
                  txtField22.setDisable(true);
             }
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == 0) { 
                  txtField22.setDisable(false);
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
            
            
             txtField14.setText(CommonUtils.NumberFormat(Double.parseDouble(oTrans.getInvModel().getDiscountLevel1().toString()), "#,##0.00"));
            txtField15.setText(CommonUtils.NumberFormat(Double.parseDouble(oTrans.getInvModel().getDiscountLevel2().toString()), "#,##0.00"));
            txtField16.setText(CommonUtils.NumberFormat(Double.parseDouble(oTrans.getInvModel().getDiscountLevel3().toString()), "#,##0.00"));
            txtField17.setText(CommonUtils.NumberFormat(Double.parseDouble(oTrans.getInvModel().getDealerDiscount().toString()), "#,##0.00"));
            
            txtField26.setText(String.valueOf(oTrans.getInvModel().getMinLevel()));
            txtField27.setText(String.valueOf(oTrans.getInvModel().getMaxLevel()));
            txtField29.setText(String.valueOf(oTrans.getInvModel().getMinLevel()));
            txtField30.setText(String.valueOf(oTrans.getInvModel().getMaxLevel()));
            
            txtField18.setText(CommonUtils.NumberFormat(Double.parseDouble(oTrans.getInvModel().getUnitPrice().toString()), "#,##0.00"));
            txtField19.setText(CommonUtils.NumberFormat(Double.parseDouble(oTrans.getInvModel().getSelPrice().toString()), "#,##0.00"));
            
            txtField20.setText((String) oTrans.getInvModel().getSupersed());
            txtField21.setText(String.valueOf(oTrans.getInvModel().getShlfLife()));
            
            cmbField01.setValue(String.valueOf(oTrans.getInvModel().getCategName2()));
            chkField01.setSelected("1".equals(oTrans.getInvModel().getSerialze()));
            chkField02.setSelected("1".equals(oTrans.getInvModel().getComboInv()));
            chkField03.setSelected("1".equals(oTrans.getInvModel().getWthPromo()));
            chkField04.setSelected((oTrans.getInvModel().isActive()));
            
            txtField22.setText((String)oTrans.getModel().getLocationnName());
            txtField23.setText((String)oTrans.getModel().getWareHouseNm());
            txtField24.setText((String)oTrans.getModel().getSectionName());
            txtField25.setText(String.valueOf(oTrans.getModel().getBinNumber()));
            
            txtField28.setText((String.valueOf(oTrans.getModel().getBegQtyxx())));
            txtField31.setText((String.valueOf(oTrans.getModel().getClassify())));
            txtField32.setText((String.valueOf(oTrans.getModel().getAvgMonSl())));
            txtField33.setText((String.valueOf(oTrans.getModel().getResvOrdr())));
            txtField34.setText((String.valueOf(oTrans.getModel().getQtyOnHnd())));

            if(pnEditMode == EditMode.ADDNEW) txtField22.setPromptText("PRESS F3: Search");
           
            lblStatus.setText(chkField04.isSelected() ? "ACTIVE" : "INACTIVE");
           DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            // Get the object from the model
            Object dbegInvxx = oTrans.getModel().getDBegInvxx();

            if (dbegInvxx == null) {
                // If the object is null, set the DatePicker to the current date
                dpField01.setValue(LocalDate.now());
            } else if (dbegInvxx instanceof Timestamp) {
                // If the object is a Timestamp, convert it to LocalDate
                Timestamp timestamp = (Timestamp) dbegInvxx;
                LocalDate localDate = timestamp.toLocalDateTime().toLocalDate();
                dpField01.setValue(localDate);
            } else if (dbegInvxx instanceof Date) {
                // If the object is a java.sql.Date, convert it to LocalDate
                Date sqlDate = (Date) dbegInvxx;
                LocalDate localDate = sqlDate.toLocalDate();
                dpField01.setValue(localDate);
            } else {
                // Handle unexpected types or throw an exception
                throw new IllegalArgumentException("Expected a Timestamp or Date, but got: " + dbegInvxx.getClass().getName());
            }            
            initSubItemForm();
     }
    }
    private void initSubItemForm(){
        if (!oTrans.getInvModel().getCategCd1().isEmpty()) { // Ensure the string is not empty
            switch (oTrans.getInvModel().getCategCd1()) {
                case "0001":
                case "0002":
                case "0003":
                    lblMeasure.setVisible(false);
                    lblShelf.setVisible(false);
                    txtField13.setVisible(false);
                    txtField21  .setVisible(false);
                    break;
                case "0004":
                    lblMeasure.setVisible(true);
                    lblShelf.setVisible(true);
                    txtField13.setVisible(true);
                    txtField21.setVisible(true);
                    break;
            }
        }
    }
    private void InitTextFields(){

        // Create an array for text fields with focusedProperty listeners
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

        // Create an array for text fields with setOnKeyPressed handlers
        TextField[] keyPressedTextFields = {
            txtField06, txtField07, txtField08, txtField09, txtField10,
            txtField11, txtField12, txtField22, txtField23
        };

        // Set the same key pressed event handler for each text field in the keyPressedTextFields array
        for (TextField textField : keyPressedTextFields) {
            textField.setOnKeyPressed(this::txtField_KeyPressed);
        }
        
        txtSeeks01.setOnKeyPressed(this::txtSeeks_KeyPressed);
        txtSeeks02.setOnKeyPressed(this::txtSeeks_KeyPressed);
        
        lblStatus.setText(chkField04.isSelected() ? "ACTIVE" : "INACTIVE");

    }
    /*Text seek/search*/
    private void txtSeeks_KeyPressed(KeyEvent event){
        TextField txtSeeks = (TextField)event.getSource();
        int lnIndex = Integer.parseInt(((TextField)event.getSource()).getId().substring(8,10));
        String lsValue = (txtSeeks.getText() == null ?"": txtSeeks.getText());
        JSONObject poJSON;
        switch (event.getCode()) {
            case F3:            
            case ENTER:
                switch (lnIndex){
                    
                    case 1: /*search Barrcode*/
                        System.out.print("LSVALUE OF SEARCH 1 ==== " + lsValue);
                        poJSON = oTrans.SearchInventory(lsValue, true);
                        if ("error".equals((String) poJSON.get("result"))){
                            ShowMessageFX.Information((String) poJSON.get("message"), "Computerized Acounting System", pxeModuleName);
                            txtSeeks01.clear();
                            break;
                        }
                        pnEditMode = oTrans.getEditMode();
                        
                        if(pnEditMode==EditMode.READY){
                            txtSeeks01.setText(oTrans.getModel().getBarCodex());
                            txtSeeks02.setText(oTrans.getModel().getDescript());
                        }else{
                            txtSeeks01.clear();
                            txtSeeks02.clear();
                        }
                        initButton(pnEditMode);
                        System.out.print("\neditmode on browse == " + pnEditMode);
                        initTabAnchor();
                        loadInventory();
                        
                        break;
                    case 2 :
                         poJSON = oTrans.SearchInventory(lsValue, false);
                        if ("error".equals((String) poJSON.get("result"))){
                            ShowMessageFX.Information((String) poJSON.get("message"), "Computerized Acounting System", pxeModuleName);
                            txtSeeks02.clear();
                            break;
                        }
                         pnEditMode = oTrans.getEditMode();
                        
                        if(pnEditMode==EditMode.READY){
                            txtSeeks01.setText(oTrans.getModel().getBarCodex());
                            txtSeeks02.setText(oTrans.getModel().getDescript());
                        }else{
                            txtSeeks01.clear();
                            txtSeeks02.clear();
                        }
                        initButton(pnEditMode);
                        System.out.print("\neditmode on browse == " + pnEditMode);
                        
                        initTabAnchor();
                        loadInventory();
                        break;
                }
                
        }
        switch (event.getCode()){
        case ENTER:
            CommonUtils.SetNextFocus(txtSeeks);
        case DOWN:
            CommonUtils.SetNextFocus(txtSeeks);
            break;
        case UP:
            CommonUtils.SetPreviousFocus(txtSeeks);
        }
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
                case 25: /*Stock ID*/
                    UnaryOperator<TextFormatter.Change> limitText = change -> {
                        String newText = change.getControlNewText();
                        if (newText.length() > 5) {
                            return null; // Disallow the change if it exceeds 5 characters
                        }
                        return change; // Allow the change
                    };

                    // Create a TextFormatter with the UnaryOperator
                    TextFormatter<String> textFormatter = new TextFormatter<>(limitText);

                    // Apply the TextFormatter to the TextField
                    txtField.setTextFormatter(textFormatter);
                    
                    oTrans.getModel().setBinNumber(Integer.parseInt(lsValue));
                    break;            
            }                  
        } else
            txtField.selectAll();
    };
    /*Txtfield search*/
    private void txtField_KeyPressed(KeyEvent event){
        TextField txtField = (TextField)event.getSource();
        int lnIndex = Integer.parseInt(((TextField)event.getSource()).getId().substring(8,10));
        String lsValue = (txtField.getText() == null ?"": txtField.getText());
        JSONObject poJson;
        switch (event.getCode()) {
            case F3:
                switch (lnIndex){
                    case 22:
                        poJson = new JSONObject();
                        poJson = oTrans.SearchMaster(4,lsValue, false);
                        System.out.println("poJson = " + poJson.toJSONString());
                        if("error".equalsIgnoreCase(poJson.get("result").toString())){
                            ShowMessageFX.Information((String) poJson.get("message"), "Computerized Acounting System", pxeModuleName);                              
                        }
                        System.out.print( "Location == " + oTrans.getMaster(27));
                        txtField22.setText((String) oTrans.getMaster(27)); 
                        txtField23.setText(oTrans.getModel().getWareHouseNm());
                        txtField24.setText(oTrans.getModel().getSectionName()); 
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
//    public void setOverlay(boolean fbVal){
//        overlay.setVisible(fbVal);
//    }
    public void loadResult(String fsValue, boolean fbVal){
        JSONObject poJson = new JSONObject();
        overlay.setVisible(fbVal);
        poJson = oTrans.openRecord(fsValue);
        if("error".equalsIgnoreCase(poJson.get("result").toString())){
            ShowMessageFX.Information((String) poJson.get("message"), "Computerized Acounting System", pxeModuleName);                              
        }
        loadInventory();
               
    }
}