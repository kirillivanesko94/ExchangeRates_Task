package com.example.exchangeratestask.configuration;

import com.example.exchangeratestask.migration.DbMigration;
import com.example.exchangeratestask.migration.FlywayMigration;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfiguration {
    @Bean
    @ConfigurationProperties("db")
    public DBConfig dbConfig() {
        return new DBConfig();
    }

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
    @Bean
    public DbMigration dbMigration(DataSource dataSource) {
        return new FlywayMigration(dataSource);
    }
}
