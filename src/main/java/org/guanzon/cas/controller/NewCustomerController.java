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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.clients.Client_Master;
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
//    private JSONObject poJSON;
    private int pnEditMode;  
    
    private String oTransnox = "";
    
    private boolean state = false;
    private boolean pbLoaded = false;
    
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
        // TODO
    /**
     * Initializes the controller class.
     */
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
            switch (clickedButton.getId()) {
                
                /*button cancel*/
                case "btnCancel":
//                    pnEditMode = EditMode.UNKNOWN;
////                    initButton(pnEditMode);
                    
                    if (ShowMessageFX.YesNo("Do you want to save transaction?", "Computerized Acounting System", pxeModuleName)){
                        pnEditMode = EditMode.UNKNOWN;
//                        initButton(pnEditMode);
                    }
                    break;                    
                /*button okay*/    
                case "btnOkay":
                        oTrans.setMaster( 8,String.valueOf(personalinfo01.getText()));
                        JSONObject saveResult = oTrans.saveRecord();
                        if ("success".equals((String) saveResult.get("result"))){
                            System.err.println((String) saveResult.get("message"));
                            ShowMessageFX.Information((String) saveResult.get("message"), "Computerized Acounting System", pxeModuleName);
                            System.out.println("Record saved successfully.");
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
        personalinfo09.focusedProperty().addListener(personalinfo_Focus);
        
        
        
        /*PERSONAL INFO KEYPRESSED*/
//        personalinfo07.setOnKeyPressed(this::personalinfo_KeyPressed);
//        personalinfo06.setOnKeyPressed(this::personalinfo_KeyPressed);
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
    
    
    /***************************/
    /*initialize value to data */
    /*serves also as lost focus*/
    /***************************/
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
                    break;
                case 2:/*last name*/
                    personalinfo01.setText("");
                    personalinfo01.setText(personalinfo02.getText() + "," + personalinfo03.getText() + " " + (personalinfo05.getText() + " ") + personalinfo04.getText());
                    System.out.println(String.valueOf(lsValue));
                    jsonObject = oTrans.setMaster( 3,lsValue);
                    break;
                case 3:/*frist name*/
                    personalinfo01.setText("");
                    personalinfo01.setText(personalinfo02.getText() + "," + personalinfo03.getText() + " " + personalinfo05.getText() + " " + personalinfo04.getText());
                    jsonObject = oTrans.setMaster( 4,lsValue);
                    break;
                case 4:/*middle name*/
                    personalinfo01.setText("");
                    personalinfo01.setText(personalinfo02.getText() + "," + personalinfo03.getText() + " " + personalinfo05.getText() + " " + personalinfo04.getText());
                    jsonObject = oTrans.setMaster( 5,lsValue);
                    break;
                case 5:/*suffix name*/
                    jsonObject = oTrans.setMaster( 6,lsValue);
                    break;
                case 6:/*citizenship */
                    jsonObject = oTrans.setMaster( 7,lsValue);
                    break;
                case 7:/*birthday */
                    // Define the format of the input string
//                    jsonObject = oTrans.setMaster( "dBirthDte", dateFormatter(lsValue));
                    break;
                case 8:/*birth place */
                    jsonObject = oTrans.setMaster( 9,lsValue);
                    break;
            }
        } else
            personalinfo.selectAll();
        
//            pnIndex = lnIndex;
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
    // Create a list of civilStatuses    
    }
}    

