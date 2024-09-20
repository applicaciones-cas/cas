/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.cas.controller;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.stream.IntStream;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
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
import org.guanzon.cas.inventory.models.Model_Inv_Stock_Request_Detail;
import org.guanzon.cas.inventory.stock.Inv_Request;
import org.guanzon.cas.inventory.stock.request.RequestControllerFactory;
import org.guanzon.cas.model.ModelStockRequest;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author User
 */
public class InvRequestEntryMCROQController implements Initializable ,ScreenInterface {
    
    private final String pxeModuleName = "Inventory Request MC ROQ";
    private GRider oApp;
    private int pnEditMode;  
    private Inv_Request oTrans;
    private boolean pbLoaded = false;
    private ObservableList<ModelStockRequest> R1data = FXCollections.observableArrayList();
    private ObservableList<ModelStockRequest> R2data = FXCollections.observableArrayList();
    private int pnRow = 0;
    private int pnRow1 = 0;
    
    @FXML
    private AnchorPane 
                AnchorMain,
                AnchorInput,
                AnchorTable;

    @FXML
    private TextField 
                txtSeeks01, 
                txtField01, 
                txtField02, 
                txtField03, 
                txtField04, 
                txtField05, 
                txtField06, 
                txtField07, 
                txtField08, 
                txtField09, 
                txtField10, 
                txtField11, 
                txtField12, 
                txtField13, 
                txtField14, 
                txtField15, 
                txtField16, 
                txtField17, 
                txtField18;

    @FXML
    private DatePicker 
                txtSeeks02, 
                dpField01;

    @FXML
    private HBox 
                hbButtons;

    @FXML
    private Button 
                btnBrowse, 
                btnNew, 
                btnSave, 
                btnUpdate, 
                btnSearch, 
                btnCancel,
                btnPrint ,
                btnClose;

    @FXML
    private Label 
                lblStatus;

    @FXML
    private TableView<ModelStockRequest> 
                tblRequest01,
                tblRequest02;

