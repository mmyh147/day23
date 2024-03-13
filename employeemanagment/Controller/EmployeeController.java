package com.example.employeemanagment.Controller;


import com.example.employeemanagment.Model.Employee;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("api/v1/employee")
public class EmployeeController {
    ArrayList<Employee> employees = new ArrayList<>();

    @PostMapping("post")
    public ResponseEntity addEmployee(@RequestBody @Valid Employee employee, Errors errors) {
        if (errors.hasErrors()) {
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);

        }
        employees.add(employee);
        return ResponseEntity.status(200).body("Employee added ");

    }


    @RequestMapping("get")
    public ArrayList<Employee> getEmployee() {


        return employees;
    }

    @PutMapping("update/{ID}")
    public ResponseEntity updateEmployee(@PathVariable String ID, @RequestBody @Valid Employee employee, Errors errors) {
        if (errors.hasErrors()) {
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);

        }
        for (Employee e : employees) {
            if (e.getId().equals(ID)) {
                employees.set(employees.indexOf(e), employee);
                return ResponseEntity.status(200).body("user updated");
            }
        }
        return ResponseEntity.status(200).body("user not found");

    }


    @DeleteMapping("delete/{ID}")
    public ResponseEntity deleteEmployee(@PathVariable String ID) {
        for (Employee employee : employees) {
            if (employee.getId().equals(ID)) {
                employees.remove(employees.indexOf(employee));
                return ResponseEntity.status(200).body("employee deleted");
            }
        }
        return ResponseEntity.status(200).body("employee not found");

    }

    @GetMapping("position/{position}")
    public ResponseEntity getEmployeesByPosition(@PathVariable String position) {

        if (!(position.equals("supervisor") || position.equals("coordinator"))){

            return ResponseEntity.status(400).body("Invalid Role");
        }

        ArrayList<Employee> employeeList = new ArrayList<>();
        for (Employee employee : employees){
            if(employee.getPosition().equals(position)){
                employeeList.add(employee);
    }
}
        return ResponseEntity.status(200).body(employeeList);
    }


    @GetMapping("byage/{minAge}/{maxAge}")
    public ResponseEntity  getEmployeesByAgeRange(@PathVariable int minAge, @PathVariable int maxAge) {
        ArrayList<Employee> employeeList = new ArrayList<>();

        if (minAge < 0 || maxAge < 0){ return ResponseEntity.status(400).body("Min Or Max can not be less than 0");}


            if (minAge > maxAge) {
            return ResponseEntity.status(400).body("Min con not be greater than Max");
        }

        for (Employee employee : employees) {
            if (employee.getAge() >= minAge && employee.getAge() <= maxAge) {
                employeeList.add(employee);
            }
        }

        return ResponseEntity.status(200).body(employeeList);
    }

    @PostMapping("/leave/{ID}")
    public ResponseEntity<?> applyForAnnualLeave(@PathVariable String ID) {
        Employee employee = employees.stream().filter(e -> e.getId().equals(ID))
                .findFirst()
                .orElse(null);
        if (employee == null){
            return ResponseEntity.status(400).body("No employee found");
        }

        if (employee.getOnLeave()) {
            return ResponseEntity.status(400).body("Employee is already on leave");
        }

        if (employee.getAnnualLeave() <= 0) {
            return ResponseEntity.status(400).body("Employee has no annual leave remaining.");
        }

        employee.setOnLeave(true);
        employee.setAnnualLeave(employee.getAnnualLeave() - 1);

        employees.set(employees.indexOf(employee), employee);

        return ResponseEntity.ok("Annual leave application successfully");
    }


    @GetMapping("/noleave")
    public ResponseEntity getEmployeesWithNoAnnualLeave() {
        ArrayList<Employee> noAnnualLeave = new ArrayList<>();


        for (Employee employee : employees) {
            if (employee.getAnnualLeave() <= 0) {
                noAnnualLeave.add(employee);
            }
        }

        return ResponseEntity.status(400).body(noAnnualLeave);
    }


    @PutMapping("/promote/{supervisorId}/{coordinatorId}")
    public ResponseEntity promoteEmployee(@PathVariable String supervisorId, @PathVariable String coordinatorId) {

        Employee employee = employees.stream().filter(e -> e.getId().equals(coordinatorId))
                .findFirst()
                .orElse(null);
        Employee supervisor = employees.stream().filter(e -> e.getId().equals(supervisorId))
                .findFirst()
                .orElse(null);

        if (supervisor == null || employee == null) {
            return ResponseEntity.status(400).body("Not found");
        }

        if (!supervisor.getPosition().equals("supervisor")) {
            return ResponseEntity.status(400).body("Only supervisors are allowed to promote employees.");
        }

        if (employee.getAge() == null || employee.getAge() < 30) {
            return ResponseEntity.status(400).body("Coordinator must be at least 30 years old for promotion.");
        }

        if (employee.getOnLeave()) {
            return ResponseEntity.status(400).body("Coordinator on leave and can not be promoted.");
        }

        employee.setPosition("supervisor");

        employees.set(employees.indexOf(employee), employee);

        return ResponseEntity.ok("Coordinator promoted successfully.");
    }


}
