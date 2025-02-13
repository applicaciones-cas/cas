package org.guanzon.cas.controller;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.sql.SQLException;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
import javax.sql.rowset.CachedRowSet;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.base.LogWrapper;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.cas.model.ModelInvSerialLedger;
import org.guanzon.cas.model.ModelListParameter;
import org.guanzon.cas.model.ModelResultSet;
import org.guanzon.cas.parameter.services.ParamControllers;
import org.json.simple.JSONObject;

public class LaborModelController implements Initializable, ScreenInterface {

    private GRider oApp;
    private final String pxeModuleName = "Labor Model";
    private int pnEditMode;
    private ParamControllers oParameters;
    private boolean state = false;
    private boolean pbLoaded = false;
    private int pnInventory = 0;
    private int pnRow = 0;
    private ObservableList<ModelListParameter> data = FXCollections.observableArrayList();
    private CachedRowSet cacheLaborList;

    private int pnIndex;
    private int pnListRow;
    String brand = "";
    @FXML
    private AnchorPane AnchorMain, AnchorInputs;
    @FXML
    private HBox hbButtons;

    @FXML
    private Button btnBrowse,
            btnNew,
            btnSave,
            btnCancel,
            btnActivate,
            btnClose;

    @FXML
    private FontAwesomeIconView faActivate;

    @FXML
    private TextField txtField01,
            txtField02,
            txtField03,
            txtField04,
            txtSeeks01;

    @FXML
    private CheckBox cbField01;

    @FXML
    private TableView tblList;

