📚 Library Book Issue & Fine System

A console-based Java application using JDBC and MySQL to manage library operations and automate fine calculation.

🚀 Features

* Add books to library
* Issue books with availability check
* Automatic due date (14 days from issue)
* Return books with fine calculation (₹2 per day overdue)
* View overdue books with fine amount

🛠️ Tech Stack

* Core Java (Console Application)
* MySQL Database (MySQL 8.0.44)
* JDBC (MySQL Connector J)

▶️ Setup Instructions

1. Create database:

   CREATE DATABASE library_fine;
   USE library_fine;
   
2. Run `schema.sql`
3. Place MySQL connector `.jar` in `src/`
4. Compile and run:

   javac -cp ".;mysql-connector-j-9.6.0.jar" *.java
   java -cp ".;mysql-connector-j-9.6.0.jar" Main

🔐 Note

* Database credentials are not included for security. Set password accordingly as per the users' own passwords.
* Overdue Simulation Query (run after issuing second book):

   UPDATE issues
   SET due_date = CURDATE() - INTERVAL 5 DAY
   WHERE issue_id = 2;

📂 Project Structure

* `src/` → Java source files
* `sql/` → database schema
* `screenshots/` → output proof