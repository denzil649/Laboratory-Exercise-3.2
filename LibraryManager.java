package lab_act_32;
import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Stack;
import java.util.Comparator;

public class LibraryManager extends JFrame {
    private JTextField titleField;
    private JTextField authorField;
    private JTextField indexField;
    private JTextArea displayArea;
    
    private ArrayList<Book> books = new ArrayList<>();
    
    private Stack<Book> undoStack = new Stack<>();
    private Stack<Book> redoStack = new Stack<>();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LibraryManager().setVisible(true));
    }

    public LibraryManager() {
        setTitle("Library Management System");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setLayout(null);
        
        JLabel titleLabel = new JLabel("Title:");
        titleLabel.setBounds(10, 10, 50, 25);
        getContentPane().add(titleLabel);
        
        titleField = new JTextField();
        titleField.setBounds(70, 10, 150, 25);
        getContentPane().add(titleField);
        
        JLabel authorLabel = new JLabel("Author:");
        authorLabel.setBounds(10, 50, 50, 25);
        getContentPane().add(authorLabel);
        
        authorField = new JTextField();
        authorField.setBounds(70, 50, 150, 25);
        getContentPane().add(authorField);
        
        JLabel indexLabel = new JLabel("Index:");
        indexLabel.setBounds(230, 10, 50, 25);
        getContentPane().add(indexLabel);
        
        indexField = new JTextField();
        indexField.setBounds(290, 10, 179, 25);
        getContentPane().add(indexField);
        
        JButton insertButton = new JButton("Insert");
        insertButton.setBounds(490, 10, 80, 25);
        getContentPane().add(insertButton);
        
        JButton sortButton = new JButton("Sort");
        sortButton.setBounds(490, 45, 80, 25);
        getContentPane().add(sortButton);
        
        JButton undoButton = new JButton("Undo");
        undoButton.setBounds(490, 80, 80, 25);
        getContentPane().add(undoButton);
        
        JButton redoButton = new JButton("Redo");
        redoButton.setBounds(490, 115, 80, 25);
        getContentPane().add(redoButton);
        
        displayArea = new JTextArea();
        displayArea.setBounds(10, 150, 560, 200);
        displayArea.setEditable(false);
        getContentPane().add(displayArea);
        
        insertButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                insertBook();
            }
        });
        
        sortButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sortBooks();
                displayBooks();
            }
        });
        
        undoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                undoAction();
            }
        });
        
        redoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                redoAction();
            }
        });
    }
 
    class Book {
        String title;
        String author;
        int index;

        Book(String title, String author, int index) {
            this.title = title;
            this.author = author;
            this.index = index;
        }

        public String toString() {
            return "Index: " + index + ", Title: " + title + ", Author: " + author;
        }
    }

    private void insertBook() {
        String title = titleField.getText();
        String author = authorField.getText();
        int index;
        
        try {
            index = Integer.parseInt(indexField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number for index.");
            return;
        }
        
        Book newBook = new Book(title, author, index);
        books.add(newBook);
        undoStack.push(newBook); 
        redoStack.clear(); 
        displayBooks();
    }
    
    private void displayBooks() {
        displayArea.setText("");
        for (Book book : books) {
            displayArea.append(book.toString() + "\n");
        }
    }
    
    private void sortBooks() {
        heapSort(books);
    }
    
    private void heapSort(ArrayList<Book> list) {
        int n = list.size();

        for (int i = n / 2 - 1; i >= 0; i--)
            heapify(list, n, i);

        for (int i = n - 1; i > 0; i--) {

            Book temp = list.get(0);
            list.set(0, list.get(i));
            list.set(i, temp);

            heapify(list, i, 0);
        }
    }

    private void heapify(ArrayList<Book> list, int n, int i) {
        int largest = i;  
        int left = 2 * i + 1;  
        int right = 2 * i + 2;  

        if (left < n && list.get(left).index > list.get(largest).index)
            largest = left;

        if (right < n && list.get(right).index > list.get(largest).index)
            largest = right;

        if (largest != i) {
            Book swap = list.get(i);
            list.set(i, list.get(largest));
            list.set(largest, swap);

            heapify(list, n, largest);
        }
    }

    private void undoAction() {
        if (!undoStack.isEmpty()) {
            Book lastAction = undoStack.pop();
            redoStack.push(lastAction);
            books.remove(lastAction);
            displayBooks();
        } else {
            JOptionPane.showMessageDialog(this, "Nothing to undo.");
        }
    }

    private void redoAction() {
        if (!redoStack.isEmpty()) {
            Book lastUndone = redoStack.pop();
            books.add(lastUndone);
            undoStack.push(lastUndone);
            displayBooks();
        } else {
            JOptionPane.showMessageDialog(this, "Nothing to redo.");
        }
    }
}
