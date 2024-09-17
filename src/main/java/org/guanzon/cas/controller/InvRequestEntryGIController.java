/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.cas.controller;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import static javafx.scene.input.KeyCode.DOWN;
import static javafx.scene.input.KeyCode.ENTER;
import static javafx.scene.input.KeyCode.F3;
import static javafx.scene.input.KeyCode.UP;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.cas.inventory.base.InvMaster;
import org.guanzon.cas.inventory.base.Inventory;
import org.guanzon.cas.inventory.stock.InvStockRequest;
import org.guanzon.cas.inventory.stock.Inv_Request;
import org.guanzon.cas.inventory.stock.request.RequestControllerFactory;
import org.guanzon.cas.model.ModelInvSubUnit;
import org.guanzon.cas.model.ModelStockRequest;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author User
 */
 

public class InvRequestEntryGIController implements Initializable,ScreenInterface {
    private final String pxeModuleName = "GI Stock Request";
    private GRider oApp;
    private int pnEditMode;  
    private Inv_Request oTrans;
    private boolean pbLoaded = false;
    private ObservableList<ModelStockRequest> R1data = FXCollections.observableArrayList();
    private int pnRow = 0;
    
    
    @FXML
    private AnchorPane AnchorMain, 
        AnchorInput, 
        AnchorTable;

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
            txtField11, 
            txtField12, 
            txtField13;

    // Date Pickers
    @FXML
    private DatePicker txtSeeks02, 
            dpField01;

    // ComboBox
    @FXML
    private ComboBox cmbField01;

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
    private TableView tblRequest01;

    // Table Columns for tblRequest01
    @FXML
    private TableColumn R1index01, 
            R1index02, 
            R1index03, 
            R1index04, 
            R1index05, 
            R1index06, 
            R1index07, 
            R1index08, 
            R1index09;

    // HBox
    @FXML
    private HBox hbButtons;

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
        oTrans = new Inv_Request(oApp, false);
      
        initTrans();
        ClickButton();
        clearAllFields();
        InitTextFields();        
        initButton(pnEditMode);
        initTableR1();
        loadDetails();
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
                        initButton(pnEditMode);
                        clearAllFields();
                        initdatepicker();
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
                    if(oTrans.getDetailModel().size()<=1){
                      if(oTrans.getDetailModel().get(0).getStockID().isEmpty() ||
                            oTrans.getDetailModel().get(0).getBarcode().isEmpty()){
                            ShowMessageFX.Information("Empty item request detected, Please check your entry", "Computerized Acounting System", pxeModuleName);
                        break;    
                      }
                    }
                    poJSON = oTrans.saveTransaction();
                        System.out.println(poJSON.toJSONString());
                        if ("success".equals((String) poJSON.get("result"))){
                            System.err.println((String) poJSON.get("message"));
                            ShowMessageFX.Information((String) poJSON.get("message"), "Computerized Acounting System", pxeModuleName);

                            pnEditMode = EditMode.UNKNOWN;
                            initButton(pnEditMode);
                            clearAllFields();
                            initTabAnchor();

                            System.out.println("Record saved successfully.");
                        } else {
                            ShowMessageFX.Information((String)poJSON.get("message"), "Computerized Acounting System", pxeModuleName);
                            System.out.println("The record was not saved successfully.");
                            System.out.println((String) poJSON.get("message"));
                        }
                    break;
                case"btnUpdate":
                    poJSON = oTrans.updateTransaction();
                        if ("error".equals((String) poJSON.get("result"))){
                            ShowMessageFX.Information((String)poJSON.get("message"), "Computerized Acounting System", pxeModuleName);
                            break;
                        }
                         pnEditMode =  oTrans.getEditMode();
                        System.out.println("EDITMODE sa update= " + pnEditMode);
                        initButton(pnEditMode);
                        initTableR1();
                        initTabAnchor();
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
                        pnEditMode = oTrans.getEditMode();
                        R1data.clear();                           
                        loadTransaction();
                        initTableR1();
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
                        pnEditMode = oTrans.getEditMode();
                        pnRow = oTrans.getDetailModel().size() - 1;
                        clearItem();
                        loadItemData();
                    break;  
                    