    @FXML
    private TableColumn index01, index02, index03;

    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        pnEditMode = EditMode.UNKNOWN;
        initButton(pnEditMode);
        initializeObject();
        InitTextFields();
        ClickButton();
        initTabAnchor();
        initTable();
        pbLoaded = true;
    }

    private void initializeObject() {
        LogWrapper logwrapr = new LogWrapper("CAS", System.getProperty("sys.default.path.temp") + "cas-error.log");
        oParameters = new ParamControllers(oApp, logwrapr);
        oParameters.LaborModel().setRecordStatus("0123");
        System.out.println("init1 == " + oParameters.LaborModel());
        System.out.println("init2 == " + oParameters.LaborModel().getModel().getLaborId());
    }

    private void ClickButton() {
        btnBrowse.setOnAction(this::handleButtonAction);
        btnNew.setOnAction(this::handleButtonAction);
        btnSave.setOnAction(this::handleButtonAction);
//        btnUpdate.setOnAction(this::handleButtonAction);
        btnCancel.setOnAction(this::handleButtonAction);
        btnActivate.setOnAction(this::handleButtonAction);
        btnClose.setOnAction(this::handleButtonAction);
    }

    private void handleButtonAction(ActionEvent event) {
        Object source = event.getSource();

        if (source instanceof Button) {
            Button clickedButton = (Button) source;
            unloadForm appUnload = new unloadForm();
            switch (clickedButton.getId()) {
                case "btnClose":
                    if (ShowMessageFX.YesNo("Do you really want to cancel this record? \nAny data collected will not be kept.", "Computerized Acounting System", pxeModuleName)) {
                        appUnload.unloadForm(AnchorMain, oApp, pxeModuleName);
                    }
                    break;
                case "btnNew":
                    clearAllFields();
                    txtField02.requestFocus();
                    JSONObject poJSON = oParameters.LaborModel().newRecord();
                    pnEditMode = EditMode.READY;
                    if ("success".equals((String) poJSON.get("result"))) {
                        pnEditMode = EditMode.ADDNEW;
                        initButton(pnEditMode);
                        initTabAnchor();

                        loadRecord();
                    } else {
                        ShowMessageFX.Information((String) poJSON.get("message"), "Computerized Acounting System", pxeModuleName);
                        initTabAnchor();
                    }
                    break;
                case "btnBrowse":
                    
                    String lsValue = (txtSeeks01.getText() == null) ? "" : txtSeeks01.getText();
                    poJSON = oParameters.Model().searchRecord(lsValue, false);
                            if ("error".equalsIgnoreCase(poJSON.get("result").toString())) {
                                ShowMessageFX.Information((String) poJSON.get("message"), "Computerized Acounting System", pxeModuleName);
                                break;
                            }
                     poJSON = oParameters.Brand().searchRecord(oParameters.Model().getModel().getBrandId(), true);
                            if ("error".equalsIgnoreCase(poJSON.get("result").toString())) {
                                ShowMessageFX.Information((String) poJSON.get("message"), "Computerized Acounting System", pxeModuleName);
                                break;
                            }
                            txtField01.setText((String) oParameters.Brand().getModel().getDescription());
                            oParameters.LaborModel().getModel().setModelId(oParameters.Model().getModel().getModelId());
                            txtField02.setText((String) oParameters.Model().getModel().getDescription());
                            oParameters.LaborModel().LaborList(oParameters.Model().getModel().getModelId());
                    pnEditMode = EditMode.UPDATE;
                    initButton(pnEditMode);
                    LoadList();
                    initTabAnchor(); 
                    break;
//                case "btnUpdate":
////                    poJSON = oParameters.LaborModel().updateRecord();
////                    if ("error".equals((String) poJSON.get("result"))) {
////                        ShowMessageFX.Information((String) poJSON.get("message"), "Computerized Acounting System", pxeModuleName);
////                        break;
////                    }
//                    pnEditMode = EditMode.UPDATE ;
//                    initButton(pnEditMode);
//                    initTabAnchor();
//                    break;
                case "btnCancel":
                    if (ShowMessageFX.YesNo("Do you really want to cancel this record? \nAny data collected will not be kept.", "Computerized Acounting System", pxeModuleName)) {
                        clearAllFields();
                        initializeObject();
                        pnEditMode = EditMode.UNKNOWN;
                        initButton(pnEditMode);
                        initTabAnchor();
                    }
                    break;
                case "btnSave":
                    System.out.println("model id == " + oParameters.Model().getModel().getModelId());
                    oParameters.LaborModel().getModel().setModelId(oParameters.Model().getModel().getModelId());
                    oParameters.LaborModel().getModel().setModifyingId(oApp.getUserID());
                    oParameters.LaborModel().getModel().setModifiedDate(oApp.getServerDate());
                    JSONObject saveResult = oParameters.LaborModel().saveRecord();
                    if ("success".equals((String) saveResult.get("result"))) {
                        ShowMessageFX.Information((String) saveResult.get("message"), "Computerized Acounting System", pxeModuleName);
                        pnEditMode = EditMode.UNKNOWN;
                        initButton(pnEditMode);
                        clearAllFields();
                    } else {
                        ShowMessageFX.Information((String) saveResult.get("message"), "Computerized Acounting System", pxeModuleName);
                    }
                    break;
                case "btnActivate":
                    String Status = oParameters.LaborModel().getModel().getRecordStatus();
                    JSONObject poJsON;

                    switch (Status) {
                        case "0":
                            if (ShowMessageFX.YesNo(null, pxeModuleName, "Do you want to Activate this Parameter?") == true) {
//                                poJsON = oParameters.LaborModel().postTransaction();
                                oParameters.LaborModel().getModel().setRecordStatus("1");
//                                ShowMessageFX.Information((String) poJsON.get("message"), "Computerized Accounting System", pxeModuleName);
                                loadRecord();
                            }
                            break;
                        case "1":
                            if (ShowMessageFX.YesNo(null, pxeModuleName, "Do you want to Deactivate this Parameter?") == true) {
//                                poJsON = oParameters.LaborModel().voidTransaction();
                                oParameters.LaborModel().getModel().setRecordStatus("0");
//                                ShowMessageFX.Information((String) poJsON.get("message"), "Computerized Accounting System", pxeModuleName);
                                loadRecord();
                            }
                            break;
                        default:

                            break;

                    }
                    break;
            }
        }
    }

    private void clearAllFields() {
        txtField01.clear();
        txtField02.clear();
        txtField03.clear();
        txtField04.clear();
        txtSeeks01.clear();
        data.clear();
    }

    private void initButton(int fnValue) {
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);

        btnCancel.setVisible(lbShow);
        btnCancel.setManaged(lbShow);
        btnSave.setVisible(lbShow);
        btnSave.setManaged(lbShow);
