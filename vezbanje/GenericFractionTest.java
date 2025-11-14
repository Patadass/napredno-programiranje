import java.util.Scanner;

class GenericFraction<T extends Number, U extends Number>{
    protected T numerator;
    protected U denominator;

    public GenericFraction(T n, U d) throws ZeroDenominatorException{
        if(d.equals(0)){
            throw new ZeroDenominatorException();
        }
        this.numerator = n;
        this.denominator = d;
    }

    public GenericFraction<Double, Double> add(GenericFraction<? extends Number, ? extends Number> gf){
        try{
            GenericFraction<Double, Double> a = new GenericFraction<Double, Double>(this.numerator.doubleValue(), this.denominator.doubleValue());
            GenericFraction<Double, Double> b = new GenericFraction<Double, Double>(gf.numerator.doubleValue(), gf.denominator.doubleValue());
            a.denominator *= b.denominator;
            a.numerator *= b.denominator;
            b.numerator *= this.denominator.doubleValue();
            a.numerator += b.numerator;
            double divby = idk(a.denominator, a.numerator);
            a.denominator /= divby;
            a.numerator /= divby;
            return a;
        }catch(ZeroDenominatorException e){
        }
        return null;
    }

    public double toDouble(){
        return numerator.doubleValue() / denominator.doubleValue();
    }

    @Override
    public String toString(){
        return String.format("%.2f / %.2f", numerator.doubleValue(), denominator.doubleValue());
    }

    public double idk(double a, double b){
        for(int i = (int)Math.min(a, b);i >= 2;i--){
            if((int)a % i == 0 && (int)b % i == 0){
                return i;
            }
        }
        return 1;
    }
}

class ZeroDenominatorException extends Exception{
    public ZeroDenominatorException(){
        super("Denominator cannot be zero");
    }
}

public class GenericFractionTest {
    public static void main(String[] args) {
    	Scanner scanner = new Scanner(System.in);
        double n1 = scanner.nextDouble();
        double d1 = scanner.nextDouble();
        float n2 = scanner.nextFloat();
        float d2 = scanner.nextFloat();
        int n3 = scanner.nextInt();
        int d3 = scanner.nextInt();
        try {
        	GenericFraction<Double, Double> gfDouble = new GenericFraction<Double, Double>(n1, d1);
        	GenericFraction<Float, Float> gfFloat = new GenericFraction<Float, Float>(n2, d2);
        	GenericFraction<Integer, Integer> gfInt = new GenericFraction<Integer, Integer>(n3, d3);
            System.out.printf("%.2f\n", gfDouble.toDouble());
            System.out.println(gfDouble.add(gfFloat));
            System.out.println(gfInt.add(gfFloat));
            System.out.println(gfDouble.add(gfInt));
            gfInt = new GenericFraction<Integer, Integer>(n3, 0);
        } catch(ZeroDenominatorException e) {
            System.out.println(e.getMessage());
        }
        
        scanner.close();
    }

}
