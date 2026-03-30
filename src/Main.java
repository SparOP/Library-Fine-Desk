import java.sql.*;
import java.util.Scanner;

public class Main {

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        while (true) {
            System.out.println("\n==== Library Fine System ====");
            System.out.println("1. Add Book");
            System.out.println("2. Issue Book");
            System.out.println("3. Return Book");
            System.out.println("4. Overdue List");
            System.out.println("5. Exit");
            System.out.print("Enter choice: ");

            int ch = sc.nextInt();

            switch (ch) {
                case 1: addBook(); break;
                case 2: issueBook(); break;
                case 3: returnBook(); break;
                case 4: overdueList(); break;
                case 5: System.exit(0);
                default: System.out.println("Invalid choice!");
            }
        }
    }

    // ✅ Add Book
    static void addBook() {
        try (Connection conn = DBConnection.getConnection()) {

            sc.nextLine();
            System.out.print("Title: ");
            String title = sc.nextLine();

            System.out.print("Author: ");
            String author = sc.nextLine();

            System.out.print("Copies: ");
            int copies = sc.nextInt();

            String query = "INSERT INTO books (title, author, copies) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, title);
            ps.setString(2, author);
            ps.setInt(3, copies);

            ps.executeUpdate();
            System.out.println("Book added!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ✅ Issue Book
    static void issueBook() {
        try (Connection conn = DBConnection.getConnection()) {

            System.out.print("Enter Book ID: ");
            int id = sc.nextInt();

            sc.nextLine();
            System.out.print("Student Name: ");
            String student = sc.nextLine();

            String check = "SELECT copies FROM books WHERE book_id=?";
            PreparedStatement ps1 = conn.prepareStatement(check);
            ps1.setInt(1, id);
            ResultSet rs = ps1.executeQuery();

            if (rs.next() && rs.getInt("copies") > 0) {

                String issue = "INSERT INTO issues (book_id, student, due_date) VALUES (?, ?, DATE_ADD(CURDATE(), INTERVAL 14 DAY))";
                PreparedStatement ps2 = conn.prepareStatement(issue);
                ps2.setInt(1, id);
                ps2.setString(2, student);
                ps2.executeUpdate();

                String update = "UPDATE books SET copies = copies - 1 WHERE book_id=?";
                PreparedStatement ps3 = conn.prepareStatement(update);
                ps3.setInt(1, id);
                ps3.executeUpdate();

                System.out.println("Book issued!");

            } else {
                System.out.println("Book not available!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ✅ Return Book
    static void returnBook() {
        try (Connection conn = DBConnection.getConnection()) {

            System.out.print("Enter Issue ID: ");
            int issueId = sc.nextInt();

            String get = "SELECT book_id, due_date FROM issues WHERE issue_id=?";
            PreparedStatement ps1 = conn.prepareStatement(get);
            ps1.setInt(1, issueId);
            ResultSet rs = ps1.executeQuery();

            if (rs.next()) {

                int bookId = rs.getInt("book_id");

                String fineQuery = "SELECT GREATEST(DATEDIFF(CURDATE(), due_date),0)*2 AS fine FROM issues WHERE issue_id=?";
                PreparedStatement ps2 = conn.prepareStatement(fineQuery);
                ps2.setInt(1, issueId);
                ResultSet rs2 = ps2.executeQuery();

                rs2.next();
                double fine = rs2.getDouble("fine");

                String update = "UPDATE issues SET returned_on=CURDATE(), fine=? WHERE issue_id=?";
                PreparedStatement ps3 = conn.prepareStatement(update);
                ps3.setDouble(1, fine);
                ps3.setInt(2, issueId);
                ps3.executeUpdate();

                String restore = "UPDATE books SET copies = copies + 1 WHERE book_id=?";
                PreparedStatement ps4 = conn.prepareStatement(restore);
                ps4.setInt(1, bookId);
                ps4.executeUpdate();

                System.out.println("Returned! Fine = Rs " + fine);

            } else {
                System.out.println("Invalid Issue ID!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ✅ Overdue List
    static void overdueList() {
        try (Connection conn = DBConnection.getConnection()) {

            String query = "SELECT * FROM issues WHERE returned_on IS NULL AND due_date < CURDATE()";
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                System.out.println("Issue ID: " + rs.getInt("issue_id") +
                        " | Student: " + rs.getString("student") +
                        " | Due Date: " + rs.getDate("due_date"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}