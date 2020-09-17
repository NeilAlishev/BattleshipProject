package bigProject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static bigProject.Util.*;

/**
 * @author Neil Alishev
 */
public class GameField {
    private String playerName;

    // корабль: 1
    // ореол корабля: 0
    // пустое пространство: -1
    // атакованная клетка: -2
    private int[][] playerField;

    // выставляется true тогда, когда до конца отрабатывает метод arrangePlayerField()
    private boolean playerFieldArranged;

    // инициализируется поле с пустым пространством (-1)
    public GameField(String playerName) {
        this.playerName = playerName;

        this.playerField = new int[10][10];

        // инициализируем матрицу значением -1 (пустое пространство)
        for (int[] row : this.playerField)
            Arrays.fill(row, -1);
    }

    public String getPlayerName() {
        return playerName;
    }

    public int[][] getPlayerField() {
        return playerField;
    }

    public boolean isPlayerFieldArranged() {
        return playerFieldArranged;
    }

    // Расставляет все корабли для этого поля
    public void arrangePlayerField() {
        if (playerFieldArranged) {
            System.out.println("Поле игрока " + playerName + " уже готово");
            return;
        }

        System.out.println("Начнем расставлять корабли на поле " + playerName + ". Другой игрок, не смотри!");

        Scanner scanner = new Scanner(System.in);

        System.out.println("Введи координаты четырёхпалубного корабля (формат: x,y;x,y;x,y;x,y)");

        String userInput = scanner.nextLine();

        while (!arrangeShip(userInput, 4))
            userInput = scanner.nextLine();

        System.out.println("Введи координаты первого трехпалубного корабля (формат: x,y;x,y;x,y)");
        userInput = scanner.nextLine();

        while (!arrangeShip(userInput, 3))
            userInput = scanner.nextLine();

        System.out.println("Введи координаты второго трехпалубного корабля (формат: x,y;x,y;x,y)");
        userInput = scanner.nextLine();

        while (!arrangeShip(userInput, 3))
            userInput = scanner.nextLine();

        System.out.println("Введи координаты первого двухпалубного корабля (формат: x,y;x,y)");
        userInput = scanner.nextLine();

        while (!arrangeShip(userInput, 2))
            userInput = scanner.nextLine();

        System.out.println("Введи координаты второго двухпалубного корабля (формат: x,y;x,y)");
        userInput = scanner.nextLine();

        while (!arrangeShip(userInput, 2))
            userInput = scanner.nextLine();

        System.out.println("Введи координаты третьего двухпалубного корабля (формат: x,y;x,y)");
        userInput = scanner.nextLine();

        while (!arrangeShip(userInput, 2))
            userInput = scanner.nextLine();

        System.out.println("Введи координаты первого однопалубного корабля (формат: x,y)");
        userInput = scanner.nextLine();

        while (!arrangeShip(userInput, 1))
            userInput = scanner.nextLine();

        System.out.println("Введи координаты второго однопалубного корабля (формат: x,y)");
        userInput = scanner.nextLine();

        while (!arrangeShip(userInput, 1))
            userInput = scanner.nextLine();

        System.out.println("Введи координаты третьего однопалубного корабля (формат: x,y)");
        userInput = scanner.nextLine();

        while (!arrangeShip(userInput, 1))
            userInput = scanner.nextLine();

        System.out.println("Введи координаты четвертого однопалубного корабля (формат: x,y)");
        userInput = scanner.nextLine();

        while (!arrangeShip(userInput, 1))
            userInput = scanner.nextLine();

        playerFieldArranged = true;
    }

