package AppointmentCalendar.view_controller;

import AppointmentCalendar.model.Customer;
import AppointmentCalendar.model.CustomerList;
import AppointmentCalendar.model.DBConnector;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class AddCustomerController {
    @FXML
    private TextField cityIDTextField;

    @FXML
    private TextField zipCodeTextField;


    @FXML
    private TextField idTextField;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField addressTextField;

    @FXML
    private TextField phoneTextField;

    @FXML
    private TextField addressIDTextField;

    private String errorMessage;

    @FXML
    void handleCancelAddCustomer(ActionEvent event) throws IOException {

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
    void handleSaveAddCustomer(ActionEvent event) {

        int customerId = Integer.parseInt(idTextField.getText());
        String customerName = nameTextField.getText();
        int address = Integer.parseInt(addressIDTextField.getText());
        String fullAddress = addressTextField.getText();
        String phoneNumber = phoneTextField.getText();
        int cityId = Integer.parseInt(cityIDTextField.getText());
        String zipCode = zipCodeTextField.getText();

        int active = 1;

                            if (customerName != null || address != 0) {
                               try {
                                   Customer addedCustomer = new Customer(customerId, customerName, active, address);
                                   addedCustomer.setCustomerId(customerId);
                                   addedCustomer.setCustomerName(customerName);
                                   addedCustomer.setActiveCustomer(active);
                                   addedCustomer.setAddressId(address);
                                   CustomerList.getCustomerList().add(addedCustomer);

                                   DBConnector.addCustomerRecord(customerName, address, active, fullAddress, cityId, zipCode, phoneNumber);
                                   System.out.println("customer was added");


                                   Parent calendarScreen = FXMLLoader.load(getClass().getResource("CalendarScreen.fxml"));
                                   Scene calendarScene = new Scene(calendarScreen);
                                   //next line is getting stage information
                                   Stage calendarStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                                   calendarStage.setScene((calendarScene));
                                   calendarStage.show();
                                   System.out.println("db branch is running");
                               } catch (Exception e) {
                                   e.printStackTrace();
                               }

                           }



                   }
       }



