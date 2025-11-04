import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

enum TYPE {
    POINT,
    CIRCLE
}

enum DIRECTION {
    UP,
    DOWN,
    LEFT,
    RIGHT
}

class ObjectCanNotBeMovedException extends Exception{
    public ObjectCanNotBeMovedException(int x, int y){
        super("Point (" + x + "," + y + ") is out of bounds");
    }
}

class MovableObjectNotFittableException extends Exception{
    public MovableObjectNotFittableException(String s){
        super(s);
    }
}

interface Movable{
    public void moveUp();
    public void moveDown();
    public void moveRight();
    public void moveLeft();
    public int getCurrentXPosition();
    public int getCurrentYPosition();
    public TYPE getType();
}

class MovablePoint implements Movable{
    private int x, y, xSpeed, ySpeed;
    private TYPE type;

    public MovablePoint(int x, int y, int xSpeed, int ySpeed){
        this.x = x;
        this.y = y;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        this.type = TYPE.POINT;
    }

    public TYPE getType(){
        return this.type;
    }

    public void moveUp(){
        try{
            if(this.y + ySpeed > MovablesCollection.Y){
                throw new ObjectCanNotBeMovedException(x, this.y + ySpeed);
            }
            this.y += ySpeed;
        }catch(ObjectCanNotBeMovedException e){
            System.out.println(e.getMessage());
        }
    }

    public void moveDown(){
        try{
            if(this.y - ySpeed < 0){
                throw new ObjectCanNotBeMovedException(x, this.y - ySpeed);
            }
            this.y -= ySpeed;
        }catch(ObjectCanNotBeMovedException e){
            System.out.println(e.getMessage());
        }    
    }

    public void moveRight(){
        try{
            if(this.x + xSpeed > MovablesCollection.X){
                throw new ObjectCanNotBeMovedException(this.x + xSpeed, y);
            }
            this.x += xSpeed;
        }catch(ObjectCanNotBeMovedException e){
            System.out.println(e.getMessage());
        }    
    }
    public void moveLeft(){
        try{
            if(this.x - xSpeed < 0){
                throw new ObjectCanNotBeMovedException(this.x - xSpeed, y);
            }
            this.x -= xSpeed;
        }catch(ObjectCanNotBeMovedException e){
            System.out.println(e.getMessage());
        }    
    }

    public int getCurrentXPosition(){
        return this.x;
    }

    public int getCurrentYPosition(){
        return this.y;
    }

    @Override
    public String toString(){
        return "Movable point with coordinates (" + Integer.toString(this.x) + "," + Integer.toString(this.y) + ")";
    }
}

class MovableCircle implements Movable{
    private int radius;
    private MovablePoint center;
    private TYPE type;
    
    public MovableCircle(int radius, MovablePoint center){
        this.radius = radius;
        this.center = center;
        this.type = TYPE.CIRCLE;
    }

    public int getRadius(){
        return this.radius;
    }

    public TYPE getType(){
        return this.type;
    }

    public void moveUp(){
        this.center.moveUp();
    }

    public void moveDown(){
        this.center.moveDown();
    }

    public void moveLeft(){
        this.center.moveLeft();
    }

    public void moveRight(){
        this.center.moveRight();
    }

    public int getCurrentXPosition(){
        return this.center.getCurrentXPosition();
    }

    public int getCurrentYPosition(){
        return this.center.getCurrentYPosition();
    }

    @Override
    public String toString(){
        return "Movable circle with center coordinates (" + Integer.toString(this.center.getCurrentXPosition()) + "," + Integer.toString(this.center.getCurrentYPosition()) + ") and radius " + Integer.toString(this.radius);
    }
}

class MovablesCollection{
    private Movable [] movable;
    private int movables = 0;
    public static int X;
    public static int Y;

    public MovablesCollection(int x_MAX, int y_MAX){
        MovablesCollection.X = x_MAX;
        MovablesCollection.Y = y_MAX;
        movable = new Movable[20];
        movables = 0;
    }

    public static void setxMax(int x){
        MovablesCollection.X = x;
    }

    public static void setyMax(int y){
        MovablesCollection.Y = y;
    }

