package com.cg.batchSchedular.config;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.RowMapper;

import com.cg.batchSchedular.entity.Employee;
import com.cg.batchSchedular.processor.EmployeeProcessor;

@Configuration
@EnableBatchProcessing
public class SpringBatchConfig {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	private DataSource dataSource;
	
	private Resource outputResource = new FileSystemResource("src/main/resources/output/output.csv");
	
	@Bean
	JdbcCursorItemReader<Employee> reader(){
		JdbcCursorItemReader<Employee> reader = new JdbcCursorItemReader<Employee>();
		reader.setDataSource(dataSource);
		reader.setSql("SELECT * FROM EMPLOYEE");
		reader.setRowMapper(new EmployeeRowMapper());
		
		return reader;
		
	}
	
	public class EmployeeRowMapper implements RowMapper<Employee>{

		@Override
		public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
			Employee employee = new Employee();
			employee.setEmpId(rs.getInt("EMPID"));
			employee.setEmpName(rs.getString("EMPNAME"));
			employee.setAge(rs.getInt("AGE"));
			employee.setDept(rs.getString("DEPT"));
			employee.setSalary(rs.getInt("SALARY"));
			return employee;
		}
		
	}
	
	@Bean
	public EmployeeProcessor employeeProcessor() {
		return new EmployeeProcessor();
	}
	
	@Bean
	public FlatFileItemWriter<Employee> writer() {
		FlatFileItemWriter<Employee> writer = new FlatFileItemWriter<>();
		writer.setResource(outputResource);
		writer.setLineAggregator(new DelimitedLineAggregator<Employee>() {
			{
				setDelimiter(",");
				setFieldExtractor(new BeanWrapperFieldExtractor<Employee>() {
					{
						setNames(new String[] { "empId", "empName", "age", "dept", "salary" });
					}
				});
			}
		});

		return writer;
	}
	
	
	@Bean
	public Step step() {
		return stepBuilderFactory.get("step")
				.<Employee,Employee> chunk(10)
				.reader(reader())
				.processor(employeeProcessor())
				.writer(writer())
				.build();
	}
	
	@Bean
	public Job exportEmployeeJob() {
		return jobBuilderFactory.get("ExportEmployeeData")
				.incrementer(new RunIdIncrementer())
				.start(step())
				.build();
	}

}
