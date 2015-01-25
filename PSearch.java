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
			if (end > A.length) {
				end = A.length;
			} else if (end < A.length && ((end + interval) > A.length)) {
				end = A.length;
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

	/*
	 * Test array generator
	 */
	public static int[] getArray() {
		int size = 100;
		int[] array = new int[size];
		int item = 0;
		for (int i = 0; i < size; i++) {
			item = (int) (Math.random() * 100);
			array[i] = item;
		}
		return array;
	}

	/*
	 * 
	 */
	public static void main(String args[]) {
		int A[] = { 41, 90, 26, 56, 26, 260, 15, 2, 65, 29, 84, 93, 45, 19, 41,
				13, 91, 93, 15, 3, 73, 2, 64, 72, 22, 44, 80, 9, 48, 44, 53,
				38, 46, 89, 36, 33, 13, 91, 80, 39, 92, 72, 19, 28, 89, 2, 63,
				14, 50, 16, 45, 80, 52, 41, 47, 40, 1, 78, 57, 30, 13, 73, 71,
				35, 90, 93, 75, 36, 76, 23, 37, 62, 66, 84, 64, 17, 77, 12, 56,
				0, 92, 54, 41, 44, 52, 87, 52, 55, 66, 82, 71, 63, 65, 81, 44,
				7, 44, 99, 26, 82, 6, 1500 };
		int x = 1500;
		PSearch s = new PSearch();
		for (int i : A) {
			System.out.print(i + ", ");
		}
		System.out.println();
		System.out.println("The index of " + x + " is "
				+ s.parallelSearch(x, A, 50));
	}

}