                case"btnDelItem":
                    if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Do you want to remove this item?") == true){  
                        oTrans.RemoveModelDetail(pnRow);
                        pnRow = oTrans.getDetailModel().size()-1;
                        clearItem();
                        loadItemData();
                    }
                    break;
                case"btnCancel":
                     if (ShowMessageFX.YesNo("Do you really want to cancel this record? \nAny data collected will not be kept.", "Computerized Acounting System", pxeModuleName)){                            
                            clearAllFields();
                            oTrans = new Inv_Request(oApp, true);
                            oTrans.setTransactionStatus("0123");
                            pnEditMode = EditMode.UNKNOWN;     
                            initButton(pnEditMode);
                            initTabAnchor();
                        }
                    break; 
            }
        }
    }
    private void clearItem(){
        TextField[][] allFields = {
            // Text fields related to specific sections
            {txtField03, txtField04, txtField05, txtField06, txtField07,
             txtField08,txtField09,  txtField10, txtField11, txtField12, txtField13},
        };
        for (TextField[] fields : allFields) {
            for (TextField field : fields) {
                field.clear();
            }
        }
    }
    
    private void clearAllFields() {
        // Arrays of TextFields grouped by sections
        TextField[][] allFields = {
            // Text fields related to specific sections
            {txtSeeks01, txtField01, txtField02, txtField03, txtField04,
             txtField05,txtField06, txtField07, txtField08, txtField09,
             txtField10, txtField11, txtField12, txtField13},
        };
        
        // Loop through each array of TextFields and clear them
        for (TextField[] fields : allFields) {
            for (TextField field : fields) {
                field.clear();
            }
        }
        R1data.clear();
        lblStatus.setText("UNKNOWN");
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
    
    /*initialize fields*/
    private void InitTextFields(){
       // Define arrays for text fields with focusedProperty listeners
        TextField[] focusTextFields = {
            txtField01, txtField02, txtField03, txtField04, txtField05,txtField06
        };

        // Add the listener to each text field in the focusTextFields array
        for (TextField textField : focusTextFields) {
            textField.focusedProperty().addListener(txtField_Focus);
        }

        // Define arrays for text fields with setOnKeyPressed handlers
        TextField[] keyPressedTextFields = {
            txtField03,txtField04, txtField07, txtField08, txtField09, txtField10,
            txtField11, txtField12, txtField13,
        };

        // Set the same key pressed event handler for each text field in the keyPressedTextFields array
        for (TextField textField : keyPressedTextFields) {
            textField.setOnKeyPressed(this::txtField_KeyPressed);
        }

        txtSeeks01.setOnKeyPressed(this::txtSeeks_KeyPressed);
        initdatepicker();

        // Set a custom StringConverter to format date
           DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    }
    
    final ChangeListener<? super Boolean> txtField_Focus = (o,ov,nv)->{ 
        if (!pbLoaded) return;
       
        TextField txtField = (TextField)((ReadOnlyBooleanPropertyBase)o).getBean();
        int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
        String lsValue = txtField.getText();
        JSONObject jsonObject = new JSONObject();
        if (lsValue == null) return;         
        if(!nv){ /*Lost Focus*/
            switch (lnIndex){
                case 1: /*TRANSACTION NO*/
//                   oTrans.getMasterModel().setStockID(lsValue);
                   System.out.print( "TRANSACTION NO == " );
                    break;
                case 2:/*REMARKS*/
//                   oTrans.getMasterModel().setBarcode(lsValue);
                        if (lsValue.length() > 128) {
                            // Call the tooltip method
                           showTooltip(txtField02, "Error: Input exceeds the maximum allowed.");
                        } else {                        
                            System.out.println("REMARKS == " + lsValue);
                            oTrans.getMasterModel().setRemarks(lsValue);
                        }
                        
                    break;
                case 3:/*BARRCODE*/   
//                   oTrans.getMasterModel().setAltBarcode (lsValue);
                   System.out.print( "BARRCODE == " );
                    break;
                case 4:/*DESCRIPTION*/
//                   oTrans.getMasterModel().setBriefDescription(lsValue);
                   System.out.print( "DESCRIPTION == " );
                    break;
                case 5:/*CATEGORY*/
//                   oTrans.getMasterModel().setDescription(lsValue);
                   System.out.print( "CATEGORY == " );
                    break;
                case 6:/*BRAND*/
//                   oTrans.getMasterModel().setDescription(lsValue);
                   System.out.print( "BRAND == ");
                    break;
                case 7:/*MODEL*/
//                   oTrans.getMasterModel().setDescription(lsValue);
                   System.out.print( "MODEL == ");
                    break;
                case 8:/*COLOR*/
//                   oTrans.getMasterModel().setDescription(lsValue);
                   System.out.print( "COLOR == ");
                    break;
                    
                case 9:/*MEASURE*/
//                   oTrans.getMasterModel().setDescription(lsValue);
                   System.out.print( "MEASURE == ");
                    break;
                case 10:/*MIN LVL*/
//                   oTrans.getMasterModel().setDescription(lsValue);
                   System.out.print( "MIN LVL == ");
                    break;
                case 11:/*MAX LVL*/
//                   oTrans.getMasterModel().setDescription(lsValue);
                   System.out.print( "MAX LVL == ");
                    break;
                case 12:/*QOH*/
//                   oTrans.getMasterModel().setDescription(lsValue);
                   System.out.print( "QOH == ");
                    break;
                    
                case 13:/*REQUEST*/
//                   oTrans.getMasterModel().setDescription(lsValue);
                   System.out.print( "REQUEST == ");
                    break;
            }                  
        } else
            txtField.selectAll();
    };
    /*Text Field with search*/
    private void txtField_KeyPressed(KeyEvent event){
        TextField txtField = (TextField)event.getSource();
        int lnIndex = Integer.parseInt(((TextField)event.getSource()).getId().substring(8,10));
        String lsValue = (txtField.getText() == null ?"": txtField.getText());
        JSONObject poJson;
        switch (event.getCode()) {
            case F3:
            case ENTER:
                switch (lnIndex){
                    case 03: /*search by barrcode*/
                        poJson = new JSONObject();
                        poJson =  oTrans.searchDetail(pnRow, 3, lsValue, true);
                           System.out.println("poJson = " + poJson.toJSONString());
                           if("error".equalsIgnoreCase(poJson.get("result").toString())){
                               ShowMessageFX.Information((String) poJson.get("message"), "Computerized Acounting System", pxeModuleName);    
                           }
                        loadDetails();
                        loadItemData();
                        break;
                    case 04: /*search by description*/
                        poJson = new JSONObject();
                        poJson =  oTrans.searchDetail(pnRow, 3, lsValue, false);
                           System.out.println("poJson = " + poJson.toJSONString());
                           if("error".equalsIgnoreCase(poJson.get("result").toString())){
                               ShowMessageFX.Information((String) poJson.get("message"), "Computerized Acounting System", pxeModuleName);      
                               break;
                           }
                        loadDetails();
                        loadItemData();
                        break;
                }      
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
            case ENTER:    
                switch (lnIndex){
                    case 1: /*transaction no*/
                        System.out.print("search transaction == " + lsValue);
                       poJSON = oTrans.searchTransaction("sTransNox", lsValue, pbLoaded);
                        if ("error".equals((String) poJSON.get("result"))){
                            ShowMessageFX.Information((String) poJSON.get("message"), "Computerized Acounting System", pxeModuleName);
                           
                            txtSeeks01.clear();
                            break;
                        }
                        txtSeeks01.setText(oTrans.getMasterModel().getTransactionNumber());
                        pnEditMode = oTrans.getEditMode();
                        R1data.clear();                           
                        loadTransaction();
                        initTableR1();
                        loadItemData();
                        initTabAnchor();
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
    
    private void initButton(int fnValue){
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);
        btnCancel.setVisible(lbShow);
        btnSearch.setVisible(lbShow);
        btnSave.setVisible(lbShow); 
        
        btnSave.setManaged(lbShow);
        btnCancel.setManaged(lbShow);
        btnSearch.setManaged(lbShow);
        btnUpdate.setVisible(!lbShow);
        btnBrowse.setVisible(!lbShow);
        btnNew.setVisible(!lbShow);

        txtSeeks01.setDisable(!lbShow);
        txtSeeks02.setDisable(!lbShow);
        
        
        
        btnAddItem.setDisable(true);
        btnDelItem.setDisable(true);
        if (lbShow){
            txtSeeks01.setDisable(lbShow);
            txtSeeks01.clear();
            txtSeeks02.setDisable(lbShow);
            
            btnCancel.setVisible(lbShow);
            btnSearch.setVisible(lbShow);
            btnSave.setVisible(lbShow);
            btnUpdate.setVisible(!lbShow);
            btnBrowse.setVisible(!lbShow);
            btnNew.setVisible(!lbShow);
            btnBrowse.setManaged(false);
            btnNew.setManaged(false);
            btnUpdate.setManaged(false);
            btnClose.setManaged(false);
            
            btnAddItem.setDisable(false);
            btnDelItem.setDisable(false);
            
        }
        else{
            txtSeeks01.setDisable(lbShow);
            txtSeeks01.requestFocus();
            txtSeeks02.setDisable(lbShow);  
        }
    }
    
    private void initTableR1() {
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
    }
    
    private void showTooltip(TextField textField, String message) {
        // Create and configure the Tooltip
        Tooltip tooltip = new Tooltip(message);
        tooltip.setStyle("-fx-border-color: red; -fx-text-fill: white;"); // Customize tooltip style if needed

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
     if(pnEditMode == EditMode.READY || 
                pnEditMode == EditMode.ADDNEW || 
                pnEditMode == EditMode.UPDATE){

            txtField03.setText((String) oTrans.getDetailModel().get(pnRow).getBarcode()); 
            txtField04.setText((String) oTrans.getDetailModel().get(pnRow).getDescription()); 
            txtField05.setText((String) oTrans.getDetailModel().get(pnRow).getCategoryName()); 
            
            txtField06.setText((String) oTrans.getDetailModel().get(pnRow).getBrandName()); 
            txtField07.setText((String) oTrans.getDetailModel().get(pnRow).getModelName()); 
            txtField08.setText((String) oTrans.getDetailModel().get(pnRow).getColorName()); 
            txtField08.setText((String) oTrans.getDetailModel().get(pnRow).getMeasureName()); 
            txtField11.setText(Integer.toString(oTrans.getDetailModel().get(pnRow).getMaximumLevel())); 
            txtField12.setText( oTrans.getDetailModel().get(pnRow).getQuantityOnHand().toString()); 
     }
    }
    
    private void loadItemData(){
        int lnCtr;
        R1data.clear();
        if(oTrans.getDetailModel()!= null){
            for (lnCtr = 0; lnCtr < oTrans.getDetailModel().size(); lnCtr++){
                R1data.add(new ModelStockRequest(String.valueOf(lnCtr + 1),
                        (String)oTrans.getDetailModel().get(lnCtr).getBarcode(),
                        (String)oTrans.getDetailModel().get(lnCtr).getDescription(),
                        (String)oTrans.getDetailModel().get(lnCtr).getClassify(),
                        oTrans.getDetailModel().get(lnCtr).getReservedOrder().toString(),
                        (Integer.toString(oTrans.getDetailModel().get(lnCtr).getAverageMonthlySalary())),
                         oTrans.getDetailModel().get(lnCtr).getQuantityOnHand().toString(),
                        oTrans.getDetailModel().get(lnCtr).getBackOrder().toString(),
                        oTrans.getDetailModel().get(lnCtr).getQuantity().toString(),
                        ""));  
            }
        }
    }
    
    public void initdatepicker(){
        // Set a custom StringConverter to format date
          DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        dpField01.setConverter(new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    oTrans.setMaster(4,dateFormatter.format(date).toString());
                    System.out.println("dCltSince = " + date);
                    
                    dpField01.setValue(date);
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    oTrans.setMaster(4,string);
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        });
    }
    
   @FXML
    private void tblRequest01_Clicked (MouseEvent event) {
        System.out.println("pnRow = " + pnRow);
//        loadItemData();
        if (tblRequest01.getSelectionModel().getSelectedIndex() >= 0){
            pnRow = tblRequest01.getSelectionModel().getSelectedIndex();
            loadDetails();
            txtField03.requestFocus();
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
