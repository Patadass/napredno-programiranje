import java.util.ArrayList;
import java.util.Scanner;


enum Color {
	RED, GREEN, BLUE
}

interface Scalable{
    public void scale(float scaleFactor);
}

interface Stackable{
    public float weight();
}

class Canvas {
    ArrayList<Shape> shapes;

    public Canvas(){
        shapes = new ArrayList<Shape>();
    }

    public void add(String id, Color color, float radius){
        this.add(new Circle(id, color, radius));
    }

    public void add(String id, Color color, float width, float height){
        this.add(new Rectangle(id, color, width, height));
    }

    private void add(Shape s){
        if(shapes.isEmpty()){
            shapes.add(s);
            return;
        }
        for(int i = 0;i < shapes.size();i++){
            if(shapes.get(i).weight() < s.weight()){
                if(i - 1 < 0){
                    shapes.addFirst(s);
                    return;
                }
                shapes.add(i, s);
                return;
            }
        }
        shapes.addLast(s);
    }

    public void scale(String id, float scaleFactor){
        Shape s = null;
        int pos = 0;
        for(int i = 0;i < shapes.size();i++){
            if(shapes.get(i).getId() == id){
                s = shapes.get(i);
                pos = i;
                break;
            }
        }
        if(s == null){
            return;
        }
        s.scale(scaleFactor);
        if(pos - 1 < 0){
            return;
        }
        if(shapes.get(pos - 1).weight() >= s.weight()){
            return;
        }
        shapes.remove(s);
        for(int i = 1;i < shapes.size();i++){
            if(shapes.get(i).weight() < s.weight()){
                shapes.add(i, s);
            }
        }
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder("");
        for(Shape s: shapes){
            sb.append(s.toString() + "\n");
        }
        return sb.toString();
    }
}

class Shape implements Scalable, Stackable{
    String id;
    Color color;

    public Shape(String id, Color color){
        this.id = id;
        this.color = color;  
    }

    public String getId(){
        return id;
    }

    public void scale(float scaleFactor){
        return;
    }
    public float weight(){
        return 0;
    }
}

class Circle extends Shape{
    float radius;

    public Circle(String id, Color color, float radius){
        super(id, color);
        this.radius = radius;
    }

    @Override
    public void scale(float scaleFactor){
        this.radius *= scaleFactor;
    }

    @Override
    public float weight(){
        return (float)Math.PI * radius * radius;
    }

    @Override
    public String toString(){
        return "C: " + this.id + "   " + this.color.name() + "          " + String.format("%.2f", weight());
    }
}

class Rectangle extends Shape{
    float width, height;

    public Rectangle(String id, Color color, float width, float height){
        super(id, color);
        this.width = width;
        this.height = height;
    }

    @Override
    public void scale(float scaleFactor){
        this.width *= scaleFactor;
        this.height *= scaleFactor;
    }

    @Override
    public float weight(){
        return this.width * this.height;
    }

    @Override
    public String toString(){
        return "R: " + this.id + "   " + this.color.name() + "          " + String.format("%.2f", weight());
    }
}


public class ShapesTest {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		Canvas canvas = new Canvas();
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			String[] parts = line.split(" ");
			int type = Integer.parseInt(parts[0]);
			String id = parts[1];
			if (type == 1) {
                Color color = Color.valueOf(parts[2]);
				float radius = Float.parseFloat(parts[3]);
				canvas.add(id, color, radius);
			} else if (type == 2) {
                Color color = Color.valueOf(parts[2]);
				float width = Float.parseFloat(parts[3]);
				float height = Float.parseFloat(parts[4]);
				canvas.add(id, color, width, height);
			} else if (type == 3) {
				float scaleFactor = Float.parseFloat(parts[2]);
                System.out.println("ORIGNAL:");
				System.out.print(canvas);
				canvas.scale(id, scaleFactor);
				System.out.printf("AFTER SCALING: %s %.2f\n", id, scaleFactor);
				System.out.print(canvas);
			}

		}
	}
}

