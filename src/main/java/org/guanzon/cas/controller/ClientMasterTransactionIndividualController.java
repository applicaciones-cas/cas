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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.cas.clients.Client_Master;
import org.guanzon.cas.model.ModelAddress;
import org.guanzon.cas.model.ModelEmail;
import org.guanzon.cas.model.ModelMobile;
import org.guanzon.cas.model.ModelSocialMedia;
import org.guanzon.cas.validators.ValidatorFactory;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author User
 */
public class ClientMasterTransactionIndividualController implements Initializable, ScreenInterface {
    private final String pxeModuleName = "Client Master Parameter";
    private GRider oApp;
    private Client_Master oTrans;
//    private JSONObject poJSON;
    private int pnEditMode;  
//    private unloadForm appUnload;
    private String oTransnox = "";
    
    private boolean state = false;
    private boolean pbLoaded = false;
    
    @FXML
    private AnchorPane AnchorMain;

    @FXML
    private TextField txtField02;

    @FXML
    private TextField txtField01;

    @FXML
    private TextField txtField03;

    @FXML
    private TextField personalinfo02;

    @FXML
    private TextField personalinfo03;

    @FXML
    private TextField personalinfo04;

    @FXML
    private TextField personalinfo05;

    @FXML
    private TextField personalinfo12;

    @FXML
    private ComboBox personalinfo09;

    @FXML
    private ComboBox personalinfo10;

    @FXML
    private TextField personalinfo06;

    @FXML
    private DatePicker personalinfo07;

    @FXML
    private TextField personalinfo08;

    @FXML
    private TextField personalinfo11;

    @FXML
    private TextField personalinfo01;

    @FXML
    private AnchorPane anchorAddress;

    @FXML
    private TableView tblAddress;

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
    private TextField AddressField01;

    @FXML
    private TextField AddressField02;

    @FXML
    private TextField AddressField03;

    @FXML
    private TextField AddressField04;

    @FXML
    private TextField AddressField05;

    @FXML
    private TextField AddressField06;

    @FXML
    private CheckBox cbAddress03;

    @FXML
    private CheckBox cbAddress04;

    @FXML
    private CheckBox cbAddress05;

    @FXML
    private CheckBox cbAddress06;

    @FXML
    private CheckBox cbAddress07;

    @FXML
    private CheckBox cbAddress08;

    @FXML
    private CheckBox cbAddress01;

    @FXML
    private CheckBox cbAddress02;

    @FXML
    private Button btnAddAddress;

    @FXML
    private Button btnDelAddress; 
    
    @FXML
    private Button btnDelMobile;

    @FXML
    private Label lblAddressStat;

    @FXML
    private TableView tblMobile;

    @FXML
    private TableColumn indexMobileNo01;

    @FXML
    private TableColumn indexMobileNo02;

    @FXML
    private TableColumn indexMobileNo03;

    @FXML
    private TableColumn indexMobileNo04;

    @FXML
    private ComboBox cmbMobile01;

    @FXML
    private ComboBox cmbMobile02;

    @FXML
    private TextField txtMobile01;

    @FXML
    private CheckBox cbMobileNo02;

    @FXML
    private CheckBox cbMobileNo01;

    @FXML
    private Button btnAddMobile;

    @FXML
    private Button btnDelEmail;

    @FXML
    private Label lblMobileStat;

    @FXML
    private TableView tblEmail;

    @FXML
    private TableColumn indexEmail01;

    @FXML
    private TableColumn indexEmail02;

    @FXML
    private TableColumn indexEmail03;

    @FXML
    private ComboBox cmbEmail01;

    @FXML
    private TextField mailFields01;

    @FXML
    private CheckBox cbEmail02;

    @FXML
    private CheckBox cbEmail01;

    @FXML
    private Button btnAddEmail;

    @FXML
    private Label lblEmailStat;

    @FXML
    private TableView tblSocMed;

    @FXML
    private TableColumn indexSocMed01;

    @FXML
    private TableColumn indexSocMed02;

    @FXML
    private TableColumn indexSocMed03;

    @FXML
    private TableColumn indexSocMed04;

    @FXML
    private ComboBox cmbSocMed01;

    @FXML
    private TextField txtSocial01;

    @FXML
    private TextArea txtSocial02;

    @FXML
    private CheckBox cbSocMed01;

    @FXML
    private Button btnAddSocMed;

    @FXML
    private Button btnDelSocMed;

    @FXML
    private Label lblSocMedStat;

    @FXML
    private TextField personalinfo13;

    @FXML
    private TextField personalinfo14;

    @FXML
    private TextField personalinfo15;

    @FXML
    private HBox hbButtons;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnCancel;
    
    private ObservableList<ModelMobile> data = FXCollections.observableArrayList();
    private ObservableList<ModelEmail> email_data = FXCollections.observableArrayList();
    private ObservableList<ModelSocialMedia> social_data = FXCollections.observableArrayList();    
    private ObservableList<ModelAddress> address_data = FXCollections.observableArrayList();    
    
    ObservableList<String> mobileType = FXCollections.observableArrayList("Mobile No", "Tel No", "Fax No");
    ObservableList<String> mobileOwn = FXCollections.observableArrayList("Personal", "Office", "Others");
    ObservableList<String> EmailOwn = FXCollections.observableArrayList("Personal", "Office", "Others");
    ObservableList<String> socialTyp = FXCollections.observableArrayList("Facebook", "Instagram", "Twitter");
    
    ObservableList<String> genders = FXCollections.observableArrayList(
            "Male",
            "Female",
            "Other"
        );
          // Create a list of civilStatuses    
        ObservableList<String> civilStatuses = FXCollections.observableArrayList(
            "Single",
            "Married",
            "Divorced",
            "Widowed"
        );
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
        oTrans = new Client_Master(oApp, false, oApp.getBranchCode());

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
        InitPersonalInfo();
        initAddressInfo();
        InitMobileInfo();
        InitEmailInfo( );        
        InitSocMedInfo();
        initComboBoxes();
        
