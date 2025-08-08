import sys
import csv
import re
from tabulate import tabulate
from datetime import date
import os

#Taken from copilot (barely modify)
class Utility:
    @staticmethod
    def clear_terminal():
        # Clears the terminal based on the operating system
        os.system('cls' if os.name == 'nt' else 'clear')


class Admin:
    files = ["Dog", "Cat", "Bird", "Rabbit", "Adoption_record"]
    
    def __init__(self):
        self._password = "admin123"

    @property
    def password(self):
       return self._password

    @password.setter
    def password(self,new_password):
        self._password = new_password

    def verify_password(self,entered_password):
        return entered_password == self._password  
    
    def admin_login(self):
        Utility.clear_terminal()
        num_attempts = 3

        while num_attempts > 0:
            entered_password = input("\nEnter admin password: ").strip()
        
            if self.verify_password(entered_password):
                print("Login sucessful!")
                self.display_admin_menu()
                
            num_attempts -= 1
            print(f"Incorrect password. {num_attempts} attempts remaining.")
        
        sys.exit("\nAccess denied. No attempts left")

    def display_admin_menu(self):
        while True:
            print("\nADMIN MENU\n")

            print("1. Confirm Adoption Payment")
            print("2. Add records")
            print("3. Delete records")
            print("4. View file")
            print("5. Update file")
            print("6. Logout")

            choice = input("\nChoose an option (1/2/3/4/5/6): ").strip()
            while choice not in ["1", "2", "3", "4", "5","6"]:
                choice = input("Invalid choice.Please try again -> Enter choice: ").strip()

            if choice == "1":
                self.confirm_payment()
                Utility.clear_terminal()
                
            elif choice == "2":
                self.add_records()
                Utility.clear_terminal()
            
            elif choice == "3":
                self.delete_records()
                Utility.clear_terminal()

            elif choice == "4":
                self.view_files()
                Utility.clear_terminal()
            
            elif choice == "5":
                self.update_file()
                Utility.clear_terminal()
            
            elif choice == "6":
                sys.exit("Logging out....")
                
    @staticmethod
    def confirm_payment():
        while True:
            id = input("\nEnter adoption id of payment: ").strip().capitalize()
            while not re.fullmatch(r"Ad\d{3}",id):
                id = input("Invalid id -> Enter id again: ").strip().capitalize()
                
            adopters = Adopter.get_all_adopters()
            
            found = False
            for a in adopters:
                if a.Adoption_Id == id:
                    found = True
                    a.Payment = "Done"
            if found:
                print("\nRecord(s) payment confirm")
            else:
                print("\nRecord not found")
                return
            
            continue_confirmation = input("\nContinue confirmation of payments[y/n]? ")
            while continue_confirmation not in ["y","n"]:
                continue_confirmation = input("Enter either y/n -> Continue confirmation of payments[y/n]? ").strip()
            
            if continue_confirmation == "n":
                break
                
                
        Adopter.store_modify_data(adopters)
    
    @staticmethod
    def add_records():
        
        print(f"\nFiles available in system: {Admin.files}")
        
        while True:
            filename = input("Enter which file you would like to add records: ").strip().capitalize()
            while filename not in Admin.files:
                filename = input("File doesn't exist -> Enter an existing file: ").strip().capitalize()
            while True:
                if filename == "Adoption_record":
                    animaltype = input("\nEnter which animal is being adopted: ").strip().capitalize()
                    while animaltype not in ["Dog", "Cat", "Bird", "Rabbit"]:
                        animaltype = input("This animal do not exist in system database -> Enter another animal: ").strip().capitalize()
                    
                    animals = Animal.get_animals(animaltype)
                    print()
                    Adopter.adopt_pet(animaltype, animals)
                else:
                    print()
                    Animal.store_new_animal(filename)
                
                continue_add = input("\nAdd more records[y/n]? ").strip()
                while continue_add not in ["y","n"]:
                    continue_add = input("Enter either y/n -> Add more records[y/n]? ").strip()

                if continue_add == "n":
                    break
                else:
                    Utility.clear_terminal()
            
            continue_add = input("\nContinue addision to other files[y/n]? ").strip()
            while continue_add not in ["y","n"]:
                continue_add = input("Enter either y/n -> Continue addision to other files[y/n]? ").strip()
                
            if continue_add == "n":
                return
            else:
                Utility.clear_terminal()
                print(f"\nFiles available in system: {Admin.files}")
                
    @staticmethod
    def delete_records():
        print(f"\nFiles available in system: {Admin.files}")
        
        while True:
            filename = input("Enter which file you would like to delete records: ").strip().capitalize()
            while filename not in Admin.files:
                filename = input("File doesn't exist -> Enter an existing file: ").strip().capitalize()
            
            delete_type = input("\nPerform single delete or mass delete by criteria[s/m]? ").strip()
            while delete_type not in ["s","m"]:
                delete_type = input("Enter either s/m -> Perform single delete or mass delete by criteria[s/m]? ").strip()
                
            if delete_type == "s":
                while True:
                    if filename == "Adoption_record":
                        adopters = Adopter.get_all_adopters()
                        
                        id = input("\nEnter adoption id of record to be deleted: ").strip().capitalize()
                        while not re.fullmatch(r"Ad\d{3}",id):
                            id = input("Invalid id -> Enter id again: ").strip().capitalize()
                        
                        found = False
                        for a in adopters:
                            if a.Adoption_Id == id:
                                found = True
                                adopters.remove(a)
                        if found:
                            print("Record deleted")
                        else:
                            print("Record not found")
                            return
                        
                    else:
                        animals = Animal.get_animals(filename)
                        
                        id = input("\nEnter animal id of record to be deleted: ").strip().capitalize()
                        l = filename[0]
                        while not re.fullmatch(rf"{l}\d{{3}}",id):
                            id = input("Invalid -> Enter id again: ").strip().capitalize()
                        
                        found = False
                        for a in animals:
                            if a.Animal_Id == id:
                                found = True
                                animals.remove(a)
                        if found:
                            print("Record deleted")
                        else:
                            print("Record not found")
                            return
                    
                    continue_del = input("\nDelete more records[y/n]? ").strip()
                    while continue_del not in ["y","n"]:
                        continue_del = input("Enter either y/n -> Delete more records[y/n]? ").strip()
                    
                    if continue_del == "n":
                        break
                    
            else:
                while True:
                    if filename == "Adoption_record":
                        adopters = Adopter.get_all_adopters()
                        
                        field = input("\nEnter the field of the criteria: ").strip().title()
                        value = input("Enter the value of the field: ").strip()
                        field, value = Adopter.valid_field_value(field, value)
                        
                        found = False
                        for a in adopters:
                            if getattr(a, field) == value: #retrieve the value of instance 'a' of the attribute 'field' dynamically(learn from copilot)
                                found = True
                                adopters.remove(a)
                        if found:
                            print("Records deleted")
                        else:
                            print("Record not found")
                            return
                    
                    else:
                        animals = Animal.get_animals(filename)
                        
                        field = input("\nEnter the field of the criteria: ").strip().title()
                        value = input("Enter the value of the field: ").strip().capitalize()
                        field, value = Animal.valid_field_value(filename, field, value)
                        
                        found = False
                        for a in animals:
                            if getattr(a, field) == value:
                                found = True
                                animals.remove(a)
                        if found:
                            print("Records deleted")
                        else:
                            print("Record not found")
                            return
                    
                    continue_del = input("\nDelete more records[y/n]? ").strip()
                    while continue_del not in ["y","n"]:
                        continue_del = input("Enter either y/n -> Delete more records[y/n]? ").strip()
                    
                    if continue_del == "n":
                        break
            
            continue_del = input("\nContinue deletion in other files[y/n]? ").strip()
            while continue_del not in ["y","n"]:
                continue_del = input("Enter either y/n -> Continue deletion in other files[y/n]? ").strip()
                    
            if continue_del == "n":
                break
            else:
                Utility.clear_terminal()
        
        if filename == "Adoption_record":
            Adopter.store_modify_data(adopters)
        else:
            Animal.store_modify_data(filename, animals)
        
    @staticmethod
    def view_files():
        print(f"\nFiles available in system: {Admin.files}")
        while True:
            filename = input("Enter which file you would like to view: ").strip().capitalize()
            while filename not in Admin.files:
                filename = input("File doesn't exist -> Enter an existing file: ").strip().capitalize()
            
            table = []
            with open(f"{filename}.csv") as file:
                reader = csv.DictReader(file)
                for row in reader:
                    table.append(list(row.values()))
                    
            headers = list(row.keys())
            print(tabulate(table, headers, tablefmt="fancy_grid"))
            
            continue_view = input("\nContinue to view files[y/n]? ").strip()
            while continue_view not in ["y","n"]:
                continue_view = input("Enter either y/n -> Continue to view files[y/n]? ").strip()
                    
            if continue_view == "n":
                break
            else:
                Utility.clear_terminal()
            
    
    @staticmethod
    def update_file():
        print(f"\nFiles available in system: {Admin.files}")
        
        while True:
            filename = input("Enter which file you would like to update: ").strip().capitalize()
            while filename not in Admin.files:
                filename = input("File doesn't exist -> Enter an existing file: ").strip().capitalize()
            
            update_type = input("\nPerform single update or mass update[s/m]? ").strip()
            while update_type not in ["s","m"]:
                update_type = input("Enter either s/m -> Perform single update or mass update[s/m]? ").strip()
                
            if update_type == "s":
                while True:
                    if filename == "Adoption_record":
                        adopters = Adopter.get_all_adopters()
                        
                        id = input("\nEnter adoption id of record to be update: ").strip().capitalize()
                        while not re.fullmatch(r"Ad\d{3}",id):
                            id = input("Invalid id -> Enter id again: ").strip().capitalize()
                        
                        field = input("Enter the field to be updated: ").strip().title()
                        new_value = input("Enter the new value of the field: ").strip()
                        field, new_value = Adopter.valid_field_value(field, new_value)
                        
                        found = False
                        for a in adopters:
                            if a.Adoption_Id == id:
                                found = True
                                setattr(a, field, new_value)  #update the value of intance 'a' of attribute 'field' to 'new_value' dynamically(learn from copilot)
                        if found:
                            print("Record updated")
                        else:
                            print("Record not found")
                            return
                        
                    else:
                        animals = Animal.get_animals(filename)
                        
                        id = input("\nEnter animal id of record to be update: ").strip().capitalize()
                        l = filename[0]
                        while not re.fullmatch(rf"{l}\d{{3}}",id):
                            id = input("Invalid -> Enter id again: ").strip().capitalize()
                        
                        field = input("Enter the field to be updated: ").strip().title()
                        new_value = input("Enter the new value of the field: ").strip().capitalize()
                        field, new_value = Animal.valid_field_value(filename, field, new_value)
                        
                        found = False
                        for a in animals:
                            if a.Animal_Id == id:
                                found = True
                                setattr(a, field, new_value)
                        if found:
                            print("Record updated")
                        else:
                            print("Record not found")
                            return
                    
                    continue_update = input("\nUpdate more records[y/n]? ").strip()
                    while continue_update not in ["y","n"]:
                        continue_update = input("Enter either y/n -> Update more records[y/n]? ").strip()
                    
                    if continue_update == "n":
                        break
                    
            else:
                while True:
                    if filename == "Adoption_record":
                        adopters = Adopter.get_all_adopters()
                        
                        field = input("\nEnter the field to be updated: ").strip().title()
                        value = input("Enter the value of the field: ").strip()
                        new_value = input("Enter the new value of the field: ").strip()
                        field, value = Adopter.valid_field_value(field, value)
                        _, new_value = Adopter.valid_field_value(field, new_value)
                        
                        found = False
                        for a in adopters:
                            if getattr(a, field) == value:
                                found = True
                                setattr(a, field, new_value)
                        if found:
                            print("Records updated")
                        else:
                            print("Record not found")
                            return
                    
                    else:
                        animals = Animal.get_animals(filename)
                        
                        field = input("\nEnter the field to be updated: ").strip().title()
                        value = input("Enter the value of the field: ").strip().capitalize()
                        new_value = input("Enter the new value of the field: ").strip().capitalize()
                        field, value = Animal.valid_field_value(filename,field, value)
                        _, new_value = Animal.valid_field_value(filename, field, new_value)
                        
                        found = False
                        for a in animals:
                            if getattr(a, field) == value:
                                found = True
                                setattr(a, field, new_value)
                        if found:
                            print("Records updated")
                        else:
                            print("Record not found")
                            return
                    
                    continue_update = input("\nUpdate more records[y/n]? ").strip()
                    while continue_update not in ["y","n"]:
                        continue_update = input("Enter either y/n -> Update more records[y/n]? ").strip()
                    
                    if continue_update == "n":
                        break
            
            continue_update = input("\nContinue update in other files[y/n]? ").strip()
            while continue_update not in ["y","n"]:
                continue_update = input("Enter either y/n -> Continue update in other files[y/n]? ").strip()
                    
            if continue_update == "n":
                break
            else:
                Utility.clear_terminal()
        
        if filename == "Adoption_record":
            Adopter.store_modify_data(adopters)
        else:
            Animal.store_modify_data(filename, animals)
    
        