    // Выводит поле на экран
    public void printField() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (playerField[i][j] == 1)
                    System.out.print("\uD83D\uDEE5️");
                else if (playerField[i][j] == 0)
                    System.out.print("\uD83D\uDFE6");
                else if (playerField[i][j] == -2)
                    System.out.print("\uD83D\uDFE5");
                else
                    System.out.print("⬜");
            }

            System.out.println();
        }
    }

    // Пытается поставить на поле новый корабль, принимая на вход ввод из консоли
    // Вовзращает true, если корабль удалось поставить и false, если не удалось (не прошли все проверки, описанные в задании)
    private boolean arrangeShip(String userInput, int shipSize) {
        if (!checkCoordinates(userInput, shipSize))
            return false;

        int[][] shipCoordinates = parseShipCoordinates(userInput, shipSize);

        if (!checkShip(shipCoordinates, shipSize)) {
            System.out.println("Ваш корабль не валиден. " +
                    "Валидный корабль - это одна или несколько последовательно идущих клеток (по вертикали или горизонтали)");
            return false;
        }

        if (!arrangementPossible(shipCoordinates, shipSize)) {
            System.out.println("Корабль должен занимать только свободное пространство на карте. " +
                    "Помимо этого, Вокруг корабля должна быть область шириной в одну клетку, " +
                    "в которой не может быть других кораблей (ореол корабля)");
            return false;
        }

        // all checks done, arrange ship on the map (with 1s and 0s)
        arrangeShip(shipCoordinates, shipSize);

        return true;
    }

    // этот метод вызывается в методе arrangeShip(String userInput, int shipSize) после того,
    // как прошли проверки на все возможные условия
    // этот метод непосредственно наносит на поле сам корабль и его ореол
    // (меняет значения в двумерном массиве)
    // 1 - сам корабль. 0 - ореол этого корабля
    private void arrangeShip(int[][] shipCoordinates, int shipSize) {
        // arrange ship
        for (int[] shipCoordinate : shipCoordinates)
            playerField[shipCoordinate[0]][shipCoordinate[1]] = 1;

        // arrange aureole
        List<Integer[]> shipAureole = getShipAureole(shipCoordinates, shipSize);

        for (Integer[] shipAureoleCoordinate : shipAureole)
            playerField[shipAureoleCoordinate[0]][shipAureoleCoordinate[1]] = 0;
    }

    // проверяет, что валидный корабль можно поставить на поле
    // проверяет, что:
    // 1) Сам корабль занимает свободное пространство на поле
    // 2) В ореоле этого корабля нет кораблей
    private boolean arrangementPossible(int[][] shipCoordinates, int shipSize) {
        // проверяем пространство для самого корабля
        for (int[] shipCoordinate : shipCoordinates) {
            if (playerField[shipCoordinate[0]][shipCoordinate[1]] == 1)
                return false;
        }

        // проверяем простанство ареола корабля
        List<Integer[]> shipAureole = getShipAureole(shipCoordinates, shipSize);

        for (Integer[] shipAureoleCoordinate : shipAureole) {
            if (playerField[shipAureoleCoordinate[0]][shipAureoleCoordinate[1]] == 1)
                return false;
        }

        return true;
    }

    // Возвращает координаты ореола корабля
    private List<Integer[]> getShipAureole(int[][] shipCoordinates, int shipSize) {
        List<Integer[]> shipAureole = new ArrayList<>();

        // Определяем расположение корабля (вертикально или горизонтально)
        boolean vertical = verticalOrHorizontal(shipCoordinates);

        if (vertical) {
            // добавляем правый борт
            if (shipCoordinates[0][1] + 1 <= 9) {
                for (int[] shipCoordinate : shipCoordinates)
                    shipAureole.add(new Integer[]{shipCoordinate[0], shipCoordinate[1] + 1});

                // добавляем правую верхнюю клетку
                if (shipCoordinates[0][0] - 1 >= 0)
                    shipAureole.add(new Integer[]{shipCoordinates[0][0] - 1, shipCoordinates[0][1] + 1});

                // добавляем правую нижнюю клетку
                if (shipCoordinates[shipSize - 1][0] + 1 <= 9) {
                    shipAureole.add(new Integer[]{shipCoordinates[shipSize - 1][0] + 1,
                            shipCoordinates[shipSize - 1][1] + 1});
                }
            }

            // добавляем левый борт
            if (shipCoordinates[0][1] - 1 >= 0) {
                for (int[] shipCoordinate : shipCoordinates)
                    shipAureole.add(new Integer[]{shipCoordinate[0], shipCoordinate[1] - 1});

                // добавляем левую верхнюю клетку
                if (shipCoordinates[0][0] - 1 >= 0)
                    shipAureole.add(new Integer[]{shipCoordinates[0][0] - 1, shipCoordinates[0][1] - 1});

                // добавляем левую нижнюю клетку
                if (shipCoordinates[shipSize - 1][0] + 1 <= 9)
                    shipAureole.add(new Integer[]{shipCoordinates[shipSize - 1][0] + 1, shipCoordinates[shipSize - 1][1] - 1});
            }

            // добавляем верхнюю клетку
            if (shipCoordinates[0][0] - 1 >= 0)
                shipAureole.add(new Integer[]{shipCoordinates[0][0] - 1, shipCoordinates[0][1]});

            // добавляем нижнюю клетку
            if (shipCoordinates[shipSize - 1][0] + 1 <= 9)
                shipAureole.add(new Integer[]{shipCoordinates[shipSize - 1][0] + 1, shipCoordinates[shipSize - 1][1]});

        } else {
            // добавляем верхний борт
            if (shipCoordinates[0][0] - 1 >= 0) {
                for (int[] shipCoordinate : shipCoordinates)
                    shipAureole.add(new Integer[]{shipCoordinate[0] - 1, shipCoordinate[1]});

                // добавляем правую верхнюю клетку
                if (shipCoordinates[shipSize - 1][1] + 1 <= 9)
                    shipAureole.add(new Integer[]{shipCoordinates[shipSize - 1][0] - 1, shipCoordinates[shipSize - 1][1] + 1});

                // добавляем левую верхнюю клетку
                if (shipCoordinates[0][1] - 1 >= 0)
                    shipAureole.add(new Integer[]{shipCoordinates[0][0] - 1, shipCoordinates[0][1] - 1});
            }

            // добавляем нижний борт
            if (shipCoordinates[0][0] + 1 <= 9) {
                for (int[] shipCoordinate : shipCoordinates)
                    shipAureole.add(new Integer[]{shipCoordinate[0] + 1, shipCoordinate[1]});

                // добавляем левую нижнюю клетку
                if (shipCoordinates[0][1] - 1 >= 0)
                    shipAureole.add(new Integer[]{shipCoordinates[0][0] + 1, shipCoordinates[0][1] - 1});

                // добавляем правую нижнюю клетку
                if (shipCoordinates[shipSize - 1][1] + 1 <= 9)
                    shipAureole.add(new Integer[]{shipCoordinates[shipSize - 1][0] + 1, shipCoordinates[shipSize - 1][1] + 1});
            }

            // добавляем левую клетку
            if (shipCoordinates[0][1] - 1 >= 0)
                shipAureole.add(new Integer[]{shipCoordinates[0][0], shipCoordinates[0][1] - 1});

            // добавляем правую клетку
            if (shipCoordinates[shipSize - 1][1] + 1 <= 9)
                shipAureole.add(new Integer[]{shipCoordinates[shipSize - 1][0], shipCoordinates[shipSize - 1][1] + 1});
        }

        return shipAureole;
    }
}
