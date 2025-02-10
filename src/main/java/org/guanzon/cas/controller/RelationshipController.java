package org.guanzon.cas.controller;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
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
import org.guanzon.appdriver.base.LogWrapper;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.cas.model.ModelResultSet;
import org.guanzon.cas.parameter.services.ParamControllers;
import org.json.simple.JSONObject;

public class RelationshipController implements Initializable, ScreenInterface {

    private GRider oApp;
    private final String pxeModuleName = "Relationship";
    private int pnEditMode;
    private ParamControllers oParameters;
    private boolean state = false;
    private boolean pbLoaded = false;
    private int pnInventory = 0;
    private int pnRow = 0;
    private ObservableList<ModelResultSet> data = FXCollections.observableArrayList();

    @FXML
    private AnchorPane AnchorMain, AnchorInputs;
    @FXML
    private HBox hbButtons;

    @FXML
    private Button btnBrowse,
            btnNew,
            btnSave,
            btnUpdate,
            btnCancel,
            btnActivate,
            btnClose;

    @FXML
    private FontAwesomeIconView faActivate;

    @FXML
    private TextField txtField01,
            txtField02,
            txtSeeks01;

    @FXML
    private CheckBox cbField01;

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
        pbLoaded = true;
    }

    private void initializeObject() {
        LogWrapper logwrapr = new LogWrapper("CAS", System.getProperty("sys.default.path.temp") + "cas-error.log");
        oParameters = new ParamControllers(oApp, logwrapr);
        oParameters.Relationship().setRecordStatus("0123");
    }

    private void ClickButton() {
        btnBrowse.setOnAction(this::handleButtonAction);
        btnNew.setOnAction(this::handleButtonAction);
        btnSave.setOnAction(this::handleButtonAction);
        btnUpdate.setOnAction(this::handleButtonAction);
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
                    JSONObject poJSON = oParameters.Relationship().newRecord();
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
                    poJSON = oParameters.Relationship().searchRecord(lsValue, false);
                    if ("error".equals((String) poJSON.get("result"))) {
                        ShowMessageFX.Information((String) poJSON.get("message"), "Computerized Acounting System", pxeModuleName);
                        txtSeeks01.clear();
                        break;
                    }
                    pnEditMode = EditMode.READY;
                    loadRecord();
                    initTabAnchor();
                    break;
                case "btnUpdate":
                    poJSON = oParameters.Relationship().updateRecord();
                    if ("error".equals((String) poJSON.get("result"))) {
                        ShowMessageFX.Information((String) poJSON.get("message"), "Computerized Acounting System", pxeModuleName);
                        break;
                    }
                    pnEditMode = oParameters.Relationship().getEditMode();
                    initButton(pnEditMode);
                    initTabAnchor();
                    break;
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
                    oParameters.Relationship().getModel().setModifyingId(oApp.getUserID());
                    oParameters.Relationship().getModel().setModifiedDate(oApp.getServerDate());
                    JSONObject saveResult = oParameters.Relationship().saveRecord();
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
                    String Status = oParameters.Relationship().getModel().getRecordStatus();
                    JSONObject poJsON;

                    switch (Status) {
                        case "0":
                            if (ShowMessageFX.YesNo(null, pxeModuleName, "Do you want to Activate this Parameter?") == true) {
                                poJsON = oParameters.Relationship().postTransaction();
                                ShowMessageFX.Information((String) poJsON.get("message"), "Computerized Accounting System", pxeModuleName);
                                loadRecord();
                            }
                            break;
                        case "1":
                            if (ShowMessageFX.YesNo(null, pxeModuleName, "Do you want to Deactivate this Parameter?") == true) {
                                poJsON = oParameters.Relationship().voidTransaction();
                                ShowMessageFX.Information((String) poJsON.get("message"), "Computerized Accounting System", pxeModuleName);
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
        txtSeeks01.clear();
        cbField01.setSelected(false);
    }

    private void initButton(int fnValue) {
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);

        btnCancel.setVisible(lbShow);
        btnCancel.setManaged(lbShow);
        btnSave.setVisible(lbShow);
        btnSave.setManaged(lbShow);
        btnUpdate.setVisible(!lbShow);
        btnUpdate.setManaged(!lbShow);

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
                    case 1:
                        oParameters.Relationship().getModel().setRelationId(lsValue);
                        break;
                    case 2:
                        oParameters.Relationship().getModel().setRelationshipDesc(lsValue);
                        break;

                    default:
                        break;
                }
            } catch (Exception e) {
                System.err.println("Error processing input: " + e.getMessage());
            }
        } else {
            txtField.selectAll();
        }
    };

    private void loadRecord() {
        boolean lbActive = oParameters.Relationship().getModel().getRecordStatus() == "1";

        txtField01.setText(oParameters.Relationship().getModel().getRelationId());
        txtField02.setText(oParameters.Relationship().getModel().getRelationshipDesc());

        switch (oParameters.Relationship().getModel().getRecordStatus()) {
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
            oParameters.Relationship().getModel().setRecordStatus("1");
        } else {
            oParameters.Relationship().getModel().setRecordStatus("0");
        }
    }

    private void initTabAnchor() {
        if (AnchorInputs == null) {
            System.err.println("Error: AnchorInput is not initialized.");
            return;
        }

        boolean isEditable = (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE);
        AnchorInputs.setDisable(!isEditable);
    }

}
