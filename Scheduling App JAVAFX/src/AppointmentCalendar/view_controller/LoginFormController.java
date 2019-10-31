package AppointmentCalendar.view_controller;



import AppointmentCalendar.model.Appointment;
import AppointmentCalendar.model.DBConnector;
import AppointmentCalendar.model.Logger;
import AppointmentCalendar.model.User;
import com.mysql.cj.protocol.Resultset;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javafx.event.ActionEvent;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Locale;
import java.util.ResourceBundle;


public class LoginFormController implements Initializable {

    @FXML
    private Label userLogInLabel;

    @FXML
    private TextField passwordTextField;

    @FXML
    private TextField usernameTextField;

    @FXML
    private Button loginButton;

    @FXML
    private Label passwordLabel;

    @FXML
    private Label usernameLabel;

    @FXML
    private Label mainMessage;

    @FXML
    private Label languageMessage;



    private String errorHeader;
    private String errorTitle;
    private String errorText;






    @FXML
    public void handleLogin (ActionEvent event) throws SQLException, ClassNotFoundException, IOException {
        String userName = usernameTextField.getText();
        String password = passwordTextField.getText();

        //check if credentials are correct in DB
        if (DBConnector.isLoginValid(userName, password)) {

            try {

                LocalDateTime now = LocalDateTime.now();
                ZoneId currentZone = ZoneId.systemDefault();
                ZonedDateTime zdt = now.atZone(currentZone);
                LocalDateTime ldt1 = zdt.withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();
                LocalDateTime ldt2 = ldt1.plusMinutes(15);


                Statement stmt = DBConnector.makeConnection().createStatement();
                ResultSet resultset = stmt.executeQuery("SELECT * FROM appointment WHERE start BETWEEN '" + Timestamp.valueOf(ldt1) + "' AND '" + Timestamp.valueOf(ldt2)+ "'");
                if (resultset.next()) {
                    JOptionPane.showMessageDialog(null, "You have an upcoming appointment in 15 minutes");
                    Logger.log(userName);
                    Parent calendarScreen = FXMLLoader.load(getClass().getResource("CalendarScreen.fxml"));
                    Scene calendarScene = new Scene(calendarScreen);
                    //next line is getting stage information
                    Stage calendarStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    calendarStage.setScene((calendarScene));
                    calendarStage.show();
                } else {

                    Logger.log(userName);
                    System.out.println(userName);

                    Parent calendarScreen = FXMLLoader.load(getClass().getResource("CalendarScreen.fxml"));
                    Scene calendarScene = new Scene(calendarScreen);
                    //next line is getting stage information
                    Stage calendarStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    calendarStage.setScene((calendarScene));
                    calendarStage.show();
                }
            }
            catch (SQLException e){
                e.printStackTrace();
            }
    }


            else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(errorTitle);
                alert.setHeaderText(errorHeader);
                alert.setContentText(errorText);
                alert.showAndWait();
            }

        }


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Locale locale = Locale.getDefault();
        rb = ResourceBundle.getBundle("AppointmentCalendar/Languages/login", locale);
        usernameLabel.setText(rb.getString("username"));
        passwordLabel.setText(rb.getString("password"));
        loginButton.setText(rb.getString("login"));
        mainMessage.setText(rb.getString("message"));
        languageMessage.setText(rb.getString("language"));
        errorHeader = rb.getString("errorheader");
        errorTitle = rb.getString("errortitle");
        errorText = rb.getString("errortext");
    }
}







