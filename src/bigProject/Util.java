package bigProject;

/**
 * @author Neil Alishev
 * <p>
 * Collection of useful static methods
 */
// Класс с полезными статическими методами
public class Util {
    // Парсит одну координату (x,y)
    public static int[] parseCoordinate(String inputCoordinate) {
        String[] shipPartCoordinate = inputCoordinate.split(",");

        return new int[]{Integer.parseInt(shipPartCoordinate[0]),
                Integer.parseInt(shipPartCoordinate[1])};
    }

    // Парсит координаты корабля (x1,y1;xn,yn)
    public static int[][] parseShipCoordinates(String shipCoordinates, int shipSize) {
        int[][] intCoordinates = new int[shipSize][2];

        String[] shipParts = shipCoordinates.split(";");

        for (int i = 0; i < shipParts.length; i++)
            intCoordinates[i] = parseCoordinate(shipParts[i]);

        return intCoordinates;
    }

    // Проверяет координату x,y на валидность
    public static boolean checkCoordinate(String inputCoordinate) {
        String[] xy = inputCoordinate.split(",");

        if (xy.length != 2) {
            System.out.println("В каждой из координат должно быть два значения, разделенных запятой");
            return false;
        }

        try {
            Integer.parseInt(xy[0]);
            Integer.parseInt(xy[1]);
        } catch (NumberFormatException e) {
            System.out.println("В качестве координат можно вводить только целые числа");
            return false;
        }

        if (Integer.parseInt(xy[0]) > 9 || Integer.parseInt(xy[0]) < 0
                || Integer.parseInt(xy[1]) > 9 || Integer.parseInt(xy[1]) < 0) {

            System.out.println("Координата может быть только в диапазоне 0...9");
            return false;
        }

        return true;
    }

    // проверяет координаты корабля на валидность
    // (проверяет, что количество координат соответствует размеру корабля и проверяет отдельно каждую
    // координату x,y на валидность)
    public static boolean checkCoordinates(String userInput, int correctNumberOfCoordinates) {
        String[] inputCoordinates = userInput.split(";");

        if (inputCoordinates.length != correctNumberOfCoordinates) {
            System.out.println("Недостаточное количество координат. Необходимо " + correctNumberOfCoordinates);
            return false;
        }

        for (String coordinate : inputCoordinates) {
            if (!checkCoordinate(coordinate))
                return false;
        }

        return true;
    }

    // Проверяет, что координаты - это валидный корабль
    // для корректного корабля одна координата всегда должна быть одинаковой
    // а вторая координата должна увеличиваться на единицу
    public static boolean checkShip(int[][] shipCoordinates, int shipSize) {
        if (shipSize == 1)
            return true;

        // массивы только X и только Y для простоты следующих проверок
        int[] onlyX = new int[shipSize];
        int[] onlyY = new int[shipSize];

        for (int i = 0; i < shipSize; i++) {
            onlyX[i] = shipCoordinates[i][0];
            onlyY[i] = shipCoordinates[i][1];
        }

        // проверка на одну координату константу
        if (!allValuesEqual(onlyX) && !allValuesEqual(onlyY))
            return false;

        // проверка на одну возрастающую на единицу координату
        return allValuesAscending(onlyX) || allValuesAscending(onlyY);
    }

    // Проверяет, что в массиве все значения одинаковые (используется в методе checkShip)
    private static boolean allValuesEqual(int[] array) {
        for (int i = 1; i < array.length; i++) {
            if (array[i] != array[0])
                return false;
        }

        return true;
    }

    // Проверяет, что в массиве значения увеличиваются на единицу (используется в методе checkShip)
    private static boolean allValuesAscending(int[] array) {
        for (int i = 0; i < array.length - 1; i++) {
            if (array[i + 1] - array[i] != 1)
                return false;
        }

        return true;
    }

    // возвращает true, если корабль расположен вертикально и false, если горизонтально
    public static boolean verticalOrHorizontal(int[][] shipCoordinates) {
        // массив только X для простоты следующих проверок
        int[] onlyX = new int[shipCoordinates.length];

        for (int i = 0; i < shipCoordinates.length; i++)
            onlyX[i] = shipCoordinates[i][0];

        if (allValuesAscending(onlyX))
            return true;
        else
            return false;
    }
}
