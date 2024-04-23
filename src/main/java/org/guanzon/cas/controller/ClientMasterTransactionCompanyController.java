/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.cas.controller;

import com.rmj.guanzongroup.cas.maven.model.ModelInstitutionalContactPerson;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import static javafx.scene.input.KeyCode.DOWN;
import static javafx.scene.input.KeyCode.ENTER;
import static javafx.scene.input.KeyCode.F3;
import static javafx.scene.input.KeyCode.UP;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.cas.model.ModelAddress;
import org.guanzon.cas.model.ModelEmail;
import org.guanzon.cas.model.ModelMobile;
import org.guanzon.cas.model.ModelSocialMedia;
import org.guanzon.clients.Client_Master;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author User
 */
public class ClientMasterTransactionCompanyController implements Initializable, ScreenInterface {
    private final String pxeModuleName = "Client Master Parameter";
    private GRider oApp;
    private Client_Master oTrans;
//    private JSONObject poJSON;
    private int pnEditMode;  
    private ObservableList<ModelInstitutionalContactPerson> contact_data = FXCollections.observableArrayList();
    private int pnContact = 0;
    private String oTransnox = "";
    
    private boolean state = false;
    private boolean pbLoaded = false;
    
    @FXML
    private Button btnSave;
    @FXML
    private TextField txtField01;
    @FXML
    private TextField cmpnyInfo02;

    @FXML
    private TextField cmpnyInfo03;

    @FXML
    private TextField cmpnyInfo05;

    @FXML
    private TextField cmpnyInfo06;

    @FXML
    private TextField cmpnyInfo04;

    @FXML
    private TextField cmpnyInfo01;

    @FXML
    private TableView tblContact;

    @FXML
    private TableColumn indexContact01;

    @FXML
    private TableColumn indexContact02;

    @FXML
    private TableColumn indexContact03;

    @FXML
    private TableColumn indexContact04;

    @FXML
    private TableColumn indexContact05;

    @FXML
    private TableColumn indexContact06;

    @FXML
    private TableColumn indexContact07;

    @FXML
    private TextField txtContact01;

    @FXML
    private TextField txtContact02;

    @FXML
    private TextField txtContact03;

    @FXML
    private TextField txtContact06;

    @FXML
    private TextArea txtContact10;

    @FXML
    private Button btnAddInsContact;

    @FXML
    private CheckBox cbContact01;

    @FXML
    private CheckBox cbContact02;

    @FXML
    private TextField txtContact07;

    @FXML
    private TextField txtContact08;

    @FXML
    private TextField txtContact04;

    @FXML
    private TextField txtContact05;

    @FXML
    private TextField txtContact09;

    @FXML
    private TextField cmpnyInfo07;

    @FXML
    private TextField cmpnyInfo08;

    @FXML
    private TextField cmpnyInfo09;

    
    private ObservableList<ModelMobile> data = FXCollections.observableArrayList();
    private ObservableList<ModelEmail> email_data = FXCollections.observableArrayList();
    private ObservableList<ModelSocialMedia> social_data = FXCollections.observableArrayList();    
    private ObservableList<ModelAddress> address_data = FXCollections.observableArrayList();    
    
    ObservableList<String> mobileType = FXCollections.observableArrayList("Mobile No", "Tel No", "Fax No");
    ObservableList<String> mobileOwn = FXCollections.observableArrayList("Personal", "Office", "Others");
    ObservableList<String> EmailOwn = FXCollections.observableArrayList("Personal", "Office", "Others");
    ObservableList<String> socialTyp = FXCollections.observableArrayList("Facebook", "Instagram", "Twitter");
    private int pnMobile = 0;
    private int pnEmail = 0;
    private int pnSocMed = 0;
    private int pnAddress = 0;  
    /**
     * Initializes the controller class.
     */
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
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        if (oTransnox == null || oTransnox.isEmpty()) { // Check if oTransnox is null or empty
            pnEditMode = EditMode.ADDNEW;
            initButton(pnEditMode);
        }
        ClickButton();

        // Initialize the Client_Master transaction
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

        initTextFields();
        InitContctPersonInfo();
        loadContctPerson();
        initContctPersonGrid();
        
