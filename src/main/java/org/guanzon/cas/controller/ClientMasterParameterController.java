/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.cas.controller;

import com.rmj.guanzongroup.cas.maven.model.ModelInstitutionalContactPerson;
import org.guanzon.cas.model.ModelAddress;
import org.guanzon.cas.model.ModelEmail;
import org.guanzon.cas.model.ModelMobile;
import org.guanzon.cas.model.ModelSocialMedia;
import com.sun.javafx.scene.control.skin.TableHeaderRow;
import java.net.URL;
import java.sql.SQLException;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
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
import javafx.scene.text.Text;
import javafx.util.StringConverter;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.cas.clients.ClientTypes;
import org.guanzon.cas.clients.Client_Master;
import org.guanzon.cas.validators.ValidatorFactory;

import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author User
 */


public class ClientMasterParameterController implements Initializable, ScreenInterface {
    private final String pxeModuleName = "ClientMasterParameter";
    private GRider oApp;
    private Client_Master oTrans;
//    private JSONObject poJSON;
    private int pnEditMode;  
    
    private String oTransnox = "";
    
    private boolean state = false;
    private boolean pbLoaded = false;
    
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
    private TextField txtField01;
    
    @FXML
    private Text lblAddressType;

    @FXML
    private ComboBox cmbField01;

    @FXML
    private TextField txtField02;

    @FXML
    private TextField txtField03;

    @FXML
    private TextField txtField04;

    @FXML
    private TextField txtField05;

    @FXML
    private TextField txtField06;

    @FXML
    private TextField txtField10;

    @FXML
    private ComboBox txtField12;

    @FXML
    private ComboBox txtField13;

    @FXML
    private DatePicker txtField07;

    @FXML
    private TextField txtField08;

    @FXML
    private TextField txtField11;

    @FXML
    private TextField txtField09;

    @FXML
    private TabPane tabpane01;

    @FXML
    private Tab tabIndex01;

    @FXML
    private AnchorPane anchorPersonal;

    @FXML
    private TextField personalinfo02;

    @FXML
    private TextField personalinfo03;

    @FXML
    private TextField personalinfo04;

    @FXML
    private TextField personalinfo05;

    @FXML
    private TextField personalinfo06;

    @FXML
    private DatePicker personalinfo07;

    @FXML
    private TextField personalinfo08;

    @FXML
    private TextField personalinfo11;

    @FXML
    private TextField personalinfo12;

    @FXML
    private ComboBox personalinfo09;

    @FXML
    private ComboBox personalinfo10;

    @FXML
    private TextField personalinfo01;

    @FXML
    private Tab tabIndex02;

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
    private CheckBox cbAddress04;

    @FXML
    private Button btnAddAddress;

    @FXML
    private Button btnDelAddress;

    @FXML
    private CheckBox cbAddress03;

    @FXML
    private CheckBox cbAddress06;

    @FXML
    private CheckBox cbAddress07;

    @FXML
    private CheckBox cbAddress08;

    @FXML
    private CheckBox cbAddress02;

    @FXML
    private CheckBox cbAddress05;

    @FXML
    private CheckBox cbAddress01;

    @FXML
    private Label lblAddressStat;

    @FXML
    private Tab tabIndex03;

    @FXML
    private AnchorPane anchorMobile;

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
    private TextField txtMobile01;

    @FXML
    private ComboBox cmbMobile02;

    @FXML
    private ComboBox cmbMobile01;

    @FXML
    private CheckBox cbMobileNo01;

    @FXML
    private CheckBox cbMobileNo02;

    @FXML
    private Button btnAddMobile;

    @FXML
    private Button btnDelMobile;

    @FXML
    private Label lblMobileStat;

    @FXML
    private Tab tabIndex04;

    @FXML
    private AnchorPane anchorEmail;

    @FXML
    private TableView tblEmail;

    @FXML
    private TableColumn indexEmail01;

    @FXML
    private TableColumn indexEmail02;

    @FXML
    private TableColumn indexEmail03;

    @FXML
    private TextField mailFields01;

    @FXML
    private ComboBox cmbEmail01;

    @FXML
    private Button btnAddEmail;

    @FXML
    private Button btnDelEmail;

    @FXML
    private Label lblEmailStat;

    @FXML
    private CheckBox cbEmail02;

    @FXML
    private CheckBox cbEmail01;

    @FXML
    private Tab tabIndex05;

    @FXML
    private AnchorPane anchorSocial;

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
    private TextField txtSocial01;

    @FXML
    private TextArea txtSocial02;

    @FXML
    private ComboBox cmbSocMed01;

    @FXML
    private Button btnAddSocMed;

    @FXML
    private Button btnDelSocMed;

    @FXML
    private CheckBox cbSocMed01;

    @FXML
    private Label lblSocMedStat;

    @FXML
    private Tab tabIndex06;

    @FXML
    private AnchorPane anchorContctPerson;

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
    private TextField txtContact04;

    @FXML
    private TextField txtContact05;

    @FXML
    private TextField txtContact09;

    @FXML
    private TextField txtContact06;

    @FXML
    private TextField txtContact07;

    @FXML
    private TextField txtContact08;

    @FXML
    private TextArea txtContact10;

    @FXML
    private Button btnAddInsContact;

    @FXML
    private Button btnDelContPerson;

    @FXML
    private CheckBox cbContact01;

    @FXML
    private Label lblContactPersonStat;

    @FXML
    private CheckBox cbContact02;

    @FXML
    private Tab tabIndex07;

    @FXML
    private AnchorPane anchorOtherInfo;

    @FXML
    private TextField personalinfo13;

    @FXML
    private TextField personalinfo14;

    @FXML
    private TextField personalinfo15;

    @FXML
    private ComboBox cmbSearch;

    @FXML
    private TextField txtSeeks99;
    
    private ObservableList<ModelMobile> data = FXCollections.observableArrayList();
    private ObservableList<ModelEmail> email_data = FXCollections.observableArrayList();
    private ObservableList<ModelSocialMedia> social_data = FXCollections.observableArrayList();    
    private ObservableList<ModelAddress> address_data = FXCollections.observableArrayList();    
    private ObservableList<ModelInstitutionalContactPerson> contact_data = FXCollections.observableArrayList();
    
    ObservableList<String> mobileType = FXCollections.observableArrayList("Mobile No", "Tel No", "Fax No");
    ObservableList<String> mobileOwn = FXCollections.observableArrayList("Personal", "Office", "Others");
    ObservableList<String> EmailOwn = FXCollections.observableArrayList("Personal", "Office", "Others");
    ObservableList<String> socialTyp = FXCollections.observableArrayList("Facebook", "Instagram", "Twitter");
    
