--Pet Availability Status Update
CREATE TRIGGER UpdatePetStatus
ON Adoption
AFTER UPDATE, INSERT
AS
BEGIN
    DECLARE @adstatus VARCHAR(20), @adid INT;
	SELECT @adid = AdoptionID FROM INSERTED;
	SELECT @adstatus = AdoptionStatus FROM Adoption WHERE AdoptionID = @adid;
	IF @adstatus = 'Approved'
	BEGIN
		UPDATE Pet SET PetStatus = 'Adopted'
		WHERE PetID = (SELECT PetID FROM Adoption WHERE AdoptionID = @adid);
		PRINT 'Adoption status updated successfully!';
	END;
END;

--Adoption Status Management Trigger
CREATE TRIGGER ManageAdoptionStatus
ON Adoption
INSTEAD OF UPDATE, INSERT
AS
BEGIN
	--Adoption Date Auto-Insert
	DECLARE @date DATE;
	SELECT @date = AdoptionDate FROM INSERTED
	IF @date IS NULL
	BEGIN
		SET @date = GETDATE()
	END;

	DECLARE @updatestatus VARCHAR(20), @currentstatus VARCHAR(20), @adid INT;
	SELECT @updatestatus = AdoptionStatus,  @adid = AdoptionID FROM INSERTED;
	SELECT @currentstatus = AdoptionStatus FROM Adoption WHERE AdoptionID = @adid;
	IF @currentstatus = 'Approved' AND @updatestatus = 'Pending'
	BEGIN
		PRINT 'This adoption has already been approved thus cannot be pending again!!!';
		RETURN;
	END;
	ELSE IF @currentstatus = 'Approved' AND @updatestatus = 'Rejected'
	BEGIN
		PRINT 'This adoption has already been approved thus cannot be rejected!!!';
		RETURN;
	END;
	ELSE
    BEGIN
		-- check if INSERT or UPDATE operation
		IF EXISTS (SELECT * FROM Adoption WHERE AdoptionID = @adid)
		BEGIN
			UPDATE Adoption
			SET AdoptionStatus = @updatestatus
			WHERE AdoptionID = @adid;
			PRINT 'Adoption status updated successfully!';
		END;
		ELSE
		BEGIN
			INSERT INTO Adoption (AdoptionID, PetID, AdopterID, AdoptionDate, AdoptionStatus)
			SELECT AdoptionID, PetID, AdopterID, @date, AdoptionStatus
			FROM INSERTED;
		END;
	END;
END;

--Adoption Fee Validation Trigger
CREATE TRIGGER ValidateAdoptionFee
ON Pet
INSTEAD OF UPDATE, INSERT
AS
BEGIN
	DECLARE @fee DECIMAL(7,2);
	SELECT @fee = AdoptionFee FROM INSERTED;
	IF @fee < 1000 --you guys can change it if you want
	BEGIN
		PRINT 'Invalid fee!!! Adoption fee should be the standard fee of Rs 1000 or greater';
		RETURN;
	END;
	ELSE
    BEGIN
        -- check if INSERT or UPDATE operation
        IF EXISTS (SELECT * FROM INSERTED WHERE PetID IN (SELECT PetID FROM Pet))
        BEGIN
            UPDATE Pet
            SET
                PetName = i.PetName,
                Breed = i.Breed,
                Age = i.Age,
                PetStatus = i.PetStatus,
                AdoptionFee = i.AdoptionFee,
                ShelterID = i.ShelterID
            FROM INSERTED i
            WHERE Pet.PetID = i.PetID;
        END
        ELSE
        BEGIN
            INSERT INTO Pet (PetID, PetName, Breed, Age, PetStatus, AdoptionFee, ShelterID)
            SELECT PetID, PetName, Breed, Age, PetStatus, AdoptionFee, ShelterID
            FROM INSERTED;
        END;
        PRINT 'Adoption fee validated successfully and operation completed!';
    END;
END;

--Payment Integrity Trigger
CREATE TRIGGER PaymentIntegrity
ON Payment
INSTEAD OF INSERT
AS
BEGIN
	--Payment Date Auto-Insert
	DECLARE @date DATE;
	SELECT @date = PaymentDate FROM INSERTED
	IF @date IS NULL
	BEGIN
		SET @date = GETDATE()
	END;

	DECLARE @adfee DECIMAL(7,2), @feepaid DECIMAL(7,2), @adid INT;
	SELECT @feepaid = Amount, @adid = AdoptionID FROM INSERTED;
	SELECT @adfee = p.AdoptionFee 
	FROM Pet p, Adoption ad
	WHERE @adid = ad.AdoptionID AND ad.PetID = p.PetID;
	IF @feepaid != @adfee
	BEGIN
		PRINT 'Incorrect payment amount!!! Amount should be ' + CAST(@adfee AS VARCHAR(10));
		RETURN;
	END;
	ELSE
	BEGIN
		INSERT INTO Payment (PaymentID, Amount, PaymentDate, AdoptionID) 
		SELECT PaymentID, Amount, @date , AdoptionID
		FROM INSERTED
	END;
	PRINT 'Amount validated successfully and insert completed!';
END;

--Password Strength Validator
CREATE TRIGGER ValidatePassword
ON UserAccount
INSTEAD OF UPDATE, INSERT
AS
BEGIN
	DECLARE @Password VARCHAR(16)
	SELECT @Password = Password FROM INSERTED
	IF LEN(@Password) >= 8 AND LEN(@Password) <= 16 AND @Password LIKE '%[a-zA-Z]%' AND @Password LIKE '%[0-9]%'
	BEGIN
		-- check if INSERT or UPDATE operation
        IF EXISTS (SELECT * FROM INSERTED WHERE UserID IN (SELECT UserID FROM UserAccount))
        BEGIN
            UPDATE UserAccount
            SET
                UserID = i.UserID, 
				Username = i.Username,
				Password = i.Password,
				UserRole = i.UserRole,
				StaffID = i.StaffID,
				AdopterID = i.AdopterID
            FROM INSERTED i
            WHERE UserAccount.UserID = i.UserID;
        END;
        ELSE
        BEGIN
			INSERT INTO UserAccount (UserID, Username, Password, UserRole, StaffID, AdopterID)
			SELECT UserID, Username, Password, UserRole, StaffID, AdopterID
			FROM INSERTED;
		END;
		PRINT 'Password validated successfully and operation completed!'
	END;
	ELSE
	BEGIN
		PRINT 'Invalid password! It must be 8-16 characters long and contain at least one alphabet and one number.';
	END;
END;