        loadAddress();
        loadMobile();
        loadEmail();
        initMobileGrid();
        initEmailGrid();
        initAddressGrid();
        loadSocialMedia();
        initSocialMediaGrid();
        oTrans.setType(ValidatorFactory.ClientTypes.INDIVIDUAL);
        personalinfo02.requestFocus();
        pbLoaded = true;
    
}
    
    private void personalinfo_KeyPressed(KeyEvent event){
        TextField personalinfo = (TextField)event.getSource();
        int lnIndex = Integer.parseInt(((TextField)event.getSource()).getId().substring(12,14));
        String lsValue = personalinfo.getText();
        JSONObject poJson;
        switch (event.getCode()) {
            case F3:
                switch (lnIndex){
                    case 6: /*search branch*/
                        poJson = new JSONObject();
                           poJson =  oTrans.searchCitizenship(lsValue, false);
                           System.out.println("poJson = " + poJson.toJSONString());
                           if("error".equalsIgnoreCase(poJson.get("result").toString())){
                               
                                ShowMessageFX.Information((String) poJson.get("message"), "Computerized Acounting System", pxeModuleName);
                                personalinfo06.clear();
                           }
                           personalinfo06.setText((String) poJson.get("sNational"));
                        break;
                    case 8: /*search branch*/
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
                    case 16: /*search branch*/
//                        
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
    
    private void InitMobileInfo(){
        /*MOBILE INFO FOCUSED PROPERTY*/
        txtMobile01.focusedProperty().addListener(mobileinfo_Focus);
        
        cmbMobile01.setItems(mobileOwn);
        cmbMobile01.getSelectionModel().select(0);
        
        cmbMobile02.setItems(mobileType);
        cmbMobile02.getSelectionModel().select(0);
        
        oTrans.setMobile(pnMobile, "cOwnerxxx", cmbMobile01.getSelectionModel().getSelectedIndex());
        oTrans.setMobile(pnMobile, "cMobileTp", cmbMobile02.getSelectionModel().getSelectedIndex());
        
        cmbMobile01.setOnAction(event -> {
            oTrans.setMobile(pnMobile, "cOwnerxxx", cmbMobile01.getSelectionModel().getSelectedIndex());
            loadMobile();
            initMobileGrid();
        });
        cmbMobile02.setOnAction(event -> {
            oTrans.setMobile(pnMobile, "cMobileTp", cmbMobile02.getSelectionModel().getSelectedIndex());
            loadMobile();
            initMobileGrid();
        });
        
        
    }
    private void InitEmailInfo(){
        /*MOBILE INFO FOCUSED PROPERTY*/
        mailFields01.focusedProperty().addListener(mailInfo_Focus);
        
        cmbEmail01.setItems(EmailOwn);
        cmbEmail01.getSelectionModel().select(0);
        
        oTrans.setEmail(pnEmail, "cOwnerxxx", cmbEmail01.getSelectionModel().getSelectedIndex());
        
        cmbEmail01.setOnAction(event -> {
            oTrans.setEmail(pnEmail, "cOwnerxxx", cmbEmail01.getSelectionModel().getSelectedIndex());
            loadEmail();
            initEmailGrid();
        });
        
        
        
    }
    
    
    private void InitSocMedInfo(){
        /*MOBILE INFO FOCUSED PROPERTY*/
        txtSocial01.focusedProperty().addListener(socialinfo_Focus);
        txtSocial02.focusedProperty().addListener(socialinfoTextArea_Focus);
        
        cmbSocMed01.setItems(socialTyp);
        cmbSocMed01.getSelectionModel().select(0);
        
        
        oTrans.setSocialMed(pnSocMed, "cSocialTp", cmbSocMed01.getSelectionModel().getSelectedIndex());
        cmbSocMed01.setOnAction(event -> {
            oTrans.setSocialMed(pnSocMed, "cSocialTp", cmbSocMed01.getSelectionModel().getSelectedIndex());
            loadSocialMedia();
            initSocialMediaGrid();
        });
        
        
    }
    private void InitPersonalInfo(){
        /*PERSONAL INFO FOCUSED PROPERTY*/
        personalinfo01.focusedProperty().addListener(personalinfo_Focus);
        personalinfo02.focusedProperty().addListener(personalinfo_Focus);
        personalinfo03.focusedProperty().addListener(personalinfo_Focus);
        personalinfo04.focusedProperty().addListener(personalinfo_Focus);
        personalinfo05.focusedProperty().addListener(personalinfo_Focus);
        personalinfo06.focusedProperty().addListener(personalinfo_Focus);
        personalinfo08.focusedProperty().addListener(personalinfo_Focus);
        personalinfo11.focusedProperty().addListener(personalinfo_Focus);
        personalinfo12.focusedProperty().addListener(personalinfo_Focus);
        personalinfo13.focusedProperty().addListener(personalinfo_Focus);
        personalinfo14.focusedProperty().addListener(personalinfo_Focus);
        personalinfo15.focusedProperty().addListener(personalinfo_Focus);
        
        /*PERSONAL INFO KEYPRESSED*/
        personalinfo08.setOnKeyPressed(this::personalinfo_KeyPressed);
        personalinfo06.setOnKeyPressed(this::personalinfo_KeyPressed);
         // Set a custom StringConverter to format date
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        personalinfo07.setConverter(new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    oTrans.setMaster("dBirthDte", dateFormatter.format(date));
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
    
    private void initAddressInfo(){
        /*Address FOCUSED PROPERTY*/
        AddressField01.focusedProperty().addListener(address_Focus);
        AddressField02.focusedProperty().addListener(address_Focus);
        AddressField03.focusedProperty().addListener(address_Focus);
        AddressField04.focusedProperty().addListener(address_Focus);
        AddressField05.focusedProperty().addListener(address_Focus);        
        AddressField06.focusedProperty().addListener(address_Focus);
        
        AddressField03.setOnKeyPressed(this::addressinfo_KeyPressed);
        AddressField04.setOnKeyPressed(this::addressinfo_KeyPressed);
        
    }
    
    private void addressinfo_KeyPressed(KeyEvent event){
        TextField personalinfo = (TextField)event.getSource();
        int lnIndex = Integer.parseInt(((TextField)event.getSource()).getId().substring(12,14));
        String lsValue = personalinfo.getText();
        JSONObject poJson;
        switch (event.getCode()) {
            case F3:
                switch (lnIndex){
                    case 3: /*search barangay*/
                        poJson = new JSONObject();
                           poJson = oTrans.SearchTownAddress(pnAddress, lsValue, false);
                           System.out.println("poJson = " + poJson.toJSONString());
                           if("error".equalsIgnoreCase(poJson.get("result").toString())){
                               
                                ShowMessageFX.Information((String) poJson.get("message"), "Computerized Acounting System", pxeModuleName);
                                AddressField03.clear();
                           }
                           AddressField03.setText((String) poJson.get("sTownName") + ", " + (String) poJson.get("sProvName"));
                        break;
                    case 4: /*search branch*/
                        poJson = new JSONObject();

                           poJson =  oTrans.SearchBarangayAddress(pnAddress, lsValue, false);
                           System.out.println("poJson = " + poJson.toJSONString());
                           if("error".equalsIgnoreCase(poJson.get("result").toString())){
                               
                                ShowMessageFX.Information((String) poJson.get("message"), "Computerized Acounting System", pxeModuleName);
                                AddressField04.clear();
                           }
                           AddressField04.setText((String) poJson.get("sBrgyName"));

                        poJson =  oTrans.SearchBarangayAddress(pnAddress, lsValue, false);
                        System.out.println("poJson = " + poJson.toJSONString());
                        if("error".equalsIgnoreCase(poJson.get("result").toString())){

                             ShowMessageFX.Information((String) poJson.get("message"), "Computerized Acounting System", pxeModuleName);
                             AddressField04.clear();
                        }
//                           AddressField04.setText((String) oTrans.getAddress(pnAddress,"sBrgyName"));
                        AddressField04.setText((String)oTrans.getAddress(pnAddress, 21));
//                           AddressField04.setText(oTrans.getAddress(pnAddress).getBarangayName());

                        break;
                }
            case ENTER:
                
        }
        loadAddress();
        
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
        txtField02.focusedProperty().addListener(txtField_Focus);
        txtField03.focusedProperty().addListener(txtField_Focus);
        
    }
    
    private void initButton(int fnValue){
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);
        
        btnCancel.setVisible(lbShow);
       
        btnSave.setVisible(lbShow);
        
        btnSave.setManaged(lbShow);
        btnCancel.setManaged(lbShow);
        
//        
//        txtSeeks21.setDisable(!lbShow);
//        txtSeeks22.setDisable(!lbShow);
        
        if (lbShow){
//            txtSeeks21.setDisable(lbShow);
//            txtSeeks21.clear();
//            txtSeeks22.setDisable(lbShow);
//            txtSeeks22.clear();
            
            btnCancel.setVisible(lbShow);
            
            btnSave.setVisible(lbShow);
            
        }
        else{
//            txtSeeks21.setDisable(lbShow);
//            txtSeeks21.requestFocus();
//            txtSeeks22.setDisable(lbShow);  
        }
    }
    private void ClickButton() {
        btnCancel.setOnAction(this::handleButtonAction);
        
        btnSave.setOnAction(this::handleButtonAction);
        btnAddMobile.setOnAction(this::handleButtonAction);
        btnAddSocMed.setOnAction(this::handleButtonAction);      
        btnAddAddress.setOnAction(this::handleButtonAction);     
        btnAddEmail.setOnAction(this::handleButtonAction);   
        btnDelAddress.setOnAction(this::handleButtonAction);   
        btnDelMobile.setOnAction(this::handleButtonAction);      
        btnDelEmail.setOnAction(this::handleButtonAction);     
        btnDelSocMed.setOnAction(this::handleButtonAction);      
    }
    
    private void handleButtonAction(ActionEvent event) {
        Object source = event.getSource();
        if (source instanceof Button) {
            Button clickedButton = (Button) source;
            unloadForm appUnload = new unloadForm();
            switch (clickedButton.getId()) {
                case "btnCancel":
                     if (ShowMessageFX.YesNo("Do you really want to cancel this record? \nAny data collected will not be kept.", "Computerized Acounting System", pxeModuleName)){
//                            clearAllFields();
                            pnEditMode = EditMode.UNKNOWN;
//                            unloadForm();
                            
                            appUnload.unloadForm(AnchorMain, oApp, "Client Transactions Individual");
                        }
                    break;
                case "btnSave":
                    
                        if(!personalinfo01.getText().toString().isEmpty()){
                            oTrans.getModel().setFullName(personalinfo01.getText());
                        }
                        oTrans.getModel().setClientType("1");
                        JSONObject saveResult = oTrans.saveRecord();
                        if ("success".equals((String) saveResult.get("result"))){
                            System.err.println((String) saveResult.get("message"));
                            ShowMessageFX.Information((String) saveResult.get("message"), "Computerized Acounting System", pxeModuleName);
                            System.out.println("Record saved successfully.");
                            appUnload.unloadForm(AnchorMain, oApp, "Client Transactions Individual");
                        } else {
                            ShowMessageFX.Information((String)saveResult.get("message"), "Computerized Acounting System", pxeModuleName);
                            System.out.println("Record not saved successfully.");
                            System.out.println((String) saveResult.get("message"));
                        }
                     break;
                
                case "btnAddAddress":
                        JSONObject addObjAddress = oTrans.addAddress();
                           System.out.println((String) addObjAddress.get("message"));
                       if ("error".equals((String) addObjAddress.get("result"))){
                           ShowMessageFX.Information((String) addObjAddress.get("message"), "Computerized Acounting System", pxeModuleName);
                           break;
                       } 
                        AddressField01.clear();
                        AddressField02.clear();
                        AddressField03.clear();
                        AddressField04.clear();
                        AddressField05.clear();
                        AddressField06.clear();
                        cbAddress01.setSelected(false);
                        cbAddress02.setSelected(false);
                        cbAddress03.setSelected(false);
                        cbAddress04.setSelected(false);
                        cbAddress05.setSelected(false);
                        cbAddress06.setSelected(false);
                        cbAddress07.setSelected(false);
                        cbAddress08.setSelected(false);
                        loadAddress();
                       
                       pnAddress = oTrans.getAddressList().size()-1;

                       tblAddress.getSelectionModel().select(pnAddress + 1);
//                       initAddressGrid();
                         break;
                     
                     
                case "btnAddEmail":
                        JSONObject addObjMail = oTrans.addMail();
                        
                            System.out.println((String) addObjMail.get("message"));
                        if ("error".equals((String) addObjMail.get("result"))){
                            ShowMessageFX.Information((String) addObjMail.get("message"), "Computerized Acounting System", pxeModuleName);
                            break;
                        } 
                        mailFields01.clear();
                        loadEmail();
                        pnEmail = oTrans.getEmailList().size()-1;
                        tblEmail.getSelectionModel().select(pnEmail + 1);
                     break;
                case "btnAddMobile":
                        JSONObject addObj = oTrans.addContact();
                        
                            System.out.println((String) addObj.get("message"));
                        if ("error".equals((String) addObj.get("result"))){
                            ShowMessageFX.Information((String) addObj.get("message"), "Computerized Acounting System", pxeModuleName);
                            break;
                        } 
                        txtMobile01.clear();
                        loadMobile();
                        pnMobile = oTrans.getMobileList().size()-1;
                        
                        tblMobile.getSelectionModel().select(pnMobile + 1);
                     break;
                case "btnAddSocMed":
                        JSONObject addSocMed = oTrans.addSocialMedia();
                        
                            System.out.println((String) addSocMed.get("message"));
                        if ("error".equals((String) addSocMed.get("result"))){
                            ShowMessageFX.Information((String) addSocMed.get("message"), "Computerized Acounting System", pxeModuleName);
                            break;
                        } 
                        txtSocial01.clear();
                        txtSocial02.clear();
                        pnSocMed = oTrans.getSocialMediaList().size()-1;
                        
                        loadSocialMedia();
                        tblSocMed.getSelectionModel().select(pnSocMed + 1);
                     break;
                     
                case "btnDelAddress":
                    if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Do you want to remove these details? ") == true){  
                        oTrans.getAddressList().remove(pnAddress);
                        if(oTrans.getAddressList().size() <= 0){
                            oTrans.addAddress();
                        }
                        
                        pnAddress = oTrans.getAddressList().size()-1;
                        loadAddress();
                        clearAddress();
                        AddressField01.requestFocus();
                    }
                    break;
                case "btnDelMobile":
                    if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Do you want to remove these details? ") == true){  
                        oTrans.getMobileList().remove(pnMobile);
                        if(oTrans.getMobileList().size() <= 0){
                            oTrans.addContact();
                        }
                        
                        pnMobile = oTrans.getMobileList().size()-1;
                        loadMobile();
                        clearMobile();
                    }
                    break;
                case "btnDelEmail":
                    if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Do you want to remove these details? ") == true){  
                        oTrans.getEmailList().remove(pnEmail);
                        if(oTrans.getEmailList().size() <= 0){
                            oTrans.addMail();
                        }
                        
                        pnEmail = oTrans.getEmailList().size()-1;
                        loadEmail();
                        clearEmail();
                    }
                    break;
                case "btnDelSocMed":
                    if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Do you want to remove these details? ") == true){  
                        oTrans.getSocialMediaList().remove(pnSocMed);
                        if(oTrans.getSocialMediaList().size() <= 0){
                            oTrans.addSocialMedia();
                        }
                        
                        pnSocMed = oTrans.getSocialMediaList().size()-1;
                        loadSocialMedia();
                        clearSocMed();
                    }
                    break;     
                     
               
            }
        }
    }
    
    
    /* this is where you insert data */
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
                    jsonObject = oTrans.setMaster( 3,lsValue);
                    break;
                case 3:/*frist name*/
                    jsonObject = oTrans.setMaster( 4,lsValue);
                    break;
                case 4:/*middle name*/
                    jsonObject = oTrans.setMaster( 5,lsValue);
                    break;
                case 5:/*suffix name*/
                    jsonObject = oTrans.setMaster( 6,lsValue);
                    break;
                case 6:/*citizenship */
                    jsonObject = (JSONObject) oTrans.getMaster(13);
                    
                    System.out.println(String.valueOf("citizenship = " + oTrans.getMaster(13)));
                    break;