    // Create a list of genders
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
        
        
          // Create a list of clientType    
        ObservableList<String> clientType = FXCollections.observableArrayList(
            "Company",
            "Individual"
        );

    private int pnMobile = 0;
    private int pnEmail = 0;
    private int pnSocMed = 0;
    private int pnAddress = 0;    
    private int pnContact = 0;
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
            pnEditMode = EditMode.UNKNOWN;
            initButton(pnEditMode);
        }
        ClickButton();

        // Initialize the Client_Master transaction
        oTrans = new Client_Master(oApp, false, oApp.getBranchCode());
        oTrans.setType(ValidatorFactory.ClientTypes.PARAMETER);
        oTrans.setClientType("0");
        pnEditMode = EditMode.UNKNOWN;
        initTabAnchor();
        initButton(pnEditMode);
        // Call newRecord to initialize a new record
//        oTrans.newRecord();

        // Access sClientID directly from the jsonResult and set it to txtField01
//        String sClientID = (String) oTrans.getMaster("sClientID");
//        if (txtField01 != null) { // Check if txtField01 is not null before setting its text
//            txtField01.setText(sClientID);
//        } else {
//            // Handle the case where txtField01 is null
//            System.out.println("txtField01 is null");
//        }

        initTextFields();
        InitPersonalInfo();
        initAddressInfo();
        InitMobileInfo();
        InitEmailInfo( );        
        InitContctPersonInfo();
        InitSocMedInfo();
        initComboBoxes();
        loadDetail();
        initClientType();