    public void addMovableObject(Movable m){
        try{
            if(m.getType() == TYPE.POINT){
                if(m.getCurrentXPosition() < 0 || m.getCurrentXPosition() > MovablesCollection.X || m.getCurrentYPosition() < 0 || m.getCurrentYPosition() > MovablesCollection.Y){
                    throw new MovableObjectNotFittableException(m.toString() + " can not be fitted into the collection");
                }
            }
            if(m.getType() == TYPE.CIRCLE){
                MovableCircle mc = (MovableCircle) m;
                if(mc.getCurrentXPosition() - mc.getRadius() < 0 || mc.getCurrentXPosition() + mc.getRadius() > MovablesCollection.X || mc.getCurrentYPosition() - mc.getRadius() < 0 || mc.getCurrentYPosition() + mc.getRadius() > MovablesCollection.Y){
                    throw new MovableObjectNotFittableException("Movable circle with center (" + mc.getCurrentXPosition() + "," + m.getCurrentYPosition() + ") and radius " + mc.getRadius() + " can not be fitted into the collection");
                }
            }
            movable[movables] = m;
            movables++;
        }catch(MovableObjectNotFittableException e){
            System.out.println(e.getMessage());
        }
    }

    public void moveObjectsFromTypeWithDirection(TYPE type, DIRECTION direction){
        for(Movable m : movable){
            if(m == null){
                continue;
            }
            if(m.getType() != type){
                continue;
            }
            if(direction == DIRECTION.UP){
                m.moveUp();
            }
            if(direction == DIRECTION.DOWN){
                m.moveDown();
            }
            if(direction == DIRECTION.LEFT){
                m.moveLeft();
            }
            if(direction == DIRECTION.RIGHT){
                m.moveRight();
            }
        }
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder("");
        sb.append("Collection of movable objects with size " + movables + ":\n");
        for(Movable m : movable){
            if(m == null){
                continue;
            }
            sb.append(m.toString() + "\n");
        }
        return sb.toString();
    }
}

public class CirclesTest {

    public static void main(String[] args) {

        System.out.println("===COLLECTION CONSTRUCTOR AND ADD METHOD TEST===");
        MovablesCollection collection = new MovablesCollection(100, 100);
        Scanner sc = new Scanner(System.in);
        int samples = Integer.parseInt(sc.nextLine());
        for (int i = 0; i < samples; i++) {
            String inputLine = sc.nextLine();
            String[] parts = inputLine.split(" ");

            int x = Integer.parseInt(parts[1]);
            int y = Integer.parseInt(parts[2]);
            int xSpeed = Integer.parseInt(parts[3]);
            int ySpeed = Integer.parseInt(parts[4]);

            if (Integer.parseInt(parts[0]) == 0) { //point
                collection.addMovableObject(new MovablePoint(x, y, xSpeed, ySpeed));
            } else { //circle
                int radius = Integer.parseInt(parts[5]);
                collection.addMovableObject(new MovableCircle(radius, new MovablePoint(x, y, xSpeed, ySpeed)));
            }
           
        }
        System.out.println(collection.toString());

        System.out.println("MOVE POINTS TO THE LEFT");
        collection.moveObjectsFromTypeWithDirection(TYPE.POINT, DIRECTION.LEFT);
        System.out.println(collection.toString());

        System.out.println("MOVE CIRCLES DOWN");
        collection.moveObjectsFromTypeWithDirection(TYPE.CIRCLE, DIRECTION.DOWN);
        System.out.println(collection.toString());

        System.out.println("CHANGE X_MAX AND Y_MAX");
        MovablesCollection.setxMax(90);
        MovablesCollection.setyMax(90);

        System.out.println("MOVE POINTS TO THE RIGHT");
        collection.moveObjectsFromTypeWithDirection(TYPE.POINT, DIRECTION.RIGHT);
        System.out.println(collection.toString());

        System.out.println("MOVE CIRCLES UP");
        collection.moveObjectsFromTypeWithDirection(TYPE.CIRCLE, DIRECTION.UP);
        System.out.println(collection.toString());


    }


}
