import java.util.Scanner;
import java.util.Arrays;
import java.util.LinkedList;

class ResizableArray<T>{
    protected T [] array;
    protected int pos;

    @SuppressWarnings("unchecked")
    public ResizableArray(){
        array = (T[]) new Object[50];
        pos = 0;
    }

    @SuppressWarnings("unchecked")
    public void addElement(T o){
        if(pos >= array.length){
            T[] ph = (T[]) new Object[array.length + 50];
            for(int i = 0;i < array.length;i++){
                ph[i] = array[i];
            }
            array = ph;
        }
        array[pos] = o;
        pos++;
    }

    public boolean removeElement(T o){
        boolean is = false;
        for(int i = 0;i < this.count();i++){
            if(!is && array[i].equals(o)){
                is = true;
                continue;
            }
            if(is){
                array[i - 1] = array[i];
            }
        }
        if(is){
            pos--;
        }
        return is;
    }

    public boolean contains(T o){
        for(int i = 0;i < this.count();i++){
            if(this.elementAt(i).equals(o)){
                return true;
            }
        }
        return false;
    }

    public Object[] toArray(){
        return array;
    }

    public boolean isEmpty(){
        return pos == 0;
    }

    public int count(){
        return pos;
    }

    public T elementAt(int idx){
        try{
            if(idx > pos || idx < 0){
                throw new ArrayIndexOutOfBoundsException();
            }
            return array[idx];
        }catch(ArrayIndexOutOfBoundsException e){
            e.getMessage();
        }
        return null;
    }

    static public <T> void copyAll(ResizableArray<? super T> dest, ResizableArray<? extends T> src){
        T[] ph = Arrays.copyOf(src.array, src.array.length);
        int s = src.count();
        for(int i = 0;i < s;i++){
            dest.addElement(ph[i]);
        }
    }
}

class IntegerArray extends ResizableArray<Integer>{
    public double sum(){
        double s = 0;
        for(int i = 0;i < this.count();i++){
            s += this.elementAt(i);
        }
        return s;
    }

    public double mean(){
        return this.sum() / this.count();
    }

    public int countNonZero(){
        int c = 0;
        for(int i = 0;i < this.count();i++){
            if(this.elementAt(i) != 0){
                c++;
            }
        }
        return c;
    }

    public IntegerArray distinct(){
        IntegerArray distinctArray = new IntegerArray();
        for(int i = 0;i < this.count();i++){
            if(!distinctArray.contains(this.elementAt(i))){
                distinctArray.addElement(this.elementAt(i));
            }
        }
        return distinctArray;
    }

    public IntegerArray increment(int offset){
        IntegerArray incremented = new IntegerArray();
        for(int i = 0;i < this.count();i++){
            incremented.addElement(this.elementAt(i) + offset);
        }
        return incremented;
    }
}

public class ResizableArrayTest {
	
	public static void main(String[] args) {
		Scanner jin = new Scanner(System.in);
		int test = jin.nextInt();
		if ( test == 0 ) { //test ResizableArray on ints
			ResizableArray<Integer> a = new ResizableArray<Integer>();
			System.out.println(a.count());
			int first = jin.nextInt();
			a.addElement(first);
			System.out.println(a.count());
			int last = first;
			while ( jin.hasNextInt() ) {
				last = jin.nextInt();
				a.addElement(last);
			}
			System.out.println(a.count());
			System.out.println(a.contains(first));
			System.out.println(a.contains(last));
			System.out.println(a.removeElement(first));
			System.out.println(a.contains(first));
			System.out.println(a.count());
		}
		if ( test == 1 ) { //test ResizableArray on strings
			ResizableArray<String> a = new ResizableArray<String>();
			System.out.println(a.count());
			String first = jin.next();
			a.addElement(first);
			System.out.println(a.count());
			String last = first;
			for ( int i = 0 ; i < 4 ; ++i ) {
				last = jin.next();
				a.addElement(last);
			}
			System.out.println(a.count());
			System.out.println(a.contains(first));
			System.out.println(a.contains(last));
			System.out.println(a.removeElement(first));
			System.out.println(a.contains(first));
			System.out.println(a.count());
			ResizableArray<String> b = new ResizableArray<String>();
			ResizableArray.copyAll(b, a);
			System.out.println(b.count());
			System.out.println(a.count());
			System.out.println(a.contains(first));
			System.out.println(a.contains(last));
			System.out.println(b.contains(first));
			System.out.println(b.contains(last));
			ResizableArray.copyAll(b, a);
			System.out.println(b.count());
			System.out.println(a.count());
			System.out.println(a.contains(first));
			System.out.println(a.contains(last));
			System.out.println(b.contains(first));
			System.out.println(b.contains(last));
			System.out.println(b.removeElement(first));
			System.out.println(b.contains(first));
			System.out.println(b.removeElement(first));
			System.out.println(b.contains(first));

			System.out.println(a.removeElement(first));
			ResizableArray.copyAll(b, a);
			System.out.println(b.count());
			System.out.println(a.count());
			System.out.println(a.contains(first));
			System.out.println(a.contains(last));
			System.out.println(b.contains(first));
			System.out.println(b.contains(last));
		}
		if ( test == 2 ) { //test IntegerArray
			IntegerArray a = new IntegerArray();
			System.out.println(a.isEmpty());
			while ( jin.hasNextInt() ) {
				a.addElement(jin.nextInt());
			}
			jin.next();
			System.out.println(a.sum());
			System.out.println(a.mean());
			System.out.println(a.countNonZero());
			System.out.println(a.count());
			IntegerArray b = a.distinct();
			System.out.println(b.sum());
			IntegerArray c = a.increment(5);
			System.out.println(c.sum());
			if ( a.sum() > 100 )
				ResizableArray.copyAll(a, a);
			else
				ResizableArray.copyAll(a, b);
			System.out.println(a.sum());
			System.out.println(a.removeElement(jin.nextInt()));
			System.out.println(a.sum());
			System.out.println(a.removeElement(jin.nextInt()));
			System.out.println(a.sum());
			System.out.println(a.removeElement(jin.nextInt()));
			System.out.println(a.sum());
			System.out.println(a.contains(jin.nextInt()));
			System.out.println(a.contains(jin.nextInt()));
		}
		if ( test == 3 ) { //test insanely large arrays
			LinkedList<ResizableArray<Integer>> resizable_arrays = new LinkedList<ResizableArray<Integer>>();
			for ( int w = 0 ; w < 500 ; ++w ) {
				ResizableArray<Integer> a = new ResizableArray<Integer>();
				int k =  2000;
				int t =  1000;
				for ( int i = 0 ; i < k ; ++i ) {
					a.addElement(i);
				}
				
				a.removeElement(0);
				for ( int i = 0 ; i < t ; ++i ) {
					a.removeElement(k-i-1);
				}
				resizable_arrays.add(a);
			}
			System.out.println("You implementation finished in less then 3 seconds, well done!");
		}
	}
	
}
