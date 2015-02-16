/**
 * Implementation of a fair read and write monitor
 * 
 * @author Faniel ghirmay and jose garcia
 *
 */

public class FairReadWriteLock {
	private int numOfReaders;
	private int numOfWriters;
	private int writersWaiting;
	final ReadWriteLockLogger logger = new ReadWriteLockLogger();
	
	
	FairReadWriteLock() {
		numOfReaders = 0;
		numOfWriters = 0;
		writersWaiting = 0;
	}

	/**
	 * @throws InterruptedException 
	 * 
	 */
	public synchronized void beginRead() throws InterruptedException {
		
		logger.logTryToRead(); 
		while (numOfWriters == 1 || writersWaiting > 0){
			this.wait();
		}
		numOfReaders++;
		logger.logBeginRead(); 
	}

	public synchronized void endRead() {
		
		numOfReaders--;
		notifyAll();
		logger.logEndRead();
	}

	/**
	 * @throws InterruptedException 
	 * 
	 */
	public synchronized void beginWrite() throws InterruptedException {
		
		logger.logTryToWrite();
		writersWaiting++;
		while (numOfReaders > 0){
			this.wait();
		}
		numOfWriters++;
		writersWaiting--;
		logger.logBeginWrite();
	}

	public synchronized void endWrite() throws InterruptedException {
		
		numOfWriters--;
		notifyAll();
		logger.logEndWrite();
	}
	
	public static void main(String args[]){
		FairReadWriteLock doc  = new FairReadWriteLock();
		Thread w[] = new Thread[10];
		Thread r[] = new Thread[10];
		for(int i=0; i < 10; i+=1){
			Writer wr = new Writer(i, i+"@writer", doc);
			Reader rr = new Reader(i, i+"@reader", doc);
			w[i] = new Thread(wr);
			r[i] = new Thread(rr);
			w[i].start();
			r[i].start();
		}
	}
}


class Writer implements Runnable{
	int x;
	String name;
	FairReadWriteLock file;
	Writer(int x, String n, FairReadWriteLock f){
		this.x = x;
		name = n;
		file = f;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			file.beginWrite();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} finally {
		try {
			file.endWrite();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		}
	}
}
class Reader implements Runnable{
	int x;
	String name;
	FairReadWriteLock file;
	Reader(int x, String n, FairReadWriteLock f){
		this.x = x;
		name = n;
		file = f;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			file.beginRead();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} finally {
		file.endRead();
		}
	}
}


