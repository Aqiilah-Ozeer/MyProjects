CREATE TABLE Lecturer (
    Lecturer_Id VARCHAR(20) PRIMARY KEY,
    First_Name NVARCHAR(50),
    Last_Name NVARCHAR(50),
    Title NVARCHAR(20),
    Email NVARCHAR(100),
    Phone NVARCHAR(20),
    Department NVARCHAR(100),
    Specialisation NVARCHAR(100),
    Qualification NVARCHAR(100),
    Employment_Type NVARCHAR(50)
);
CREATE TABLE Students (
    student_id INT PRIMARY KEY,
    first_name NVARCHAR(50),
    last_name NVARCHAR(50),
    email NVARCHAR(100),
    phone NVARCHAR(20),
    date_of_birth DATE,
    address NVARCHAR(255)
);
CREATE TABLE Users (
    user_id INT PRIMARY KEY IDENTITY(1,1),
    username VARCHAR(50),
    password VARCHAR(100),
    role VARCHAR(20) NOT NULL CHECK (role IN ('Admin', 'Student', 'Lecturer'))
);
CREATE TABLE Courses (
    id          INT           PRIMARY KEY IDENTITY(1,1),
    name        NVARCHAR(100),
    description NVARCHAR(255),
    credits     INT
);
CREATE TABLE Modules (
    id         INT           PRIMARY KEY IDENTITY(1,1),
    name       NVARCHAR(100),
    course_id  INT,
    lecturer_id   VARCHAR(20),
    schedule   NVARCHAR(50),
    credits    INT,
    FOREIGN KEY (course_id) REFERENCES Courses(id),
	FOREIGN KEY (lecturer_id) REFERENCES Lecturer(Lecturer_Id)
);
CREATE TABLE Enrollments (
    student_id       INT,
    course_id        INT,
    enrollment_date  VARCHAR(20),
    PRIMARY KEY (student_id, course_id),
    FOREIGN KEY (student_id) REFERENCES Students(student_id),
    FOREIGN KEY (course_id)  REFERENCES Courses(id)
);
CREATE TABLE Payments (
    PaymentID   INT            PRIMARY KEY IDENTITY(1,1),
    StudentID   INT,
    Amount      DECIMAL(10,2),
    PaymentDate DATETIME,
    Method      VARCHAR(50),
    CardNumber  VARCHAR(20),
    Course      VARCHAR(100),
    Year        INT,
    FOREIGN KEY (StudentID) REFERENCES Students(student_id)
);
CREATE TABLE Results (
    result_id INT PRIMARY KEY IDENTITY(1,1),
    student_id INT,
    module_id INT,
    marks INT,
    grade NVARCHAR(5),
    FOREIGN KEY (student_id)
        REFERENCES Students(student_id),
    FOREIGN KEY (module_id)
        REFERENCES Modules(id)
);

GO

--Triggers for users sync: -- When a student is added, create a user
CREATE TRIGGER trg_AddStudentUser
ON Students
AFTER INSERT
AS
BEGIN
    INSERT INTO Users (username, password, role)
    SELECT CAST(student_id AS VARCHAR(50)), 'student123', 'Student'
    FROM inserted;
END;
GO

GO

-- When a student is deleted, remove the user
CREATE TRIGGER trg_DeleteStudentUser
ON Students
AFTER DELETE
AS
BEGIN
    DELETE FROM Users
    WHERE username IN (SELECT CAST(student_id AS VARCHAR(50)) FROM deleted)
      AND role = 'Student';
END;
GO

GO

-- When a lecturer is added, create a user
CREATE TRIGGER trg_AddLecturerUser
ON Lecturer
AFTER INSERT
AS
BEGIN
    INSERT INTO Users (username, password, role)
    SELECT Lecturer_Id, 'lecturer123', 'Lecturer'
    FROM inserted;
END;
GO

GO

-- When a lecturer is deleted, remove the user
CREATE TRIGGER trg_DeleteLecturerUser
ON Lecturer
AFTER DELETE
AS
BEGIN
    DELETE FROM Users
    WHERE username IN (SELECT Lecturer_Id FROM deleted)
      AND role = 'Lecturer';
END;
GO
INSERT INTO Users (username, password, role) VALUES
('admin', 'admin123', 'Admin');

