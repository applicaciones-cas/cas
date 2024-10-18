/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.cas.controller;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
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
import org.guanzon.cas.controller.ScreenInterface;
import org.guanzon.cas.inventory.base.Inventory;
import org.guanzon.cas.inventory.models.Model_Inv_Stock_Request_Detail;
import org.guanzon.cas.model.ModelPurchaseOrderMC;
import org.guanzon.cas.model.ModelPurchaseOrder2;
import org.guanzon.cas.parameters.Branch;
import org.guanzon.cas.parameters.Color;
import org.guanzon.cas.parameters.Model;
import org.guanzon.cas.parameters.Model_Variant;
import org.guanzon.cas.purchasing.controller.PurchaseOrder;
import org.json.simple.JSONObject;
import org.junit.Assert;

/**
 * FXML Controller class
 *
 * @author User
 */
public class PurchaseOrderConfirmationMCController implements Initializable, ScreenInterface {

    private final String pxeModuleName = "Purchase Order Confirmation MC";
    private GRider oApp;
    private PurchaseOrder oTrans;
    private JSONObject poJSON;
    private int pnEditMode;

    private String psPrimary = "";
    private boolean pbLoaded = false;
    private int pnIndex;
    private int pnDetailRow;

    @FXML
    private AnchorPane MainAnchorPane;
    @FXML
    private AnchorPane apBrowse;
    @FXML
    private AnchorPane apButton;
    @FXML
    private AnchorPane apTable;
    @FXML
    private AnchorPane apDetail;

    @FXML
    private Button btnBrowse, btnPrint, btnCancel, btnClose, btnConfirm;

    @FXML
    private HBox hbButtons;

    @FXML
    private AnchorPane apMaster;
    @FXML
    private AnchorPane apTransactionIssues;

    @FXML
    private Label lblStatus;
    @FXML
    private TextField txtField01, txtField02, txtField03, txtField04, txtField05, txtField06, txtField08, txtField09, txtField10,
            txtField11, txtField12, txtField99, txtField98, txtField97;
    @FXML
    private TextArea txtField07;

    @FXML
    private Label lblStatus1;

    @FXML
    private TableColumn index12, index13;
    @FXML
    private TextField txtDetail01, txtDetail02, txtDetail03, txtDetail04, txtDetail05, txtDetail06, txtDetail07;
    @FXML
    private TableView tblDetails, tblTransactionIssues;
    @FXML
    private TableColumn index01, index02, index03, index04, index05, index06, index07, index08, index09, index10, index11;

