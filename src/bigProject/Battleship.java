package bigProject;

import java.util.Scanner;

import static bigProject.Util.*;

/**
 * @author Neil Alishev
 */
public class Battleship {
    // поле первого игрока
    private GameField player1Field;
    // поле второго игрока
    private GameField player2Field;

    // true, пока игра идет. false, когда игра заканчивается
    private boolean gameIsOn;

    // кто сейчас ходит (true - player1, false - player2)
    private boolean isPlayer1;

    // количество кораблей на плаву игрока 1
    private int player1ShipCount;
    // количество кораблей на плаву игрока 2
    private int player2ShipCount;

    // единственный конструктор
    // внутри проверяется, что оба поля заполнены
    // также, инициализируются значения всех полей
    public Battleship(GameField player1Field, GameField player2Field) {
        if (!player1Field.isPlayerFieldArranged() || !player2Field.isPlayerFieldArranged()) {
            System.out.println("Создание игры остановлено. Корабли на обоих полях должны быть расставлены.");
            throw new IllegalArgumentException();
        }

        this.player1Field = player1Field;
        this.player2Field = player2Field;

        this.player1ShipCount = 10;
        this.player2ShipCount = 10;

        gameIsOn = true;

        // who makes the first move
        isPlayer1 = Math.random() >= 0.5;
    }

    // запускает цикл игры пока gameIsOn = true
    public void play() {
        Scanner scanner = new Scanner(System.in);

        while (gameIsOn) {
            if (isPlayer1)
                System.out.println(player1Field.getPlayerName() + ", ваш ход!");
            else
                System.out.println(player2Field.getPlayerName() + ", ваш ход!");

            String userInput = scanner.nextLine();

            while (!makeMove(isPlayer1, userInput))
                System.out.println("Ваш ход невалиден. Повторите ход.");
        }
    }

    // возвращает true, если userInput - валидный ход. Возвращает false, если userInput - невалидный ход
    // первый аргумент true если это ход первого игрока, false - если это ход второго игрока
    private boolean makeMove(boolean isPlayer1, String userInput) {
        if (!checkCoordinate(userInput))
            return false;

        int[] coordinate = parseCoordinate(userInput);

        if (isPlayer1)
            hit(player2Field.getPlayerField(), coordinate);
        else
            hit(player1Field.getPlayerField(), coordinate);


        if (player1ShipCount == 0) {
            System.out.println(player2Field.getPlayerName() + " победил! Игра заканчивается");
            gameIsOn = false;
        }

        if (player2ShipCount == 0) {
            System.out.println(player1Field.getPlayerName() + " победил! Игра заканчивается");
            gameIsOn = false;
        }

        return true;
    }

    // Производит удар по ячейке
    // Выводит сообщение либо "Мимо!", либо "Попадание", либо "Утопил".
    // В случае потопления декременитирует количество кораблей на плаву
    // Переводит право на ход другому игроку, если удар был "Мимо!"
    private void hit(int[][] playerField, int[] hitCoordinate) {

        if (playerField[hitCoordinate[0]][hitCoordinate[1]] == 1) {
            playerField[hitCoordinate[0]][hitCoordinate[1]] = -2;

            if (shipSank(playerField, hitCoordinate)) {
                System.out.println("Утопил!");

                if (isPlayer1)
                    player2ShipCount--;
                else
                    player1ShipCount--;
            } else {
                System.out.println("Попадание!");
            }

        } else {
            System.out.println("Мимо!");
            isPlayer1 = !isPlayer1; // переход хода
        }
    }

    // true - если удар утопил корабль
    // false - если удар не утопил корабль
    private boolean shipSank(int[][] playerField, int[] hitCoordinate) {
        // идем вверх - вниз и вправо-влево пока не упремся в ореол.
        // проверяем, есть ли 1
        int x = hitCoordinate[0];
        int y = hitCoordinate[1];

        while (playerField[x][y] != 0) {
            x -= 1;
            if (playerField[x][y] == 1)
                return false;
        }

        x = hitCoordinate[0];
        while (playerField[x][y] != 0) {
            x += 1;
            if (playerField[x][y] == 1)
                return false;
        }

        x = hitCoordinate[0];
        while (playerField[x][y] != 0) {
            y -= 1;
            if (playerField[x][y] == 1)
                return false;
        }

        y = hitCoordinate[0];
        while (playerField[x][y] != 0) {
            y += 1;
            if (playerField[x][y] == 1)
                return false;
        }

        return true;
    }
}