class Animal:
    def __init__(self, Animal_Id, Name, Breed, Age, Gender, Color, Vaccinated, Microchipped, Adoption_Fee):
        self._Animal_Id = Animal_Id
        self._Name = Name
        self._Breed = Breed
        self._Age = Age
        self._Gender = Gender
        self._Color = Color
        self._Vaccinated = Vaccinated
        self._Microchipped = Microchipped
        self._Adoption_Fee = Adoption_Fee
    
    @property   
    def Animal_Id(self):
        return self._Animal_Id
    
    @Animal_Id.setter
    def Animal_Id(self, value):
        self._Animal_Id = value

    @property
    def Name(self):
        return self._Name
    
    @Name.setter
    def Name(self, value):
        self._Name = value

    @property
    def Breed(self):
        return self._Breed
    
    @Breed.setter
    def Breed(self, value):
        self._Breed = value

    @property
    def Age(self):
        return self._Age
    
    @Age.setter
    def Age(self, value):
        self._Age = value

    @property
    def Gender(self):
        return self._Gender
    
    @Gender.setter
    def Gender(self, value):
        self._Gender = value

    @property
    def Color(self):
        return self._Color
    
    @Color.setter
    def Color(self, value):
        self._Color = value

    @property
    def Vaccinated(self):
        return self._Vaccinated
    
    @Vaccinated.setter
    def Vaccinated(self, value):
        self._Vaccinated = value

    @property
    def Microchipped(self):
        return self._Microchipped
    
    @Microchipped.setter
    def Microchipped(self, value):
        self._Microchipped = value

    @property
    def Adoption_Fee(self):
        return self._Adoption_Fee
    
    @Adoption_Fee.setter
    def Adoption_Fee(self, value):
        self._Adoption_Fee = value
    
    def get_common_attributes_values(self):
        return {
            "Animal_Id": self._Animal_Id,
            "Name": self._Name,
            "Breed": self._Breed,
            "Age": self._Age,
            "Gender": self._Gender,
            "Color": self._Color,
            "Vaccinated": self._Vaccinated,
            "Microchipped": self._Microchipped,
            "Adoption_Fee(Rs)": self._Adoption_Fee}
    def get_attributes_values(self):    #helps in display functions of files & append/write functions to files
        return self.get_common_attributes_values()
    
    @staticmethod
    def get_new_animal(animaltype):
        with open(f"{animaltype}.csv") as file:
            n = 0
            for _ in file:
                n += 1
        animal_Id = f"{animaltype[0]}{n:03}"
        
        name = input(f"Enter the name of the {animaltype.lower()}: ").strip().capitalize()
        while name == "":
            name = input("Can't be left blank -> Enter the name: ").strip().capitalize()
            
        breed = input(f"Enter the breed of the {animaltype.lower()}: ").strip().capitalize()
        while breed == "":
            breed = input("Can't be left blank -> Enter the breed: ").strip().capitalize()
            
        while True:
            try:
                age = int(input(f"Enter the age of the {animaltype.lower()}: ").strip())
                while age < 1 or age > 15:
                    age = int(input(f"Invalid age -> Enter the age again: ").strip())
                break
            except ValueError:
                print("Invalid input -> Please enter a numeric value.")

        gender = input(f"Enter the gender of the {animaltype.lower()}: ").strip().capitalize()
        while gender not in ["Female", "Male"]:
            gender = input(f"Invalid gender -> Enter the gender again: ").strip().capitalize()
        
        color = input(f"Enter the color of the {animaltype.lower()}: ").strip().capitalize()
        while color == "":
            color = input("Can't be left blank -> Enter the color: ").strip().capitalize()
            
        vaccinated = input(f"Is the {animaltype.lower()} vaccinated [yes/no]: ").strip().capitalize()
        while vaccinated not in ["Yes", "No"]:
            vaccinated = input(f"Enter either yes/no -> Is the {animaltype.lower()} vaccinated [yes/no]: ").strip().capitalize()
            
        microchipped = input(f"Is the {animaltype.lower()} microchipped [yes/no]: ").strip().capitalize()
        while microchipped not in ["Yes", "No"]:
            microchipped = input(f"Enter either yes/no -> Is the {animaltype.lower()} microchipped [yes/no]: ").strip().capitalize()
        
        while True:
            try:
                adoption_Fee = int(input(f"Enter the adoption fee of the {animaltype.lower()}: ").strip())
                while adoption_Fee < 400 or adoption_Fee > 10000:
                    adoption_Fee = int(input(f"Invalid fee -> Enter the fee again: ").strip())
                break
            except ValueError:
                print("Invalid input -> Please enter a numeric value.")
        
        match animaltype:
            case "Dog":
                return Dog(animal_Id, name, breed, age, gender, color, vaccinated, microchipped, adoption_Fee)
            
            case "Cat":
                return Cat(animal_Id, name, breed, age, gender, color, vaccinated, microchipped, adoption_Fee)
            
            case "Rabbit":
                neutered = input(f"Is the {animaltype.lower()} neutered [yes/no]: ").strip().capitalize()
                while neutered not in ["Yes", "No"]:
                    neutered = input(f"Enter either yes/no -> Is the {animaltype.lower()} neutered [yes/no]: ").strip().capitalize()
                return Rabbit(animal_Id, name, breed, age, gender, color, vaccinated, microchipped, adoption_Fee, neutered)
            
            case "Bird":
                tame = input(f"Is the {animaltype.lower()} tame [yes/no]: ").strip().capitalize()
                while tame not in ["Yes", "No"]:
                    tame = input(f"Enter either yes/no -> Is the {animaltype.lower()} tame [yes/no]: ").strip().capitalize()
                return Bird(animal_Id, name, breed, age, gender, color, vaccinated, microchipped, adoption_Fee, tame)
            
    @staticmethod
    def store_new_animal(animaltype):
        new_animal = Animal.get_new_animal(animaltype)
        
        new_animal_data = new_animal.get_attributes_values()
        
        with open(f"{animaltype}.csv", "a", newline="") as file:
            writer = csv.DictWriter(file, fieldnames=new_animal_data.keys())
            writer.writerow(new_animal_data)
        
        print(f"New {animaltype} record added")
        
    @staticmethod
    def get_animals(animalType):
        animals = []
        sorted_animals = []
        
        with open(f"{animalType}.csv") as file:
            reader = csv.DictReader(file)
            for row in reader:
                animals.append(row)
        
        for animal in sorted(animals, key=lambda animal: int(animal["Animal_Id"][1:])):
            if animalType == "Dog":
                sorted_animals.append(Dog(animal["Animal_Id"], animal["Name"], animal["Breed"], int(animal["Age"]), animal["Gender"], animal["Color"], animal["Vaccinated"], animal["Microchipped"], animal["Adoption_Fee(Rs)"]))
            
            elif animalType == "Cat":
                sorted_animals.append(Cat(animal["Animal_Id"], animal["Name"], animal["Breed"], int(animal["Age"]), animal["Gender"], animal["Color"], animal["Vaccinated"], animal["Microchipped"], animal["Adoption_Fee(Rs)"]))
            
            elif animalType == "Rabbit":
                sorted_animals.append(Rabbit(animal["Animal_Id"], animal["Name"], animal["Breed"], int(animal["Age"]), animal["Gender"], animal["Color"], animal["Vaccinated"], animal["Microchipped"], animal["Neutered"], animal["Adoption_Fee(Rs)"]))
            
            elif animalType == "Bird":
                sorted_animals.append(Bird(animal["Animal_Id"], animal["Name"], animal["Breed"], int(animal["Age"]), animal["Gender"], animal["Color"], animal["Vaccinated"], animal["Microchipped"], animal["Tame"], animal["Adoption_Fee(Rs)"]))
        
        return sorted_animals 
    
    @staticmethod
    def store_modify_data(filename, animals):
        with open(f"{filename}.csv") as file:
            reader = csv.reader(file)
            fieldnames = next(reader)
        
        with open(f"{filename}.csv", "w", newline="") as file:
            writer = csv.DictWriter(file, fieldnames)
            writer.writeheader()
            for a in animals:
                writer.writerow(a.get_attributes_values())
    
    @staticmethod
    def valid_field_value(animaltype,field, value):
        l = animaltype[0]
        while True:    
            match field:
                case "Animal_Id":
                    while not re.fullmatch(rf"{l}\d{{3}}",value):
                        value = input("Invalid id -> Enter id again: ").strip().capitalize()
                    return field, value
                
                case "Name":
                    while value == "":
                        value = input("Can't be left blank -> Enter the name: ").strip().capitalize()
                    return field, value
                
                case "Breed":
                    while value == "":
                        value = input("Can't be left blank -> Enter the breed: ").strip().capitalize()
                    return field, value
               
                case "Age":
                    while True:
                        try:
                            value = int(value)
                            while value < 1 or value > 15:
                                value = int(input(f"Invalid age -> Enter the age again: ").strip())
                            break
                        except ValueError:
                            value = input("Invalid input -> Please enter a numeric value: ").strip()
                    return field, value
                
                case "Gender":
                    while value not in ["Female", "Male"]:
                        value = input(f"Invalid gender -> Enter the gender again: ").strip().capitalize()
                    return field, value
                
                case "Color":
                    while value == "":
                        value = input("Can't be left blank -> Enter the color: ").strip().capitalize()
                    return field, value
                
                case "Vaccinated":
                    while value not in ["Yes", "No"]:
                        value = input(f"Enter either yes/no -> Is the {animaltype.lower()} vaccinated [yes/no]: ").strip().capitalize()
                    return field, value
                
                case "Microchipped":
                    while value not in ["Yes", "No"]:
                        value = input(f"Enter either yes/no -> Is the {animaltype.lower()} microchipped [yes/no]: ").strip().capitalize()
                    return field, value
                
                case "Adoption_Fee(Rs)":
                    while True:
                        try:
                            value = int(value)
                            while value < 400 or value > 10000:
                                value = int(input(f"Invalid fee -> Enter the fee again: ").strip())
                            break
                        except ValueError:
                            value = input("Invalid input -> Please enter a numeric value: ").strip()
                    return field, value
                
                case "Neutered":
                    if animaltype != "Rabbit":
                        field = input("Field do not exist -> Enter field: ").strip().title()
                        value = input("Enter value: ").strip().capitalize()
                    else:
                        while value not in ["Yes", "No"]:
                            value = input(f"Enter either yes/no -> Is the {animaltype.lower()} neutered [yes/no]: ").strip().capitalize()
                        return field, value
                
                case "Tame":
                    if animaltype != "Bird":
                        field = input("Field do not exist -> Enter field: ").strip().title()
                        value = input("Enter value: ").strip().capitalize()
                    else:
                        while value not in ["Yes", "No"]:
                            value = input(f"Enter either yes/no -> Is the {animaltype.lower()} tame [yes/no]: ").strip().capitalize()
                        return field, value
                
                case _:
                    field = input("Field do not exist -> Enter field: ").strip().title()
                    value = input("Enter value: ").strip().capitalize()
    
    @staticmethod
    def display_animals(animals): #parameter is a list of objects created by get_animals method
        table = []
        for animal in animals:
            attrs = animal.get_attributes_values()
            table.append(list(attrs.values()))
        
        headers = list(attrs.keys())
        print(tabulate(table, headers, tablefmt="fancy_grid"))
    
    @staticmethod
    def advanced_display(filename, animals): #parameter is a list of objects created by get_animals method
        criteria = input("\nEnter the criteria by which you would like to search: ").strip().title()
        with open(f"{filename}.csv") as file:
            reader = csv.reader(file)
            table_headers = next(reader)
        while criteria not in table_headers:
            criteria = input("Invalid criteria! -> Enter again: ").strip().title()
            
        criteria_value = input("Enter the value of criteria by which you would like to search: ").strip().capitalize()
        
        table = []
        filtered_animals = []
        for animal in animals:
            value = getattr(animal, criteria)  #retrieve the value of instance 'animal' of the attribute 'criteria' dynamically(learn from copilot)
            if value.strip() == criteria_value:
                filtered_animals.append(animal)
                attrs = animal.get_attributes_values()
                table.append(list(attrs.values()))
                
        headers = list(attrs.keys())
        if len(table) == 0:
            print("No animals found!")
            return filtered_animals
        else:
            print(tabulate(table, headers, tablefmt="fancy_grid"))
            return filtered_animals
        
