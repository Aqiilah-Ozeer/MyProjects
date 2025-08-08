use master
CREATE DATABASE PAsystem;

CREATE TABLE Shelter (
    ShelterID INT PRIMARY KEY,
    ShelterName VARCHAR(25) NOT NULL,
    Street VARCHAR(50) NOT NULL,
	City VARCHAR(25) NOT NULL,
    ShelterContact VARCHAR(20) NOT NULL
);

CREATE TABLE Pet (
    PetID INT PRIMARY KEY,
    PetName VARCHAR(25) NOT NULL,
    Breed VARCHAR(25) NOT NULL,
    Age INT CHECK(Age>0),
    PetStatus VARCHAR(20) CHECK (PetStatus IN ('Available', 'Adopted')),
	AdoptionFee DECIMAL(7,2) CHECK (AdoptionFee > 1000),
    ShelterID INT,
    FOREIGN KEY (ShelterID) REFERENCES Shelter(ShelterID) ON DELETE CASCADE
);

CREATE TABLE Adopter (
    AdopterID INT PRIMARY KEY,
    AdopterName VARCHAR(25) NOT NULL,
    AdopterContact VARCHAR(20) NOT NULL,
    Street VARCHAR(50) NOT NULL,
	City VARCHAR(25) NOT NULL,
);

CREATE TABLE Staff (
    StaffID INT PRIMARY KEY,
    StaffName VARCHAR(25) NOT NULL,
    StaffRole VARCHAR(25) NOT NULL,
    ShelterID INT,
    FOREIGN KEY (ShelterID) REFERENCES Shelter(ShelterID) ON DELETE CASCADE
);

CREATE TABLE Adoption (
    AdoptionID INT PRIMARY KEY,
    PetID INT,
    AdopterID INT,
    AdoptionDate DATE NOT NULL,
    AdoptionStatus VARCHAR(20) CHECK (AdoptionStatus IN ('Pending','Approved','Rejected')),
    FOREIGN KEY (PetID) REFERENCES Pet(PetID),
    FOREIGN KEY (AdopterID) REFERENCES Adopter(AdopterID)
);

CREATE TABLE UserAccount (
    UserID INT PRIMARY KEY,
    Username VARCHAR(50) NOT NULL,
    Password VARCHAR(16) NOT NULL,
    UserRole VARCHAR(20) CHECK (UserRole IN ('Staff','Adopter')),
    StaffID INT,
    AdopterID INT,
    FOREIGN KEY (StaffID) REFERENCES Staff(StaffID) ON DELETE CASCADE,
    FOREIGN KEY (AdopterID) REFERENCES Adopter(AdopterID) ON DELETE CASCADE
);

CREATE TABLE Payment (
    PaymentID INT PRIMARY KEY,
    Amount DECIMAL(10, 2) CHECK (Amount>1000),
    PaymentDate DATE NOT NULL,
    AdoptionID INT,
    FOREIGN KEY (AdoptionID) REFERENCES Adoption(AdoptionID) ON DELETE CASCADE
);

--1. Shelter Table
INSERT INTO Shelter (ShelterID, ShelterName, Street, City, ShelterContact) VALUES
(1, 'Paws Haven', '123 Maple Street', 'Port Louis', '+230 1234567'),
(2, 'Fur Friends', '456 Oak Avenue', 'Vacoas', '+230 7654321'),
(3, 'Whisker Wonders', '789 Pine Road', 'Curepipe', '+230 9876543');

--2. Pet Table
INSERT INTO Pet (PetID, PetName, Breed, Age, PetStatus, AdoptionFee, ShelterID) VALUES
(1, 'Buddy', 'Golden Retriever', 3, 'Available', 7500.00, 1),
(2, 'Shadow', 'German Shepherd', 4, 'Adopted', 6500.00, 1),
(3, 'Luna', 'Siberian Husky', 2, 'Available', 9000.00, 2),
(4, 'Bella', 'Persian Cat', 1, 'Available', 5000.00, 3);

--3. Adopter Table
INSERT INTO Adopter (AdopterID, AdopterName, AdopterContact, Street, City) VALUES
(1, 'John Doe', '+230 1122334', '34 Elm Street', 'Port Louis'),
(2, 'Jane Smith', '+230 5566778', '78 Birch Road', 'Vacoas'),
(3, 'Alice Brown', '+230 9988776', '21 Cedar Lane', 'Curepipe');

--4. Staff Table
INSERT INTO Staff (StaffID, StaffName, StaffRole, ShelterID) VALUES
(1, 'Emma Wilson', 'Manager', 1),
(2, 'Liam Johnson', 'Caretaker', 2),
(3, 'Noah Davis', 'Veterinarian', 3);

--5. Adoption Table
INSERT INTO Adoption (AdoptionID, PetID, AdopterID, AdoptionDate, AdoptionStatus) VALUES
(1, 2, 1, '2025-01-15', 'Approved'),
(2, 4, 2, '2025-02-20', 'Pending');

--6. UserAccount Table
INSERT INTO UserAccount (UserID, Username, Password, UserRole, StaffID, AdopterID) VALUES
(1, 'emma.w', 'securePass248', 'Staff', 1, NULL),
(2, 'john.d', 'adoptMe789', 'Adopter', NULL, 1);

--7. Payment Table
INSERT INTO Payment (PaymentID, Amount, PaymentDate, AdoptionID) VALUES
(1, 6500.00, '2025-01-16', 1);

select * from Pet
delete from Pet where PetID = 5