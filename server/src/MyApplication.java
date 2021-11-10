import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import mediator.Server;
import model.Model;
import model.ModelManager;
import java.rmi.RemoteException;
import java.sql.SQLException;

public class MyApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            Model model = new ModelManager();
            Server server = new Server(model);
        } catch (Exception e) {
            System.err.println("Could not start the server.");
            System.err.println(e instanceof SQLException ? "Server could not reach the database." : e instanceof RemoteException ? "Server could not export the registry." : "Server could not start the authentication proxy.");
            System.err.println(e.getMessage());
            Platform.exit();
        }
    }
}
