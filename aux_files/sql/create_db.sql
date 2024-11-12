CREATE DATABASE IF NOT EXISTS InCycleClinique;
USE InCycleClinique;

CREATE TABLE IF NOT EXISTS Patient
(
    PatientID int primary key auto_increment,
    FirstName varchar(50),
    LastName  varchar(50),
    DateOfBirth date
);

CREATE TABLE IF NOT EXISTS Address
(
    AddressID int primary key auto_increment,
    PatientID int,
    AddressLine1 varchar(50),
    AddressLine2 varchar(50),
    City varchar(50),
    State varchar(50),
    ZipCode varchar(50),
    constraint fk_patient foreign key (PatientID) references Patient(PatientID)
);

CREATE TABLE IF NOT EXISTS Doctor
(
    DoctorID int primary key auto_increment,
    FirstName varchar(50),
    LastName varchar(50),
    Specialty varchar(50)
);

CREATE TABLE IF NOT EXISTS Nurse
(
    NurseID int primary key auto_increment,
    FirstName varchar(50),
    LastName varchar(50),
    Specialty varchar(50)
);

CREATE TABLE IF NOT EXISTS DrAppointment
(
    AppointmentID int primary key auto_increment,
    PatientID int,
    AppointmentDate date,
    AppointmentTime time,
    AppointmentType varchar(50),
    AmountToBePaid decimal(10,2) default 110,
    DoctorID int,
    constraint fk_patient_appointment foreign key (PatientID) references Patient(PatientID),
    constraint fk_doctor foreign key (DoctorID) references Doctor(DoctorID)
);

CREATE TABLE IF NOT EXISTS NurseAppointment
(
    AppointmentID int primary key auto_increment,
    PatientID int,
    AppointmentDate date,
    AppointmentTime time,
    AppointmentType varchar(50),
    NurseID int,
    AmountToBePaid decimal(10,2) default 50,
    constraint fk_patient_nurse foreign key (PatientID) references Patient(PatientID),
    constraint fk_nurse foreign key (NurseID) references Nurse(NurseID)
);

CREATE TABLE IF NOT EXISTS DrInvoice
(
    InvoiceID int primary key auto_increment,
    InvoiceDate date,
    InvoiceAmount decimal(10,2),
    AppointmentID int,
    constraint fk_appointment_invoice foreign key (AppointmentID) references DrAppointment(AppointmentID)
);

CREATE TABLE IF NOT EXISTS NurseInvoice
(
    InvoiceID int primary key auto_increment,
    InvoiceDate date,
    InvoiceAmount decimal(10,2),
    AppointmentID int,
    constraint fk_appointment_invoice_nurse foreign key (AppointmentID) references NurseAppointment(AppointmentID)
);

-- Stored Procedure to Create a Doctor
DELIMITER //
DROP PROCEDURE IF EXISTS CreateDoctor;
CREATE PROCEDURE CreateDoctor(
    IN p_FirstName VARCHAR(50),
    IN p_LastName VARCHAR(50),
    IN p_Specialty VARCHAR(50)
)
BEGIN
    INSERT INTO Doctor (FirstName, LastName, Specialty)
    VALUES (p_FirstName, p_LastName, p_Specialty);
END //
DELIMITER ;

-- Stored Procedure to Update a Doctor
DELIMITER //
DROP PROCEDURE IF EXISTS UpdateDoctor;
CREATE PROCEDURE UpdateDoctor(
    IN p_DoctorID INT,
    IN p_FirstName VARCHAR(50),
    IN p_LastName VARCHAR(50),
    IN p_Specialty VARCHAR(50)
)
BEGIN
    UPDATE Doctor
    SET FirstName = p_FirstName, LastName = p_LastName, Specialty = p_Specialty
    WHERE DoctorID = p_DoctorID;
END //
DELIMITER ;

-- Stored Procedure to Delete a Doctor
DELIMITER //
DROP PROCEDURE IF EXISTS DeleteDoctor;
CREATE PROCEDURE DeleteDoctor(
    IN p_DoctorID INT
)
BEGIN
    DELETE FROM Doctor WHERE DoctorID = p_DoctorID;
END //
DELIMITER ;

-- Stored Procedure to Create a Nurse
DELIMITER //
DROP PROCEDURE IF EXISTS CreateNurse;
CREATE PROCEDURE CreateNurse(
    IN p_FirstName VARCHAR(50),
    IN p_LastName VARCHAR(50),
    IN p_Specialty VARCHAR(50)
)
BEGIN
    INSERT INTO Nurse (FirstName, LastName, Specialty)
    VALUES (p_FirstName, p_LastName, p_Specialty);
END //
DELIMITER ;

-- Stored Procedure to Update a Nurse
DELIMITER //
DROP PROCEDURE IF EXISTS UpdateNurse;
CREATE PROCEDURE UpdateNurse(
    IN p_NurseID INT,
    IN p_FirstName VARCHAR(50),
    IN p_LastName VARCHAR(50),
    IN p_Specialty VARCHAR(50)
)
BEGIN
    UPDATE Nurse
    SET FirstName = p_FirstName, LastName = p_LastName, Specialty = p_Specialty
    WHERE NurseID = p_NurseID;
END //
DELIMITER ;

-- Stored Procedure to Delete a Nurse
DELIMITER //
DROP PROCEDURE IF EXISTS DeleteNurse;
CREATE PROCEDURE DeleteNurse(
    IN p_NurseID INT
)
BEGIN
    DELETE FROM Nurse WHERE NurseID = p_NurseID;
END //
DELIMITER ;