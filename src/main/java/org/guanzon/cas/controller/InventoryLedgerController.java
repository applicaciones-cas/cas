/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.cas.controller;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.agent.TableModel;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.base.SQLUtil;
import org.guanzon.cas.inventory.base.InvLedger;
import org.guanzon.cas.inventory.base.InvMaster;
import org.guanzon.cas.model.ModelAPClientLedger;
import org.guanzon.cas.model.ModelInvLedger;
import org.json.simple.JSONObject;
/**
 * FXML Controller class
 *
 * @author User
 */

public class InventoryLedgerController implements Initializable, ScreenInterface {  
    private final String pxeModuleName = "Inventory Ledger";
    private GRider oApp;
    private int pnEditMode;  
    
    private int pnIndex = -1;
    private int pnRow = 0;
    
    private boolean pbLoaded = false;
    private boolean state = false;
    
    private String psCode;
    private String lsStockID;
    private InvLedger oTrans;
    private InventoryDetailController parentController;
    
    public int tbl_row = 0;
    
    private ObservableList<ModelInvLedger> data = FXCollections.observableArrayList();
    public void setParentController(InventoryDetailController cVal){
        parentController =cVal;
    }
    
    public static TableModel empModel;
    @FXML
    private AnchorPane AnchorMain;
    @FXML
    private HBox hbButtons;
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnClose;
    @FXML
    private Button btnRecalculate;
    @FXML
    private DatePicker dpField02;
    @FXML
    private DatePicker dpField01;
    @FXML
    private Button btnLoadLedger;
    @FXML
    private TextField txtField01;
    @FXML
    private TextField txtField02;
    @FXML
    private TextField txtField04;
    @FXML
    private TextField txtField03;
    @FXML
    private TextField txtField05;
    @FXML
    private TextField txtField06;
    @FXML
    private TableView tblInventoryLedger;

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

    @FXML
    private TableColumn index08;

    public void setGRider(GRider foValue) {
        oApp = foValue;
    }
    public void setStockID(String foValue) {
        lsStockID = foValue;
    }
    private String fsCode;
    
    private InvMaster poTrans;
    public void setFsCode(InvMaster fsCode) {
        this.poTrans = fsCode;
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
            oTrans = new InvLedger(oApp, true);
            oTrans.setRecordStatus("01234");
            btnCancel.setOnAction(this::cmdButton_Click);
            btnClose.setOnAction(this::cmdButton_Click);
            btnLoadLedger.setOnAction(this::cmdButton_Click);
            btnRecalculate.setOnAction(this::cmdButton_Click);
            pbLoaded = true;
            initTable();
            txtField01.setText(poTrans.getModel().getBarCodex());
            txtField02.setText(poTrans.getModel().getDescript());
            txtField03.setText(poTrans.getInvModel().getBrandName());
            txtField04.setText(poTrans.getInvModel().getModelName());
            txtField05.setText(poTrans.getInvModel().getColorName());
            txtField06.setText(poTrans.getInvModel().getMeasureName());
    }    
    
