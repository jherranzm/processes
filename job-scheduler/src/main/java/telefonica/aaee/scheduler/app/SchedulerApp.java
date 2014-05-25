package telefonica.aaee.scheduler.app;

import org.springframework.batch.core.Job;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import telefonica.aaee.scheduler.BatchProcessor;
import telefonica.aaee.scheduler.config.BatchProcessorConfig;

public class SchedulerApp {

	public static void main(String[] args) {
		@SuppressWarnings("resource")
		ApplicationContext ctx = new AnnotationConfigApplicationContext(
				BatchProcessorConfig.class);
		
		Job job = (Job)ctx.getBean("helloWorldJob");
		
		System.out.println(job.getName());
		
		BatchProcessor batchProcessStarter = (BatchProcessor)ctx.getBean("batchProcessor");
		
		batchProcessStarter.setJob(job);
        batchProcessStarter.start();
	}

}
