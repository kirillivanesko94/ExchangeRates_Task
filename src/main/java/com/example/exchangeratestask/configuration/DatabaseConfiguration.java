package com.example.exchangeratestask.configuration;

import com.example.exchangeratestask.migration.DbMigration;
import com.example.exchangeratestask.migration.FlywayMigration;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * This class is responsible for the configuration of the database
 */
@Configuration
public class DatabaseConfiguration {
    /**
     * Bean to configure the database properties.
     *
     * @return the DBConfig object with the configured properties
     */
    @Bean
    @ConfigurationProperties("db")
    public DBConfig dbConfig() {
        return new DBConfig();
    }
    /**
     * Bean to create the DataSource using the configured properties.
     *
     * @param config the DBConfig object with the database properties
     * @return the created DataSource
     */
    @Bean
    public DataSource dataSource(DBConfig config) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(config.getJdbcUrl());
        hikariConfig.setUsername(config.getUsername());
        hikariConfig.setPassword(config.getPassword());
        hikariConfig.setDriverClassName("org.postgresql.Driver");
        hikariConfig.setPoolName("Fake Pool");
        hikariConfig.addDataSourceProperty("ApplicationName", "Fake");

        hikariConfig.setMaximumPoolSize(config.getMaximumPoolSize());
        hikariConfig.setMinimumIdle(config.getMinimumIdle());
        hikariConfig.setIdleTimeout(config.getIdleTimeout());
        hikariConfig.setConnectionTimeout(config.getConnectionTimeout());

        return new HikariDataSource(hikariConfig);
    }
    /**
     * Bean to create the database migration instance.
     *
     * @param dataSource the DataSource to be used for the migration
     * @return the DbMigration implementation, using FlywayMigration
     */
    @Bean
    public DbMigration dbMigration(DataSource dataSource) {
        return new FlywayMigration(dataSource);
    }
}
