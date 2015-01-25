import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Parallel Quicksort implementation
 * 
 * @author Faniel Ghirmay ffg92 and Jose
 *
 */
public class PSort implements Runnable {
	public static ExecutorService threadPool = Executors.newCachedThreadPool();
	private static int[] a;
	int array[];
	int low;
	int high;

	PSort(int a[], int begin, int end) {
		array = a;
		low = begin;
		high = end;
	}

	public static void main(String[] args) {
		a = getArray();
		printArray(a);
		parallelSort(a, 0, a.length - 1);

		System.out.println("");

		printArray(a);
		System.out.println();

	}

	/**
	 * Performs parallel Quicksort algorithm 
	 * @param A
	 * @param begin
	 * @param end
	 */
	public static void parallelSort(int A[], int begin, int end) {
		if (begin >= end)
			return;

		int pivot = A[end];
		int partition = partition(A, begin, end, pivot);

		PSort p1 = new PSort(A, begin, partition - 1);
		PSort p2 = new PSort(A, partition + 1, end);
		
		Thread t1 = new Thread(p1);
		Thread t2 = new Thread(p2);
		
		t1.start();
		t2.start();
		
		try {
			t1.join();
			t2.join();
		} catch (Exception e) {

		}
	}

	/**
	 * computes and returns the index where the array is divided into sub problems
	 * @param A
	 * @param left
	 * @param right
	 * @param pivot
	 * @return
	 */
	private static int partition(int A[], int left, int right, int pivot) {
		int leftCursor = left - 1;
		int rightCursor = right;
		while (leftCursor < rightCursor) {
			while (A[++leftCursor] < pivot)
			;
				
			while (rightCursor > 0 && A[--rightCursor] > pivot)
				;
			if (leftCursor >= rightCursor) {
				break;
			} else {
				swap(A, leftCursor, rightCursor);
			}
		}
		swap(A, leftCursor, right);
		return leftCursor;
	}

	/**
	 * 
	 * @param A
	 * @param left
	 * @param right
	 */
	public static void swap(int A[], int left, int right) {
		int temp = A[left];
		A[left] = A[right];
		A[right] = temp;
	}

	/**
	 * 
	 * @param A
	 */
	public static void printArray(int A[]) {
		for (int i : A) {
			System.out.print(i + " ");
		}
	}

	/**
	 * Random array generator
	 * @return
	 */
	public static int[] getArray() {
		int size = 10;
		int[] array = new int[size];
		int item = 0;
		for (int i = 0; i < size; i++) {
			item = (int) (Math.random() * 100);
			array[i] = item;
		}
		return array;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		parallelSort(array, low, high);

	}
}










