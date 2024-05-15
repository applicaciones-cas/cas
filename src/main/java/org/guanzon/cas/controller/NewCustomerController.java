/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.cas.controller;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import static javafx.scene.input.KeyCode.DOWN;
import static javafx.scene.input.KeyCode.ENTER;
import static javafx.scene.input.KeyCode.F3;
import static javafx.scene.input.KeyCode.UP;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.StringConverter;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.cas.clients.Client_Master;
import org.guanzon.cas.model.ModelMobile;
import org.guanzon.cas.validators.ValidatorFactory;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author User
 */
public class NewCustomerController  implements Initializable, ScreenInterface {
    
    private final String pxeModuleName = "Client Master Parameter";
    private GRider oApp;
    private Client_Master oTrans;
    private int pnEditMode;  
    
    private String oTransnox = "";
    private int pnMobile = 0;
    private boolean state = false;
    private boolean pbLoaded = false;
    @FXML
    private AnchorPane AnchorMain;
    @FXML
    private TextField txtField01;
    
    @FXML
    private Button btnOkay;

    @FXML
    private Button btnCancel;

    @FXML
    private TextField personalinfo02;

    @FXML
    private TextField personalinfo03;

    @FXML
    private TextField personalinfo04;

    @FXML
    private TextField personalinfo05;

    @FXML
    private ComboBox personalinfo09;

    @FXML
    private DatePicker personalinfo07;

    @FXML
    private TextField personalinfo08;

    @FXML
    private TextField txtMobile01;

    @FXML
    private TextArea AddressField02;
    
    @FXML
    private TextField AddressField04;
    
    @FXML
    private TextField AddressField03;

    @FXML
    private TextField personalinfo01;

    @FXML
    private TextField AddressField01;
    
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
    private ObservableList<ModelMobile> data = FXCollections.observableArrayList();

    private int pnAddress = 0;  
    /***********************************/
    /*Initializes the controller class.*/
    /***********************************/
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        if (oTransnox == null || oTransnox.isEmpty()) { // Check if oTransnox is null or empty
            pnEditMode = EditMode.ADDNEW;
//            initButton(pnEditMode);
        }
        
        oTrans = new Client_Master(oApp, true, oApp.getBranchCode());
        
        // Call newRecord to initialize a new record
        oTrans.newRecord();

