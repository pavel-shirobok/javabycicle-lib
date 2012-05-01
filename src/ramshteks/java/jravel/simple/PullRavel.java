package ramshteks.java.jravel.simple;

import ramshteks.java.jravel.Ravel;
import ramshteks.java.jravel.core.RavelCore;

import java.util.*;

public class PullRavel implements Ravel, Comparator<RavelCore.RavelTask>{

	private List<RavelCore.RavelTask> tasks = new LinkedList<RavelCore.RavelTask>();

	private List<RavelCore.RavelWorkResult> results =new LinkedList<RavelCore.RavelWorkResult>();

	private List<RavelWorker> workers = new LinkedList<RavelWorker>();

	public PullRavel(int threadsCount){
		RavelWorker worker;
		for(int i =0; i<threadsCount; i++){
			worker = new RavelWorker(tasks, results);
			workers.add(worker);
			worker.setPriority(Thread.MIN_PRIORITY);
			worker.start();
		}
	}

	public void push(RavelCore.RavelTask task) {
		synchronized (tasks){
			tasks.add(task);
			//Collections.sort(tasks, this);
		}
	}

	public void stop() {
		RavelWorker worker;

		for(int i =0, len = workers.size(); i<len; i++){
			worker = workers.get(i);
			worker.stop();
		}
	}

	public int task_count() {
		int res = 0;
		synchronized (tasks){
			res = tasks.size();
		}
		return res;
	}

	public boolean busy() {
		return false;
	}

	public boolean hasResult() {
		boolean res = false;
		synchronized (results){
			if(results.size()!=0)res = true;
		}

		return res;
	}

	public RavelCore.RavelWorkResult nextResult() {
		RavelCore.RavelWorkResult result = null;

		synchronized (results){
			if(results.size()!=0)result = results.remove(0);

		}
		return result;
	}


	public int compare(RavelCore.RavelTask o1, RavelCore.RavelTask o2) {
		if(o1.priority().getPriority() == o2.priority().getPriority())return 0;
		if(o1.priority().getPriority() < o2.priority().getPriority())return 1;
		return -1;
	}

	private class RavelWorker extends Thread{

			private List<RavelCore.RavelTask> tasks;
			private List<RavelCore.RavelWorkResult> results;

			public RavelWorker(List<RavelCore.RavelTask> tasks, List<RavelCore.RavelWorkResult> results){
				this.tasks = tasks;
				this.results = results;
			}

			public void run() {
				RavelCore.RavelTask task;

				while (true){
					int size = 0;
					synchronized (tasks){
						size = tasks.size();
					}

					if(size==0){
						try{
							Thread.sleep(1);
						}catch (Exception te){}
						continue;
					}

					synchronized (tasks){
                        if(tasks.size()==0)continue;
						task = tasks.remove(0);
					}

					Exception exception = null;
					try{
						task.run();
					}catch (Exception e){
						exception = e;
					}

					synchronized (results){
						results.add(new RavelCore.BaseRavelWorkResult(task, exception));
					}
				}
			}
		}
}