class Dog(Animal):
    def __init__(self, ID, N, B, A, G, C, V, M, Ad):
        super().__init__(ID, N, B, A, G, C, V, M, Ad)
        
class Cat(Animal):
    def __init__(self, ID, N, B, A, G, C, V, M, Ad):
        super().__init__(ID, N, B, A, G, C, V, M, Ad)
    
class Rabbit(Animal):
    def __init__(self, ID, N, B, A, G, C, V, M, Ad, Neutered):
        super().__init__(ID, N, B, A, G, C, V, M, Ad)
        self._Neutered = Neutered
    
    def get_attributes_values(self):
        attrs = self.get_common_attributes_values()
        attrs["Neutered"] = self._Neutered
        return attrs
    
    @property
    def Neutered(self):
        return self._Neutered
    
    @Neutered.setter
    def Neutered(self, value):
        self._Neutered = value
        
class Bird(Animal):
    def __init__(self, ID, N, B, A, G, C, V, M, Ad, Tame):
        super().__init__(ID, N, B, A, G, C, V, M, Ad)
        self._Tame = Tame
    
    def get_attributes_values(self):
        attrs = self.get_common_attributes_values()
        attrs["Tame"] = self._Tame
        return attrs
    
    @property
    def Tame(self):
        return self._Tame
    
    @Tame.setter
    def Tame(self, value):
        self._Tame = value

       
