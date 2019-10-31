package AppointmentCalendar.view_controller;
import AppointmentCalendar.model.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.event.ActionEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.*;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.ResourceBundle;

public class CalendarScreenController  implements Initializable {


    @FXML
    private TableView<Appointment> tvAppointment;

    @FXML
    private TableColumn<Appointment, Integer> tvColumnAppointmentId;

    @FXML
    private TableColumn<Appointment, Integer> tvColumnAppointmentCustomer;

    @FXML
    private TableColumn<Appointment, String> tvColumnAppointmentTitle;

    @FXML
    private TableColumn<Appointment, String> tvColumnAppointmentLocation;

    @FXML
    private TableColumn<Appointment, String> tvColumnAppointmentContact;

    @FXML
    private TableColumn<Appointment, LocalDateTime> tvColumnAppointmentStart;

    @FXML
    private TableColumn<Appointment, LocalDateTime> tvColumnAppointmentEnd;


    @FXML
    private TableColumn<Appointment, String> tvColumnAppointmentUrl;

    @FXML
    private TableColumn<Appointment, String> tvColumnAppointmentDescription;

    @FXML
    private Button addAppointmentButton;

    @FXML
    private Button modifyAppointmentButton;

    @FXML
    private Button deleteAppointmentButton;

    @FXML
    private TableView<Customer> tvCustomer;

    @FXML
    private TableColumn<Customer, Integer> tvColumnCustomerId;

    @FXML
    private TableColumn<Customer, String> tvColumnCustomerName;

    @FXML
    private TableColumn<Customer, Integer> tvColumnCustomerAddress;

    @FXML
    private TableColumn<Customer, Integer> tvColumnActiveUser;

    @FXML
    private TableColumn<Customer, String> tvColumnPhoneNumber;

    @FXML
    private Button addCustomerButton;

    @FXML
    private Button modifyCustomerButton;

    @FXML
    private Button deleteCustomerButton;

    @FXML
    private Button filterApptWeekButton;

    @FXML
    private Button filterApptMonthButton;

    @FXML
    private LocalDateTime currentDate = LocalDateTime.now();

    @FXML
    private LocalDateTime currentDatePlusSeven = currentDate.plusDays(7);

    @FXML
    private LocalDateTime currentDatePlusThirty = currentDate.plusDays(30);


    private static int modifyCustomerIndex;
    private static int modifyAppointmentIndex;

    public static int getModifyCustomerIndex() {
        return modifyCustomerIndex;
    }

    public static int getModifyAppointmentIndex() {
        return modifyAppointmentIndex;
    }


    @FXML
    public void handleAddAppointment(ActionEvent event) throws IOException {
        Parent addAppointmentScreen = FXMLLoader.load(getClass().getResource("AddAppointment.fxml"));
        Scene addAppointmentScene = new Scene(addAppointmentScreen);
        //next line is getting stage information
        Stage addAppointmentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        addAppointmentStage.setScene((addAppointmentScene));
        addAppointmentStage.show();
    }

    @FXML
    public void handleModifyAppointment(ActionEvent event) throws IOException {

        Appointment appointmentToModify = tvAppointment.getSelectionModel().getSelectedItem();
        modifyAppointmentIndex = AppointmentList.getAppointmentList().indexOf(appointmentToModify);

        Parent addAppointmentScreen = FXMLLoader.load(getClass().getResource("ModifyAppointment.fxml"));
        Scene addAppointmentScene = new Scene(addAppointmentScreen);
        //next line is getting stage information
        Stage addAppointmentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        addAppointmentStage.setScene((addAppointmentScene));
        addAppointmentStage.show();
    }

    {

    }

