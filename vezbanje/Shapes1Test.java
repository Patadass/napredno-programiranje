import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;
import java.util.ArrayList;

class Window implements Comparable<Window>{
    public String id;
    public ArrayList<Integer> sizes;

    public Window(){
    }

    public Window(String id, ArrayList<Integer> sizes){
        this.id = id;
        this.sizes = sizes;
    }

    public int sum(){
        int sum = 0;
        for(Integer i : sizes){
            sum += i * 4;
        }
        return sum;
    }

    public int compareTo(Window w){
        return Integer.compare(this.sum(), w.sum());
    }
}

class ShapesApplication{
    private ArrayList<Window> wins;

    public ShapesApplication(){
        wins = new ArrayList<Window>();
    }

    public int readCanvases(InputStream inputStream){
        int c = 0;
        Scanner sc = new Scanner(inputStream);
        String line;
        while(sc.hasNext()){
            line = sc.nextLine();
            String[] in = line.split(" ");
            String id = in[0];
            ArrayList<Integer> sizes = new ArrayList<Integer>();
            for(int i = 1;i < in.length;i++){
                sizes.add(Integer.parseInt(in[i]));
                c++;
            }
            wins.add(new Window(id, sizes));

        }
        sc.close();
        return c;
    }

    public void printLargestCanvasTo(OutputStream os){
        Window maxw = null;
        for(Window w : wins){
            if(maxw == null){
                maxw = w;
                continue;
            }
            if(maxw.compareTo(w) == -1){
                maxw = w;
            }
        }
        String out = maxw.id + " " + maxw.sizes.size() + " " + maxw.sum();
        try{
            os.write(out.getBytes());
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
    }
}

public class Shapes1Test {
    public static void main(String[] args) {
        ShapesApplication shapesApplication = new ShapesApplication();

        System.out.println("===READING SQUARES FROM INPUT STREAM===");
        System.out.println(shapesApplication.readCanvases(System.in));
        System.out.println("===PRINTING LARGEST CANVAS TO OUTPUT STREAM===");
        shapesApplication.printLargestCanvasTo(System.out);

    }
}
