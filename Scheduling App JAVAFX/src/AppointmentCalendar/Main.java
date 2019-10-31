package AppointmentCalendar;
import AppointmentCalendar.model.DBConnector;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.sql.SQLException;
import java.util.logging.Logger;



public class Main extends Application  {

    Logger logger = Logger.getLogger("log.txt");



    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/AppointmentCalendar/view_controller/LoginForm.fxml"));
        primaryStage.setTitle("Login Form");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }{
        try {
            DBConnector.makeConnection();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

}
