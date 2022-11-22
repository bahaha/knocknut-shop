package dev.claycheng.knocknut.members.config;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@Configuration
@EnableTransactionManagement
@RequiredArgsConstructor
public class FlywayConfig {

  private static final String DB_MIGRATION_ROOT = "classpath:db/";
  private final DataSource dataSource;

  @Bean
  public void migrate() {
    DynamicRoutingDataSource ds = (DynamicRoutingDataSource) dataSource;
    var dataSources = ds.getCurrentDataSources();
    dataSources.forEach(
        (name, dsOfModule) -> {
          log.info("Doing database migration with data source [{}]...", name);
          var flyway =
              Flyway.configure()
                  .dataSource(dsOfModule)
                  .locations(DB_MIGRATION_ROOT + name)
                  .baselineOnMigrate(true)
                  .cleanOnValidationError(true)
                  .validateOnMigrate(true)
                  .cleanDisabled(true)
                  .failOnMissingLocations(false)
                  .load();
          flyway.migrate();
        });
  }
}