//                case 6:/*citizenship */
//                    jsonObject = oTrans.setMaster( 11,lsValue);
//                    break;
//                case 7:/*birthday */
//                    // Define the format of the input string
//                    jsonObject = oTrans.setMaster( "dBirthDte", dateFormatter(lsValue));
//                    
//                    break;
//                case 8:/*birthplace */
//                    jsonObject = oTrans.setMaster( 13,lsValue);
//                    break;    
                case 11:/*spouse */
                    jsonObject = oTrans.setMaster( 15,lsValue);
                    break;
                case 12:/*mothers maiden namex */
                    jsonObject = oTrans.setMaster( 7,lsValue);
                case 13:/*mothers maiden namex */
                    jsonObject = oTrans.setMaster( 16,lsValue);
                case 14:/*mothers maiden namex */
                    jsonObject = oTrans.setMaster( 17,lsValue);
                case 15 :/*mothers maiden namex */
                    jsonObject = oTrans.setMaster( 18,lsValue);
                    break;
            }
            personalinfo01.setText(personalinfo02.getText() + ", " + personalinfo03.getText() + " " + personalinfo05.getText() + " " + personalinfo04.getText());
            
            txtField02.setText(personalinfo02.getText() + ", " + personalinfo03.getText() + " " + personalinfo05.getText() + " " + personalinfo04.getText());
        } else
            personalinfo.selectAll();
        
