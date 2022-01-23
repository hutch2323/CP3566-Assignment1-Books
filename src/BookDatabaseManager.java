import java.sql.*;
import java.util.List;

public class BookDatabaseManager {
    static private String DB_URL = "jdbc:mariadb://localhost:3300/books";
    static private String USER = "root";
    static private String PASS = "password";
    private List<Book> bookList;
    private List<Author> authorList;

    public BookDatabaseManager(){

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

    private ResultSet loadDatabase(String sqlQuery){
        // Open a connection
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sqlQuery);) {
            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void loadBooks() throws SQLException {
        String querySQL = "Select * from titles";
        ResultSet rs = loadDatabase(querySQL);
        while (rs.next()) {
            Book book = new Book(rs.getString("isbn"), rs.getString("title"),
                    rs.getInt("editionNumber"), rs.getString("copyright"));
            this.getBookList().add(book);
        }
    }

    public void loadAuthors() throws SQLException {
        String querySQL = "Select * from authors";
        ResultSet rs = loadDatabase(querySQL);
        while (rs.next()) {
            Author author = new Author(rs.getInt("authorID"), rs.getString("firstName"),
                    rs.getString("lastName"));
            this.getAuthorList().add(author);
        }
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
    }
}