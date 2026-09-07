import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Random;

public class Homework2_1 {

    private static final Random random = new Random();

    @BeforeEach
    void printBefore() {
        System.out.println("Test method start");
        System.out.println("========================");
    }

    @AfterEach
    void printAfter(){
        System.out.println("========================");
        System.out.println("Test method end");
    }
    @Test
    void evenRandomTest() {
        int number = random.nextInt(1, 101);
        System.out.println("Number is " + number);
        System.out.println("It's even: " + Main.isEven(number));
    }
    @RepeatedTest(20)
    void checkAccessRandomTest(){

        int age = random.nextInt(100);
        System.out.println("Age: " + age);
        System.out.println(Main.checkAccess(age));
        }
        //System.out.println(Main.isPositive(-15));


    @ParameterizedTest()
    @MethodSource ("Main#randomScore")
    void getGradeRandomTest(int randomScore) {
        System.out.println("Score: " + randomScore);
        System.out.println(Main.getGrade(randomScore));
    }
}


