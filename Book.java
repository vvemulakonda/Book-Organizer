/**
 * Stores the information of a book object
 */

/*
  @author Vivek Vemulakonda
 * @version 1.0
 */

/**
 *
 * @param isbn
 * @param authors
 * @param publicationYear
 * @param originalTitle
 * @param title
 * @param averageRating
 */
public record Book(String isbn, String authors, int publicationYear, String originalTitle,
                   String title, double averageRating) {
}

