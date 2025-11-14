import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
public class ComponentTest {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		String name = scanner.nextLine();
		Window window = new Window(name);
		Component prev = null;
		while (true) {
            try {
				int what = scanner.nextInt();
				scanner.nextLine();
				if (what == 0) {
					int position = scanner.nextInt();
					window.addComponent(position, prev);
				} else if (what == 1) {
					String color = scanner.nextLine();
					int weight = scanner.nextInt();
					Component component = new Component(color, weight);
					prev = component;
				} else if (what == 2) {
					String color = scanner.nextLine();
					int weight = scanner.nextInt();
					Component component = new Component(color, weight);
					prev.addComponent(component);
                    prev = component;
				} else if (what == 3) {
					String color = scanner.nextLine();
					int weight = scanner.nextInt();
					Component component = new Component(color, weight);
					prev.addComponent(component);
				} else if(what == 4) {
                	break;
                }
                
            } catch (InvalidPositionException e) {
				System.out.println(e.getMessage());
			}
            scanner.nextLine();			
		}
		
        System.out.println("=== ORIGINAL WINDOW ===");
		System.out.println(window);
		int weight = scanner.nextInt();
		scanner.nextLine();
		String color = scanner.nextLine();
		window.changeColor(weight, color);
        System.out.println(String.format("=== CHANGED COLOR (%d, %s) ===", weight, color));
		System.out.println(window);
		int pos1 = scanner.nextInt();
		int pos2 = scanner.nextInt();
        System.out.println(String.format("=== SWITCHED COMPONENTS %d <-> %d ===", pos1, pos2));
		window.swichComponents(pos1, pos2);
		System.out.println(window);
	}
}

class Component{
    String color;
    int weight;
    ArrayList<Component> comps;

    public Component(String c,int w){
        color = c;
        weight = w;
        comps = new ArrayList<Component>();
    }

    public void addComponent(Component c){
        if(comps.isEmpty()){
            comps.add(c);
            return;
        }

        for(int i = 0;i < comps.size();i++){
            if(comps.get(i).compareTo(c) >= 1){
                comps.add(i, c);
                return;
            }
        }
        comps.add(c);
    }

    public void changeColor(int weight, String color){
        if(this.weight < weight){
            this.color = color;
        }
        if(comps.isEmpty()){
            return;
        }
        comps.stream().forEach(a -> a.changeColor(weight, color));
    }
    
    public int compareTo(Component b){
        if(Integer.compare(this.weight, b.weight) == 0){
            return this.color.compareTo(b.color);
        }
        return Integer.compare(this.weight, b.weight);
    }

    public String toString(int step){
        StringBuilder sb = new StringBuilder();
        comps.stream().forEach(a -> {
            for(int i = 0;i < step;i++){
                sb.append("---");
            } 
            sb.append(String.format("%d:%s\n", a.weight, a.color));
            sb.append(String.format("%s", a.toString(step+1)));
        });
        return sb.toString();
    }
}

class Window{
    String name;
    ArrayList<Component> comps;
    HashMap<Integer, Component> map;

    public Window(String name){
        this.name = name;
        comps = new ArrayList<Component>();
        map = new HashMap<Integer, Component>();
    }

    public void addComponent(int pos, Component component) throws InvalidPositionException{
        if(map.containsKey(pos)){
            throw new InvalidPositionException(pos);
        }
        map.put(pos, component);
    }

    public void changeColor(int weight, String color){
        map.forEach((key, value) -> {
            if(value.weight < weight){
                value.color = color;
            }
            value.comps.forEach(a -> a.changeColor(weight, color));
        });
    }

    public void swichComponents(int pos1, int pos2){
        Component a = map.get(pos1);
        Component b = map.get(pos2);
        map.put(pos1, b);
        map.put(pos2, a);
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("WINDOW %s\n",this.name));
        map.forEach((key, value) -> {
            sb.append(String.format("%d:%d:%s\n", key, value.weight, value.color));
            sb.append(String.format("%s", value.toString(1)));
        });
        return sb.toString();
    }
}

class InvalidPositionException extends Exception{
    public InvalidPositionException(int pos){
        super(String.format("Invalid position %d, alredy taken!", pos));
    }
}
