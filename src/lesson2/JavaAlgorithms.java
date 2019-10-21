package lesson2;

import kotlin.NotImplementedError;
import kotlin.Pair;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("unused")
public class JavaAlgorithms {
    /**
     * Получение наибольшей прибыли (она же -- поиск максимального подмассива)
     * Простая
     *
     * Во входном файле с именем inputName перечислены цены на акции компании в различные (возрастающие) моменты времени
     * (каждая цена идёт с новой строки). Цена -- это целое положительное число. Пример:
     *
     * 201
     * 196
     * 190
     * 198
     * 187
     * 194
     * 193
     * 185
     *
     * Выбрать два момента времени, первый из них для покупки акций, а второй для продажи, с тем, чтобы разница
     * между ценой продажи и ценой покупки была максимально большой. Второй момент должен быть раньше первого.
     * Вернуть пару из двух моментов.
     * Каждый момент обозначается целым числом -- номер строки во входном файле, нумерация с единицы.
     * Например, для приведённого выше файла результат должен быть Pair(3, 4)
     *
     * В случае обнаружения неверного формата файла бросить любое исключение.
     */
    static public Pair<Integer, Integer> optimizeBuyAndSell(String inputName) {
        throw new NotImplementedError();
    }

    /**
     * Задача Иосифа Флафия.
     * Простая
     *
     * Образовав круг, стоят menNumber человек, пронумерованных от 1 до menNumber.
     *
     * 1 2 3
     * 8   4
     * 7 6 5
     *
     * Мы считаем от 1 до choiceInterval (например, до 5), начиная с 1-го человека по кругу.
     * Человек, на котором остановился счёт, выбывает.
     *
     * 1 2 3
     * 8   4
     * 7 6 х
     *
     * Далее счёт продолжается со следующего человека, также от 1 до choiceInterval.
     * Выбывшие при счёте пропускаются, и человек, на котором остановился счёт, выбывает.
     *
     * 1 х 3
     * 8   4
     * 7 6 Х
     *
     * Процедура повторяется, пока не останется один человек. Требуется вернуть его номер (в данном случае 3).
     *
     * 1 Х 3
     * х   4
     * 7 6 Х
     *
     * 1 Х 3
     * Х   4
     * х 6 Х
     *
     * х Х 3
     * Х   4
     * Х 6 Х
     *
     * Х Х 3
     * Х   х
     * Х 6 Х
     *
     * Х Х 3
     * Х   Х
     * Х х Х
     *
     * Общий комментарий: решение из Википедии для этой задачи принимается,
     * но приветствуется попытка решить её самостоятельно.
     */

    /**
     * Time Complexity  O(n) - так как требуется пройти по menNumber 1 раз
     * Resource Complexity O(1) - так как количество дополнительных переменных не зависит от объёма входных данных
     */
    static public int josephTask(int menNumber, int choiceInterval) {
        if (menNumber <= 0 || choiceInterval <= 0) {
            return 0;
        }
        if (choiceInterval == 1) {
            return menNumber;
        }
        int g = 0;
        for (int i = 0; i < menNumber; ++i) {
            g = (g + choiceInterval) % (i + 1);
        }
        return g + 1;
    }

    /**
     * Наибольшая общая подстрока.
     * Средняя
     *
     * Дано две строки, например ОБСЕРВАТОРИЯ и КОНСЕРВАТОРЫ.
     * Найти их самую длинную общую подстроку -- в примере это СЕРВАТОР.
     * Если общих подстрок нет, вернуть пустую строку.
     * При сравнении подстрок, регистр символов *имеет* значение.
     * Если имеется несколько самых длинных общих подстрок одной длины,
     * вернуть ту из них, которая встречается раньше в строке first.
     */

    /**
     * Time Complexity  O(mn), m - length of first, n - length of second - так как мы проходим по всем символам
     * входных слов вложенным циклом
     * Resource Complexity O(mn) - так как размер матрицы перекрытий зависит от длины входных слов
     */
    static public String longestCommonSubstring(String first, String second) {
        int[][] overlap = new int[first.length() + 1][second.length() + 1];
        int max = 0;
        int k = 0;
        for (int i = 0; i <= first.length(); i++) {
            for (int j = 0; j <= second.length(); j++) {
                if (i == 0 || j == 0 || first.charAt(i - 1) != second.charAt(j - 1)) {
                    overlap[i][j] = 0;
                } else {
                    overlap[i][j] = overlap[i - 1][j - 1] + 1;
                }
                if (overlap[i][j] > max) {
                    max = overlap[i][j];
                    k = i;
                }
            }
        }
        return max != 0 ? first.substring(k - max, k) : "";
    }

