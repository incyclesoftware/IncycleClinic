package Incycle.Clinique;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        System.out.println("InCycle Appointment Manager");
        System.out.println("============================");
        String database = "InCycleClinique";
        Connection connection = null;
        Scanner scanner = new Scanner(System.in);
        String connectionString = null;
        // get database connection from json file located in the connections folder if it exists
        File file = new File("connections/database.txt");
        try {
            if (file.exists()) {
                FileInputStream stream = new FileInputStream(file);
                byte[] data = new byte[(int) file.length()];
                stream.read(data);
                stream.close();
                connectionString = new String(data, "UTF-8");
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        if (connectionString == null) {
            System.out.println("Enter database string, exit to leave");
            connectionString = scanner.nextLine().trim().toLowerCase();
            if (connectionString.equals("exit")) {
                System.out.println("exiting");
                System.exit(0);
            }
            if (connectionString.isEmpty()) {
                System.out.println("Invalid database URL format");
                System.exit(1);
            }
        }
        try {
            connection = DriverManager.getConnection(connectionString);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
        System.out.println("Connected to the database");

        String command;
        try {
            do {
                System.out.println("\r\nEnter a command");
                System.out.println("============================");
                System.out.println("nad. Create a new Admin");
                System.out.println("npt. Create a new Patient");
                System.out.println("nnu. Create a new Nurse");
                System.out.println("ndr. Create a new Doctor");
                System.out.println("nap. Create a new Nurse Appointment");
                System.out.println("nda. Create a new Dr Appointment");
                System.out.println("lpa. List all Patients");
                System.out.println("lpd. List all Doctors");
                System.out.println("lap. List all Appointments");
                System.out.println("ndi. Create a new Dr Invoice");
                System.out.println("nni. Create a new Nurse Invoice");
                System.out.println("lpi. List all Invoices");
                System.out.println("laa. List all admins");
                System.out.println("exit. Exits the programs\r\n");
                command = scanner.nextLine();
                if (command.equals("nad")) {
                    System.out.println("Creating a new Admin");
                    System.out.println("Enter the username");
                    String newUsername = scanner.nextLine();
                    Statement statement = connection.createStatement();
                    String query = String.format("SELECT COUNT(User) as number FROM mysql.user WHERE User = '%s'", newUsername);
                    ResultSet result = statement.executeQuery(query);
                    result.next();
                    if (result.getInt(1) == 0) {
                        System.out.println("Enter the password");
                        String newPassword = scanner.nextLine();
                        query = String.format("CREATE USER '%s'@'%%' IDENTIFIED BY '%s'", newUsername, newPassword);
                        statement.executeUpdate(query);
                        query = String.format("GRANT ALL PRIVILEGES ON %s.* TO '%s'@'%%'", database, newUsername);
                        statement.executeUpdate(query);
                        System.out.println("User created successfully");
                    } else {
                        System.out.println("User already exists");
                    }
                } else if (command.equals("npt")) {
                    System.out.println("Enter the patient's first name");
                    String firstName = scanner.nextLine();
                    System.out.println("Enter the patient's last name");
                    String lastName = scanner.nextLine();
                    Statement statement = connection.createStatement();
                    String query = String.format("SELECT PatientID FROM Patient WHERE FirstName = '%s' AND LastName = '%s'", firstName, lastName);
                    ResultSet result = statement.executeQuery(query);
                    if (result.next()) {
                        System.out.println("Patient already exists with ID: " + result.getString(1));
                        System.out.println("Do you still want to create a new patient? (yes/no)");
                        String confirmation = scanner.nextLine().trim().toLowerCase();
                        if (!confirmation.equals("yes")) {
                            System.out.println("Patient creation cancelled");
                            continue;
                        }
                    }
                    query = String.format("INSERT INTO Patient (FirstName, LastName) VALUES ('%s', '%s')", firstName, lastName);
                    if (statement.executeUpdate(query) == 1) {
                        // get last id
                        query = "SELECT LAST_INSERT_ID()";
                        result = statement.executeQuery(query);
                        result.next();
                        String patientId = result.getString(1);
                        System.out.println("Patient created successfully with ID: " + patientId);
                    }

                } else if (command.equals("nni")) {
                    System.out.println("Creating a new Nurse Invoice");
                    System.out.println("=============================");
                    System.out.println("Enter the appointment ID");
                    String appointmentID = scanner.nextLine();
                    String query = String.format("SELECT PatientID,NurseID FROM NurseAppointment WHERE AppointmentID = '%s'", appointmentID);
                    Statement statement = connection.createStatement();
                    ResultSet result = statement.executeQuery(query);
                    if (result.next()) {
                        System.out.println("Patient found: " + result.getString(1));
                        System.out.println("Nurse found: " + result.getString(2));
                    } else {
                        System.out.println("Appointment not found");
                        continue;
                    }
                    System.out.println("Enter the amount");
                    String amount = scanner.nextLine();
                    query = String.format("INSERT INTO NurseInvoice (PatientID,NurseID,Amount) VALUES ('%s','%s','%s')", result.getString(1), result.getString(2), amount);
                    if (statement.executeUpdate(query) == 1) {
                        System.out.println("Invoice created successfully");
                    }
                } else if (command.equals("laa")) {
                    System.out.println("Listing all admins");
                    Statement statement = connection.createStatement();
                    String query = "SELECT User FROM mysql.user";
                    ResultSet result = statement.executeQuery(query);
                    System.out.println("User");
                    while (result.next()) {
                        System.out.println(result.getString(1));
                    }
                } else if (command.equals("ndi")) {
                    System.out.println("Creating a new Dr Invoice");
                    System.out.println("=============================");
                    System.out.println("Enter the appointment ID");
                    String appointmentID = scanner.nextLine();
                    String query = String.format("SELECT PatientID,DoctorID,AmountToBePaid FROM DrAppointment WHERE AppointmentID = '%s'", appointmentID);
                    Statement statement = connection.createStatement();
                    ResultSet result = statement.executeQuery(query);
                    if (result.next()) {
                        System.out.println("Patient found: " + result.getString(1));
                        System.out.println("Doctor found: " + result.getString(2));
                        System.out.println("Amount to be paid: " + result.getString(3));
                    } else {
                        System.out.println("Appointment not found");
                        continue;
                    }
                    System.out.println("Enter the amount");
                    String amount = scanner.nextLine();
                    query = String.format("INSERT INTO DrInvoice (PatientID,DoctorID,Amount) VALUES ('%s','%s','%s')", result.getString(1), result.getString(2), amount);
                    if (statement.executeUpdate(query) == 1) {
                        System.out.println("Invoice created successfully");
                    }
                } else if (command.equals("lpi")) {
                    System.out.println("Listing invoices");
                    System.out.println("Enter patient name, firstname lastname");
                    String patientName = scanner.nextLine();
                    String[] names = patientName.split(" ");
                    Statement statement = connection.createStatement();
                    String query = String.format("SELECT PatientID FROM Patient WHERE FirstName = '%s' AND LastName = '%s'", names[0], names[1]);
                    ResultSet result = statement.executeQuery(query);
                    if (result.next()) {
                        String patientID = result.getString(1);
                        System.out.println("Patient found: " + patientID);
                        query = String.format("SELECT * FROM DrInvoice WHERE PatientID = '%s'", patientID);
                        result = statement.executeQuery(query);
                        System.out.println("Doctor Invoices");
                        System.out.println("InvoiceID PatientID DoctorID Amount");
                        while (result.next()) {
                            System.out.println(result.getString(1) + " " + result.getString(2) + " " + result.getString(3) + " " + result.getString(4));
                        }
                        query = String.format("SELECT * FROM NurseInvoice WHERE PatientID = '%s'", patientID);
                        result = statement.executeQuery(query);
                        System.out.println("Nurse Invoices");
                        System.out.println("InvoiceID PatientID NurseID Amount");
                        while (result.next()) {
                            System.out.println(result.getString(1) + " " + result.getString(2) + " " + result.getString(3) + " " + result.getString(4));
                        }
                    } else {
                        System.out.println("Patient not found");
                    }
                } else if (command.equals("nda")) {
                    System.out.println("Creating a new Dr Appointment");
                    System.out.println("=============================");
                    System.out.println("Enter the patient's id");
                    String patientID = scanner.nextLine();
                    String query = String.format("SELECT FirstName,LastName FROM Patient WHERE PatientID = '%s'", patientID);
                    Statement statement = connection.createStatement();
                    ResultSet result = statement.executeQuery(query);
                    if (result.next()) {
                        System.out.println("Patient found: " + result.getString(1) + " " + result.getString(2));
                    } else {
                        System.out.println("Patient not found");
                        continue;
                    }
                    System.out.println("Enter the doctor's ID");
                    String doctorId = scanner.nextLine();
                    query = String.format("SELECT FirstName,LastName FROM Doctor WHERE DoctorID = '%s'", doctorId);
                    result = statement.executeQuery(query);
                    if (result.next()) {
                        System.out.println("Doctor found: " + result.getString(1) + " " + result.getString(2));
                    } else {
                        System.out.println("Doctor not found");
                        continue;
                    }
                    System.out.println("Enter the date of the appointment");
                    String date = scanner.nextLine();
                    System.out.println("Enter the time of the appointment");
                    String time = scanner.nextLine();
                    query = String.format("INSERT INTO DrAppointment (PatientID,DoctorID,AppointmentDate,AppointmentTime) VALUES ('%s','%s','%s','%s')", patientID, doctorId, date, time);
                    if (statement.executeUpdate(query) == 1) {
                        System.out.println("Appointment created successfully");
                    }
                    System.out.println("Appointment created successfully");
                    query = "SELECT LAST_INSERT_ID()";
                    result = statement.executeQuery(query);
                    if (result.next()) {
                        String appointmentId = result.getString(1);
                        System.out.println("Appointment ID: " + appointmentId);
                    }

                } else if (command.equals("nap")) {
                    System.out.println("Creating a new nurse Appointment");
                    System.out.println("Enter the patient's id");
                    String patientID = scanner.nextLine();
                    String query = String.format("SELECT FirstName,LastName FROM Patient WHERE PatientID = '%s'", patientID);
                    Statement statement = connection.createStatement();
                    ResultSet result = statement.executeQuery(query);
                    if (result.next()) {
                        System.out.println("Patient found: " + result.getString(1) + " " + result.getString(2));
                    } else {
                        System.out.println("Patient not found");
                        continue;
                    }
                    System.out.println("Enter the nurse's ID");
                    String nurseId = scanner.nextLine();
                    query = String.format("SELECT FirstName,LastName FROM Nurse WHERE NurseID = '%s'", nurseId);
                    result = statement.executeQuery(query);
                    if (result.next()) {
                        System.out.println("Nurse found: " + result.getString(1) + " " + result.getString(2));
                    } else {
                        System.out.println("Doctor not found");
                        continue;
                    }
                    System.out.println("Enter the date of the appointment");
                    String date = scanner.nextLine();
                    System.out.println("Enter the time of the appointment");
                    String time = scanner.nextLine();
                    query = String.format("INSERT INTO NurseAppointment (PatientID,NurseID,AppointmentDate,AppointmentTime) VALUES ('%s','%s','%s','%s')", patientID, nurseId, date, time);
                    if (statement.executeUpdate(query) == 1) {
                        System.out.println("Appointment created successfully");
                    }
                } else if (command.equals("ndr")) {
                    System.out.println("Inserting a new Doctor");
                    System.out.println("Enter the doctor's first name");
                    String firstName = scanner.nextLine();
                    System.out.println("Enter the doctor's last name");
                    String lastName = scanner.nextLine();
                    Statement statement = connection.createStatement();
                    String query = String.format("SELECT DoctorID FROM Doctor WHERE FirstName = '%s' AND LastName = '%s'", firstName, lastName);
                    ResultSet result = statement.executeQuery(query);
                    if (result.next()) {
                        System.out.println("Doctor already exists with ID: " + result.getString(1));
                        System.out.println("Do you still want to create a new doctor? (yes/no)");
                        String confirmation = scanner.nextLine().trim().toLowerCase();
                        if (!confirmation.equals("yes")) {
                            System.out.println("Doctor creation cancelled");
                            continue;
                        }
                    }
                    //query = String.format("CALL CreateDoctor('%s', '%s')", firstName, lastName);
                    query = String.format("INSERT INTO Doctor (FirstName, LastName) VALUES ('%s', '%s')", firstName, lastName);
                    statement.executeUpdate(query);
                    query = "SELECT LAST_INSERT_ID()"; // get the last id
                    result = statement.executeQuery(query);
                    if (result.next()) {
                        System.out.println("Doctor created successfully with ID: " + result.getString(1));
                    }
                    // create a CRUD user as well?
                    System.out.println("Do you want to create a user for the doctor? (yes/no)");
                    String confirmation = scanner.nextLine().trim().toLowerCase();
                    if (confirmation.equals("yes")) {
                        System.out.println("Enter the username");
                        String newUsername = scanner.nextLine();
                        query = String.format("SELECT COUNT(User) as number FROM mysql.user WHERE User = '%s'", newUsername);
                        result = statement.executeQuery(query);
                        if (result.next()) {
                            if (result.getInt(1) == 0) {
                                System.out.println("Enter the password");
                                String newPassword = scanner.nextLine();
                                query = String.format("CREATE USER '%s'@'%%' IDENTIFIED BY '%s'", newUsername, newPassword);
                                statement.executeUpdate(query);
                                query = String.format("GRANT ALL PRIVILEGES ON %s.* TO '%s'@'%%'", database, newUsername);
                                statement.executeUpdate(query);
                                System.out.println("User created successfully");
                            } else {
                                System.out.println("User already exists");
                            }
                        }
                    }
                }
                // list patients
                else if (command.equals("lpa")) {
                    System.out.println("Listing all patients");
                    Statement statement = connection.createStatement();
                    String query = "SELECT PatientID, FirstName, LastName FROM Patient";
                    ResultSet result = statement.executeQuery(query);
                    // print the header
                    System.out.println("PatientID FirstName LastName");
                    int headerPrinter = 0;
                    while (result.next()) {
                        if (++headerPrinter == 10) {
                            System.out.println("PatientID FirstName LastName");
                            headerPrinter = 0;
                        }
                        System.out.println(result.getString(1) + " " + result.getString(2) + " " + result.getString(3));
                    }
                }
                // list drs
                else if (command.equals("lpd")) {
                    System.out.println("Listing all doctors");
                    Statement statement = connection.createStatement();
                    String query = "SELECT DoctorID, FirstName, LastName FROM Doctor";
                    ResultSet result = statement.executeQuery(query);
                    // print the header
                    System.out.println("DoctorID FirstName LastName");
                    int headerPrinter = 0;
                    while (result.next()) {
                        if (++headerPrinter == 10) {
                            System.out.println("DoctorID FirstName LastName");
                            headerPrinter = 0;
                        }
                        System.out.println(result.getString(1) + " " + result.getString(2) + " " + result.getString(3));
                    }
                }
                // list appointments
                else if (command.equals("lap")) {
                    System.out.println("Listing all appointments");
                    Statement statement = connection.createStatement();
                    String query = "SELECT AppointmentID, PatientID, DoctorID,AmountToBePaid, AppointmentDate, AppointmentTime FROM DrAppointment";
                    ResultSet result = statement.executeQuery(query);
                    // print the header

                    System.out.println("AppointmentID\tPatientID\tDoctorID\tAmountToBePaid\tAppointmentDate\tAppointmentTime");
                    int headerPrinter = 0;
                    while (result.next()) {
                        if (++headerPrinter == 10) {
                            System.out.println("AppointmentID\tPatientID\tDoctorID\tAmountToBePaid\tAppointmentDate\tAppointmentTime");
                            headerPrinter = 0;
                        }
                        System.out.println(result.getInt(1) + "\t\t\t\t\t" + result.getInt(2) + "\t\t\t" + result.getInt(3) + "\t\t\t" + result.getDouble(4) + "\t\t\t" + result.getString(5) + "\t\t" + result.getString(6));
                    }
                }
                // new nurse
                else if (command.equals("nnu")) {
                    System.out.println("Inserting a new Nurse");
                    System.out.println("Enter the nurse's first name");
                    String firstName = scanner.nextLine();
                    System.out.println("Enter the nurse's last name");
                    String lastName = scanner.nextLine();
                    Statement statement = connection.createStatement();
                    String query = String.format("SELECT NurseID FROM Nurse WHERE FirstName = '%s' AND LastName = '%s'", firstName, lastName);
                    ResultSet result = statement.executeQuery(query);
                    if (result.next()) {
                        System.out.println("Nurse already exists with ID: " + result.getString(1));
                        System.out.println("Do you still want to create a new nurse? (yes/no)");
                        String confirmation = scanner.nextLine().trim().toLowerCase();
                        if (!confirmation.equals("yes")) {
                            System.out.println("Nurse creation cancelled");
                            continue;
                        }
                    }
                    System.out.println("Enter the nurse's ID");
                    String nurseId = scanner.nextLine();
                    query = String.format("INSERT INTO Nurse (NurseID, FirstName, LastName) VALUES ('%s', '%s', '%s')", nurseId, firstName, lastName);
                    if (statement.executeUpdate(query) == 1) {
                        System.out.println("Nurse created successfully with ID: " + nurseId);
                    }
                }


            } while (!command.equals("exit"));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            scanner.close();
        }
    }
}