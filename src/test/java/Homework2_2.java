import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Objects;
import java.util.Random;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class Homework2_2 {

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

    @ParameterizedTest
    @CsvSource({"1, false", "4, true"})
    void isEvenCsvTest(int number, boolean expected) {
        if (Main.isEven(number) == expected) {
            System.out.println("TEST PASSED");
        } else {
            System.out.println("TEST FAILED");
        }
    }

    @ParameterizedTest
    @CsvSource({
            "15, Denied",
            "18, Denied",
            "19, Allowed",
            "25, Allowed",
            "100, Denied"
    })
    void checkAccessTest(int age, String expected) {
        if (Objects.equals(Main.checkAccess(age), expected)) {
            System.out.println("TEST PASSED");
        } else {
            System.out.println("TEST FAILED");
        }
    }
    @ParameterizedTest
    @MethodSource("Main#randomNumber")
    void isPositiveRandomTest(int number) {
        boolean expected = number > 0;
        boolean actual = Main.isPositive(number);
        if (actual == expected) {
            System.out.println("TEST PASSED");
        } else {
            System.out.println("TEST FAILED");
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {90, 91, 95, 99, 100})
    void getGradeATest(int score) {
        String actual = Main.getGrade(score);
        String expected = "A";
        if (Objects.equals(actual, expected)) {
            System.out.println("TEST PASSED");
        } else {
            System.out.println("TEST FAILED");
        }
    }

    @RepeatedTest(5)
    void blastOffRandomTest() {
        int randomBlast = random.nextInt(3, 6); // даст 3, 4 или 5
        String actual = Main.blastOff(randomBlast);
        String expected = switch (randomBlast) {
            case 3 -> "3 2 1 Blastoff!";
            case 4 -> "4 3 2 1 Blastoff!";
            default -> "5 4 3 2 1 Blastoff!";
        };

        if (Objects.equals(actual, expected)) {
            System.out.println("TEST PASSED");
        } else {
            System.out.println("TEST FAILED");
        }
    }

    @RepeatedTest(10)
    void sumToNRandomTest(){
        int numberToSum = random.nextInt(21);
        int actual = Main.sumToN(numberToSum);
        int expected = numberToSum * (numberToSum + 1) / 2;
        if (actual == expected) {
            System.out.println("TEST PASSED");
        } else {
            System.out.println("TEST FAILED");
        }
    }
    @Test
    void hasBugTest() {
        String[] wordsWithBug = {"Bu", "Bung", "bubung", "bug", "Bug"};
        boolean actualWithBug = Main.hasBug(wordsWithBug);
        boolean expectedWithBug = true;
        if (actualWithBug == expectedWithBug) {
            System.out.println("В передаваемом массиве: " + Arrays.toString(wordsWithBug) + " есть слово bug");
            System.out.println("TEST PASSED");
        } else {
            System.out.println("TEST FAILED");
        }
        String[] wordsWithoutBug = {"apple", "banana", "cat"};
        boolean actualWithoutBug = Main.hasBug(wordsWithoutBug);
        boolean expectedWithoutBug = false;
        if (actualWithoutBug == expectedWithoutBug) {
            System.out.println("В передаваемом массиве: " + Arrays.toString(wordsWithoutBug) + " нет слова bug");
            System.out.println("TEST PASSED");
        } else {
            System.out.println("TEST FAILED");
        }
    }

    @Test
    void getEvenInRangeFromListTest(){
        int start = 2;
        int end = 19;
        String expected = "2 4 6 8 10 12 14 16 18";
        String actual = Main.getEvenInRange(start, end);
        if (Objects.equals(actual, expected)) {
            System.out.println("TEST PASSED");
        } else {
            System.out.println("TEST FAILED");
        }
    }

    @RepeatedTest(5)
    void findMaxRandomTest() {
        int[] numbers = random.ints(5, -50, 51).toArray();
        int expected = Arrays.stream(numbers).max().orElseThrow();
        int actual = Main.findMax(numbers);
        if (actual == expected) {
            System.out.println("TEST PASSED");
        } else {
            System.out.println("TEST FAILED");
        }
    }

    @Test
    void reverseFromListTest() {
        String[] original = {"!","world","Hello"};
        String[] expected = {"Hello","world","!"};
        String[] actual = Main.reverse(original);

        if (Arrays.equals(actual, expected)) {
            System.out.println("TEST PASSED");
        } else {
            System.out.println("TEST FAILED");
        }
    }

    @RepeatedTest(5)
    void calcAverageRandomTest() {
        int a = random.nextInt(1, 10) * 2;
        int b = random.nextInt(1, 10) * 2;
        List<Integer> numbers = List.of(a, b);
        double expected = (a + b) / 2.0;
        double actual = Main.calcAverage(numbers);
        if (actual == expected) {
            System.out.println("TEST PASSED");
        } else {
            System.out.println("TEST FAILED");
        }
    }

    @Test
    void removeSpecificNameTest() {
        List<String> names = new ArrayList<>(List.of("Дмитрий", "Лжедмитрий", "Петр", "Иоан", "Лжедмитрий"));
        String nameToRemove = "Лжедмитрий";
        List<String> expected = List.of("Дмитрий", "Петр", "Иоан");
        List<String> actual = Main.removeSpecificName(names, nameToRemove);
        if (Objects.equals(actual, expected)) {
            System.out.println("TEST PASSED");
        } else {
            System.out.println("TEST FAILED");
        }
    }
    }

