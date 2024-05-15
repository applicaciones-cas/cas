/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.cas.controller;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.cas.clients.account.Account_Accreditation;
import org.guanzon.cas.model.ModelAccredetations;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author User
 */
public class FrmAccountsAccreditationController implements Initializable,ScreenInterface {
    private final String pxeModuleName = "Accounts Accreditation";
    private GRider oApp;
    private  Account_Accreditation oTrans;
    private String oTransnox = "";
    private int pnEditMode;  
    private String a = "";
    private String b = "";
    private String c = "";
    private boolean state = false;
    private boolean pbLoaded = false;
    private int pnCompany = 0; 
    ObservableList<String> AccountType = FXCollections.observableArrayList(
            "Accounts Payable",
            "Accounts Receivable"
        );
    ObservableList<String> AccountStatus = FXCollections.observableArrayList(
            "Open",
            "Approved",
            "Disapproved",
            "Blocklisted"
        );
    
    private ObservableList<ModelAccredetations> data = FXCollections.observableArrayList();
    
    @FXML
    private AnchorPane AnchorMain,AnchroInputs;

    @FXML
    private HBox hbButtons;

    @FXML
    private Button btnBrowse;

    @FXML
    private Button btnNew;

    @FXML
    private Button btnApproved;

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
    private ComboBox cmbField01;

    @FXML
    private DatePicker cpField01;

    @FXML
    private TextField txtSeek01;

    @FXML
    private ComboBox cmbField02;
    
    
    @FXML
    private ComboBox cmbField03;

    @FXML
    private TextField txtField02;

    @FXML
    private DatePicker cpField02;

    @FXML
    private TextField txtField03;

    @FXML
    private TextField txtField04;

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnDelete;

    @FXML
    private TextField txtField01;

    @FXML
    private TableView tblAccreditation;

    @FXML
    private TableColumn indexCompany01;

    @FXML
    private TableColumn indexCompany02;

    @FXML
    private TableColumn indexCompany03;

    @FXML
    private TableColumn indexCompany04;

    @FXML
    private TableColumn indexCompany05;

    @FXML
    private TableColumn indexCompany06;

