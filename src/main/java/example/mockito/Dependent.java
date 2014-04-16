package example.mockito;

/**
 * 
 * @author Naeem Tahir
 *
 */
public class Dependent {
    
    Dependency dependency;

    public Dependent(Dependency obj) {
        this.dependency = obj;
    }

    public void method1() {
        dependency.operationX();
    }

    public int method2() {
        String str = dependency.operationY();
        return str.length();
    }
}
