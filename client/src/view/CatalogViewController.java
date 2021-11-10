package view;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.events.JFXDialogEvent;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.ImageInput;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import viewmodel.CatalogViewModel;
import viewmodel.object.ProductViewModel;

public class CatalogViewController extends ViewController {

    private ViewHandler viewHandler;
    private CatalogViewModel viewModel;

    // FXML instance variables of catalog table.
    @FXML private TableView<ProductViewModel> catalogTable;
    @FXML private TableColumn<ProductViewModel, Integer> catalogAvailabilityColumn;
    @FXML private TableColumn<ProductViewModel, String> catalogNameColumn;
    @FXML private TableColumn<ProductViewModel, String> catalogDescriptionColumn;
    @FXML private TableColumn<ProductViewModel, Double> catalogPriceColumn;

    // The rest FXML instance variables of the view.
    @FXML public StackPane rootPane;
    @FXML public BorderPane borderPane;
    @FXML private Label usernameLabel;
    @FXML private Button basketButton;
    @FXML private Button manageProductsButton;
    @FXML private Button manageUsersButton;
    @FXML private TextArea notificationCommentField;
    @FXML private Button notificationButton;
    @FXML private Label errorLabel;

    @Override
    protected void init() {
        viewHandler = getViewHandler();
        viewModel = getViewModelFactory().getCatalogViewModel();

        // Bindings for the catalog table.
        catalogAvailabilityColumn.setCellValueFactory(cellData -> cellData.getValue().getQuantityProperty());
        catalogNameColumn.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
        catalogDescriptionColumn.setCellValueFactory(cellData -> cellData.getValue().getDescriptionProperty());
        catalogPriceColumn.setCellValueFactory(cellData -> cellData.getValue().getPriceProperty());
        catalogTable.setItems(viewModel.getCatalogTable());
        catalogTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> viewModel.setSelectedCatalogProductProperty(newVal));

        // Bindings for the rest of the user interface elements.
        usernameLabel.textProperty().bind(viewModel.getUsernameProperty());
        basketButton.textProperty().bind(viewModel.getBasketButtonTitleProperty());
        manageProductsButton.visibleProperty().bind(viewModel.getShowProductManagementButtonProperty());
        manageUsersButton.visibleProperty().bind(viewModel.getShowUserManagementButtonProperty());
        notificationCommentField.visibleProperty().bind(viewModel.getShowEventFieldProperty());
        Bindings.bindBidirectional(notificationCommentField.textProperty(), viewModel.getEventTextProperty());
        notificationButton.visibleProperty().bind(viewModel.getShowEventButtonProperty());
        errorLabel.textProperty().bind(viewModel.getErrorProperty());

        // Code for displaying new product notifications.
        viewModel.getShowNotificationProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                Image image = new Image("file:assets/cakeArt.png");

                ImageInput imageInput = new ImageInput();
                imageInput.setX(250);
                imageInput.setY(100);
                imageInput.setSource(image);

                //blur.setInput(imageInput);

                BoxBlur blur = new BoxBlur(3, 3, 3);

