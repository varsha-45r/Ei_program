import java.util.*;
import java.util.logging.Logger;

// --- Entity Classes ---

public class Virtual_Classroom {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        VirtualClassroomManager manager = new VirtualClassroomManager();

        System.out.println("Welcome to Virtual Classroom Manager!");
        System.out.println("Type commands (add_classroom, add_student, schedule_assignment, submit_assignment, list_classrooms, list_students, exit).");

        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine();
            String[] parts = input.split(" ", 3);

            if (parts[0].equalsIgnoreCase("exit")) {
                System.out.println("Exiting Virtual Classroom Manager. Goodbye!");
                break;
            }

            try {
                switch (parts[0].toLowerCase()) {
                    case "add_classroom":
                        manager.addClassroom(parts[1]);
                        break;
                    case "add_student":
                        manager.addStudent(parts[1], parts[2]);
                        break;
                    case "schedule_assignment":
                        manager.scheduleAssignment(parts[1], parts[2]);
                        break;
                    case "submit_assignment":
                        String[] subParts = parts[2].split(" ", 2);
                        manager.submitAssignment(parts[1], subParts[0], subParts[1]);
                        break;
                    case "list_classrooms":
                        manager.listClassrooms();
                        break;
                    case "list_students":
                        manager.listStudents(parts[1]);
                        break;
                    default:
                        System.out.println("Unknown command.");
                }
            } catch (Exception e) {
                System.out.println("Invalid command format. Please try again.");
            }
        }

        scanner.close();
    }
}

class Student {
    private final String studentId;

    public Student(String studentId) {
        this.studentId = studentId;
    }

    public String getId() {
        return studentId;
    }
}

class Assignment {
    private final String details;
    private final Set<String> submissions = new HashSet<>();

    public Assignment(String details) {
        this.details = details;
    }

    public String getDetails() {
        return details;
    }

    public void submit(String studentId) {
        submissions.add(studentId);
    }

    public boolean isSubmitted(String studentId) {
        return submissions.contains(studentId);
    }
}

// --- Manager Class ---

class Classroom {
    private final String name;
    private final Map<String, Student> students = new HashMap<>();
    private final List<Assignment> assignments = new ArrayList<>();

    public Classroom(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addStudent(Student student) {
        students.put(student.getId(), student);
    }

    public Collection<Student> getStudents() {
        return students.values();
    }

    public void scheduleAssignment(String details) {
        assignments.add(new Assignment(details));
    }

    public List<Assignment> getAssignments() {
        return assignments;
    }
}

// --- Main App ---

class VirtualClassroomManager {
    private static final Logger logger = Logger.getLogger(VirtualClassroomManager.class.getName());
    private final Map<String, Classroom> classrooms = new HashMap<>();

    public void addClassroom(String name) {
        if (classrooms.containsKey(name)) {
            logger.warning("Classroom already exists: " + name);
            return;
        }
        classrooms.put(name, new Classroom(name));
        System.out.println("Classroom " + name + " has been created.");
    }

    public void addStudent(String studentId, String className) {
        Classroom classroom = classrooms.get(className);
        if (classroom == null) {
            logger.warning("Classroom not found: " + className);
            return;
        }
        Student student = new Student(studentId);
        classroom.addStudent(student);
        System.out.println("Student " + studentId + " has been enrolled in " + className + ".");
    }

    public void scheduleAssignment(String className, String details) {
        Classroom classroom = classrooms.get(className);
        if (classroom == null) {
            logger.warning("Classroom not found: " + className);
            return;
        }
        classroom.scheduleAssignment(details);
        System.out.println("Assignment for " + className + " has been scheduled.");
    }

    public void submitAssignment(String studentId, String className, String details) {
        Classroom classroom = classrooms.get(className);
        if (classroom == null) {
            logger.warning("Classroom not found: " + className);
            return;
        }

        for (Assignment assignment : classroom.getAssignments()) {
            if (assignment.getDetails().equals(details)) {
                if (classroom.getStudents().stream().noneMatch(s -> s.getId().equals(studentId))) {
                    logger.warning("Student not enrolled: " + studentId);
                    return;
                }
                assignment.submit(studentId);
                System.out.println("Assignment submitted by Student " + studentId + " in " + className + ".");
                return;
            }
        }
        logger.warning("Assignment not found: " + details);
    }

    public void listClassrooms() {
        if (classrooms.isEmpty()) {
            System.out.println("No classrooms available.");
            return;
        }
        classrooms.keySet().forEach(System.out::println);
    }

    public void listStudents(String className) {
        Classroom classroom = classrooms.get(className);
        if (classroom == null) {
            logger.warning("Classroom not found: " + className);
            return;
        }
        classroom.getStudents().forEach(s -> System.out.println(s.getId()));
    }
}