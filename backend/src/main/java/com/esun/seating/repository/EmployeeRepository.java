package com.esun.seating.repository;

import com.esun.seating.model.EmployeeDto;
import com.esun.seating.util.SqlFileLoader;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EmployeeRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SqlFileLoader sqlFileLoader;

    public EmployeeRepository(NamedParameterJdbcTemplate jdbcTemplate, SqlFileLoader sqlFileLoader) {
        this.jdbcTemplate = jdbcTemplate;
        this.sqlFileLoader = sqlFileLoader;
    }

    public List<EmployeeDto> findAllEmployees() {
        String sql = sqlFileLoader.load("sql/queries/employees/find_all.sql");
        return jdbcTemplate.query(sql, new MapSqlParameterSource(), (rs, rowNum) ->
                new EmployeeDto(
                        rs.getString("EMP_ID"),
                        rs.getString("NAME")
                )
        );
    }
}
