package org.guanzon.cas.controller;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.cas.model.ImageModel;

public class FileAttachmentPreviewController {

    private double mouseAnchorX;
    private double mouseAnchorY;
    private double scaleFactor = 1.0;
    private FileChooser fileChooser;
    private int pnAttachmentRow;
    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    private AnchorPane mainPane;
    @FXML
    private AnchorPane bottomNav;
    @FXML
    private AnchorPane draggablePane;

    @FXML
    private Button btnClose;
    @FXML
    private Label text;

    @FXML
    private TableView tblAttachments;

    @FXML
    private ImageView imageView;

    @FXML
    private Button btnAttachment;

    @FXML
    private StackPane stackPane1;

    @FXML
    private TableColumn index12;

    @FXML
    private TableColumn index13;

    private final ObservableList<ImageModel> img_data = FXCollections.observableArrayList();

    @FXML
    void closeDialog(MouseEvent event) {

    }

    public void initialize() {
        draggablePane.setOnMousePressed(this::handleMousePressed);
        draggablePane.setOnMouseDragged(this::handleMouseDragged);
        
          // Add inline CSS for rounded corners
        mainPane.setStyle(
                "-fx-background-radius: 5; " 
        );
        
        draggablePane.setStyle(
                "-fx-background-radius: 5 5 0 0; " + // Top-left and top-right corners rounded
                "-fx-background-color:#4D5656; "
        );
         bottomNav.setStyle(
                "-fx-background-radius: 0 0 5 5; " // Top-left and top-right corners rounded
        );
        
        btnClose.setOnAction(event -> {
            Stage stage = (Stage) btnClose.getScene().getWindow();
            stage.close();
        });
        stackPane1.layoutBoundsProperty().addListener((observable, oldBounds, newBounds) -> {
            stackPane1.setClip(new javafx.scene.shape.Rectangle(
                    newBounds.getMinX(),
                    newBounds.getMinY(),
                    newBounds.getWidth(),
                    newBounds.getHeight()
            ));
        });

        imageView.setOnScroll((ScrollEvent event) -> {
            double delta = event.getDeltaY();
            scaleFactor = Math.max(0.5, Math.min(scaleFactor * (delta > 0 ? 1.1 : 0.9), 5.0));
            imageView.setScaleX(scaleFactor);
            imageView.setScaleY(scaleFactor);
        });

        imageView.setOnMousePressed((MouseEvent event) -> {
            mouseAnchorX = event.getSceneX() - imageView.getTranslateX();
            mouseAnchorY = event.getSceneY() - imageView.getTranslateY();
        });

        imageView.setOnMouseDragged((MouseEvent event) -> {
            double translateX = event.getSceneX() - mouseAnchorX;
            double translateY = event.getSceneY() - mouseAnchorY;
            imageView.setTranslateX(translateX);
            imageView.setTranslateY(translateY);
        });
    }
    
    
    private void handleMousePressed(MouseEvent event) {
        // Capture the initial offset between the mouse and the stage
        xOffset = event.getSceneX();
        yOffset = event.getSceneY();
    }

    private void handleMouseDragged(MouseEvent event) {
        // Get the current stage and update its position
        Stage stage = (Stage) draggablePane.getScene().getWindow();
        stage.setX(event.getScreenX() - xOffset);
        stage.setY(event.getScreenY() - yOffset);
    }

    private void loadTableAttachment() {
        //load image data
//        img_data.clear();
        List<ImageModel> tempData = new ArrayList<>();

        // Copy the data
        for (int i = 0; i < img_data.size(); i++) {
            tempData.add(new ImageModel(String.valueOf(i), img_data.get(i).getIndex13()));
        }

        // Clear the original data
        img_data.clear();

        // Add the copied data back if needed
        img_data.addAll(tempData);

    }