    /**
     * Число простых чисел в интервале
     * Простая
     *
     * Рассчитать количество простых чисел в интервале от 1 до limit (включительно).
     * Если limit <= 1, вернуть результат 0.
     *
     * Справка: простым считается число, которое делится нацело только на 1 и на себя.
     * Единица простым числом не считается.
     */

    /**
     * Time Complexity  O(n^(3/2)) - так как первый цикл проходит по всем цифрам до входного числа,
     * а вложенный в первый доходит до квадратного корня из входного числа
     * Resource Complexity O(1) - так как количество дополнительных переменных не зависит от объёма входных данных
     */
    static public int calcPrimesNumber(int limit) {
        int res = 0;
        if (limit <= 1) {
            return res;
        }
        for (int i = 1; i <= limit; i++) {
            if (isPrime(i)) {
                res++;
            }
        }
        return res;
    }

    private static boolean isPrime(int n) {
        if (n % 2 == 0) {
            return false;
        }
        for (int i = 3; i * i <= n; i += 2) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Балда
     * Сложная
     *
     * В файле с именем inputName задана матрица из букв в следующем формате
     * (отдельные буквы в ряду разделены пробелами):
     * <p>
     * И Т Ы Н
     * К Р А Н
     * А К В А
     * <p>
     * В аргументе words содержится множество слов для поиска, например,
     * ТРАВА, КРАН, АКВА, НАРТЫ, РАК.
     * <p>
     * Попытаться найти каждое из слов в матрице букв, используя правила игры БАЛДА,
     * и вернуть множество найденных слов. В данном случае:
     * ТРАВА, КРАН, АКВА, НАРТЫ
     * <p>
     * И т Ы Н     И т ы Н
     * К р а Н     К р а н
     * А К в а     А К В А
     * <p>
     * Все слова и буквы -- русские или английские, прописные.
     * В файле буквы разделены пробелами, строки -- переносами строк.
     * Остальные символы ни в файле, ни в словах не допускаются.
     */

    /**
     * Time Complexity  O(n * m * wordsSize) - так как в двух вложенных циклах мы проходим сначала по словам
     * во входном словаре(wordSize), потом по числу рядов (n) и столбцов (m)
     * Resource Complexity  O(n * m * wordsSize) - так как в методе может создаваться wordSize матриц размером m * n
     */
    static public Set<String> baldaSearcher(String inputName, Set<String> words) throws Exception {
        int lines = Files.readAllLines(Paths.get(inputName), StandardCharsets.UTF_8).size();
        char[][] boggle;
        int i;
        int j;
        try (BufferedReader inp = new BufferedReader(new FileReader(new File(inputName)))) {
            String line;
            j = (line = inp.readLine()) != null ? line.split(" ").length : 0;
            if (!line.matches("([A-ZА-ЯЁ] )+[A-ZА-ЯЁ]")) {
                throw new IllegalArgumentException();
            }
            boggle = new char[lines][j];
            String[] letters = line.split(" ");
            i = 0;
            for (int k = 0; k < j; k++) {
                boggle[i][k] = letters[k].charAt(0);
            }
            i++;
            while ((line = inp.readLine()) != null) {
                if (!line.matches("([A-ZА-ЯЁ] )+[A-ZА-ЯЁ]")) {
                    throw new IllegalArgumentException();
                }
                letters = line.split(" ");
                for (int k = 0; k < j; k++) {
                    boggle[i][k] = letters[k].charAt(0);
                }
                i++;
            }
        } catch (IndexOutOfBoundsException e) {
            throw new Exception();
        }
        Set<String> result = new HashSet<>();
        for (String word : words) {
            if (!word.matches("[A-ZА-ЯЁ]+")) {
                throw new IllegalArgumentException();
            }
            for (int k = 0; k < i; k++) {
                for (int l = 0; l < j; l++) {
                    if (boggle[k][l] == word.charAt(0)) {
                        char[][] temp = new char[i][j];
                        for (int x = 0; x < i; x++) {
                            System.arraycopy(boggle[x], 0, temp[x], 0, j);
                        }
                        if (searchWord(temp, word, k, l, 0)) {
                            result.add(word);
                        }
                    }
                }
            }
        }
        return result;
    }

    private static boolean searchWord(char[][] board, String word, int i, int j, int k) {
        if (i < 0 || j < 0 || i >= board.length || j >= board[0].length || k > word.length() - 1) {
            return false;
        }
        if (board[i][j] == word.charAt(k)) {
            char temp = board[i][j];
            board[i][j] = '0';
            if (k == word.length() - 1) {
                return true;
            } else if (searchWord(board, word, i - 1, j, k + 1) || searchWord(board, word, i + 1, j, k + 1)
                    || searchWord(board, word, i, j - 1, k + 1) || searchWord(board, word, i, j + 1, k + 1)) {
                board[i][j] = temp;
                return true;
            }
        } else {
            return false;
        }
        return false;
    }
}