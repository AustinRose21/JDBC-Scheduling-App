package AppointmentCalendar.view_controller;
import AppointmentCalendar.model.Appointment;
import AppointmentCalendar.model.AppointmentList;
import AppointmentCalendar.model.DBConnector;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;



public class AddAppointmentController {


    @FXML
    private TextField idTextField;

    @FXML
    private TextField customerIdTextField;

    @FXML
    private TextField titleTextField;

    @FXML
    private TextField descriptionTextField;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    @FXML
    private TextField locationTextField;

    @FXML
    private TextField contactTextField;

    @FXML
    private TextField startHourTextField;

    @FXML
    private TextField urlTextField;

    @FXML
    private TextField startMinuteTextField;

    @FXML
    private TextField endHourTextField;

    @FXML
    private TextField endMinuteTextField;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private TextField userIdTextField;

    @FXML
    private TextField apptTypeTextField;


    @FXML
    void handleCancelAddAppointment(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Back to Main!");
        alert.setHeaderText("Main Screen!");
        alert.setContentText("Going back to main screen");
        //Lambda to make code cleaner on notifying user they are navigating back to the main screen.
        alert.showAndWait().ifPresent((response -> {
            if (response == ButtonType.OK) {
                System.out.println("Going back to main screen!");

                try {
                    Parent calendarScreen = FXMLLoader.load(getClass().getResource("CalendarScreen.fxml"));
                    Scene calendarScene = new Scene(calendarScreen);
                    //next line is getting stage information
                    Stage calendarStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    calendarStage.setScene((calendarScene));
                    calendarStage.show();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }));

    }


    @FXML
    public void handleSaveAddAppointment(ActionEvent event) throws ClassNotFoundException {


        int apptId = DBConnector.getLatestAppointmentId();
        int apptCustomerId = Integer.parseInt(customerIdTextField.getText());
        int userId = Integer.parseInt(userIdTextField.getText());
        String apptTitle = titleTextField.getText();
        String apptType = apptTypeTextField.getText();
        String apptDescription = descriptionTextField.getText();
        String apptLocation = locationTextField.getText();
        String apptContact = contactTextField.getText();
        String apptUrl = urlTextField.getText();
        LocalDate start = startDatePicker.getValue();
        LocalDate end = endDatePicker.getValue();
        String startHour = startHourTextField.getText();
        String startMinute = startMinuteTextField.getText();
        String endHour = endHourTextField.getText();
        String endMinute = endMinuteTextField.getText();
        Date startDate = Date.valueOf(start);
        Date endDate = Date.valueOf(end);


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        String startTime = start + " " + startHour + ":" + startMinute;
        String endTime = end + " " + endHour + ":" + endMinute;

        //converting start/end to localDateTime
        LocalTime localStartTime = LocalTime.parse(startTime, formatter);
        LocalTime localEndTime = LocalTime.parse(endTime, formatter);

        LocalDateTime startDateTime = LocalDateTime.of(start, localStartTime);
        LocalDateTime endDateTime = LocalDateTime.of(end, localEndTime);

        //setting local time for zonedatetime
        ZoneId localId = ZoneId.systemDefault();
        ZonedDateTime zonedStartLocal = startDateTime.atZone(localId);
        ZonedDateTime zonedEndLocal = endDateTime.atZone(localId);

        System.out.println("Local Time: " + zonedStartLocal);
        System.out.println("Local Time end: " + zonedEndLocal);

        //converting to UTC
        ZonedDateTime utcStart = zonedStartLocal.withZoneSameInstant(ZoneId.of("UTC"));
        ZonedDateTime utcEnd = zonedEndLocal.withZoneSameInstant(ZoneId.of("UTC"));

        System.out.println("Zoned time UTC: " + utcStart);
        System.out.println("Zoned end time UTC: " + utcEnd);

        //timestamp of LocalDateTime
        LocalDateTime localZonedStart = utcStart.toLocalDateTime();
        LocalDateTime localZonedEnd = utcEnd.toLocalDateTime();

        Timestamp insertStart = Timestamp.valueOf(localZonedStart);
        Timestamp insertEnd = Timestamp.valueOf(localZonedEnd);

        int startHourText = Integer.parseInt(startHourTextField.getText());


        if (startHourText < 18 && startHourText > 4) {
            try {

                Statement stmt = DBConnector.makeConnection().createStatement();
                Statement stmt2 = DBConnector.makeConnection().createStatement();
                ResultSet resultSet = stmt.executeQuery("SELECT * FROM appointment WHERE start BETWEEN '" + insertStart + "' AND '" + insertEnd + "'");
                ResultSet resultSet1 = stmt2.executeQuery("SELECT * FROM customer WHERE customerId = " + apptCustomerId + "");
                if (resultSet.next()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setHeaderText("Overlapping appointments");
                    alert.setContentText("You cannot schedule overlapping appointments");
                    alert.showAndWait();
                    resultSet.close();
                } else if (!resultSet1.next()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setHeaderText("Customer Invalid");
                    alert.setContentText("Please enter a valid customer Id");
                    alert.showAndWait();
                    resultSet1.close();
                } else {


                    //create new appointment and add to obs list
                    Appointment addedAppointment = new Appointment(apptId, apptCustomerId, userId, apptTitle, apptDescription, apptLocation, apptContact, apptType, apptUrl, localZonedStart, localZonedEnd);
                    addedAppointment.setAppointmentId(apptId);
                    addedAppointment.setCustomerId(apptCustomerId);
                    addedAppointment.setUserId(userId);
                    addedAppointment.setTitle(apptTitle);
                    addedAppointment.setApptType(apptType);
                    addedAppointment.setLocation(apptLocation);
                    addedAppointment.setContact(apptContact);
                    addedAppointment.setUrl(apptUrl);
                    addedAppointment.setStartDate(startDate);
                    addedAppointment.setEndDate(endDate);
                    addedAppointment.setDescription(apptDescription);
                    AppointmentList.getAppointmentList().add(addedAppointment);


                    //add to DB
                    DBConnector.addAppointmentRecord(apptId, apptCustomerId, userId, apptTitle, apptDescription, apptLocation, apptContact, apptType, apptUrl, insertStart, insertEnd);
                    System.out.println("appointment added to DB no errors");

                    System.out.println(apptId);
                    System.out.println(apptCustomerId);
                    System.out.println(userId);
                    System.out.println(apptTitle);
                    System.out.println(apptDescription);
                    System.out.println(apptLocation);
                    System.out.println(apptContact);
                    System.out.println(apptType);
                    System.out.println(apptUrl);
                    System.out.println(insertStart);
                    System.out.println(insertEnd);


                    //set the stage back to calendar screen after inserting/adding to observable list
                    Parent calendarScreen = FXMLLoader.load(getClass().getResource("CalendarScreen.fxml"));
                    Scene calendarScene = new Scene(calendarScreen);
                    //next line is getting stage information
                    Stage calendarStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    calendarStage.setScene((calendarScene));
                    calendarStage.show();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }


        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Time Error");
            alert.setHeaderText("Schedule Conflict");
            alert.setContentText("Please schedule appointment hour start time during business hours");
            //Lambda to make code cleaner on notifying user they are navigating back to the main screen.
            alert.showAndWait();
        }

    }
}

