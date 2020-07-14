package funfunfun;

public class Test {

    public static void main(String[] args){
        String tester = "yay";
        Shape sTest = new Shape(tester);
        String s = sTest.getIsShape();
        s = "hello";
        System.out.println(sTest.getIsShape());
        
        //Shape s = new Triangle();
        //s.match(s);
        
        //Triangle t = new Triangle();
        //t.match(t);
        
        //t.match(s);
    }
}

class Shape {
    
    private String isShape;
    
    public Shape(String isShape){
        this.isShape = isShape;
    }
    
   public String getIsShape(){
       return isShape;
   }
   
    public void match(Shape s){
        System.out.println(name() + " has a " + s.name());
        
    }
    
    public String name() { return "Shape";}
    
    
}

class Triangle extends Shape {
    
    public Triangle(String isShape) {
        super(isShape);
        // TODO Auto-generated constructor stub
    }
    public void match(Triangle t){
        System.out.println(name() + " got a " + t.name());
        
        
    }
    public String name() { return "Triangle";}
}