        // Access sClientID directly from the jsonResult and set it to txtField01
        String sClientID = (String) oTrans.getMaster("sClientID");
        if (txtField01 != null) { // Check if txtField01 is not null before setting its text
            txtField01.setText(sClientID);
        } else {
            // Handle the case where txtField01 is null
            System.out.println("txtField01 is null");
        }
        ClickButton();
        InitPersonalInfo();
        initComboBoxes();
        oTrans.setType(ValidatorFactory.ClientTypes.STANDARD);
        pbLoaded = true;
    }
    
    /***********************/
    /*initialize the button*/
    /***********************/
        private void ClickButton() {
        btnCancel.setOnAction(this::handleButtonAction);
        btnOkay.setOnAction(this::handleButtonAction);
    }
    
    /****************************/
    /*Click Button handle action*/
    /****************************/
        private void handleButtonAction(ActionEvent event) {
        Object source = event.getSource();
        if (source instanceof Button) {
            Button clickedButton = (Button) source;
            
            unloadForm appUnload = new unloadForm();
            switch (clickedButton.getId()) {
                
                /*button cancel*/
                case "btnCancel":
                    if (ShowMessageFX.YesNo("Do you really want to cancel this record? \nAny data collected will not be kept.", "Computerized Acounting System", pxeModuleName)) {
//                            clearAllFields();
                        pnEditMode = EditMode.UNKNOWN;
                        appUnload.unloadForm(AnchorMain, oApp, "Client Transactions Standard");
                    }
                    break;               
                /*button okay*/    
                case "btnOkay":
                        oTrans.setMaster( 8,String.valueOf(personalinfo01.getText()));
                        if(personalinfo07.getValue() == null){
                            
                            oTrans.setMaster("dBirthDte", "1990-01-01");
                        }
                        JSONObject saveResult = oTrans.saveRecord();
                        if ("success".equals((String) saveResult.get("result"))){
                            System.err.println((String) saveResult.get("message"));
                            ShowMessageFX.Information((String) saveResult.get("message"), "Computerized Acounting System", pxeModuleName);
                            System.out.println("Record saved successfully.");
                            appUnload.unloadForm(AnchorMain, oApp, "Client Transactions Standard");
                        } else {
                            ShowMessageFX.Information((String)saveResult.get("message"), "Computerized Acounting System", pxeModuleName);
                            System.out.println("Record not saved successfully.");
                            System.out.println((String) saveResult.get("message"));
                        }
                     break;
            }
        }
    }
        
    /************************/
    /*initialize text fields*/
    /************************/
    private void InitPersonalInfo(){
        /*PERSONAL INFO FOCUSED PROPERTY*/
        personalinfo01.focusedProperty().addListener(personalinfo_Focus);
        personalinfo02.focusedProperty().addListener(personalinfo_Focus);
        personalinfo03.focusedProperty().addListener(personalinfo_Focus);
        personalinfo04.focusedProperty().addListener(personalinfo_Focus);
        personalinfo05.focusedProperty().addListener(personalinfo_Focus);
        personalinfo08.focusedProperty().addListener(personalinfo_Focus);
//        personalinfo09.focusedProperty().addListener(personalinfo_Focus);
        
        /*MOBILE INFO FOCUSED PROPERTY*/
        txtMobile01.focusedProperty().addListener(mobileinfo_Focus);
        
        /*ADDRESS INFO FOCUSED PROPERTY*/
        AddressField01.focusedProperty().addListener(address_Focus);
        AddressField02.focusedProperty().addListener(address_Focus);
        AddressField03.focusedProperty().addListener(address_Focus);
        
        /*PERSONAL INFO KEYPRESSED*/
        personalinfo08.setOnKeyPressed(this::personalinfo_KeyPressed);
        
        /*ADDRESS INFO KEYPRESSED*/
        AddressField03.setOnKeyPressed(this::address_KeyPressed);
        
         // Set a custom StringConverter to format date
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        personalinfo07.setConverter(new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    oTrans.setMaster("dBirthDte", dateFormatter.format(date).toString());
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    oTrans.setMaster("dBirthDte",LocalDate.parse(string, dateFormatter).toString());
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        });
    }
    
    /**************************************/
    /*initialize value to data            */
    /*serves also as lost focus FOR MOBILE*/
    /**************************************/
    final ChangeListener<? super Boolean> mobileinfo_Focus = (o,ov,nv)->{ 
        if (!pbLoaded) return;
       
        TextField mobileinfo = (TextField)((ReadOnlyBooleanPropertyBase)o).getBean();
        int lnIndex = Integer.parseInt(mobileinfo.getId().substring(9, 11));
        String lsValue = mobileinfo.getText();
        JSONObject jsonObject = new JSONObject();
        if (lsValue == null) return;         
        if(!nv){ /*Lost Focus*/
            switch (lnIndex){
                case 1: /*company name*/
                    
                    System.out.println(pnMobile);
                    oTrans.setMobile(pnMobile, "sMobileNo", lsValue);
                    break;
            }
        } else
            mobileinfo.selectAll();
    };
    

    /*********************************************/
    /*initialize value to data                   */
    /*serves also as lost focus FOR PERSONAL INFO*/
    /*********************************************/
    final ChangeListener<? super Boolean> personalinfo_Focus = (o,ov,nv)->{ 
        if (!pbLoaded) return;
       
        TextField personalinfo = (TextField)((ReadOnlyBooleanPropertyBase)o).getBean();
        int lnIndex = Integer.parseInt(personalinfo.getId().substring(12, 14));
        String lsValue = personalinfo.getText();
        JSONObject jsonObject = new JSONObject();
        if (lsValue == null) return;         
        if(!nv){ /*Lost Focus*/
            switch (lnIndex){
                case 1: /*company name*/
                    jsonObject = oTrans.setMaster( 8,lsValue);
                    System.out.println(String.valueOf("company name = " +lsValue));
                    break;
                case 2:/*last name*/
                    jsonObject = oTrans.setMaster( 3,lsValue);
                    System.out.println(String.valueOf("last name = " +lsValue));
                    break;
                case 3:/*frist name*/
                    jsonObject = oTrans.setMaster( 4,lsValue);
                    System.out.println(String.valueOf("first name = " +lsValue));
                    break;
                case 4:/*middle name*/
                    jsonObject = oTrans.setMaster( 5,lsValue);                    
                    System.out.println(String.valueOf("middle name = " +lsValue));
                    break;
                case 5:/*suffix name*/
                    jsonObject = oTrans.setMaster( 6,lsValue);
                    System.out.println(String.valueOf("suffix name = " +lsValue));
                    break;
                case 6:/*citizenship */
                    jsonObject = oTrans.setMaster( 11,lsValue);
                    System.out.println(String.valueOf("citizenship = " + lsValue));
                    break;
                case 7:/*birthday */
                    // Define the format of the input string
//                    jsonObject = oTrans.setMaster( "dBirthDte", dateFormatter(lsValue));
                    break;
//                case 8:/*birth place */
//                    jsonObject = oTrans.setMaster( 13,lsValue);
//                    
//                    System.out.println(String.valueOf("birth place = " + lsValue));
//                    break;
            }
            personalinfo01.setText(personalinfo02.getText() + "," + personalinfo03.getText() + " " + personalinfo05.getText() + " " + personalinfo04.getText());
            
        } else
            personalinfo.selectAll();
        
//            pnIndex = lnIndex;
    };
    
    /********************************************/
    /*initialize value to data                  */
    /*serves also as lost focus FOR ADDRESS INFO*/
    /********************************************/
    final ChangeListener<? super Boolean> address_Focus = (o,ov,nv)->{ 
        if (!pbLoaded) return;
       
        TextField AddressField = (TextField)((ReadOnlyBooleanPropertyBase)o).getBean();
        int lnIndex = Integer.parseInt(AddressField.getId().substring(12, 14));
        String lsValue = AddressField.getText();
        JSONObject jsonObject = new JSONObject();
        if (lsValue == null) return;         
        if(!nv){ /*Lost Focus*/
            switch (lnIndex){   
                case 1:/*house no*/
                    oTrans.setAddress(0, 3, lsValue);
                        break;
                case 2:/*cutomer addresss*/
                    oTrans.setAddress(0, 4, lsValue);
                    break;
            }
            AddressField04.setText(AddressField01.getText() + " " +  AddressField02.getText() + ", " + AddressField03.getText());
        }
    };
    
    /*********************/
    /*initialize combobox*/
    /*********************/
    private void initComboBoxes(){
    // Create a list of genders
        ObservableList<String> genders = FXCollections.observableArrayList(
            "Male",
            "Female",
            "Other"
        );

        // Set the items of the ComboBox to the list of genders
        personalinfo09.setItems(genders);
        personalinfo09.getSelectionModel().getSelectedIndex();
        personalinfo09.setOnAction(event ->{
            oTrans.setMaster(9, personalinfo09.getSelectionModel().getSelectedIndex());
        });
    }
    
    
    /**************************/
    /*initialize value to data*/
    /*PERSONAL INFO KEYPRESS  */
    /**************************/
    private void personalinfo_KeyPressed(KeyEvent event){
        TextField personalinfo = (TextField)event.getSource();
        int lnIndex = Integer.parseInt(((TextField)event.getSource()).getId().substring(12,14));
        String lsValue = personalinfo.getText();
        JSONObject poJson;
        switch (event.getCode()) {
            case F3:
                switch (lnIndex){
                    
                    case 8: /*search birthplace*/
                        poJson = new JSONObject();
                           poJson =  oTrans.searchBirthPlce(lsValue, false);
                           System.out.println("poJson = " + poJson.toJSONString());
                           if("error".equalsIgnoreCase(poJson.get("result").toString())){
                               
                                ShowMessageFX.Information((String) poJson.get("message"), "Computerized Acounting System", pxeModuleName);
                                personalinfo08.clear();
                           }
                           personalinfo08.setText((String) poJson.get("xBrthPlce"));
                           
                        break;
                }
            case ENTER:
                switch (lnIndex){
                }
        }
        switch (event.getCode()){
        case ENTER:
            CommonUtils.SetNextFocus(personalinfo);
        case DOWN:
            CommonUtils.SetNextFocus(personalinfo);
            break;
        case UP:
            CommonUtils.SetPreviousFocus(personalinfo);
        }
    }
    
    /**************************/
    /*initialize value to data*/
    /*ADDRESS INFO KEYPRESS   */
    /**************************/
    private void address_KeyPressed(KeyEvent event){
        TextField AddressField = (TextField)event.getSource();
        int lnIndex = Integer.parseInt(((TextField)event.getSource()).getId().substring(12,14));
        String lsValue = AddressField.getText();
        JSONObject poJson;
        switch (event.getCode()) {
            case F3:
                switch (lnIndex){
                    
                    case 3: /*search town*/
                        poJson = new JSONObject();
                           poJson = oTrans.SearchTownAddress(pnAddress, lsValue, false);
                           System.out.println("poJson = " + poJson.toJSONString());
                           if("error".equalsIgnoreCase(poJson.get("result").toString())){
                               
                                ShowMessageFX.Information((String) poJson.get("message"), "Computerized Acounting System", pxeModuleName);
                                AddressField03.clear();
                           }
                           AddressField03.setText((String) poJson.get("sTownName") + ", " + (String) poJson.get("sProvName"));
                        break;
                }
            case ENTER:
                switch (lnIndex){
                }
        }
        switch (event.getCode()){
        case ENTER:
            CommonUtils.SetNextFocus(AddressField);
        case DOWN:
            CommonUtils.SetNextFocus(AddressField);
            break;
        case UP:
            CommonUtils.SetPreviousFocus(AddressField);
        }
    }

    private void unloadform() {
       
    }

    
    
}    

