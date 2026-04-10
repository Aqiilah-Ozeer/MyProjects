CREATE DATABASE IF NOT EXISTS pet_adoption;
USE pet_adoption;

DROP TABLE IF EXISTS Pet;
CREATE TABLE Pet (
    PetID INT AUTO_INCREMENT PRIMARY KEY,
    PetName VARCHAR(50),
    Animal VARCHAR(20),
    Breed VARCHAR(50),
    Age INT,
    Gender VARCHAR(10),
    PetStatus VARCHAR(20),
    AdoptionFee INT,
    Summary TEXT,
    Tags VARCHAR(255)
);

INSERT INTO Pet (PetName, Animal, Breed, Age, Gender, PetStatus, AdoptionFee, Summary, Tags) VALUES
('Bella', 'Dog', 'Border Collie Mix', 3, 'Female', 'Available', 3500, 'Bella is a smart and energetic dog who loves to play fetch and learn new tricks.', 'Intelligent,Energetic,Trained'),
('Milo', 'Cat', 'Domestic Shorthair', 1, 'Male', 'Available', 3000, 'Milo is a cuddly and playful cat who enjoys naps and chasing toys.', 'Affectionate,Litter-trained,Playful'),
('Luna', 'Dog', 'Golden Retriever Mix', 2, 'Female', 'Available', 4000, 'Luna is a gentle dog who adores kids and gets along well with other pets.', 'Good with kids,House-trained'),
('Tweety', 'Bird', 'Canary', 1, 'Male', 'Available', 500, 'Tweety is a cheerful canary with a beautiful singing voice that brightens any room.', 'Excellent singer,Lovely,Trained'),
('Misty', 'Cat', 'Colorpoint Shorthair', 2, 'Female', 'Available', 3000, 'Misty is a calm and curious cat who enjoys quiet corners and gentle affection.', 'Quiet,Curious,Affectionate'),
('Max', 'Dog', 'Golden Retriever', 5, 'Male', 'Available', 4000, 'Max is a loyal dog who loves long walks and belly rubs.', 'Loyal,Calm,Trained');
