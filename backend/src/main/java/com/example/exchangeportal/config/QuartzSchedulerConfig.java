package com.example.exchangeportal.config;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import com.example.exchangeportal.scheduling.ExchangeRateFetchJob;

import jakarta.annotation.PostConstruct;

@Configuration
public class QuartzSchedulerConfig {

	private static final Logger logger = LoggerFactory.getLogger(QuartzSchedulerConfig.class);

	public static final String FETCH_RATES_JOB_NAME = "fetchRatesJob";
	public static final String FETCH_RATES_CRON_EXPRESSION = "0 1 0 * * ?";
	public static final String FETCH_RATES_TRIGGER_NAME = "fetchRatesTrigger";
	public static final String IMMEDIATE_FETCH_RATES_JOB_NAME = "immediateFetchRatesJob";
	public static final String IMMEDIATE_FETCH_RATES_TRIGGER_NAME = "immediateFetchRatesTrigger";

	@Autowired
	private SchedulerFactoryBean schedulerFactoryBean;

	private Scheduler scheduler;

	@PostConstruct
	public void init() {
		try {
			initScheduler();
			scheduleCronFetchRatesJob();
			scheduleImmediateFetchRatesJob();
			startScheduler();
		} catch (SchedulerException e) {
			logger.error("Failed to start scheduler", e);
		}
	}

	private void initScheduler() {
		scheduler = schedulerFactoryBean.getScheduler();
	}

	private void scheduleCronFetchRatesJob() throws SchedulerException {
		JobDetail job = JobBuilder.newJob(ExchangeRateFetchJob.class)
				.withIdentity(FETCH_RATES_JOB_NAME)
				.build();

		Trigger trigger = TriggerBuilder.newTrigger()
				.withIdentity(FETCH_RATES_TRIGGER_NAME)
				.withSchedule(CronScheduleBuilder.cronSchedule(FETCH_RATES_CRON_EXPRESSION))
				.build();

		scheduler.scheduleJob(job, trigger);
		logger.info("Scheduled {} with cron {}", FETCH_RATES_JOB_NAME, FETCH_RATES_CRON_EXPRESSION);
	}

	private void scheduleImmediateFetchRatesJob() throws SchedulerException {
		JobDetail job = JobBuilder.newJob(ExchangeRateFetchJob.class)
				.withIdentity(IMMEDIATE_FETCH_RATES_JOB_NAME)
				.build();

		Trigger trigger = TriggerBuilder.newTrigger()
				.withIdentity(IMMEDIATE_FETCH_RATES_TRIGGER_NAME, "immediateGroup")
				.startNow()
				.build();

		scheduler.scheduleJob(job, trigger);
		logger.info("Scheduled {} to run immediately", IMMEDIATE_FETCH_RATES_JOB_NAME);
	}

	private void startScheduler() throws SchedulerException {
		scheduler.start();
	}
}