class Adopter:
    def __init__(self, Adoption_Id, Name, Phone, Email, Address, Date, Animal_Type, Animal_Id, Animal_Name, Adoption_Fee, Payment):
        self._Adoption_Id = Adoption_Id
        self._Name = Name
        self._Phone = Phone
        self._Email = Email
        self._Address = Address
        self._Date = Date
        self._Animal_Type = Animal_Type
        self._Animal_Id = Animal_Id
        self._Animal_Name = Animal_Name
        self._Adoption_Fee = Adoption_Fee
        self._Payment = Payment
        
    @property
    def Adoption_Id(self):
        return self._Adoption_Id
    
    @Adoption_Id.setter
    def Adoption_Id(self, value):
        self._Adoption_Id = value

    @property
    def Name(self):
        return self._Name
    
    @Name.setter
    def Name(self, value):
        self._Name = value

    @property
    def Phone(self):
        return self._Phone
    
    @Phone.setter
    def Phone(self, value):
        self._Phone = value

    @property
    def Email(self):
        return self._Email
    
    @Email.setter
    def Email(self, value):
        self._Email = value

    @property
    def Address(self):
        return self._Address
    
    @Address.setter
    def Address(self, value):
        self._Address = value
    
    @property
    def Date(self):
        return self._Date

    @Date.setter
    def Date(self, value):
        self._Date = value

    @property
    def Animal_Type(self):
        return self._Animal_Type

    @Animal_Type.setter
    def Animal_Type(self, value):
        self._Animal_Type = value

    @property
    def Animal_Id(self):
        return self._Animal_Id

    @Animal_Id.setter
    def Animal_Id(self, value):
        self._Animal_Id = value

    @property
    def Animal_Name(self):
        return self._Animal_Name

    @Animal_Name.setter
    def Animal_Name(self, value):
        self._Animal_Name = value

    @property
    def Adoption_Fee(self):
        return self._Adoption_Fee

    @Adoption_Fee.setter
    def Adoption_Fee(self, value):
        self._Adoption_Fee = value

    @property
    def Payment(self):
        return self._Payment

    @Payment.setter
    def Payment(self, value):
        self._Payment = value
    
    def get_attributes_values(self):    #helps in display functions of files & append/write functions to files
        return {
            "Adoption_ID": self.Adoption_Id,
            "Adopter_Name": self.Name,
            "Adopter_Phone": self.Phone,
            "Adopter_Email": self.Email,
            "Adopter_Address": self.Address,
            "Adoption_Date": self.Date,
            "Animal_Type": self.Animal_Type,
            "Animal_Id": self.Animal_Id,
            "Animal_Name": self.Animal_Name,
            "Adoption_Fee(Rs)": self.Adoption_Fee,
            "Payment": self.Payment}
    
    def get_all_adopters():
        adopters = []
        
        with open("Adoption_record.csv") as file:
            reader = csv.DictReader(file)
            for row in reader:
                a = Adopter(row["Adoption_ID"], row["Adopter_Name"], row["Adopter_Phone"], row["Adopter_Email"], row["Adopter_Address"], row["Adoption_Date"], row["Animal_Type"], row["Animal_Id"], row["Animal_Name"], row["Adoption_Fee(Rs)"], row["Payment"])
                adopters.append(a)
        
        return adopters
    
    @staticmethod
    def get_adopter(animaltype, animals):
        with open("Adoption_record.csv") as file:
            n = 0
            for _ in file:
                n += 1
        adoption_id = f"Ad{n:03}" #ensure id is always in format Ad001 by adding leading zeros
        
        name = input("Enter your name: ").strip().capitalize()
        while name == "":
            name = input("Can't be left blank -> Enter your name: ").strip().capitalize()
            
        phone = input("Enter your phone number: ").strip()
        while not re.fullmatch(r"5\d{7}", phone):
            phone = input("Invalid phone number -> Enter your phone number: ").strip()
            
        email = input("Enter your email: ").strip()
        while not re.fullmatch(r"[\w.-]+@\w+\.[a-z]{2,}+",email):
            email = input("Invalid email -> Enter your email: ").strip()
            
        address = input("Enter your address: ").strip()
        while address == "":
            address = input("Can't be left blank -> Enter your address: ").strip()
            
        Date = date.today()
        
        animal_type = animaltype
        
        animal_id = input("Enter the animal id of your future pet: ").strip().capitalize()
        while True:
            found = False
            for a in animals:
                if a.Animal_Id == animal_id:
                    found = True
                    break
            if found:
                break
            else:
                animal_id = input("Invalid animal id -> Enter the animal id of your future pet: ").strip().capitalize()
                
        animal_name = input("Enter the name of your future pet: ").strip().capitalize()
        while True:
            found = False
            for a in animals:
                if a.Name == animal_name and a.Animal_Id == animal_id:
                    found = True
                    break
            if found:
                break
            else:
                animal_name = input("Wrong name -> Enter the name of your future pet: ").strip().capitalize()
                
        for a in animals:
            if a.Name == animal_name and a.Animal_Id == animal_id:   
                adoption_fee = a.Adoption_Fee
                break
        
        payment = "Pending"
        
        return Adopter(adoption_id, name, phone, email, address, Date, animal_type, animal_id, animal_name, adoption_fee, payment)

    @staticmethod
    def adopt_pet(animaltype, animals):
        adopter = Adopter.get_adopter(animaltype, animals)
        
        adoption_data = adopter.get_attributes_values()
        
        with open("Adoption_record.csv", "a", newline="") as file:
            writer = csv.DictWriter(file, fieldnames=adoption_data.keys())
            writer.writerow(adoption_data)
            
        print(f"Adoption_ID: {adopter.Adoption_Id}")
        print(f"Adoption_Fee(Rs): {adopter.Adoption_Fee}")
        
    @staticmethod
    def store_modify_data(adopters):
        with open(f"Adoption_record.csv") as file:
            reader = csv.reader(file)
            fieldnames = next(reader)
        
        with open("Adoption_record.csv", "w", newline="") as file:
            writer = csv.DictWriter(file, fieldnames)
            writer.writeheader()
            for a in adopters:
                writer.writerow(a.get_attributes_values())

    @staticmethod
    def valid_field_value(field, value):
        while True:    
            match field:
                case "Adoption_ID":
                    while not re.fullmatch(r"Ad\d{3}",value.capitalize()):
                        value = input("Invalid id -> Enter id again: ").strip().capitalize()
                    return field, value.capitalize()
                
                case "Adopter_Name":
                    while value == "":
                        value = input("Can't be left blank -> Enter your name: ").strip().capitalize()
                    return field, value.capitalize()
                
                case "Adopter_Phone":
                    while not re.fullmatch(r"5\d{7}", value):
                        value = input("Invalid phone number -> Enter your phone number: ").strip()
                    return field, value
                
                case "Adopter_Email":
                    while not re.fullmatch(r"[\w.-]+@\w+\.[a-z]{2,}+",value):
                        value = input("Invalid email -> Enter your email: ").strip()
                    return field, value
                
                case "Adopter_Address":
                    while value == "":
                        value = input("Can't be left blank -> Enter your address: ").strip()
                    return field, value
                
                case "Adoption_Date":
                    while not re.fullmatch(r"\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])",value):
                        value = input("Invalid date format -> Enter date (yyyy-mm-dd): ").strip()
                    return field, value
                
                case "Animal_Type":
                    while value not in ["dog", "cat", "bird", "rabbit"]:
                        value = input("This animal do not exist in system database -> Enter another animal: ").strip().capitalize()
                    return field, value.capitalize()
                
                case "Animal_Id":
                    while not re.fullmatch(r"[BCDR]\d{3}",value.capitalize()):
                        value = input("Invalid id -> Enter id again: ").strip().capitalize()
                    return field, value.capitalize()
                
                case "Animal_Name":
                    while value == "":
                        value = input("Can't be left blank -> Enter animal name: ").strip().capitalize()
                    return field, value.capitalize()
                
                case "Adoption_Fee(Rs)":
                    while True:
                        try:
                            value = int(value)
                            while value < 400 or value > 10000:
                                value = int(input(f"Invalid fee -> Enter the fee again: ").strip())
                            break
                        except ValueError:
                            value = input("Invalid input -> Please enter a numeric value: ").strip()
                    return field, value
                
                case "Payment":
                    while value not in ["pending", "done"]:
                        value = input("Enter either Pending/Done -> Enter payment status: ").strip().capitalize()
                    return field, value.capitalize()
                
                case _:
                    field = input("Field do not exist -> Enter field: ").strip().title()
                    value = input("Enter value: ").strip()
    
    @staticmethod
    def display_menu():
        print("\nChoose one of the following pets to adopt:\n")
        
        print("1. Cats")
        print("2. Dogs")
        print("3. Birds")
        print("4. Rabbits")
        print("5. Exit")

    @staticmethod
    def sub_adoption_menu(animalType, animals):
        unfiltered_animals = animals
        #Displays adoption menu for the selected pet type
        while True:
            print(f"\n{animalType} Adoption Menu\n")
        
            print("1. Advanced Search")
            print("2. Adoption")
            print("3. Return to Main Menu")
            print("4. Exit")

            choice = input("\nEnter the number corresponding to your choice (1/2/3/4): ")
            while choice not in ["1", "2", "3", "4"]:
                choice = input("Invalid choice.Please try again -> Enter choice: ")

            if choice == "1":
                print(f"\n Performing an Advanced Search for {animalType}s.....")
                #search filter method
                animals = Animal.advanced_display(animalType, animals) #starting from orignal animal list, the list is filtered until empty
                if len(animals) == 0:                                  #empty list is reset to original list
                    animals = unfiltered_animals

            elif choice == "2":
                print(f"\n Proceeding with adoption for a {animalType}....")
                #adopt_pet method
                Adopter.adopt_pet(animalType, animals)
                print("Please note the adoption id and fee then head to our pet shop to finalise adoption and pick up your new pet")
            
            elif choice == "3":
                print("\n Returning to Adopter Menu....")
                Utility.clear_terminal()
                return

            elif choice == "4":
                sys.exit("\n Exiting the system...")

    @staticmethod
    def choose_pet():
        while True:
            Utility.clear_terminal()
            Adopter.display_menu()

            choice = input("\nEnter the number corresponding to your choice: ")
            while choice not in ["1", "2", "3", "4", "5"]:
                choice = input("Invalid choice.Please try again -> Enter choice: ")

            if choice == "1":
                print("\nYou chose Cats")
                cats = Cat.get_animals("Cat")
                Cat.display_animals(cats)
                Adopter.sub_adoption_menu("Cat", cats)
            
            elif choice == "2":
                print("\nYou chose Dogs")
                dogs = Dog.get_animals("Dog")
                Dog.display_animals(dogs)
                Adopter.sub_adoption_menu("Dog", dogs)
             
            elif choice == "3":
                print("\nYou chose Birds")
                birds = Bird.get_animals("Bird")
                Bird.display_animals(birds)
                Adopter.sub_adoption_menu("Bird", birds)

            elif choice == "4":
                print("\nYou chose Rabbits")
                rabbits = Rabbit.get_animals("Rabbit")
                Rabbit.display_animals(rabbits)
                Adopter.sub_adoption_menu("Rabbit", rabbits)
            
            elif choice == "5":
                sys.exit("\nExiting....")
                

#Outside class 
#Initial menu to choose between adminlogin/adoption/exit:
def display_main_menu():
    print("\nMAIN MENU\n")

    print("1. Admin login")
    print("2. Adopt a pet")
    print("3. Exit")

    choice = input("\nPlease choose an option (1/2/3): ")
    while choice not in ["1", "2", "3"]:
        choice = input("Invalid choice.Please try again -> Enter choice: ")

    if choice == "1":
        admin = Admin()
        admin.admin_login()       #calls admin login (admin password + task menu )
        
    elif choice == "2":
        Adopter.choose_pet()      #calls choose pet (customer menu....)      
       
    elif choice == "3":
        sys.exit("\nExiting....")
 
            
def main():
    display_main_menu()
    
if __name__ == "__main__":
    main()