//        LocalDate currentDate = LocalDate.now();
//        personalinfo07.setValue(currentDate);
        pbLoaded = true;
    
}
    private void searchinfo_KeyPressed(KeyEvent event){
        TextField personalinfo = (TextField)event.getSource();
        int lnIndex = Integer.parseInt(((TextField)event.getSource()).getId().substring(8,10));
        String lsValue = personalinfo.getText();
        JSONObject poJson;
        switch (event.getCode()) {
            case F3:
                switch (lnIndex){
                    case 99: /*search branch*/
                        oTrans.setClientType(String.valueOf(cmbSearch.getSelectionModel().getSelectedIndex()));
                        poJson = oTrans.searchRecord(lsValue, false);
                        if ("error".equals((String) poJson.get("result"))){
                            ShowMessageFX.Information((String) poJson.get("message"), "Computerized Acounting System", pxeModuleName);
                           
                            txtSeeks99.clear();
                            break;
                        }
                        pnEditMode = oTrans.getEditMode();
                        loadDetail();
                        
//                        retrieveDetails();
                        break;
                }
            case ENTER:
                
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
                                txtField09.clear();
                           }
                           
                           personalinfo06.setText((String) oTrans.getMaster(28));
                           txtField09.setText((String) oTrans.getMaster(28));
                        break;
                    case 8: /*search branch*/
                        poJson = new JSONObject();
                           poJson =  oTrans.searchBirthPlce(lsValue, false);
                           System.out.println("poJson = " + poJson.toJSONString());
                           if("error".equalsIgnoreCase(poJson.get("result").toString())){
                               
                                ShowMessageFX.Information((String) poJson.get("message"), "Computerized Acounting System", pxeModuleName);
                                personalinfo08.clear();
                                txtField08.clear();
                           }
                           personalinfo08.setText((String) oTrans.getMaster(27));
                           txtField08.setText((String) oTrans.getMaster(27));
                           
                        break;
                }
            case ENTER:
                
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
    //                           AddressField03.setText((String) oTrans.getAddress(pnAddress,"sTownName") + ", " + (String) oTrans.getAddress(pnAddress,"sProvName"));
    //                           AddressField03.setText(oTrans.getAddress(pnAddress).getTownName());

                        AddressField03.setText((String)oTrans.getAddress(pnAddress, 20) + ", " + (String) oTrans.getAddress(pnAddress, 22));
                        AddressField04.setText((String)oTrans.getAddress(pnAddress, 21));
                        break;
                    case 4: /*search branch*/
                        poJson = new JSONObject();
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
    private void InitMobileInfo(){
        /*MOBILE INFO FOCUSED PROPERTY*/
        txtMobile01.focusedProperty().addListener(mobileinfo_Focus);
        CommonUtils.addTextLimiter(txtMobile01, 11);
        
        cmbMobile01.setItems(mobileOwn);
        cmbMobile01.getSelectionModel().select(0);
        
        cmbMobile02.setItems(mobileType);
        cmbMobile02.getSelectionModel().select(0);
        
        if((pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE)){
            oTrans.setMobile(pnMobile, "cOwnerxxx", cmbMobile01.getSelectionModel().getSelectedIndex());
            oTrans.setMobile(pnMobile, "cMobileTp", cmbMobile02.getSelectionModel().getSelectedIndex());
        }
        
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
        
        if((pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE)){
            oTrans.setEmail(pnEmail, "cOwnerxxx", cmbEmail01.getSelectionModel().getSelectedIndex());
        }
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
        
        if((pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE)){
            oTrans.setSocialMed(pnSocMed, "cSocialTp", cmbSocMed01.getSelectionModel().getSelectedIndex());
        }
        
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
//        personalinfo06.
        
        /*PERSONAL INFO KEYPRESSED*/
        personalinfo08.setOnKeyPressed(this::personalinfo_KeyPressed);
        personalinfo06.setOnKeyPressed(this::personalinfo_KeyPressed);
         // Set a custom StringConverter to format date
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
//        personalinfo07.setConverter(new StringConverter<LocalDate>() {
//            @Override
//            public String toString(LocalDate date) {
//                if (date != null) {
//                    oTrans.setMaster("dBirthDte", dateFormatter.format(date).toString());
//                    System.out.println("dBirthDte = " + date);
//                    
//                    txtField07.setValue(LocalDate.parse(date.format(dateFormatter), dateFormatter));
//                    return dateFormatter.format(date);
//                } else {
//                    return "";
//                }
//            }
//
//            @Override
//            public LocalDate fromString(String string) {
//                if (string != null && !string.isEmpty()) {
//                    oTrans.setMaster("dBirthDte",LocalDate.parse(string, dateFormatter).toString());
//                    
////                    txtField07.setValue(LocalDate.parse(string, dateFormatter));
//                    return LocalDate.parse(string, dateFormatter);
//                } else {
//                    return null;
//                }
//            }
//        });
        txtField07.setConverter(new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
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
    
    private void initTextFields(){
        /*textFields FOCUSED PROPERTY*/
        txtField01.focusedProperty().addListener(txtField_Focus);
        txtSeeks99.setOnKeyPressed(this::searchinfo_KeyPressed);
        
//        txtSeeks99.focusedProperty().addListener(txtField_Focus);
    }
    private void initClientType(){
        if(cmbField01.getSelectionModel().getSelectedIndex() == 0){
            tabIndex03.setDisable(true);
            tabIndex04.setDisable(true);
            tabIndex05.setDisable(true);
            tabIndex06.setDisable(false);
            
        }else{
            tabIndex06.setDisable(true);
            tabIndex03.setDisable(false);
            tabIndex04.setDisable(false);
            tabIndex05.setDisable(false);
        }
        Integer lsValue = cmbField01.getSelectionModel().getSelectedIndex();
        disablefields(lsValue);
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
        cmbField01.setDisable(!lbShow);

        txtSeeks99.setDisable(!lbShow);
        cmbSearch.setDisable(!lbShow);
        
        if (lbShow){
            txtSeeks99.setDisable(lbShow);
            txtSeeks99.clear();
            cmbSearch.setDisable(lbShow);
            
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
            txtSeeks99.setDisable(lbShow);
            txtSeeks99.requestFocus();
            cmbSearch.setDisable(lbShow);  
        }
        initClientType();
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
////        
////        txtSeeks21.setDisable(!lbShow);
////        txtSeeks22.setDisable(!lbShow);
//        
//        if (lbShow){
//            txtSeeks99.setDisable(lbShow);
//            txtSeeks99.clear();
//            cmbSearch.setDisable(lbShow);
//            
//            btnCancel.setVisible(lbShow);
//            btnSearch.setVisible(lbShow);
//            btnSave.setVisible(lbShow);
//            btnUpdate.setVisible(!lbShow);
//            btnBrowse.setVisible(!lbShow);
//            btnNew.setVisible(!lbShow);
//            btnBrowse.setManaged(!lbShow);
//            btnNew.setManaged(!lbShow);
//            btnUpdate.setManaged(!lbShow);
//        }
//        else{
////            txtSeeks21.setDisable(lbShow);
////            txtSeeks21.requestFocus();
////            txtSeeks22.setDisable(lbShow);  
//        }
    }
    private void ClickButton() {
        btnBrowse.setOnAction(this::handleButtonAction);
        btnCancel.setOnAction(this::handleButtonAction);
        btnNew.setOnAction(this::handleButtonAction);
        btnSave.setOnAction(this::handleButtonAction);
        btnUpdate.setOnAction(this::handleButtonAction);
        btnAddMobile.setOnAction(this::handleButtonAction);
        btnAddSocMed.setOnAction(this::handleButtonAction);      
        btnAddAddress.setOnAction(this::handleButtonAction);     
        btnAddEmail.setOnAction(this::handleButtonAction);        
        btnAddInsContact.setOnAction(this::handleButtonAction);
        btnClose.setOnAction(this::handleButtonAction);
        
        btnDelAddress.setOnAction(this::handleButtonAction);     
        btnDelMobile.setOnAction(this::handleButtonAction);        
        btnDelEmail.setOnAction(this::handleButtonAction);
        btnDelSocMed.setOnAction(this::handleButtonAction);
        btnDelContPerson.setOnAction(this::handleButtonAction);
    }
    
    private void handleButtonAction(ActionEvent event) {
        Object source = event.getSource();
        JSONObject poJSON;
        if (source instanceof Button) {
            Button clickedButton = (Button) source;
            unloadForm appUnload = new unloadForm();
            switch (clickedButton.getId()) {
                case "btnNew":
                        poJSON = oTrans.newRecord();
                        if ("success".equals((String) poJSON.get("result"))){
                            pnEditMode = EditMode.ADDNEW;
                            clearAllFields();
                            cmbField01.setDisable(false);
                            txtField01.setText((String) oTrans.getMaster("sClientID"));
                            loadDetail();
                            initTabAnchor();
                        }else{
                            ShowMessageFX.Information((String)poJSON.get("message"), "Computerized Acounting System", pxeModuleName);
                            System.out.println("Record not saved successfully.");
                            System.out.println((String) poJSON.get("message"));
                            
                            initTabAnchor();
                        }
                    break;
                case "btnBrowse":
                        oTrans.setClientType(String.valueOf(cmbSearch.getSelectionModel().getSelectedIndex()));
                        poJSON = oTrans.searchRecord(txtSeeks99.getText().toString(), false);
                        if ("error".equals((String) poJSON.get("result"))){
                            ShowMessageFX.Information((String) poJSON.get("message"), "Computerized Acounting System", pxeModuleName);
                           
                            txtSeeks99.clear();
                            break;
                        }
                        pnEditMode = EditMode.READY;
                        loadDetail();
                        
                    break;
                case "btnUpdate":
                        poJSON = oTrans.updateRecord();
                        if ("success".equals((String) poJSON.get("result"))){
                            pnEditMode = EditMode.UPDATE;
                            initTabAnchor();
                            cmbField01.setDisable(false);
                        }else{
                            ShowMessageFX.Information((String)poJSON.get("message"), "Computerized Acounting System", pxeModuleName);
                            System.out.println("Record not saved successfully.");
                            System.out.println((String) poJSON.get("message"));
                            
                            initTabAnchor();
                            
                            loadDetail();
                        }
                    break;
                case "btnCancel":
                        if (ShowMessageFX.YesNo("Do you really want to cancel this record? \nAny data collected will not be kept.", "Computerized Acounting System", pxeModuleName)){
                            clearAllFields();
                            
                            // Initialize the Client_Master transaction
                            oTrans = new Client_Master(oApp, false, oApp.getBranchCode());
                            oTrans.setType(ValidatorFactory.ClientTypes.PARAMETER);
                            oTrans.setClientType("0");
                            pnEditMode = EditMode.UNKNOWN;
                            initTabAnchor();
                        }
                    break;
                case "btnSave":
                    
                        if(!personalinfo01.getText().toString().isEmpty()){
                            oTrans.getModel().setFullName(personalinfo01.getText());
                        }
                        JSONObject saveResult = oTrans.saveRecord();
                        if ("success".equals((String) saveResult.get("result"))){
                            System.err.println((String) saveResult.get("message"));
                            ShowMessageFX.Information((String) saveResult.get("message"), "Computerized Acounting System", pxeModuleName);
                            clearAllFields();
                            pnEditMode = EditMode.READY;
                            initButton(pnEditMode);
                            initTabAnchor();
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
                        clearInsContctFields();
                        
                        loadContctPerson();
                        tblContact.getSelectionModel().select(pnContact + 1);
                     break;
                     
                case "btnAddAddress":
                        JSONObject addObjAddress = oTrans.addAddress();
                           System.out.println((String) addObjAddress.get("message"));
                       if ("error".equals((String) addObjAddress.get("result"))){
                           ShowMessageFX.Information((String) addObjAddress.get("message"), "Computerized Acounting System", pxeModuleName);
                           break;
                       } 
                       
                       
                       pnAddress = oTrans.getAddressList().size()-1;
                       clearAddressFields();

                       loadAddress();

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
                        pnEmail = oTrans.getEmailList().size()-1;
                        clearEmailFields();
                        loadEmail();
                        tblEmail.getSelectionModel().select(pnEmail + 1);
                     break;
                case "btnAddMobile":
                        JSONObject addObj = oTrans.addContact();
                        
                            System.out.println((String) addObj.get("message"));
                        if ("error".equals((String) addObj.get("result"))){
                            ShowMessageFX.Information((String) addObj.get("message"), "Computerized Acounting System", pxeModuleName);
                            break;
                        } 
                        pnMobile = oTrans.getMobileList().size()-1;
                        clearMobileFields();
                        loadMobile();
                        
                        tblMobile.getSelectionModel().select(pnMobile + 1);
                     break;
                case "btnAddSocMed":
                        JSONObject addSocMed = oTrans.addSocialMedia();
                        
                            System.out.println((String) addSocMed.get("message"));
                        if ("error".equals((String) addSocMed.get("result"))){
                            ShowMessageFX.Information((String) addSocMed.get("message"), "Computerized Acounting System", pxeModuleName);
                            break;
                        } 
                        pnSocMed = oTrans.getSocialMediaList().size()-1;
                        clearSocialFields();
                        
                        loadSocialMedia();
                        tblSocMed.getSelectionModel().select(pnSocMed + 1);
                     break;
                case "btnClose":
                        
                        if (ShowMessageFX.YesNo("Do you really want to close this record? \nAny data collected will not be kept.", "Computerized Acounting System", pxeModuleName)){
                            
                            appUnload.unloadForm(AnchorMain, oApp, "Client Parameter");
                        }
                     break;
                
                case "btnDelAddress":
                    if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Do you want to remove these details? ") == true){  
                        oTrans.getAddressList().remove(pnAddress);
                        if(oTrans.getAddressList().size() <= 0){
                            oTrans.addAddress();
                        }
                        
                        pnAddress = oTrans.getAddressList().size()-1;
                        loadAddress();
                        clearAddressFields();
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
                        clearMobileFields();
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
                        clearEmailFields();
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
                        clearSocialFields();
                    }
                     break; 
                case "btnDelContPerson":
                    if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Do you want to remove these details? ") == true){  
                        oTrans.getInsContactList().remove(pnContact);
                        if(oTrans.getInsContactList().size() <= 0){
                            oTrans.addInsContact();
                        }
                        
                        pnContact = oTrans.getInsContactList().size()-1;
                        loadContctPerson();
                        clearInsContctFields();
                    }
                    break;
                // Add more cases for other buttons if needed
            }
            
            initButton(pnEditMode);
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
//                    jsonObject = oTrans.setMaster( 8,lsValue);
                    
                    jsonObject = oTrans.getModel().setFullName(lsValue);
                    break;
                case 2:/*last name*/
//                    jsonObject = oTrans.setMaster( "sLastName",lsValue);
                    jsonObject = oTrans.getModel().setLastName(lsValue);
                    break;
                case 3:/*frist name*/
//                    jsonObject = oTrans.setMaster( 4,lsValue);
                    jsonObject = oTrans.getModel().setFirstName(lsValue);
                    break;
                case 4:/*middle name*/
//                    jsonObject = oTrans.setMaster( 5,lsValue);
                    jsonObject = oTrans.getModel().setMiddleName(lsValue);
                    break;
                case 5:/*suffix name*/
//                    jsonObject = oTrans.setMaster( 6,lsValue);
                    jsonObject = oTrans.getModel().setSuffixName(lsValue);
                    break;  
                case 11:/*spouse */
                    jsonObject = oTrans.setMaster( 15,lsValue);
                    break;
                case 12:/*mothers maiden namex */
                    jsonObject = oTrans.setMaster( 7,lsValue);
                    break;
                case 13:/*mothers maiden namex */
//                    jsonObject = oTrans.setMaster( "sTaxIDNox",lsValue);
                    jsonObject = oTrans.getModel().setTaxIDNumber(lsValue);
                    break;
                case 14:/*mothers maiden namex */
//                    jsonObject = oTrans.setMaster( "sLTOIDxxx",lsValue);
                    jsonObject = oTrans.getModel().setLTOIDNumber(lsValue);
                    break;
                case 15 :/*mothers maiden namex */
//                    jsonObject = oTrans.setMaster( "sPHBNIDxx",lsValue);
                    jsonObject = oTrans.getModel().setPHNationalIDNumber(lsValue);
                    break;
            }
            if (cmbField01.getSelectionModel().getSelectedIndex() == 1){
                personalinfo01.setText(personalinfo02.getText() + "," + personalinfo03.getText() + " " + personalinfo05.getText() + " " + personalinfo04.getText());
                txtField02.setText(personalinfo01.getText());
                txtField06.setText(personalinfo12.getText());
            }
            
            
                    
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
                    oTrans.setAddress(pnAddress, "sHouseNox", lsValue);
                    break;
                case 2:/*address*/
                    oTrans.setAddress(pnAddress, "sAddressx", lsValue);
                    break;
//                case 3:/*Province*/
//                    oTrans.setAddress(pnAddress, "cProvince", lsValue);
//                    break;
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
                    
                    System.out.println(pnMobile);
                    
//                    CommonUtils.fixMobileNo(lsValue);
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
    
    
    private void initComboBoxes(){
        
        // Set the items of the ComboBox to the list of genders
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
        
        txtField12.setItems(civilStatuses);
        txtField13.setItems(genders);
        
        txtField12.getSelectionModel().select(0);
        txtField13.getSelectionModel().select(0);
        
        cmbSearch.setItems(clientType);
        cmbSearch.getSelectionModel().select(0);
        cmbField01.setItems(clientType);
        cmbField01.getSelectionModel().select(0);
        
        cmbField01.setOnAction(event -> {
            oTrans.setMaster("cClientTp", cmbField01.getSelectionModel().getSelectedIndex());
            initClientType();
            
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
        if(oTrans.getMobileList() != null){
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
        
       
    }
    private void loadEmail(){
        email_data.clear();
        if(oTrans.getEmailList() != null){
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
    //            System.out.println("index = " + oTrans.getMobile(lnCtr, "cOwnerxxx").toString());
    //            System.out.println("value = " + mobileOwn.get(Integer.parseInt(oTrans.getMobile(lnCtr, "cOwnerxxx").toString())));
            }
        }
       
    }
    private void loadSocialMedia(){
        social_data.clear();
        if(oTrans.getSocialMediaList() != null){
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
        
       
    }
    private void initMobileGrid() {
        indexMobileNo01.setStyle("-fx-alignment: CENTER;");
        indexMobileNo02.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        indexMobileNo03.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        indexMobileNo04.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
//        
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
//        tblMobile.getSelectionModel().select(pnMobile + 1);
        tblMobile.autosize();
    }
    
    private void initEmailGrid() {
        indexEmail01.setStyle("-fx-alignment: CENTER;");
        indexEmail02.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        indexEmail03.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
//        indexMobileNo04.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
//        index06.setStyle("-fx-alignment: CENTER-RIGHT;-fx-padding: 0 5 0 0;");
//        index07.setStyle("-fx-alignment: CENTER-RIGHT;-fx-padding: 0 5 0 0;");
//        index08.setStyle("-fx-alignment: CENTER-RIGHT;-fx-padding: 0 5 0 0;");
//        
        indexEmail01.setCellValueFactory(new PropertyValueFactory<>("index01"));
        indexEmail02.setCellValueFactory(new PropertyValueFactory<>("index02"));
        indexEmail03.setCellValueFactory(new PropertyValueFactory<>("index03"));
//        indexMobileNo04.setCellValueFactory(new PropertyValueFactory<>("index04"));
        tblEmail.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblEmail.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });
        tblEmail.setItems(email_data);
//        tblMobile.getSelectionModel().select(pnMobile + 1);
        tblEmail.autosize();
    }
    
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
    private void initSocialMediaGrid() {
        indexSocMed01.setStyle("-fx-alignment: CENTER;");
        indexSocMed02.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        indexSocMed03.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        indexSocMed04.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
//        index06.setStyle("-fx-alignment: CENTER-RIGHT;-fx-padding: 0 5 0 0;");
//        index07.setStyle("-fx-alignment: CENTER-RIGHT;-fx-padding: 0 5 0 0;");
//        index08.setStyle("-fx-alignment: CENTER-RIGHT;-fx-padding: 0 5 0 0;");
//        
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
//        tblMobile.getSelectionModel().select(pnMobile + 1);
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
//        tableAddress.getSelectionModel().select(address_data + 1);
        tblAddress.getSelectionModel().select(pnAddress + 1);
        tblAddress.autosize();
       
    }
    
    private void loadContctPerson(){
        contact_data.clear();
        if(oTrans.getInsContactList() != null){
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
                    oTrans.getInsContact(lnCtr, "cPrimaryx").toString(),
                    oTrans.getInsContact(lnCtr, "cRecdStat").toString()));
            }
        }
        
       
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
    
    @FXML
    private void tblMobile_Clicked(MouseEvent event) {
        pnMobile = tblMobile.getSelectionModel().getSelectedIndex(); 
        if(pnMobile >= 0){
             getSelectedMobileItem();
             tblMobile.getSelectionModel().clearAndSelect(pnMobile);
        }
    }
    
    @FXML
    private void tblAddress_Clicked(MouseEvent event) {
        pnAddress = tblAddress.getSelectionModel().getSelectedIndex(); 
        if(pnAddress >= 0){
            getSelectedAddressItem();
            tblAddress.getSelectionModel().clearAndSelect(pnAddress);
        }
    }
    
    @FXML
    private void tblSocMed_Clicked(MouseEvent event) {
        pnSocMed = tblSocMed.getSelectionModel().getSelectedIndex(); 
        if(pnSocMed >= 0){
            getSelectedSocMedItem();
            tblSocMed.getSelectionModel().clearAndSelect(pnSocMed);
        }
    }
    @FXML
    private void tblEmail_Clicked(MouseEvent event) {
        pnEmail = tblEmail.getSelectionModel().getSelectedIndex(); 
        if(pnEmail >= 0){
            getSelectedEmailItem();
            tblEmail.getSelectionModel().clearAndSelect(pnEmail);
        }
    }
    
    @FXML
    private void tblContact_Clicked(MouseEvent event) {
        pnContact = tblContact.getSelectionModel().getSelectedIndex(); 
        if(pnContact >= 0){
            getSelectedInsContctItem();
        }
    }
    
    @FXML
    private void CheckPrimary_Clicked(MouseEvent event) {
        boolean isChecked = cbMobileNo01.isSelected();
//        oTrans.setMobile(pnMobile, "cPrimaryx", (isChecked)? "1":"0");

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
        oTrans.setMobile(pnMobile, "cRecdStat", (isChecked)? "1":"0");
        loadMobile();
        String val = (isChecked)? "1":"0";
        System.out.println("isChecked = " + val);
        System.out.println("value = " + oTrans.getMobile(pnMobile, "cRecdStat"));
        
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
//        oTrans.setEmail(pnEmail, "cPrimaryx", (isChecked)? "1":"0");

        for (int lnCtr = 0; lnCtr < oTrans.getEmailList().size(); lnCtr++){
            if(pnEmail == lnCtr){
                if(isChecked){
                    oTrans.setEmail(pnEmail, "cPrimaryx", "1");
                }else{
                    oTrans.setEmail(lnCtr, "cPrimaryx", "0");
                }
            }else{
                oTrans.setEmail(lnCtr, "cPrimaryx", "0");
            }
            
        }
        loadEmail();
        String val = (isChecked)? "1":"0";
        System.out.println("isChecked = " + val);
        System.out.println("value = " + oTrans.getEmail(pnMobile, "cPrimaryx"));
        
    }
    
    @FXML
    private void CheckMailStatus_Clicked(MouseEvent event) {
        boolean isChecked = cbEmail02.isSelected();
        oTrans.setEmail(pnEmail, "cRecdStat", (isChecked)? "1":"0");

        loadEmail();
        String val = (isChecked)? "1":"0";
        System.out.println("isChecked = " + val);
        System.out.println("value = " + oTrans.getEmail(pnMobile, "cRecdStat"));
        
    }
    @FXML
    private void CheckContact01_Clicked(MouseEvent event) {
        boolean isChecked = cbContact01.isSelected();
//        oTrans.setInsContact(pnContact, "cPrimaryx", (isChecked)? "1":"0");
        
        for (int lnCtr = 0; lnCtr < oTrans.getInsContactList().size(); lnCtr++){
            if(pnContact == lnCtr){
                if(isChecked){
                    oTrans.setInsContact(pnContact, "cRecdStat", "1");
                    System.out.println("value = " + oTrans.getInsContact(pnContact, "cRecdStat"));
                }else{
                    oTrans.setInsContact(lnCtr, "cRecdStat", "0");
                    System.out.println("value = " + oTrans.getInsContact(pnContact, "cRecdStat"));
                }
            }else{
                oTrans.setInsContact(lnCtr, "cRecdStat", "0");
                System.out.println("value = " + oTrans.getInsContact(pnContact, "cRecdStat"));
            }
            
        }
        loadContctPerson();
//        String val = (isChecked)? "1":"0";
//        System.out.println("isChecked = " + val);
//        System.out.println("value = " + oTrans.getInsContact(pnContact, "cRecdStat"));
        
    }
    @FXML
    private void CheckContact02_Clicked(MouseEvent event) {
        boolean isChecked = cbContact02.isSelected();
       
        for (int lnCtr = 0; lnCtr < oTrans.getInsContactList().size(); lnCtr++){
            if(pnContact == lnCtr){
                if(isChecked){
                    oTrans.setInsContact(pnContact, "cPrimaryx", "1");
                    System.out.println("cPrimaryx = " + oTrans.getInsContact(pnContact, "cPrimaryx"));
                }else{
                    oTrans.setInsContact(lnCtr, "cPrimaryx", "0");
                    System.out.println("cPrimaryx = " + oTrans.getInsContact(pnContact, "cPrimaryx"));
                }
            }else{
                oTrans.setInsContact(lnCtr, "cPrimaryx", "0");
                System.out.println("cPrimaryx = " + oTrans.getInsContact(pnContact, "cPrimaryx"));
            }
            
        }
        loadContctPerson();
        
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
       
//        for (int lnCtr = 0; lnCtr < oTrans.getAddressList().size(); lnCtr++){
//            if(isChecked){
//                oTrans.setAddress(pnAddress, "cPrimaryx", "1");
//            }else{
//                oTrans.setAddress(lnCtr, "cPrimaryx", "0");
//            }
//        }
        loadAddress();
    }
    
    @FXML
    private void cbAddress03_Clicked(MouseEvent event) {
        boolean isChecked = cbAddress03.isSelected();
        oTrans.setAddress(pnAddress, "cOfficexx", (isChecked)? "1":"0");
    }
    @FXML
    private void cbAddress04_Clicked(MouseEvent event) {
        boolean isChecked = cbAddress04.isSelected();
        oTrans.setAddress(pnAddress, "cBillingx", (isChecked)? "1":"0");
    }
    
    @FXML
    private void cbAddress05_Clicked(MouseEvent event) {
        boolean isChecked = cbAddress05.isSelected();
        oTrans.setAddress(pnAddress, "cShipping", (isChecked)? "1":"0");
    }
    
    @FXML
    private void cbAddress06_Clicked(MouseEvent event) {
        boolean isChecked = cbAddress06.isSelected();
        oTrans.setAddress(pnAddress, "cProvince", (isChecked)? "1":"0");
    }
    
    @FXML
    private void cbAddress07_Clicked(MouseEvent event) {
        boolean isChecked = cbAddress07.isSelected();
        oTrans.setAddress(pnAddress, "cCurrentx", (isChecked)? "1":"0");
    }
    
    @FXML
    private void cbAddress08_Clicked(MouseEvent event) {
        boolean isChecked = cbAddress08.isSelected();
        oTrans.setAddress(pnAddress, "cLTMSAddx", (isChecked)? "1":"0");
    }
    
    
    
    private void clearAllFields() {
        // Arrays of TextFields grouped by sections
        TextField[][] allFields = {
            // Text fields related to specific sections
            {txtSeeks99, txtField01, txtField02, txtField03, txtField04,
             txtField05, txtField06, txtField10, txtField08, txtField11, txtField09},

            {personalinfo02, personalinfo03, personalinfo04, personalinfo05,
             personalinfo12, personalinfo13, personalinfo14, personalinfo15,
             personalinfo06, personalinfo08, personalinfo11, personalinfo01},
            
        };
        txtField07.setValue(null);
        personalinfo07.setValue(null);
        personalinfo09.getSelectionModel().select(0);
        personalinfo10.getSelectionModel().select(0);
        txtField12.getSelectionModel().select(0);
        txtField13.getSelectionModel().select(0);
        cmbSearch.getSelectionModel().select(0);
        cmbField01.getSelectionModel().select(0);
        cmbField01.setDisable(true);
        

        // Loop through each array of TextFields and clear them
        for (TextField[] fields : allFields) {
            for (TextField field : fields) {
                field.clear();
            }
        }
        
        pnAddress = 0;
        pnMobile = 0;
        pnEmail = 0;
        pnSocMed = 0;
        pnContact = 0;
        clearAddressFields();
        clearMobileFields();
        clearEmailFields();
        clearSocialFields();
        clearInsContctFields();
        data.clear();
        email_data.clear();
        social_data.clear();
        address_data.clear();
        contact_data.clear();

    }
    private void clearInsContctFields(){
        TextField[] fields = { txtSocial01, txtContact01, txtContact02,
             txtContact03, txtContact04, txtContact05, txtContact06, txtContact07,
             txtContact08, txtContact09
        };

        // Loop through each array of TextFields and clear them
        for (TextField field : fields) {
            field.clear();
        }
        txtContact10.clear();
        if(oTrans.getInsContactList() != null && !oTrans.getInsContactList().isEmpty()){
            cbContact01.setSelected(("1".equals((String) oTrans.getInsContact(pnContact, "cRecdStat"))));
            cbContact02.setSelected(("1".equals((String) oTrans.getInsContact(pnContact, "cPrimaryx"))));
        }
    }

    private void clearSocialFields(){
        txtSocial01.clear();
        txtSocial02.clear();
        cmbSocMed01.getSelectionModel().select(0);
        if(oTrans.getSocialMediaList() != null && !oTrans.getSocialMediaList().isEmpty()){
            cbSocMed01.setSelected(("1".equals((String) oTrans.getSocialMed(pnSocMed, "cRecdStat"))));
        }
    }
    
    private void clearMobileFields(){
        cmbMobile01.getSelectionModel().select(0);
        cmbMobile02.getSelectionModel().select(0);
        txtMobile01.clear();    
        
        if(oTrans.getMobileList() != null &&!oTrans.getMobileList().isEmpty()){
            cbMobileNo02.setSelected(("1".equals((String) oTrans.getMobile(pnMobile, "cRecdStat"))));
            cbMobileNo01.setSelected(("1".equals((String) oTrans.getMobile(pnMobile, "cPrimaryx"))));
        }
    }
    
    private void clearEmailFields(){
        cmbEmail01.getSelectionModel().select(0);
        mailFields01.clear();    
        
        if(oTrans.getEmailList() != null &&!oTrans.getEmailList().isEmpty()){
            cbEmail02.setSelected(("1".equals((String) oTrans.getEmail(pnEmail, "cRecdStat"))));
            cbEmail01.setSelected(("1".equals((String) oTrans.getEmail(pnEmail, "cPrimaryx"))));
        }
    }
    
    private void clearAddressFields(){
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
        
        if(oTrans.getAddressList() != null && !oTrans.getAddressList().isEmpty()){
            cbAddress01.setSelected(("1".equals((String) oTrans.getAddress(pnAddress, "cRecdStat"))));
            cbAddress02.setSelected(("1".equals((String) oTrans.getAddress(pnAddress, "cPrimaryx"))));
        }

    }

    private void retrieveDetails(){
        if(pnEditMode == EditMode.READY || 
                pnEditMode == EditMode.ADDNEW || 
                pnEditMode == EditMode.UPDATE){
            personalinfo01.setText((String) oTrans.getModel().getFullName());
            personalinfo02.setText((String) oTrans.getModel().getLastName());
            personalinfo03.setText((String) oTrans.getModel().getFirstName());
            personalinfo04.setText((String) oTrans.getModel().getMiddleName());
            personalinfo05.setText((String) oTrans.getModel().getSuffixName());
            personalinfo12.setText((String) oTrans.getModel().getMaidenName());
            personalinfo13.setText((String) oTrans.getModel().getTaxIDNumber());
            personalinfo14.setText((String) oTrans.getModel().getLTOIDNumber());
            personalinfo15.setText((String) oTrans.getModel().getPHNationalIDNumber());
            personalinfo06.setText((String) oTrans.getMaster(28));
            personalinfo08.setText((String) oTrans.getMaster(27));
            
            personalinfo11.setText((oTrans.getMaster(29) == null)? "" : (String) oTrans.getMaster(29));
            if(oTrans.getModel().getSex() != null && !oTrans.getModel().getSex().trim().isEmpty()){
                personalinfo09.getSelectionModel().select(Integer.parseInt((String) oTrans.getModel().getSex()));
                txtField13.getSelectionModel().select(personalinfo09.getSelectionModel().getSelectedIndex());
            }
            if(oTrans.getModel().getCivilStatus() != null && !oTrans.getModel().getCivilStatus().trim().isEmpty()){
                personalinfo10.getSelectionModel().select(Integer.parseInt((String) oTrans.getModel().getCivilStatus()));
                txtField12.getSelectionModel().select(personalinfo10.getSelectionModel().getSelectedIndex());
            }

            
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            // Parse the formatted date string into a LocalDate object
            if(oTrans.getMaster(12) != null && !oTrans.getMaster(12).toString().trim().isEmpty()){
                LocalDate localbdate = LocalDate.parse(oTrans.getMaster(12).toString(), formatter);

                // Set the value of the DatePicker to the parsed LocalDate
                personalinfo07.setValue(localbdate);
                txtField07.setValue(localbdate);
            }

            txtField01.setText((String) oTrans.getModel().getClientID());
            txtField02.setText((String) oTrans.getModel().getFullName());
            txtField06.setText((oTrans.getMaster(29) == null)? "" : (String) oTrans.getMaster(29));
            txtField05.setText((String) oTrans.getModel().getMaidenName());
            txtField09.setText((String) oTrans.getMaster(28));
            txtField08.setText((String) oTrans.getMaster(27));
            if(!oTrans.getModel().getClientType().trim().isEmpty() && oTrans.getModel().getClientType()!= null){
                cmbField01.getSelectionModel().select(Integer.parseInt((String) oTrans.getModel().getClientType()));
            }
            if(!address_data.isEmpty()){
                for(int lnctr = 0; lnctr < oTrans.getAddressList().size(); lnctr++){    
                    if(oTrans.getAddress(lnctr, "cPrimaryx").equals("1")){
                        String lsAddress = oTrans.getAddress(lnctr).getHouseNo() + " " + oTrans.getAddress(lnctr).getAddress() +
                                " " + (String) oTrans.getAddress(lnctr, 21) + ", " + (String)  oTrans.getAddress(lnctr, 20)+ ", " + (String)  oTrans.getAddress(lnctr, 22);
                        txtField03.setText(lsAddress);
                    }
                }
            }
            
            if(!data.isEmpty()){
                for(int lnctr = 0; lnctr < oTrans.getMobileList().size(); lnctr++){
                    if(oTrans.getMobile(lnctr, "cPrimaryx").equals("1")){
                        txtField10.setText((String) oTrans.getMobile(lnctr, "sMobileNo"));
                    }
                }
            }
            
            if(!email_data.isEmpty()){
                for(int lnctr = 0; lnctr < oTrans.getEmailList().size(); lnctr++){
                    if(oTrans.getEmail(lnctr, "cPrimaryx").equals("1")){
                        txtField11.setText((String) oTrans.getEmail(lnctr, "sEMailAdd"));
                    }
                }
            }
            
            if(!contact_data.isEmpty()){
                for(int lnctr = 0; lnctr < oTrans.getInsContactList().size(); lnctr++){
                    if(oTrans.getInsContact(lnctr, "cPrimaryx").equals("1")){
                        txtField04.setText((String) oTrans.getInsContact(lnctr, "sCPerson1"));
                    }
                }
            }
            
            
        }
         
    }

    private void loadDetail(){
        loadAddress();
        loadMobile();
        loadEmail();
        loadContctPerson();
        initContctPersonGrid();
        initMobileGrid();
        initEmailGrid();
        initAddressGrid();
        loadSocialMedia();
        initSocialMediaGrid();
        
        retrieveDetails();
        personalinfo07.setValue(LocalDate.now());
    }
    private void initTabAnchor(){
        boolean pbValue = pnEditMode == EditMode.ADDNEW || 
                pnEditMode == EditMode.UPDATE;
        anchorPersonal.setDisable(!pbValue);
        anchorAddress.setDisable(!pbValue);
        anchorMobile.setDisable(!pbValue);
        anchorEmail.setDisable(!pbValue);
        anchorSocial.setDisable(!pbValue);
        anchorContctPerson.setDisable(!pbValue);
        anchorOtherInfo.setDisable(!pbValue);
    
    }
    
    private void getSelectedAddressItem(){
       
        if(!oTrans.getAddressList().isEmpty()){    
            AddressField01.setText((String) oTrans.getAddress(pnAddress, "sHouseNox"));
            AddressField02.setText((String) oTrans.getAddress(pnAddress, "sAddressx"));
            AddressField03.setText((String)  oTrans.getAddress(pnAddress, 20)+ ", " + (String)  oTrans.getAddress(pnAddress, 22));
            AddressField04.setText((String) oTrans.getAddress(pnAddress, 21));
            AddressField05.setText(oTrans.getAddress(pnAddress, "nLatitude").toString());
            AddressField06.setText(oTrans.getAddress(pnAddress, "nLongitud").toString());
            
            cbAddress01.setSelected(("1".equals((String) oTrans.getAddress(pnAddress, "cRecdStat"))));
            cbAddress02.setSelected(("1".equals((String) oTrans.getAddress(pnAddress, "cPrimaryx"))));
            cbAddress03.setSelected(("1".equals((String) oTrans.getAddress(pnAddress, "cOfficexx"))));
            cbAddress04.setSelected(("1".equals((String) oTrans.getAddress(pnAddress, "cBillingx"))));
            cbAddress05.setSelected(("1".equals((String) oTrans.getAddress(pnAddress, "cShipping"))));
            cbAddress06.setSelected(("1".equals((String) oTrans.getAddress(pnAddress, "cProvince"))));
            cbAddress07.setSelected(("1".equals((String) oTrans.getAddress(pnAddress, "cCurrentx"))));
            cbAddress08.setSelected(("1".equals((String) oTrans.getAddress(pnAddress, "cLTMSAddx"))));
        }
    }
    
    private void getSelectedMobileItem(){
        if(!oTrans.getMobileList().isEmpty()){    
            txtMobile01.setText((String) oTrans.getMobile(pnMobile, "sMobileNo"));
            
            if(oTrans.getMobile(pnMobile, "cOwnerxxx") != null && !oTrans.getMobile(pnMobile, "cOwnerxxx").toString().trim().isEmpty()){
                cmbMobile01.getSelectionModel().select(Integer.parseInt(oTrans.getMobile(pnMobile, "cOwnerxxx").toString()));
            }
            
            if(oTrans.getMobile(pnMobile, "cMobileTp") != null && !oTrans.getMobile(pnMobile, "cMobileTp").toString().trim().isEmpty()){
                System.out.println("cMobileTp = " + oTrans.getMobile(pnMobile, "cMobileTp"));
                cmbMobile02.getSelectionModel().select(Integer.parseInt(oTrans.getMobile(pnMobile, "cMobileTp").toString()));
            }     
            cbMobileNo02.setSelected(("1".equals((String) oTrans.getMobile(pnMobile, "cRecdStat"))));
            cbMobileNo01.setSelected(("1".equals((String) oTrans.getMobile(pnMobile, "cPrimaryx"))));
        }
    }
    private void getSelectedEmailItem(){
        if(!oTrans.getEmailList().isEmpty()){    
            mailFields01.setText((String) oTrans.getEmail(pnEmail, "sEMailAdd"));
            if(oTrans.getEmail(pnEmail, "cOwnerxxx") != null &&  !oTrans.getEmail(pnEmail, "cOwnerxxx").toString().isEmpty()){
                cmbEmail01.getSelectionModel().select(Integer.parseInt(oTrans.getEmail(pnEmail, "cOwnerxxx").toString()));
            }
            cbEmail02.setSelected(("1".equals((String) oTrans.getEmail(pnEmail, "cRecdStat"))));
            cbEmail01.setSelected(("1".equals((String) oTrans.getEmail(pnEmail, "cPrimaryx"))));
        }
    }
    
    private void getSelectedSocMedItem(){
        if(!oTrans.getSocialMediaList().isEmpty()){    
            txtSocial01.setText((String) oTrans.getSocialMed(pnSocMed, "sAccountx"));
            txtSocial02.setText((String) oTrans.getSocialMed(pnSocMed, "sRemarksx"));
            if(oTrans.getSocialMed(pnSocMed, "cSocialTp") != null &&  !oTrans.getSocialMed(pnSocMed, "cSocialTp").toString().isEmpty()){
                cmbSocMed01.getSelectionModel().select(Integer.parseInt(oTrans.getSocialMed(pnSocMed, "cSocialTp").toString()));
            }
            cbSocMed01.setSelected(("1".equals((String) oTrans.getSocialMed(pnSocMed, "cRecdStat"))));
        }
    }
    private void getSelectedInsContctItem(){
        if(!oTrans.getInsContactList().isEmpty()){    
            txtContact01.setText((String) oTrans.getInsContact(pnContact, "sCPerson1"));
            txtContact02.setText((String) oTrans.getInsContact(pnContact, "sCPPosit1"));
            txtContact03.setText((String) oTrans.getInsContact(pnContact, "sAccount1"));
            txtContact04.setText((String) oTrans.getInsContact(pnContact, "sAccount2"));
            txtContact05.setText((String) oTrans.getInsContact(pnContact, "sAccount3"));
            txtContact06.setText((String) oTrans.getInsContact(pnContact, "sMobileNo"));
            txtContact07.setText((String) oTrans.getInsContact(pnContact, "sTelNoxxx"));
            txtContact08.setText((String) oTrans.getInsContact(pnContact, "sFaxNoxxx"));
            txtContact09.setText((String) oTrans.getInsContact(pnContact, "sEMailAdd"));
            txtContact10.setText((String) oTrans.getInsContact(pnContact, "sRemarksx"));
            
            cbContact01.setSelected(("1".equals((String) oTrans.getInsContact(pnContact, "cRecdStat"))));
            cbContact02.setSelected(("1".equals((String) oTrans.getInsContact(pnContact, "cPrimaryx"))));
        }
    }

    void loadReturn(String lsValue) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    private void disablefields(int fsValue) {
        boolean lbShow = (fsValue == 0);
        
        // Arrays of TextFields grouped by sections
        TextField[][] allFields = {
            // Text fields related to specific sections
            {personalinfo02, personalinfo03, personalinfo04, personalinfo05, personalinfo06, personalinfo08
            , personalinfo11, personalinfo12,AddressField05,AddressField06,AddressField05},
        };
        personalinfo09.setDisable(lbShow);
        personalinfo10.setDisable(lbShow);
        
        cbAddress03.setVisible(lbShow);
        cbAddress04.setVisible(lbShow);
        cbAddress05.setVisible(lbShow);
        cbAddress06.setVisible(lbShow);
        cbAddress07.setVisible(lbShow);
        cbAddress08.setVisible(lbShow);
        lblAddressType.setVisible(lbShow);
        // Loop through each array of TextFields and clear them
        for (TextField[] fields : allFields) {
            for (TextField field : fields) {
                field.setDisable(lbShow);
            }
        }
        personalinfo01.setDisable(!lbShow);
    }
}   
