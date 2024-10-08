
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class BookBrowser extends JFrame {
    private final TreeMap<String, Book> isbnTreeMap;
    private final TreeMap<String, Book> authorsTreeMap;
    private final TreeMap<Integer, Book> pubYearTreeMap;
    private final TreeMap<String, Book> originalTitleTreeMap;
    private final TreeMap<String, Book> titleTreeMap;
    private final TreeMap<Double, Book> averageRatingTreeMap;

    private final HashMap<String, TreeMap<?, Book>> bookMaps;

    private final JTextField isbnField;
    private final JTextField authorsField;
    private final JTextField yearField;
    private final JTextField originalTitleField;
    private final JTextField titleField;
    private final JTextField avgRatingField;

    private Object[] isbnKeys;
    private int currentIndex = 0;
    private JComboBox<String> comboBox;
    private final CoverPanel coverPanel;

    public static void main(String[] args) {
        new BookBrowser();
    }

    public BookBrowser() {
        setTitle("Book Browser");
        setSize(1200, 600);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        isbnTreeMap = new TreeMap<>();
        authorsTreeMap = new TreeMap<>();
        pubYearTreeMap = new TreeMap<>();
        originalTitleTreeMap = new TreeMap<>();
        titleTreeMap = new TreeMap<>();
        averageRatingTreeMap = new TreeMap<>();
        bookMaps = new HashMap<>();

        File bookData = new File("BooksDataFile.txt");
        readBooks(bookData);

        isbnKeys = isbnTreeMap.toKeyArray(new String[0]);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBackground(Color.GRAY);
        getContentPane().add(mainPanel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(createOrderPanel(), gbc);

        gbc.gridy++;
        mainPanel.add(createLabeledField("ISBN:", isbnField = new JTextField(20)), gbc);

        gbc.gridy++;
        mainPanel.add(createLabeledField("Authors:", authorsField = new JTextField(20)), gbc);

        gbc.gridy++;
        mainPanel.add(createLabeledField("Year:", yearField = new JTextField(20)), gbc);

        gbc.gridy++;
        mainPanel.add(createLabeledField("Orig. Title:", originalTitleField =
                new JTextField(20)), gbc);

        gbc.gridy++;
        mainPanel.add(createLabeledField("Title:", titleField = new JTextField(20)), gbc);

        gbc.gridy++;
        mainPanel.add(createLabeledField("Avg. Rating:", avgRatingField = new JTextField(20)),
                gbc);

        gbc.gridy++;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.GRAY);
        buttonPanel.add(createNavigationButton("|<-"));
        buttonPanel.add(createNavigationButton("< Prev"));
        buttonPanel.add(createNavigationButton("Next >"));
        buttonPanel.add(createNavigationButton("->|"));
        mainPanel.add(buttonPanel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridheight = GridBagConstraints.RELATIVE;
        gbc.fill = GridBagConstraints.BOTH;
        coverPanel = new CoverPanel();
        coverPanel.setPreferredSize(new Dimension(300, 400));
        mainPanel.add(coverPanel, gbc);

        if (isbnKeys.length > 0) {
            updateFields(isbnTreeMap.get((String) isbnKeys[0]));
        }

        setVisible(true);
    }

    private JPanel createOrderPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(Color.GRAY);

        JLabel label = createLabel("Order:");
        String[] options = {"ISBN", "Authors", "Year", "Orig. Title", "Title", "Avg. Rating"};
        comboBox = new JComboBox<>(options);
        comboBox.setMaximumSize(new Dimension(200, 30));

        comboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selected = (String) comboBox.getSelectedItem();
                TreeMap<?, Book> selectedMap = bookMaps.get(selected);
                if (selectedMap != null) {
                    currentIndex = 0;
                    if (selected.equals("Year")) {
                        isbnKeys = ((TreeMap<Integer, Book>) selectedMap).toKeyArray
                                (new Integer[0]);
                    } else if (selected.equals("Avg. Rating")) {
                        isbnKeys = ((TreeMap<Double, Book>) selectedMap).toKeyArray(new Double[0]);
                    } else {
                        isbnKeys = ((TreeMap<String, Book>) selectedMap).toKeyArray(new String[0]);
                    }
                    updateFields(getBookFromMap(selectedMap, isbnKeys[currentIndex]));
                }
            }
        });

        panel.add(label);
        panel.add(comboBox);

        return panel;
    }

    private JPanel createLabeledField(String labelText, JTextField textField) {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(Color.GRAY);

        JLabel label = createLabel(labelText);
        textField.setMaximumSize(new Dimension(400, 30));
        textField.setEditable(false);

        panel.add(label);
        panel.add(textField);

        return panel;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        return label;
    }

    private JButton createNavigationButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(Color.BLUE);
        button.setForeground(Color.BLUE);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch (text) {
                    case "|<-":
                        if (isbnKeys.length > 0) {
                            currentIndex = 0;
                            updateFields(getBookFromMap(bookMaps.get(comboBox.getSelectedItem()),
                                    isbnKeys[currentIndex]));
                        }
                        break;
                    case "< Prev":
                        if (currentIndex > 0) {
                            currentIndex--;
                            updateFields(getBookFromMap(bookMaps.get(comboBox.getSelectedItem()),
                                    isbnKeys[currentIndex]));
                        }
                    case "Next >":
                        if (currentIndex < isbnKeys.length - 1) {
                            currentIndex++;
                            updateFields(getBookFromMap(bookMaps.get(comboBox.getSelectedItem()),
                                    isbnKeys[currentIndex]));
                        }
                        break;
                    case "->|":
                        if (isbnKeys.length > 0) {
                            currentIndex = isbnKeys.length - 1;
                            updateFields(getBookFromMap(bookMaps.get(comboBox.getSelectedItem()),
                                    isbnKeys[currentIndex]));
                        }
                        break;
                }
            }
        });
        return button;
    }

    private void updateFields(Book book) {
        if (book != null) {
            isbnField.setText(book.isbn());
            authorsField.setText(book.authors());
            yearField.setText(String.valueOf(book.publicationYear()));
            originalTitleField.setText(book.originalTitle());
            titleField.setText(book.title());
            avgRatingField.setText(String.valueOf(book.averageRating()));

            coverPanel.loadCoverImage(book.isbn());
        }
    }

    private Book getBookFromMap(TreeMap<?, Book> map, Object key) {
        if (map.size() == 0 || key == null) return null;

        if (key instanceof String) {
            return ((TreeMap<String, Book>) map).get((String) key);
        } else if (key instanceof Integer) {
            return ((TreeMap<Integer, Book>) map).get((Integer) key);
        } else if (key instanceof Double) {
            return ((TreeMap<Double, Book>) map).get((Double) key);
        } else {
            return null;
        }
    }

    private void readBooks(File file) {
        try (Scanner scan = new Scanner(file)) {
            scan.nextLine();
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                String[] fields = line.split("~");
                String isbn = fields[2];
                String authors = fields[3];
                String pubYear = fields[4];
                String originalTitle = fields[5];
                String title = fields[6];
                String averageRating = fields[7];
                int publicationYear = Integer.parseInt(pubYear);
                double avgRating = Double.parseDouble(averageRating);

                Book book = new Book(isbn, authors, publicationYear, originalTitle, title,
                        avgRating);
                isbnTreeMap.put(isbn, book);
                authorsTreeMap.put(authors, book);
                pubYearTreeMap.put(publicationYear, book);
                originalTitleTreeMap.put(originalTitle, book);
                titleTreeMap.put(title, book);
                averageRatingTreeMap.put(avgRating, book);

                bookMaps.put("ISBN", isbnTreeMap);
                bookMaps.put("Authors", authorsTreeMap);
                bookMaps.put("Year", pubYearTreeMap);
                bookMaps.put("Orig. Title", originalTitleTreeMap);
                bookMaps.put("Title", titleTreeMap);
                bookMaps.put("Avg. Rating", averageRatingTreeMap);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static class CoverPanel extends JPanel {
        private Image coverImage;

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (coverImage != null) {
                g.drawImage(coverImage, 0, 0, getWidth(), getHeight(), this);
            } else {

                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(Color.RED);
                g2d.drawLine(0, 0, getWidth(), getHeight());
                g2d.drawLine(0, getHeight(), getWidth(), 0);
            }
        }

        public void loadCoverImage(String isbn) {
            String urlString = "https://covers.openlibrary.org/b/isbn/" + isbn + "-L.jpg";
            try {
                URI uri = new URI(urlString);
                URL url = uri.toURL();
                coverImage = Toolkit.getDefaultToolkit().getImage(url);
                waitForImageToLoad(coverImage);
                repaint();
            } catch (MalformedURLException | URISyntaxException e) {
                coverImage = null;
                repaint();
            }
        }

        private void waitForImageToLoad(Image img) {
            MediaTracker tracker = new MediaTracker(this);
            tracker.addImage(img, 0);
            try {
                tracker.waitForAll();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
