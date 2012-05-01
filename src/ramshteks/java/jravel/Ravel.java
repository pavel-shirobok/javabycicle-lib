package ramshteks.java.jravel;

import ramshteks.java.jravel.core.RavelCore;

public interface Ravel {

	/**
	 * Add new task in pull
	 * @param task
	 */
	void push(RavelCore.RavelTask task);

	void stop();

	int task_count();


	//troubles проблемы следующие:
	/*
	надо чтобы по таймату потоки убивались, а на их место ставился новый
	Может даже хуй забить и поюзать вот эту штуку:
    http://gee.cs.oswego.edu/dl/classes/EDU/oswego/cs/dl/util/concurrent/PooledExecutor.html
	 */


	/**
	 * Check for existing results
	 * @return <code>true</code> if results existing
	 */
	boolean hasResult();

	/**
	 * Return and remove first result in pull
	 * @return
	 */
	RavelCore.RavelWorkResult nextResult();
}
