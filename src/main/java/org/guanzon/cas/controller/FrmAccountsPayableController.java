/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.cas.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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
import org.guanzon.cas.clients.account.AP_Client_Master;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author User
 */
public class FrmAccountsPayableController implements Initializable,ScreenInterface {
    private final String pxeModuleName = "Accounts Payable Clients";
    private GRider oApp;
    private int pnEditMode;  
    private AP_Client_Master oTrans;
    private boolean state = false;
    private boolean pbLoaded = false;
    private String oTransnox = "";
    private DashboardController fdController = new DashboardController();
    @FXML
    private AnchorPane AnchorMain;

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
    private TextField txtSearch02;

    @FXML
    private TextField txtSearch01;

    @FXML
    private TextField txtField01;

    @FXML
    private TextField txtField04;

    @FXML
    private TextField txtField10;

    @FXML
    private TextField txtField02;

    @FXML
    private TextField txtField09;

    @FXML
    private TextField txtField11;

    @FXML
    private TextField txtField08;

    @FXML
    private TextField txtField12;

    @FXML
    private TextField txtField13;

    @FXML
    private TextField txtField07;

    @FXML
    private CheckBox chkfield01;

    @FXML
    private TextField txtField03;

    @FXML
    private TextField txtField05;

    @FXML
    private TextField txtField06;

    @FXML
    private DatePicker cpField01;

    @FXML
    private TableView tblLedger;

    @FXML
    private TableColumn indexAddress01;

    @FXML
    private TableColumn indexAddress02;

    @FXML
    private TableColumn indexAddress03;

    @FXML
    private TableColumn indexAddress04;

    @FXML
    private TableColumn indexAddress05;

    @FXML
    private TableColumn indexAddress06;

