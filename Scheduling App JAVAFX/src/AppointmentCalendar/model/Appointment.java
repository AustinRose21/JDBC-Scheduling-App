package AppointmentCalendar.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Appointment {

    private final IntegerProperty appointmentId;
    private final IntegerProperty customerId;
    private IntegerProperty userId;
    private final StringProperty title;
    private final StringProperty location;
    private final StringProperty contact;
    private final StringProperty url;
    private StringProperty apptType;
    private  Timestamp startTimeStamp;
    private  Timestamp endTimeStamp;
    private Date startDate;
    private Date endDate;
    private LocalDateTime startLocalDateTime;
    private LocalDateTime endLocalDateTime;

    private  StringProperty description;




    //partial constructor
    public Appointment(int appointmentId, int customerId, int userId, String title, String apptType, String location, String contact, String url, Date start, Date end, String description){
        this.appointmentId = new SimpleIntegerProperty(appointmentId);
        this.customerId = new SimpleIntegerProperty(customerId);
        this.userId = new SimpleIntegerProperty(userId);
        this.title = new SimpleStringProperty(title);
        this.apptType = new SimpleStringProperty(apptType);
        this.location = new SimpleStringProperty(location);
        this.contact = new SimpleStringProperty(contact);
        this.url = new SimpleStringProperty(url);
        this.startDate = start;
        this.endDate = end;

        this.description = new SimpleStringProperty(description);

    }

    //partial constructor
    public Appointment(int appointmentId, int customerId, int userId, String title, String description, String location, String contact, String apptType, String url, Date start, Date end){
        this.appointmentId = new SimpleIntegerProperty(appointmentId);
        this.customerId = new SimpleIntegerProperty(customerId);
        this.userId = new SimpleIntegerProperty(userId);
        this.title = new SimpleStringProperty(title);
        this.apptType = new SimpleStringProperty(apptType);
        this.location = new SimpleStringProperty(location);
        this.contact = new SimpleStringProperty(contact);
        this.url = new SimpleStringProperty(url);
        this.startDate = start;
        this.endDate = end;

        this.description = new SimpleStringProperty(description);

    }

    //full constructor with LocalDateTime
    public Appointment(int appointmentId, int customerId, int userId, String title, String description, String location, String contact, String apptType, String url, LocalDateTime startDate,
                       LocalDateTime endDate) {
        this.appointmentId = new SimpleIntegerProperty(appointmentId);
        this.customerId = new SimpleIntegerProperty(customerId);
        this.userId = new SimpleIntegerProperty(userId);
        this.title = new SimpleStringProperty(title);
        this.apptType = new SimpleStringProperty(apptType);
        this.location = new SimpleStringProperty(location);
        this.contact = new SimpleStringProperty(contact);
        this.url = new SimpleStringProperty(url);
        this.startLocalDateTime = startDate;
        this.endLocalDateTime = endDate;
        this.description = new SimpleStringProperty(description);
    }


    //full constructor with timestamps
    public Appointment(int appointmentId, int customerId, int userId, String title, String apptType, String location, String contact, String url, Timestamp startTimeStamp,
                       Timestamp endTimeStamp, String description) {
        this.appointmentId = new SimpleIntegerProperty(appointmentId);
        this.customerId = new SimpleIntegerProperty(customerId);
        this.userId = new SimpleIntegerProperty(userId);
        this.title = new SimpleStringProperty(title);
        this.apptType = new SimpleStringProperty(apptType);
        this.location = new SimpleStringProperty(location);
        this.contact = new SimpleStringProperty(contact);
        this.url = new SimpleStringProperty(url);
        this.startTimeStamp = startTimeStamp;
        this.endTimeStamp = endTimeStamp;
        this.description = new SimpleStringProperty(description);
    }



    //setters
    public void setAppointmentId(int appointmentId) {
        this.appointmentId.set(appointmentId);
    }

    public void setCustomerId(int customerId) {
        this.customerId.set(customerId);
    }

    public void setUserId(int userId) {this.userId.set(userId);}

    public void setTitle(String title) {
        this.title.set(title);
    }

    public void setApptType(String apptType) {this.apptType.set(apptType);}

    public void setLocation(String location) {
        this.location.set(location);
    }

    public void setContact(String contact) {
        this.contact.set(contact);
    }

    public void setUrl(String url) {
        this.url.set(url);
    }

    public void setDescription(String description) {this.description.set(description);}

    public void setStartTimeStamp(Timestamp startTimeStamp) {this.startTimeStamp = startTimeStamp;}

    public void setEndLocalDate(Timestamp endTimeStamp) {this.endTimeStamp = endTimeStamp;}

    public void setEndDate(Date endDate) {this.endDate = endDate;}

    public void setStartDate (Date startDate) {this.startDate = startDate;}





    //getters


    public int getAppointmentId() {
        return appointmentId.get();
    }

    public int getCustomerId() {
        return customerId.get();
    }

    public int getUserId() {return userId.get();}

    public String getTitle() {
        return title.get();
    }

    public String getApptType() {return apptType.get();}

    public String getDescription(){return description.get();}

    public String getLocation() {
        return location.get();
    }

    public String getContact() {
        return contact.get();
    }

    public String getUrl() {
        return url.get();
    }

   // public LocalDateTime getStartLocalDate() { return startLocalDateTime; }

    public LocalDateTime getStartDate() {
        return startLocalDateTime;
    }

   // public LocalDateTime getEndLocalDate() { return endLocalDateTime; }

    public LocalDateTime getEndDate() { return endLocalDateTime; }

   // public Date getStartDate() {return startDate;}

   // public Date getEndDate() {return endDate;}

    public Timestamp getStartTimeStamp() {return startTimeStamp;}

    public Timestamp getEndTimeStamp() {return endTimeStamp;}


}






