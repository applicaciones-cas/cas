/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.cas.controller;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import java.net.URL;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import static javafx.scene.input.KeyCode.DOWN;
import static javafx.scene.input.KeyCode.ENTER;
import static javafx.scene.input.KeyCode.F3;
import static javafx.scene.input.KeyCode.UP;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.cas.inventory.base.InvSerial;
import org.guanzon.cas.inventory.base.InvSerialLedger;
import org.guanzon.cas.model.ModelInvSerialLedger;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author User
 */
public class InventorySerialParamController implements Initializable,ScreenInterface {
    private final String pxeModuleName = "Inventory Serial Parameter";
    private GRider oApp;
    private String oTransnox = "";
    private int pnEditMode;  
    private InvSerial oTrans;    
    private InvSerialLedger poTrans;
    
    private boolean state = false;
    private boolean pbLoaded = false;
    private int pnInventory = 0; 
    private int pnRow = 0;
    
    private ObservableList<ModelInvSerialLedger> data = FXCollections.observableArrayList();
    @FXML
    private TextField txtSeeks02;

    @FXML
    private TextField txtSeeks01;

    @FXML
    private AnchorPane AnchorInput,AnchorMain;

    @FXML
    private TextField txtField01;

    @FXML
    private TextField txtField02;

    @FXML
    private TextField txtField03;

    @FXML
    private ComboBox cmbField01;

    @FXML
    private TextField txtField04;

    @FXML
    private TextField txtField05;

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
    private Label lblStatus;

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
    private TableView tblInventorySerialLedger;

    @FXML
    private TableColumn index01;

    @FXML
    private TableColumn index02;

    @FXML
    private TableColumn index03;

    @FXML
    private TableColumn index04;

    @FXML
    private TableColumn index05;


    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
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
    public void initialize(URL url, ResourceBundle rb) {
        oTrans = new InvSerial(oApp, true);
        poTrans = new InvSerialLedger(oApp, true);
        
        if (oTransnox == null || oTransnox.isEmpty()) { // Check if oTransnox is null or empty
            pnEditMode = EditMode.UNKNOWN;
        }
        oTrans.setRecordStatus("0123");
        pnEditMode = EditMode.UNKNOWN;    
        ClickButton();        
        initTable();
        cmbField01.setItems(unitType);
        txtSeeks01.setOnKeyPressed(this::txtSeeks_KeyPressed);
        txtSeeks02.setOnKeyPressed(this::txtSeeks_KeyPressed);
        pbLoaded = true;
    }    
    
