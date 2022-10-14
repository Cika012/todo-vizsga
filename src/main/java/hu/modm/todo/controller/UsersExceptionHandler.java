//package hu.modm.todo.controller;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.validation.FieldError;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.zalando.problem.Problem;
//import org.zalando.problem.Status;
//import org.zalando.problem.spring.web.advice.ProblemHandling;
//import org.zalando.problem.violations.Violation;
//
//import java.net.URI;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@ControllerAdvice
//public class UsersExceptionHandler implements ProblemHandling {
//
//    @ExceptionHandler({IllegalArgumentException.class})
//    public ResponseEntity<Problem> handleNotFound(IllegalArgumentException  e) {
//        Problem problem = Problem.builder()
//                .withType(URI.create("users/user-not-found"))
//                .withTitle("Not found")
//                .withStatus(Status.NOT_FOUND)
//                .withDetail(e.getMessage())
//                .build();
//
//        return ResponseEntity
//                .status(HttpStatus.NOT_FOUND)
//                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
//                .body(problem);
//    }
//
//    @ExceptionHandler({MethodArgumentNotValidException.class})
//    public ResponseEntity<Problem> handleValidationError(MethodArgumentNotValidException e) {
//        List<Violation> violations = e.getBindingResult().getFieldErrors().stream()
//                .map((FieldError fe) -> new Violation(fe.getField(), fe.getDefaultMessage()))
//                .collect(Collectors.toList());
//
//        Problem problem = Problem.builder()
//                .withType(URI.create("users/validation-error"))
//                .withTitle("Validation error")
//                .withStatus(Status.BAD_REQUEST)
//                .withDetail(e.getMessage())
//                .with("violations", violations)
//                .build();
//
//        return ResponseEntity
//                .status(HttpStatus.BAD_REQUEST)
//                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
//                .body(problem);
//    }
//}
