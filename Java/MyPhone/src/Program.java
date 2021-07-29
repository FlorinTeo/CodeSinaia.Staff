import java.util.ArrayList;
import java.util.List;

public class Program {

    public static void main(String[] args) {
        System.out.println("Hello MyPhone!");
        IPhone iPhone = new IPhone(16);
//        System.out.println(iPhone);
        AndroidPhone aPhone = new AndroidPhone(24);
//        System.out.println(aPhone);
        
        List<Phone> allPhones = new ArrayList<Phone>();
        allPhones.add(iPhone);
        allPhones.add(aPhone);
        
        for (Phone p : allPhones) {
            System.out.println(p);
            if (p instanceof IPhone) {
                ((IPhone)p).facetime();
            }
        }

    }

}