    @FXML
    void tblAccreditation_Clicked(MouseEvent event) {
        pnCompany = tblAccreditation.getSelectionModel().getSelectedIndex();
        getAccreditationSelectedItems();

    }
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
        oTrans = new Account_Accreditation(oApp, true);
        if (oTransnox == null || oTransnox.isEmpty()) { // Check if oTransnox is null or empty
            pnEditMode = EditMode.UNKNOWN;
        initButton(pnEditMode);
        }
        pnEditMode = EditMode.UNKNOWN;        
        initButton(pnEditMode);
        ClickButton();
        initTabAnchor();
        initComboBoxes();
        InitTextFields();
        initMobileGrid();
        pbLoaded = true;
    }    

    private void ClickButton() {
        btnCancel.setOnAction(this::handleButtonAction);
        btnNew.setOnAction(this::handleButtonAction);
        btnSave.setOnAction(this::handleButtonAction);
        btnUpdate.setOnAction(this::handleButtonAction);
        btnClose.setOnAction(this::handleButtonAction);
        btnAdd.setOnAction(this::handleButtonAction);
        btnDelete.setOnAction(this::handleButtonAction);
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
                    poJSON = oTrans.newTransaction();
                    if ("success".equals((String) poJSON.get("result"))){
                            pnEditMode = EditMode.ADDNEW;
                            initButton(pnEditMode);
                            txtField01.setText((String) oTrans.getTransNox());
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
                            System.out.println("dTransact = " + oTrans.getAccount(pnCompany, "dTransact"));
                            String dateStr = oTrans.getAccount(pnCompany, "dTransact").toString();
                            if (!dateStr.isEmpty()) {
                                // Parse the date string
                                LocalDateTime dateTime = LocalDateTime.parse(dateStr, formatter);
                                LocalDate localbdate = dateTime.toLocalDate();

                                // Set the value of the DatePicker to the parsed LocalDate
                                cpField02.setValue(localbdate);
                            } else {
                                // Handle empty or null date string
                                // For example, set the DatePicker value to null or display an error message
                                cpField02.setValue(null); // Set to null or handle appropriately
                            }
                            System.out.println("ADDNEW = " + EditMode.ADDNEW);
                            System.out.println("EDITMODE = " + pnEditMode);
                            loadComapany();
                            initTabAnchor();
                        }else{
                            ShowMessageFX.Information((String)poJSON.get("message"), "Computerized Acounting System", pxeModuleName);
                            System.out.println("Record not saved successfully.");
                            System.out.println((String) poJSON.get("message"));
                            
                            initTabAnchor();
                        }
                        
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
                    
                        JSONObject saveResult = oTrans.saveTransaction();
                        if ("success".equals((String) saveResult.get("result"))){
                            System.err.println((String) saveResult.get("message"));
                            ShowMessageFX.Information((String) saveResult.get("message"), "Computerized Acounting System", pxeModuleName);
//                            clearAllFields();
                            pnEditMode = EditMode.UNKNOWN;
                            initButton(pnEditMode);
                            initTabAnchor();
                            clearCompanyFields();
                            System.out.println("Record saved successfully.");
                        } else {
                            ShowMessageFX.Information((String)saveResult.get("message"), "Computerized Acounting System", pxeModuleName);
                            System.out.println("Record not saved successfully.");
                            System.out.println((String) saveResult.get("message"));
                        }
                        
                     break;

                case "btnAdd":
                    JSONObject addDetails = oTrans.addDetail();
                         System.out.println((String) addDetails.get("message"));
                        if ("error".equals((String) addDetails.get("result"))){
                            ShowMessageFX.Information((String) addDetails.get("message"), "Computerized Acounting System", pxeModuleName);
                            break;
                        } 
//                        clearMobileFields();
                        loadComapany();
                        pnCompany = oTrans.getAccount().size()-1;
                        tblAccreditation.getSelectionModel().select(pnCompany + 1);
                        clearCompanyFields();
                break;
                case "btnDelete":
                    if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Do you want to remove ?") == true){  
                     
                        oTrans.getAccount().remove(pnCompany);
                        if(oTrans.getAccount().size() <= 0){
                            oTrans.addDetail();
                            
                        }
                        
                        pnCompany = oTrans.getAccount().size()-1;
                        loadComapany();
                        clearCompanyFields();
                        txtField02.requestFocus();
                    }
                    
                break;
        }
    }
}
    private void loadComapany(){
        data.clear();
        if(oTrans.getAccount()!= null){
            for (int lnCtr = 0; lnCtr < oTrans.getAccount().size(); lnCtr++){
                data.add(new ModelAccredetations(String.valueOf(lnCtr + 1),
                    oTrans.getAccount(lnCtr, "sClientID").toString(),
                    oTrans.getAccount(lnCtr, "sCompnyNm").toString(),
                    oTrans.getAccount(lnCtr, "sCPerson1").toString(),
                    oTrans.getAccount(lnCtr, "sRemarksx").toString(),
                    oTrans.getAccount(lnCtr, "cTranType").toString(),
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    ""));
//                System.out.println("index = " + oTrans.getAccount(lnCtr, "cOwnerxxx").toString());
//                System.out.println("value = " + AccountType.get(Integer.parseInt(oTrans.getAccount(lnCtr, "cOwnerxxx").toString())));
            }
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
        

//        txtSearch01.setDisable(!lbShow);
//        txtSearch02.setDisable(!lbShow);
        
        if (lbShow){
//            txtSearch01.setDisable(lbShow);
//            txtSearch01.clear();
//            txtSearch02.setDisable(lbShow);
            
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
//            txtSearch01.setDisable(lbShow);
//            txtSearch01.requestFocus();
//            txtSearch02.setDisable(lbShow);  
        }
        
    }
    private void initTabAnchor(){
        boolean pbValue = pnEditMode == EditMode.ADDNEW || 
                pnEditMode == EditMode.UPDATE;
        AnchroInputs.setDisable(!pbValue);
//        anchorAddress.setDisable(!pbValue);
//        anchorMobile.setDisable(!pbValue);
//        anchorEmail.setDisable(!pbValue);
//        anchorSocial.setDisable(!pbValue);
//        anchorContctPerson.setDisable(!pbValue);
//        anchorOtherInfo.setDisable(!pbValue);
    
    }
    private void initComboBoxes(){
        
        // Set the items of the ComboBox to the list of genders
        cmbField02.setItems(AccountType);
        cmbField02.getSelectionModel().select(0);
        
        
        cmbField02.setOnAction(event -> {
            oTrans.setAccount(pnCompany,3, String.valueOf(cmbField02.getSelectionModel().getSelectedIndex()));
            System.out.print("\ncAcctType = " + cmbField02.getSelectionModel().getSelectedIndex());
        });
        
        
        cmbField03.setItems(AccountStatus);
        cmbField03.getSelectionModel().select(0);
        
        
        cmbField03.setOnAction(event -> {
            oTrans.setAccount(pnCompany,9, cmbField03.getSelectionModel().getSelectedIndex());
             System.out.print("\ncTranStat = " + cmbField03.getSelectionModel().getSelectedIndex());
        });
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
//                        receivedDataLabel.setText(receivedData);
                        poJson = new JSONObject();
                        String input = "";
                        input = String.valueOf(cmbField02.getSelectionModel().getSelectedIndex());
//                            poJson = oTrans.setMaster("cAcctType", input);
                            poJson =  oTrans.SearchClient(pnCompany,lsValue, false);
                           System.out.println("poJson = " + poJson.toJSONString());
                           if("error".equalsIgnoreCase(poJson.get("result").toString())){
//                               loadCompanyTransaction();
                               ShowMessageFX.Information((String) poJson.get("message"), "Computerized Acounting System", pxeModuleName);                              
                           }
//                           txtField01.setText((String) poJson.get("sClientID"));                    
                           txtField02.setText((String) poJson.get("sCompnyNm"));                           
                           txtField03.setText((String) poJson.get("sCPerson1"));                                                     
                           txtField03.setText((String) poJson.get("sCPerson1"));
               
                           a = (String) poJson.get("sClientID");
                           b = (String) poJson.get("sContctID");
//                           oTransnox = (String) poJson.get("sClientID");
//                           txtField04.setText((String) poJson.get("sCPerson1"));                         
//                           txtField05.setText((String) poJson.get("sMobileNo"));
//                           txtField06.setText((String) poJson.get("sTaxIDNox"));
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
    
    private void InitTextFields(){
        
        txtField01.focusedProperty().addListener(txtField_Focus);
        txtField02.focusedProperty().addListener(txtField_Focus);
        txtField03.focusedProperty().addListener(txtField_Focus);
        txtField04.focusedProperty().addListener(txtField_Focus);;
        
        txtField02.setOnKeyPressed(this::txtField_KeyPressed);
        
        // Set a custom StringConverter to format date
          DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        cpField02.setConverter(new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    
                    oTrans.setAccount(pnCompany,"dTransact", dateFormatter.format(date).toString());
                    oTrans.setAccount(pnCompany,"dApproved", "0000-00-00");
                    System.out.println("dTransact = " + date);
                    
                    cpField02.setValue(LocalDate.parse(date.format(dateFormatter), dateFormatter));
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    oTrans.setAccount(pnCompany,2,LocalDate.parse(string, dateFormatter).toString());
                    System.out.println("dTransact1 = " + LocalDate.parse(string, dateFormatter).toString());
//                    txtField07.setValue(LocalDate.parse(string, dateFormatter));
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        });
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
                   oTrans.setAccount(pnCompany, 1, lsValue);
                   System.out.print( oTrans.getAccount(1, lsValue));
                    break;
                case 2:/*company name*/
//                    String x = "";
//                    x = (String)jsonObject.get("sClientID");
                    oTrans.setAccount( pnCompany,4, a);
//                   System.out.print( oTrans.getAccount(4, String.valueOf(jsonObject.get("sClientID"))));
                    break;
                case 3:/*Contact person*/   
//                      String y = "";
//                      y = jsonObject.get("sContctID").toString();
                    oTrans.setAccount(pnCompany, 5, b);
//                    System.out.print( oTrans.getAccount(5, (String)jsonObject.get("sContctID")));
                    break;
                case 4:/*Contact person*/
                    oTrans.setAccount(pnCompany, "sRemarksx",lsValue);
//                   System.out.print( oTrans.getAccount(6, lsValue));
                    break;
            }        

                            loadComapany();            
        } else
            txtField.selectAll();
    };
    
    private void initMobileGrid() {
        indexCompany01.setStyle("-fx-alignment: CENTER;");
        indexCompany02.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        indexCompany03.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        indexCompany04.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        indexCompany05.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        indexCompany06.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
//        
        indexCompany01.setCellValueFactory(new PropertyValueFactory<>("index01"));
        indexCompany02.setCellValueFactory(new PropertyValueFactory<>("index02"));
        indexCompany03.setCellValueFactory(new PropertyValueFactory<>("index03"));
        indexCompany04.setCellValueFactory(new PropertyValueFactory<>("index04"));
        indexCompany05.setCellValueFactory(new PropertyValueFactory<>("index05"));
        indexCompany06.setCellValueFactory(new PropertyValueFactory<>("index06"));
        
        tblAccreditation.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblAccreditation.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });
        tblAccreditation.setItems(data);