    private void ClickButton() {
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
                
                case "btnBrowse": 
                    String lsValue = (txtSeeks01.getText().toString().isEmpty() ?"": txtSeeks01.getText().toString());
                       poJSON = oTrans.searchRecord(lsValue, true);
                        if ("error".equals((String) poJSON.get("result"))){
                            ShowMessageFX.Information((String) poJSON.get("message"), "Computerized Acounting System", pxeModuleName);
                            txtSeeks01.clear();
                            break;
                        }
                        lsValue = (String) oTrans.getMaster(pnRow, "sSerialID");
                        poJSON = poTrans.OpenInvSerialLedger(lsValue);
                        System.out.println("poJson = " + poJSON.toJSONString());
                        if("error".equalsIgnoreCase(poJSON.get("result").toString())){
                            ShowMessageFX.Information((String) poJSON.get("message"), "Computerized Acounting System", pxeModuleName);                              
                        }  
                        pnEditMode = oTrans.getEditMode();
                        clearAllFields();
                        loadSerial();
                        loadSerialLedger();
                    break;
            }
        }
    
    }


    private void loadSerial() {
    if (pnEditMode == EditMode.READY || 
        pnEditMode == EditMode.ADDNEW || 
        pnEditMode == EditMode.UPDATE) {

        // Set text fields from oTrans
        txtField01.setText((String) oTrans.getMaster(pnRow, "sSerialID"));
        txtField02.setText((String) oTrans.getMaster(pnRow, "sSerial01"));
        txtField03.setText((String) oTrans.getMaster(pnRow, "sSerial02")); 
        txtField04.setText((String) oTrans.getMaster(pnRow, "sStockIDx"));
        txtField05.setText((String) oTrans.getMaster(pnRow, "xCompanyNm"));
        txtField06.setText((String) oTrans.getMaster(pnRow, "sWarranty"));
       
        String soldStat = (String) oTrans.getMaster(pnRow, "cSoldStat");
        txtField07.setText("1".equals(soldStat) ? "Yes" : "No");
        
        txtField08.setText((String) oTrans.getMaster(pnRow, "xBranchNm"));
        // Define an array of location descriptions
        String[] locationDescriptions = {
            "Warehouse",       // Index 0
            "Branch",          // Index 1
            "Supplier",        // Index 2
            "Customer",        // Index 3
            "On Transit",      // Index 4
            "Service Center",  // Index 5
            "Service Unit"     // Index 6
        };

        // Retrieve the location code and handle it
        String locationCode = (String) oTrans.getMaster(pnRow, "cLocation");
        int index;
        try {
            index = Integer.parseInt(locationCode);
        } catch (NumberFormatException e) {
            throw new AssertionError("Invalid location code format: " + locationCode);
        }

        // Set the description or handle an invalid code
        if (index >= 0 && index < locationDescriptions.length) {
            txtField09.setText(locationDescriptions[index]);
        } else {
            throw new AssertionError("Unexpected location code: " + locationCode);
        }
        txtField10.setText(CommonUtils.NumberFormat(Double.parseDouble(oTrans.getMaster(pnRow,"nUnitPrce").toString()), "#,##0.00"));
//                oTrans.getModel().getUnitPrice().toString()), "#,##0.00"));
//        txtField10.setText(CommonUtils.NumberFormat(Double.parseDouble(, "#,##0.00")));
        
        cmbField01.getSelectionModel().select(Integer.parseInt(oTrans.getMaster(pnRow, "cUnitType").toString()));
    }
}

    private void clearAllFields() {
        // Arrays of TextFields grouped by sections
        TextField[][] allFields = {
            // Text fields related to specific sections
            {txtSeeks01, txtSeeks02, txtField01, txtField02, txtField03, txtField04,
             txtField05, txtField06, txtField07, txtField08, txtField09,txtField10},
        };

        cmbField01.setValue(null);
        cmbField01.setValue(null);
        
        // Loop through each array of TextFields and clear them
        for (TextField[] fields : allFields) {
            for (TextField field : fields) {
                field.clear();
            }
        }
    }
    
    private void txtSeeks_KeyPressed(KeyEvent event){
        TextField txtSeeks = (TextField)event.getSource();
        int lnIndex = Integer.parseInt(((TextField)event.getSource()).getId().substring(8,10));
        String lsValue = (txtSeeks.getText() == null ?"": txtSeeks.getText());
        JSONObject poJSON;
        switch (event.getCode()) {
            case F3:
                switch (lnIndex){
                    
                    case 1: /*search Barrcode*/
                        
                    
                       poJSON = oTrans.searchRecord(lsValue, true);
                        if ("error".equals((String) poJSON.get("result"))){
                            ShowMessageFX.Information((String) poJSON.get("message"), "Computerized Acounting System", pxeModuleName);
                            txtSeeks01.clear();
                            break;
                        }
                        lsValue = (String) oTrans.getMaster(pnRow, "sSerialID");
                        poJSON = poTrans.OpenInvSerialLedger(lsValue);
                        System.out.println("poJson = " + poJSON.toJSONString());
                        if("error".equalsIgnoreCase(poJSON.get("result").toString())){
                            ShowMessageFX.Information((String) poJSON.get("message"), "Computerized Acounting System", pxeModuleName);    
                        }  
                        pnEditMode = oTrans.getEditMode();
                        clearAllFields();
                        loadSerial();
                        loadSerialLedger();
                        
//                     
                        break;
                    case 2 :
                       poJSON = oTrans.searchRecord(lsValue, false);
                        if ("error".equals((String) poJSON.get("result"))){
                            ShowMessageFX.Information((String) poJSON.get("message"), "Computerized Acounting System", pxeModuleName);
                            txtSeeks01.clear();
                            break;
                        }
                        
                        pnEditMode = oTrans.getEditMode();
//                        initButton(pnEditMode);
                        loadSerial();
                        
                        loadSerialLedger();
                        break;
                }
            case ENTER:
                
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
    private void loadSerialLedger(){
        int lnCtr;
        data.clear();
        if(poTrans.getMaster()!= null){
            for (lnCtr = 0; lnCtr < poTrans.getMaster().size(); lnCtr++){
                data.add(new ModelInvSerialLedger(String.valueOf(lnCtr + 1),
                    poTrans.getMaster(lnCtr, 4).toString(), 
                    (String)poTrans.getModel(lnCtr).getBranchName(),
                    (String)poTrans.getMaster(lnCtr, 5), 
                    (String)poTrans.getMaster(lnCtr, 6), 
                "",
                "",
                ""));  

                System.out.println(String.valueOf(lnCtr + 1) + "-------------------------------------------------------");
                System.out.println( poTrans.getMaster(lnCtr, 1).toString());
                System.out.println( poTrans.getMaster(lnCtr, 2).toString());
                System.out.println( poTrans.getMaster(lnCtr, 3).toString());
                System.out.println( poTrans.getMaster(lnCtr, 4).toString());
                System.out.println( poTrans.getMaster(lnCtr, 5).toString());
                System.out.println( poTrans.getMaster(lnCtr, 6).toString());
                System.out.println( poTrans.getMaster(lnCtr, 7).toString());
                System.out.println( poTrans.getMaster(lnCtr, 8).toString());
                System.out.println( poTrans.getMaster(lnCtr, 9).toString());
                System.out.println( poTrans.getMaster(lnCtr, 10).toString());
                System.out.println( poTrans.getMaster(lnCtr, 11).toString());
                System.out.println( poTrans.getMaster(lnCtr, 12).toString());
                System.out.println( poTrans.getMaster(lnCtr, 13).toString());
                System.out.println("--------------------------------------------------------");
                
                
            }
            
        }
    }
    private void initTable() {
        index01.setStyle("-fx-alignment: CENTER;");
        index02.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        index03.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        index04.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        index05.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");

        index01.setCellValueFactory(new PropertyValueFactory<>("index01"));
        index02.setCellValueFactory(new PropertyValueFactory<>("index02"));
        index03.setCellValueFactory(new PropertyValueFactory<>("index03"));
        index04.setCellValueFactory(new PropertyValueFactory<>("index04"));
        index05.setCellValueFactory(new PropertyValueFactory<>("index05"));
        tblInventorySerialLedger.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblInventorySerialLedger.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });
        tblInventorySerialLedger.setItems(data);
        tblInventorySerialLedger.autosize();
    }
    
}
