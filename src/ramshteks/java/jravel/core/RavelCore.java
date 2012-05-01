package ramshteks.java.jravel.core;

public class RavelCore {
	public static enum RavelPriority{
		LOW(0), MEDIUM(1), HIGH(2);

		RavelPriority(int v){
			priority_value = v;
		}

		private int priority_value;
		public int getPriority(){
			return priority_value;
		}
	}

	public static interface RavelTask{
		void run();
		RavelPriority priority();
	}

	public static interface RavelWorkResult{
		RavelTask task();
		Exception exception();
	}

	public static class BaseRavelWorkResult implements RavelWorkResult {
		private RavelTask task;
		private Exception exception;

		public BaseRavelWorkResult(RavelTask task, Exception exception){
			this.task = task;
			this.exception = exception;
		}


		public RavelTask task() {
			return task;
		}

		public Exception exception() {
			return exception;
		}

	}

}
