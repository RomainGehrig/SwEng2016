package icynote.note;

import java.util.GregorianCalendar;

/**
 * Gives access to a note's attributes.
 * <p>
 * Depending of which implementation you use, some attributes modifications
 * might be deactivated. Hence, you should always check the returned Response object.
 *
 * @author Julien Harbulot
 * @version 1.0
 */
public interface Note<S> {
    /**
     * Returns the unique id of this note.
     */
    int getId();

    /**
     * Returns the title of this note.
     *
     * @return the returned field is never null.
     */
    S getTitle();

    /**
     * Returns the content of this note.
     *
     * @return the returned field is never null.
     */
    S getContent();

    /**
     * Returns the date of creation of this note.
     *
     * @return the returned field is never null.
     */
    GregorianCalendar getCreation();

    /**
     * Returns the date of last modification of this note.
     *
     * @return the returned field is never null.
     */
    GregorianCalendar getLastUpdate();

    /**
     * Replaces this note's unique id by the provided one.
     * <p>
     * Pre condition: the argument is never null. <br>
     *
     * @return a non null response object indicating whether the set was successful.
     */
    Response setId(int newId);

    /**
     * Replaces this note's title by the provided one.
     * <p>
     * Pre condition: the argument is never null. <br>
     * Exception: {@code IllegalArgumentException} if the argument is {@code null}.
     *
     * @return a non null response object indicating whether the set was successful.
     */
    Response setTitle(S newTitle);

    /**
     * Replaces this note's content by the provided one.
     * <p>
     * Pre condition: the argument is never null. <br>
     * Exception: {@code IllegalArgumentException} if the argument is {@code null}.
     *
     * @return a non null response object indicating whether the set was successful.
     */
    Response setContent(S newContent);

    /**
     * Replaces this note's date of creation by the provided one.
     * <p>
     * Pre condition: the argument is never null. <br>
     * Exception: {@code IllegalArgumentException} if the argument is {@code null}.
     *
     * @return a non null response object indicating whether the set was successful.
     */
    Response setCreation(GregorianCalendar creationDate);

    /**
     * Replaces this note's date of last modification by the provided one.
     * <p>
     * Pre condition: the argument is never null. <br>
     * Exception: {@code IllegalArgumentException} if the argument is {@code null}.
     *
     * @return a non null response object indicating whether the set was successful.
     */
    Response setLastUpdate(GregorianCalendar lastUpdateDate);
}
