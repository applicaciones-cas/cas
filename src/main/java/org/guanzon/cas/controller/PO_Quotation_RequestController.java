package org.guanzon.cas.controller;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Date;
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
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.base.SQLUtil;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.cas.model.ModelPOQuotationRequest;
import org.guanzon.cas.model.inventory.PO_Quotation_Request;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author luke
 */
public class PO_Quotation_RequestController implements Initializable, ScreenInterface {

    private final String pxeModuleName = "PO_Quotation";
    private GRider oApp;
    private PO_Quotation_Request oTrans;
    private JSONObject poJSON;
    private int pnEditMode;

    private String psPrimary = "";

    private boolean state = false;
    private boolean pbLoaded = false;
    private int pnIndex;
    private int pnDetailRow;

    @FXML
    private AnchorPane MainAnchorPain, apButton, apTable, apDetail, apTextField;

    @FXML
    private Button btnNew, btnBrowse, btnCancel, btnSave, btnClose, btnUpdate, btnConfirm, btnVoid;

    @FXML
    private HBox hbButtons;
    @FXML
    private Label lblStatus;

    @FXML
    private TextField txtField01, txtField02, txtField03, txtField04, txtField05, txtField06;
    @FXML
    private TextArea txtField07;
    @FXML
    private TextField txtDetail01, txtDetail02, txtDetail03, txtDetail04, txtDetail05, txtDetail06, txtDetail07;
    @FXML
    private TableView tblDetails;
    @FXML
    private TableColumn index01, index02, index03, index04, index05, index06, index07;

    private ObservableList<ModelPOQuotationRequest> data = FXCollections.observableArrayList();

