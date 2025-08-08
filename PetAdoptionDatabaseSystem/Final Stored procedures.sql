----1. Adding a new pet to a shelter
CREATE PROCEDURE AddPets
	   @petID INT,
       @petName VARCHAR(25),
	   @breed  VARCHAR(25),
	   @petAge INT,
	   @petStatus VARCHAR(20),
	   @adoptionFee DECIMAL(7,2),
	   @ShelterID INT
AS
BEGIN
     BEGIN TRY

       INSERT INTO Pet (PetID, PetName, Breed, Age, PetStatus, AdoptionFee, ShelterID)
	   VALUES (@petID, @petName, @breed, @petAge, @petStatus, @adoptionFee, @ShelterID);

	 END TRY

	 BEGIN CATCH

	      SELECT ERROR_MESSAGE() AS ErrorMessage;

	 END CATCH
END;

---Execution
EXEC AddPets 5, 'Cookie', 'Golden Retriever', 2, 'Available', 8000.00, 1;


---2. REgistering an Adopter
CREATE PROCEDURE RegisterAdopter
	   @adopterID INT,
       @adopterName VARCHAR(25),
	   @adopterContact VARCHAR(20),
	   @street VARCHAR(50),
	   @city VARCHAR(25),
	   @ResultMessage VARCHAR(200) OUTPUT
AS
BEGIN
     SET @ResultMessage = '';
	 BEGIN TRY
	       INSERT INTO Adopter (AdopterID, AdopterName, AdopterContact, Street, City)
		   VALUES (@adopterID, @adopterName, @adopterContact, @street, @city);

		   SET @ResultMessage = 'Adopter registered successfully.';
     END TRY
	 BEGIN CATCH 
	       SET @ResultMessage = 'Error occurred: ' + ERROR_MESSAGE();
	 END CATCH

END;
---Execute
DECLARE @Message VARCHAR(200);

EXEC RegisterAdopter
	 @adopterID = 4,
     @adopterName = 'Kendall Jenner',
	 @adopterContact = '56729473',
	 @street = 'Voldemort Street',
	 @city = 'Port Louis',
	 @ResultMessage = @Message OUTPUT;

PRINT @Message



---3.Deleting a staff from the table
CREATE PROCEDURE DeleteStaff
       @StaffID INT,
	   @ResultMessage NVARCHAR(200) OUTPUT
AS
BEGIN 
     SET @ResultMessage = '';
     BEGIN TRY
	     DELETE FROM Staff WHERE StaffID = @StaffID;

		 --Checks if any row was deleted
		 IF @@ROWCOUNT  = 0

		    SET @ResultMessage = 'Error: StaffID does not exist.';

         ELSE 
		    
			SET @ResultMessage = 'Staff deleted successfully.' ;

	 END TRY

	 BEGIN CATCH
	       --If other errors occur
	       SET @ResultMessage = 'Error occurred: ' + ERROR_MESSAGE();
	 END CATCH

END;
--Execution
DECLARE @Result NVARCHAR(200);

EXEC DeleteStaff
     @StaffID = 4,
	 @ResultMessage = @Result OUTPUT;

PRINT @Result;


---4. Displays Pets from specified shelter
CREATE PROCEDURE GetPetsByShelter
       @ShelterID INT
AS
BEGIN
   
   SELECT P.PetID, P.PetName, P.Breed, P.Age, P.PetStatus, P.AdoptionFee, S.ShelterName, S.Street, S.City, S.ShelterContact
   FROM Pet P
   INNER JOIN Shelter S ON P.ShelterID = S.ShelterID
   WHERE P.ShelterID = @ShelterID

END;
---Execute
EXEC GetPetsByShelter @ShelterID = 1;



---5.Record payments
CREATE PROCEDURE RecordPayment
	   @PaymentID INT,
	   @Amount DECIMAL(10,2),
	   @PaymentDate DATE,
	   @AdoptionID INT,
	   @ResultMessage VARCHAR(200) OUTPUT
AS
BEGIN
   SET @ResultMessage = '';
   BEGIN TRY

       --checks if adoption record exists

	   IF EXISTS (SELECT * FROM Adoption WHERE AdoptionID = @AdoptionID)
	   BEGIN
	     
	     --Validate payment amount
	      IF @Amount <= 1000
	      BEGIN 
		     SET @ResultMessage = 'Error: Payment must be greater than Rs 1000 standard fee';
		     RETURN;
		  END

	  --Insert payment record
       INSERT INTO Payment (PaymentID, Amount, PaymentDate, AdoptionID)
	   VALUES (@PaymentID, @Amount, @PaymentDate, @AdoptionID);

	   IF @@ROWCOUNT = 0
	   BEGIN 
	      SET @ResultMessage = 'Error: Payment could not be recorded.';
		  RETURN;
	   END

	   SET @ResultMessage = 'Payment recorded successfully.';
   END 
   ELSE
   BEGIN
       SET @ResultMessage = 'Error: User not found.';
   END
  END TRY

   BEGIN CATCH
       SET @ResultMessage = 'Error occurred: ' + ERROR_MESSAGE();
   END CATCH
END;

--Execute
DECLARE @Message VARCHAR(200);

EXEC RecordPayment
     @PaymentID = 2,
	 @Amount = 5000.00,
	 @PaymentDate = '2025-02-21',
	 @AdoptionID = 2 ,
	 @ResultMessage = @Message OUTPUT;

PRINT @Message;




---6. Update user account
CREATE PROCEDURE UpdateUserAccount
       @UserID INT,
	   @Username VARCHAR(50) = NULL,
	   @Password VARCHAR(16) = NULL,
	   @UserRole VARCHAR(20) = NULL,
	   @ResultMessage NVARCHAR(200)  OUTPUT
AS
BEGIN
    SET @ResultMessage = '';
    BEGIN TRY

	      -- Check if the user exists
          IF NOT EXISTS (SELECT * FROM UserAccount WHERE UserID = @UserID)
          BEGIN
            SET @ResultMessage = 'Error: User not found.';
            RETURN;
          END
	     
		 -- Update fields only if they are provided
	     IF @Username IS NOT NULL
		    UPDATE UserAccount SET Username = @Username WHERE UserID = @UserID;

		 IF @Password IS NOT NULL
		    UPDATE UserAccount SET Password = @Password WHERE UserID = @UserID;

		 IF @UserRole IS NOT NULL
		    UPDATE UserAccount SET UserRole = @UserRole WHERE UserID = @UserID;

		
		-- Check if at least one update happened
		 IF @Username IS NOT NULL OR @Password IS NOT NULL OR @UserRole IS NOT NULL
		    
			 SET @ResultMessage = 'User account successfully updated.';
		 ELSE 
		     
			 SET @ResultMessage = 'Error: No values provided for update.';
    END TRY

	BEGIN CATCH
	    SET @ResultMessage = 'Error occurred: ' + ERROR_MESSAGE();
	END CATCH
END;

---Execution
--Update Only username & password:
DECLARE @Message NVARCHAR(200);

EXEC UpdateUserAccount
     @UserID = 1,
	 @Username = 'NewUser123',
	 @Password = 'security248',
	 @ResultMessage = @Message OUTPUT;

PRINT @Message;
