package lecturers;

public class Lecturer {
    private String lecturerId;
    private String firstName;
    private String lastName;
    private String title;
    private String email;
    private String phone;
    private String department;
    private String specialisation;
    private String qualification;
    private String employmentType;

    public Lecturer(String lecturerId, String firstName, String lastName, String title,
            String email, String phone, String department, String specialisation,
            String qualification, String employmentType) {
        this.lecturerId = lecturerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.title = title;
        this.email = email;
        this.phone = phone;
        this.department = department;
        this.specialisation = specialisation;
        this.qualification = qualification;
        this.employmentType = employmentType;
    }

    // Getters
    public String getLecturerId() {
        return lecturerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getTitle() {
        return title;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getDepartment() {
        return department;
    }

    public String getSpecialisation() {
        return specialisation;
    }

    public String getQualification() {
        return qualification;
    }

    public String getEmploymentType() {
        return employmentType;
    }

    // Setters
    public void setLecturerId(String lecturerId) {
        this.lecturerId = lecturerId;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setSpecialisation(String specialisation) {
        this.specialisation = specialisation;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public void setEmploymentType(String employmentType) {
        this.employmentType = employmentType;
    }
}
