package com.example.exchangeratestask.migration;
/**
 * Interface for describing database migration execution.
 */
@FunctionalInterface
public interface DbMigration {
    void migrate();
}
