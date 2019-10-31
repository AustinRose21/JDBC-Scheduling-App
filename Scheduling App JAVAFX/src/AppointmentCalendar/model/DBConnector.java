package AppointmentCalendar.model;


import AppointmentCalendar.view_controller.CalendarScreenController;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;



public class DBConnector {

    private static String currentUsername;

    private static final String databaseName = "U05K6C";
    private static final String DB_URL = "jdbc:mysql://52.206.157.109/" + databaseName;
    private static final String username = "U05K6C";
    private static final String password = "53688525198";
    private static final String driver = "com.mysql.jdbc.Driver";
    static Connection conn;


    //find a driver, then connect to db, create connection object
    public static Connection makeConnection() throws ClassNotFoundException, SQLException {
        Class.forName(driver);
        conn = DriverManager.getConnection(DB_URL, username, password);
        System.out.println("Connection was successful");
        return conn;
    }

    public static void closeConnection() throws SQLException {
        conn.close();
        System.out.println("Connection was successfully closed");
    }



    //get appointment data from DB
    public static ResultSet upDateAppointmentQuery() {
        ResultSet rsUpdateAppointment = null;

        try {
            conn = makeConnection();
            Statement stmt;
            rsUpdateAppointment = null;

            try {
                stmt = conn.createStatement();
                rsUpdateAppointment = stmt.executeQuery("SELECT appointmentId, customerId, userId, title, description, location, contact, type, url, start, end FROM appointment ORDER BY appointmentId ");
                while (rsUpdateAppointment.next()){
                    Integer appointmentId = rsUpdateAppointment.getInt(1);
                    Integer customerId = rsUpdateAppointment.getInt(2);
                    Integer userId = rsUpdateAppointment.getInt(3);
                    String title = rsUpdateAppointment.getString(4);
                    String description = rsUpdateAppointment.getString(5);
                    String location = rsUpdateAppointment.getString(6);
                    String contact = rsUpdateAppointment.getString(7);
                    String type = rsUpdateAppointment.getString(8);
                    String url = rsUpdateAppointment.getString(9);
                   //LocalDateTime start = rsUpdateAppointment.getDate(10);
                   // Date end = rsUpdateAppointment.getDate(11);

                    //1. Timestamp values for dates
                    Timestamp startTimestamp = rsUpdateAppointment.getTimestamp(10);
                    Timestamp endTimestamp = rsUpdateAppointment.getTimestamp(11);

                    //2. converting Timestamp to localdatetime
                    LocalDateTime startLocalDateTime = startTimestamp.toLocalDateTime();
                    LocalDateTime endLocalDateTime = endTimestamp.toLocalDateTime();


                    //3. converting to UTC ZONEDDATETIME
                    ZonedDateTime utcStart = startLocalDateTime.atZone(ZoneId.of("UTC"));
                    ZonedDateTime utcEnd = endLocalDateTime.atZone(ZoneId.of("UTC"));

                    System.out.println("Zoned time UTC:     " + utcStart );
                    System.out.println("Zoned end time UTC:    " + utcEnd);

                    //4. convert to ZoneDateTime with localTimeZone
                    ZoneId localId = ZoneId.systemDefault();
                    ZonedDateTime zonedStartLocal = utcStart.withZoneSameLocal(localId);
                    ZonedDateTime zonedEndLocal = utcEnd.withZoneSameLocal(localId);

                    System.out.println("Local Time:    " + zonedStartLocal);
                    System.out.println("Local Time end:    " + zonedEndLocal);

                    //convert back to localdatetime
                    LocalDateTime insertLocalStart = zonedStartLocal.toLocalDateTime();
                    LocalDateTime insertLocalEnd = zonedEndLocal.toLocalDateTime();

                    // convert values into string
                    String localStart = String.valueOf(zonedStartLocal);
                    String localEnd = String.valueOf(zonedEndLocal);


                    Appointment updatedAppt = new Appointment(appointmentId, customerId, userId, title, description, location, contact, type, url, insertLocalStart, insertLocalEnd);
                    AppointmentList.getAppointmentList().add(updatedAppt);


                }

            } catch (SQLException ex) {
                ex.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return rsUpdateAppointment;
    }






    public static void deleteAppointmentRecord(Appointment deletedAppointment){
        int appointmentId = deletedAppointment.getAppointmentId();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText("Confirm");
        alert.setContentText("Are you sure you want to delete?");
        Optional<ButtonType> result = alert.showAndWait();
        //if the button was clicked delete customer entry by setting to inactive
        if (result.get() == ButtonType.OK){
            try(Statement stmt = makeConnection().createStatement()){
                stmt.executeUpdate(" DELETE FROM appointment WHERE appointmentId = " + appointmentId);
            }catch (Exception e){
                e.printStackTrace();
            }

            upDateAppointmentQuery();
            System.out.println("Delete SQL branch worked");
            AppointmentList.getAppointmentList().remove(appointmentId);
        }

    }

    public static void addAppointmentRecord(int appointmentId, int customerId, int userId, String title, String description, String apptLocation, String contact, String type, String url, Timestamp startDateTime, Timestamp endDateTime){


        try{
            conn = makeConnection();
            Statement stmt;
            stmt = conn.createStatement();
            stmt.executeUpdate("INSERT INTO appointment (customerId, userId, title, description, location, contact, type, url, start, end, createDate, createdBy, lastUpdate)" +
                    "VALUES ("+customerId+",'"+userId+"','"+title+"','"+description+"','"+apptLocation+"', '"+contact+"', '"+type+"', '"+url+"', '"+startDateTime+"', '"+endDateTime+"', CURRENT_DATE, CURRENT_USER, CURRENT_TIMESTAMP)");
            System.out.println("SQL was executed correctly");
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    public static void updateAppointmentRecord(int customerId, int userId, String title, String description, String apptLocation, String contact, String type, String url, Timestamp startDateTime, Timestamp endDateTime){


        try{
            conn = makeConnection();
            Statement stmt;
            stmt = conn.createStatement();
            stmt.executeUpdate("UPDATE appointment SET customerId = "+customerId+", userId = "+userId+", title = '"+title+"', description = '"+description+"', location = '"+apptLocation+"', contact = '"+contact+"', type = '"+type+"', url = '"+url+"', start = '"+startDateTime+"', end = '"+endDateTime+"', createDate = CURRENT_DATE, createdBy = CURRENT_USER, lastUpdate = CURRENT_TIMESTAMP");
            System.out.println("SQL was executed correctly");
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    //get appointment data from DB
    public static ResultSet upDateAppointmentQueryByWeek(Timestamp startDate, Timestamp startDatePlusSeven) {
        ResultSet rsUpdateAppointmentByWeek = null;
        System.out.println(startDate);
        System.out.println(startDatePlusSeven);

        try {
            conn = makeConnection();
            Statement stmt;
            rsUpdateAppointmentByWeek = null;

            try {
                stmt = conn.createStatement();
                rsUpdateAppointmentByWeek = stmt.executeQuery("SELECT appointmentId, customerId, userId, title, description, location, contact, type, url, start, end FROM appointment WHERE start BETWEEN '"+startDate+"' AND '"+startDatePlusSeven+"' ORDER BY appointmentId ");
                while (rsUpdateAppointmentByWeek.next()){
                    Integer appointmentId = rsUpdateAppointmentByWeek.getInt(1);
                    Integer customerId = rsUpdateAppointmentByWeek.getInt(2);
                    Integer userId = rsUpdateAppointmentByWeek.getInt(3);
                    String title = rsUpdateAppointmentByWeek.getString(4);
                    String description = rsUpdateAppointmentByWeek.getString(5);
                    String location = rsUpdateAppointmentByWeek.getString(6);
                    String contact = rsUpdateAppointmentByWeek.getString(7);
                    String type = rsUpdateAppointmentByWeek.getString(8);
                    String url = rsUpdateAppointmentByWeek.getString(9);

                    //1. Timestamp values for dates
                    Timestamp startTimestamp = rsUpdateAppointmentByWeek.getTimestamp(10);
                    Timestamp endTimestamp = rsUpdateAppointmentByWeek.getTimestamp(11);

                    //2. converting Timestamp to localdatetime
                    LocalDateTime startLocalDateTime = startTimestamp.toLocalDateTime();
                    LocalDateTime endLocalDateTime = endTimestamp.toLocalDateTime();


                    //3. converting to UTC ZONEDDATETIME
                    ZonedDateTime utcStart = startLocalDateTime.atZone(ZoneId.of("UTC"));
                    ZonedDateTime utcEnd = endLocalDateTime.atZone(ZoneId.of("UTC"));

                    System.out.println("Zoned time UTC:     " + utcStart );
                    System.out.println("Zoned end time UTC:    " + utcEnd);

                    //4. convert to ZoneDateTime with localTimeZone
                    ZoneId localId = ZoneId.systemDefault();
                    ZonedDateTime zonedStartLocal = utcStart.withZoneSameLocal(localId);
                    ZonedDateTime zonedEndLocal = utcEnd.withZoneSameLocal(localId);

                    System.out.println("Local Time:    " + zonedStartLocal);
                    System.out.println("Local Time end:    " + zonedEndLocal);

                    //convert back to localdatetime
                    LocalDateTime insertLocalStart = zonedStartLocal.toLocalDateTime();
                    LocalDateTime insertLocalEnd = zonedEndLocal.toLocalDateTime();

                    // convert values into string
                    String localStart = String.valueOf(zonedStartLocal);
                    String localEnd = String.valueOf(zonedEndLocal);


                    Appointment updatedAppt = new Appointment(appointmentId, customerId, userId, title, description, location, contact, type, url, insertLocalStart, insertLocalEnd);
                    AppointmentList.getAppointmentList().add(updatedAppt);


                }

            } catch (SQLException ex) {
                ex.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return rsUpdateAppointmentByWeek;
    }

    //get appointment data to update by month
    //get appointment data from DB
    public static ResultSet upDateAppointmentQueryByMonth(Timestamp startDate, Timestamp startDatePlusThirty) {
        ResultSet rsUpdateAppointmentByMonth = null;
        System.out.println(startDate);
        System.out.println(startDatePlusThirty);

        try {
            conn = makeConnection();
            Statement stmt;
            rsUpdateAppointmentByMonth = null;

            try {
                stmt = conn.createStatement();
                rsUpdateAppointmentByMonth = stmt.executeQuery("SELECT appointmentId, customerId, userId, title, description, location, contact, type, url, start, end FROM appointment WHERE start BETWEEN '"+startDate+"' AND '"+startDatePlusThirty+"' ORDER BY appointmentId ");
                while (rsUpdateAppointmentByMonth.next()){
                    Integer appointmentId = rsUpdateAppointmentByMonth.getInt(1);
                    Integer customerId = rsUpdateAppointmentByMonth.getInt(2);
                    Integer userId = rsUpdateAppointmentByMonth.getInt(3);
                    String title = rsUpdateAppointmentByMonth.getString(4);
                    String description = rsUpdateAppointmentByMonth.getString(5);
                    String location = rsUpdateAppointmentByMonth.getString(6);
                    String contact = rsUpdateAppointmentByMonth.getString(7);
                    String type = rsUpdateAppointmentByMonth.getString(8);
                    String url = rsUpdateAppointmentByMonth.getString(9);


                    //1. Timestamp values for dates
                    Timestamp startTimestamp = rsUpdateAppointmentByMonth.getTimestamp(10);
                    Timestamp endTimestamp = rsUpdateAppointmentByMonth.getTimestamp(11);

                    //2. converting Timestamp to localdatetime
                    LocalDateTime startLocalDateTime = startTimestamp.toLocalDateTime();
                    LocalDateTime endLocalDateTime = endTimestamp.toLocalDateTime();


                    //3. converting to UTC ZONEDDATETIME
                    ZonedDateTime utcStart = startLocalDateTime.atZone(ZoneId.of("UTC"));
                    ZonedDateTime utcEnd = endLocalDateTime.atZone(ZoneId.of("UTC"));

                    System.out.println("Zoned time UTC:     " + utcStart );
                    System.out.println("Zoned end time UTC:    " + utcEnd);

                    //4. convert to ZoneDateTime with localTimeZone
                    ZoneId localId = ZoneId.systemDefault();
                    ZonedDateTime zonedStartLocal = utcStart.withZoneSameLocal(localId);
                    ZonedDateTime zonedEndLocal = utcEnd.withZoneSameLocal(localId);

                    System.out.println("Local Time:    " + zonedStartLocal);
                    System.out.println("Local Time end:    " + zonedEndLocal);

                    //convert back to localdatetime
                    LocalDateTime insertLocalStart = zonedStartLocal.toLocalDateTime();
                    LocalDateTime insertLocalEnd = zonedEndLocal.toLocalDateTime();

                    // convert values into string
                    String localStart = String.valueOf(zonedStartLocal);
                    String localEnd = String.valueOf(zonedEndLocal);


                    Appointment updatedAppt = new Appointment(appointmentId, customerId, userId, title, description, location, contact, type, url, insertLocalStart, insertLocalEnd);
                    AppointmentList.getAppointmentList().add(updatedAppt);


                }

            } catch (SQLException ex) {
                ex.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return rsUpdateAppointmentByMonth;
    }

    //get the incremented appointmentId from the DB
    public static int getLatestAppointmentId() throws ClassNotFoundException {
        ResultSet apptIdSet = null;
        int correctId = 0;

        try {
            conn = makeConnection();
            Statement stmt;

            try {
                stmt = conn.createStatement();
                apptIdSet = stmt.executeQuery("SELECT appointmentId FROM appointment");
                int idDB = 0;
                while (apptIdSet.next()) {
                    idDB++;
                }
                correctId = (idDB + 1);
            }
            catch (SQLException e){
                e.printStackTrace();
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

        return correctId;
    }




//get customer data from DB
    public static ResultSet upDateCustomerQuery() {
        ResultSet rsUpdateCustomer = null;

        try {
            conn = makeConnection();
            Statement stmt;


            try {
                stmt = conn.createStatement();
                rsUpdateCustomer = stmt.executeQuery("SELECT customerId, customerName, addressId, active FROM customer WHERE active = 1 ");
                while (rsUpdateCustomer.next()){
                    Integer customerId = rsUpdateCustomer.getInt(1);
                    String customerName = rsUpdateCustomer.getString(2);
                    Integer addressId = rsUpdateCustomer.getInt(3);
                    Integer isActive = rsUpdateCustomer.getInt(4);



                    Customer updatedCustomer = new Customer(customerId, customerName, addressId, isActive);
                    CustomerList.getCustomerList().add(updatedCustomer);

                }

            } catch (SQLException ex) {
                ex.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return rsUpdateCustomer;
    }

    public static void deleteCustomerRecord(Customer deletedCustomer){
        int customerId = deletedCustomer.getCustomerId();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText("Confirm");
        alert.setContentText("Are you sure you want to delete?");
        Optional<ButtonType> result = alert.showAndWait();
        //if the button was clicked delete customer entry by setting to inactive
        if (result.get() == ButtonType.OK){
            try(Statement stmt = makeConnection().createStatement()){
                stmt.executeUpdate("UPDATE customer SET active = 0 WHERE customerId = " + customerId);
            }catch (Exception e){
                e.printStackTrace();
            }
            upDateCustomerQuery();
            System.out.println("Delete SQL branch worked");
        }
    }

    public static void addCustomerRecord (String name, int addressId, int active, String address, int cityId, String postalCode, String phoneNumber){



        try{
            conn = makeConnection();
            Statement stmt;
            stmt = conn.createStatement();
            stmt.executeUpdate("INSERT INTO customer (customerName, addressId, active, createDate, createdBy, lastUpdateBy  ) " +
                                                            " VALUES ('"+name+"',"+addressId+","+active+", CURRENT_DATE, CURRENT_USER, CURRENT_USER)");
                System.out.println("SQL was executed correctly");
            } catch (Exception e){
                e.printStackTrace();
            }


        try{
            conn = makeConnection();
            Statement stmt;
            stmt = conn.createStatement();
            stmt.executeUpdate("INSERT INTO address (address, cityId, postalCode, phone, createDate, createdBy, lastUpdateBy  ) " +
                    " VALUES ('"+address+"', '"+cityId+"','"+postalCode+"','"+phoneNumber+"', CURRENT_DATE, CURRENT_USER, CURRENT_USER)");
            System.out.println("SQL was executed correctly");
        } catch (Exception e){
            e.printStackTrace();
        }


        }







    public static void updateCustomerRecord(int customerId, String customerName, int addressId, int active, int oldId, String address, int cityId, String zipCode, String phoneNumber, int oldAddressID ) {

            try {
                conn = makeConnection();
                Statement stmt;
                stmt = conn.createStatement();
                stmt.executeUpdate("UPDATE customer SET customerId = "+customerId+", customerName = '"+customerName+"', addressId = "+addressId+", active = "+active+"  " +
                        " WHERE customerId = "+oldId+"");
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("SQL update completed correctly.");
            }

        try{
            conn = makeConnection();
            Statement stmt;
            stmt = conn.createStatement();
            stmt.executeUpdate("UPDATE address SET address = '"+address+"', cityId = "+cityId+", postalCode = '"+zipCode+"', phone = '"+phoneNumber+"'  "+
                    " WHERE addressId = "+oldAddressID+"");
            System.out.println("address updated successfully");
        } catch (Exception e){
            e.printStackTrace();
        }
        }






    //get username of current user logging in
    private static String getUserName(String username) throws SQLException, ClassNotFoundException {

        Statement stmt = DBConnector.makeConnection().createStatement();
        ResultSet resultIdSet = stmt.executeQuery("SELECT userName FROM user WHERE userName = '" + username + "'");

        if (resultIdSet.next()) {
            username = resultIdSet.getString("userName");
        }
        try {
            resultIdSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return username;
    }

    //get password of current user logging in
    private static String getUserPassword(String password) {

        try {
            Statement stmt = DBConnector.makeConnection().createStatement();
            ResultSet resultPasswordSet = stmt.executeQuery("SELECT password FROM user WHERE password = '" + password + "'");
            if (resultPasswordSet.next()) {
                password = resultPasswordSet.getString("password");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return password;
    }

    public static int getUserId(String userName) throws ClassNotFoundException {
        int userId = -1;
        try (Statement stmt = makeConnection().createStatement()) {
            ResultSet rsUserId = stmt.executeQuery("SELECT userid FROM user WHERE userName = '" + userName + "'");

            //retrieve userId from resultset
            if (rsUserId.next()) {
                userId = rsUserId.getInt("userid");
            }
            rsUserId.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userId;
    }

    public static boolean isPasswordValid(int userId, String password) {
        try (Statement stmt = makeConnection().createStatement()) {
            ResultSet rsPassword = stmt.executeQuery("SELECT password FROM user WHERE userid = '" + userId + "'");

            //retrieves password string from database
            String passwordDB = null;
            if (rsPassword.next()) {
                passwordDB = rsPassword.getString("password");
            } else {
                return false;
            }
            rsPassword.close();

            if (passwordDB.equals(password)) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    //checks to see if login is valid
    public static boolean isLoginValid(String userName, String password) throws ClassNotFoundException {
        int userId = getUserId(userName);
        boolean correctPassword = isPasswordValid(userId, password);
        if (correctPassword) {
            setCurrentUsername(userName);
            return true;
        } else {
            return false;
        }
    }


    private static void setCurrentUsername(String userName) {
        userName = currentUsername;
    }
}







