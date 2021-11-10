package view;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Region;
import java.util.HashMap;
import java.util.Map;

public abstract class ViewCreator {

    private Map<String, ViewController> allInstances = new HashMap<>();

    public ViewController getViewController(View view) throws Exception {
        ViewController viewController = allInstances.get(view.getFxmlFile());
        if (viewController == null) {
            viewController = loadFromFXML(view.getFxmlFile());
            allInstances.put(view.getFxmlFile(), viewController);
        } else {
            viewController.reset();
        }
        return viewController;
    }

    protected abstract void initViewController(ViewController viewController, Region root);

    private ViewController loadFromFXML(String fxmlFile) throws Exception {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ViewController.class.getResource(fxmlFile));
            Region root = loader.load();
            ViewController viewController = loader.getController();
            initViewController(viewController, root);
            return viewController;
        } catch (Exception e) {
            throw new Exception("Could not load the fxml file: " + fxmlFile);
        }
    }
}
