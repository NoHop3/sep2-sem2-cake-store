import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import model.Model;
import model.ModelManager;
import view.ViewHandler;
import viewmodel.ViewModelFactory;

public class MyApplication extends Application {

    private Model model;

    @Override
    public void start(Stage primaryStage) {
        try {
            model = new ModelManager();
            ViewModelFactory viewModelFactory = new ViewModelFactory(model);
            ViewHandler viewHandler = new ViewHandler(viewModelFactory);
            viewHandler.start(primaryStage);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            Platform.exit();
        }
    }

    // Needed in order to stop all the RMI client communication threads to the server.
    // Otherwise the running threads won't allow the application process to close.
    @Override
    public void stop() {
        if (model != null) model.stop();
    }
}
