package dao;
import exceptions.DatabaseException;

import java.util.List;
import java.util.Optional;

/**
 * Generisches Interface für CRUD-Operationen.
 *
 * @param <T>  Typ der Entität
 * @param <ID> Typ des Identifikators
 */
public interface GenericDAO<T, ID> {

    /**
     * Speichert eine neue Entität in der Datenbank.
     * @param entity zu speichernde Entität
     * @return gespeicherte Entität
     */
  T save(T entity) throws DatabaseException;

    /**
     * Aktualisiert eine bestehende Entität in der Datenbank.
     * @param entity zu aktualisierende Entität
     */
    void update(T entity) throws DatabaseException;

    /**
     * Löscht eine Entität aus der Datenbank anhand der ID.
     * @param id Identifikator der Entität
     */
    void delete(ID id) throws DatabaseException;

    /**
     * Findet eine Entität anhand der ID.
     * @param id Identifikator der Entität
     * @return Optional der gefundenen Entität
     */
    Optional<T> findById(ID id) throws DatabaseException;

    /**
     * Gibt alle Entitäten zurück.
     * @return Liste aller Entitäten
     */
    List<T> findAll() throws DatabaseException;
}