//            pnIndex = lnIndex;
    };
    
    final ChangeListener<? super Boolean> address_Focus = (o,ov,nv)->{ 
        if (!pbLoaded) return;
       
        TextField AddressField = (TextField)((ReadOnlyBooleanPropertyBase)o).getBean();
        int lnIndex = Integer.parseInt(AddressField.getId().substring(12, 14));
        String lsValue = AddressField.getText();
        JSONObject jsonObject = new JSONObject();
        if (lsValue == null) return;         
        if(!nv){ /*Lost Focus*/
            switch (lnIndex){
                case 1: /*houseno*/
                    oTrans.setAddress(pnAddress, 3, lsValue);
                    break;
                case 2:/*address*/
                    oTrans.setAddress(pnAddress, 4, lsValue);
                    break;
                case 6:/*latitude*/
                    oTrans.setAddress(pnAddress, 7, lsValue);
                    break;
                case 7:/*longitud*/
                    oTrans.setAddress(pnAddress, 8, lsValue);
                    break;
            }
            loadAddress();
            
        } else
            AddressField.selectAll();
    };
    
    final ChangeListener<? super Boolean> socialinfo_Focus = (o,ov,nv)->{ 
        if (!pbLoaded) return;
       
        TextField socialinfo = (TextField)((ReadOnlyBooleanPropertyBase)o).getBean();
        int lnIndex = Integer.parseInt(socialinfo.getId().substring(9, 11));
        String lsValue = socialinfo.getText();
        JSONObject jsonObject = new JSONObject();
        if (lsValue == null) return;         
        if(!nv){ /*Lost Focus*/
            switch (lnIndex){
                case 1: /*company name*/
                    oTrans.setSocialMed(pnSocMed, "sAccountx", lsValue);
                    break;
                
            }
            loadSocialMedia();
        } else
            socialinfo.selectAll();
        
//            pnIndex = lnIndex;
    };
    
    final ChangeListener<? super Boolean> socialinfoTextArea_Focus = (o,ov,nv)->{ 
        if (!pbLoaded) return;
       
        TextArea socialinfo = (TextArea)((ReadOnlyBooleanPropertyBase)o).getBean();
        int lnIndex = Integer.parseInt(socialinfo.getId().substring(9, 11));
        String lsValue = socialinfo.getText();
        JSONObject jsonObject = new JSONObject();
        if (lsValue == null) return;         
        if(!nv){ /*Lost Focus*/
            switch (lnIndex){
                case 2: /*company name*/
                    oTrans.setSocialMed(pnSocMed, "sRemarksx", lsValue);
                    break;
                
            }
            loadSocialMedia();
            initSocialMediaGrid();
        } else
            socialinfo.selectAll();
        
//            pnIndex = lnIndex;
    };
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
                                       
                     if (lsValue.matches("\\d{11}")) {
                            oTrans.setMobile(pnMobile, "sMobileNo", lsValue);
                        } else {
                             ShowMessageFX.OkayCancel(null, pxeModuleName, "Contact number must be exactly 11 digits.");
                            mobileinfo.requestFocus();
                            break;
                        }
                        oTrans.setMobile(pnMobile, "sMobileNo", lsValue);
                    
                    break;
                
            }
            loadMobile();
        } else
            mobileinfo.selectAll();
        
