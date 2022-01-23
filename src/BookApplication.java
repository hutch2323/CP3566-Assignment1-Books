import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class BookApplication {
    public static void main(String[] args) throws SQLException {
        BookDatabaseManager booksDB = new BookDatabaseManager();
        Scanner inputDevice = new Scanner(System.in);
        displayOptionsMenu();
        while(true){
            int userChoice = getUserSelection(inputDevice);
            if (userChoice < 1 || userChoice > 5){
                System.err.print("Invalid selection. Please choose the number associated with the desired option. Try again!\n");
                continue;
            }

            switch (userChoice){
                case 1:
                    booksDB.loadBooks();
                    break;
                case 2:
                    booksDB.loadAuthors();
                    break;
                case 3:
                    booksDB.addNewBook();
                    break;
                case 4:
                    booksDB.addNewBook();
                    break;
                case 5:
                    System.out.println("Thanks for using the Books Database. Goodbye!");
                    break;
            }

            if (userChoice == 5){
                break;
            }
        }
    }

    public static void displayOptionsMenu(){
        System.out.println("Books Database Options Menu" +
            "\n1. Print all the books" +
            "\n2. Print all the authors" +
            "\n3. Add a new book" +
            "\n4. Add a new author" +
            "\n5. Quit");
    }

    // method that handles getting the user's selection from the menu options. Used for main menu as well as transfer menu.
    public static int getUserSelection(Scanner inputDevice){
        while (true) {
            try {
                System.out.print("\nSelection: ");
                return inputDevice.nextInt();
            } catch (InputMismatchException e) {
                inputDevice.nextLine();
                System.err.print("Invalid selection. Please choose the number associated with the desired option. Try again!\n");
            }
        }
    }
}