        pbLoaded = true;
    
}
    
    private void InitContctPersonInfo(){
        /*MOBILE INFO FOCUSED PROPERTY*/
        txtContact01.focusedProperty().addListener(contactinfo_Focus);
        txtContact02.focusedProperty().addListener(contactinfo_Focus);
        txtContact03.focusedProperty().addListener(contactinfo_Focus);
        txtContact04.focusedProperty().addListener(contactinfo_Focus);
        txtContact05.focusedProperty().addListener(contactinfo_Focus);
        txtContact06.focusedProperty().addListener(contactinfo_Focus);
        txtContact07.focusedProperty().addListener(contactinfo_Focus);
        txtContact08.focusedProperty().addListener(contactinfo_Focus);
        txtContact09.focusedProperty().addListener(contactinfo_Focus);
        txtContact10.focusedProperty().addListener(contactinfoTextArea_Focus);
    }
    
    
    final ChangeListener<? super Boolean> contactinfoTextArea_Focus = (o,ov,nv)->{ 
        if (!pbLoaded) return;
       
        TextArea socialinfo = (TextArea)((ReadOnlyBooleanPropertyBase)o).getBean();
        int lnIndex = Integer.parseInt(socialinfo.getId().substring(10, 12));
        String lsValue = socialinfo.getText();
        JSONObject jsonObject = new JSONObject();
        if (lsValue == null) return;         
        if(!nv){ /*Lost Focus*/
            switch (lnIndex){
                case 10: /*company name*/
                    oTrans.setInsContact(pnContact, "sRemarksx", lsValue);
                    break;
                
            }
            loadContctPerson();
        } else
            socialinfo.selectAll();
        
//            pnIndex = lnIndex;
    };
    
    private void loadContctPerson(){
        contact_data.clear();
        for (int lnCtr = 0; lnCtr < oTrans.getInsContactList().size(); lnCtr++){
            contact_data.add(new ModelInstitutionalContactPerson(String.valueOf(lnCtr + 1),
                oTrans.getInsContact(lnCtr, "sCPerson1").toString(),
                oTrans.getInsContact(lnCtr, "sCPPosit1").toString(),
                oTrans.getInsContact(lnCtr, "sMobileNo").toString(),
                oTrans.getInsContact(lnCtr, "sTelNoxxx").toString(),
                oTrans.getInsContact(lnCtr, "sFaxNoxxx").toString(),
                oTrans.getInsContact(lnCtr, "sEMailAdd").toString(),
                oTrans.getInsContact(lnCtr, "sAccount1").toString(),
                oTrans.getInsContact(lnCtr, "sAccount2").toString(),
                oTrans.getInsContact(lnCtr, "sAccount3").toString(),
                oTrans.getInsContact(lnCtr, "sRemarksx").toString(),
                oTrans.getInsContact(lnCtr, 13).toString(),
                oTrans.getInsContact(lnCtr, "cRecdStat").toString()));
        }
    }
    
    private void personalinfo_KeyPressed(KeyEvent event){
        TextField personalinfo = (TextField)event.getSource();
        int lnIndex = Integer.parseInt(((TextField)event.getSource()).getId().substring(12,14));
        String lsValue = personalinfo.getText();
        
        switch (event.getCode()) {
            case F3:
                
            case ENTER:
                switch (lnIndex){
                    case 16: /*search branch*/
//                        if(!oTrans.searchBranch(lsValue, false)) {
//                            
////                                txtField16.setText((String) oTrans.getMaster("sBranchNm"));
//txtField.setText((String) oTrans.getMaster("xBranchNm"));
//ShowMessageFX.Warning(getStage(), "Unable to update branch. " + (String) oTrans.getMaster("xBranchNm"), "Warning", null);
//                        }
                        break;
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
    
    private void initTextFields(){
        /*textFields FOCUSED PROPERTY*/
        txtField01.focusedProperty().addListener(txtField_Focus);
    }
    
    private void initButton(int fnValue){
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);
        
//        btnCancel.setVisible(lbShow);
//        btnSearch.setVisible(lbShow);
//        btnSave.setVisible(lbShow);
//        
//        btnSave.setManaged(lbShow);
//        btnCancel.setManaged(lbShow);
//        btnSearch.setManaged(lbShow);
//        btnUpdate.setVisible(!lbShow);
//        btnBrowse.setVisible(!lbShow);
//        btnNew.setVisible(!lbShow);
//        
//        txtSeeks21.setDisable(!lbShow);
//        txtSeeks22.setDisable(!lbShow);
        
        if (lbShow){
//            txtSeeks21.setDisable(lbShow);
//            txtSeeks21.clear();
//            txtSeeks22.setDisable(lbShow);
//            txtSeeks22.clear();
            
//            btnCancel.setVisible(lbShow);
//            btnSearch.setVisible(lbShow);
//            btnSave.setVisible(lbShow);
//            btnUpdate.setVisible(!lbShow);
//            btnBrowse.setVisible(!lbShow);
//            btnNew.setVisible(!lbShow);
//            btnBrowse.setManaged(false);
//            btnNew.setManaged(false);
//            btnUpdate.setManaged(false);
        }
        else{
//            txtSeeks21.setDisable(lbShow);
//            txtSeeks21.requestFocus();
//            txtSeeks22.setDisable(lbShow);  
        }
    }
    private void ClickButton() {
//        btnCancel.setOnAction(this::handleButtonAction);
//        btnNew.setOnAction(this::handleButtonAction);
          btnSave.setOnAction(this::handleButtonAction);   
          btnAddInsContact.setOnAction(this::handleButtonAction);
    }
    
    private void handleButtonAction(ActionEvent event) {
        Object source = event.getSource();
        if (source instanceof Button) {
            Button clickedButton = (Button) source;
            switch (clickedButton.getId()) {
                case "btnCancel":
                    pnEditMode = EditMode.UNKNOWN;
                    initButton(pnEditMode);
                    break;
                case "btnSave":
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
                case "btnAddInsContact":
                        JSONObject addInsContct = oTrans.addInsContact();
                        
                            System.out.println((String) addInsContct.get("message"));
                        if ("error".equals((String) addInsContct.get("result"))){
                            ShowMessageFX.Information((String) addInsContct.get("message"), "Computerized Acounting System", pxeModuleName);
                            break;
                        } 
//                        txtSocial01.clear();
//                        txtSocial02.clear();
                        pnContact = oTrans.getInsContactList().size()-1;
                        
                        loadContctPerson();
                        tblContact.getSelectionModel().select(pnContact + 1);
                     break;
                
                
                // Add more cases for other buttons if needed
            }
        }
    }
    
    
    /* this is where you insert data */
    final ChangeListener<? super Boolean> txtField_Focus = (o,ov,nv)->{ 
        if (!pbLoaded) return;
        
        TextField txtField = (TextField)((ReadOnlyBooleanPropertyBase)o).getBean();
        int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
        String lsValue = txtField.getText();
        
        if (lsValue == null) return;
        JSONObject jsonObject = new JSONObject();
//        Client_Master model = new Client_Master(oApp, false, oApp.getBranchCode());
//        jsonObject = model.newRecord();
        if(!nv){ /*Lost Focus*/
            switch (lnIndex){
                case 1:
//                    jsonObject = oTrans.setMaster(1, lsValue);
                    if("error".equalsIgnoreCase((String)jsonObject.get("result"))){
                    }
                    break;
                case 2:
                    jsonObject = oTrans.setMaster(2, "0");
                    if ("error".equals((String) jsonObject.get("result"))){
                        System.err.println((String) jsonObject.get("message"));
                        System.exit(1);
                    }
                    break;
//                case 35:
//                    jsonObject = oTrans.setMaster(3, lsValue);
//                    break;
            }
        } else
            txtField.selectAll();
        
//            pnIndex = lnIndex;
    };
    
    private String dateFormatter(String lsValue) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("M/d/yyyy");
        // Parse the input string into a LocalDate object
        LocalDate date = LocalDate.parse(lsValue, inputFormatter);
        // Define the desired output format
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        // Format the date into the desired format
        return date.format(outputFormatter);
    }
    
    final ChangeListener<? super Boolean> contactinfo_Focus = (o,ov,nv)->{ 
        if (!pbLoaded) return;
       
        TextField socialinfo = (TextField)((ReadOnlyBooleanPropertyBase)o).getBean();
        int lnIndex = Integer.parseInt(socialinfo.getId().substring(10, 12));
        String lsValue = socialinfo.getText();
        JSONObject jsonObject = new JSONObject();
        if (lsValue == null) return;         
        if(!nv){ /*Lost Focus*/
            switch (lnIndex){
                case 1: /*company name*/
                    oTrans.setInsContact(pnContact, 3, lsValue);
                    break;
                case 2: /*company name*/
                    oTrans.setInsContact(pnContact, "sCPPosit1", lsValue);
                    break;
                case 3: /*company name*/
                    oTrans.setInsContact(pnContact, "sAccount1", lsValue);
                    break;
                case 4: /*company name*/
                    oTrans.setInsContact(pnContact, "sAccount2", lsValue);
                    break;
                case 5: /*company name*/
                    oTrans.setInsContact(pnContact, "sAccount3", lsValue);
                    break;
                case 6: /*company name*/
                    oTrans.setInsContact(pnContact, "sMobileNo", lsValue);
                    break;
                case 7: /*company name*/
                    oTrans.setInsContact(pnContact, "sTelNoxxx", lsValue);
                    break;
                case 8: /*company name*/
                    oTrans.setInsContact(pnContact, "sFaxNoxxx", lsValue);
                    break;
                case 9: /*company name*/
                    oTrans.setInsContact(pnContact, "sEMailAdd", lsValue);
                    break;
                
            }
            loadContctPerson();
        } else
            socialinfo.selectAll();
        
//            pnIndex = lnIndex;
    };
    
   private void initContctPersonGrid() {
        indexContact01.setStyle("-fx-alignment: CENTER;");
        indexContact02.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        indexContact03.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        indexContact04.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        indexContact05.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        indexContact06.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        indexContact07.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
//        index06.setStyle("-fx-alignment: CENTER-RIGHT;-fx-padding: 0 5 0 0;");
//        index07.setStyle("-fx-alignment: CENTER-RIGHT;-fx-padding: 0 5 0 0;");
//        index08.setStyle("-fx-alignment: CENTER-RIGHT;-fx-padding: 0 5 0 0;");
//        
        indexContact01.setCellValueFactory(new PropertyValueFactory<>("index01"));
        indexContact02.setCellValueFactory(new PropertyValueFactory<>("index02"));
        indexContact03.setCellValueFactory(new PropertyValueFactory<>("index03"));
        indexContact04.setCellValueFactory(new PropertyValueFactory<>("index04"));
        indexContact05.setCellValueFactory(new PropertyValueFactory<>("index05"));
        indexContact06.setCellValueFactory(new PropertyValueFactory<>("index06"));
        indexContact07.setCellValueFactory(new PropertyValueFactory<>("index07"));
        tblContact.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblContact.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });
        tblContact.setItems(contact_data);
//        tblMobile.getSelectionModel().select(pnMobile + 1);
        tblContact.autosize();
    }
   
    @FXML
    private void CheckContact01_Clicked(MouseEvent event) {
        boolean isChecked = cbContact01.isSelected();
        oTrans.setInsContact(pnContact, 13, (isChecked)? "1":"0");
        loadContctPerson();
        String val = (isChecked)? "1":"0";
        System.out.println("isChecked = " + val);
        System.out.println("value = " + oTrans.getInsContact(pnContact, "cRecdStat"));
        
    }
    @FXML
    private void CheckContact02_Clicked(MouseEvent event) {
        boolean isChecked = cbContact02.isSelected();
        oTrans.setInsContact(pnContact, "cRecdStat", (isChecked)? "1":"0");
        loadContctPerson();
        String val = (isChecked)? "1":"0";
        System.out.println("isChecked = " + val);
        System.out.println("value = " + oTrans.getInsContact(pnContact, "cRecdStat"));
        
    }
    
    
    private void clearallFields(){
    
    }
}
