/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.cas.controller;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.cas.inventory.base.InvMaster;
import org.guanzon.cas.inventory.base.InvSerial;
import org.guanzon.cas.model.ModelInvSerial;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author User
 */
public class InventorySerialController implements Initializable {
   private final String pxeModuleName = "Inventory Serial";
    private GRider oApp;
    private int pnEditMode;  
    
    private int pnIndex = -1;
    private int pnRow = 0;
    
    private boolean pbLoaded = false;
    private boolean state = false;
    
    private String psCode;
    private String lsStockID;
    private InvSerial oTrans;
    
    public int tbl_row = 0;
    private ObservableList<ModelInvSerial> data = FXCollections.observableArrayList();
    @FXML
    private AnchorPane AnchorMain;
    @FXML
    private HBox hbButtons;
    @FXML
    private Button btnOkay;
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
    private TextField txtField06;
    @FXML
    private ComboBox cmbField01;
    @FXML
    private Button btnLoadSerial;
    @FXML
    private TableView tblSerialLedger;

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

    @FXML
    private TableColumn index06;

    @FXML
    private TableColumn index07;
    
     ObservableList<String> unitType = FXCollections.observableArrayList(
                "LDU",
                "Regular",
                "Free",
                "Live",
                "Service",
                "RDU",
                "Others",
                "All"
        );
    
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }
    private InvMaster poTrans;
    public void setFsCode(InvMaster fsCode) {
        this.poTrans = fsCode;
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
            oTrans = new InvSerial(oApp, true);
            ClickButton();
            oTrans.setRecordStatus("01234");
            pbLoaded = true;
            initTable();
            poTrans.getInvModel().setUnitType("7");
            txtField01.setText(poTrans.getModel().getBarCodex());
            txtField02.setText(poTrans.getModel().getDescript());
            txtField03.setText(poTrans.getInvModel().getBrandName());
            txtField04.setText(poTrans.getInvModel().getModelName());
            txtField05.setText(poTrans.getInvModel().getColorName());
            txtField06.setText(poTrans.getInvModel().getMeasureName());
            
            cmbField01.setItems(unitType);
            cmbField01.getSelectionModel().select(7);
            cmbField01.setOnAction(event -> {
                poTrans.getInvModel().setUnitType(String.valueOf(cmbField01.getSelectionModel().getSelectedIndex()));
            
        });
    } 
    
    /*Handle button click*/
    private void ClickButton() {
        btnOkay.setOnAction(this::handleButtonAction);
        btnLoadSerial.setOnAction(this::handleButtonAction);
        btnClose.setOnAction(this::handleButtonAction);
    }
    private void handleButtonAction(ActionEvent event) {
        Object source = event.getSource();
        JSONObject poJSON;
        if (source instanceof Button) {
            Button clickedButton = (Button) source;
            unloadForm appUnload = new unloadForm();
            switch (clickedButton.getId()) {
                case"btnClose":
                    CommonUtils.closeStage(btnClose);
                    break;
                case "btnLoadSerial":
                    String UnitType = String.valueOf(cmbField01.getSelectionModel().getSelectedIndex()); 
                    poJSON = new JSONObject();
                    
                    if (UnitType.equals("7")) {
                       poJSON = oTrans.OpenInvSerial(poTrans.getModel().getStockID());
                        System.out.print("\nunitype == " + UnitType);
                        System.out.println("poJson = " + poJSON.toJSONString());
                        loadSerial();
                    } else {
                        poJSON = oTrans.OpenInvSerialWithCondition(poTrans.getModel().getStockID(), " a.cUnitType = '" + UnitType + "'");
                        System.out.print("\nunitype == " + UnitType);
                        System.out.println("poJson = " + poJSON.toJSONString());
                        loadSerial();
                    }
                    break;
            }
        }
    }
    
    private void loadSerial(){
        int lnCtr;
        data.clear();
        if(oTrans.getMaster()!= null){
            for (lnCtr = 0; lnCtr < oTrans.getMaster().size(); lnCtr++){
                data.add(new ModelInvSerial(String.valueOf(lnCtr + 1),
                    (String) oTrans.getMaster(lnCtr, "sSerialID"), 
                    (String)oTrans.getMaster(lnCtr, "sSerial01"),
                    (String)oTrans.getMaster(lnCtr, "sSerial02"), 
                    (String)oTrans.getMaster(lnCtr, "xBranchNm"), 
                    (String)oTrans.getMaster(lnCtr, "cSoldStat"), 
                    (String)oTrans.getMaster(lnCtr, "cUnitType"),
              ""));  
                    System.out.println("\nNo == " + String.valueOf(lnCtr + 1));
                    System.out.println("\nsSerialID == " + (String) oTrans.getMaster(lnCtr, "sSerialID"));
                    System.out.println("\nsSerial01 == " + (String) oTrans.getMaster(lnCtr, "sSerial01"));
                    System.out.println("\nsSerial02 == " + (String) oTrans.getMaster(lnCtr, "sSerial02"));
                    System.out.println("\ncLocation == " + (String) oTrans.getMaster(lnCtr, "xBranchNm"));
                    System.out.println("\ncSoldStat == " + (String) oTrans.getMaster(lnCtr, "cSoldStat"));
                    System.out.println("\ncUnitType == " + (String) oTrans.getMaster(lnCtr, "cUnitType"));
            
            }
        }
    }
      private void initTable() {
        index01.setStyle("-fx-alignment: CENTER;");
        index02.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        index03.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        index04.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        index05.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        index06.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        index07.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");

        index01.setCellValueFactory(new PropertyValueFactory<>("index01"));
        index02.setCellValueFactory(new PropertyValueFactory<>("index02"));
        index03.setCellValueFactory(new PropertyValueFactory<>("index03"));
        index04.setCellValueFactory(new PropertyValueFactory<>("index04"));
        index05.setCellValueFactory(new PropertyValueFactory<>("index05"));
        index06.setCellValueFactory(new PropertyValueFactory<>("index06"));
        index07.setCellValueFactory(new PropertyValueFactory<>("index07"));
        tblSerialLedger.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblSerialLedger.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });
        tblSerialLedger.setItems(data);
        tblSerialLedger.autosize();
    }
}
