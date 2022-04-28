package se.demo.service.laundrybooking.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import se.demo.service.laundrybooking.repository.util.FileReader;

@Repository
public class OwnerFacade {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private FileReader fileReader;

    public void createCustomerTable() {
        String sqlStatement = fileReader.readFileContent("create_owner_table.sql");
        jdbcTemplate.execute(sqlStatement);
    }
}
