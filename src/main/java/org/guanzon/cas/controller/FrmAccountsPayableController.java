/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.cas.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.constant.EditMode;
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
    private TextField txtSeeks99;

    @FXML
    private TextField txtField011;

    @FXML
    private TextField txtField01;

    @FXML
    private TextField txtField03;

    @FXML
    private TextField txtField05;

    @FXML
    private TextField txtField06;

    @FXML
    private TextField txtField02;

    @FXML
    private TextField txtField07;

    @FXML
    private TextField txtField08;

    @FXML
    private TextField txtField09;

    @FXML
    private TextField txtField10;

    @FXML
    private TextField txtField11;

    @FXML
    private TextField txtField04;

    @FXML
    private CheckBox chkfield01;

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
    private TableColumn indexAddress051;

    @FXML
    void tblLedger_Clicked(MouseEvent event) {

    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        ClickButton();
        initTabAnchor();
    }    

    @Override
    public void setGRider(GRider foValue) {
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    private void ClickButton() {
        btnCancel.setOnAction(this::handleButtonAction);
        btnNew.setOnAction(this::handleButtonAction);
        btnSave.setOnAction(this::handleButtonAction);
        btnUpdate.setOnAction(this::handleButtonAction);
        btnClose.setOnAction(this::handleButtonAction);
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
}