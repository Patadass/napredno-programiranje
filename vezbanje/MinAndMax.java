import java.util.Scanner;

class MinMax<E extends Comparable<E>>{
    private E min, max;
    private int br_obraboteni, br_isti_min, br_isti_max;

    public MinMax(){
        br_isti_min = 0;
        br_isti_max = 0;
        br_obraboteni = 0;
    }

    public void update(E element){
        if(min == null || min.compareTo(element) >= 1){
            if(min != null && min.equals(max)){
                br_isti_max += br_isti_min;
            }
            min = element;
            br_isti_min = 0;
        }
        if(max == null || max.compareTo(element) <= -1){
            if(max != null && max.equals(min)){
                br_isti_min += br_isti_max;
            }
            max = element;
            br_isti_max = 0;
        }
        if(min.compareTo(element) == 0){
            br_isti_min++;
        }else if(max.compareTo(element) == 0){
            br_isti_max++;
        }
        br_obraboteni++;
    }

    public E max(){
        return max;
    }
    public E min(){
        return min;
    }

    public int calc(){
        return br_obraboteni - (br_isti_max + br_isti_min);
    }

    @Override
    public String toString(){
        return min + " " + max + " " + calc() + '\n';
    }
}

public class MinAndMax {
	public static void main(String[] args) throws ClassNotFoundException {
		Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        MinMax<String> strings = new MinMax<String>();
        for(int i = 0; i < n; ++i) {
            String s = scanner.next();
            strings.update(s);
        }
		System.out.println(strings);
		MinMax<Integer> ints = new MinMax<Integer>();
        for(int i = 0; i < n; ++i) {
           	int x = scanner.nextInt();
            ints.update(x);
        }
        System.out.println(ints);
	}
}
