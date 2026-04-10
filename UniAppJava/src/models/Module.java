package models;

public class Module {
  private int id;
  private String name;
  private int courseId;
  private int lecturerId;
  private String schedule;
  private int credits;
  private String lecturerName; // for display purposes

  public Module() {}

  public Module(int id, String name, int courseId, int lecturerId, String schedule, int credits) {
    this.id = id;
    this.name = name;
    this.courseId = courseId;
    this.lecturerId = lecturerId;
    this.schedule = schedule;
    this.credits = credits;
  }

  public int getId() { return id; }
  public void setId(int id) { this.id = id; }
    
  public String getName() { return name; }
  public void setName(String name) { this.name = name; }
    
  public int getCourseId() { return courseId; }
  public void setCourseId(int courseId) { this.courseId = courseId; }
    
  public int getLecturerId() { return lecturerId; }
  public void setLecturerId(int lecturerId) { this.lecturerId = lecturerId; }
    
  public String getSchedule() { return schedule; }
  public void setSchedule(String schedule) { this.schedule = schedule; }

  public int getCredits() { return credits; }
  public void setCredits(int credits) { this.credits = credits; }
    
  public String getLecturerName() { return lecturerName; }
  public void setLecturerName(String lecturerName) { this.lecturerName = lecturerName; }

  @Override
  public String toString() {
      return name;
  }

}
