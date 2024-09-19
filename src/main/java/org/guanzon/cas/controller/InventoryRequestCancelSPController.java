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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.cas.model.ModelStockRequest;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author User
 */
public class InventoryRequestCancelSPController implements Initializable {
    private final String pxeModuleName = "SP Stock Request Cancel";
    private GRider oApp;
    private int pnEditMode;  
//    private Inventory oTrans;
    private boolean pbLoaded = false;
    private ObservableList<ModelStockRequest> R1data = FXCollections.observableArrayList();

    @FXML
    private AnchorPane AnchorMain;

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
                       txtField09;

    @FXML
    private DatePicker txtSeeks02, 
                        dpField01;

    // Table Views
    @FXML
    private TableView tblRequest01;

    @FXML
    private HBox hbButtons;

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

    @FXML
    private Label lblStatus;

    @FXML
    private ComboBox cmbField01;

    @FXML
    private TableColumn<?, ?> R1index01, 
                             R1index02, 
                             R1index03, 
                             R1index04, 
                             R1index05, 
                             R1index06, 
                             R1index07, 
                             R1index08, 
                             R1index09;


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        pnEditMode = EditMode.UNKNOWN;    
        
        clearAllFields();
        InitTextFields();        
        initButton(pnEditMode);
        initTableR1();
        
        pbLoaded = true;
    }    
    
    private void clearAllFields() {
        // Arrays of TextFields grouped by sections
        TextField[][] allFields = {
            // Text fields related to specific sections
            {txtSeeks01, txtField01, txtField02, txtField03, txtField04,
             txtField05,txtField06, txtField07, txtField08, txtField09,},
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
        TextField[] focusTextFields = { txtSeeks01,
                                        txtField01,
                                        txtField02,
                                        txtField03, 
                                        txtField04,
                                        txtField05,
                                        txtField06, 
                                        txtField07, 
                                        txtField08, 
                                        txtField09};

        // Add the listener to each text field in the focusTextFields array
        for (TextField textField : focusTextFields) {
            textField.focusedProperty().addListener(txtField_Focus);
        }

        // Define arrays for text fields with setOnKeyPressed handlers
//        TextField[] keyPressedTextFields = {
//            txtField06, txtField07, txtField08, txtField09, txtField10,
//            txtField11, txtField12, txtField13, txtField22
//        };

        // Set the same key pressed event handler for each text field in the keyPressedTextFields array
//        for (TextField textField : keyPressedTextFields) {
//            textField.setOnKeyPressed(this::txtField_KeyPressed);
//        }

        txtSeeks01.setOnKeyPressed(this::txtSeeks_KeyPressed);
        txtSeeks02.setOnKeyPressed(this::txtSeeks_KeyPressed);
        
//        lblStatus.setText(chkField04.isSelected() ? "ACTIVE" : "INACTIVE");        
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
                case 5:/*DESCRIPTION*/
//                   oTrans.getModel().setDescription(lsValue);
                   System.out.print( "DESCRIPTION == " );
                    break;
                case 6:/*QOH*/
//                   oTrans.getModel().setDescription(lsValue);
                   System.out.print( "QOH == ");
                    break;
                case 7:/*AMC*/
//                   oTrans.getModel().setDescription(lsValue);
                   System.out.print( "AMC == ");
                    break;
                case 8:/*CO*/
//                   oTrans.getModel().setDescription(lsValue);
                   System.out.print( "CO == ");
                    break;
                    
                case 9:/*QTY*/
//                   oTrans.getModel().setDescription(lsValue);
                   System.out.print( "QTY == ");
                    break;
                
            }                  
        } else
            txtField.selectAll();
    };
    
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
//                        poJSON = oTrans.searchRecord(lsValue, true);
//                        if ("error".equals((String) poJSON.get("result"))){
//                            ShowMessageFX.Information((String) poJSON.get("message"), "Computerized Acounting System", pxeModuleName);
//                           
//                            txtSeeks01.clear();
//                            break;
//                        }
//                        txtSeeks01.setText(oTrans.getModel().getBarcode());
//                        txtSeeks02.setText(oTrans.getModel().getDescription());
//                        pnEditMode = oTrans.getEditMode();
//                        loadInventory();
//                        loadSubUnitData();
//                        loadSubUnit();
//                        initTable();
                        break;
                    case 2 :/*transaction date*/
                            System.out.print("search date == " + lsValue);
//                         poJSON = oTrans.searchRecord(lsValue, false);
//                        if ("error".equals((String) poJSON.get("result"))){
//                            ShowMessageFX.Information((String) poJSON.get("message"), "Computerized Acounting System", pxeModuleName);
//                           
//                            txtSeeks01.clear();
//                            break;
//                        }
//                        pnEditMode = oTrans.getEditMode();
//                        
//                        txtSeeks01.setText(oTrans.getModel().getBarcode());
//                        txtSeeks02.setText(oTrans.getModel().getDescription());
//                        System.out.print("\neditmode on browse == " +pnEditMode);
//                        loadInventory();
//                        loadSubUnitData();
//                        loadSubUnit();
//                        initTable();
//                        System.out.println("EDITMODE sa cancel= " + pnEditMode);
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
        
        if (lbShow){
            txtSeeks01.setDisable(lbShow);
            txtSeeks01.clear();
            txtSeeks02.setDisable(lbShow);
//            txtSeeks02.clear();
            
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
}