//            pnIndex = lnIndex;
    };
    
    final ChangeListener<? super Boolean> mailInfo_Focus = (o,ov,nv)->{ 
        if (!pbLoaded) return;

        TextField mailFields = (TextField)((ReadOnlyBooleanPropertyBase)o).getBean();
        int lnIndex = Integer.parseInt(mailFields.getId().substring(10, 12));
        String lsValue = mailFields.getText();
        System.out.println(lsValue);
        System.out.println(String.valueOf(lnIndex));
        JSONObject jsonObject = new JSONObject();
        if (lsValue == null) return;         
        if(!nv){ /*Lost Focus*/
            switch (lnIndex){
                case 1: /*company name*/
                    System.out.println(lsValue);
                    System.out.println(pnEmail);
                    oTrans.setEmail(pnEmail ,3,  lsValue);
                    
                    break;
                
            }
            loadEmail();
        } else
            mailFields01.selectAll();
        
//            pnIndex = lnIndex;
    };
    final ChangeListener<? super Boolean> txtField_Focus = (o,ov,nv)->{ 
        if (!pbLoaded) return;
        
        TextField txtField = (TextField)((ReadOnlyBooleanPropertyBase)o).getBean();
        int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
        String lsValue = txtField.getText();
        
        if (lsValue == null) return;
        JSONObject jsonObject = new JSONObject();
        if(!nv){ /*Lost Focus*/
            switch (lnIndex){
                case 1:
//                    jsonObject = oTrans.setMaster(1, lsValue);
                    if("error".equalsIgnoreCase((String)jsonObject.get("result"))){
                    }
                    break;
                case 2:
                    break;
                case 3:
                    break;
            }
        } else
            txtField.selectAll();
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
    
    
    private void initComboBoxes(){
    // Create a list of genders
        personalinfo09.setItems(genders);
        personalinfo09.getSelectionModel().select(0);
        
        personalinfo09.setOnAction(event -> {
            oTrans.setMaster(9, personalinfo09.getSelectionModel().getSelectedIndex());
        });
  
        // Set the items of the ComboBox to the list of genders
        personalinfo10.setItems(civilStatuses);
        personalinfo10.getSelectionModel().select(0);
        
        personalinfo10.setOnAction(event -> {
            oTrans.setMaster(10, personalinfo10.getSelectionModel().getSelectedIndex());
        });

    }
    private void loadAddress(){
        int lnCtr;
        address_data.clear();
//        oTrans.getAddress(pnAddress).list();
        if(oTrans.getAddressList() != null){
            for (lnCtr = 0; lnCtr < oTrans.getAddressList().size(); lnCtr++){
                String lsTown = (String)oTrans.getAddress(lnCtr, 20) + ", " + (String)oTrans.getAddress(lnCtr, 22);
                address_data.add(new ModelAddress(String.valueOf(lnCtr + 1),
                    (String)oTrans.getAddress(lnCtr, "sHouseNox"), 
                    (String)oTrans.getAddress(lnCtr, "sAddressx"), 
                    lsTown,
                    (String)oTrans.getAddress(lnCtr,  21),
                    (String)oTrans.getAddress(lnCtr,  "sTownIDxx"),
                    (String)oTrans.getAddress(lnCtr,  "sBrgyIDxx"),
                "",
                "",
                ""));  

            }
        }
        
    }
    
    private void loadMobile(){
        data.clear();
        for (int lnCtr = 0; lnCtr < oTrans.getMobileList().size(); lnCtr++){
            data.add(new ModelMobile(String.valueOf(lnCtr + 1),
                oTrans.getMobile(lnCtr, "sMobileNo").toString(),
                oTrans.getMobile(lnCtr, "cOwnerxxx").toString(),
                oTrans.getMobile(lnCtr, "cMobileTp").toString(),
                oTrans.getMobile(lnCtr, "cRecdStat").toString(),
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
                "",
                ""));
            System.out.println("index = " + oTrans.getMobile(lnCtr, "cOwnerxxx").toString());
            System.out.println("value = " + mobileOwn.get(Integer.parseInt(oTrans.getMobile(lnCtr, "cOwnerxxx").toString())));
        }
       
    }
    private void loadEmail(){
        email_data.clear();
        for (int lnCtr = 0; lnCtr < oTrans.getEmailList().size(); lnCtr++){
                email_data.add(new ModelEmail(String.valueOf(lnCtr + 1),
                oTrans.getEmail(lnCtr, "cOwnerxxx").toString(),
                oTrans.getEmail(lnCtr, "sEMailAdd").toString(),
                oTrans.getEmail(lnCtr, "cRecdStat").toString(),
                "",
                "",
                "",
                "",
                "",
                ""));
        }
       
    }
    private void loadSocialMedia(){
        social_data.clear();
        for (int lnCtr = 0; lnCtr < oTrans.getSocialMediaList().size(); lnCtr++){
            social_data.add(new ModelSocialMedia(String.valueOf(lnCtr + 1),
                oTrans.getSocialMed(lnCtr, "sAccountx").toString(),
                oTrans.getSocialMed(lnCtr, "cSocialTp").toString(),
                oTrans.getSocialMed(lnCtr, "sRemarksx").toString(),
                oTrans.getSocialMed(lnCtr, "cRecdStat").toString()));
            System.out.println("index = " + oTrans.getSocialMed(lnCtr, "cSocialTp").toString());
//            System.out.println("value = " + oTrans.getSocialMed(lnCtr, "cSocialTp").toString());
        }
        
       
    }
    private void initMobileGrid() {
        indexMobileNo01.setStyle("-fx-alignment: CENTER;");
        indexMobileNo02.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        indexMobileNo03.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        indexMobileNo04.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        
        indexMobileNo01.setCellValueFactory(new PropertyValueFactory<>("index01"));
        indexMobileNo02.setCellValueFactory(new PropertyValueFactory<>("index02"));
        indexMobileNo03.setCellValueFactory(new PropertyValueFactory<>("index03"));
        indexMobileNo04.setCellValueFactory(new PropertyValueFactory<>("index04"));
        tblMobile.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblMobile.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });
        tblMobile.setItems(data);
        tblMobile.autosize();
    }
    
    private void initEmailGrid() {
        indexEmail01.setStyle("-fx-alignment: CENTER;");
        indexEmail02.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        indexEmail03.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
//        
        indexEmail01.setCellValueFactory(new PropertyValueFactory<>("index01"));
        indexEmail02.setCellValueFactory(new PropertyValueFactory<>("index02"));
        indexEmail03.setCellValueFactory(new PropertyValueFactory<>("index03"));
        tblEmail.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblEmail.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });
        tblEmail.setItems(email_data);
        tblEmail.autosize();
    }
    
    private void initSocialMediaGrid() {
        indexSocMed01.setStyle("-fx-alignment: CENTER;");
        indexSocMed02.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        indexSocMed03.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        indexSocMed04.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        
        indexSocMed01.setCellValueFactory(new PropertyValueFactory<>("index01"));
        indexSocMed02.setCellValueFactory(new PropertyValueFactory<>("index02"));
        indexSocMed03.setCellValueFactory(new PropertyValueFactory<>("index03"));
        indexSocMed04.setCellValueFactory(new PropertyValueFactory<>("index04"));
        tblSocMed.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblSocMed.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });
        tblSocMed.setItems(social_data);
        tblMobile.getSelectionModel().select(pnMobile + 1);
        tblSocMed.autosize();
    }
    
   
    
    public void initAddressGrid() {   
        indexAddress01.setStyle("-fx-alignment: CENTER;");
        indexAddress02.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        indexAddress03.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        indexAddress04.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        indexAddress05.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        
        indexAddress01.setCellValueFactory(new PropertyValueFactory<>("index01"));
        indexAddress02.setCellValueFactory(new PropertyValueFactory<>("index02"));
        indexAddress03.setCellValueFactory(new PropertyValueFactory<>("index03")); 
        indexAddress04.setCellValueFactory(new PropertyValueFactory<>("index04"));  
        indexAddress05.setCellValueFactory(new PropertyValueFactory<>("index05"));  
        
        tblAddress.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblAddress.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });
        tblAddress.setItems(address_data);
        tblAddress.getSelectionModel().select(pnAddress + 1);
        tblAddress.autosize();
       
    }

    
    @FXML
    private void tblSocMed_Clicked(MouseEvent event) {
        pnSocMed = tblSocMed.getSelectionModel().getSelectedIndex(); 
    }
    @FXML
    private void CheckPrimary_Clicked(MouseEvent event) {
        boolean isChecked = cbMobileNo01.isSelected();

        oTrans.setMobile(pnMobile, "cPrimaryx", (isChecked)? "1":"0");        
        for (int lnCtr = 0; lnCtr < oTrans.getMobileList().size(); lnCtr++){
            if(pnMobile == lnCtr){
                if(isChecked){
                    oTrans.setMobile(pnMobile, "cPrimaryx", "1");
                }else{
                    oTrans.setMobile(lnCtr, "cPrimaryx", "0");
                }
            }else{
                oTrans.setMobile(lnCtr, "cPrimaryx", "0");
            }
        }
        loadMobile();
        String val = (isChecked)? "1":"0";
        System.out.println("isChecked = " + val);
        System.out.println("value = " + oTrans.getMobile(pnMobile, "cPrimaryx"));
        
    }
    
    @FXML
    private void CheckStatus_Clicked(MouseEvent event) {
        boolean isChecked = cbMobileNo02.isSelected();
        oTrans.setMobile(pnMobile, "cPrimaryx", (isChecked)? "1":"0");
        loadMobile();
        String val = (isChecked)? "1":"0";
        System.out.println("isChecked = " + val);
        System.out.println("value = " + oTrans.getMobile(pnMobile, "cPrimaryx"));
        
    }
    
    @FXML
    private void CheckSocMedtatus_Clicked(MouseEvent event) {
        boolean isChecked = cbSocMed01.isSelected();
        oTrans.setMobile(pnSocMed, "cRecdStat", (isChecked)? "1":"0");
        loadSocialMedia();
        String val = (isChecked)? "1":"0";
        System.out.println("isChecked = " + val);
        System.out.println("value = " + oTrans.getSocialMed(pnSocMed, "cRecdStat"));
        
    }
    
    @FXML
    private void CheckPrimaryEmail_Clicked(MouseEvent event) {
        boolean isChecked = cbEmail01.isSelected();
        oTrans.setEmail(pnEmail, "cPrimaryx", (isChecked)? "1":"0");
        loadEmail();
        String val = (isChecked)? "1":"0";
        System.out.println("isChecked = " + val);
        System.out.println("value = " + oTrans.getEmail(pnMobile, "cPrimaryx"));
        
    }
    
    @FXML
    private void CheckMailStatus_Clicked(MouseEvent event) {
        boolean isChecked = cbEmail02.isSelected();
        oTrans.setEmail(pnEmail, "cPrimaryx", (isChecked)? "1":"0");
        loadEmail();
        String val = (isChecked)? "1":"0";
        System.out.println("isChecked = " + val);
        System.out.println("value = " + oTrans.getEmail(pnMobile, "cPrimaryx"));
        
    }
    
    private void clearAddress() {
    // Arrays of TextFields grouped by sections
    TextField[][] allFields = {
        {AddressField01, AddressField02, AddressField03, AddressField04,
         AddressField05, AddressField06}
    };

    // Loop through each array of TextFields and clear them
    for (TextField[] fields : allFields) {
        for (TextField field : fields) {
            field.clear();
        }
    }
    }
    private void clearMobile(){
        txtMobile01.clear();
        cmbMobile01.setItems(mobileOwn);
        cmbMobile01.getSelectionModel().select(0);
        
        cmbMobile02.setItems(mobileType);
        cmbMobile02.getSelectionModel().select(0);
    }
    private void clearEmail(){
        mailFields01.clear();
        cmbEmail01.setItems(EmailOwn);
        cmbEmail01.getSelectionModel().select(0);
        cbEmail02.setSelected(true);
        cbEmail01.setSelected(false);
        
    }
    private void clearSocMed(){
        txtSocial01.clear();
        txtSocial02.clear();
        cmbSocMed01.setItems(socialTyp);
        cmbSocMed01.getSelectionModel().select(0);
        cbSocMed01.setSelected(true);
        
    }
    @FXML
    private void cbAddress01_Clicked(MouseEvent event) {
        boolean isChecked = cbAddress01.isSelected();
       
        oTrans.setAddress(pnAddress, "cRecdStat", (isChecked)? "1":"0");
        
        loadAddress();
    }
    
    @FXML
    private void cbAddress02_Clicked(MouseEvent event) {
        boolean isChecked = cbAddress02.isSelected();
        
        for (int lnCtr = 0; lnCtr < oTrans.getAddressList().size(); lnCtr++){
            if(pnAddress == lnCtr){
                if(isChecked){
                    oTrans.setAddress(pnAddress, "cPrimaryx", "1");
                }else{
                    oTrans.setAddress(lnCtr, "cPrimaryx", "0");
                }
            }else{
                oTrans.setAddress(lnCtr, "cPrimaryx", "0");
            }
            
        }
       if(address_data.size() > 0){
                for(int lnctr = 0; lnctr < oTrans.getAddressList().size(); lnctr++){    
                    if(oTrans.getAddress(lnctr, "cPrimaryx").equals("1")){
                        String lsAddress = oTrans.getAddress(lnctr).getHouseNo() + " " + oTrans.getAddress(lnctr).getAddress() +
                                " " + (String) oTrans.getAddress(lnctr, 21) + ", " + (String)  oTrans.getAddress(lnctr, 20)+ ", " + (String)  oTrans.getAddress(lnctr, 22);
                        txtField03.setText(lsAddress);
                    }
                }
            }
        loadAddress();
    }
    
    @FXML
    private void cbAddress03_Clicked(MouseEvent event) {
        boolean isChecked = cbAddress03.isSelected();
        oTrans.setAddress(pnAddress, 10, (isChecked)? "1":"0");;
    }
    @FXML
    private void cbAddress04_Clicked(MouseEvent event) {
        boolean isChecked = cbAddress04.isSelected();
        oTrans.setAddress(pnAddress, 12, (isChecked)? "1":"0");;
    }
    
    @FXML
    private void cbAddress05_Clicked(MouseEvent event) {
        boolean isChecked = cbAddress05.isSelected();
        oTrans.setAddress(pnAddress, 13, (isChecked)? "1":"0");;
    }
    
    @FXML
    private void cbAddress06_Clicked(MouseEvent event) {
        boolean isChecked = cbAddress06.isSelected();
        oTrans.setAddress(pnAddress, 11, (isChecked)? "1":"0");;
    }
    
    @FXML
    private void cbAddress07_Clicked(MouseEvent event) {
        boolean isChecked = cbAddress07.isSelected();
        oTrans.setAddress(pnAddress, 14, (isChecked)? "1":"0");;
    }
    
    @FXML
    private void cbAddress08_Clicked(MouseEvent event) {
        boolean isChecked = cbAddress08.isSelected();
        oTrans.setAddress(pnAddress, 15, (isChecked)? "1":"0");;
    }

    
    @FXML
    private void tblAddress_Clicked (MouseEvent event) {
        pnAddress = tblAddress.getSelectionModel().getSelectedIndex();
        loadAddress();
        if (pnAddress >= 0){
            getSelectedAddressItem();
        }
    }
    private void getSelectedAddressItem(){
        TextField[] fields = {AddressField01, AddressField02, AddressField03, AddressField04,
             AddressField05, AddressField06};
        
        // Loop through each array of TextFields and clear them
        for (TextField field : fields) {
            field.clear();
        }
        
        CheckBox[] checkboxs = {cbAddress01,cbAddress02, cbAddress03,
        cbAddress04, cbAddress05, cbAddress06, cbAddress07, cbAddress08};

        // Loop through each array of TextFields and clear them
        for (CheckBox checkbox : checkboxs) {
            checkbox.setSelected(false);
        }
        
        if(oTrans.getAddressList().size()>0){    
            AddressField01.setText((String) oTrans.getAddress(pnAddress, "sHouseNox"));
            AddressField02.setText((String) oTrans.getAddress(pnAddress, "sAddressx"));
            AddressField03.setText((String)  oTrans.getAddress(pnAddress, 20)+ ", " + (String)  oTrans.getAddress(pnAddress, 22));
            AddressField04.setText((String) oTrans.getAddress(pnAddress, 21));
            AddressField05.setText(oTrans.getAddress(pnAddress, "nLatitude").toString());
            AddressField06.setText(oTrans.getAddress(pnAddress, "nLongitud").toString());
            
            cbAddress01.setSelected(((String) oTrans.getAddress(pnAddress, "cRecdStat") == "1")? true : false);
            cbAddress02.setSelected(((String) oTrans.getAddress(pnAddress, "cPrimaryx")== "1")? true : false);
            cbAddress03.setSelected(((String) oTrans.getAddress(pnAddress, "cOfficexx") == "1")? true : false);
            cbAddress04.setSelected(((String) oTrans.getAddress(pnAddress, "cBillingx")== "1")? true : false);
            cbAddress05.setSelected(((String) oTrans.getAddress(pnAddress, "cShipping") == "1")? true : false);
            cbAddress06.setSelected(((String) oTrans.getAddress(pnAddress, "cProvince")== "1")? true : false);
            cbAddress07.setSelected(((String) oTrans.getAddress(pnAddress, "cCurrentx") == "1")? true : false);
            cbAddress08.setSelected(((String) oTrans.getAddress(pnAddress, "cLTMSAddx")== "1")? true : false);
        }
    }
        
    @FXML
    private void tblMobile_Clicked(MouseEvent event) {
        pnMobile = tblMobile.getSelectionModel().getSelectedIndex();
        loadMobile();
        if (pnMobile >= 0){
            getMobileSelectedItem();
        }
    }
    private void getMobileSelectedItem() {
        CheckBox[] checkboxs = {cbMobileNo01,cbMobileNo01};
        // Loop through each array of TextFields and clear them
        for (CheckBox checkbox : checkboxs) {
            checkbox.setSelected(false);
        }
        
        if(oTrans.getMobileList().size()>0){    
            txtMobile01.setText((String) oTrans.getMobile(pnMobile, 3));
            cbMobileNo02.setSelected(((String) oTrans.getMobile(pnMobile, "cRecdStat") == "1")? true : false);
            cbMobileNo01.setSelected(((String) oTrans.getMobile(pnMobile, "cPrimaryx")== "1")? true : false);
            cmbMobile01.getSelectionModel().select(Integer.parseInt((String)oTrans.getMobile(pnMobile, "cOwnerxxx")));
            cmbMobile02.getSelectionModel().select(Integer.parseInt((String)oTrans.getMobile(pnMobile, "cMobileTp")));
        }
    }
}   
