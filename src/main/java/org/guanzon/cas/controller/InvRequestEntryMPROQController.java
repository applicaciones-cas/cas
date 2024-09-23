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
public class InvRequestEntryMPROQController implements Initializable ,ScreenInterface {
    
    private final String pxeModuleName = "Inventory Request MP ROQ";
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
                AnchorTable,
                AnchorTable01;

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
                txtField10;

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
                btnClose,
                btnPrint;

    @FXML
    private Label 
                lblStatus;

    @FXML
    private TableView<ModelStockRequest> 
                tblRequest,
                tblSummary;

    @FXML
    private TableColumn
                R1index01, 
                R1index02, 
                R1index03, 
                R1index04, 
                R1index05a, 
                R1index05b, 
                R1index06a, 
                R1index06b, 
                R1index07a, 
                R1index07b,
                sIndex01, 
                sIndex02, 
                sIndex03, 
                sIndex04, 
                sIndex05,
                sIndex06,
                sIndex07;

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

                            initTrans();
//                            pnEditMode = EditMode.UNKNOWN;
//                            initButton(pnEditMode);
                            clearAllFields();
                            initTabAnchor();

                            System.out.println("Record saved successfully.");
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
                  
                
                    
            }
        }
    }
    
    private void clearAllFields() {
        // Arrays of TextFields grouped by sections
        TextField[][] allFields = {
            // Text fields related to specific sections
            {txtSeeks01, txtField01, txtField02, txtField03, txtField04,
             txtField05,txtField06, txtField07, txtField08, txtField09,
             txtField10},

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
                                        txtField10
        };

        // Add the listener to each text field in the focusTextFields array
        for (TextField textField : focusTextFields) {
            textField.focusedProperty().addListener(txtField_Focus);
        }

        // Define arrays for text fields with setOnKeyPressed handlers
        TextField[] keyPressedTextFields = {
            txtField04, txtField05, txtField08, txtField09,txtField10
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
                case 2:/*Brand*/
                    System.out.println("Brand == " + lsValue);
                    break;
                case 3:/*Barrcode*/   
//                   oTrans.getModel().setAltBarcode (lsValue);
                   System.out.print( "Barrcode == " );
                    break;
                case 4:/*Description*/
//                   oTrans.getModel().setBriefDescription(lsValue);
                    if (lsValue.length() > 128) {
                            // Call the tooltip method
//                             showTooltip(txtField02, "Error: Input exceeds the maximum allowed.");
                        } else {      
                            oTrans.getMasterModel().setRemarks(lsValue);
                            System.out.println("REMARKS == " + lsValue);
                        }
                   System.out.print( "Description == " );
                    break;
                case 5:/*On Transit*/
//                   oTrans.getModel().setDescription(lsValue);
                   System.out.print( "On Transit == " );
                    break;
                case 6:/*AMC*/
//                   oTrans.getModel().setDescription(lsValue);
                   System.out.print( "AMC == ");
                    break;
                    
                case 7:/*QOH*/
//                   oTrans.getModel().setDescription(lsValue);
                   System.out.print( "QOH == ");
                    break;
                    
                case 8:/*ROQ*/
//                   oTrans.getModel().setDescription(lsValue);
                   System.out.print( "ROQ == ");
                    break;
                    
                case 9:/*Classification*/
//                   oTrans.getModel().setDescription(lsValue);
                    System.out.print( "Classification == ");
                    break;    
                 
                case 10:/*QTY Request*/
                    int lnValue = (lsValue.isEmpty())?0:Integer.valueOf(lsValue);
                    if (lnValue == 0) {
                        // Remove the detail at pnRow1 if the value is 0
                        oTrans.RemoveModelDetail(pnRow1);
                    } else {
                        // Update the quantity for the detail
                        oTrans.getDetailModel(pnRow1).setQuantity(lnValue);
                        oTrans.getDetailModelOthers().get(pnRow).setQuantity(Integer.valueOf(lsValue));

                        // Loop in reverse order to avoid index shifting when removing elements
                        for (int lnCtr = oTrans.getItemCount() - 1; lnCtr >= 0; lnCtr--) {
                            // Check if the quantity is 0 and remove the record if so
                            if (oTrans.getDetailModel().get(lnCtr).getStockID().isEmpty() ||
                                    Integer.parseInt(oTrans.getDetailModel().get(lnCtr).getQuantity().toString()) == 0) {
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
                    
                    pnRow1 = oTrans.getItemCount() - 1;
                    tblSummary.getSelectionModel().select(pnRow1);
                    loadDetailSummary();
                   System.out.print( "Quantity == ");
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
                txtField10.requestFocus();
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
        AnchorTable01.setDisable(!pbValue);
        if (pnEditMode == EditMode.READY){
            AnchorTable.setDisable(false);
            AnchorTable01.setDisable(false);
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
        R1index05a.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        R1index05b.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        R1index06a.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        R1index06b.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        R1index07a.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        R1index07b.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        
        R1index01.setCellValueFactory(new PropertyValueFactory<>("index01"));
        R1index02.setCellValueFactory(new PropertyValueFactory<>("index02"));
        R1index03.setCellValueFactory(new PropertyValueFactory<>("index03"));
        R1index04.setCellValueFactory(new PropertyValueFactory<>("index04"));
        R1index05a.setCellValueFactory(new PropertyValueFactory<>("index05"));
        R1index05b.setCellValueFactory(new PropertyValueFactory<>("index06"));
        R1index06a.setCellValueFactory(new PropertyValueFactory<>("index07"));
        R1index06b.setCellValueFactory(new PropertyValueFactory<>("index08"));
        R1index07a.setCellValueFactory(new PropertyValueFactory<>("index09"));
        R1index07b.setCellValueFactory(new PropertyValueFactory<>("index10"));
        
        tblRequest.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblRequest.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });
        tblRequest.setItems(R1data);
        tblRequest.autosize();
        
        sIndex01.setStyle("-fx-alignment: CENTER;");
        sIndex02.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        sIndex03.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        sIndex04.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        sIndex05.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        sIndex06.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        sIndex07.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        
        sIndex01.setCellValueFactory(new PropertyValueFactory<>("index01"));
        sIndex02.setCellValueFactory(new PropertyValueFactory<>("index02"));
        sIndex03.setCellValueFactory(new PropertyValueFactory<>("index03"));
        sIndex04.setCellValueFactory(new PropertyValueFactory<>("index04"));
        sIndex05.setCellValueFactory(new PropertyValueFactory<>("index05"));
        sIndex06.setCellValueFactory(new PropertyValueFactory<>("index06"));
        sIndex07.setCellValueFactory(new PropertyValueFactory<>("index07"));
        
        tblSummary.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblSummary.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });
        tblSummary.setItems(R2data);
        tblSummary.autosize();
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
        try {
            if(!oTrans.getDetailModelOthers().isEmpty()){ 
                txtField02.setText((String) oTrans.getDetailModelOthers().get(pnRow).getBrandName()); 
                txtField03.setText((String) oTrans.getDetailModelOthers().get(pnRow).getBarcode()); 
                txtField04.setText((String) oTrans.getDetailModelOthers().get(pnRow).getDescription());   
                txtField05.setText(String.valueOf(oTrans.getDetailModelOthers().get(pnRow).getOnTransit())); 
                txtField06.setText(String.valueOf(oTrans.getDetailModelOthers().get(pnRow).getAverageMonthlySalary())); 
                txtField07.setText(String.valueOf(oTrans.getDetailModelOthers().get(pnRow).getQuantityOnHand())); 
                txtField08.setText(String.valueOf(oTrans.getDetailModelOthers().get(pnRow).getRecordOrder()));  
                txtField09.setText((String) oTrans.getDetailModelOthers().get(pnRow).getClassify());
                txtField10.setText(String.valueOf(oTrans.getDetailModelOthers().get(pnRow).getQuantity()));
            }
         } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }
    
    private void loadDetailSummary(){
        try {
            if(!oTrans.getDetailModel().isEmpty()){
                txtField02.setText((String) oTrans.getDetailModel().get(pnRow1).getBrandName()); 
                txtField03.setText((String) oTrans.getDetailModel().get(pnRow1).getBarcode()); 
                txtField04.setText((String) oTrans.getDetailModel().get(pnRow1).getDescription());   
                txtField05.setText(String.valueOf(oTrans.getDetailModel().get(pnRow1).getOnTransit())); 
                txtField06.setText(String.valueOf(oTrans.getDetailModel().get(pnRow1).getAverageMonthlySalary())); 
                txtField07.setText(String.valueOf(oTrans.getDetailModel().get(pnRow1).getQuantityOnHand())); 
                txtField08.setText(String.valueOf(oTrans.getDetailModel().get(pnRow1).getRecordOrder()));  
                txtField09.setText((String) oTrans.getDetailModel().get(pnRow1).getClassify());
                txtField10.setText(String.valueOf(oTrans.getDetailModel().get(pnRow1).getQuantity()));
            }            
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
        
    }
    
   
    private void loadItemData(){
        int lnCtr;
        R1data.clear();
        R2data.clear();
        if(oTrans.getDetailModelOthers()!= null){
            for (lnCtr = 0; lnCtr < oTrans.getDetailModelOthers().size(); lnCtr++){
                
                R1data.add(new ModelStockRequest(
                   String.valueOf(lnCtr + 1),
                        (String)oTrans.getDetailModelOthers().get(lnCtr).getBarcode(),
                        (String)oTrans.getDetailModelOthers().get(lnCtr).getDescription(),
                        (String)oTrans.getDetailModelOthers().get(lnCtr).getBrandName(),
                   String.valueOf(oTrans.getDetailModelOthers().get(lnCtr).getAverageMonthlySalary()),
                   String.valueOf(oTrans.getDetailModelOthers().get(lnCtr).getQuantityOnHand()),
                   String.valueOf(oTrans.getDetailModelOthers().get(lnCtr).getMinimumLevel()),
                   String.valueOf(oTrans.getDetailModelOthers().get(lnCtr).getReservedOrder()),
                   String.valueOf(oTrans.getDetailModelOthers().get(lnCtr).getRecordOrder()),
                   String.valueOf(oTrans.getDetailModelOthers().get(lnCtr).getQuantity()),
                        (String)oTrans.getDetailModelOthers().get(lnCtr).getStockID()));  
                
            }   
        }
        
        /*tblsummary data*/
        if(oTrans.getDetailModel()!= null){
            for(lnCtr = 0; lnCtr < oTrans.getDetailModel().size(); lnCtr++){
                R2data.add(new ModelStockRequest(
                    String.valueOf(lnCtr+1),  // Keep the existing index
                    oTrans.getDetailModel().get(lnCtr).getBarcode(),  // Update values from currentItem
                    oTrans.getDetailModel().get(lnCtr).getDescription(),
                    String.valueOf(oTrans.getDetailModel().get(lnCtr).getQuantityOnHand()),
                    String.valueOf(oTrans.getDetailModel().get(lnCtr).getAverageMonthlySalary()),
                    String.valueOf(oTrans.getDetailModel().get(lnCtr).getRecordOrder()),
                    String.valueOf(oTrans.getDetailModel().get(lnCtr).getQuantity()),                        
                        (String)oTrans.getDetailModelOthers().get(lnCtr).getStockID(),
                        "",
                        "",
                        ""
                ));
           }
        }
        
        
    }
    
    @FXML
    private void tblSummary_Clicked (MouseEvent event) {
        if (tblSummary.getSelectionModel().getSelectedIndex() >= 0) {
            pnRow1 = tblSummary.getSelectionModel().getSelectedIndex();
            Model_Inv_Stock_Request_Detail clickedItem = oTrans.getDetailModel().get(pnRow1);
            int matchedIndex = IntStream.range(0, oTrans.getDetailModelOthers().size())
             .filter(i -> oTrans.getDetailModelOthers().get(i).getStockID().equals(clickedItem.getStockID()))
             .findFirst()
             .orElse(-1);  // Return -1 if no match is found

            if (matchedIndex != -1) {
                pnRow = matchedIndex;
                System.out.println("StockID found at index: " + matchedIndex);
            }
            txtField09.requestFocus();
            if(!clickedItem.getStockID().isEmpty()){
                tblRequest.getSelectionModel().select(pnRow);
                loadDetails();
            }else{
                tblRequest.getSelectionModel().clearSelection();
                loadDetailSummary();
            }
        }
    }
    
    @FXML
    private void tblDetails_Clicked (MouseEvent event) {
        System.out.println("pnRow = " + pnRow);
        
        // Check if a valid row is selected
        if (tblRequest.getSelectionModel().getSelectedIndex() >= 0) {
            pnRow = tblRequest.getSelectionModel().getSelectedIndex(); 
            loadDetails();  // Load details of the clicked item
            txtField10.requestFocus();
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
                        tblSummary.getSelectionModel().select(pnRow1);
                        System.out.println("Empty stock ID found. Replaced with clicked item.");
                        break;
                    }
                }
        
            loadItemData();
            tblRequest.getSelectionModel().select(pnRow);
            tblSummary.getSelectionModel().select(pnRow1);
            tblRequest.setOnKeyReleased((KeyEvent t)-> {
                KeyCode key = t.getCode();
                switch (key){
                    case DOWN:
                        pnRow = tblRequest.getSelectionModel().getSelectedIndex(); 
                        if (pnRow == tblRequest.getItems().size()) {
                            pnRow = tblRequest.getItems().size();
                            loadDetails();
                        }else {
                            loadDetails();
                        }
                        break;
                    case UP:
                        int pnRows = 0;
                        int x = 1;
                         pnRow = tblRequest.getSelectionModel().getSelectedIndex(); 

                            loadDetails();
                        break;
                    default:
                        break; 
                    }
                });
            }
        }
        
        
        
        
        
        
        
