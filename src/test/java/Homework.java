import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

public class Homework {
    @Test
    void runTest(){
        System.out.println(Main.isEven(4));
        System.out.println(Main.checkAccess(15));
        System.out.println(Main.isPositive(-15));
        System.out.println(Main.getGrade(75));
        System.out.println(Main.blastOff(7));
        System.out.println(Main.sumToN(6));
        System.out.println(Main.hasBug(new String[]{"Bu", "Bung", "bubung", "bug", "Bug"}));
        System.out.println(Main.getEvenInRange(2, 19));
        System.out.println(Main.findMax(new int[]{1, 17, -5, 0, -64, 31}));
        System.out.println(Arrays.toString(Main.reverse(new String[]{"!","world","Hello"})));
        System.out.println(Main.calcAverage(List.of(1, 3, 13, 5)));
        System.out.println(Main.removeSpecificName(List.of("Дмитрий", "Лжедмитрий", "Петр", "Иоан", "Лжедмитрий"), "Лжедмитрий"));
    }
    }

