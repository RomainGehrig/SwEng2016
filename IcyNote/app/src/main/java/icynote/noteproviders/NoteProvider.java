package icynote.noteproviders;

import icynote.note.Response;
import util.Optional;

/**
 * Provides and persists notes of generic type {@code N}.
 * @param <N> the type of the notes to store
 * @author Julien Harbulot
 * @version 1.0
 */

public interface NoteProvider<N> {
    /**
     * Creates a new note with a unique id.
     * <p>
     * Pre condition: none. <br>
     * Post condition: the returned object is never null. <br>
     * Exception: none. <br>
     *
     * @return an newly created note or Empty if an error occurred.
     */
    Optional<N> createNote();

    /**
     * Fetches the unique note corresponding to the provided id.
     * <p>
     * Pre condition: the provided id exists. If it doesn't, Empty is returned. <br>
     * Post condition: the returned object is never null. <br>
     * Exception: none. <br>
     *
     * @param id the id of an existing note.
     * @return the existing note if found or Empty if not found.
     */
    Optional<N> getNote(int id);

    /**
     * Fetches every notes and sorts the result based on the
     * specific field determined by {@code index}.
     * <p>
     * Pre condition: arguments are never null. <br>
     * <p>
     * Post condition: the returned collection is never null and
     * the elements it contains are never null.<br>
     * <p>
     * Exception: if one of the argument is null,
     * an {@code IllegalArgumentException} in thrown.<br>
     *
     * @param index the field to use for sorting.
     * @param order the direction of the sort.
     * @return a collection of notes sorted according to {@code OrderBy} and {@code OrderType}.
     */
    Iterable<N> getNotes(OrderBy index, OrderType order);

    /**
     * Persists the changes made to the provided note.
     * <p>
     * Pre condition: the note is not null and wasn't previously deleted. <br>
     * Post condition: the note is persisted and can later be fetched with {@code getNote}. <br>
     * Exception: {@code IllegalArgumentException} if the argument is {@code null}. <br>
     *
     * @return a non null response object indicating whether the operation
     * was successful.
     */
    Response persist(N n);

    /**
     * Deletes the unique note corresponding to the provided id. The method {@code persist} may not
     * be used with this specific note.
     * <p>
     * Pre condition: the id corresponds to an existing note. <br>
     * Post condition: the note cannot be fetched with {@code getNote} anymore.<br>
     * Exception: none. <br>
     *
     * @return a non null response object indicating whether the operation
     * was successful.
     */
    Response delete(int id);
}