    @FXML
    private TableColumn
                R1index01, 
                R1index02, 
                R1index03, 
                R1index04, 
                R1index05, 
                R1index06, 
                R1index07, 
                R1index08, 
                R1index09, 
                R1index10,
                R2index01, 
                R2index02, 
                R2index03, 
                R2index04, 
                R2index05;

    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        initTrans();
        ClickButton();
        clearAllFields();
        InitTextFields();        
        initButton(pnEditMode);
        initTblDetails();
        initTabAnchor();
        lblStatus.setText("UNKNOWN");
        pbLoaded = true;
    }    
    
    private void ClickButton() {
        btnCancel.setOnAction(this::handleButtonAction);
        btnNew.setOnAction(this::handleButtonAction);
        btnSave.setOnAction(this::handleButtonAction);
        btnUpdate.setOnAction(this::handleButtonAction);
        btnClose.setOnAction(this::handleButtonAction);
        btnBrowse.setOnAction(this::handleButtonAction);  
        btnPrint.setOnAction(this::handleButtonAction);  
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
                    
                case"btnNew":
                     poJSON = oTrans.newTransaction();
                    if ("success".equals((String) poJSON.get("result"))){
                            pnEditMode = oTrans.getEditMode();
                            pnRow = 0;
                            pnRow1 = 0;
                            initButton(pnEditMode);
                            clearAllFields();
//                            initdatepicker();
                            initTabAnchor();
                            loadItemData();
                            loadDetails();
                            txtField01.setText((String) oTrans.getMasterModel().getTransactionNumber()); 
                            LocalDate currentDate = LocalDate.now();

                            // Optionally format the date (not necessary for setting value in DatePicker)
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                            String formattedDate = currentDate.format(formatter);
                            dpField01.setValue(currentDate);
                        }
                    break;  
                    
                case"btnSave":   
//                    if(oTrans.getDetailModelOthers().size()<=1){
//                      if(oTrans.getDetailModelOthers().get(0).getStockID().isEmpty() ||
//                            oTrans.getDetailModelOthers().get(0).getBarcode().isEmpty())
//                          System.out.println("detailmodel save == " +  oTrans.getDetailModelOthers().size());
//                        ShowMessageFX.Information("Empty item request detected, Please check your entry", "Computerized Acounting System", pxeModuleName);
//                        
//                        break;    
//                    }
                    poJSON = oTrans.saveTransaction();
                        System.out.println(poJSON.toJSONString());
                        if ("success".equals((String) poJSON.get("result"))){
                            System.err.println((String) poJSON.get("message"));
                            ShowMessageFX.Information((String) poJSON.get("message"), "Computerized Acounting System", pxeModuleName);
                            
                            if (ShowMessageFX.YesNo("Do you want to print this record?", "Computerized Acounting System", pxeModuleName)){                            
                                initTrans();
    //                            pnEditMode = EditMode.UNKNOWN;
    //                            initButton(pnEditMode);
                                clearAllFields();
                                initTabAnchor();
                                System.out.println("Record saved successfully.");
                            }else{
                                pnEditMode = EditMode.READY;
                                initButton(pnEditMode);
                            }
                            
                            
                        } else {
                            ShowMessageFX.Information((String)poJSON.get("message"), "Computerized Acounting System", pxeModuleName);
                            System.out.println("The record was not saved successfully.");
                            System.out.println((String) poJSON.get("message"));
                            
                            loadItemData();
                        }
                    break;
                case"btnUpdate":
                        poJSON = oTrans.updateTransaction();
                        if ("error".equals((String) poJSON.get("result"))){
                            ShowMessageFX.Information((String)poJSON.get("message"), "Computerized Acounting System", pxeModuleName);
                            break;
                        }
                        
//                        poJSON = oTrans.AddModelDetail();
//                        System.out.println(poJSON.toJSONString());
//                        if ("error".equals((String) poJSON.get("result"))){
//                            ShowMessageFX.Information((String) poJSON.get("message"), "Computerized Acounting System", pxeModuleName);
//                            break;
//                        }
                        pnEditMode =  oTrans.getEditMode();
                        pnRow = oTrans.getDetailModelOthers().size() - 1;
                        pnRow = oTrans.getDetailModel().size() - 1;
//                        
//                        System.out.println("pnRow sa update= " + pnRow);
                        loadItemData();
                        initButton(pnEditMode);
                        initTblDetails();
                        initTabAnchor();
                        txtField03.requestFocus();
                        
//                        tblDetails.getSelectionModel().select(pnRow + 1);
//                        initTabAnchor();
                    break;     
                
                case"btnBrowse":
                    clearAllFields();
                    String lsValue = (txtSeeks01.getText()==null)?"":txtSeeks01.getText();
                        poJSON = oTrans.searchTransaction("sTransNox", lsValue, pbLoaded);
                        if ("error".equals((String) poJSON.get("result"))){
                            ShowMessageFX.Information((String) poJSON.get("message"), "Computerized Acounting System", pxeModuleName);
                           
                            txtSeeks01.clear();
                            break;
                        }
                        txtSeeks01.setText(oTrans.getMasterModel().getTransactionNumber());
                        System.out.println(oTrans.getEditMode());
                        pnEditMode = oTrans.getEditMode();
                        R1data.clear();                           
                        loadTransaction();
                        initTblDetails();
                        loadItemData();
                        initTabAnchor();
                    break;     
                
                case"btnAddItem":
                   
                        poJSON = oTrans.AddModelDetail();
                        System.out.println(poJSON.toJSONString());
                        if ("error".equals((String) poJSON.get("result"))){
                            ShowMessageFX.Information((String) poJSON.get("message"), "Computerized Acounting System", pxeModuleName);
                            break;
                        }
                        
                            pnRow = oTrans.getDetailModelOthers().size()-1;
                            pnRow1 = oTrans.getDetailModel().size()-1;
                            clearItem();
                            pnEditMode = oTrans.getEditMode();
                            loadItemData();
//                            tblDetails.getSelectionModel().select(pnRow + 1);
                        
                    break;  
                    
                case"btnDelItem":
                    if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Do you want to remove this item?") == true){  
                        oTrans.RemoveModelDetail(pnRow);
                       
                        pnRow = oTrans.getDetailModelOthers().size()-1;
                        clearItem();
                        loadItemData();
                        txtField04.requestFocus();
                    }
                    break;
                case"btnCancel":
                     if (ShowMessageFX.YesNo("Do you really want to cancel this record? \nAny data collected will not be kept.", "Computerized Acounting System", pxeModuleName)){                            
//                            clearAllFields();
                            if(pnEditMode == EditMode.UPDATE){
                                oTrans.cancelUpdate();
                            }
                            initTrans();
                            initTabAnchor();
                            
                        }
                    break; 
                    
                 case"btnPrint":
                    if (ShowMessageFX.YesNo("Do you want to print this record?", "Computerized Acounting System", pxeModuleName)){                            
                        initTrans();
                        initTabAnchor();
                    }
                    break;  
                
                    
            }
        }
    }
    
    private void clearAllFields() {
        // Arrays of TextFields grouped by sections
        TextField[][] allFields = {
            // Text fields related to specific sections
            {txtSeeks01, txtField01, txtField02, txtField03, txtField04,
             txtField05,txtField06, txtField07, txtField08, txtField09,
             txtField10,txtField11,txtField12,txtField13,txtField14},

        };

        // Loop through each array of TextFields and clear them
        for (TextField[] fields : allFields) {
            for (TextField field : fields) {
                field.clear();
            }
        }
        R1data.clear();
        R2data.clear();
    }
    
    /*initialize fields*/
    private void InitTextFields(){
       // Define arrays for text fields with focusedProperty listeners
        TextField[] focusTextFields = {
                                        txtField01,
                                        txtField02,
                                        txtField03,
                                        txtField04,
                                        txtField05,
                                        txtField06, 
                                        txtField07,
                                        txtField08, 
                                        txtField09,
                                        txtField11
        };

        // Add the listener to each text field in the focusTextFields array
        for (TextField textField : focusTextFields) {
            textField.focusedProperty().addListener(txtField_Focus);
        }

        // Define arrays for text fields with setOnKeyPressed handlers
        TextField[] keyPressedTextFields = {
            txtField04, txtField05, txtField08, txtField09,
            txtField11
        };

//        // Set the same key pressed event handler for each text field in the keyPressedTextFields array
        for (TextField textField : keyPressedTextFields) {
            textField.setOnKeyPressed(this::txtField_KeyPressed);
        }

        txtSeeks01.setOnKeyPressed(this::txtSeeks_KeyPressed);
        txtSeeks02.setOnKeyPressed(this::txtSeeks_KeyPressed);
              
    }
    
    final ChangeListener<? super Boolean> txtField_Focus = (o,ov,nv)->{ 
        if (!pbLoaded) return;
       
        TextField txtField = (TextField)((ReadOnlyBooleanPropertyBase)o).getBean();
        int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
        String lsValue = (txtField.getText() == null ?"": txtField.getText());
        JSONObject jsonObject = new JSONObject();
        if (lsValue == null) return;         
        if(!nv){ /*Lost Focus*/
            switch (lnIndex){
                case 1: /*TRANSACTION NO*/
//                   oTrans.getModel().setStockID(lsValue);
                   System.out.print( "TRANSACTION NO == " );
                    break;
                case 2:/*REMARKS*/
//                   oTrans.getModel().setBarcode(lsValue);
                        if (lsValue.length() > 128) {
                            // Call the tooltip method
//                             showTooltip(txtField02, "Error: Input exceeds the maximum allowed.");
                        } else {      
                            oTrans.getMasterModel().setRemarks(lsValue);
                            System.out.println("REMARKS == " + lsValue);
                        }
                    break;
                case 3:/*Inventory Type*/   
//                   oTrans.getModel().setAltBarcode (lsValue);
                   System.out.print( "Inventory Type == " );
                    break;
                case 4:/*Category*/
//                   oTrans.getModel().setBriefDescription(lsValue);
                   System.out.print( "Category == " );
                    break;
                case 5:/*Barrcode*/
//                   oTrans.getModel().setDescription(lsValue);
                   System.out.print( "Barrcode == " );
                    break;
                case 6:/*Description*/
//                   oTrans.getModel().setDescription(lsValue);
                   System.out.print( "Description == ");
                    break;
                    
                case 7:/*Brand*/
//                   oTrans.getModel().setDescription(lsValue);
                   System.out.print( "Brand == ");
                    break;
                    
                case 8:/*Model*/
//                   oTrans.getModel().setDescription(lsValue);
                   System.out.print( "Model == ");
                    break;
                    
                case 9:/*Color*/
//                   oTrans.getModel().setDescription(lsValue);
                    int lnValue = (lsValue==null)?0:Integer.valueOf(lsValue);
                    if (lnValue == 0) {
                        // Remove the detail at pnRow1 if the value is 0
                        oTrans.RemoveModelDetail(pnRow1);
                    } else {
                        // Update the quantity for the detail
                        oTrans.getDetailModel(pnRow1).setQuantity(lnValue);

                        // Loop in reverse order to avoid index shifting when removing elements
                        for (int lnCtr = oTrans.getItemCount() - 1; lnCtr >= 0; lnCtr--) {
                            // Check if the quantity is 0 and remove the record if so
                            if (Integer.parseInt(oTrans.getDetailModel().get(lnCtr).getQuantity().toString()) == 0) {
                                oTrans.RemoveModelDetail(lnCtr);
                            }
                        }
                    }
                    
                    // Now check if there are any remaining items with quantity 0 before adding a new detail
                    boolean hasZeroQuantity = oTrans.getDetailModel().stream()
                        .anyMatch(item -> Integer.parseInt(item.getQuantity().toString()) == 0);

                    // Only add a new detail if no existing item has a quantity of 0
                    if (!hasZeroQuantity) {
                        oTrans.AddModelDetail();
                    } else {
                        System.out.println("Cannot add detail: Some items have a quantity of 0.");
                    }
                    oTrans.getDetailModelOthers().get(pnRow).setQuantity(Integer.valueOf(lsValue));
                    
                    pnRow1 = oTrans.getItemCount() - 1;
                    tblRequest02.getSelectionModel().select(pnRow1);
                   System.out.print( "Quantity == ");
                    break;    
                 
                case 10:/*Measure*/
//                   oTrans.getModel().setDescription(lsValue);
                   System.out.print( "Measure == ");
                    break; 
                    
                case 11:/*Min Level*/
                   System.out.println( "Min Level == " + lsValue + "\n");
                    break;  
                
                case 12:/*Max Level*/
                   System.out.println( "Max Level == " + lsValue + "\n");
                    break; 
                
                case 13:/*On Hand*/
                   System.out.println( "On Hand == " + lsValue + "\n");
                    break; 
                
                case 14:/*Qty Request*/
                   System.out.println( "QTY Request == " + lsValue + "\n");
                    break; 
            }  
            loadItemData();
        } else
            
            txtField.selectAll();
    };
    
     private void txtField_KeyPressed(KeyEvent event){
        TextField txtField = (TextField)event.getSource();
        int lnIndex = Integer.parseInt(((TextField)event.getSource()).getId().substring(8,10));
        String lsValue = (txtField.getText() == null ?"": txtField.getText());
        JSONObject poJson;
        switch (event.getCode()) {
            case ENTER:
            case F3:
                switch (lnIndex){
                    case 04: /*search barcode*/
//                        poJson = new JSONObject();
//                        poJson =  oTrans.searchDetail(pnRow, 3, lsValue, true);
//                        System.out.println("poJson = " + poJson.toJSONString());
//                           if("error".equalsIgnoreCase(poJson.get("result").toString())){
//                               ShowMessageFX.Information((String) poJson.get("message"), "Computerized Acounting System", pxeModuleName);  
//                               break;
//                           }
                        break;
                } 
//                loadDetails();
                txtField11.requestFocus();
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
     
     /*Text seek/search*/
    private void txtSeeks_KeyPressed(KeyEvent event){
        TextField txtSeeks = (TextField)event.getSource();
        int lnIndex = Integer.parseInt(((TextField)event.getSource()).getId().substring(8,10));
        String lsValue = (txtSeeks.getText() == null ?"": txtSeeks.getText());
        JSONObject poJSON;
        switch (event.getCode()) {
            case F3:
                switch (lnIndex){
                    
                    case 1: /*transaction no*/
                        System.out.print("search transaction == " + lsValue);

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
     
    /*USE TO DISABLE ANCHOR BASE ON INITMODE*/    
    private void initTabAnchor(){
        System.out.print("EDIT MODE == " + pnEditMode);
        boolean pbValue = pnEditMode == EditMode.ADDNEW || 
                pnEditMode == EditMode.UPDATE;
        
        System.out.print("pbValue == " + pbValue);
        AnchorInput.setDisable(!pbValue);
        AnchorTable.setDisable(!pbValue);
        if (pnEditMode == EditMode.READY){
            AnchorTable.setDisable(false);
        }
    }     
    private void initButton(int fnValue) {
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);

        // Manage visibility and managed state of buttons
        btnCancel.setVisible(lbShow);
        btnSearch.setVisible(lbShow);
        btnSave.setVisible(lbShow);

        btnCancel.setManaged(lbShow);
        btnSearch.setManaged(lbShow);
        btnSave.setManaged(lbShow);

        // Permanently hide btnUpdate
        btnUpdate.setVisible(false);
        btnUpdate.setManaged(false);
        btnBrowse.setVisible(false);
        btnBrowse.setManaged(false);

        // Manage visibility and managed state of other buttons
//        btnBrowse.setVisible(!lbShow);
        btnNew.setVisible(!lbShow);
//        btnBrowse.setManaged(!lbShow);
        btnNew.setManaged(!lbShow);
        btnClose.setVisible(!lbShow);

        // Manage text field states
        txtSeeks01.setDisable(!lbShow);
        txtSeeks02.setDisable(!lbShow);

        if (lbShow) {
            txtSeeks01.clear();
            // Uncomment this line if you want to clear txtSeeks02 as well when lbShow is true
            // txtSeeks02.clear();
        } else {
            txtSeeks01.requestFocus();
        }
    btnPrint.setDisable(fnValue == EditMode.UNKNOWN);

    }
    private void initTblDetails() {
        R1index01.setStyle("-fx-alignment: CENTER;");
        R1index02.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        R1index03.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        R1index04.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        R1index05.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        R1index06.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        R1index07.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        R1index08.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        R1index09.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        
        R1index01.setCellValueFactory(new PropertyValueFactory<>("index01"));
        R1index02.setCellValueFactory(new PropertyValueFactory<>("index02"));
        R1index03.setCellValueFactory(new PropertyValueFactory<>("index03"));
        R1index04.setCellValueFactory(new PropertyValueFactory<>("index04"));
        R1index05.setCellValueFactory(new PropertyValueFactory<>("index05"));
        R1index06.setCellValueFactory(new PropertyValueFactory<>("index06"));
        R1index07.setCellValueFactory(new PropertyValueFactory<>("index07"));
        R1index08.setCellValueFactory(new PropertyValueFactory<>("index08"));
        R1index09.setCellValueFactory(new PropertyValueFactory<>("index09"));
        
        tblRequest01.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblRequest01.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });
        tblRequest01.setItems(R1data);
        tblRequest01.autosize();
        
        R2index01.setStyle("-fx-alignment: CENTER;");
        R2index02.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        R2index03.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        R2index04.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        R2index05.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        
        R2index01.setCellValueFactory(new PropertyValueFactory<>("index01"));
        R2index02.setCellValueFactory(new PropertyValueFactory<>("index02"));
        R2index03.setCellValueFactory(new PropertyValueFactory<>("index03"));
        R2index04.setCellValueFactory(new PropertyValueFactory<>("index08"));
        R2index05.setCellValueFactory(new PropertyValueFactory<>("index09"));
        
        tblRequest02.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblRequest02.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });
        tblRequest02.setItems(R2data);
        tblRequest02.autosize();
    }
    
    private void loadTransaction(){
     if(pnEditMode == EditMode.READY || 
                pnEditMode == EditMode.ADDNEW || 
                pnEditMode == EditMode.UPDATE){

//            txtField01.setText((String) oTrans.getMasterModel().getTransactionNumber());
//            txtField02.setText((String) oTrans.getMasterModel().getRemarks());
//            dpField01.setValue((Date) oTrans.getMasterModel().getTransaction());
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            // Parse the formatted date string into a LocalDate object
            if(oTrans.getMasterModel().getTransaction() != null && !oTrans.getMasterModel().getTransaction().toString().trim().isEmpty()){
                LocalDate localbdate = LocalDate.parse(oTrans.getMasterModel().getTransaction().toString(), formatter);
                    
                // Set the value of the DatePicker to the parsed LocalDate
                dpField01.setValue(localbdate);
            }
            // Parse the formatted date string into a LocalDate object
            if(oTrans.getMasterModel().getTransaction() != null && !oTrans.getMasterModel().getTransaction().toString().trim().isEmpty()){
                LocalDate localbdate = LocalDate.parse(oTrans.getMasterModel().getTransaction().toString(), formatter);

                // Set the value of the DatePicker to the parsed LocalDate
                dpField01.setValue(localbdate);
            }
            
            switch (oTrans.getMasterModel().getTransactionStatus()) {
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
                case "5":
                    lblStatus.setText("VOID");
                    break;
                default:
                    lblStatus.setText("UNKNOWN");
                    break;
            }
        }
    }
    
    private void loadDetails(){
        if(!oTrans.getDetailModelOthers().isEmpty()){ 
            txtField05.setText((String) oTrans.getDetailModelOthers().get(pnRow).getDescription()); 
            txtField06.setText((String) oTrans.getDetailModelOthers().get(pnRow).getCategoryName()); 
            txtField06.setText((String) oTrans.getDetailModelOthers().get(pnRow).getBarcode()); 
            txtField07.setText((String) oTrans.getDetailModelOthers().get(pnRow).getModelName()); 
            txtField08.setText((String) oTrans.getDetailModelOthers().get(pnRow).getColorName()); 
            txtField09.setText(String.valueOf(oTrans.getDetailModelOthers().get(pnRow).getQuantity())); 
            txtField10.setText(oTrans.getDetailModelOthers().get(pnRow).getMinimumLevel().toString());
            txtField11.setText(String.valueOf(oTrans.getDetailModelOthers().get(pnRow).getMaximumLevel())); 
            txtField12.setText(oTrans.getDetailModelOthers().get(pnRow).getClassify()); 
            txtField13.setText(String.valueOf(oTrans.getDetailModelOthers().get(pnRow).getQuantityOnHand())); 
            txtField14.setText(String.valueOf(oTrans.getDetailModelOthers().get(pnRow).getOnTransit())); 
            txtField15.setText(String.valueOf(oTrans.getDetailModelOthers().get(pnRow).getAverageMonthlySalary())); 
            txtField16.setText(String.valueOf(oTrans.getDetailModelOthers().get(pnRow).getReservedOrder())); 
            txtField17.setText(String.valueOf(oTrans.getDetailModelOthers().get(pnRow).getBackOrder())); 
            txtField18.setText(String.valueOf(oTrans.getDetailModelOthers().get(pnRow).getRecordOrder())); 
        }
    }
    
   
    private void loadItemData(){
        int lnCtr;
        R1data.clear();
        R2data.clear();
        if(oTrans.getDetailModelOthers()!= null){
            for (lnCtr = 0; lnCtr < oTrans.getDetailModelOthers().size(); lnCtr++){
                
                R1data.add(new ModelStockRequest(String.valueOf(lnCtr + 1),
                        (String)oTrans.getDetailModelOthers().get(lnCtr).getModelName(),
                        (String)oTrans.getDetailModelOthers().get(lnCtr).getColorName(),
                        String.valueOf(oTrans.getDetailModelOthers().get(lnCtr).getAverageMonthlySalary()),
                        oTrans.getDetailModelOthers().get(lnCtr).getQuantityOnHand().toString(),
                        oTrans.getDetailModelOthers().get(lnCtr).getMinimumLevel().toString(),
                        oTrans.getDetailModelOthers().get(lnCtr).getReservedOrder().toString(),
                        oTrans.getDetailModelOthers().get(lnCtr).getRecordOrder().toString(),
                        oTrans.getDetailModelOthers().get(lnCtr).getQuantity().toString(),
                        (String)oTrans.getDetailModelOthers().get(lnCtr).getStockID()));  
                
            }   
        }
        if(oTrans.getDetailModel()!= null){
            for(lnCtr = 0; lnCtr < oTrans.getDetailModel().size(); lnCtr++){
                R2data.add(new ModelStockRequest(
                    String.valueOf(lnCtr+1),  // Keep the existing index
                    oTrans.getDetailModel().get(lnCtr).getModelName(),  // Update values from currentItem
                    oTrans.getDetailModel().get(lnCtr).getColorName(),
                    String.valueOf(oTrans.getDetailModel().get(lnCtr).getAverageMonthlySalary()),
                    oTrans.getDetailModel().get(lnCtr).getQuantityOnHand().toString(),
                    oTrans.getDetailModel().get(lnCtr).getMinimumLevel().toString(),
                    oTrans.getDetailModel().get(lnCtr).getReservedOrder().toString(),
                    oTrans.getDetailModel().get(lnCtr).getRecordOrder().toString(),
                    oTrans.getDetailModel().get(lnCtr).getQuantity().toString(),
                    oTrans.getDetailModel().get(lnCtr).getStockID()
                ));
           }
        }
        
        
    }
    
    @FXML
    private void tblSummary_Clicked (MouseEvent event) {
        if (tblRequest02.getSelectionModel().getSelectedIndex() >= 0) {
            pnRow1 = tblRequest02.getSelectionModel().getSelectedIndex();
            Model_Inv_Stock_Request_Detail clickedItem = oTrans.getDetailModel().get(pnRow1);
            int matchedIndex = IntStream.range(0, oTrans.getDetailModelOthers().size())
             .filter(i -> oTrans.getDetailModelOthers().get(i).getStockID().equals(clickedItem.getStockID()))
             .findFirst()
             .orElse(-1);  // Return -1 if no match is found

            if (matchedIndex != -1) {
                pnRow = matchedIndex;
                System.out.println("StockID found at index: " + matchedIndex);
            } 
            loadDetails();
            txtField09.requestFocus();
            if(!clickedItem.getStockID().isEmpty()){
                tblRequest01.getSelectionModel().select(pnRow);
            }
        }
    }
    
    @FXML
    private void tblDetails_Clicked (MouseEvent event) {
        System.out.println("pnRow = " + pnRow);
        
        // Check if a valid row is selected
        if (tblRequest01.getSelectionModel().getSelectedIndex() >= 0) {
            pnRow = tblRequest01.getSelectionModel().getSelectedIndex(); 
            loadDetails();  // Load details of the clicked item
            txtField09.requestFocus();
            // Retrieve the clicked item from getDetailModelOthers
            Model_Inv_Stock_Request_Detail clickedItem = oTrans.getDetailModelOthers().get(pnRow);
            

            // Variable to track if the clicked item has been processed (either replaced or added)
            int matchedIndex = IntStream.range(0, oTrans.getItemCount())
                .filter(i -> oTrans.getDetailModel(i).getStockID().equals(clickedItem.getStockID()))
                .findFirst()
                .orElse(-1);  // Return -1 if no match is found

            if (matchedIndex != -1) {
                pnRow1 = matchedIndex;
                System.out.println("StockID found at index: " + matchedIndex);
            } else {
                for (int lnCtr = 0; lnCtr < oTrans.getItemCount(); lnCtr++) {
                    Model_Inv_Stock_Request_Detail detailModelItem = oTrans.getDetailModel(lnCtr);

                    // Check if StockID is empty
                    if (Integer.parseInt(detailModelItem.getQuantity().toString())==0) {
                        // Replace empty stock ID with clickedItem
                        oTrans.getDetailModel().set(lnCtr, clickedItem);
                        pnRow1 = lnCtr; // Update pnRow1 with the current row index
                        tblRequest02.getSelectionModel().select(pnRow1);
                        System.out.println("Empty stock ID found. Replaced with clicked item.");
                        break;
                    }
                }
        
            loadItemData();
            tblRequest01.getSelectionModel().select(pnRow);
            tblRequest02.getSelectionModel().select(pnRow1);
            tblRequest01.setOnKeyReleased((KeyEvent t)-> {
                KeyCode key = t.getCode();
                switch (key){
                    case DOWN:
                        pnRow = tblRequest01.getSelectionModel().getSelectedIndex(); 
                        if (pnRow == tblRequest01.getItems().size()) {
                            pnRow = tblRequest01.getItems().size();
                            loadDetails();
                        }else {
                            loadDetails();
                        }
                        break;
                    case UP:
                        int pnRows = 0;
                        int x = 1;
                         pnRow = tblRequest01.getSelectionModel().getSelectedIndex(); 

                            loadDetails();
                        break;
                    default:
                        break; 
                    }
                });
            }
        }
    }
    private void clearItem(){
        TextField[][] allFields = {
            // Text fields related to specific sections
            { txtField05, txtField06, txtField07,
             txtField08,txtField09, txtField11 },
        };
        for (TextField[] fields : allFields) {
            for (TextField field : fields) {
                field.clear();
            }
        }
    }
    
    private void initTrans(){
        clearAllFields();
        oTrans = new Inv_Request(oApp, true);
        oTrans.setType(RequestControllerFactory.RequestType.MC);
        oTrans.setCategoryType(RequestControllerFactory.RequestCategoryType.WITH_ROQ);
        oTrans.setTransactionStatus("0123");
        pnEditMode = EditMode.UNKNOWN;     
        initButton(pnEditMode);
    }
}