//        tblMobile.getSelectionModel().select(pnMobile + 1);
        tblAccreditation.autosize();
    }
    
    
        private void clearCompanyFields(){
        TextField[] fields = { txtField02, txtField03, txtField04 };

        // Loop through each array of TextFields and clear them
        for (TextField field : fields) {
            field.clear();
        }
       cmbField02.setItems(AccountType);
       cmbField02.getSelectionModel().select(0);
       
       cmbField03.setItems(AccountStatus);
       cmbField03.getSelectionModel().select(-1);
    }
        private void getAccreditationSelectedItems() {
            txtField02.setText(oTrans.getAccount(pnCompany, "sCompnyNm") == null || oTrans.getAccount(pnCompany, "sCompnyNm").toString().isEmpty() ? "" : (String) oTrans.getAccount(pnCompany, "sCompnyNm"));
            txtField03.setText(oTrans.getAccount(pnCompany, "sCPerson1") == null || oTrans.getAccount(pnCompany, "sCPerson1").toString().isEmpty() ? "" : (String) oTrans.getAccount(pnCompany, "sCPerson1"));
            txtField04.setText(oTrans.getAccount(pnCompany, "sRemarksx") == null || oTrans.getAccount(pnCompany, "sRemarksx").toString().isEmpty() ? "" : (String) oTrans.getAccount(pnCompany, "sRemarksx"));
            txtField02.requestFocus();
    }

   
}

