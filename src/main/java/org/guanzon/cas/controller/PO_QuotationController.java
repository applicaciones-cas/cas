/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.cas.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
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
 * @author user
 */
public class PO_QuotationController implements Initializable, ScreenInterface{

    @FXML
    private AnchorPane MainAnchorPane, apBrowse, apMaster, apButton, apTable, apDetail;

    @FXML
    private Button btnNew, btnBrowse, btnCancel, btnSave, btnClose, btnUpdate,
            btnConfirm, btnVoid, btnSearch, btnDelDetail;

    @FXML
    private HBox hbButtons;
    @FXML
    private Label lblStatus;

    @FXML
    private TextField txtField01, txtField02, txtField03, txtField04, txtField05, txtField06, txtField99, txtField98;
    @FXML
    private TextArea txtField07;
    @FXML
    private TextField txtDetail01, txtDetail02, txtDetail03, txtDetail04, txtDetail05, txtDetail06, txtDetail07;
    @FXML
    private TableView tblDetails;
    @FXML
    private TableColumn index01, index02, index03, index04, index05, index06, index07;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @Override
    public void setGRider(GRider foValue) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
