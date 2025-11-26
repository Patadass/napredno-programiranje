import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

class Student{
    String id;
    ArrayList<Integer> grades;

    public Student(String id, List<Integer> grades){
        this.id = id;
        this.grades = (ArrayList<Integer>) grades;
    }

    public void addGrade(Integer Grade){
        grades.add(Grade);
    }

    public double avg(){
        double sum = 0;
        for(Integer grade : grades){
            sum += grade;
        }
        return sum / grades.size();
    }

    @Override
    public String toString(){
        return String.format("Student{id='%s', grades=%s}", id, grades.toString());
    }
}

class SortByAvg implements Comparator<Student>{
    @Override
    public int compare(Student a, Student b){
        if(Double.compare(b.avg(), a.avg()) == 0){
            if(Integer.compare(b.grades.size(), a.grades.size()) == 0){
                return b.id.compareTo(a.id);
            }else{
                return Integer.compare(b.grades.size(), a.grades.size());
            }
        }else{
            return Double.compare(b.avg(), a.avg());
        }
    }
}

class SortByPassed implements Comparator<Student>{
    @Override
    public int compare(Student a, Student b){
        if(Integer.compare(a.grades.size(), b.grades.size()) == 0){
            if(Double.compare(b.avg(), a.avg()) == 0){
                return a.id.compareTo(b.id);
            }else{
                return Double.compare(b.avg(), a.avg());
            }
        }else{
            return Integer.compare(b.grades.size(), a.grades.size());
        }
    }
}

class Faculty{
    HashMap<String, Student> students;

    public Faculty(){
        students = new HashMap<String, Student>();
    }

    public void addStudent(String id, List<Integer> grades){
        try{

            Student s = new Student(id, grades);
            if(students.containsKey(id)){
                throw new SameIdException(id);
            }
            students.put(id, s);
        }catch(SameIdException e){
            System.out.println(e.getMessage());
        }
    }

    public void addGrade(String id, Integer grade){
        if(!students.containsKey(id)){
            return;
        }
        students.get(id).addGrade(grade);
    }

    public Set<Student> getStudentsSortedByAverageGrade(){
        Set<Student> ph = new TreeSet<Student>(new SortByAvg());
        ph.addAll(students.values());
        return ph;
    }

    public Set<Student> getStudentsSortedByCoursesPassed(){
        Set <Student> ph = new TreeSet<Student>(new SortByPassed());
        ph.addAll(students.values());
        return ph;
    }
}

class SameIdException extends Exception{
    public SameIdException(String id){
        super(String.format("Student with ID %s already exists", id));
    }
}

public class SetsTest {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Faculty faculty = new Faculty();

        while (true) {
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("exit")) {
                break;
            }

            String[] tokens = input.split("\\s+");
            String command = tokens[0];

            switch (command) {
                case "addStudent":
                    String id = tokens[1];
                    List<Integer> grades = new ArrayList<>();
                    for (int i = 2; i < tokens.length; i++) {
                        grades.add(Integer.parseInt(tokens[i]));
                    }
                    try {
                        faculty.addStudent(id, grades);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case "addGrade":
                    String studentId = tokens[1];
                    int grade = Integer.parseInt(tokens[2]);
                    faculty.addGrade(studentId, grade);
                    break;

                case "getStudentsSortedByAverageGrade":
                    System.out.println("Sorting students by average grade");
                    Set<Student> sortedByAverage = faculty.getStudentsSortedByAverageGrade();
                    for (Student student : sortedByAverage) {
                        System.out.println(student);
                    }
                    break;

                case "getStudentsSortedByCoursesPassed":
                    System.out.println("Sorting students by courses passed");
                    Set<Student> sortedByCourses = faculty.getStudentsSortedByCoursesPassed();
                    for (Student student : sortedByCourses) {
                        System.out.println(student);
                    }
                    break;

                default:
                    break;
            }
        }

        scanner.close();
    }
}

