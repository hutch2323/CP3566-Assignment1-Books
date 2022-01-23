import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class BookApplication {
    public static void main(String[] args) throws SQLException {
        BookDatabaseManager booksDB = new BookDatabaseManager();
        Scanner inputDevice = new Scanner(System.in);

        while(true){
            displayOptionsMenu();
            int userChoice = getUserSelection(inputDevice);
            if (userChoice < 1 || userChoice > 5){
                System.err.print("Invalid selection. Please choose the number associated with the desired option. Try again!\n");
                continue;
            }

            switch (userChoice){
                case 1:
                    for(Book book : booksDB.getBookList()){
                        System.out.println("Title: " + book.getTitle());
                        System.out.println("ISBN: " + book.getIsbn());
                        System.out.println("Edition: " + book.getEditionNumber());
                        System.out.println("Copyright: " + book.getCopyright());
                        String authorString = "";
                        int count = 0;
                        for (Author author : book.getAuthorList()) {
                            if (book.getAuthorList().size() == 1){
                                authorString += author.getFirstName() + " " + author.getLastName();
                            } else if ((count + 1) == book.getAuthorList().size()) {
                                authorString += "and " + author.getFirstName() + " " + author.getLastName();
                            } else if (book.getAuthorList().size() == 2) {
                                authorString += author.getFirstName() + " " + author.getLastName() + " ";
                            } else {
                                authorString += author.getFirstName() + " " + author.getLastName() + ", ";
                            }
                            count++;
                        }
                        System.out.println("Authors: " + authorString + "\n");
                    }
                    break;
                case 2:
                    for(Author author : booksDB.getAuthorList()){
                        System.out.println("Author ID: " + author.getAuthorID());
                        System.out.println("First Name: " + author.getFirstName());
                        System.out.println("Last Name: " + author.getLastName());
                        String bookString = "";
                        int count = 0;
                        for (Book book : author.getBookList()) {
                            if (author.getBookList().size() == 1){
                                bookString += book.getTitle();
                            }else if ((count + 1) == author.getBookList().size()) {
                                bookString += "and " + book.getTitle();
                            } else if (book.getAuthorList().size() == 2) {
                                bookString += book.getTitle() + " ";
                            } else {
                                bookString += book.getTitle() + ", ";
                            }
                            count++;
                        }
                        System.out.println("Books: " + bookString + "\n");
                    }
                    break;
                case 3:
                    inputDevice.nextLine();
                    System.out.println("\nISBN: ");
                    String isbn = inputDevice.nextLine();
                    System.out.println("Title: ");
                    String title = inputDevice.nextLine();
                    System.out.println("Edition: ");
                    int edition = inputDevice.nextInt();
                    inputDevice.nextLine();
                    System.out.println("Copyright: ");
                    String copyright = inputDevice.nextLine();

                    Book book = new Book(isbn, title, edition, copyright);
                    System.out.println("Number of Authors: ");
                    int numAuthors = inputDevice.nextInt();

                    for(int i = 0; i < numAuthors; i++){
                        System.out.println("\nAuthor ID: ");
                        int authorID = inputDevice.nextInt();
//                        inputDevice.nextLine();
//                        System.out.println("First Name: ");
//                        String firstName = inputDevice.nextLine();
//                        System.out.println("Last Name: ");
//                        String lastName = inputDevice.nextLine();

//                        System.out.println("Author info: " + authorID + " " + firstName + " " + lastName);
                        Author author = new Author(authorID, "", "");
                        book.getAuthorList().add(author);
                    }

//                    System.out.println(isbn + "\n" + title + "\n" + edition  + "\n" + copyright);
                    booksDB.addNewBook(book);

                    break;
                case 4:
                    System.out.println("\nAuthor ID: ");
                    int authorID = inputDevice.nextInt();
                    inputDevice.nextLine();
                    System.out.println("First Name: ");
                    String firstName = inputDevice.nextLine();
                    System.out.println("Last Name: ");
                    String lastName = inputDevice.nextLine();

                    Author newAuthor = new Author(authorID, firstName, lastName);
                    System.out.println("Number of Books Written: ");
                    int numBooks = inputDevice.nextInt();
                    inputDevice.nextLine();

                    for(int i = 0; i < numBooks; i++){
                        System.out.println("\nISBN: ");
                        String bookIsbn = inputDevice.nextLine();
//                        System.out.println("Title: ");
//                        String title = inputDevice.nextLine();
//                        System.out.println("Edition: ");
//                        int edition = inputDevice.nextInt();
//                        inputDevice.nextLine();
//                        System.out.println("Copyright: ");
//                        String copyright = inputDevice.nextLine();

                        Book newBook = new Book(bookIsbn, "", 0, "");
                        newAuthor.getBookList().add(newBook);
                    }

//                    System.out.println(isbn + "\n" + title + "\n" + edition  + "\n" + copyright);
                    booksDB.addNewAuthor(newAuthor);
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
            "\n5. Quit\n");
    }

    // method that handles getting the user's selection from the menu options. Used for main menu as well as transfer menu.
    public static int getUserSelection(Scanner inputDevice){
        while (true) {
            try {
                System.out.print("Selection: ");
                return inputDevice.nextInt();
            } catch (InputMismatchException e) {
                inputDevice.nextLine();
                System.err.print("Invalid selection. Please choose the number associated with the desired option. Try again!\n");
            }
        }
    }
}
