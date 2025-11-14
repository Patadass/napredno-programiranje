import java.util.*;

class Triple<E extends Number>{
    E a,b,c;

    public Triple(E a,E b,E c){
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public double max(){
        if(a.doubleValue() < b.doubleValue()){
            if(b.doubleValue() < c.doubleValue()){
                return c.doubleValue();
            }else{
                return b.doubleValue();
            }
        }else{
            if(a.doubleValue() < c.doubleValue()){
                return c.doubleValue();
            }else{
                return a.doubleValue();
            }
        }
    }

    public double avarage(){
        return (a.doubleValue() + b.doubleValue() + c.doubleValue()) / 3;
    }
    
    public void sort(){
        ArrayList<E> ph = new ArrayList<E>();
        if(a.doubleValue() < b.doubleValue()){
            if(a.doubleValue() < c.doubleValue()){
                ph.add(a);
                if(b.doubleValue() < c.doubleValue()){
                    ph.add(b);
                    ph.add(c);
                }else{
                    ph.add(c);
                    ph.add(b);
                }
            }else{
                ph.add(c);
                ph.add(a);
                ph.add(b);
            }
        }else{
            if(b.doubleValue() < c.doubleValue()){
                ph.add(b);
                if(a.doubleValue() < c.doubleValue()){
                    ph.add(a);
                    ph.add(c);
                }else{
                    ph.add(c);
                    ph.add(a);
                }
            }else{
                ph.add(c);
                ph.add(b);
                ph.add(a);
            }
        }
        a = ph.get(0);
        b = ph.get(1);
        c = ph.get(2);
    }

    @Override
    public String toString(){
        return String.format("%.2f %.2f %.2f", a.doubleValue(), b.doubleValue(), c.doubleValue());
    }
}

public class TripleTest {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		int a = scanner.nextInt();
		int b = scanner.nextInt();
		int c = scanner.nextInt();
		Triple<Integer> tInt = new Triple<Integer>(a, b, c);
		System.out.printf("%.2f\n", tInt.max());
		System.out.printf("%.2f\n", tInt.avarage());
		tInt.sort();
		System.out.println(tInt);
		float fa = scanner.nextFloat();
		float fb = scanner.nextFloat();
		float fc = scanner.nextFloat();
		Triple<Float> tFloat = new Triple<Float>(fa, fb, fc);
		System.out.printf("%.2f\n", tFloat.max());
		System.out.printf("%.2f\n", tFloat.avarage());
		tFloat.sort();
		System.out.println(tFloat);
		double da = scanner.nextDouble();
		double db = scanner.nextDouble();
		double dc = scanner.nextDouble();
		Triple<Double> tDouble = new Triple<Double>(da, db, dc);
		System.out.printf("%.2f\n", tDouble.max());
		System.out.printf("%.2f\n", tDouble.avarage());
		tDouble.sort();
		System.out.println(tDouble);
	}
}
