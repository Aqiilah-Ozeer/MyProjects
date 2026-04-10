package results;

public class Result {
    private int resultId;
    private int studentId;
    private String studentName;
    private int moduleId;
    private String moduleName;
    private int marks;
    private String grade;

    public Result(int resultId, int studentId, String studentName,
                  int moduleId, String moduleName, int marks, String grade) {
        this.resultId = resultId;
        this.studentId = studentId;
        this.studentName = studentName;
        this.moduleId = moduleId;
        this.moduleName = moduleName;
        this.marks = marks;
        this.grade = grade;
    }

    public int getResultId() { return resultId; }
    public int getStudentId() { return studentId; }
    public String getStudentName() { return studentName; }
    public int getModuleId() { return moduleId; }
    public String getModuleName() { return moduleName; }
    public int getMarks() { return marks; }
    public String getGrade() { return grade; }

    public void setMarks(int marks) { this.marks = marks; }
    public void setGrade(String grade) { this.grade = grade; }
}