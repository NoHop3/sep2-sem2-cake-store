package view;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import viewmodel.ViewModelFactory;

public class ViewHandler extends ViewCreator {

    private ViewModelFactory viewModelFactory;
    private Scene currentScene;
    private Stage primaryStage;

    public ViewHandler(ViewModelFactory viewModelFactory) {
        this.viewModelFactory = viewModelFactory;
        this.currentScene = new Scene(new Region());
    }

    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        openView(View.AUTHENTICATION);
    }

    public void openView(View view) throws Exception {
        ViewController viewController = getViewController(view);
        Region root = viewController.getRoot();
        currentScene.setRoot(root);

        String title;
        primaryStage.getIcons().add(new Image("file:assets/cake2.png"));

        primaryStage.setTitle((title = (String) root.getUserData()) != null ? title : "");
        primaryStage.setScene(currentScene);
        primaryStage.setWidth(root.getPrefWidth() + 16);
        primaryStage.setHeight(root.getPrefHeight() + 39);
        primaryStage.setResizable(false);
        viewController.reset();
        primaryStage.show();
    }

    @Override
    protected void initViewController(ViewController viewController, Region root) {
        viewController.init(this, viewModelFactory, root);
    }
}
