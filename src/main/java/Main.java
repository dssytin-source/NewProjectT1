import java.util.ArrayList;
import java.util.List;

public class Main {

    public static boolean isEven(int n){
        return n % 2 == 0;
    }

    public static String checkAccess(int age){
        String result;
        if (age < 18){
            result = "Allowed";
        }
        else {
            result = "Denied";
        }
        return result;
    }

    public static boolean isPositive(int n){
        boolean ternaryStatus = (n>=0) ? true : false;
        return ternaryStatus;
    }

    public static String getGrade(int score){
        String grade;
        if (score >= 0 && score <= 20) {
            grade = "E";
        }
        else if (score > 20 && score <= 40) {
            grade = "D";
        }
        else if (score > 40 && score <= 60) {
            grade = "C";
        }
        else if (score > 60 && score <= 80) {
            grade = "B";
        }
        else if (score > 80 && score <= 100){
            grade = "A";
        }
        else {
            grade = "ERROR";
        }
        return grade;
    }

    public static String blastOff(int start){
        String countdown = "";
        for (int i = start; i >= 1; i--){
            countdown += i + " ";
        }
        return countdown + "Поехали";
    }

    public static int sumToN(int n){
        Integer sum = 0;
        for (int i = 1; i <= n; i++){
            sum = sum + i;
        }
        return sum;
    }

    public static boolean hasBug(String[] messages){
        for (String message : messages){
if ("Bug".equals(message)){
    return true;
}
        }
        return false;
    }


    public static String getEvenInRange(int start, int end) {
        String result = "";
        for (int i = start; i <= end; i++) {
            if (i % 2 == 0) {
                if (result.isEmpty()) {
                    result += i;
                } else {
                    result += " " + i;
                }
            }
        }
        return result;
    }


    public static int findMax(int[] arr) {
        int max = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > max) {
                max = arr[i];
            }
        }
        return max;
    }

    public static String[] reverse(String[] arr) {
        String[] reversed = new String[arr.length];
        for (int i = 0; i < arr.length; i++) {
            reversed[i] = arr[arr.length - 1 - i];
        }
        return reversed;
    }

    public static double calcAverage(List<Integer> list) {
        if (list == null || list.isEmpty()) {
            return 0.0;
        }
        double sum = 0;
        for (int num : list) {
            sum += num;
        }
        return sum / list.size();
    }

        public static List<String> removeSpecificName(List<String> list, String nameToRemove) {
            List<String> result = new ArrayList<>();
            if (list == null) {
                return result;
            }
            for (String name : list) {
                if (!name.equals(nameToRemove)) {
                    result.add(name);
                }
            }
            return result;
        }
    }

