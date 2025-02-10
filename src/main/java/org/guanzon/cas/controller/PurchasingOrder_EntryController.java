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
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import org.guanzon.appdriver.base.GRider;

/**
 * FXML Controller class
 *
 * @author User
 */
public class PurchasingOrder_EntryController implements Initializable, ScreenInterface {

    private GRider oApp;
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
    private Button btnClose;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnSearch;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnCancel;
    @FXML
    private TextField txtField01;
    @FXML
    private TextField txtField02;
    @FXML
    private TextField txtField03;
    @FXML
    private TextField txtField04;
    @FXML
    private TextField txtField05;
    @FXML
    private TextArea textArea07;
    @FXML
    private TextField txtField08;
    @FXML
    private CheckBox cbAdv;
    @FXML
    private TextField txtField09;
    @FXML
    private TextField txtField11;
    @FXML
    private TextField txtField10;
    @FXML
    private TextField txtField12;
    @FXML
    private TextField txtField13;
    @FXML
    private TextField txtField14;
    @FXML
    private CheckBox cbVatIncl;
    @FXML
    private TextField txtField06;
    @FXML
    private Label lblStatus;
    @FXML
    private TextField txtField18;
    @FXML
    private TextField txtField19;
    @FXML
    private TextField txtField25;
    @FXML
    private TextField txtField26;
    @FXML
    private TextField txtField27;
    @FXML
    private TextField txtField17;
    @FXML
    private TextField txtField16;
    @FXML
    private TextField txtField15;
    @FXML
    private TextField txtField20;
    @FXML
    private TextField txtField21;
    @FXML
    private TextField txtField22;
    @FXML
    private TextField txtField23;
    @FXML
    private TextField txtField24;
    @FXML
    private TableColumn<?, ?> tblindex01;
    @FXML
    private TableColumn<?, ?> tblindex02;
    @FXML
    private TableColumn<?, ?> tblindex03;
    @FXML
    private TableColumn<?, ?> tblindex04;
    @FXML
    private TableView<?> tblViewOrderDetails;
    @FXML
    private TableColumn<?, ?> tblindex01_order_details;
    @FXML
    private TableColumn<?, ?> tblindex02_order_details;
    @FXML
    private TableColumn<?, ?> tblindex03_order_details;
    @FXML
    private TableColumn<?, ?> tblindex04_order_details;
    @FXML
    private TableColumn<?, ?> tblindex05_order_details;
    @FXML
    private TableColumn<?, ?> tblindex06_order_details;
    @FXML
    private TableColumn<?, ?> tblindex07_order_details;
    @FXML
    private TableColumn<?, ?> tblindex08_order_details;
    @FXML
    private TableColumn<?, ?> tblindex09_order_details;
    @FXML
    private TableColumn<?, ?> tblindex10_order_details;
    @FXML
    private TableColumn<?, ?> tblindex11_order_details;
    @FXML
    private TableColumn<?, ?> tblindex12_order_details;
    @FXML
    private TableColumn<?, ?> tblindex13_order_details;
    @FXML
    private TableView<?> tblViewStock_Request;

    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void cmdButton_Click(ActionEvent event) {
    }

}