INSERT INTO Lecturer VALUES ('LEC-001','Severus',   'Snape',      'Prof.', 'snape@hogwarts.edu',         '+230-59783178','Potions',                       'Potions',                       'PhD','Full-Time');
INSERT INTO Lecturer VALUES ('LEC-002','Pomona',    'Sprout',     'Prof.', 'sprout@hogwarts.edu',        '+230-52254778','Herbology',                     'Herbology',                     'PhD','Full-Time');
INSERT INTO Lecturer VALUES ('LEC-003','Minerva',   'McGonagall', 'Prof.', 'mcgonagall@hogwarts.edu',    '+230-59365875','Transfiguration',               'Transfiguration',               'PhD','Full-Time');
INSERT INTO Lecturer VALUES ('LEC-004','Filius',    'Flitwick',   'Prof.', 'flitwick@hogwarts.edu',      '+230-58284575','Charms',                        'Charms',                        'PhD','Part-Time');
INSERT INTO Lecturer VALUES ('LEC-005','Rubeus',    'Hagrid',     'Prof.', 'hagrid@hogwarts.edu',        '+230-51243869','Magical Creatures',             'Care of Magical Creatures',     'MSc','Full-Time');
INSERT INTO Lecturer VALUES ('LEC-006','Remus',     'Lupin',      'Prof.', 'lupin@hogwarts.edu',         '+230-59112233','Defence Against the Dark Arts', 'Defence Against the Dark Arts', 'PhD','Full-Time');
INSERT INTO Lecturer VALUES ('LEC-007','Cuthbert',  'Binns',      'Prof.', 'binns@hogwarts.edu',         '+230-59334455','History of Magic',              'History of Magic',              'PhD','Full-Time');
INSERT INTO Lecturer VALUES ('LEC-008','Sybill',    'Trelawney',  'Prof.', 'trelawney@hogwarts.edu',     '+230-59556677','Divination',                    'Divination',                    'PhD','Part-Time');
INSERT INTO Lecturer VALUES ('LEC-009','Bathsheda', 'Babbling',   'Prof.', 'babbling@hogwarts.edu',      '+230-59778899','Ancient Runes',                 'Ancient Runes',                 'PhD','Full-Time');
INSERT INTO Lecturer VALUES ('LEC-010','Rolanda',   'Hooch',      'Madam', 'hooch@hogwarts.edu',         '+230-59990011','Flying',                        'Flying & Quidditch',            'MSc','Full-Time');

INSERT INTO Students VALUES (241001,'Neville','Longbottom','neville.longbottom@umail.com','5012 3456','2001-07-30','12 Longbottom Lane, Hogsmeade, Scotland');
INSERT INTO Students VALUES (241002,'Seamus','Finnigan',  'seamus.finnigan@umail.com',  '5012 3457','2002-04-15','45 Finnigan Road, Dublin, Ireland');
INSERT INTO Students VALUES (241003,'Cho',   'Chang',     'cho.chang@umail.com',         '5012 3458','2001-09-24','8 Cherry Blossom Court, Edinburgh, Scotland');
INSERT INTO Students VALUES (241004,'Colin', 'Creevey',   'colin.creevey@umail.com',     '5012 3459','2003-02-10','22 Creevey Cottage, Ottery St Catchpole, Devon');
INSERT INTO Students VALUES (241005,'Dean',  'Thomas',    'dean.thomas@umail.com',       '5012 3460','2001-12-03','19 Thomas Street, London, England');

-- 5 Hogwarts Courses (grouped subjects, 30 credits each)
INSERT INTO Courses (name, description, credits) VALUES
    ('Transfiguration',               'The art of changing the form or appearance of an object',                30),
    ('Charms',                         'Spells that add properties to an object or creature',                  30),
    ('Potions',                        'The art of creating mixtures with magical effects',                    30),
    ('History of Magic',               'Study of magical history, goblin rebellions and wizarding wars',       30),
    ('Defence Against the Dark Arts',  'Defensive magic against dark creatures, curses and the Dark Arts',     30),
    ('Astronomy',                      'Study of stars, planets and celestial magical events',                 30),
    ('Herbology',                      'Study of magical and mundane plants and their uses',                   30);

-- Course 1: Transfiguration (30 cr) – Prof. McGonagall
INSERT INTO Modules (name, course_id, lecturer_id, schedule, credits) VALUES
    ('Switching Spells',              1, 'LEC-003', 'Mon 9-11',   10),
    ('Vanishing & Conjuring',         1, 'LEC-003', 'Wed 9-11',   10),
    ('Human Transfiguration',         1, 'LEC-003', 'Fri 9-11',   10);

-- Course 2: Charms (30 cr) – Prof. Flitwick
INSERT INTO Modules (name, course_id, lecturer_id, schedule, credits) VALUES
    ('Charm Theory',                  2, 'LEC-004', 'Mon 11-13',  10),
    ('Practical Charms',              2, 'LEC-004', 'Wed 11-13',  10),
    ('Advanced Enchantments',         2, 'LEC-004', 'Fri 11-13',  10);

-- Course 3: Potions (30 cr) – Prof. Snape
INSERT INTO Modules (name, course_id, lecturer_id, schedule, credits) VALUES
    ('Potion Theory',                 3, 'LEC-001', 'Tue 9-11',   10),
    ('Practical Brewing',             3, 'LEC-001', 'Thu 9-11',   10),
    ('Antidotes & Elixirs',           3, 'LEC-001', 'Fri 14-16',  10);