    @FXML
    public void handleDeleteAppointment(ActionEvent event) throws IOException {
        // Get selected appointment from table view
        Appointment appointmentToRemove = tvAppointment.getSelectionModel().getSelectedItem();
        // Check if no customer was selected
        if (appointmentToRemove == null) {
            // Create alert saying a customer must be selected to be removed
            ResourceBundle rb = ResourceBundle.getBundle("MainScreen", Locale.getDefault());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(rb.getString("error"));
            alert.setHeaderText(rb.getString("errorRemovingAppointment"));
            alert.setContentText(rb.getString("errorRemovingAppointmentMessage"));
            alert.showAndWait();
            return;
        }


        DBConnector.deleteAppointmentRecord(appointmentToRemove);


        Parent calendarScreen = FXMLLoader.load(getClass().getResource("CalendarScreen.fxml"));
        Scene calendarScene = new Scene(calendarScreen);
        //next line is getting stage information
        Stage calendarStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        calendarStage.setScene((calendarScene));
        calendarStage.show();

    }

    @FXML
    public void handleAddCustomer(ActionEvent event) throws IOException {
        Parent addAppointmentScreen = FXMLLoader.load(getClass().getResource("AddCustomer.fxml"));
        Scene addAppointmentScene = new Scene(addAppointmentScreen);
        //next line is getting stage information
        Stage addAppointmentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        addAppointmentStage.setScene((addAppointmentScene));
        addAppointmentStage.show();
    }

    {

    }

    @FXML
    public void handleModifyCustomer(ActionEvent event) throws IOException {
        Customer customerToModify = tvCustomer.getSelectionModel().getSelectedItem();
        modifyCustomerIndex = CustomerList.getCustomerList().indexOf(customerToModify);


        Parent addAppointmentScreen = FXMLLoader.load(getClass().getResource("ModifyCustomer.fxml"));
        Scene addAppointmentScene = new Scene(addAppointmentScreen);
        //next line is getting stage information
        Stage addAppointmentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        addAppointmentStage.setScene((addAppointmentScene));
        addAppointmentStage.show();
    }

    {

    }

