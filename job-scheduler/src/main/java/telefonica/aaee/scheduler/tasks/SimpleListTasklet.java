package telefonica.aaee.scheduler.tasks;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class SimpleListTasklet implements Tasklet {
	
	private int iteraciones = 10;
	
	public SimpleListTasklet(){
		
	}

	public SimpleListTasklet(int i){
		iteraciones = i;
	}

	@Override
	public RepeatStatus execute(StepContribution arg0, ChunkContext arg1)
			throws Exception {
		
		System.out.println("SimpleListTasklet!");
		
		for(int i = 0; i<iteraciones; i++){
			System.out.println("Tarea :" + i);
		}
		
		return RepeatStatus.FINISHED;
	}

}
