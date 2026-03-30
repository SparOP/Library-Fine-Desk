CREATE DATABASE library_fine;
USE library_fine;

CREATE TABLE books (
    book_id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(200),
    author VARCHAR(100),
    copies INT
);

CREATE TABLE issues (
    issue_id INT PRIMARY KEY AUTO_INCREMENT,
    book_id INT,
    student VARCHAR(100),
    issued_on DATE DEFAULT (CURRENT_DATE),
    due_date DATE,
    returned_on DATE DEFAULT NULL,
    fine DECIMAL(8,2) DEFAULT 0,
    FOREIGN KEY (book_id) REFERENCES books(book_id)
);