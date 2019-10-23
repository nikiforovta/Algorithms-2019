package lesson1;

import javafx.util.Pair;
import kotlin.NotImplementedError;

import java.io.*;
import java.util.*;

@SuppressWarnings("unused")
public class JavaTasks {
    /**
     * Сортировка времён
     *
     * Простая
     * (Модифицированная задача с сайта acmp.ru)
     *
     * Во входном файле с именем inputName содержатся моменты времени в формате ЧЧ:ММ:СС AM/PM,
     * каждый на отдельной строке. См. статью википедии "12-часовой формат времени".
     *
     * Пример:
     *
     * 01:15:19 PM
     * 07:26:57 AM
     * 10:00:03 AM
     * 07:56:14 PM
     * 01:15:19 PM
     * 12:40:31 AM
     *
     * Отсортировать моменты времени по возрастанию и вывести их в выходной файл с именем outputName,
     * сохраняя формат ЧЧ:ММ:СС AM/PM. Одинаковые моменты времени выводить друг за другом. Пример:
     *
     * 12:40:31 AM
     * 07:26:57 AM
     * 10:00:03 AM
     * 01:15:19 PM
     * 01:15:19 PM
     * 07:56:14 PM
     *
     * В случае обнаружения неверного формата файла бросить любое исключение.
     */

    /**
     * Time Complexity  O(n * log(n)) - сложность алгоритма сортировки
     * Resource Complexity O(n) - так как необходимо создать ArrayList, количество элементов которого равно размеру входных данных
     */
    static public void sortTimes(String inputName, String outputName) throws Exception {
        ArrayList<Pair<Integer, String>> toSort = new ArrayList<>();
        final int hourSec = 3600;
        final int minSec = 60;
        try (BufferedReader inp = new BufferedReader(new FileReader(new File(inputName)))) {
            String line;
            while ((line = inp.readLine()) != null) {
                if (!line.matches("(([0-9]{2}:[0-9]{2}:[0-9]{2}) (AM|PM))")) {
                    throw new IllegalArgumentException();
                }
                String[] time = line.split("[: ]");

                int h = Integer.parseInt(time[0]);
                int m = Integer.parseInt(time[1]);
                int s = Integer.parseInt(time[2]);
                if (h > 12 || h < 1 || m > 59 || m < 0 || s > 59 || s < 0) {
                    throw new IllegalArgumentException();
                }
                int seconds = Integer.parseInt(time[2]) + minSec * Integer.parseInt(time[1]);
                if (time[3].equals("AM")) seconds += (Integer.parseInt(time[0]) % 12) * hourSec;
                else seconds += (Integer.parseInt(time[0]) % 12 + 12) * hourSec;
                toSort.add(new Pair<>(seconds, line));
            }
        }
        toSort.sort(Comparator.comparing(Pair::getKey));

        FileWriter writer = new FileWriter(outputName, true);
        for (Pair<Integer, String> time : toSort) {
            String out = time.getValue();
            writer.write(out + System.getProperty("line.separator"));
        }
        writer.close();
    }

    /**
     * Сортировка адресов
     *
     * Средняя
     *
     * Во входном файле с именем inputName содержатся фамилии и имена жителей города с указанием улицы и номера дома,
     * где они прописаны. Пример:
     *
     * Петров Иван - Железнодорожная 3
     * Сидоров Петр - Садовая 5
     * Иванов Алексей - Железнодорожная 7
     * Сидорова Мария - Садовая 5
     * Иванов Михаил - Железнодорожная 7
     *
     * Людей в городе может быть до миллиона.
     *
     * Вывести записи в выходной файл outputName,
     * упорядоченными по названию улицы (по алфавиту) и номеру дома (по возрастанию).
     * Людей, живущих в одном доме, выводить через запятую по алфавиту (вначале по фамилии, потом по имени). Пример:
     *
     * Железнодорожная 3 - Петров Иван
     * Железнодорожная 7 - Иванов Алексей, Иванов Михаил
     * Садовая 5 - Сидоров Петр, Сидорова Мария
     *
     * В случае обнаружения неверного формата файла бросить любое исключение.
     */

