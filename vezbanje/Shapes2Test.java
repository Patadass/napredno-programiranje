import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.math.*;
import java.util.*;

abstract class Shape{
    public String type;
    public double size;

    public Shape(){
    }

    public Shape(double size, String type){
        this.size = size;
        this.type = type;
    }

    public abstract double area();
}

class Square extends Shape{

    public Square(double size){
        super(size, "S");
    }

    public double area(){
        return size*size;
    }
}

class Circle extends Shape{
    public Circle(double size){
        super(size, "C");
    }

    public double area(){
        return Math.PI * size * size;
    }
}

class Window{
    public String id;
    public ArrayList<Shape> shapes;
    
    public Window(String id, ArrayList<Shape> shapes){
        this.id = id;
        this.shapes = shapes;
    }

    public int totalCircles(){
        int c = 0;
        for(Shape s: shapes){
            if(s.type.equals("C")){
                c++;
            }
        }
        return c;
    }

    public int totalSqures(){
        return shapes.size() - totalCircles();
    }

    public double maxArea(){
        Shape max = null;
        for(Shape s : shapes){
            if(max == null){
                max = s;
                continue;
            }
            if(max.area() < s.area()){
                max = s;
            }
        }
        return max.area();
    }

    public double minArea(){
        Shape max = null;
        for(Shape s : shapes){
            if(max == null){
                max = s;
                continue;
            }
            if(max.area() > s.area()){
                max = s;
            }
        }
        return max.area();
    }

    public double avgArea(){
        return this.sum() / shapes.size();
    }

    public double sum(){
        double sum = 0;
        for(Shape s : shapes){
            sum += s.area();
        }
        return sum;
    }
}

class SortByArea implements Comparator<Window>{
    public int compare(Window a, Window b){
        return Double.compare(b.sum(), a.sum());
    }
}

class ShapesApplication{
    public double maxArea;
    public ArrayList<Window> windows;

    public ShapesApplication(double maxArea){
        this.maxArea = maxArea;
        this.windows = new ArrayList<Window>();
    }

    public void readCanvases(InputStream is){
        Scanner sc = new Scanner(is);
        while(sc.hasNext()){
            String[] in = sc.nextLine().split(" ");
            String id = in[0];
            ArrayList<Shape> shapes = new ArrayList<Shape>();
            try{
                for(int i = 1;i < in.length;i++){
                    String type = in[i++];
                    double size = Double.valueOf(in[i]);
                    Shape s = null;
                    if(type.equals("C")){
                        s = new Circle(size);
                    }
                    if(type.equals("S")){
                        s = new Square(size);
                    }
                    if(s.area() > maxArea){
                        throw new IrregularCanvasException("Canvas " + id + " has a shape with area larger than " + String.format("%.2f", maxArea));
                    }else{
                        shapes.add(s);
                    }
                }
                windows.add(new Window(id, shapes));
            }catch(IrregularCanvasException e){
                System.out.println(e.getMessage());
            } 
        }
        sc.close();
    }

    public void printCanvases(OutputStream os){
        windows.sort(new SortByArea());
        for(Window w : windows){
            String line = w.id + " " + w.shapes.size() + " " + w.totalCircles() + " " + w.totalSqures() + " " + String.format("%.2f", w.minArea()) + " " + String.format("%.2f", w.maxArea()) + " " + String.format("%.2f", w.avgArea()) + "\n";
            try{
                os.write(line.getBytes());
            }catch(IOException e){
                System.out.println(e.getMessage());
            }
        }
    }
}

class IrregularCanvasException extends Exception{
    public IrregularCanvasException(String msg){
        super(msg);
    }
}

public class Shapes2Test {

    public static void main(String[] args) {

        ShapesApplication shapesApplication = new ShapesApplication(10000);

        System.out.println("===READING CANVASES AND SHAPES FROM INPUT STREAM===");
        shapesApplication.readCanvases(System.in);

        System.out.println("===PRINTING SORTED CANVASES TO OUTPUT STREAM===");
        shapesApplication.printCanvases(System.out);


    }
}
