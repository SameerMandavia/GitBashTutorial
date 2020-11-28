package com.cg.batchSchedular.processor;

import org.springframework.batch.item.ItemProcessor;

import com.cg.batchSchedular.entity.Employee;

public class EmployeeProcessor implements ItemProcessor<Employee, Employee>{

	@Override
	public Employee process(Employee employee) throws Exception {
		// TODO Auto-generated method stub
		return employee;
	}

}
