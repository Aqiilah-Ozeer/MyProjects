package models;

import java.util.ArrayList;
import java.util.List;

public class Course {

  private int id;
  private String name; 
  private String description;
  private int credits;
  private List<Module> modules;// List of modules belonging to this course

  //Constructors
  public Course(){
    this.modules = new ArrayList<>();
  }

  public Course(int id, String name, String description, int credits) {
      this.id = id;
      this.name = name;
      this.description = description;
      this.credits = credits;
      this.modules = new ArrayList<>();
  }

  public Course(String name, String description, int credits) {
      this.name = name;
      this.description = description;
      this.credits = credits;
      this.modules = new ArrayList<>();
  }

  // Getters and Setters
  public int getId(){
    return id;
  }

  public void setId(int id){
    this.id = id;
  }

  public String getName(){
    return name;
  }

  public void setName(String name) {
      this.name = name;
  }
    
  public String getDescription() {
      return description;
  }
    
  public void setDescription(String description) {
      this.description = description;
  }

  public int getCredits() {
      return credits;
  }
    
  public void setCredits(int credits) {
      this.credits = credits;
  }
    
  public List<Module> getModules() {
      return modules;
  }

  public void setModules(List<Module> modules) {
      this.modules = modules;
  }
    
  public void addModule(Module module) {
      this.modules.add(module);
  }
    
  public void removeModule(Module module) {
      this.modules.remove(module);
  }

  @Override
  public String toString() {
      return name + " (" + id + ")";
  }
  
}
