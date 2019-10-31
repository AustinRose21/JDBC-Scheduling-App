package AppointmentCalendar.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AppointmentList {

    private static ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

    public static ObservableList<Appointment> getAppointmentList(){
        return appointmentList;
    }
}
