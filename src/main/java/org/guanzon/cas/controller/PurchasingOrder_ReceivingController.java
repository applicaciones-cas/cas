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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import org.guanzon.appdriver.base.GRider;

/**
 * FXML Controller class
 *
 * @author User
 */
public class PurchasingOrder_ReceivingController implements Initializable, ScreenInterface {

    private GRider oApp;
    @FXML
    private AnchorPane MainAnchorPane;
    @FXML
    private AnchorPane apBrowse;
    @FXML
    private AnchorPane apButton;
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
    private Button btnCancel;
    @FXML
    private Button btnPrint;
    @FXML
    private Button btnSearch;
    @FXML
    private Button btnClose;
    @FXML
    private AnchorPane apMaster;
    @FXML
    private Label lblStatus;
    @FXML
    private TextField txtField01;
    @FXML
    private TextField txtField02;
    @FXML
    private TextField txtField03;
    @FXML
    private TextField txtField04;
    @FXML
    private TextArea textArea05;
    @FXML
    private TextField txtField08;
    @FXML
    private TextField txtField09;
    @FXML
    private DatePicker datePicker06;
    @FXML
    private TextField txtField10;
    @FXML
    private DatePicker datePicker07;
    @FXML
    private TextField txtField11;
    @FXML
    private TextField txtField13;
    @FXML
    private TextField txtField14;
    @FXML
    private TextField txtField15;
    @FXML
    private TextField txtField16;
    @FXML
    private TextField txtField19;
    @FXML
    private TextField txtField20;
    @FXML
    private TextField txtField23;
    @FXML
    private TextField txtField17;
    @FXML
    private TextField txtField12;
    @FXML
    private TextField txtField18;
    @FXML
    private TextField txtField22;
    @FXML
    private TextField txtField21;
    @FXML
    private AnchorPane apTable1;
    @FXML
    private TableView<?> tblViewRecevingDetails;
    @FXML
    private TableColumn<?, ?> tblindex01_receiving;
    @FXML
    private TableColumn<?, ?> tblindex02_receiving;
    @FXML
    private TableColumn<?, ?> tblindex03_receiving;
    @FXML
    private TableColumn<?, ?> tblindex04_receiving;
    @FXML
    private TableColumn<?, ?> tblindex05_receiving;
    @FXML
    private TableColumn<?, ?> tblindex06_receiving;
    @FXML
    private TableColumn<?, ?> tblindex07_receiving;
    @FXML
    private TableColumn<?, ?> tblindex08_receiving;
    @FXML
    private TableColumn<?, ?> tblindex09_receiving;
    @FXML
    private TableColumn<?, ?> tblindex10_receiving;
    @FXML
    private TableColumn<?, ?> tblindex11_receiving;
    @FXML
    private TableColumn<?, ?> tblindex12_receiving;
    @FXML
    private TableColumn<?, ?> tblindex13_receiving;
    @FXML
    private AnchorPane apTable;
    @FXML
    private TableView<?> tblViewApprovedOrder;
    @FXML
    private TableColumn<?, ?> tblindex01;
    @FXML
    private TableColumn<?, ?> tblindex02;
    @FXML
    private TableColumn<?, ?> tblindex03;
    @FXML
    private TableColumn<?, ?> tblindex04;
    @FXML
    private TableColumn<?, ?> tblindex05;
    @FXML
    private TableColumn<?, ?> tblindex06;

    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    private void cmdButton_Click(ActionEvent event) {
    }
}