     public void cmdButton_Click(ActionEvent event) {
        String lsButton = ((Button)event.getSource()).getId();
         
        JSONObject poJson;    
        unloadForm appUnload = new unloadForm();
        switch (lsButton){
           case "btnClose":  //Close
                if(parentController != null){
                    appUnload.useParentController(poTrans.getModel().getStockID());
                }
                CommonUtils.closeStage(btnClose);
            break;
            case "btnRecalculate":  //Rcalculate
                if (data.isEmpty()){
                    ShowMessageFX.Information("Please ensure the ledger is loaded before performing recalculation."
                            + "Recalculation cannot be completed correctly without loading the ledger first.", 
                            "Computerized Acounting System", pxeModuleName);     
                    break;
                }else{
                    try {
                        
                        LocalDate fromDate = dpField01.getValue();
                        LocalDate thruDate = dpField02.getValue();
                        poTrans.recalculate(poTrans.getModel().getStockID());
                        ShowMessageFX.Information("Recalculation completed succesfully", 
                            "Computerized Acounting System", pxeModuleName); 
                        poJson = new JSONObject();
                        poJson = oTrans.OpenInvLedger(poTrans.getModel().getStockID());
                        
                        System.out.println("poJson = " + poJson.toJSONString());
                        if("error".equalsIgnoreCase(poJson.get("result").toString())){
                            ShowMessageFX.Information((String) poJson.get("message"), "Computerized Acounting System", pxeModuleName);                              
                        }  
                        loadLedger();
                        
                        
//                        LocalDate fromDate = dpField01.getValue();
//                        LocalDate thruDate = dpField02.getValue();
//                        poTrans.recalculate(poTrans.getModel().getStockID());
//                        ShowMessageFX.Information("Recalculation completed succesfully", 
//                            "Computerized Acounting System", pxeModuleName); 
//                        poJson = new JSONObject();
//                        poJson = oTrans.OpenInvLedgerWithCondition(poTrans.getModel().getStockID(), " a.dTransact BETWEEN '" + fromDate + "' AND '" + thruDate +"'");
//                        System.out.println("poJson = " + poJson.toJSONString());
//                        if("error".equalsIgnoreCase(poJson.get("result").toString())){
//                            ShowMessageFX.Information((String) poJson.get("message"), "Computerized Acounting System", pxeModuleName);                              
//                        }  
//                        loadLedger();
                    } catch (SQLException ex) {
                        Logger.getLogger(InventoryLedgerController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            
            break;

           case "btnLoadLedger":  //Close
//                CommonUtils.closeStage(btnClose);
                LocalDate fromDate = dpField01.getValue();
                LocalDate thruDate = dpField02.getValue();

                if (fromDate != null && thruDate != null) {
                    if (fromDate.isAfter(thruDate)) {
                        ShowMessageFX.Information("Invalid Date Range: 'From Date' should not be after 'Thru Date'", "Computerized Acounting System", pxeModuleName);     
                        System.out.println("Invalid Date Range: 'From Date' should not be after 'Thru Date'");
                    } else {
                        poJson = new JSONObject();
                        poJson = oTrans.OpenInvLedgerWithCondition(poTrans.getModel().getStockID(), " a.dTransact BETWEEN '" + fromDate + "' AND '" + thruDate +"'");
                        System.out.println("poJson = " + poJson.toJSONString());
                        if("error".equalsIgnoreCase(poJson.get("result").toString())){
                            ShowMessageFX.Information((String) poJson.get("message"), "Computerized Acounting System", pxeModuleName);                              
                        }  
                        loadLedger();
                        System.out.println("From Date: " + fromDate);
                        System.out.println("Thru Date: " + thruDate);
                    }
                } else {
                    System.out.println("Please select both dates.");
                    ShowMessageFX.Information("Please select both dates.", "Computerized Acounting System", pxeModuleName);    
                }
            break;
            
           case "btnCancel": //OK;
                if(parentController != null){
                    appUnload.useParentController("");
                }
                CommonUtils.closeStage(btnCancel);
            break;
            
               default: 
                   ShowMessageFX.Warning(null, pxeModuleName, "Button with name " + lsButton + " not registered.");
                return;        
        }     
    } 
     
     private void loadLedger(){
        int lnCtr;
        data.clear();
        if(oTrans.getMaster()!= null){
            for (lnCtr = 0; lnCtr < oTrans.getMaster().size(); lnCtr++){
                data.add(new ModelInvLedger(String.valueOf(lnCtr + 1),
                     oTrans.getMaster(lnCtr, "dTransact").toString(), 
                    (String)oTrans.getMaster(lnCtr, "xBranchNm"),
                    (String)oTrans.getMaster(lnCtr, "sSourceCd"), 
                    (String)oTrans.getMaster(lnCtr, "sSourceNo"), 
                    oTrans.getMaster(lnCtr, "nQtyInxxx").toString(),
                    (String)oTrans.getMaster(lnCtr, "nQtyOutxx").toString(), 
                    (String)oTrans.getMaster(lnCtr, "nQtyOnHnd").toString()));  
                
//                System.out.println();
//                System.out.println("DATE        : " + oTrans.getMaster(lnCtr, "dTransact").toString());
//                System.out.println("WAREHOUSE   : " + oTrans.getMaster(lnCtr, "xWHouseNm").toString());
//                System.out.println("SOURCE CODE : " + oTrans.getMaster(lnCtr, "sSourceCd").toString());
//                System.out.println("SOURCE NO   : " + oTrans.getMaster(lnCtr, "sSourceNo").toString());
//                System.out.println("QTY IN      : " + oTrans.getMaster(lnCtr, "nQtyInxxx").toString());
//                System.out.println("QTY OUT     : " + oTrans.getMaster(lnCtr, "nQtyOutxx").toString());
//                System.out.println("QTY ON HAND : " + oTrans.getMaster(lnCtr, "nQtyOnHnd").toString());
//                System.out.println();
//                System.out.println("-------------------------------------------------------------------------");

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
        index08.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        
        index01.setCellValueFactory(new PropertyValueFactory<>("index01"));
        index02.setCellValueFactory(new PropertyValueFactory<>("index02"));
        index03.setCellValueFactory(new PropertyValueFactory<>("index03"));
        index04.setCellValueFactory(new PropertyValueFactory<>("index04"));
        index05.setCellValueFactory(new PropertyValueFactory<>("index05"));
        index06.setCellValueFactory(new PropertyValueFactory<>("index06"));
        index07.setCellValueFactory(new PropertyValueFactory<>("index07"));
        index08.setCellValueFactory(new PropertyValueFactory<>("index08"));
        tblInventoryLedger.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblInventoryLedger.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });
        tblInventoryLedger.setItems(data);
        tblInventoryLedger.autosize();
    }
     
}
