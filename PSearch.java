import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class PSearch implements Callable {
	public static int index = -1;
	int numToFind;
	int lowIndex;
	int highIndex;
	int array[];

	PSearch() {

	}

	PSearch(int x, int A[], int lo, int high) {
		numToFind = x;
		lowIndex = lo;
		highIndex = high;
		array = A;
	}

	/**
	 * 
	 * @param x
	 * @param A
	 * @param numOfThreads
	 * @return
	 */
	public static int parallelSearch(int x, int A[], int numOfThreads) {
		StopWatch timer = new StopWatch();
		if(numOfThreads > A.length){
			numOfThreads = A.length;
		}
		timer.start();
		ExecutorService threadPool = Executors.newFixedThreadPool(numOfThreads);

		Future F[] = scheduleTasks(x, A, numOfThreads, threadPool);

		int index = joinTasks(F, numOfThreads, threadPool);
		threadPool.shutdown(); // free the remaining threads
		timer.stop();
		long mytime = timer.getElapsedTime();
			System.out.println("Time elapsed in nanoseconds is: " + mytime);
			System.out.println("Time elapsed in seconds is: " + mytime
					/ timer.NANOS_PER_SEC);
		return index;
	}

	/**
	 * Searches through the assigned section of the array for the item
	 */

	@Override
	public Object call() throws Exception {
		// TODO Auto-generated method stub
		for (int i = lowIndex; i < highIndex; i += 1) {
			if (array[i] == numToFind) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Schedule tasks for parallel searching
	 * 
	 * @param x
	 * @param A
	 * @param numOfThreads
	 */
	public static Future[] scheduleTasks(int x, int A[], int numOfThreads,
			ExecutorService threadPool) {
		int begin = 0;
		int end = 0;
		int interval = (A.length / numOfThreads);
		Future F[] = new Future[numOfThreads];
		for (int i = 0; i < numOfThreads; i += 1) {
			begin = end;
			end = begin + interval;
			if (end >= A.length) {
				end = A.length - 1;
			} else if (end < A.length && (A.length - end < 4)) {
				end = A.length - 1;
			}
			F[i] = threadPool.submit(new PSearch(x, A, begin, end));
			end += 1;
		}
		return F;
	}
	/**
	 * waits to join the tasks to return the index found
	 * @param F
	 * @param numOfThreads
	 * @param threadPool
	 * @return
	 */
	public static int joinTasks(Future F[], int numOfThreads,
			ExecutorService threadPool) {
		for (int i = 0; i < numOfThreads; i += 1) {
			try {
				int index = (int) F[i].get();
				if (index != -1) {
					return index;
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return -1;
	}
}
