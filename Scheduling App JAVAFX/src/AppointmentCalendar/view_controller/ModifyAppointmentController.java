package AppointmentCalendar.view_controller;

import AppointmentCalendar.model.Appointment;
import AppointmentCalendar.model.AppointmentList;
import AppointmentCalendar.model.DBConnector;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class ModifyAppointmentController implements Initializable {
    @FXML
    private TextField idTextField;

    @FXML
    private TextField customerIdTextField;

    @FXML
    private TextField userIdTextField;

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
    private TextField apptTypeTextField;


    Appointment appointmentToBeModified = AppointmentList.getAppointmentList().get(CalendarScreenController.getModifyAppointmentIndex());

    @FXML
    void handleCancelModifyAppointment(ActionEvent event) throws IOException {
        Parent calendarScreen = FXMLLoader.load(getClass().getResource("CalendarScreen.fxml"));
        Scene calendarScene = new Scene(calendarScreen);
        //next line is getting stage information
        Stage calendarStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        calendarStage.setScene((calendarScene));
        calendarStage.show();

    }

    @FXML
    void handleSaveModifyAppointment(ActionEvent event) {

        int apptId = appointmentToBeModified.getAppointmentId();
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

        java.sql.Date startDate = java.sql.Date.valueOf(start);
        java.sql.Date endDate = Date.valueOf(end);


        //converting date/time to LocalDateTime
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        String startTime = start + " " + startHour + ":" + startMinute;
        String endTime = end + " " + endHour + ":" + endMinute;

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

        System.out.println("Zoned time UTC: " + utcStart );
        System.out.println("Zoned end time UTC: " + utcEnd);

        //timestamp of LocalDateTime
        LocalDateTime localZonedStart = utcStart.toLocalDateTime();
        LocalDateTime localZonedEnd = utcEnd.toLocalDateTime();

        Timestamp insertStart = Timestamp.valueOf(localZonedStart);
        Timestamp insertEnd = Timestamp.valueOf(localZonedEnd);



        try {
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
            DBConnector.updateAppointmentRecord(apptCustomerId, userId, apptTitle, apptDescription, apptLocation, apptContact, apptType, apptUrl, insertStart,insertEnd);


            Parent calendarScreen = FXMLLoader.load(getClass().getResource("CalendarScreen.fxml"));
            Scene calendarScene = new Scene(calendarScreen);
            //next line is getting stage information
            Stage calendarStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            calendarStage.setScene((calendarScene));
            calendarStage.show();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
        // initialize method that loads the screen with modified appointment information
        @Override
        public void initialize (URL url, ResourceBundle rb){



            customerIdTextField.setText(String.valueOf(appointmentToBeModified.getCustomerId()));
            userIdTextField.setText(String.valueOf(appointmentToBeModified.getUserId()));
            titleTextField.setText(appointmentToBeModified.getTitle());
            descriptionTextField.setText(appointmentToBeModified.getDescription());
            locationTextField.setText(appointmentToBeModified.getLocation());
            contactTextField.setText(appointmentToBeModified.getContact());
            urlTextField.setText(appointmentToBeModified.getUrl());
           // startDatePicker.setValue(appointmentToBeModified.getStartLocalDate().toLocalDate());
            //endDatePicker.setValue(appointmentToBeModified.getEndLocalDate().toLocalDate());

        }

    }



