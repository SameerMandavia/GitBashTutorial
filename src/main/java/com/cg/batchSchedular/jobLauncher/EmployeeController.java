package com.cg.batchSchedular.jobLauncher;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableScheduling
@RestController
@RequestMapping("/schedule")
public class EmployeeController {

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	private Job job;

	@GetMapping
	@Scheduled(cron = "0 */1 * * * *",zone = "IST")//Schedule as run app on every 1 minute.	
	public BatchStatus schedule() throws JobExecutionAlreadyRunningException, JobRestartException,
		JobInstanceAlreadyCompleteException, JobParametersInvalidException {
		
		Map<String, JobParameter> addTime = new HashMap<>();
		addTime.put("Time", new JobParameter(System.currentTimeMillis()));
		JobParameters jobParameters = new JobParameters(addTime);
		JobExecution jobExecution = jobLauncher.run(job, jobParameters);
		log.info("Schedule Status" + jobExecution);
		return jobExecution.getStatus();

	}
}
