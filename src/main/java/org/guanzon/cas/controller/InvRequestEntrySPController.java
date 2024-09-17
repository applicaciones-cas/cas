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
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import static javafx.scene.input.KeyCode.DOWN;
import static javafx.scene.input.KeyCode.ENTER;
import static javafx.scene.input.KeyCode.F3;
import static javafx.scene.input.KeyCode.UP;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.cas.inventory.stock.Inv_Request;
import org.guanzon.cas.inventory.stock.request.RequestControllerFactory;
import org.guanzon.cas.model.ModelStockRequest;
import org.guanzon.cas.model.SharedModel;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author User
 */
public class InvRequestEntrySPController implements Initializable ,ScreenInterface {
    private final String pxeModuleName = "Inventory Request SP";
    private GRider oApp;
    private int pnEditMode;  
    private Inv_Request oTrans;
    private boolean pbLoaded = false;
    private ObservableList<ModelStockRequest> R1data = FXCollections.observableArrayList();
    private ObservableList<ModelStockRequest> R2data = FXCollections.observableArrayList();
    private int pnRow = 0;

    // Main Pane
    @FXML
    private AnchorPane AnchorMain,AnchorTrans,AnchorDetails,AnchorTable;

    // Text Fields
    @FXML
    private TextField txtSeeks01, 
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
            txtField11;

    // Date Pickers
    @FXML
    private DatePicker txtSeeks02, 
            dpField01;

    // Buttons
    @FXML
    private Button btnBrowse, 
            btnNew, 
            btnSave,
            btnUpdate,
            btnSearch,
            btnCancel,
            btnClose,
            btnAddItem,
            btnDelItem;

    // Labels
    @FXML
    private Label lblStatus;

    // Table Views
    @FXML
    private TableView tblDetails;

    // Table Columns
    @FXML
    private TableColumn 
            R2index01,
            R2index02,
            R2index03, 
            R2index04,
            R2index05, 
            R2index06 
           ;

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
        /*Handle button click*/
    private void ClickButton() {
        btnCancel.setOnAction(this::handleButtonAction);
        btnNew.setOnAction(this::handleButtonAction);
        btnSave.setOnAction(this::handleButtonAction);
        btnUpdate.setOnAction(this::handleButtonAction);
        btnClose.setOnAction(this::handleButtonAction);
        btnBrowse.setOnAction(this::handleButtonAction);        
        btnAddItem.setOnAction(this::handleButtonAction);       
        btnDelItem.setOnAction(this::handleButtonAction);
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
//                    if(oTrans.getDetailModel().size()<=1){
//                      if(oTrans.getDetailModel().get(0).getStockID().isEmpty() ||
//                            oTrans.getDetailModel().get(0).getBarcode().isEmpty())
//                          System.out.println("detailmodel save == " +  oTrans.getDetailModel().size());
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
                        pnRow = oTrans.getDetailModel().size() - 1;
//                        
//                        System.out.println("pnRow sa update= " + pnRow);
                        loadItemData();
                        initButton(pnEditMode);
                        initTblDetails();
                        initTabAnchor();
                        txtField03.requestFocus();
                        
                        tblDetails.getSelectionModel().select(pnRow + 1);
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
                        
                            pnRow = oTrans.getDetailModel().size()-1;
                            clearItem();
                            pnEditMode = oTrans.getEditMode();
                            loadItemData();
                            tblDetails.getSelectionModel().select(pnRow + 1);
                        
                    break;  
                    
                case"btnDelItem":
                    if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Do you want to remove this item?") == true){  
                        oTrans.RemoveModelDetail(pnRow);
                       
                        pnRow = oTrans.getDetailModel().size()-1;
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
                            oTrans = new Inv_Request(oApp, true);
                            oTrans.setType(RequestControllerFactory.RequestType.SP);
                            oTrans.setTransactionStatus("0123");
                            pnEditMode = EditMode.UNKNOWN;     
                            initButton(pnEditMode);
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
             txtField05,txtField06, txtField07, txtField08, txtField09, txtField10,
             txtField11},

        };
