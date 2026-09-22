import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@Tag("homeworkLecture")
public class Homework3LectureTest {

    @Test
    @DisplayName("Проверка четности числа (булево значение)")
    void testIsEven() {
        int number = 4;
        boolean actual = Main.isEven(number);
        assertTrue(actual, "Ожидалось, что число " + number + " будет определено как четное");
    }

    @Test
    @DisplayName("Проверка удаления элемента из списка")
    void testRemoveSpecificName() {
        List<String> names = new ArrayList<>(List.of("Дмитрий", "Лжедмитрий", "Петр", "Иоан", "Лжедмитрий"));
        String nameToRemove = "Лжедмитрий";
        List<String> expectedList = List.of("Дмитрий", "Петр", "Иоан");
        List<String> actualList = Main.removeSpecificName(names, nameToRemove);
        assertIterableEquals(expectedList, actualList,
                "Список после удаления '" + nameToRemove + "' не совпадает с ожидаемым");
    }

    @Test
    @DisplayName("Проверка получения статуса доступа")
    void testCheckAccess() {
        int age = 21;
        String expected = "Allowed";
        String actual = Main.checkAccess(age);
        assertEquals(expected, actual,
                "Для возраста " + age + " ожидался статус доступа '" + expected + "'");
    }

    @Test
    @DisplayName("Падающий тест для демонстрации ошибки сравнения")
    void testFailingMethod() {
        int n = 5;
        int actualSum = Main.sumToN(n);
        int wrongExpected = 100;
        assertEquals(wrongExpected, actualSum,
                "Демонстрация падения: ожидаемая сумма заведомо указана неверно для n = " + n);
    }
}
