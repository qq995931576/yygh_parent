import org.springframework.data.mongodb.core.mapping.TextScore;

/******
 @author 阿昌
 @create 2021-03-24 19:35
 *******
 */
public class test {

    public final static Double pi = 3.14;

    public static void main(String[] args) {
        double a = 10;
        double b = 5;
        Double area = area(a, b);
        System.out.println(area);
    }

    //三角形
    private static Double area(Double a,Double b,double c){
        return (a+b)/2;
    }

    //矩形
    private static Double area(Double i,Double j){
        return i*j;
    }

    //圆形
    private static Double area(Double a){
        return Math.pow(a,2)*pi;
    }



}
