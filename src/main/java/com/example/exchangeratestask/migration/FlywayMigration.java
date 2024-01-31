package com.example.exchangeratestask.migration;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.ClassicConfiguration;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

/**
 * This class describes the migration rules for the database
 */
public class FlywayMigration implements DbMigration {
    private final DataSource dataSource;

    public FlywayMigration(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Method to initiate the database migration using Flyway.
     */
    @Override
    @PostConstruct
    public void migrate() {
        ClassicConfiguration config = new ClassicConfiguration();
        config.setBaselineOnMigrate(true);
        config.setTable("schema_version");
        config.setDataSource(dataSource);

         new Flyway(config).migrate();
    }
}
