package org.guanzon.cas.controller;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
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
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.cas.model.ModelParameter;
import org.guanzon.cas.parameters.Category_Level3;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author Maynard
 */
public class CategoryLevel3Controller implements Initializable, ScreenInterface {

    private final String pxeModuleName = "Category Level 3";
    private GRider oApp;
    private Category_Level3 oTrans;
    private JSONObject poJSON;
    private int pnEditMode;

    private String psPrimary = "";

    private boolean state = false;
    private boolean pbLoaded = false;
    private int pnIndex;
    private int pnListRow;

    private ObservableList<ModelParameter> ListData = FXCollections.observableArrayList();

    @FXML
    private AnchorPane ChildAnchorPane;
    @FXML
    private HBox hbButtons;
    @FXML
    private Button btnNew;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnActivate;
    @FXML
    private Button btnClose;
    @FXML
    private Button btnBrowse;
    @FXML
    private TextField txtField01;
    @FXML
    private TextField txtField02;
    @FXML
    private TextField txtField99;
    @FXML
    private CheckBox cbActive;
    @FXML
    private FontAwesomeIconView faActivate;
    @FXML
    private TableView tblList;
    @FXML
    private TableColumn index01, index02;

    @FXML
    void cmdButton_Click(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();

        switch (lsButton) {

            case "btnNew":
                
                clearFields();
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
                poJSON = oTrans.saveRecord();

                pnEditMode = oTrans.getModel().getEditMode();
                if ("error".equals((String) poJSON.get("result"))) {
                    System.err.println((String) poJSON.get("message"));

                    pnEditMode = EditMode.UNKNOWN;
                    return;

                } else {
                    oTrans = new Category_Level3(oApp, true);
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
                    oTrans = new Category_Level3(oApp, true);
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
                                oTrans = new Category_Level3(oApp, false);
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
                                oTrans = new Category_Level3(oApp, false);
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
    public void initialize(URL location, ResourceBundle resources) {

        oTrans = new Category_Level3(oApp, false);
        oTrans.setRecordStatus("10");
        pbLoaded = true;

        pnEditMode = EditMode.UNKNOWN;

        initButton(pnEditMode);
        initTextFields();
        clearFields();

        pbLoaded = true;

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

        txtField02.requestFocus();
        tblList.setDisable(lbShow);

    }

    private void initTextFields() {
        /*textFields FOCUSED PROPERTY*/
        txtField01.focusedProperty().addListener(txtField_Focus);
        txtField02.focusedProperty().addListener(txtField_Focus);
        txtField99.focusedProperty().addListener(txtField_Focus);

        /*textFields KeyPressed PROPERTY*/
        txtField99.setOnKeyPressed(this::txtField_KeyPressed);

    }

    private void txtField_KeyPressed(KeyEvent event) {
        TextField textField = (TextField) event.getSource();
        int lnIndex = Integer.parseInt(((TextField) event.getSource()).getId().substring(8, 10));
        String lsValue = textField.getText();
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

                case 2:
                    poJSON = oTrans.getModel().setDescription(lsValue);
                    if ("error".equals((String) poJSON.get("result"))) {
                        System.err.println((String) poJSON.get("message"));
                        return;
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

        psPrimary = oTrans.getModel().getCategoryCode();
        txtField01.setText(psPrimary);
        txtField02.setText(oTrans.getModel().getDescription());
//        txtField03.setText(oTrans.getModel().getMainCategoryName());

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

        psPrimary = "";
        btnActivate.setText("Activate");
        cbActive.setSelected(false);

        loadTableDetail();
    }

    private void loadTableDetail() {
        int lnCtr;
        ListData.clear();

        poJSON = oTrans.loadModelList();
        if ("error".equals((String) poJSON.get("result"))) {
            System.err.println((String) poJSON.get("message"));
            ShowMessageFX.Information((String) poJSON.get("message"), "Computerized Acounting System", pxeModuleName);

            return;
        }

        int lnItem = oTrans.getModelList().size();
        if (lnItem <= 0) {
            return;
        }

        for (lnCtr = 0; lnCtr <= lnItem - 1; lnCtr++) {
            ListData.add(new ModelParameter(
                    (String) oTrans.getModelList().get(lnCtr).getCategoryCode(),
                    (String) oTrans.getModelList().get(lnCtr).getDescription(),
                    "",
                    "",
                    ""));

        }

        initListGrid();
    }

    public void initListGrid() {
        index01.setStyle("-fx-alignment: CENTER;");
        index02.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");

        index01.setCellValueFactory(new PropertyValueFactory<ModelParameter, String>("index01"));
        index02.setCellValueFactory(new PropertyValueFactory<ModelParameter, String>("index02"));

        tblList.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblList.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });

        tblList.setItems(ListData);

    }

    @FXML
    void tblList_Clicked(MouseEvent event) {
        pnListRow = tblList.getSelectionModel().getSelectedIndex();
        if (pnListRow >= 0) {
            oTrans.openRecord(ListData.get(pnListRow).getIndex01());
            loadRecord();
        }
    }

}
