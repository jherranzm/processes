package telefonica.aaee.scheduler.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import telefonica.aaee.scheduler.tasks.HelloWorldTasklet;
import telefonica.aaee.scheduler.tasks.SimpleListTasklet;
import telefonica.aaee.util.UtilNombre;

@Configuration
@Import(StandaloneInfrastructureConfiguration.class)
public class FirstJobConfiguration {
	
	@Autowired
    private JobBuilderFactory jobBuilders;
     
    @Autowired
    private StepBuilderFactory stepBuilders;
     
    @Bean
    public Job helloWorldJob(){
        return jobBuilders.get(UtilNombre.nombreAleatorio())
                .start(step00())
                .next(step01())
                .next(step02())
                .next(step03())
                .build();
    }
     
    @Bean
    public Step step00(){
        return stepBuilders.get("paso00")
                .tasklet(tasklet())
                .build();
    }
     
    @Bean
    public Step step01(){
        return stepBuilders.get("paso01")
                .tasklet(stasklet())
                .build();
    }

    @Bean
    public Step step02(){
        return stepBuilders.get("paso02")
                .tasklet(stasklet())
                .build();
    }

    @Bean
    public Step step03(){
        return stepBuilders.get("paso03")
                .tasklet(tasklet1000())
                .build();
    }

    @Bean
    public Tasklet tasklet() {
        return new HelloWorldTasklet();
    }

    @Bean
    public Tasklet stasklet() {
        return new SimpleListTasklet();
    }

    @Bean
    public Tasklet tasklet1000() {
        return new SimpleListTasklet(1000);
    }

}
