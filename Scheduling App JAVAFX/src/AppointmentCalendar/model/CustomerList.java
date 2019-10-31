package AppointmentCalendar.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CustomerList {

    private static ObservableList<Customer> customerList = FXCollections.observableArrayList();


    public static ObservableList<Customer> getCustomerList(){
        return customerList;

    }




}