//        chkField01.setSelected(false);
//        chkField02.setSelected(false);
//        chkField03.setSelected(false);
//        chkField04.setSelected(false);
//        cmbField01.setValue(null);
//        cmbField01.setValue(null);
        
        // Loop through each array of TextFields and clear them
        for (TextField[] fields : allFields) {
            for (TextField field : fields) {
                field.clear();
            }
        }
        R1data.clear();
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
                                        txtField10,
                                        txtField11
        };

        // Add the listener to each text field in the focusTextFields array
        for (TextField textField : focusTextFields) {
            textField.focusedProperty().addListener(txtField_Focus);
        }

        // Define arrays for text fields with setOnKeyPressed handlers
        TextField[] keyPressedTextFields = {
            txtField04, txtField05, txtField08, txtField09, txtField10,
            txtField11
        };

        // Set the same key pressed event handler for each text field in the keyPressedTextFields array
        for (TextField textField : keyPressedTextFields) {
            textField.setOnKeyPressed(this::txtField_KeyPressed);
        }

        txtSeeks01.setOnKeyPressed(this::txtSeeks_KeyPressed);
        txtSeeks02.setOnKeyPressed(this::txtSeeks_KeyPressed);
        
//        lblStatus.setText(chkField04.isSelected() ? "ACTIVE" : "INACTIVE");        
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
                             showTooltip(txtField02, "Error: Input exceeds the maximum allowed.");
                        } else {                        
                            System.out.println("REMARKS == " + lsValue);
                        }
                    break;
                case 3:/*APPROVE BY*/   
//                   oTrans.getModel().setAltBarcode (lsValue);
                   System.out.print( "APPROVE BY == " );
                    break;
                case 4:/*BARRCODE*/
//                   oTrans.getModel().setBriefDescription(lsValue);
                   System.out.print( "BARRCODE == " );
                    break;
                case 5:/*DESC*/
//                   oTrans.getModel().setDescription(lsValue);
                   System.out.print( "DESC == " );
                    break;
                case 6:/*Supplier*/
//                   oTrans.getModel().setDescription(lsValue);
                   System.out.print( "Supplier == ");
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
                   System.out.print( "Color == ");
                    break;    
                 
                case 10:/*Size*/
