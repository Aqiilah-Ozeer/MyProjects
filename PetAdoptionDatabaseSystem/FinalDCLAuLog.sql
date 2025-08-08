
-- Security Measures (DCL)


-- Create roles
CREATE ROLE AdminRole;
CREATE ROLE StaffRole;
CREATE ROLE AdopterRole;

-- Grant permissions to AdminRole (full control)
GRANT CONTROL ON DATABASE::PAsystem TO AdminRole;
GRANT SELECT, INSERT, UPDATE, DELETE ON SCHEMA::dbo TO AdminRole;

-- Grant permissions to StaffRole (limited access)
GRANT SELECT, INSERT, UPDATE ON Pet TO StaffRole;
GRANT SELECT, INSERT, UPDATE ON Adoption TO StaffRole;
GRANT SELECT ON Adopter TO StaffRole;
REVOKE DELETE ON Pet FROM StaffRole; -- Staff cannot delete pets

-- Grant permissions to AdopterRole (read-only + limited writes)
GRANT SELECT ON Pet TO AdopterRole;
GRANT INSERT ON Adoption TO AdopterRole; -- Adopters can apply for adoption
GRANT INSERT, SELECT ON Payment TO AdopterRole; -- Adopters can make/view payments
REVOKE DELETE, UPDATE ON Adoption FROM AdopterRole; -- No modification of adoption records

-- Assign users to roles (example)
CREATE USER AdminUser WITHOUT LOGIN;
CREATE USER StaffUser WITHOUT LOGIN;
CREATE USER AdopterUser WITHOUT LOGIN;

ALTER ROLE AdminRole ADD MEMBER AdminUser;
ALTER ROLE StaffRole ADD MEMBER StaffUser;
ALTER ROLE AdopterRole ADD MEMBER AdopterUser;

-- 5. Data Integrity Constraints


-- Enforcing Unique Username
ALTER TABLE UserAccount
ADD CONSTRAINT UQ_Username UNIQUE (Username);

-- Enforcing Password Strength
ALTER TABLE UserAccount
ADD CONSTRAINT CK_PwdStrength 
CHECK (LEN(Password) >= 8 
		AND LEN(Password) <= 16 
		AND Password LIKE '%[a-zA-Z]%' 
		AND Password LIKE '%[0-9]%');

-- Audit and Monitoring


-- Creating an Audit Log Table
CREATE TABLE AuditLog (
    LogID INT PRIMARY KEY IDENTITY(1,1),
    UserName NVARCHAR(100),
    Action NVARCHAR(50),
    TableName NVARCHAR(50),
    TimeStamp DATETIME DEFAULT GETDATE()
);

-- Trigger: Log Changes in Adoption Table
CREATE TRIGGER trg_LogAdoptionActions 
ON Adoption AFTER INSERT, UPDATE, DELETE 
AS 
BEGIN 
	-- Exit if no rows affected 
	IF (@@ROWCOUNT = 0) 
		RETURN; 
	DECLARE @changetype VARCHAR(10); 
	-- Determine type of change 
	IF EXISTS (SELECT * FROM INSERTED) AND EXISTS (SELECT * FROM DELETED) 
		SET @changetype = 'update'; 
	ELSE IF EXISTS (SELECT * FROM INSERTED) 
		SET @changetype = 'insert'; 
	ELSE IF EXISTS (SELECT * FROM DELETED) 
		SET @changetype = 'delete'; 
	-- Log changes into AuditLog 
	IF (@changetype = 'insert' OR @changetype = 'update') 
	BEGIN 
		INSERT INTO AuditLog (UserName, Action, TableName, TimeStamp)
		SELECT SUSER_NAME(), @changetype, 'Adoption' , GETDATE()
		FROM INSERTED;
	END;
	ELSE IF (@changetype = 'delete') 
	BEGIN
		INSERT INTO AuditLog (UserName, Action, TableName, TimeStamp)
		SELECT SUSER_NAME(), @changetype, 'Adoption' , GETDATE()
		FROM DELETED; 
	END;
END;

-- Trigger: Log Changes in UserAccount Table
CREATE TRIGGER trg_LogUserAccountActions 
ON UserAccount AFTER INSERT, UPDATE, DELETE 
AS 
BEGIN 
	IF (@@ROWCOUNT = 0) 
		RETURN; 
	DECLARE @changetype VARCHAR(10); 
	-- Determine the type of change 
	IF EXISTS (SELECT * FROM INSERTED) AND EXISTS (SELECT * FROM DELETED) 
		SET @changetype = 'update'; 
	ELSE IF EXISTS (SELECT * FROM INSERTED) 
		SET @changetype = 'insert'; 
	ELSE IF EXISTS (SELECT * FROM DELETED) 
		SET @changetype = 'delete';
	-- Insert change info into AuditLog 
	IF (@changetype = 'insert' OR @changetype = 'update') 
	BEGIN 
		INSERT INTO AuditLog (UserName, Action, TableName, TimeStamp)
		SELECT SUSER_NAME(), @changetype, 'UserAccount' , GETDATE()
		FROM INSERTED; 
	END; 
	ELSE IF (@changetype = 'delete') 
	BEGIN 
		INSERT INTO AuditLog (UserName, Action, TableName, TimeStamp)
		SELECT SUSER_NAME(), @changetype, 'UserAccount' , GETDATE()
		FROM DELETED; 
	END;
END;


-- Trigger: Log Changes in UserPayment Table
CREATE TRIGGER trg_LogUserPaymentActions 
ON Payment AFTER INSERT, UPDATE, DELETE 
AS 
BEGIN 
	IF (@@ROWCOUNT = 0) 
		RETURN; 
	DECLARE @changetype VARCHAR(10); 
	-- Determine the change type 
	IF EXISTS (SELECT * FROM INSERTED) AND EXISTS (SELECT * FROM DELETED)
		SET @changetype = 'update'; 
	ELSE IF EXISTS (SELECT * FROM INSERTED) 
		SET @changetype = 'insert'; 
	ELSE IF EXISTS (SELECT * FROM DELETED) 
		SET @changetype = 'delete'; 
	-- Insert into AuditLog accordingly 
	IF (@changetype = 'insert' OR @changetype = 'update') 
	BEGIN 
		INSERT INTO AuditLog (UserName, Action, TableName, TimeStamp)
		SELECT SUSER_NAME(), @changetype, 'Payment' , GETDATE()
		FROM INSERTED; 
	END; 
	ELSE IF (@changetype = 'delete') 
	BEGIN 
		INSERT INTO AuditLog (UserName, Action, TableName, TimeStamp)
		SELECT SUSER_NAME(), @changetype, 'Payment' , GETDATE()
		FROM DELETED; 
	END; 
END;

-- Check Audit Logs
SELECT * FROM AuditLog ORDER BY TimeStamp DESC;