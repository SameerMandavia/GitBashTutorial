package com.cg.batchSchedular.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "EMPLOYEE")
public class Employee {

	@Id
	@Column(name = "EMPID")
	private int empId;

	@Column(name = "EMPNAME")
	private String empName;

	@Column(name = "AGE")
	private int age;

	@Column(name = "DEPT")
	private String dept;

	@Column(name = "SALARY")
	private int salary;

}
