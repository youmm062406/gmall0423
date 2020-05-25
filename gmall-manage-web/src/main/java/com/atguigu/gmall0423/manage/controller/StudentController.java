package com.atguigu.gmall0423.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall0423.bean.Student;
import com.atguigu.gmall0423.service.StudentService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class StudentController {

    @Reference
    private StudentService studentService;
    @RequestMapping("findAll")
    public List<Student> findAll(){
      return studentService.findAll();
    }
}