    /**
     * Time Complexity  O(n * log(n)) - сложность алгоритма сортировки
     * Resource Complexity O(n) - - так как необходимо создать ArrayList,
     * количество элементов которого равно размеру входных данных
     */
    static public void sortAddresses(String inputName, String outputName) throws Exception {
        HashMap<String, LinkedList<String>> toSort = new HashMap<>();
        try (BufferedReader inp = new BufferedReader(new FileReader(new File(inputName)))) {
            String line;
            while ((line = inp.readLine()) != null) {
                String[] info = line.split(" - ");

                if (info[1].split(" ").length != 2) {
                    throw new IllegalArgumentException();
                }
                if (toSort.containsKey(info[1])) {
                    toSort.get(info[1]).add(info[0]);
                } else {
                    toSort.put(info[1], new LinkedList<>(Collections.singletonList(info[0])));
                }
            }
        }

        FileWriter writer = new FileWriter(outputName);
        toSort.entrySet().stream().sorted(Map.Entry.comparingByKey(new AddressSortingComparator())).forEachOrdered(e -> {
            StringBuilder out = new StringBuilder();
            out.append(e.getKey()).append(" - ");
            SortedSet<String> people = new TreeSet<>(e.getValue());
            for (String man : people) {
                out.append(man).append(", ");
            }
            out.delete(out.length() - 2, out.length());
            try {
                writer.write(out + System.getProperty("line.separator"));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        writer.close();
    }

    static class AddressSortingComparator implements Comparator<String> {
        @Override
        public int compare(String addr1, String addr2) {
            int strCompare = addr1.split(" ")[0].compareTo(addr2.split(" ")[0]);
            int numCompare = Integer.compare(Integer.parseInt(addr1.split(" ")[1]), Integer.parseInt(addr2.split(" ")[1]));
            if (strCompare == 0) return numCompare > 0 ? 1 : -1;
            else return strCompare;
        }
    }

    /**
     * Сортировка температур
     *
     * Средняя
     * (Модифицированная задача с сайта acmp.ru)
     *
     * Во входном файле заданы температуры различных участков абстрактной планеты с точностью до десятых градуса.
     * Температуры могут изменяться в диапазоне от -273.0 до +500.0.
     * Например:
     *
     * 24.7
     * -12.6
     * 121.3
     * -98.4
     * 99.5
     * -12.6
     * 11.0
     *
     * Количество строк в файле может достигать ста миллионов.
     * Вывести строки в выходной файл, отсортировав их по возрастанию температуры.
     * Повторяющиеся строки сохранить. Например:
     *
     * -98.4
     * -12.6
     * -12.6
     * 11.0
     * 24.7
     * 99.5
     * 121.3
     */

    /**
     * Time complexity  O(n) - так как необходимо один раз пройтись по входным данным
     * Resource complexity  O(1) - так как независимо от объёма входных данных создаётся массив фиксированного размера
     */
    static public void sortTemperatures(String inputName, String outputName) throws Exception {
        int[] tempArray = new int[7732];
        try (BufferedReader input = new BufferedReader(new FileReader(new File(inputName)))) {
            String line;
            while ((line = input.readLine()) != null) {
                if (!line.matches("(-?\\d{1,3}\\.\\d)")) {
                    throw new IllegalArgumentException();
                }
                double temperature = Double.parseDouble(line);
                if (temperature > 500.0 || temperature < -273.0) {
                    throw new IllegalArgumentException();
                }
                tempArray[(int) (temperature * 10 + 2730)]++;
            }
        }
        FileWriter writer = new FileWriter(outputName);
        for (int i = 0; i < tempArray.length; i++) {
            double out = 0.0;
            if (tempArray[i] > 0) {
                out = (double) (i - 2730) / 10;
            }
            while (tempArray[i] > 0) {
                writer.write(out + System.getProperty("line.separator"));
                tempArray[i]--;
            }
        }
        writer.close();
    }

    /**
     * Сортировка последовательности
     *
     * Средняя
     * (Задача взята с сайта acmp.ru)
     *
     * В файле задана последовательность из n целых положительных чисел, каждое в своей строке, например:
     *
     * 1
     * 2
     * 3
     * 2
     * 3
     * 1
     * 2
     *
     * Необходимо найти число, которое встречается в этой последовательности наибольшее количество раз,
     * а если таких чисел несколько, то найти минимальное из них,
     * и после этого переместить все такие числа в конец заданной последовательности.
     * Порядок расположения остальных чисел должен остаться без изменения.
     *
     * 1
     * 3
     * 3
     * 1
     * 2
     * 2
     * 2
     */
    static public void sortSequence(String inputName, String outputName) {
        throw new NotImplementedError();
    }

    /**
     * Соединить два отсортированных массива в один
     *
     * Простая
     *
     * Задан отсортированный массив first и второй массив second,
     * первые first.size ячеек которого содержат null, а остальные ячейки также отсортированы.
     * Соединить оба массива в массиве second так, чтобы он оказался отсортирован. Пример:
     *
     * first = [4 9 15 20 28]
     * second = [null null null null null 1 3 9 13 18 23]
     *
     * Результат: second = [1 3 4 9 9 13 15 20 23 28]
     */

    /**
     * Time Complexity  O(n) - так как необходимо пройти по всей длине второго массива
     * Resource Complexity  O(1) - так как не требуется создание дополнительных переменных
     */
    static <T extends Comparable<T>> void mergeArrays(T[] first, T[] second) {
        System.arraycopy(first, 0, second, 0, first.length);
        int li = 0, ri = first.length;
        for (int i = 0; i < second.length; i++) {
            if (li < first.length && (ri == second.length || first[li].compareTo(second[ri]) <= 0)) {
                second[i] = first[li++];
            } else {
                second[i] = second[ri++];
            }
        }
    }
}