                JFXDialogLayout dialogLayout = new JFXDialogLayout();
                JFXButton button = new JFXButton("Okay");
                JFXDialog dialog = new JFXDialog(rootPane,dialogLayout,JFXDialog.DialogTransition.TOP);
                button.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> dialog.close());

                //unprofessional styling, but it works
                button.setStyle("-fx-background-color: #461d5e; -fx-text-fill: white;");
                dialog.setStyle("-fx-background-color: #ffb1b1cc;");
                dialogLayout.setStyle("-fx-background-color: #ffb1b1cc;");

                ProductViewModel newProduct = viewModel.getNewNotificationProduct();
                dialogLayout.setHeading(new Label("Check out this product, NOW!\n" + newProduct.getNameProperty().getValue() + "\n" + newProduct.getDescriptionProperty().getValue()));
                dialogLayout.setActions(button);
                dialog.show();
                dialog.setOnDialogClosed((JFXDialogEvent event) -> borderPane.setEffect(null));
                borderPane.setEffect(blur);
                viewModel.getShowNotificationProperty().set(false);
            }
        });

        // Code for displaying new event notifications.
        viewModel.getShowEventProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                Image image = new Image("file:assets/cakeArt.png");

                ImageInput imageInput = new ImageInput();
                imageInput.setX(250);
                imageInput.setY(100);
                imageInput.setSource(image);

                //blur.setInput(imageInput);

                BoxBlur blur = new BoxBlur(3, 3, 3);

                JFXDialogLayout dialogLayout = new JFXDialogLayout();
                JFXButton button = new JFXButton("Okay");
                JFXDialog dialog = new JFXDialog(rootPane,dialogLayout,JFXDialog.DialogTransition.CENTER);
                button.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> dialog.close());

                //unprofessional styling, but it works
                button.setStyle("-fx-background-color: #461d5e; -fx-text-fill: white;");
                dialog.setStyle("-fx-background-color: #ffb1b1cc;");
                dialogLayout.setStyle("-fx-background-color: #ffb1b1cc;");

                dialogLayout.setHeading(new Label("New Event!\n" + viewModel.getNewEventText()));
                dialogLayout.setActions(button);
                dialog.show();
                dialog.setOnDialogClosed((JFXDialogEvent event) -> borderPane.setEffect(null));
                borderPane.setEffect(blur);
                viewModel.getShowEventProperty().set(false);
            }
        });

        // Code for completed order notifications.
        viewModel.getShowCompletedOrderProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                Image image = new Image("file:assets/cakeArt.png");

                ImageInput imageInput = new ImageInput();
                imageInput.setX(250);
                imageInput.setY(100);
                imageInput.setSource(image);

                //blur.setInput(imageInput);

                BoxBlur blur = new BoxBlur(3, 3, 3);

                JFXDialogLayout dialogLayout = new JFXDialogLayout();
                JFXButton button = new JFXButton("Okay");
                JFXDialog dialog = new JFXDialog(rootPane,dialogLayout,JFXDialog.DialogTransition.CENTER);
                button.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> dialog.close());

                //unprofessional styling, but it works
                button.setStyle("-fx-background-color: #461d5e; -fx-text-fill: white;");
                dialog.setStyle("-fx-background-color: #ffb1b1cc;");
                dialogLayout.setStyle("-fx-background-color: #ffb1b1cc;");

                dialogLayout.setHeading(new Label("Congrats your order from " + viewModel.getCompletedOrder().getDate().getValue().toString() + " has been completed!"));
                dialogLayout.setActions(button);
                dialog.show();
                dialog.setOnDialogClosed((JFXDialogEvent event) -> borderPane.setEffect(null));
                borderPane.setEffect(blur);
                viewModel.getShowCompletedOrderProperty().set(false);
            }
        });
    }

    public void reset() {
        // Deselect any items upon reopening the window.
        catalogTable.getSelectionModel().clearSelection();
        viewModel.reset();
    }

    @FXML
    private void openBasketView() {
        try {
            viewHandler.openView(View.BASKET);
        } catch (Exception e) {
            viewModel.getErrorProperty().set("Can not manage the basket at this time. Try later.");
        }
    }

    @FXML
    private void openProductManagementView() {
        try {
            viewHandler.openView(View.MANAGEPRODUCTS);
        } catch (Exception e) {
            viewModel.getErrorProperty().set("Can not manage products at this time. Try later.");
        }
    }

    @FXML
    private void openUserManagementView() {
        try {
            viewHandler.openView(View.USERS);
        } catch (Exception e) {
            viewModel.getErrorProperty().set("Can not manage users at this time. Try later.");
        }
    }

    @FXML public void openOrdersView() {
        try {
            viewHandler.openView(View.ORDERS);
        } catch (Exception e) {
            viewModel.getErrorProperty().set("Can not view orders at this time. Try later.");
        }
    }

    @FXML
    private void addToBasket() {
        viewModel.addToBasket();
    }

    @FXML
    private void sendNotification() {
        viewModel.sendNotification();
    }

    @FXML
    private void deauthenticate() {
        if (!viewModel.deauthenticate()) {
            viewModel.getErrorProperty().set("Could not deauthenticate the user.");
            return;
        }
        try {
            viewHandler.openView(View.AUTHENTICATION);
        } catch (Exception e) {
            viewModel.getErrorProperty().set("Could not logout at this time. Try later.");
        }
    }
}
