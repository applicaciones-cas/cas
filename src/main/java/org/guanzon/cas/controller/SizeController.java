/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package OjtProject.View;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

/**
 * FXML Controller class
 *
 * @author User-pc
 */
public class SizeController implements Initializable {

    @FXML
    private Button Add, Save, Edit, Cancel, Deactive, Close;
    @FXML
    private TextField Size_ID, Size_Name;
    @FXML
    private CheckBox Active;
    @FXML
    private TableView<ProjectSizeClass> Table;
    @FXML
    private TableColumn<ProjectSizeClass, String> SizeID;
    @FXML
    private TableColumn<ProjectSizeClass, String> SizeName;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ProjectSizeClass TableClassSize = new ProjectSizeClass("", "");
        TableClassSize.initialize(SizeID, SizeName, Active, Save);
        // TODO
    }

    @FXML
    public void BoxAction(ActionEvent event) {
        ProjectSizeClass TableClassSize = new ProjectSizeClass("", "");
        TableClassSize.BoxAction(Active);
    }

    @FXML
    public void BtnAdd(ActionEvent event) {
        ProjectSizeClass TableClassSize = new ProjectSizeClass("", "");
        TableClassSize.BtnAdd(Size_ID, Size_Name, Active, Table);
    }

    @FXML
    public void BtnCancel(ActionEvent event) {
        ProjectSizeClass TableClassSize = new ProjectSizeClass("", "");
        TableClassSize.BtnCancel(Size_ID, Size_Name, Active, Add, Edit, Deactive, Save);
    }

    @FXML
    public void BtnClose(ActionEvent event) {
        ProjectSizeClass TableClassSize = new ProjectSizeClass("", "");
        TableClassSize.BtnClose(Close);
    }

    @FXML
    public void BtnDeactive(ActionEvent event) {
        int SelectedIndex = Table.getSelectionModel().getSelectedIndex();
        if (SelectedIndex >= 0) {
            Table.getItems().remove(SelectedIndex);
        }
    }

    @FXML
    public void BtnEdit(ActionEvent event) {
        ProjectSizeClass TableClassSize = new ProjectSizeClass("", "");
        TableClassSize.BtnEdit(Table, Size_ID, SizeID, Size_Name, SizeName, Add, Edit, Deactive, Save);
    }

    @FXML
    public void BtnSave(ActionEvent event) {
        ProjectSizeClass TableClassSize = new ProjectSizeClass("", "");
        TableClassSize.BtnSave(Table, Size_ID, Size_Name, Active, Save, Add, Edit, Deactive);
    }

    @FXML
    public void TFID(KeyEvent event) {
        ProjectSizeClass TableClassSize = new ProjectSizeClass("", "");
        TableClassSize.TFID(Size_ID);
    }

    @FXML
    public void TFName(KeyEvent event) {
        ProjectSizeClass TableClassSize = new ProjectSizeClass("", "");
        TableClassSize.TFName(Size_Name);
    }
}