//        btnUpdate.setVisible(!lbShow);
//        btnUpdate.setManaged(!lbShow);

        btnBrowse.setVisible(!lbShow);
        btnBrowse.setManaged(!lbShow);
        btnNew.setVisible(!lbShow);
        btnNew.setManaged(!lbShow);

        btnClose.setVisible(true);
        btnClose.setManaged(true);
    }

    private void InitTextFields() {
        txtField01.focusedProperty().addListener(txtField_Focus);
        txtField02.focusedProperty().addListener(txtField_Focus);
        txtField03.focusedProperty().addListener(txtField_Focus);
        txtField01.setOnKeyPressed(this::txtField_KeyPressed);
        txtField02.setOnKeyPressed(this::txtField_KeyPressed);
    }

    private void txtField_KeyPressed(KeyEvent event) {
        TextField txtField = (TextField) event.getSource();
        int lnIndex = Integer.parseInt(((TextField) event.getSource()).getId().substring(8, 10));
        String lsValue = (txtField.getText() == null ? "" : txtField.getText());
        JSONObject poJson;
        poJson = new JSONObject();

        switch (event.getCode()) {
            case F3:
                switch (lnIndex) {
                    case 01:
                        poJson = oParameters.Brand().searchRecord(lsValue, false);
                        if ("error".equalsIgnoreCase(poJson.get("result").toString())) {
                            ShowMessageFX.Information((String) poJson.get("message"), "Computerized Acounting System", pxeModuleName);
                        }
                        clearAllFields();
                        txtField01.setText((String) oParameters.Brand().getModel().getDescription());
                        brand = oParameters.Brand().getModel().getBrandId();
                        txtField02.requestFocus();
                        
                        break;
                    case 02:
                        if (brand.isEmpty() || brand == null) {
                            poJson = oParameters.Model().searchRecord(lsValue, false);
                            if ("error".equalsIgnoreCase(poJson.get("result").toString())) {
                                ShowMessageFX.Information((String) poJson.get("message"), "Computerized Acounting System", pxeModuleName);
                                break;
                            }
                            oParameters.LaborModel().getModel().setModelId(oParameters.Model().getModel().getModelId());
                            txtField02.setText((String) oParameters.Model().getModel().getDescription());
                            oParameters.LaborModel().LaborList(oParameters.Model().getModel().getModelId());
                        } else {
                            poJson = oParameters.Model().searchRecordbyBrand(brand, true);
                            if ("error".equalsIgnoreCase(poJson.get("result").toString())) {
                                ShowMessageFX.Information((String) poJson.get("message"), "Computerized Acounting System", pxeModuleName);
                                break;
                            }
                            oParameters.LaborModel().getModel().setModelId(oParameters.Model().getModel().getModelId());
                            txtField02.setText((String) oParameters.Model().getModel().getDescription());
                            oParameters.LaborModel().LaborList(oParameters.Model().getModel().getModelId());
                        }
                        LoadList();
                        break;

                }
            case ENTER:
        }
        switch (event.getCode()) {
            case ENTER:
                CommonUtils.SetNextFocus(txtField);
            case DOWN:
                CommonUtils.SetNextFocus(txtField);
                break;
            case UP:
                CommonUtils.SetPreviousFocus(txtField);
        }
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
            try {
                switch (lnIndex) {
//                    case 1:
////                        oParameters.LaborModel().getModel().setLaborId(lsValue);
//                        break;
//                    case 2:
//                        oParameters.LaborModel().getModel().setLaborName(lsValue);
//                        break;
                    case 4:
                        double amount = Double.parseDouble(lsValue);
                        if (lnIndex == 3) {
                            oParameters.LaborModel().getModel().setAmount(amount);
                        }
                        txtField.setText(CommonUtils.NumberFormat(amount, "0.00"));
                        break;
                    default:
                        break;
                }
                txtField.selectAll();
            } catch (Exception e) {
                System.err.println("Error processing input: " + e.getMessage());
            }
        } else {
            txtField.selectAll();
        }
    };

    private void loadRecord() {
        boolean lbActive = oParameters.LaborModel().getModel().getRecordStatus() == "1";
//
//        txtField01.setText(oParameters.LaborModel().getModel().getLaborId());
//        txtField02.setText(oParameters.LaborModel().getModel().Model().getDescription());
        txtField03.setText(oParameters.LaborModel().getModel().Model().getDescription());
        txtField04.setText(CommonUtils.NumberFormat(oParameters.LaborModel().getModel().getAmount(), "#,##0.00"));
        switch (oParameters.LaborModel().getModel().getRecordStatus()) {
            case "1":
                btnActivate.setText("Deactivate");
                faActivate.setGlyphName("CLOSE");
                cbField01.setSelected(true);
                break;
            case "0":
                btnActivate.setText("Activate");
                faActivate.setGlyphName("CHECK");
                cbField01.setSelected(false);
                break;
        }
    }

    @FXML
    void cbField01_Clicked(MouseEvent event) {
        if (cbField01.isSelected()) {
            oParameters.LaborModel().getModel().setRecordStatus("1");
        } else {
            oParameters.LaborModel().getModel().setRecordStatus("0");
        }
    }

    private void initTabAnchor() {
        if (AnchorInputs == null) {
            System.err.println("Error: AnchorInput is not initialized.");
            return;
        }

        boolean isEditable = (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE);
        AnchorInputs.setDisable(!isEditable);
        if (pnEditMode == EditMode.UPDATE){
            txtField01.setDisable(false);
            txtField02.setDisable(false);
        }
    }

    private void initTable() {
        index01.setStyle("-fx-alignment: CENTER;");
        index02.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        index03.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");

        index01.setCellValueFactory(new PropertyValueFactory<>("index01"));
        index02.setCellValueFactory(new PropertyValueFactory<>("index02"));
        index03.setCellValueFactory(new PropertyValueFactory<>("index03"));

        tblList.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblList.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });
        tblList.setItems(data);
        tblList.autosize();
    }

    private void LoadList() {
        System.out.println("Loading Labor List...");
        data.clear();

        // ✅ Get cached data from the data handler
        cacheLaborList = oParameters.LaborModel().getCachedLaborList();

        if (cacheLaborList == null) {
            System.out.println("No cached data found! Fetching from database...");
            oParameters.LaborModel().LaborList(""); // Reload data if cache is empty
            return;
        }

        try {
            cacheLaborList.beforeFirst(); // Reset cursor before reading

            while (cacheLaborList.next()) {
                String laborId = cacheLaborList.getString("sLaborIDx");
                String laborName = cacheLaborList.getString("sLaborNme");
                String amount = cacheLaborList.getString("nAmountxx");

                System.out.println("Labor ID: " + laborId);
                System.out.println("Labor Name: " + laborName);
                System.out.println("Amount: " + amount);

                data.add(new ModelListParameter(laborId, laborName, amount));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void tblList_Clicked(MouseEvent event) {
    pnListRow = tblList.getSelectionModel().getSelectedIndex();

    if (pnListRow >= 0) {
        try {
            // ✅ Ensure cached data is available
            if (cacheLaborList == null) {
                System.out.println("No cached data found! Cannot retrieve selected row.");
                return;
            }

            cacheLaborList.absolute(pnListRow + 1); // ✅ Move cursor to selected row (1-based index)

            double newAmount = cacheLaborList.getDouble("nAmountxx"); // ✅ Get current amount
            txtField04.setText(CommonUtils.NumberFormat(newAmount, "#,##0.00")); // ✅ Format & Display
            txtField03.setText(cacheLaborList.getString("sLaborNme"));
            // Debugging output
            System.out.println("Selected Labor ID: " + cacheLaborList.getString("sLaborIDx"));
            System.out.println("Selected Labor Name: " + cacheLaborList.getString("sLaborNme"));
            System.out.println("Selected Amount: " + newAmount);

            // ✅ Listen for text changes & update cache and UI when value changes
            txtField04.textProperty().addListener((observable, oldValue, newValue) -> {
                try {
                    double updatedAmount = Double.parseDouble(newValue.replace(",", "")); // Remove commas before parsing
                    cacheLaborList.updateDouble("nAmountxx", updatedAmount); // ✅ Update cache
                    cacheLaborList.updateRow(); // ✅ Commit changes to cache
                    
                    // ✅ Update data list for UI refresh
                    data.set(pnListRow, new ModelListParameter(
                            cacheLaborList.getString("sLaborIDx"),
                            cacheLaborList.getString("sLaborNme"),
                            CommonUtils.NumberFormat(updatedAmount, "#,##0.00")
                    ));

                    tblList.refresh(); // ✅ Refresh TableView to show updated value

                    System.out.println("Updated Amount for Row " + pnListRow + ": " + updatedAmount);

                } catch (NumberFormatException | SQLException e) {
                    System.out.println("Invalid amount entered.");
                }
            });

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}



}