    @FXML
    void cmdButton_Click(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();

        switch (lsButton) {

            case "btnNew":
                poJSON = oTrans.newTransaction();
                loadRecord();
                pnEditMode = oTrans.getMasterModel().getEditMode();
                oTrans.setTransactionStatus("1");
                if ("error".equals((String) poJSON.get("result"))) {
                    System.err.println((String) poJSON.get("message"));

                    pnEditMode = EditMode.UNKNOWN;
                    return;
                }
                break;

            case "btnSave":

                poJSON = oTrans.getMasterModel().setModifiedBy(oApp.getUserID());
                if ("error".equals((String) poJSON.get("result"))) {
                    System.err.println((String) poJSON.get("message"));

                    pnEditMode = EditMode.UNKNOWN;
                    return;
                }
                poJSON = oTrans.getMasterModel().setModifiedDate(oApp.getServerDate());
                if ("error".equals((String) poJSON.get("result"))) {
                    System.err.println((String) poJSON.get("message"));

                    pnEditMode = EditMode.UNKNOWN;
                    return;
                }
                poJSON = oTrans.saveTransaction();

                pnEditMode = oTrans.getMasterModel().getEditMode();
                if ("error".equals((String) poJSON.get("result"))) {
                    System.err.println((String) poJSON.get("message"));

                    pnEditMode = EditMode.UNKNOWN;
                    return;

                } else {
                    oTrans = new PO_Quotation_Request(oApp, true);
                    pbLoaded = true;
                    oTrans.setTransactionStatus("1");
                    pnEditMode = EditMode.UNKNOWN;
                    clearFields();
                    ShowMessageFX.Information(null, pxeModuleName, "Transaction successful Saved!");
                }
                break;

            case "btnUpdate":
                poJSON = oTrans.updateTransaction();

                pnEditMode = oTrans.getMasterModel().getEditMode();
                if ("error".equals((String) poJSON.get("result"))) {
                    System.err.println((String) poJSON.get("message"));

                    pnEditMode = EditMode.UNKNOWN;
                    return;
                }
                break;

            case "btnCancel":
                if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Do you want to disregard changes?") == true) {
                    oTrans = new PO_Quotation_Request(oApp, true);
                    oTrans.setTransactionStatus("1");
                    pbLoaded = true;
                    pnEditMode = EditMode.UNKNOWN;
                    clearFields();
                    break;
                } else {
                    return;
                }

            case "btnConfirm":
                if (!psPrimary.isEmpty()) {
                    if (btnConfirm.getText().equals("Activate")) {
                        if (ShowMessageFX.YesNo(null, pxeModuleName, "Do you want to Activate this Parameter?") == true) {
                            poJSON = oTrans.postTransaction(psPrimary);
                            if ("error".equals((String) poJSON.get("result"))) {
                                System.err.println((String) poJSON.get("message"));
                                return;
                            } else {
                                clearFields();
                                pnEditMode = EditMode.UNKNOWN;
                                initButton(pnEditMode);
                                oTrans = new PO_Quotation_Request(oApp, false);
                                oTrans.setTransactionStatus("1");
                                pbLoaded = true;

                            }
                        } else {
                            return;
                        }
                    } else {
                        if (ShowMessageFX.YesNo(null, pxeModuleName, "Do you want to Deactivate this Parameter?") == true) {
                            poJSON = oTrans.cancelTransaction(psPrimary);
                            if ("error".equals((String) poJSON.get("result"))) {
                                System.err.println((String) poJSON.get("message"));
                                return;
                            } else {
                                clearFields();
                                pnEditMode = EditMode.UNKNOWN;
                                initButton(pnEditMode);
                                oTrans = new PO_Quotation_Request(oApp, false);
                                oTrans.setTransactionStatus("1");
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
                poJSON = oTrans.searchTransaction("sTransNox", txtField01.getText(), false);
                pnEditMode = EditMode.READY;
                if ("error".equalsIgnoreCase(poJSON.get("result").toString())) {

                    ShowMessageFX.Information((String) poJSON.get("message"), "Computerized Acounting System", pxeModuleName);
                    txtField01.requestFocus();
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

    @FXML
    void tblDetails_Clicked(MouseEvent event) {
        pnDetailRow = tblDetails.getSelectionModel().getSelectedIndex();
        if (pnDetailRow >= 0) {
            setSelectedDetail();
        }
    }

    public void initDetailsGrid() {
        index01.setStyle("-fx-alignment: CENTER;");
        index02.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        index03.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        index04.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        index05.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        index06.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        index07.setStyle("-fx-alignment: CENTER-RIGHT;-fx-padding: 0 5 0 0;");

        index01.setCellValueFactory(new PropertyValueFactory<ModelPOQuotationRequest, String>("index01"));
        index02.setCellValueFactory(new PropertyValueFactory<ModelPOQuotationRequest, String>("index02"));
        index03.setCellValueFactory(new PropertyValueFactory<ModelPOQuotationRequest, String>("index03"));
        index04.setCellValueFactory(new PropertyValueFactory<ModelPOQuotationRequest, String>("index04"));
        index05.setCellValueFactory(new PropertyValueFactory<ModelPOQuotationRequest, String>("index05"));
        index06.setCellValueFactory(new PropertyValueFactory<ModelPOQuotationRequest, String>("index06"));
        index07.setCellValueFactory(new PropertyValueFactory<ModelPOQuotationRequest, String>("index07"));

        tblDetails.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblDetails.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });

        tblDetails.setItems(data);

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        oTrans = new PO_Quotation_Request(oApp, false);
        oTrans.setTransactionStatus("12340");

        initTextFields();
        initDetailsGrid();

        pnEditMode = EditMode.UNKNOWN;
        initButton(pnEditMode);
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
        btnConfirm.setVisible(!lbShow);
        btnClose.setVisible(!lbShow);

        txtField01.setDisable(lbShow);
        txtField02.setEditable(lbShow);
        txtField03.setEditable(lbShow);

        txtField02.requestFocus();

    }

    private void initTextFields() {
        /*textFields FOCUSED PROPERTY*/
        txtField01.focusedProperty().addListener(txtField_Focus);
        txtField02.focusedProperty().addListener(txtField_Focus);
        txtField03.focusedProperty().addListener(txtField_Focus);
        txtField01.focusedProperty().addListener(txtField_Focus);
        txtField04.focusedProperty().addListener(txtField_Focus);
        txtField05.focusedProperty().addListener(txtField_Focus);
        txtField06.focusedProperty().addListener(txtField_Focus);
        txtField07.focusedProperty().addListener(txtField_Focus);

        txtDetail01.focusedProperty().addListener(txtDetail_Focus);
        txtDetail02.focusedProperty().addListener(txtDetail_Focus);
        txtDetail03.focusedProperty().addListener(txtDetail_Focus);
        txtDetail04.focusedProperty().addListener(txtDetail_Focus);
        txtDetail05.focusedProperty().addListener(txtDetail_Focus);
        txtDetail06.focusedProperty().addListener(txtDetail_Focus);
        txtDetail07.focusedProperty().addListener(txtDetail_Focus);


        /*textFields KeyPressed PROPERTY*/
        txtField01.setOnKeyPressed(this::txtField_KeyPressed);//transaction browse
        txtField01.setOnKeyPressed(this::txtDetail_KeyPressed);//barcode
        txtField02.setOnKeyPressed(this::txtDetail_KeyPressed);//description
//        txtField10.setOnKeyPressed(this::txtField_KeyPressed);

    }

    private void txtField_KeyPressed(KeyEvent event) {

        TextField textField = (TextField) event.getSource();
        int lnIndex = Integer.parseInt(((TextField) event.getSource()).getId().substring(8, 10));
        String lsValue = textField.getText();
        switch (event.getCode()) {
            case F3:
                switch (lnIndex) {

                    case 01:
                        /*Browse Primary*/
                        poJSON = oTrans.openTransaction(lsValue);
                        if ("error".equalsIgnoreCase(poJSON.get("result").toString())) {

                            ShowMessageFX.Information((String) poJSON.get("message"), "Computerized Acounting System", pxeModuleName);
                            txtField01.requestFocus();
                        } else {
                            loadRecord();
                        }
                        break;

                }
                break;
        }
        switch (event.getCode()) {
            case ENTER:
                break;
            case DOWN:
                CommonUtils.SetNextFocus(textField);
                break;
            case UP:
                CommonUtils.SetPreviousFocus(textField);
        }

        pnIndex = lnIndex;
    }

    private void txtDetail_KeyPressed(KeyEvent event) {

        TextField textField = (TextField) event.getSource();
        int lnIndex = Integer.parseInt(((TextField) event.getSource()).getId().substring(9, 11));
        String lsValue = textField.getText();
        switch (event.getCode()) {
            case F3:
                switch (lnIndex) {
                    case 1:
                        /* Barcode & Description */

                        poJSON = oTrans.searchDetail(pnDetailRow, lnIndex, lsValue, true);
                        System.out.println("poJson Result = " + poJSON.toJSONString());
                        if ("error".equalsIgnoreCase(poJSON.get("result").toString())) {
                            ShowMessageFX.Information((String) poJSON.get("message"), "Computerized Acounting System", pxeModuleName);
                        }

                        break;
                    case 2:
                        /* Barcode & Description */
                        poJSON = oTrans.searchDetail(pnDetailRow, lnIndex, lsValue, false);
                        System.out.println("poJson Result = " + poJSON.toJSONString());
                        if ("error".equalsIgnoreCase(poJSON.get("result").toString())) {
                            ShowMessageFX.Information((String) poJSON.get("message"), "Computerized Acounting System", pxeModuleName);
                        }

                        break;

                }
                break;
        }
        switch (event.getCode()) {
            case ENTER:
                break;
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
                case 2://dtransact
                    poJSON = oTrans.getMasterModel().setTransactionDate(SQLUtil.toDate(lsValue, "yyyy-MM-dd"));
                    if ("error".equals((String) poJSON.get("result"))) {
                        System.err.println((String) poJSON.get("message"));
                        return;
                    }
                    break;
                case 4://referno
                    poJSON = oTrans.getMasterModel().setReferenceNumber(lsValue);
                    if ("error".equals((String) poJSON.get("result"))) {
                        System.err.println((String) poJSON.get("message"));
                        return;
                    }
                    break;
                case 6://expecteddate
                    poJSON = oTrans.getMasterModel().setExpectedPurchaseDate(SQLUtil.toDate(lsValue, "yyyy-MM-dd"));
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

    final ChangeListener<? super Boolean> txtArea_Focus = (o, ov, nv) -> {
        if (!pbLoaded) {
            return;
        }

        TextArea txtField = (TextArea) ((ReadOnlyBooleanPropertyBase) o).getBean();
        int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
        String lsValue = txtField.getText();

        if (lsValue == null) {
            return;
        }

        if (!nv) {
            /*Lost Focus*/
            switch (lnIndex) {
                case 7:
                    poJSON = oTrans.getMasterModel().setRemarks(lsValue);
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

    final ChangeListener<? super Boolean> txtDetail_Focus = (o, ov, nv) -> {
        if (!pbLoaded) {
            return;
        }

        TextField txtField = (TextField) ((ReadOnlyBooleanPropertyBase) o).getBean();
        int lnIndex = Integer.parseInt(txtField.getId().substring(9, 11));
        String lsValue = txtField.getText();

        if (lsValue == null) {
            return;
        }

        if (!nv) {
            /*Lost Focus*/
            switch (lnIndex) {
                case 7:
                    poJSON = oTrans.getMasterModel().setValue("nQuantity", lsValue);
                    if ("error".equals((String) poJSON.get("result"))) {
                        System.err.println((String) poJSON.get("message"));
                        return;
                    }
                    break;
//                case 5:
//                    poJSON = oTrans.getMasterModel().setDestination(lsValue);
//                    if ("error".equals((String) poJSON.get("result"))) {
//                        System.err.println((String) poJSON.get("message"));
//                        return;
//                    }
//                    break;

            }
        } else {
            txtField.selectAll();
        }
        pnIndex = lnIndex;
    };

    private void loadRecord() {
        int lnActive = oTrans.getMasterModel().getTransactionStatus();
        
        
        switch (lnActive){
            case 0: 
            lblStatus.setText("OPEN");
            break;
            case 1: 
            lblStatus.setText("CLOSED");
            break;
            case 2: 
            lblStatus.setText("POSTED");
            break;
            case 3: 
            lblStatus.setText("CANCELLED");
            break;
            default: 
            lblStatus.setText("UNKNOWN");
            break;
            
        }

        psPrimary = oTrans.getMasterModel().getTransactionNumber();
        txtField01.setText(psPrimary);
        txtField02.setText(oApp.getServerDate().toString());
        txtField03.setText(oTrans.getMasterModel().getCategoryName());
        txtField04.setText(oTrans.getMasterModel().getReferenceNumber());
        txtField05.setText(oTrans.getMasterModel().getDestination());
        txtField06.setText(oApp.getServerDate().toString());
        

        loadTableDetail();

    }

    private void clearFields() {
        txtField01.clear();
        txtField02.clear();
        txtField03.clear();

        psPrimary = "";
        btnConfirm.setText("Activate");
        pnDetailRow = -1;
        pnIndex = -1;

    }

    
    private void loadTableDetail() {
        int lnCtr;
        data.clear();

        int lnItem = oTrans.getItemCount();
        if (lnItem > 0) {
            return;
        }

        for (lnCtr = 0; lnCtr <= lnItem - 1; lnCtr++) {
            data.add(new ModelPOQuotationRequest(String.valueOf(lnCtr + 1),
                    (String) oTrans.getDetailModel(lnCtr).getValue("sBarCodex"),//if doesnot load create setter and getter on model
                    (String) oTrans.getDetailModel(lnCtr).getValue("sDescript"),//oTrans.getDetailModel(lnCtr).getBarcode() or something
                    (String) oTrans.getDetailModel(lnCtr).getValue("xModelNme"),
                    (String) oTrans.getDetailModel(lnCtr).getValue("xColorNme"),
                    (String) oTrans.getDetailModel(lnCtr).getValue("xMeasurNm"),
                    oTrans.getDetailModel(lnCtr).getValue("nQuantity").toString()));

            //display fetched detail on console
            System.out.println("\nNo == " + String.valueOf(lnCtr + 1));
            System.out.println("\nsBarCodex == " + (String) oTrans.getDetailModel(lnCtr).getValue("sBarCodex"));
            System.out.println("\nsDescript == " + (String) oTrans.getDetailModel(lnCtr).getValue("sDescript"));
            System.out.println("\nxModelNme == " + (String) oTrans.getDetailModel(lnCtr).getValue("xModelNme"));
            System.out.println("\nxColorNme == " + (String) oTrans.getDetailModel(lnCtr).getValue("xColorNme"));
            System.out.println("\nxMeasurNm == " + (String) oTrans.getDetailModel(lnCtr).getValue("xMeasurNm"));
            System.out.println("\nQuantity == " + oTrans.getDetailModel(lnCtr).getValue("nQuantity").toString());

        }
    }

    private void setSelectedDetail() {

        txtDetail01.setText((String) oTrans.getDetailModel(pnDetailRow).getValue("sBarCodex"));
        txtDetail02.setText((String) oTrans.getDetailModel(pnDetailRow).getValue("sDescript"));
        txtDetail03.setText((String) oTrans.getDetailModel(pnDetailRow).getValue("sDescript"));//category ito
        txtDetail04.setText((String) oTrans.getDetailModel(pnDetailRow).getValue("xModelNme"));
        txtDetail05.setText((String) oTrans.getDetailModel(pnDetailRow).getValue("xColorNme"));
        txtDetail06.setText((String) oTrans.getDetailModel(pnDetailRow).getValue("xMeasurNm"));
        txtDetail07.setText((String) oTrans.getDetailModel(pnDetailRow).getValue("nQuantity"));

    }
}