    /**
     * Initializes the controller class.
     */
    private ObservableList<ModelPurchaseOrderMC> data = FXCollections.observableArrayList();
    private ObservableList<ModelPurchaseOrder2> data2 = FXCollections.observableArrayList();

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
                        ShowMessageFX.Information(null, pxeModuleName, (String) poJSON.get("message"));
                        return;
                    }
                    txtField.setText(CommonUtils.xsDateLong(oTrans.getMasterModel().getTransactionDate()));
                    break;

                case 8://Reference No
                    poJSON = oTrans.getMasterModel().setReferenceNo(lsValue);
                    if ("error".equals((String) poJSON.get("result"))) {
                        System.err.println((String) poJSON.get("message"));
                        ShowMessageFX.Information(null, pxeModuleName, (String) poJSON.get("message"));
                        return;
                    }
                    break;
                case 10://Disc
                    poJSON = oTrans.getMasterModel().setDiscount(Integer.valueOf(lsValue));
                    if ("error".equals((String) poJSON.get("result"))) {
                        System.err.println((String) poJSON.get("message"));
                        ShowMessageFX.Information(null, pxeModuleName, (String) poJSON.get("message"));
                        return;
                    }
                    break;
                case 11://AddDisc Rate
                    poJSON = oTrans.getMasterModel().setAddDiscount(Integer.valueOf(lsValue));
                    if ("error".equals((String) poJSON.get("result"))) {
                        System.err.println((String) poJSON.get("message"));
                        ShowMessageFX.Information(null, pxeModuleName, (String) poJSON.get("message"));
                        return;
                    }
                    break;
                case 12://Total Order
                    poJSON = oTrans.getMasterModel().setTransactionTotal(Integer.valueOf(lsValue)); // computation
                    if ("error".equals((String) poJSON.get("result"))) {
                        System.err.println((String) poJSON.get("message"));
                        ShowMessageFX.Information(null, pxeModuleName, (String) poJSON.get("message"));
                        return;
                    }
                    break;

            }
        } else {

            switch (lnIndex) {
                case 2:
                    txtField.setText(CommonUtils.dateFormat(oTrans.getMasterModel().getTransactionDate(), "yyyy-MM-dd"));
                    if ("error".equals((String) poJSON.get("result"))) {
                        System.err.println((String) poJSON.get("message"));
                        ShowMessageFX.Information(null, pxeModuleName, (String) poJSON.get("message"));
                        return;
                    }
                    break;

            }

        }

        txtField.selectAll();
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
                case 7://Remarks
                    poJSON = oTrans.getMasterModel().setRemarks(lsValue);
                    if ("error".equals((String) poJSON.get("result"))) {
                        System.err.println((String) poJSON.get("message"));
                        ShowMessageFX.Information(null, pxeModuleName, (String) poJSON.get("message"));
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
        pnIndex = lnIndex;
        if (lsValue == null) {
            return;
        }

        if (!nv) {
            /*Lost Focus*/
            switch (lnIndex) {
                case 2:
                    String lsStockID = (String) oTrans.getDetailModel(pnDetailRow).getValue("sStockIDx");
                    if (lsStockID == null || lsStockID.isEmpty()) {
                        if (txtField.getText().length() > 128) {
                            ShowMessageFX.Warning("Max characters for `Descript` exceeds the limit.", pxeModuleName, "Please verify your entry.");
                            txtField.requestFocus();
                            return;
                        }
                        poJSON = oTrans.setDetail(pnDetailRow, "sDescript", lsValue);
                        if ("error".equals((String) poJSON.get("result"))) {
                            System.err.println((String) poJSON.get("message"));
                            return;
                        }
                    }
                    loadTableDetail();
                    break;
                case 5:
                    //Orig Cost inputted by user
                    String lsNewCost = (String) oTrans.getDetailModel(pnDetailRow).getValue("nUnitPrce");
                    if (lsNewCost == null || lsNewCost.isEmpty()) {
                        if (txtField.getText().length() > 128) {
                            ShowMessageFX.Warning("Max characters for `ROQ` exceeds the limit.", pxeModuleName, "Please verify your entry.");
                            txtField.requestFocus();
                            return;
                        }
                        poJSON = oTrans.setDetail(pnDetailRow, "nUnitPrce", lsValue);
                        if ("error".equals((String) poJSON.get("result"))) {
                            System.err.println((String) poJSON.get("message"));
                            return;
                        }
                    }

                    loadTableDetail();
                    break;
                case 7:
                    /*this must be numeric*/
                    int x = 0;
                    try {

                        x = Integer.valueOf(lsValue);
                        if (x > 999999) {
                            x = 0;

                            ShowMessageFX.Warning("Please input not greater than 999999", pxeModuleName, "");
                            txtField.requestFocus();
                        }
                    } catch (Exception e) {
                        ShowMessageFX.Warning("Please input numbers only.", pxeModuleName, e.getMessage());
                        txtField.requestFocus();
                    }

                    poJSON = oTrans.setDetail(pnDetailRow, "nQuantity", x);
                    if ("error".equals((String) poJSON.get("result"))) {
                        System.err.println((String) poJSON.get("message"));
                        ShowMessageFX.Information(null, pxeModuleName, (String) poJSON.get("message"));
                        return;
                    }

                    loadTableDetail();
                    break;

            }
        } else {
            txtField.selectAll();
        }

    };

    private void txtField_KeyPressed(KeyEvent event) {

        TextField textField = (TextField) event.getSource();
        int lnIndex = Integer.parseInt(((TextField) event.getSource()).getId().substring(8, 10));
        String lsValue = textField.getText();
        switch (event.getCode()) {
            case F3:
                switch (lnIndex) {
                    case 97:/*Browse Supplier*/
                        poJSON = oTrans.searchSupplier("sSupplier", lsValue, lnIndex == 97);
                        if ("error".equalsIgnoreCase(poJSON.get("result").toString())) {

                            ShowMessageFX.Information((String) poJSON.get("message"), "Computerized Acounting System", pxeModuleName);
                            txtField01.requestFocus();
                        } else {
                            loadRecord();
                        }
                        break;
                    case 98:/*Browse Destination*/
                        poJSON = oTrans.searchDestination("sDestinat", lsValue, lnIndex == 98);
                        if ("error".equalsIgnoreCase(poJSON.get("result").toString())) {

                            ShowMessageFX.Information((String) poJSON.get("message"), "Computerized Acounting System", pxeModuleName);
                            txtField01.requestFocus();
                        } else {
                            loadRecord();
                        }
                        break;

                    case 99:/*Browse Primary*/
                        poJSON = oTrans.searchTransaction("sTransNox", lsValue, lnIndex == 99);
                        if ("error".equalsIgnoreCase(poJSON.get("result").toString())) {

                            ShowMessageFX.Information((String) poJSON.get("message"), "Computerized Acounting System", pxeModuleName);
                            txtField01.requestFocus();
                        } else {
                            loadRecord();
                        }
                        break;

                    case 3:
                        /*sDestinat*/
                        poJSON = oTrans.searchMaster(5, lsValue, false);
                        if ("error".equalsIgnoreCase(poJSON.get("result").toString())) {

                            ShowMessageFX.Information((String) poJSON.get("message"), "Computerized Acounting System", pxeModuleName);
                            ShowMessageFX.Information(null, pxeModuleName, (String) poJSON.get("message"));
                            txtField01.requestFocus();
                        } else {
                            loadRecord();
                        }
                        break;

                    case 4:
                        /*sSupplier*/
                        poJSON = oTrans.searchMaster(6, lsValue, false);
                        if ("error".equalsIgnoreCase(poJSON.get("result").toString())) {
                            ShowMessageFX.Information((String) poJSON.get("message"), "Computerized Acounting System", pxeModuleName);
                            ShowMessageFX.Information(null, pxeModuleName, (String) poJSON.get("message"));
                            txtField01.requestFocus();
                        } else {
                            loadRecord();
                        }
                        break;
                    case 9:
                        /*sTermCode*/
                        poJSON = oTrans.searchMaster(10, lsValue, false);
                        if ("error".equalsIgnoreCase(poJSON.get("result").toString())) {

                            ShowMessageFX.Information((String) poJSON.get("message"), "Computerized Acounting System", pxeModuleName);
                            ShowMessageFX.Information(null, pxeModuleName, (String) poJSON.get("message"));
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

    private void loadRecord() {
        String lsActive = oTrans.getMasterModel().getTransactionStatus();

        switch (lsActive) {
            case "0":
                lblStatus.setText("OPEN");
                break;
            case "1":
                lblStatus.setText("CLOSED");
                break;
            case "2":
                lblStatus.setText("POSTED");
                break;
            case "3":
                lblStatus.setText("CANCELLED");
                break;
            default:
                lblStatus.setText("UNKNOWN");
                break;
        }

        //get supplier id and search contctp & contctno
        String lsClientID = oTrans.getMasterModel().getSupplier();
        if (!lsClientID.isEmpty()) {
            oTrans.searchMaster("sSupplier", lsClientID, pbLoaded);
        }

        psPrimary = oTrans.getMasterModel().getTransactionNo();
        txtField01.setText(psPrimary);
        txtField02.setText(CommonUtils.xsDateLong(oTrans.getMasterModel().getTransactionDate()));
        txtField03.setText(oTrans.getMasterModel().getDestinationOther());
        txtField04.setText(oTrans.getMasterModel().getSupplierName());
        txtField05.setText(oTrans.getMasterModel().getContactPerson1());
        txtField06.setText(oTrans.getMasterModel().getMobileNo());
        txtField07.setText(oTrans.getMasterModel().getRemarks());
        txtField08.setText(oTrans.getMasterModel().getReferenceNo());
        txtField09.setText(oTrans.getMasterModel().getTermName());

        try {
            txtField10.setText(String.valueOf(oTrans.getMasterModel().getDiscount()));
            txtField11.setText(String.valueOf(oTrans.getMasterModel().getAddDiscount()));
            txtField12.setText(String.valueOf(oTrans.getMasterModel().getTransactionTotal()));

        } catch (Exception e) {
        }
        loadTableDetail();
        loadTableDetail2();
    }

    private void loadTableDetail2() {
        int lnCtr;
        data2.clear();

        int lnItem = oTrans.getItemCount();
        if (lnItem < 0) {
            return;
        }
        //count size

        double lnTotalTransaction = 0;
        for (lnCtr = 0; lnCtr <= oTrans.getItemCount() - 1; lnCtr++) {
            String lsStockIDx = (String) oTrans.getDetailModel(lnCtr).getValue("sStockIDx");
            Inventory loInventory;
            Color loColor;
            Model loMdl;
            Model_Variant loMdlVrnt;
            if (lsStockIDx != null && !lsStockIDx.equals("")) {

                loInventory = oTrans.GetInventory((String) oTrans.getDetailModel(lnCtr).getValue("sStockIDx"), true);
                loMdl = oTrans.GetModel((String) loInventory.getMaster("sModelIDx"), true);
                loMdlVrnt = oTrans.GetModel_Variant((String) loMdl.getModel().getVariantID(), true);
                loColor = oTrans.GetColor((String) loInventory.getMaster("sColorIDx"), true);

                data2.add(new ModelPurchaseOrder2(String.valueOf(lnCtr + 1),
                        "Sample"));

                try {
                    if (oTrans.getDetailModel(lnCtr).getQuantity() != 0) {
                        lnTotalTransaction += Double.parseDouble((oTrans.getDetailModel(lnCtr).getUnitPrice().toString())) * Double.parseDouble(String.valueOf(oTrans.getDetailModel(lnCtr).getQuantity()));
                    } else {
                        lnTotalTransaction += Double.parseDouble((oTrans.getDetailModel(lnCtr).getUnitPrice().toString()));
                        System.out.println(lnTotalTransaction);
                    }
                } catch (Exception e) {
                }

            } else {
                data2.add(new ModelPurchaseOrder2(String.valueOf(lnCtr + 1),
                        "Sample"));

            }
        }
        txtField12.setText(String.valueOf(lnTotalTransaction));
        lnTotalTransaction = 0;

        /*FOCUS ON FIRST ROW*/
        if (pnDetailRow < 0 || pnDetailRow >= data.size()) {
            if (!data.isEmpty()) {
                /* FOCUS ON FIRST ROW */
                tblTransactionIssues.getSelectionModel().select(0);
                tblTransactionIssues.getFocusModel().focus(0);
                pnDetailRow = tblDetails.getSelectionModel().getSelectedIndex();
            }
//            setSelectedDetail(); //textfield data
        } else {
            /* FOCUS ON THE ROW THAT pnRowDetail POINTS TO */
            tblTransactionIssues.getSelectionModel().select(pnDetailRow);
            tblTransactionIssues.getFocusModel().focus(pnDetailRow);
//            setSelectedDetail();
        }
        initDetailsGrid2();
    }

    private void loadTableDetail() {
        int lnCtr;
        data.clear();

        int lnItem = oTrans.getItemCount();
        if (lnItem < 0) {
            return;
        }
        //count size
        for (lnCtr = 0; lnCtr < oTrans.getItemCount() - 1; lnCtr++) {
            System.out.println((String) oTrans.getDetailModel(lnCtr).getValue("sStockIDx"));
        }

        double lnTotalTransaction = 0;
        for (lnCtr = 0; lnCtr <= oTrans.getItemCount() - 1; lnCtr++) {
            String lsStockIDx = (String) oTrans.getDetailModel(lnCtr).getValue("sStockIDx");
            Inventory loInventory;
            Color loColor;
            Model loMdl;
            Model_Variant loMdlVrnt;
            if (lsStockIDx != null && !lsStockIDx.equals("")) {

                loInventory = oTrans.GetInventory((String) oTrans.getDetailModel(lnCtr).getValue("sStockIDx"), true);

                loMdl = oTrans.GetModel((String) loInventory.getMaster("sModelIDx"), true);
                loMdlVrnt = oTrans.GetModel_Variant((String) loMdl.getModel().getVariantID(), true);
                loColor = oTrans.GetColor((String) loInventory.getMaster("sColorIDx"), true);
                data.add(new ModelPurchaseOrderMC(String.valueOf(lnCtr + 1),
                        (String) loInventory.getMaster("sBarCodex"),
                        (String) oTrans.getDetailModel(lnCtr).getDescription(),
                        (String) loInventory.getMaster("xBrandNme"),
                        (String) loMdl.getModel().getModelCode(),
                        (String) loMdl.getModel().getModelDescription(),
                        loMdlVrnt.getModel().getVariantName(),
                        String.valueOf(loMdl.getModel().getYearModel()),
                        (String) loColor.getModel().getDescription(),
                        oTrans.getDetailModel(lnCtr).getValue("nUnitPrce").toString(),
                        (String) oTrans.getDetailModel(lnCtr).getValue("nQuantity").toString()
                ));

                try {
                    if (oTrans.getDetailModel(lnCtr).getQuantity() != 0) {
                        lnTotalTransaction += Double.parseDouble((oTrans.getDetailModel(lnCtr).getUnitPrice().toString())) * Double.parseDouble(String.valueOf(oTrans.getDetailModel(lnCtr).getQuantity()));
                    } else {
                        System.out.println(lnTotalTransaction);
                    }
                } catch (Exception e) {
                }

            } else {
                data.add(new ModelPurchaseOrderMC(String.valueOf(lnCtr + 1),
                        "",
                        (String) oTrans.getDetailModel(lnCtr).getValue("sDescript"),
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        String.valueOf(oTrans.getDetailModel(lnCtr).getValue("nUnitPrce")),
                        oTrans.getDetailModel(lnCtr).getValue("nQuantity").toString()));

            }
        }
        txtField12.setText(String.format("%.2f", lnTotalTransaction));
        oTrans.getMasterModel().setTransactionTotal(Double.valueOf(String.format("%.2f", lnTotalTransaction)));
        lnTotalTransaction = 0;

        /*FOCUS ON FIRST ROW*/
        if (pnDetailRow < 0 || pnDetailRow >= data.size()) {
            if (!data.isEmpty()) {
                /* FOCUS ON FIRST ROW */
                tblDetails.getSelectionModel().select(0);
                tblDetails.getFocusModel().focus(0);
                pnDetailRow = tblDetails.getSelectionModel().getSelectedIndex();
            }
            setSelectedDetail(); //textfield data
        } else {
            /* FOCUS ON THE ROW THAT pnRowDetail POINTS TO */
            tblDetails.getSelectionModel().select(pnDetailRow);
            tblDetails.getFocusModel().focus(pnDetailRow);
            setSelectedDetail();
        }
        initDetailsGrid();
    }

    private void setSelectedDetail() {
        Model_Inv_Stock_Request_Detail loModel_Inv_Stock_Request_Detail;
        loModel_Inv_Stock_Request_Detail = oTrans.GetModel_Inv_Stock_Request_Detail(oTrans.getDetailModel(pnDetailRow).getStockID());

        txtDetail01.setText((String) data.get(pnDetailRow).getIndex02());
        txtDetail02.setText((String) data.get(pnDetailRow).getIndex03());
        txtDetail03.setText(String.format("%.2f", oTrans.getDetailModel(pnDetailRow).getOriginalCost()));
        txtDetail04.setText(String.valueOf(loModel_Inv_Stock_Request_Detail.getApproved()));
        txtDetail05.setText((String) data.get(pnDetailRow).getIndex10());
        txtDetail06.setText(Integer.toString(oTrans.getDetailModel(pnDetailRow).getRecOrder()));
        txtDetail07.setText(Integer.toString(oTrans.getDetailModel(pnDetailRow).getQuantity()));

    }

    public void initDetailsGrid() {
        index01.setStyle("-fx-alignment: CENTER;");
        index02.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        index03.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        index04.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        index05.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        index06.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        index07.setStyle("-fx-alignment: CENTER-RIGHT;-fx-padding: 0 5 0 0;");
        index08.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        index09.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        index10.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        index11.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");

        index01.setCellValueFactory(new PropertyValueFactory<ModelPurchaseOrderMC, String>("index01"));
        index02.setCellValueFactory(new PropertyValueFactory<ModelPurchaseOrderMC, String>("index02"));
        index03.setCellValueFactory(new PropertyValueFactory<ModelPurchaseOrderMC, String>("index03"));
        index04.setCellValueFactory(new PropertyValueFactory<ModelPurchaseOrderMC, String>("index04"));
        index05.setCellValueFactory(new PropertyValueFactory<ModelPurchaseOrderMC, String>("index05"));
        index06.setCellValueFactory(new PropertyValueFactory<ModelPurchaseOrderMC, String>("index06"));
        index07.setCellValueFactory(new PropertyValueFactory<ModelPurchaseOrderMC, String>("index07"));
        index08.setCellValueFactory(new PropertyValueFactory<ModelPurchaseOrderMC, String>("index08"));
        index09.setCellValueFactory(new PropertyValueFactory<ModelPurchaseOrderMC, String>("index09"));
        index10.setCellValueFactory(new PropertyValueFactory<ModelPurchaseOrderMC, String>("index10"));
        index11.setCellValueFactory(new PropertyValueFactory<ModelPurchaseOrderMC, String>("index11"));

        tblDetails.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblDetails.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });

        tblDetails.setItems(data);

    }

    public void initDetailsGrid2() {
        index12.setStyle("-fx-alignment: CENTER;-fx-padding: 0 0 0 5;");
        index13.setStyle("-fx-alignment: CENTER;-fx-padding: 0 0 0 5;");

        index12.setCellValueFactory(new PropertyValueFactory<ModelPurchaseOrder2, String>("index12"));
        index13.setCellValueFactory(new PropertyValueFactory<ModelPurchaseOrder2, String>("index13"));

        tblTransactionIssues.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblTransactionIssues.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });

        tblTransactionIssues.setItems(data2);

    }

    private void txtDetail_KeyPressed(KeyEvent event) {

        TextField textField = (TextField) event.getSource();
        int lnIndex = Integer.parseInt(((TextField) event.getSource()).getId().substring(9, 11));
        String lsValue = textField.getText();
        switch (event.getCode()) {
            case F3:
                switch (lnIndex) {
                    case 1:
                        poJSON = oTrans.searchDetail(pnDetailRow, 3, lsValue, lnIndex == 1);
                        if ("error".equalsIgnoreCase(poJSON.get("result").toString())) {
                            ShowMessageFX.Information((String) poJSON.get("message"), "Computerized Acounting System", pxeModuleName);
                        }

                        break;
                    case 2:
                        /* Barcode & Description */
                        poJSON = oTrans.searchDetail(pnDetailRow, 3, lsValue, lnIndex == 1);
                        if ("error".equalsIgnoreCase(poJSON.get("result").toString())) {
                            ShowMessageFX.Information((String) poJSON.get("message"), "Computerized Acounting System", pxeModuleName);
                        }
                        break;

                }
                loadRecord();
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

    private void initTextFields() {
        /*textFields FOCUSED PROPERTY*/
        txtField01.focusedProperty().addListener(txtField_Focus);
        txtField02.focusedProperty().addListener(txtField_Focus);
        txtField03.focusedProperty().addListener(txtField_Focus);
        txtField04.focusedProperty().addListener(txtField_Focus);
        txtField05.focusedProperty().addListener(txtField_Focus);
        txtField06.focusedProperty().addListener(txtField_Focus);
        txtField07.focusedProperty().addListener(txtArea_Focus);
        txtField08.focusedProperty().addListener(txtField_Focus);
        txtField09.focusedProperty().addListener(txtField_Focus);
        txtField10.focusedProperty().addListener(txtField_Focus);
        txtField11.focusedProperty().addListener(txtField_Focus);
        txtField12.focusedProperty().addListener(txtField_Focus);

        txtField99.focusedProperty().addListener(txtField_Focus);
        txtField98.focusedProperty().addListener(txtField_Focus);
        txtField97.focusedProperty().addListener(txtField_Focus);

        txtDetail01.focusedProperty().addListener(txtDetail_Focus);
        txtDetail02.focusedProperty().addListener(txtDetail_Focus);
        txtDetail03.focusedProperty().addListener(txtDetail_Focus);
        txtDetail04.focusedProperty().addListener(txtDetail_Focus);
        txtDetail05.focusedProperty().addListener(txtDetail_Focus);
        txtDetail06.focusedProperty().addListener(txtDetail_Focus);
        txtDetail07.focusedProperty().addListener(txtDetail_Focus);

        txtField03.setOnKeyPressed(this::txtField_KeyPressed);
        txtField04.setOnKeyPressed(this::txtField_KeyPressed);
        txtField09.setOnKeyPressed(this::txtField_KeyPressed);

        txtField99.setOnKeyPressed(this::txtField_KeyPressed);
        txtField98.setOnKeyPressed(this::txtField_KeyPressed);
        txtField97.setOnKeyPressed(this::txtField_KeyPressed);

        txtDetail01.setOnKeyPressed(this::txtDetail_KeyPressed);//barcode
        txtDetail02.setOnKeyPressed(this::txtDetail_KeyPressed);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        oTrans = new PurchaseOrder(oApp, false);
        oTrans.setTransactionStatus("12340");

        initTextFields();
        initDetailsGrid();
        initDetailsGrid2();
        clearFields();

        pnEditMode = EditMode.UNKNOWN;
        initButton(pnEditMode);
        pbLoaded = true;
    }

    private void initButton(int fnValue) {
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);

        btnCancel.setVisible(lbShow);
        btnCancel.setManaged(lbShow);

        btnBrowse.setVisible(!lbShow);
        btnPrint.setVisible(!lbShow);
        btnClose.setVisible(!lbShow);
        btnBrowse.setManaged(!lbShow);
        btnPrint.setManaged(!lbShow);
        btnClose.setManaged(!lbShow);

        apBrowse.setDisable(lbShow);
        apMaster.setDisable(!lbShow);
        apDetail.setDisable(!lbShow);
//        apTable.setDisable(!lbShow);

        if (Integer.parseInt(oTrans.getMasterModel().getTransactionStatus()) != 1) {
            btnConfirm.setDisable(false);
        } else {
            btnConfirm.setDisable(true);
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
        txtField08.clear();
        txtField09.clear();
        txtField10.clear();
        txtField11.clear();
        txtField12.clear();

        txtField99.clear();
        txtField98.clear();
        txtField97.clear();

        txtDetail01.clear();
        txtDetail02.clear();
        txtDetail03.clear();
        txtDetail04.clear();
        txtDetail05.clear();
        txtDetail06.clear();
        txtDetail07.clear();

        psPrimary = "";
        lblStatus.setText("UNKNOWN");

        pnDetailRow = -1;
        pnIndex = -1;

        data.clear();

    }

    @FXML
    void cmdButton_Click(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();

        switch (lsButton) {
            case "btnBrowse":
                if (pnIndex < 98) {
                    pnIndex = 99;
                }
                poJSON = oTrans.searchTransaction("sTransNox", "", pnIndex == 99);
                pnEditMode = EditMode.READY;
                //start
                if ("error".equalsIgnoreCase(poJSON.get("result").toString())) {

                    ShowMessageFX.Information((String) poJSON.get("message"), "Computerized Acounting System", pxeModuleName);
                    txtField01.requestFocus();
                    pnEditMode = EditMode.UNKNOWN;
                    return;
                } else {
                    loadRecord();
                }
                break;

            case "btnPrint":
                poJSON = oTrans.printRecord();
                if ("error".equals((String) poJSON.get("result"))) {
                    Assert.fail((String) poJSON.get("message"));
                }

                break;
            case "btnClose":
                unloadForm appUnload = new unloadForm();
                if (ShowMessageFX.OkayCancel(null, "Close Tab", "Are you sure you want to close this Tab?") == true) {
                    appUnload.unloadForm(MainAnchorPane, oApp, pxeModuleName);

                } else {
                    return;
                }

                break;
            case "btnConfirm":
                if (!psPrimary.isEmpty()) {
                    if (btnConfirm.getText().equals("Activate")) {
                        if (ShowMessageFX.YesNo(null, pxeModuleName, "Do you want to Activate this Parameter?") == true) {
                            poJSON = oTrans.closeTransaction(oTrans.getMasterModel().getTransactionNo());
                            if ("error".equals((String) poJSON.get("result"))) {
                                System.err.println((String) poJSON.get("message"));

                                return;
                            } else {
                                clearFields();
                                pnEditMode = EditMode.UNKNOWN;
                                initButton(pnEditMode);
                                oTrans = new PurchaseOrder(oApp, false);
                                oTrans.setTransactionStatus("12340");
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
                                ShowMessageFX.Information(null, pxeModuleName, (String) poJSON.get("message"));
                                return;
                            } else {
                                clearFields();
                                pnEditMode = EditMode.UNKNOWN;
                                initButton(pnEditMode);
                                oTrans = new PurchaseOrder(oApp, false);
                                oTrans.setTransactionStatus("12340");
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

            case "btnCancel":
                if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Do you want to disregard changes?") == true) {
                    oTrans = new PurchaseOrder(oApp, true);
                    oTrans.setTransactionStatus("12340");
                    pbLoaded = true;
                    pnEditMode = EditMode.UNKNOWN;
                    clearFields();
                    break;
                } else {
                    return;
                }
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

    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

}
