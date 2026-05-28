package solid.single_responsibility.solution;

import solid.single_responsibility.Student;

public class StudentService {
    private final StudentValidator validator;
    private final StudentFileWriter writer;
    private final StudentRepository repository;
    private final NotificationService notifier;

    StudentService(StudentValidator validator,
                   StudentFileWriter writer,
                   StudentRepository repository,
                   NotificationService notifier) {
        this.validator = validator;
        this.writer = writer;
        this.repository = repository;
        this.notifier = notifier;
    }

    void addStudent(Student s) {
        validator.validate(s);
        repository.save(s);
        writer.write(s);
        notifier.notify(s);
    }
}