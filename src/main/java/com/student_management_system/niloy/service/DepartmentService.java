package com.student_management_system.niloy.service;

import com.student_management_system.niloy.model.Department;
import com.student_management_system.niloy.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Transactional
    public Department createDepartment(Department department) {
        if (departmentRepository.existsByName(department.getName())) {
            throw new RuntimeException("Department name already exists");
        }
        return departmentRepository.save(department);
    }

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public Optional<Department> getDepartmentById(Long id) {
        return departmentRepository.findById(id);
    }

    public Optional<Department> getDepartmentByName(String name) {
        return departmentRepository.findByName(name);
    }

    @Transactional
    public Department updateDepartment(Long id, Department departmentDetails) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found"));

        department.setName(departmentDetails.getName());
        department.setDescription(departmentDetails.getDescription());

        return departmentRepository.save(department);
    }

    @Transactional
    public void deleteDepartment(Long id) {
        departmentRepository.deleteById(id);
    }
}