    @FXML
    public void handleDeleteCustomer(ActionEvent event) throws IOException {
        // Get selected customer from table view
        Customer customerToRemove = tvCustomer.getSelectionModel().getSelectedItem();
        // Check if no customer was selected
        if (customerToRemove == null) {
            // Create alert saying a customer must be selected to be removed
            ResourceBundle rb = ResourceBundle.getBundle("MainScreen", Locale.getDefault());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(rb.getString("error"));
            alert.setHeaderText(rb.getString("errorRemovingCustomer"));
            alert.setContentText(rb.getString("errorRemovingCustomerMessage"));
            alert.showAndWait();
            return;
        }


        DBConnector.deleteCustomerRecord(customerToRemove);


        Parent calendarScreen = FXMLLoader.load(getClass().getResource("CalendarScreen.fxml"));
        Scene calendarScene = new Scene(calendarScreen);
        //next line is getting stage information
        Stage calendarStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        calendarStage.setScene((calendarScene));
        calendarStage.show();

    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            AppointmentList.getAppointmentList().clear();
            DBConnector.upDateAppointmentQuery();
            tvAppointment.setItems(AppointmentList.getAppointmentList());
            tvColumnAppointmentId.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("appointmentId"));
            tvColumnAppointmentCustomer.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("customerId"));
            tvColumnAppointmentTitle.setCellValueFactory(new PropertyValueFactory<Appointment, String>("title"));
            tvColumnAppointmentLocation.setCellValueFactory(new PropertyValueFactory<Appointment, String>("location"));
            tvColumnAppointmentContact.setCellValueFactory(new PropertyValueFactory<Appointment, String>("contact"));
            tvColumnAppointmentStart.setCellValueFactory(new PropertyValueFactory<Appointment, LocalDateTime>("startDate"));
            tvColumnAppointmentEnd.setCellValueFactory(new PropertyValueFactory<Appointment, LocalDateTime>("endDate"));
            tvColumnAppointmentUrl.setCellValueFactory(new PropertyValueFactory<Appointment, String>("url"));
            tvColumnAppointmentDescription.setCellValueFactory(new PropertyValueFactory<Appointment, String>("description"));


        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            CustomerList.getCustomerList().clear();
            DBConnector.upDateCustomerQuery();
            tvCustomer.setItems(CustomerList.getCustomerList());
            tvColumnCustomerId.setCellValueFactory(new PropertyValueFactory<Customer, Integer>("customerId"));
            tvColumnCustomerName.setCellValueFactory(new PropertyValueFactory<Customer, String>("customerName"));
            tvColumnCustomerAddress.setCellValueFactory(new PropertyValueFactory<Customer, Integer>("addressId"));
            tvColumnActiveUser.setCellValueFactory(new PropertyValueFactory<Customer, Integer>("activeCustomer"));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @FXML
    void handleFilterApptMonth(ActionEvent event) {
        AppointmentList.getAppointmentList().clear();
        DBConnector.upDateAppointmentQueryByMonth(Timestamp.valueOf(currentDate), Timestamp.valueOf(currentDatePlusThirty));



        tvAppointment.setItems(AppointmentList.getAppointmentList());
        tvColumnAppointmentId.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("appointmentId"));
        tvColumnAppointmentCustomer.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("customerId"));
        tvColumnAppointmentTitle.setCellValueFactory(new PropertyValueFactory<Appointment, String>("title"));
        tvColumnAppointmentLocation.setCellValueFactory(new PropertyValueFactory<Appointment, String>("location"));
        tvColumnAppointmentContact.setCellValueFactory(new PropertyValueFactory<Appointment, String>("contact"));
        tvColumnAppointmentStart.setCellValueFactory(new PropertyValueFactory<Appointment, LocalDateTime>("startDate"));
        tvColumnAppointmentEnd.setCellValueFactory(new PropertyValueFactory<Appointment, LocalDateTime>("endDate"));
        tvColumnAppointmentUrl.setCellValueFactory(new PropertyValueFactory<Appointment, String>("url"));
        tvColumnAppointmentDescription.setCellValueFactory(new PropertyValueFactory<Appointment, String>("description"));


    }

    @FXML
    void handleFilterApptWeek(ActionEvent event) throws IOException {
        AppointmentList.getAppointmentList().clear();
        DBConnector.upDateAppointmentQueryByWeek(Timestamp.valueOf(currentDate), Timestamp.valueOf(currentDatePlusSeven));


        tvAppointment.setItems(AppointmentList.getAppointmentList());
        tvColumnAppointmentId.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("appointmentId"));
        tvColumnAppointmentCustomer.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("customerId"));
        tvColumnAppointmentTitle.setCellValueFactory(new PropertyValueFactory<Appointment, String>("title"));
        tvColumnAppointmentLocation.setCellValueFactory(new PropertyValueFactory<Appointment, String>("location"));
        tvColumnAppointmentContact.setCellValueFactory(new PropertyValueFactory<Appointment, String>("contact"));
        tvColumnAppointmentStart.setCellValueFactory(new PropertyValueFactory<Appointment, LocalDateTime>("startDate"));
        tvColumnAppointmentEnd.setCellValueFactory(new PropertyValueFactory<Appointment, LocalDateTime>("endDate"));
        tvColumnAppointmentUrl.setCellValueFactory(new PropertyValueFactory<Appointment, String>("url"));
        tvColumnAppointmentDescription.setCellValueFactory(new PropertyValueFactory<Appointment, String>("description"));


    }

    @FXML
    void handleGoToReports(ActionEvent event) throws IOException {
        Parent reportsScreen = FXMLLoader.load(getClass().getResource("Reports.fxml"));
        Scene reportsScene = new Scene(reportsScreen);
        //next line is getting stage information
        Stage calendarStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        calendarStage.setScene((reportsScene));
        calendarStage.show();
    }
}






