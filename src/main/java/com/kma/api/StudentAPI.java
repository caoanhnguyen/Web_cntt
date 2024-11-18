package com.kma.api;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kma.models.StudentDTO;


@RestController
public class StudentAPI {
	
	
	@GetMapping(value = "/api/student")
	public List<StudentDTO> getAllStudent(@RequestParam(value="name", required = false) String name,
							  			  @RequestParam(value="age", required = false) Integer age) {
		
		List<StudentDTO> dto = new ArrayList<>();
		
		StudentDTO std = new StudentDTO();
		std.setId("CT060102");
		std.setName(name);
		std.setAge(age);
		std.setHomeTown("Ha Noi");
		
		dto.add(std);
		
		StudentDTO std2 = new StudentDTO();
		std2.setId("CT060103");
		std2.setName("Bao Ngoc");
		std2.setAge(21);
		std2.setHomeTown("Thanh Hoa");
		
		dto.add(std2);
		
		return dto;
	}
}
