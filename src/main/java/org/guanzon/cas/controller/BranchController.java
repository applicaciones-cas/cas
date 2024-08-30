/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.cas.controller;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import static javafx.scene.input.KeyCode.DOWN;
import static javafx.scene.input.KeyCode.ENTER;
import static javafx.scene.input.KeyCode.F3;
import static javafx.scene.input.KeyCode.UP;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.constant.EditMode;
import static org.guanzon.cas.GriderGui.oApp;
import org.guanzon.cas.parameters.Branch;
import org.guanzon.cas.parameters.Company;
import org.guanzon.cas.parameters.Province;

import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author user
 */
public class BranchController implements Initializable, ScreenInterface{
    
    private final String pxeModuleName = "Branch";
    private GRider oApp;
    private Branch oTrans;
    private JSONObject poJSON;
    private int pnEditMode;

    private String psPrimary = "";

    private boolean state = false;
    private boolean pbLoaded = false;
    private int pnIndex;

    @FXML
    private AnchorPane ChildAnchorPane;
    @FXML
    private HBox hbButtons;
    @FXML
    private TableView tblList;

    @FXML
    private TextField txtField99, txtField01, txtField02, txtField03, txtField04, txtField05, txtField06, txtField07, txtField08;

    @FXML
    private Button btnBrowse, btnNew, btnSave, btnUpdate, btnCancel, btnActivate, btnClose;

    @FXML
    private CheckBox cbActive, cbWareHouse;

    @FXML
    private FontAwesomeIconView faActivate;

    @FXML
    private TableColumn index01, index02;
    
    private Map<TextField, Integer> textFieldLimits = new HashMap<>();


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        oTrans = new Branch(oApp, false);
        oTrans.setRecordStatus("10");
        pbLoaded = true;

        pnEditMode = EditMode.UNKNOWN;

        initButton(pnEditMode);
        initTextFields();

        pbLoaded = true;
        textFieldLimits.put(txtField01, 4); // txtField01 limit is 4 characters
        textFieldLimits.put(txtField02, 50);
        textFieldLimits.put(txtField03, 50); 
        textFieldLimits.put(txtField07, 50);
        
        
        
