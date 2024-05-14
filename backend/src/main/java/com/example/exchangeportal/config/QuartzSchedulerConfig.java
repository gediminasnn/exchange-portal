package com.example.exchangeportal.config;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.listeners.JobChainingJobListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.example.exchangeportal.scheduling.CurrencyFetchJob;
import com.example.exchangeportal.scheduling.ExchangeRateFetchJob;

import jakarta.annotation.PostConstruct;

@Configuration
public class QuartzSchedulerConfig {

	private static final Logger logger = LoggerFactory.getLogger(QuartzSchedulerConfig.class);

	private static final String FETCH_RATES_JOB_NAME = "fetchRatesJob";
	private static final String FETCH_RATES_TRIGGER_NAME = "fetchRatesTrigger";
	private static final String FETCH_RATES_TRIGGER_CRON_EXPRESSION = "0 2 0 * * ?";
	private static final String FETCH_RATES_IMMEDIATE_JOB_NAME = "immediateFetchRatesJob";
	private static final String FETCH_CURRENCIES_IMMEDIATE_JOB_NAME = "immediateFetchCurrenciesJob";
	private static final String IMMEDIATE_JOBS_GROUP = "ImmediateJobsGroup";
	private static final String IMMEDIATE_JOBS_CHAIN_LISTENER = "ImmediateJobsChainListener";

	@Autowired
	private SchedulerFactoryBean schedulerFactoryBean;

	private Scheduler scheduler;

	@PostConstruct
	public void init() {
		try {
			initScheduler();
			scheduleFetchRatesJob();
			scheduleImmediateJobs();
			startScheduler();
		} catch (SchedulerException e) {
			logger.error("Failed to start scheduler", e);
		}
	}

	private void initScheduler() throws SchedulerException {
		scheduler = schedulerFactoryBean.getScheduler();
	}

	private void scheduleFetchRatesJob() throws SchedulerException {
		JobDetail job = JobBuilder.newJob(ExchangeRateFetchJob.class)
				.withIdentity(FETCH_RATES_JOB_NAME)
				.build();

		Trigger trigger = TriggerBuilder.newTrigger()
				.withIdentity(FETCH_RATES_TRIGGER_NAME)
				.withSchedule(CronScheduleBuilder.cronSchedule(FETCH_RATES_TRIGGER_CRON_EXPRESSION))
				.build();

		scheduler.scheduleJob(job, trigger);
		logger.info("Scheduled {} with cron expression: {}", FETCH_RATES_JOB_NAME, FETCH_RATES_TRIGGER_CRON_EXPRESSION);
	}

	private void scheduleImmediateJobs() throws SchedulerException {
		JobDetail immediateFetchCurrenciesJob = JobBuilder.newJob(CurrencyFetchJob.class)
				.withIdentity(FETCH_CURRENCIES_IMMEDIATE_JOB_NAME, IMMEDIATE_JOBS_GROUP)
				.build();
		Trigger immediateFetchCurrenciesTrigger = TriggerBuilder.newTrigger()
				.withIdentity(FETCH_CURRENCIES_IMMEDIATE_JOB_NAME, IMMEDIATE_JOBS_GROUP)
				.startNow()
				.build();
		scheduler.scheduleJob(immediateFetchCurrenciesJob, immediateFetchCurrenciesTrigger);

		JobDetail immediateFetchRatesJob = JobBuilder.newJob(ExchangeRateFetchJob.class)
				.withIdentity(FETCH_RATES_IMMEDIATE_JOB_NAME)
				.storeDurably()
				.build();
		scheduler.addJob(immediateFetchRatesJob, true);

		JobChainingJobListener jobChainingJobListener = new JobChainingJobListener(IMMEDIATE_JOBS_CHAIN_LISTENER);
		jobChainingJobListener.addJobChainLink(
				immediateFetchCurrenciesJob.getKey(),
				immediateFetchRatesJob.getKey());

		scheduler.getListenerManager().addJobListener(jobChainingJobListener);
		logger.info("Scheduled {} and {} to run immediately one after another",
				FETCH_RATES_IMMEDIATE_JOB_NAME,
				FETCH_CURRENCIES_IMMEDIATE_JOB_NAME);
	}

	private void startScheduler() throws SchedulerException {
		scheduler.start();
	}
}
