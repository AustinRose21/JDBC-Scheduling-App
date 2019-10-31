package AppointmentCalendar.view_controller;

import AppointmentCalendar.model.Customer;
import AppointmentCalendar.model.CustomerList;
import AppointmentCalendar.model.DBConnector;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;

public class ModifyCustomerController implements Initializable {

    @FXML
    private TextField idTextField;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField addressTextField;

    @FXML
    private Button saveButton;

    @FXML
    private TextField cityIDTextField;

    @FXML
    private TextField zipCodeTextField;

    @FXML
    private TextField phoneTextField;

    @FXML
    private TextField addressIDTextField;

    Customer customerToBeModified = CustomerList.getCustomerList().get(CalendarScreenController.getModifyCustomerIndex());

    @FXML
    void handleCancelModifyCustomer(ActionEvent event) throws IOException {
        Parent calendarScreen = FXMLLoader.load(getClass().getResource("CalendarScreen.fxml"));
        Scene calendarScene = new Scene(calendarScreen);
        //next line is getting stage information
        Stage calendarStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        calendarStage.setScene((calendarScene));
        calendarStage.show();

    }

    @FXML
    void handleSaveModifyCustomer(ActionEvent event) throws IOException {

        int oldId = customerToBeModified.getCustomerId();
        int customerId = Integer.parseInt(idTextField.getText());
        String customerName = nameTextField.getText();
        String fullAddress = addressTextField.getText();
        int cityID = Integer.parseInt(cityIDTextField.getText());
        String zipCode = zipCodeTextField.getText();
        String phoneNumber = phoneTextField.getText();
        int addressId = Integer.parseInt(addressIDTextField.getText());
        int oldAddressID = customerToBeModified.getAddressId();


        int active = customerToBeModified.getActiveCustomer();
        Date createDate = customerToBeModified.getCreateDate();
        System.out.println(createDate);


        DBConnector.updateCustomerRecord(customerId, customerName, addressId, active, oldId, fullAddress,cityID, zipCode, phoneNumber, oldAddressID);

        System.out.println("Customer updated successfully");


        Parent calendarScreen = FXMLLoader.load(getClass().getResource("CalendarScreen.fxml"));
        Scene calendarScene = new Scene(calendarScreen);
        //next line is getting stage information
        Stage calendarStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        calendarStage.setScene((calendarScene));
        calendarStage.show();

    }




    // initialize method that loads the screen with modified customer information
    @Override
    public void initialize (URL url, ResourceBundle rb) {


        nameTextField.setText(customerToBeModified.getCustomerName());
        idTextField.setText(String.valueOf(customerToBeModified.getCustomerId()));
        addressIDTextField.setText(String.valueOf(customerToBeModified.getAddressId()));



    }

}