        textFieldLimits.forEach(this::setTextFieldFormatter);
    }    

    @FXML
    void cmdButton_Click(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();

        switch (lsButton) {

            case "btnNew":
                poJSON = oTrans.newRecord();
                loadRecord();
                pnEditMode = oTrans.getModel().getEditMode();
                if ("error".equals((String) poJSON.get("result"))) {
                    System.err.println((String) poJSON.get("message"));

                    pnEditMode = EditMode.UNKNOWN;
                    return;
                }
                break;

            case "btnSave":

                poJSON = oTrans.getModel().setModifiedBy(oApp.getUserID());
                if ("error".equals((String) poJSON.get("result"))) {
                    System.err.println((String) poJSON.get("message"));

                    pnEditMode = EditMode.UNKNOWN;
                    return;
                }
                poJSON = oTrans.getModel().setModifiedDate(oApp.getServerDate());
                if ("error".equals((String) poJSON.get("result"))) {
                    System.err.println((String) poJSON.get("message"));

                    pnEditMode = EditMode.UNKNOWN;
                    return;
                }
                poJSON = oTrans.getModel().setDescription(txtField03.getText());
                if ("error".equals((String) poJSON.get("result"))) {
                    System.err.println((String) poJSON.get("message"));

                    pnEditMode = EditMode.UNKNOWN;
                    return;
                }
                poJSON = oTrans.getModel().setBranchNm(txtField02.getText());
                if ("error".equals((String) poJSON.get("result"))) {
                    System.err.println((String) poJSON.get("message"));

                    pnEditMode = EditMode.UNKNOWN;
                    return;
                }
                poJSON = oTrans.getModel().setContact(txtField04.getText());
                if ("error".equals((String) poJSON.get("result"))) {
                    System.err.println((String) poJSON.get("message"));

                    pnEditMode = EditMode.UNKNOWN;
                    return;
                }
                poJSON = oTrans.getModel().setAddress(txtField07.getText());
                if ("error".equals((String) poJSON.get("result"))) {
                    System.err.println((String) poJSON.get("message"));

                    pnEditMode = EditMode.UNKNOWN;
                    return;
                }
                
                

                
                poJSON = oTrans.saveRecord();

                pnEditMode = oTrans.getModel().getEditMode();
                if ("error".equals((String) poJSON.get("result"))) {
                    System.err.println((String) poJSON.get("message"));

                    pnEditMode = EditMode.UNKNOWN;
                    return;

                } else {
                    oTrans = new Branch(oApp, true);
                    pbLoaded = true;
                    oTrans.setRecordStatus("10");
                    pnEditMode = EditMode.UNKNOWN;
                    clearFields();
                    ShowMessageFX.Information(null, pxeModuleName, "Record successful Saved!");
                }
                break;

            case "btnUpdate":
                poJSON = oTrans.updateRecord();

                pnEditMode = oTrans.getModel().getEditMode();
                if ("error".equals((String) poJSON.get("result"))) {
                    System.err.println((String) poJSON.get("message"));

                    pnEditMode = EditMode.UNKNOWN;
                    return;
                }
                break;

            case "btnCancel":
                if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Do you want to disregard changes?") == true) {
                    oTrans = new Branch(oApp, true);
                    oTrans.setRecordStatus("10");
                    pbLoaded = true;
                    pnEditMode = EditMode.UNKNOWN;
                    clearFields();
                    break;
                } else {
                    return;
                }

            case "btnActivate":
                if (!psPrimary.isEmpty()) {
                    if (btnActivate.getText().equals("Activate")) {
                        if (ShowMessageFX.YesNo(null, pxeModuleName, "Do you want to Activate this Parameter?") == true) {
                            poJSON = oTrans.activateRecord(psPrimary);
                            if ("error".equals((String) poJSON.get("result"))) {
                                System.err.println((String) poJSON.get("message"));
                                return;
                            } else {
                                clearFields();
                                pnEditMode = EditMode.UNKNOWN;
                                initButton(pnEditMode);
                                oTrans = new Branch(oApp, false);
                                oTrans.setRecordStatus("10");
                                pbLoaded = true;

                            }
                        } else {
                            return;
                        }
                    } else {
                        if (ShowMessageFX.YesNo(null, pxeModuleName, "Do you want to Deactivate this Parameter?") == true) {
                            poJSON = oTrans.deactivateRecord(psPrimary);
                            if ("error".equals((String) poJSON.get("result"))) {
                                System.err.println((String) poJSON.get("message"));
                                return;
                            } else {
                                clearFields();
                                pnEditMode = EditMode.UNKNOWN;
                                initButton(pnEditMode);
                                oTrans = new Branch(oApp, false);
                                oTrans.setRecordStatus("10");
                                pbLoaded = true;

                            }
                        } else {
                            return;
                        }
                    }
                } else {
                    ShowMessageFX.Warning(null, pxeModuleName, "Please select a record to confirm!");
                }
                break;

            case "btnClose":
                if (ShowMessageFX.OkayCancel(null, "Close Tab", "Are you sure you want to close this Tab?") == true) {
//                        if (unload != null) {
//                            unload.unloadForm(AnchorMain, oApp, "Size");
//                        } else {
//                            ShowMessageFX.Warning(getStage(), "Please notify the system administrator to configure the null value at the close button.", "Warning", pxeModuleName);
//                        }
//                        break;
                } else {
                    return;
                }

                break;

            case "btnBrowse":
                poJSON = oTrans.searchRecord(txtField99.getText(), false);
                pnEditMode = EditMode.READY;
                if ("error".equalsIgnoreCase(poJSON.get("result").toString())) {

                    ShowMessageFX.Information((String) poJSON.get("message"), "Computerized Acounting System", pxeModuleName);
                    txtField99.requestFocus();
                    pnEditMode = EditMode.UNKNOWN;
                    return;
                } else {
                    loadRecord();
                }
                break;

            default:
                ShowMessageFX.Warning(null, pxeModuleName, "Button with name " + lsButton + " not registered.");
                return;
        }

        initButton(pnEditMode);

    }

    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }
    
    private void initButton(int fnValue) {
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);

        btnSave.setVisible(lbShow);
        btnCancel.setVisible(lbShow);

        btnSave.setManaged(lbShow);
        btnCancel.setManaged(lbShow);

        btnNew.setManaged(!lbShow);
        btnUpdate.setManaged(!lbShow);
        btnBrowse.setManaged(!lbShow);

        btnBrowse.setVisible(!lbShow);
        btnNew.setVisible(!lbShow);
        btnUpdate.setVisible(!lbShow);
        btnActivate.setVisible(!lbShow);
        btnClose.setVisible(!lbShow);

        txtField99.setDisable(lbShow);
        txtField02.setEditable(lbShow);
        txtField03.setEditable(lbShow);
        txtField04.setEditable(lbShow);
        txtField05.setEditable(lbShow);
        txtField06.setEditable(lbShow);

        txtField02.requestFocus();
    }
    
    private void initTextFields() {
        /*textFields FOCUSED PROPERTY*/
        txtField01.focusedProperty().addListener(txtField_Focus);
        txtField02.focusedProperty().addListener(txtField_Focus);
        txtField03.focusedProperty().addListener(txtField_Focus);
        txtField99.focusedProperty().addListener(txtField_Focus);

        /*textFields KeyPressed PROPERTY*/
        txtField99.setOnKeyPressed(this::txtField_KeyPressed);
        txtField05.setOnKeyPressed(this::txtField_KeyPressed);
        txtField06.setOnKeyPressed(this::txtField_KeyPressed);

    }
    
    private void txtField_KeyPressed(KeyEvent event) {
        TextField textField = (TextField) event.getSource();
        int lnIndex = Integer.parseInt(((TextField) event.getSource()).getId().substring(8, 10));
        String lsValue = textField.getText();
        if (lsValue == null){
            lsValue = "";
        } 
        switch (event.getCode()) {
            case F3:
                switch (lnIndex) {

                    case 99:
                        /*Browse Primary*/
                        poJSON = oTrans.searchRecord(lsValue, false);
                        if ("error".equalsIgnoreCase(poJSON.get("result").toString())) {

                            ShowMessageFX.Information((String) poJSON.get("message"), "Computerized Acounting System", pxeModuleName);
                            txtField99.requestFocus();
                        } else {
                            loadRecord();
                        }
                        break;
                    case 5:
                        poJSON = oTrans.searchDetail("sCompnyID", lsValue, false);
                        if ("error".equalsIgnoreCase(poJSON.get("result").toString())) {

                            ShowMessageFX.Information((String) poJSON.get("message"), "Computerized Acounting System", pxeModuleName);
                            txtField01.requestFocus();
                        } else {
                            loadRecord();
                        }
                        break;
                    case 6:
                        poJSON = oTrans.searchDetail("sProvIDxx", lsValue, false);
                        if ("error".equalsIgnoreCase(poJSON.get("result").toString())) {

                            ShowMessageFX.Information((String) poJSON.get("message"), "Computerized Acounting System", pxeModuleName);
                            txtField01.requestFocus();
                        } else {
                            loadRecord();
                        }
                        break;
                }
            case ENTER:
                switch (lnIndex) {
                }
        }
        switch (event.getCode()) {
            case ENTER:
                CommonUtils.SetNextFocus(textField);
            case DOWN:
                CommonUtils.SetNextFocus(textField);
                break;
            case UP:
                CommonUtils.SetPreviousFocus(textField);
        }

        pnIndex = lnIndex;
    }
    
    final ChangeListener<? super Boolean> txtField_Focus = (o, ov, nv) -> {
        if (!pbLoaded) {
            return;
        }
        

        TextField txtField = (TextField) ((ReadOnlyBooleanPropertyBase) o).getBean();
        int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
        String lsValue = txtField.getText();

        if (lsValue == null) {
            return;
        }
        
        
        
        if (!nv) {
            /*Lost Focus*/
            switch (lnIndex) {
                case 1:
                    poJSON = oTrans.getModel().setBranchCd(lsValue);
                    if ("error".equals((String) poJSON.get("result"))) {
                        System.err.println((String) poJSON.get("message"));
                        return;
                    }
                    break;
                
                case 2:
                    poJSON = oTrans.getModel().setBranchNm(lsValue);
                    if ("error".equals((String) poJSON.get("result"))) {
                        System.err.println((String) poJSON.get("message"));
                        return;
                    }
                    break;
                    
                case 3:
                    poJSON = oTrans.getModel().setDescription(lsValue);
                    if ("error".equals((String) poJSON.get("result"))) {
                        System.err.println((String) poJSON.get("message"));
                        return;
                    }
                    break;
                case 5:
                    String lsCompnyID = (String) oTrans.getModel().getValue("sCompnyID");
                    if (lsCompnyID != null || !lsCompnyID.isEmpty()) {
                        poJSON = oTrans.getModel().setCompanyID(lsValue);
                        if ("error".equals((String) poJSON.get("result"))) {
                            System.err.println((String) poJSON.get("message"));
                            return;
                        }
                    }
                    break;
                case 6:
                    String lsTownIDxx = (String) oTrans.getModel().getValue("sTownIDxx");
                    if (lsTownIDxx != null || !lsTownIDxx.isEmpty()) {
                        poJSON = oTrans.getModel().setTownID(lsValue);
                        if ("error".equals((String) poJSON.get("result"))) {
                            System.err.println((String) poJSON.get("message"));
                            return;
                        }
                    }
                    break;

            }
        } else {
            txtField.selectAll();
        }
        pnIndex = lnIndex;
    };
    
    private void loadRecord() {
        boolean lbActive = oTrans.getModel().isActive();
//        psPrimary = (String) poJSON.get("value");
        psPrimary = oTrans.getModel().getBranchCd();
        txtField01.setText(psPrimary);
        txtField02.setText(oTrans.getModel().getBranchNm());
        txtField03.setText(oTrans.getModel().getDescription());
        
//        txtField04.setText(oTrans.getModel().getBranchNm());
        if (oTrans.getModel().getContact() == null){
            txtField04.setText(oTrans.getModel().getTeleNum());
        } else {
            txtField04.setText(oTrans.getModel().getContact());
        }
        
        
            Company loCompany;
            String lsCompnyID = (String) oTrans.getModel().getCompanyID();
            loCompany = oTrans.GetCompanyID(lsCompnyID, true);

            txtField05.setText((String) loCompany.getMaster("sCompnyNm"));
            
            Province loProvince;
            String lsTownIDxx = (String) oTrans.getModel().getValue("sTownIDxx");
            loProvince = oTrans.GetTownID(lsTownIDxx, true);

            txtField06.setText((String) loProvince.getMaster("sProvName"));
     
        
        
        
        
        txtField07.setText(oTrans.getModel().getAddress());
        
        
        
        cbActive.setSelected(oTrans.getModel().isActive());

        if (lbActive) {
            btnActivate.setText("Deactivate");
            faActivate.setGlyphName("CLOSE");
        } else {
            btnActivate.setText("Activate");
            faActivate.setGlyphName("CHECK");
        }

    }

    private void clearFields() {
        txtField01.clear();
        txtField02.clear();
        txtField03.clear();
        txtField04.clear();
        txtField05.clear();
        txtField06.clear();
        txtField07.clear();

        psPrimary = "";
        btnActivate.setText("Activate");
        cbActive.setSelected(false);

    }
    
    private void setTextFieldFormatter(TextField textField, int limit) {
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            return newText.length() > limit ? null : change; // Reject change if it exceeds the limit
        };
        TextFormatter<String> formatter = new TextFormatter<>(filter);
        textField.setTextFormatter(formatter);
    }
}