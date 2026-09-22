import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

@Tag ("homeworkWebinar")
public class Homework3WebinarTest {

    private static final Random random = new Random();

//    @BeforeEach
//    void printBefore() {
//        System.out.println("Test method start");
//        System.out.println("========================");
//    }

//    @AfterEach
//    void printAfter(){
//        System.out.println("========================");
//        System.out.println("Test method end");
//    }

    @ParameterizedTest
    @CsvSource({"1, false", "4, true", "7, false", "8, true", "10, true",
            "11, false", "12, true", "13, false", "14, true", "16, true"})
    void isEvenCsvTest(int number, boolean expected) {
        assertEquals(expected, Main.isEven(number),
                "Неверный результат проверки четности для числа: " + number);
    }


    @ParameterizedTest
    @CsvSource({
            "15, Denied", "18, Denied", "19, Allowed", "25, Allowed", "100, Allowed",
            "10, Denied", "17, Denied", "20, Allowed", "30, Allowed", "99, Allowed"
    })
    void testCheckAccess(int age, String expected) {
        assertEquals(expected, Main.checkAccess(age),
                "Неверный статус доступа для возраста: " + age);
    }

    @ParameterizedTest
    @MethodSource("Main#randomNumber")
    void testIsPositive(int number) {
        boolean expected = number >= 0;
        assertEquals(expected, Main.isPositive(number),
                "Ошибка проверки знака для числа: " + number);
    }

    @ParameterizedTest
    @ValueSource(ints = {90, 91, 92, 93, 94, 95, 96, 97, 98, 100})
    void testGetGradeA(int score) {
        assertEquals("A", Main.getGrade(score),
                "Для балла " + score + " ожидалась оценка A");
    }

    @RepeatedTest(10)
    void blastOffRandomTest() {
        int randomBlast = random.nextInt(3, 6);
        String actual = Main.blastOff(randomBlast);
        String expected = switch (randomBlast) {
            case 3 -> "3 2 1 Поехали";
            case 4 -> "4 3 2 1 Поехали";
            default -> "5 4 3 2 1 Поехали";
        };
        assertEquals(expected, actual,
                "Неверный вывод обратного отсчета для n = " + randomBlast);
    }

    @RepeatedTest(10)
    void sumToNRandomTest() {
        int numberToSum = random.nextInt(21);
        int actual = Main.sumToN(numberToSum);
        int expected = numberToSum * (numberToSum + 1) / 2;
        assertEquals(expected, actual,
                "Сумма чисел от 1 до " + numberToSum + " вычислена неверно");
    }

    @RepeatedTest(10)
    void testHasBug() {
        String[] wordsWithBug = {"Bu", "Bung", "bubung", "bug", "Bug"};
        assertTrue(Main.hasBug(wordsWithBug),
                "Ожидалось true: в массиве " + Arrays.toString(wordsWithBug) + " содержится слово 'bug'");

        String[] wordsWithoutBug = {"apple", "banana", "cat"};
        assertFalse(Main.hasBug(wordsWithoutBug),
                "Ожидалось false: в массиве " + Arrays.toString(wordsWithoutBug) + " нет слова 'bug'");
    }

    @RepeatedTest(10)
    void getEvenInRangeFromListTest() {
        int start = 2;
        int end = 19;
        String expected = "2 4 6 8 10 12 14 16 18";
        String actual = Main.getEvenInRange(start, end);
        assertEquals(expected, actual,
                "Диапазон четных чисел от " + start + " до " + end + " сформирован неверно");
    }

    @RepeatedTest(10)
    void findMaxRandomTest() {
        int[] numbers = random.ints(5, -50, 51).toArray();
        int expected = Arrays.stream(numbers).max().orElseThrow();
        int actual = Main.findMax(numbers);
        assertEquals(expected, actual,
                "Неверно найден максимум в массиве: " + Arrays.toString(numbers));
    }

    @RepeatedTest(10)
    void reverseFromListTest() {
        String[] original = {"!", "world", "Hello"};
        String[] expected = {"Hello", "world", "!"};
        String[] actual = Main.reverse(original);
        assertArrayEquals(expected, actual,
                "Массив развернут некорректно для: " + Arrays.toString(original));
    }

    @RepeatedTest(10)
    void testCalcAverageRandom() {
        int a = random.nextInt(1, 10) * 2;
        int b = random.nextInt(1, 10) * 2;
        List<Integer> numbers = List.of(a, b);
        double expected = (a + b) / 2.0;
        double actual = Main.calcAverage(numbers);
        assertEquals(expected, actual, 0.001,
                "Среднее значение для списка " + numbers + " вычислено неверно");
    }

    @RepeatedTest(10)
    void testRemoveSpecificName() {
        List<String> names = new ArrayList<>(List.of("Дмитрий", "Лжедмитрий", "Петр", "Иоан", "Лжедмитрий"));
        String nameToRemove = "Лжедмитрий";
        List<String> expected = List.of("Дмитрий", "Петр", "Иоан");
        List<String> actual = Main.removeSpecificName(names, nameToRemove);
        assertIterableEquals(expected, actual,
                "Элемент '" + nameToRemove + "' не был корректно удален из списка");
    }
    }