//                   oTrans.getModel().setDescription(lsValue);
                   System.out.print( "Size == ");
                    break; 
                    
                case 11:/*QTY Request*/
                   System.out.println( "case 11 == " + lsValue);
                   int qty = (lsValue.isEmpty())?0:Integer.parseInt(lsValue);
                   oTrans.getDetailModel().get(oTrans.getDetailModel().size()-1).setQuantity(qty);
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
                        poJson = new JSONObject();
                        poJson =  oTrans.searchDetail(pnRow, 3, lsValue, true);
                        System.out.println("poJson = " + poJson.toJSONString());
                           if("error".equalsIgnoreCase(poJson.get("result").toString())){
                               ShowMessageFX.Information((String) poJson.get("message"), "Computerized Acounting System", pxeModuleName);  
                               break;
                           }
                        break;
                    case 05:/**/
                        poJson = new JSONObject();
                        poJson =  oTrans.searchDetail(pnRow, 3, lsValue, false);
                        System.out.println("poJson = " + poJson.toJSONString());
                            if("error".equalsIgnoreCase(poJson.get("result").toString())){
                               ShowMessageFX.Information((String) poJson.get("message"), "Computerized Acounting System", pxeModuleName);  
                               break;
                            }
                        break;
                } 
                loadDetails();
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
    /*USE TO DISABLE ANCHOR BASE ON INITMODE*/    
    private void initTabAnchor(){
        System.out.print("EDIT MODE == " + pnEditMode);
        boolean pbValue = pnEditMode == EditMode.ADDNEW || 
                pnEditMode == EditMode.UPDATE;
        
        System.out.print("pbValue == " + pbValue);
        AnchorTrans.setDisable(!pbValue);
        AnchorDetails.setDisable(!pbValue);
        AnchorTable.setDisable(!pbValue);
        if (pnEditMode == EditMode.READY){
            AnchorTable.setDisable(false);
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
//    private void initButton(int fnValue){
//        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);
//        btnCancel.setVisible(lbShow);
//        btnSearch.setVisible(lbShow);
//        btnSave.setVisible(lbShow); 
//        
//        btnSave.setManaged(lbShow);
//        btnCancel.setManaged(lbShow);
//        btnSearch.setManaged(lbShow);
//        btnUpdate.setVisible(!lbShow);
//        btnBrowse.setVisible(!lbShow);
//        btnNew.setVisible(!lbShow);
//
//        txtSeeks01.setDisable(!lbShow);
//        txtSeeks02.setDisable(!lbShow);
//        
//        if (lbShow){
//            txtSeeks01.setDisable(lbShow);
//            txtSeeks01.clear();
//            txtSeeks02.setDisable(lbShow);
////            txtSeeks02.clear();
//            
//            btnCancel.setVisible(lbShow);
//            btnSearch.setVisible(lbShow);
//            btnSave.setVisible(lbShow);
//            btnUpdate.setVisible(!lbShow);
//            btnBrowse.setVisible(!lbShow);
//            btnNew.setVisible(!lbShow);
//            btnBrowse.setManaged(false);
//            btnNew.setManaged(false);
//            btnUpdate.setManaged(false);
//            btnClose.setManaged(false);
//        }
//        else{
//            txtSeeks01.setDisable(lbShow);
//            txtSeeks01.requestFocus();
//            txtSeeks02.setDisable(lbShow);  
//        }
//    }
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

    // Manage visibility and managed state of other buttons
    btnBrowse.setVisible(!lbShow);
    btnNew.setVisible(!lbShow);
    btnBrowse.setManaged(!lbShow);
    btnNew.setManaged(!lbShow);

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
}

    
    private void initTblDetails() {
        R2index01.setStyle("-fx-alignment: CENTER;");
        R2index02.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        R2index03.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        R2index04.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        R2index05.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        R2index06.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        
        R2index01.setCellValueFactory(new PropertyValueFactory<>("index01"));
        R2index02.setCellValueFactory(new PropertyValueFactory<>("index02"));
        R2index03.setCellValueFactory(new PropertyValueFactory<>("index03"));
        R2index04.setCellValueFactory(new PropertyValueFactory<>("index04"));
        R2index05.setCellValueFactory(new PropertyValueFactory<>("index05"));
        R2index06.setCellValueFactory(new PropertyValueFactory<>("index06"));
        
        tblDetails.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblDetails.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });
        tblDetails.setItems(R1data);
        tblDetails.autosize();
    }
    
   
    private void showTooltip(TextField textField, String message) {
        // Create and configure the Tooltip
        Tooltip tooltip = new Tooltip(message);
        tooltip.setStyle("-fx-border-color: red; "); // Customize tooltip style if needed

        // Attach the Tooltip to the TextField
        textField.setTooltip(tooltip);

        // Optionally, show the tooltip programmatically if needed
        Tooltip.install(textField, tooltip);
    }
    private void loadTransaction(){
     if(pnEditMode == EditMode.READY || 
                pnEditMode == EditMode.ADDNEW || 
                pnEditMode == EditMode.UPDATE){

            txtField01.setText((String) oTrans.getMasterModel().getTransactionNumber());
            txtField02.setText((String) oTrans.getMasterModel().getRemarks());
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
            
//            switch (oTrans.getMasterModel().getTransactionStatus()) {
//                case "0":
//                    lblStatus.setText("OPEN");
//                    break;
//                case "1":
//                    lblStatus.setText("CLOSED");
//                    break;
//                case "2":
//                    lblStatus.setText("POSTED");
//                    break;
//                case "3":
//                    lblStatus.setText("CANCELLED");
//                    break;
//                case "5":
//                    lblStatus.setText("VOID");
//                    break;
//                default:
//                    lblStatus.setText("UNKNOWN");
//                    break;
//            }
            lblStatus.setText(SharedModel.TRANSACTION_STATUS.get(Integer.parseInt(oTrans.getMasterModel().getTransactionStatus().toString())));

     }
    }
    private void loadDetails(){
        if(!oTrans.getDetailModel().isEmpty()){ 
            txtField04.setText((String) oTrans.getDetailModel().get(pnRow).getBarcode()); 
            txtField05.setText((String) oTrans.getDetailModel().get(pnRow).getDescription()); 
            txtField06.setText((String) oTrans.getDetailModel().get(pnRow).getCategoryName()); 
            txtField06.setText((String) oTrans.getDetailModel().get(pnRow).getCategoryName()); 
            txtField07.setText((String) oTrans.getDetailModel().get(pnRow).getBrandName()); 
            txtField08.setText((String) oTrans.getDetailModel().get(pnRow).getModelName()); 
            txtField09.setText((String) oTrans.getDetailModel().get(pnRow).getColorName()); 
            txtField10.setText((String) oTrans.getDetailModel().get(pnRow).getMeasureName()); 
            txtField11.setText(oTrans.getDetailModel().get(pnRow).getQuantity().toString()); 
        }
    }
    
    private void loadItemData(){
        int lnCtr;
        R1data.clear();
        if(oTrans.getDetailModel()!= null){
            for (lnCtr = 0; lnCtr < oTrans.getDetailModel().size(); lnCtr++){
                
//            oTrans.getDetailModel().get(lnCtr).list();
                R1data.add(new ModelStockRequest(String.valueOf(lnCtr + 1),
                        (String)oTrans.getDetailModel().get(lnCtr).getBarcode(),
                        (String)oTrans.getDetailModel().get(lnCtr).getDescription(),
                        oTrans.getDetailModel().get(lnCtr).getQuantityOnHand().toString(),
                        oTrans.getDetailModel().get(lnCtr).getReservedOrder().toString(),
                        oTrans.getDetailModel().get(lnCtr).getQuantity().toString(),
                        "",
                        "",
                        "",
                        ""));  
                
            }
        }
    }
    @FXML
    private void tblDetails_Clicked (MouseEvent event) {
        System.out.println("pnRow = " + pnRow);
        if(tblDetails.getSelectionModel().getSelectedIndex() >= 0){
            pnRow = tblDetails.getSelectionModel().getSelectedIndex(); 
            loadDetails();
        }
        tblDetails.setOnKeyReleased((KeyEvent t)-> {
            KeyCode key = t.getCode();
            switch (key){
                case DOWN:
                    pnRow = tblDetails.getSelectionModel().getSelectedIndex(); 
                    if (pnRow == tblDetails.getItems().size()) {
                        pnRow = tblDetails.getItems().size();
                        loadDetails();
                    }else {
                        loadDetails();
                    }
                    break;
                case UP:
                    int pnRows = 0;
                    int x = 1;
                     pnRow = tblDetails.getSelectionModel().getSelectedIndex(); 

                        loadDetails();
                    break;
                default:
                    break; 
            }
        });
    }
    
    private void clearItem(){
        TextField[][] allFields = {
            // Text fields related to specific sections
            { txtField05, txtField06, txtField07,
             txtField08,txtField09,  txtField10, txtField11 },
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
        oTrans.setType(RequestControllerFactory.RequestType.SP);
        oTrans.setCategoryType(RequestControllerFactory.RequestCategoryType.WITHOUT_ROQ);
        oTrans.setTransactionStatus("0123");
        pnEditMode = EditMode.UNKNOWN;     
        initButton(pnEditMode);
    }
    
}
