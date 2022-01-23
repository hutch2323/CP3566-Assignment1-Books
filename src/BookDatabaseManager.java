import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDatabaseManager {
    static private String DB_URL = "jdbc:mariadb://localhost:3300/books";
    static private String USER = "root";
    static private String PASS = "password";
    private List<Book> bookList = new ArrayList<>();
    private List<Author> authorList = new ArrayList<>();

    public BookDatabaseManager(){
        loadBooks();
        loadAuthors();
    }

    public List<Book> getBookList() {
        return bookList;
    }

    public void setBookList(List<Book> bookList) {
        this.bookList = bookList;
    }

    public List<Author> getAuthorList() {
        return authorList;
    }

    public void setAuthorList(List<Author> authorList) {
        this.authorList = authorList;
    }

    private void loadDatabase(String query){
        // Open a connection
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query);){

            if (query.contains("titles")) {
                while (rs.next()) {
                    Book book = new Book(rs.getString("isbn"), rs.getString("title"),
                            rs.getInt("editionNumber"), rs.getString("copyright"));

                    String sql = "Select a.authorID, a.firstName, a.lastName " +
                            "from authors a join authorisbn ai " +
                            "ON a.authorID = ai.authorID " +
                            "JOIN titles t " +
                            "ON ai.isbn = t.isbn " +
                            "where t.isbn = ?";

                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, book.getIsbn());
                    ResultSet rsAuthors = pstmt.executeQuery();

                    while (rsAuthors.next()) {
                        Author author = new Author(rsAuthors.getInt("authorID"),
                                rsAuthors.getString("firstName"), rsAuthors.getString("lastName"));
                        book.getAuthorList().add(author);
                    }
                    this.getBookList().add(book);
                }
            } else {
                while (rs.next()) {
                    Author author = new Author(rs.getInt("authorID"), rs.getString("firstName"),
                            rs.getString("lastName"));

                    String sql = "Select t.isbn, t.title, t.editionNumber, t.copyright " +
                            "from titles t join authorisbn ai " +
                            "ON t.isbn = ai.isbn " +
                            "JOIN authors a " +
                            "ON ai.authorID = a.authorID " +
                            "where a.authorID = ?";

                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    pstmt.setInt(1, author.getAuthorID());
                    ResultSet rsBooks = pstmt.executeQuery();

                    while (rsBooks.next()) {
                        Book book = new Book(rsBooks.getString("isbn"), rsBooks.getString("title"),
                                rsBooks.getInt("editionNumber"), rsBooks.getString("copyright"));
                        author.getBookList().add(book);
                    }
                    this.getAuthorList().add(author);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadBooks(){
        String querySQL = "Select * from titles";
        loadDatabase(querySQL);
    }

    public void loadAuthors(){
        String querySQL = "Select * from authors";
        loadDatabase(querySQL);
    }

    public void addNewBook(Book book){
        String SQLBooks = "INSERT into titles (isbn, title, editionNumber, copyright)" +
                "Values (?, ?, ?, ?)";
        String SQLAuthorISBN = "INSERT into authorISBN (authorID, isbn)" +
                "Values (?, ?)";
        PreparedStatement pstmt = null;
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)){
            pstmt = conn.prepareStatement(SQLBooks);
            pstmt.setString(1, book.getIsbn());
            pstmt.setString(2, book.getTitle());
            pstmt.setInt(3, book.getEditionNumber());
            pstmt.setString(4, book.getCopyright());
            pstmt.execute();

            pstmt = conn.prepareStatement(SQLAuthorISBN);
            pstmt.setString(2, book.getIsbn());
            for(Author author : book.getAuthorList()){
                pstmt.setInt(1, author.getAuthorID());
                pstmt.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.getBookList().clear();
        loadBooks();
    }

    public void addNewAuthor(Author author){
        String SQLAuthors= "INSERT into authors (authorID, firstName, lastName)" +
                "Values (?, ?, ?)";
        String SQLAuthorISBN = "INSERT into authorISBN (authorID, isbn)" +
                "Values (?, ?)";
        PreparedStatement pstmt = null;
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)){
            pstmt = conn.prepareStatement(SQLAuthors);
            pstmt.setInt(1, author.getAuthorID());
            pstmt.setString(2, author.getFirstName());
            pstmt.setString(3, author.getLastName());
            pstmt.execute();

            pstmt = conn.prepareStatement(SQLAuthorISBN);
            pstmt.setInt(1, author.getAuthorID());
            for(Book book : author.getBookList()){
                pstmt.setString(2, book.getIsbn());
                pstmt.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.getAuthorList().clear();
        loadAuthors();
    }
}