-- Course 4: History of Magic (30 cr) – Prof. Binns
INSERT INTO Modules (name, course_id, lecturer_id, schedule, credits) VALUES
    ('Goblin Rebellions',             4, 'LEC-007', 'Mon 14-16',  10),
    ('Medieval Wizarding History',    4, 'LEC-007', 'Wed 14-16',  10),
    ('International Statute of Secrecy', 4, 'LEC-007', 'Fri 14-16', 10);

-- Course 5: Defence Against the Dark Arts (30 cr) – Prof. Lupin
INSERT INTO Modules (name, course_id, lecturer_id, schedule, credits) VALUES
    ('Dark Creatures',                5, 'LEC-006', 'Tue 11-13',  10),
    ('Counter-Jinxes & Hexes',        5, 'LEC-006', 'Thu 11-13',  10),
    ('Patronus & Advanced Defence',   5, 'LEC-006', 'Fri 11-13',  10);

-- Course 6: Astronomy (30 cr) – Prof. McGonagall
INSERT INTO Modules (name, course_id, lecturer_id, schedule, credits) VALUES
    ('Star Charts',                   6, 'LEC-003', 'Wed 21-23',  10),
    ('Planetary Movements',           6, 'LEC-003', 'Thu 21-23',  10),
    ('Celestial Phenomena',           6, 'LEC-003', 'Fri 21-23',  10);

-- Course 7: Herbology (30 cr) – Prof. Sprout
INSERT INTO Modules (name, course_id, lecturer_id, schedule, credits) VALUES
    ('Magical Plants',                7, 'LEC-002', 'Mon 14-16',  10),
    ('Dangerous Flora',               7, 'LEC-002', 'Wed 14-16',  10),
    ('Medicinal Herbology',           7, 'LEC-002', 'Fri 14-16',  10);

INSERT INTO Enrollments VALUES (241001, 7, '2026-01-15');   -- Neville → Herbology
INSERT INTO Enrollments VALUES (241002, 5, '2026-01-16');   -- Seamus → DADA
INSERT INTO Enrollments VALUES (241003, 2, '2026-02-01');   -- Cho → Charms
INSERT INTO Enrollments VALUES (241004, 3, '2026-02-05');   -- Colin → Potions

INSERT INTO Results (student_id, module_id, marks, grade) VALUES
    (241001, 19, 85, 'A'),  -- Neville, Magical Plants
    (241002, 13, 72, 'B'),  -- Seamus, Dark Creatures
    (241003,  4, 90, 'A');  -- Cho, Charm Theory

INSERT INTO Payments (StudentID, Amount, PaymentDate, Method, CardNumber, Faculty, Course, Year)
VALUES (241001, 1500.00, GETDATE(), 'Credit Card', '5463 5367 5363 684', 'Herbology', 'Magical Plants', 1);

INSERT INTO Payments (StudentID, Amount, PaymentDate, Method, CardNumber, Faculty, Course, Year)
VALUES (241002, 2000.00, GETDATE(), 'Debit Card', '7847 6474 6474 784', 'Defence Against the Dark Arts', 'Dark Creatures', 1);

INSERT INTO Payments (StudentID, Amount, PaymentDate, Method, CardNumber, Faculty, Course, Year)
VALUES (241003, 1800.00, GETDATE(), 'Bank Transfer', '6438 6282 6822 682', 'Charms', 'Charm Theory', 1);

INSERT INTO Payments (StudentID, Amount, PaymentDate, Method, CardNumber, Faculty, Course, Year)
VALUES (241004, 2200.00, GETDATE(), 'Credit Card', '6383 6382 7844 637', 'Potions', 'Potion Theory', 1);

INSERT INTO Payments (StudentID, Amount, PaymentDate, Method, CardNumber, Faculty, Course, Year)
VALUES (241005, 1600.00, GETDATE(), 'Debit Card', '4728 3792 6289 723', 'History of Magic', 'Goblin Rebellions', 1);

-- Insert payments without Faculty column
INSERT INTO Payments (StudentID, Amount, PaymentDate, Method, CardNumber, Course, Year)
VALUES (241001, 1500.00, GETDATE(), 'Credit Card', '5463 5367 5363 684', 'Herbology', 1);

INSERT INTO Payments (StudentID, Amount, PaymentDate, Method, CardNumber, Course, Year)
VALUES (241002, 2000.00, GETDATE(), 'Debit Card', '7847 6474 6474 784', 'Defence Against the Dark Arts', 1);

INSERT INTO Payments (StudentID, Amount, PaymentDate, Method, CardNumber, Course, Year)
VALUES (241003, 1800.00, GETDATE(), 'Bank Transfer', '6438 6282 6822 682', 'Charms', 1);

INSERT INTO Payments (StudentID, Amount, PaymentDate, Method, CardNumber, Course, Year)
VALUES (241004, 2200.00, GETDATE(), 'Credit Card', '6383 6382 7844 637', 'Potions', 1);

select * from Users;