    @FXML
    void tblLedger_Clicked(MouseEvent event) {

    }


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        oTrans = new AP_Client_Master(oApp, true);
        
//        if (txtField01 != null) { // Check if txtField01 is not null before setting its text
//            txtField01.setText(String.valueOf(oTrans.getMasterModel().getClientID().toString()));
//        } else {
//            // Handle the case where txtField01 is null
//            System.out.println("txtField01 is null");
//        }
        InitTextFields();
        ClickButton();
        initTabAnchor();
        initSearchFields();
        pbLoaded = true;
    }    

    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
        
    }
    public void setTransaction(String fsValue){
        oTransnox = fsValue;
    }
    
    public void setState(boolean fsValue){
        state = fsValue;
    }
    
    
    private void ClickButton() {
        btnCancel.setOnAction(this::handleButtonAction);
        btnNew.setOnAction(this::handleButtonAction);
        btnSave.setOnAction(this::handleButtonAction);
        btnUpdate.setOnAction(this::handleButtonAction);
        btnClose.setOnAction(this::handleButtonAction);
    }
    private void initSearchFields(){
        /*textFields FOCUSED PROPERTY*/
//        txtSearch01.focusedProperty().addListener(txtField_Focus);
        txtSearch01.setOnKeyPressed(this::searchinfo_KeyPressed);
        txtSearch02.setOnKeyPressed(this::searchinfo_KeyPressed);
        
//        txtSeeks99.focusedProperty().addListener(txtField_Focus);
    }
    private void searchinfo_KeyPressed(KeyEvent event){
        TextField txtSearch = (TextField)event.getSource();
        int lnIndex = Integer.parseInt(((TextField)event.getSource()).getId().substring(9,11));
        String lsValue = txtSearch.getText();
        JSONObject poJson;
        switch (event.getCode()) {
            case F3:
                switch (lnIndex){
                    case 02: /*search branch*/
//                        poJson = oTrans.searchRecord(lsValue, false);
//                        if ("error".equals((String) poJson.get("result"))){
//                            ShowMessageFX.Information((String) poJson.get("message"), "Computerized Acounting System", pxeModuleName);
//                            txtSearch02.clear();
//                            break;
//                        }
//                        pnEditMode = oTrans.getEditMode();
//                        loadDetail();
                        
//                        retrieveDetails();
                    break;
                    case 01: /*search branch*/
//                        poJson = oTrans.searchRecord(lsValue, false);
//                        if ("error".equals((String) poJson.get("result"))){
//                            ShowMessageFX.Information((String) poJson.get("message"), "Computerized Acounting System", pxeModuleName);
//                            txtSearch01.clear();
//                            break;
//                        }
//                        pnEditMode = oTrans.getEditMode();
//                        loadDetail();
                        
//                        retrieveDetails();
                    break;
                }
            case ENTER:
                
        }
        
        switch (event.getCode()){
        case ENTER:
            CommonUtils.SetNextFocus(txtSearch);
        case DOWN:
            CommonUtils.SetNextFocus(txtSearch);
            break;
        case UP:
            CommonUtils.SetPreviousFocus(txtSearch);
        }
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
                case "btnNew":
                        
                    break;
                case "btnUpdate":
                        
                    break;
                case "btnCancel":
                        if (ShowMessageFX.YesNo("Do you really want to cancel this record? \nAny data collected will not be kept.", "Computerized Acounting System", pxeModuleName)){
//                            clearAllFields();
//                            
//                            pnEditMode = EditMode.UNKNOWN;
                            initTabAnchor();
                        }
                    break;
                case "btnSave":
                    
//                        if(!personalinfo01.getText().toString().isEmpty()){
//                            oTrans.getModel().setFullName(personalinfo01.getText());
//                        }
//                        JSONObject saveResult = oTrans.saveRecord();
//                        if ("success".equals((String) saveResult.get("result"))){
//                            System.err.println((String) saveResult.get("message"));
//                            ShowMessageFX.Information((String) saveResult.get("message"), "Computerized Acounting System", pxeModuleName);
//                            clearAllFields();
//                            pnEditMode = EditMode.READY;
//                            initButton(pnEditMode);
//                            initTabAnchor();
//                            System.out.println("Record saved successfully.");
//                        } else {
//                            ShowMessageFX.Information((String)saveResult.get("message"), "Computerized Acounting System", pxeModuleName);
//                            System.out.println("Record not saved successfully.");
//                            System.out.println((String) saveResult.get("message"));
//                        }
                        
                     break;

                case "btnAddSocMed":
                break;
        }
    }
}
    private void InitTextFields(){
        
        txtField01.focusedProperty().addListener(txtField_Focus);
        txtField02.focusedProperty().addListener(txtField_Focus);
        txtField03.focusedProperty().addListener(txtField_Focus);
        txtField04.focusedProperty().addListener(txtField_Focus);
        txtField05.focusedProperty().addListener(txtField_Focus);
        txtField06.focusedProperty().addListener(txtField_Focus);
        txtField07.focusedProperty().addListener(txtField_Focus);
        txtField08.focusedProperty().addListener(txtField_Focus);
        txtField09.focusedProperty().addListener(txtField_Focus);
        txtField10.focusedProperty().addListener(txtField_Focus);
        txtField11.focusedProperty().addListener(txtField_Focus);
        txtField12.focusedProperty().addListener(txtField_Focus);
        txtField13.focusedProperty().addListener(txtField_Focus);
        
        txtField02.setOnKeyPressed(this::txtField_KeyPressed);
    }
    private void initTabAnchor(){
        boolean pbValue = pnEditMode == EditMode.ADDNEW || 
                pnEditMode == EditMode.UPDATE;
//        anchorPersonal.setDisable(!pbValue);
//        anchorAddress.setDisable(!pbValue);
//        anchorMobile.setDisable(!pbValue);
//        anchorEmail.setDisable(!pbValue);
//        anchorSocial.setDisable(!pbValue);
//        anchorContctPerson.setDisable(!pbValue);
//        anchorOtherInfo.setDisable(!pbValue);
    
    }
    
    private void txtField_KeyPressed(KeyEvent event){
        TextField txtField = (TextField)event.getSource();
        int lnIndex = Integer.parseInt(((TextField)event.getSource()).getId().substring(8,10));
        String lsValue = txtField.getText();
        JSONObject poJson;
        switch (event.getCode()) {
            case F3:
                switch (lnIndex){
                    case 02: /*search branch*/
                        poJson = new JSONObject();
                           poJson =  oTrans.SearchClient(lsValue, false);
                           
                           System.out.println("poJson = " + poJson.toJSONString());
                           if("error".equalsIgnoreCase(poJson.get("result").toString())){
                               loadCompanyTransaction();
                           }
                           txtField01.setText((String) poJson.get("sClientID"));                    
                           txtField02.setText((String) poJson.get("sCompnyNm"));                           
                           txtField03.setText((String) poJson.get("xAddressx"));
                           txtField04.setText((String) poJson.get("sCPerson1"));
                        break;
                        
                        

                    
                }
            case ENTER:
                
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
    
    final ChangeListener<? super Boolean> txtField_Focus = (o,ov,nv)->{ 
        if (!pbLoaded) return;
       
        TextField txtField = (TextField)((ReadOnlyBooleanPropertyBase)o).getBean();
        int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
        String lsValue = txtField.getText();
        JSONObject jsonObject = new JSONObject();
        if (lsValue == null) return;         
        if(!nv){ /*Lost Focus*/
            switch (lnIndex){
                case 1: /*company id*/
//                    jsonObject = oTrans.setMaster( 8,lsValue);
//                    jsonObject = oTrans.getModel().setFullName(lsValue);
                    break;
                case 2:/*company name*/
//                    jsonObject = oTrans.setMaster( "sLastName",lsValue);
//                    jsonObject = oTrans.getModel().setLastName(lsValue);
                    break;
                case 3:/*frist name*/
//                    jsonObject = oTrans.setMaster( 4,lsValue);
//                    jsonObject = oTrans.getModel().setFirstName(lsValue);
                    break;
                case 4:/*middle name*/
//                    jsonObject = oTrans.setMaster( 5,lsValue);
//                    jsonObject = oTrans.getModel().setMiddleName(lsValue);
                    break;
                case 5:/*suffix name*/
//                    jsonObject = oTrans.setMaster( 6,lsValue);
//                    jsonObject = oTrans.getModel().setSuffixName(lsValue);
                    break;  
                case 6:/*spouse */
//                    jsonObject = oTrans.setMaster( 15,lsValue);
                    break;
                case 7:/*mothers maiden namex */
//                    jsonObject = oTrans.setMaster( 7,lsValue);
                    break;
                case 8:/*mothers maiden namex */
//                    jsonObject = oTrans.setMaster( "sTaxIDNox",lsValue);
//                    jsonObject = oTrans.getModel().setTaxIDNumber(lsValue);
                    break;
                case 9:/*mothers maiden namex */
//                    jsonObject = oTrans.setMaster( "sLTOIDxxx",lsValue);
//                    jsonObject = oTrans.getModel().setLTOIDNumber(lsValue);
                    break;
                case 10 :/*mothers maiden namex */
//                    jsonObject = oTrans.setMaster( "sPHBNIDxx",lsValue);
//                    jsonObject = oTrans.getModel().setPHNationalIDNumber(lsValue);
                    break;
                case 11 :/*mothers maiden namex */
//                    jsonObject = oTrans.setMaster( "sPHBNIDxx",lsValue);
//                    jsonObject = oTrans.getModel().setPHNationalIDNumber(lsValue);
                    break;
                case 12 :/*mothers maiden namex */
//                    jsonObject = oTrans.setMaster( "sPHBNIDxx",lsValue);
//                    jsonObject = oTrans.getModel().setPHNationalIDNumber(lsValue);
                    break;
                case 13 :/*mothers maiden namex */
//                    jsonObject = oTrans.setMaster( "sPHBNIDxx",lsValue);
//                    jsonObject = oTrans.getModel().setPHNationalIDNumber(lsValue);
                    break;
            }                    
        } else
            txtField.selectAll();
    };

    /*OPEN WINDOW FOR */
    private void loadCompanyTransaction() {
        try {
            String sFormName = "Client Master Parameter";
            FXMLLoader fxmlLoader = new FXMLLoader();
            unloadForm unload = new unloadForm();
            ClientMasterTransactionCompanyController loControl = new ClientMasterTransactionCompanyController();
            loControl.setGRider(oApp);
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/cas/views/ClientMasterTransactionCompany.fxml"));
            fxmlLoader.setController(loControl);
            Parent parent = fxmlLoader.load();
//            loControl.setAddMode((String) oTrans.getMaster(3));
            AnchorPane otherAnchorPane = loControl.AnchorMain;

            // Get the parent of the TabContent node
            Node tabContent = AnchorMain.getParent();
            Parent tabContentParent = tabContent.getParent();

            // If the parent is a TabPane, you can work with it directly
            if (tabContentParent instanceof TabPane) {
                TabPane tabpane = (TabPane) tabContentParent;

                for (Tab tab : tabpane.getTabs()) {
                    if (tab.getText().equals(sFormName)) {
                        if (ShowMessageFX.OkayCancel(null, pxeModuleName, "You have unsaved data on Sales Job Order Information. Are you sure you want to create new sales job order record?") == true) {
                        } else {
                            return;
                        }

                        if (ShowMessageFX.OkayCancel(null, pxeModuleName, "You have opened Sales Job Order Information Form. Are you sure you want to create new sales job order record?") == true) {
                        } else {
                            return;
                        }
                        tabpane.getSelectionModel().select(tab);
                        unload.unloadForm(AnchorMain, oApp, sFormName);
                        loadCompanyTransaction();
                        return;
                    }
                }

                Tab newTab = new Tab(sFormName, parent);
                newTab.setStyle("-fx-font-weight: bold; -fx-pref-width: 180; -fx-font-size: 10.5px; -fx-font-family: arial;");
                newTab.setContextMenu(fdController.createContextMenu(tabpane, newTab, oApp));
                tabpane.getTabs().add(newTab);
                tabpane.getSelectionModel().select(newTab);
                newTab.setOnCloseRequest(event -> {
                    if (ShowMessageFX.YesNo(null, "Close Tab", "Are you sure, do you want to close tab?") == true) {
                        if (unload != null) {
                            unload.unloadForm(otherAnchorPane, oApp, sFormName);
                        } else {
//                            ShowMessageFX.Warning(getStage(), "Please notify the system administrator to configure the null value at the close button.", "Warning", pxeModuleName);
                        }
                    } else {
                        // Cancel the close request
                        event.consume();
                    }

                });

                List<String> tabName = new ArrayList<>();
                
                tabName.remove(sFormName);
                tabName.add(sFormName);
                // Save the list of tab IDs to the JSON file
                
            }

        } catch (IOException e) {
            e.printStackTrace();
//            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
            System.exit(1);

         }
    }
}