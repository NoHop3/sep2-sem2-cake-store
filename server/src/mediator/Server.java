package mediator;

import model.Model;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.ExportException;

public class Server {
    public Server(Model model) throws Exception {
        try {
            LocateRegistry.createRegistry(1099);
        } catch (ExportException e) {
            System.out.println("\u001b[35mRegistry already started.\u001b[39m");
        }
        new AuthenticationProxy(model);
        System.out.println("\u001b[35mServer started.\u001b[39m");
    }
}
