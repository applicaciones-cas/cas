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
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author User
 */
public class InvRequestEntryMPController implements Initializable ,ScreenInterface {
    private final String pxeModuleName = "Inventory Request MP";
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
                    
                    break;  
                    
                case"btnSave":
                    
                    break;
                    
                case"btnUpdate":
                        
                    break;     
                
                case"btnBrowse":
                    
                    break;     
                
                case"btnAddItem":
                   
                    break;  
                    
                case"btnDelItem":
                    
                    break;
                case"btnCancel":
                     
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
             txtField11},

        };
        
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

        // Set the same key pressed event handler for each text field in the keyPressedTextFields array
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
                 
                    
                case 11:/*QTY Request*/
                    
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
                        
                        break;
                    case 05:/**/
                        
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
       
    }
    
    private void loadItemData(){
        int lnCtr;
        R1data.clear();
        if(oTrans.getDetailModel()!= null){
            for (lnCtr = 0; lnCtr < oTrans.getDetailModel().size(); lnCtr++){
                
//            oTrans.getDetailModel().get(lnCtr).list();
                R1data.add(new ModelStockRequest(String.valueOf(lnCtr + 1),
                        "",
                        "",
                        "",
                        "",
                        "",
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
//             tblDetails.getSelectionModel().clearAndSelect(pnRow);
        }
////        loadItemData();
//        if (tblDetails.getSelectionModel().getSelectedIndex() >= 0){
//            pnRow = tblDetails.getSelectionModel().getSelectedIndex();
//            loadDetails();
//            txtField03.requestFocus();
//        }
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
        oTrans.setType(RequestControllerFactory.RequestType.MP);        
        oTrans.setCategoryType(RequestControllerFactory.RequestCategoryType.WITHOUT_ROQ);
        oTrans.setTransactionStatus("0123");
        pnEditMode = EditMode.UNKNOWN;     
        initButton(pnEditMode);
    }
    
}
