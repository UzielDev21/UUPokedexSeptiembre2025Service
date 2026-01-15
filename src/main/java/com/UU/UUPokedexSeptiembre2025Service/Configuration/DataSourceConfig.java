package com.UU.UUPokedexSeptiembre2025Service.Configuration;

import com.UU.UUPokedexSeptiembre2025Service.JPA.Result;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class DataSourceConfig {

    @Bean
    public DataSource dataSource() {
        Result result = new Result();
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        try {

            dataSource.setUrl("jdbc:oracle:thin:@192.167.0.163:1521:orcl");
            dataSource.setUsername("UUPokedexSeptiembre2025");
            dataSource.setPassword("password1");
            System.out.println("Conexión realizada con exito");
            result.correct = true;

        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
        }

        return dataSource;
    }

}