//        System.out.println("pnRow = " + pnRow);
//        
//        // Check if a valid row is selected
//        if (tblRequest.getSelectionModel().getSelectedIndex() >= 0) {
//            pnRow = tblRequest.getSelectionModel().getSelectedIndex(); 
//            loadDetails();  // Load details of the clicked item
//            txtField10.requestFocus();
//            // Retrieve the clicked item from getDetailModelOthers
//            Model_Inv_Stock_Request_Detail clickedItem = oTrans.getDetailModelOthers().get(pnRow);
//            
//
//            // Variable to track if the clicked item has been processed (either replaced or added)
//            int matchedIndex = IntStream.range(0, oTrans.getItemCount())
//                .filter(i -> oTrans.getDetailModel(i).getStockID().equals(clickedItem.getStockID()))
//                .findFirst()
//                .orElse(-1);  // Return -1 if no match is found
//
//            if (matchedIndex != -1) {
//                pnRow1 = matchedIndex;
//                System.out.println("StockID found at index: " + matchedIndex);
//            } else {
//                for (int lnCtr = 0; lnCtr < oTrans.getItemCount(); lnCtr++) {
//                    Model_Inv_Stock_Request_Detail detailModelItem = oTrans.getDetailModel(lnCtr);
//
//                    // Check if StockID is empty
//                    if (Integer.parseInt(detailModelItem.getQuantity().toString())==0) {
//                        // Replace empty stock ID with clickedItem
//                        
//                        oTrans.getDetailModel().set(lnCtr, clickedItem);
//                        pnRow1 = lnCtr; // Update pnRow1 with the current row index
//                        tblSummary.getSelectionModel().select(pnRow1);
//                        System.out.println("Empty stock ID found. Replaced with clicked item.");
//                        break;
//                    }
//                }
//        
//            loadItemData();
//            tblRequest.getSelectionModel().select(pnRow);
//            tblSummary.getSelectionModel().select(pnRow1);
//            tblRequest.setOnKeyReleased((KeyEvent t)-> {
//                KeyCode key = t.getCode();
//                switch (key){
//                    case DOWN:
//                        pnRow = tblRequest.getSelectionModel().getSelectedIndex(); 
//                        if (pnRow == tblRequest.getItems().size()) {
//                            pnRow = tblRequest.getItems().size();
//                            loadDetails();
//                        }else {
//                            loadDetails();
//                        }
//                        break;
//                    case UP:
//                        int pnRows = 0;
//                        int x = 1;
//                         pnRow = tblRequest.getSelectionModel().getSelectedIndex(); 
//
//                            loadDetails();
//                        break;
//                    default:
//                        break; 
//                    }
//                });
//            }
//        }
    }
    private void clearItem(){
        TextField[][] allFields = {
            // Text fields related to specific sections
            { txtField05, txtField06, txtField07,
             txtField08,txtField09, txtField10 },
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
        oTrans.setType(RequestControllerFactory.RequestType.MP);
        oTrans.setCategoryType(RequestControllerFactory.RequestCategoryType.WITH_ROQ);
        oTrans.setTransactionStatus("0123");
        pnEditMode = EditMode.UNKNOWN;     
        initButton(pnEditMode);
    }
}
