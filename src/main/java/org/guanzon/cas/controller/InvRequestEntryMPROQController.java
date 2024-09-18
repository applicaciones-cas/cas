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
    
    private final String pxeModuleName = "Inventory Request MC ROQ";
    private GRider oApp;
    private int pnEditMode;  
    private Inv_Request oTrans;
    private boolean pbLoaded = false;
    private ObservableList<ModelStockRequest> R1data = FXCollections.observableArrayList();
    private ObservableList<ModelStockRequest> R2data = FXCollections.observableArrayList();
    private int pnRow = 0;
    
    @FXML
    private AnchorPane 
                AnchorMain;

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
                txtField14;

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
                btnClose;

    @FXML
    private Label 
                lblStatus;

    @FXML
    private TableView 
                tblRequest01;

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
                R1index10;

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
//        initTabAnchor();
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
             txtField10,txtField11,txtField12,txtField13,txtField14},

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
                   System.out.print( "Color == ");
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
//            loadItemData();
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
        R1index01.setStyle("-fx-alignment: CENTER;");
        R1index02.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        R1index03.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        R1index04.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        R1index05.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        R1index06.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        
        R1index01.setCellValueFactory(new PropertyValueFactory<>("index01"));
        R1index02.setCellValueFactory(new PropertyValueFactory<>("index02"));
        R1index03.setCellValueFactory(new PropertyValueFactory<>("index03"));
        R1index04.setCellValueFactory(new PropertyValueFactory<>("index04"));
        R1index05.setCellValueFactory(new PropertyValueFactory<>("index05"));
        R1index06.setCellValueFactory(new PropertyValueFactory<>("index06"));
        
        tblRequest01.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblRequest01.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });
        tblRequest01.setItems(R1data);
        tblRequest01.autosize();
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
//        if(!oTrans.getDetailModel().isEmpty()){ 
//            txtField04.setText((String) oTrans.getDetailModel().get(pnRow).getBarcode()); 
//            txtField05.setText((String) oTrans.getDetailModel().get(pnRow).getDescription()); 
//            txtField06.setText((String) oTrans.getDetailModel().get(pnRow).getCategoryName()); 
//            txtField06.setText((String) oTrans.getDetailModel().get(pnRow).getCategoryName()); 
//            txtField07.setText((String) oTrans.getDetailModel().get(pnRow).getBrandName()); 
//            txtField08.setText((String) oTrans.getDetailModel().get(pnRow).getModelName()); 
//            txtField09.setText((String) oTrans.getDetailModel().get(pnRow).getColorName()); 
//            txtField11.setText(oTrans.getDetailModel().get(pnRow).getQuantity().toString()); 
//        }
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
        if(tblRequest01.getSelectionModel().getSelectedIndex() >= 0){
            pnRow = tblRequest01.getSelectionModel().getSelectedIndex(); 
            loadDetails();
        }
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
        oTrans.setType(RequestControllerFactory.RequestType.SP);
        oTrans.setCategoryType(RequestControllerFactory.RequestCategoryType.WITHOUT_ROQ);
        oTrans.setTransactionStatus("0123");
        pnEditMode = EditMode.UNKNOWN;     
        initButton(pnEditMode);
    }
}
