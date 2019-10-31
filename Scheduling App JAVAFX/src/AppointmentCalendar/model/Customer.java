package AppointmentCalendar.model;


import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.sql.Date;
import java.sql.Timestamp;



public class Customer {

    private final IntegerProperty customerId;
    private final StringProperty customerName;
    private final IntegerProperty addressId;
    private final IntegerProperty activeCustomer;
    private  Date createDate;
    private  StringProperty createdBy;
    private  Timestamp lastUpdate;
    private  StringProperty lastUpdateBy;




    public Customer(int customerId, String customerName, int addressId, int activeCustomer){
        this.customerId = new SimpleIntegerProperty(customerId);
        this.customerName = new SimpleStringProperty(customerName);
        this.addressId = new SimpleIntegerProperty(addressId);
        this.activeCustomer = new SimpleIntegerProperty(activeCustomer);

    }

    public Customer(int customerId, String customerName, int addressId, int activeCustomer, Date createDate, String createdBy, Timestamp lastUpdate, String lastUpdateBy){
        this.customerId = new SimpleIntegerProperty(customerId);
        this.customerName = new SimpleStringProperty();
        this.addressId = new SimpleIntegerProperty();
        this.activeCustomer = new SimpleIntegerProperty();
        this.createDate = createDate;
        this.createdBy = new SimpleStringProperty();
        this.lastUpdate = new Timestamp(1000);
        this.lastUpdateBy = new SimpleStringProperty();
    }

    public static String isValidCustomer(int customerId, String customerName, int addressId, String phoneNumber , String error){
                if (customerName == null){
                    error = error + "Name field is required";
                }
                else if(customerId < 1){
                    error = error + "CustomerId cannot be less than 1";
                }
                else if(addressId < 1){
                    error = error + "addressId must be non negative";

                }

                else if(phoneNumber == null){
                    error = error + "phone number must contain a value";
                }

                return error;
    }



    //setters
    public void setCustomerId(int customerId) {
        this.customerId.set(customerId);
    }

    public void setCustomerName(String customerName) {
        this.customerName.set(customerName);
    }

    public void setAddressId(int addressId) {
        this.addressId.set(addressId);
    }

    public void setActiveCustomer(int activeCustomer) {
        this.activeCustomer.set(activeCustomer);
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy.set(createdBy);
    }

    public void setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy.set(lastUpdateBy);
    }








   //getters
    public int getCustomerId() {
        return customerId.get();
    }

    public IntegerProperty customerIdProperty() {
        return customerId;
    }

    public String getCustomerName() {
        return customerName.get();
    }

    public int getAddressId() {
        return addressId.get();
    }

    public int getActiveCustomer() {
        return activeCustomer.get();
    }

    public Date getCreateDate() {
        return createDate;
    }

    public String getCreatedBy() {
        return createdBy.get();
    }

    public StringProperty createdByProperty() {
        return createdBy;
    }

    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    public String getLastUpdateBy() {
        return lastUpdateBy.get();
    }

    public StringProperty lastUpdateByProperty() {
        return lastUpdateBy;
    }
}