    private void stackPaneClip() {
        javafx.scene.shape.Rectangle clip = new javafx.scene.shape.Rectangle(
                stackPane1.getWidth() - 8, // Subtract 10 for padding (5 on each side)
                stackPane1.getHeight() - 8 // Subtract 10 for padding (5 on each side)
        );
        clip.setArcWidth(8); // Optional: Rounded corners for aesthetics
        clip.setArcHeight(8);
        clip.setLayoutX(4); // Set padding offset for X
        clip.setLayoutY(4); // Set padding offset for Y
        stackPane1.setClip(clip);
    }

    @FXML
    void cmdButton_Click(ActionEvent event) throws IOException {

        String lsButton = ((Button) event.getSource()).getId();

        switch (lsButton) {
            case "btnAttachment":
                try {
                // Open file chooser dialog
                fileChooser = new FileChooser();
                fileChooser.setTitle("Choose Image");
                fileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
                );
                java.io.File selectedFile = fileChooser.showOpenDialog((Stage) text.getScene().getWindow());

                if (selectedFile != null) {
                    // Read image from the selected file
                    Path imgPath = selectedFile.toPath();
                    Image loimage = new Image(Files.newInputStream(imgPath));
                    imageView.setImage(loimage);
                    stackPaneClip();
                    String imgPath2 = selectedFile.toString();
                    img_data.add(new ImageModel(String.valueOf(img_data.size()), imgPath2));

                    if (img_data.size() > 1) {
                        pnAttachmentRow = img_data.size() - 1;
                    }
                    loadTableAttachment();
                    setSelectedAttachment();
                    initDetailsGrid2();

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            break;
            case "btnRemoveItem":
                img_data.remove(pnAttachmentRow);
                pnAttachmentRow -= 1;
                setSelectedAttachment();
                loadTableAttachment();
                initDetailsGrid2();
                break;
            default:
                ShowMessageFX.Warning(null, "File Attachment", "Button with name " + lsButton + " not registered.");
                return;
        }
    }

    private void setSelectedAttachment() {
        try {
            if (pnAttachmentRow >= 0) {
                String filePath = (String) img_data.get(pnAttachmentRow).getIndex13();
                if (filePath.length() != 0) {
                    Path imgPath = Paths.get(filePath);
                    Image loimage = new Image(Files.newInputStream(imgPath));
                    imageView.setImage(loimage);
                    stackPaneClip();
                } else {
                    imageView.setImage(null);
                    stackPaneClip();
                }
            } else {
                imageView.setImage(null);
                stackPaneClip();
                pnAttachmentRow = 0;
            }
        } catch (IOException ex) {
            Logger.getLogger(PurchaseOrderMCController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void initDetailsGrid2() {
        /*FOCUS ON FIRST ROW*/

        index12.setStyle("-fx-alignment: CENTER;-fx-padding: 0 0 0 5;");
        index13.setStyle("-fx-alignment: CENTER;-fx-padding: 0 0 0 5;");

        index12.setCellValueFactory(new PropertyValueFactory<ImageModel, String>("index12"));
        index13.setCellValueFactory(new PropertyValueFactory<ImageModel, String>("index13"));

        tblAttachments.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblAttachments.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });

        tblAttachments.setItems(img_data);

        if (pnAttachmentRow < 0 || pnAttachmentRow >= img_data.size()) {
            if (!img_data.isEmpty()) {
                /* FOCUS ON FIRST ROW */
                tblAttachments.getSelectionModel().select(0);
                tblAttachments.getFocusModel().focus(0);
                pnAttachmentRow = tblAttachments.getSelectionModel().getSelectedIndex();
            }
        } else {
            /* FOCUS ON THE ROW THAT pnRowDetail POINTS TO */
            tblAttachments.getSelectionModel().select(pnAttachmentRow);
            tblAttachments.getFocusModel().focus(pnAttachmentRow);
        }

    }

    @FXML
    void tblAttachments_Clicked(MouseEvent event) {
        pnAttachmentRow = tblAttachments.getSelectionModel().getSelectedIndex();
        if (pnAttachmentRow >= 0) {
            setSelectedAttachment();
        }

    }

}
