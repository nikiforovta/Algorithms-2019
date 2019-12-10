package lesson6;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("unused")
public class JavaDynamicTasks {
    /**
     * Наибольшая общая подпоследовательность.
     * Средняя
     * <p>
     * Дано две строки, например "nematode knowledge" и "empty bottle".
     * Найти их самую длинную общую подпоследовательность -- в примере это "emt ole".
     * Подпоследовательность отличается от подстроки тем, что её символы не обязаны идти подряд
     * (но по-прежнему должны быть расположены в исходной строке в том же порядке).
     * Если общей подпоследовательности нет, вернуть пустую строку.
     * Если есть несколько самых длинных общих подпоследовательностей, вернуть любую из них.
     * При сравнении подстрок, регистр символов *имеет* значение.
     * <p>
     * <p>
     * Time Complexity: O(mn)
     * Memory Complexity: O(mn)
     * m - длина первой строки
     * n - длина второй строки
     */
    public static String longestCommonSubSequence(String first, String second) {
        int firstLength = first.length();
        int secondLength = second.length();
        int[][] overlap = new int[firstLength + 1][secondLength + 1];
        for (int i = firstLength - 1; i >= 0; i--) {
            for (int j = secondLength - 1; j >= 0; j--) {
                if (first.charAt(i) == second.charAt(j)) {
                    overlap[i][j] = 1 + overlap[i + 1][j + 1];
                } else {
                    overlap[i][j] = Math.max(overlap[i + 1][j], overlap[i][j + 1]);
                }
            }
        }

        StringBuilder result = new StringBuilder();
        int i = 0;
        int j = 0;
        while (i < firstLength && j < secondLength) {
            if (first.charAt(i) == second.charAt(j)) {
                result.append(first.charAt(i));
                i++;
                j++;
            } else {
                if (overlap[i + 1][j] >= overlap[i][j + 1]) {
                    i++;
                } else {
                    j++;
                }
            }

        }
        return result.toString();
    }

    /**
     * Наибольшая возрастающая подпоследовательность
     * Сложная
     * <p>
     * Дан список целых чисел, например, [2 8 5 9 12 6].
     * Найти в нём самую длинную возрастающую подпоследовательность.
     * Элементы подпоследовательности не обязаны идти подряд,
     * но должны быть расположены в исходном списке в том же порядке.
     * Если самых длинных возрастающих подпоследовательностей несколько (как в примере),
     * то вернуть ту, в которой числа расположены раньше (приоритет имеют первые числа).
     * В примере ответами являются 2, 8, 9, 12 или 2, 5, 9, 12 -- выбираем первую из них.
     * <p>
     * <p>
     * Time Complexity: O(n^2)
     * Memory Complexity: O(n)
     * n - длина списка
     */
    public static List<Integer> longestIncreasingSubSequence(List<Integer> list) {
        int n = list.size();
        if (n == 0 || n == 1) {
            return list;
        }

        int[] prev = new int[n];
        int[] d = new int[n];

        for (int i = 0; i < n; i++) {
            d[i] = 1;
            prev[i] = -1;
            for (int j = 0; j < i; j++) {
                if (list.get(j) < list.get(i) && d[j] + 1 > d[i]) {
                    d[i] = d[j] + 1;
                    prev[i] = j;
                }
            }
        }
        int pos = 0;
        int length = d[0];
        for (int i = 0; i < n; i++) {
            if (d[i] > length) {
                pos = i;
                length = d[i];
            }
        }

        List<Integer> result = new ArrayList<>();
        while (pos != -1) {
            result.add(list.get(pos));
            pos = prev[pos];
        }
        Collections.reverse(result);
        return result;
    }

    /**
     * Самый короткий маршрут на прямоугольном поле.
     * Средняя
     * <p>
     * В файле с именем inputName задано прямоугольное поле:
     * <p>
     * 0 2 3 2 4 1
     * 1 5 3 4 6 2
     * 2 6 2 5 1 3
     * 1 4 3 2 6 2
     * 4 2 3 1 5 0
     * <p>
     * Можно совершать шаги длиной в одну клетку вправо, вниз или по диагонали вправо-вниз.
     * В каждой клетке записано некоторое натуральное число или нуль.
     * Необходимо попасть из верхней левой клетки в правую нижнюю.
     * Вес маршрута вычисляется как сумма чисел со всех посещенных клеток.
     * Необходимо найти маршрут с минимальным весом и вернуть этот минимальный вес.
     * <p>
     * Здесь ответ 2 + 3 + 4 + 1 + 2 = 12
     * <p>
     * <p>
     * Time Complexity: O(mn)
     * Memory Complexity: O(mn)
     * m - ширина поля
     * n - длина поля
     */
    public static int shortestPathOnField(String inputName) {
        List<String> input = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(inputName))) {
            String inputString;
            while ((inputString = reader.readLine()) != null) {
                input.add(inputString);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        int height = input.size();
        int width = input.get(0).split(" ").length;
        int[][] field = new int[height][width];

        for (int i = 0; i < height; i++) {
            String[] line = input.get(i).split(" ");
            if (line.length != width) {
                throw new IllegalArgumentException();
            }
            for (int j = 0; j < line.length; j++) {
                field[i][j] = Integer.parseInt(line[j]);
            }
        }

        for (int i = 1; i < height; i++) {
            field[i][0] = field[i][0] + field[i - 1][0];
        }
        for (int j = 1; j < width; j++) {
            field[0][j] = field[0][j] + field[0][j - 1];
        }

        for (int i = 1; i < height; i++) {
            for (int j = 1; j < width; j++) {
                int min = Math.min(field[i - 1][j], field[i][j - 1]);
                field[i][j] += Math.min(min, field[i - 1][j - 1]);
            }
        }
        return field[height - 1][width - 1];
    }

    // Задачу "Максимальное независимое множество вершин в графе без циклов"
    // смотрите в уроке 5
}
