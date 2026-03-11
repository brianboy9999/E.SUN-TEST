package com.esun.seating.repository;

import com.esun.seating.model.SeatRowDto;
import com.esun.seating.util.SqlFileLoader;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SeatRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SqlFileLoader sqlFileLoader;

    public SeatRepository(NamedParameterJdbcTemplate jdbcTemplate, SqlFileLoader sqlFileLoader) {
        this.jdbcTemplate = jdbcTemplate;
        this.sqlFileLoader = sqlFileLoader;
    }

    public List<SeatRowDto> findAllSeats() {
        String sql = sqlFileLoader.load("sql/queries/seats/find_all.sql");
        return jdbcTemplate.query(sql, new MapSqlParameterSource(), (rs, rowNum) ->
                new SeatRowDto(
                        rs.getInt("FLOOR_SEAT_SEQ"),
                        rs.getInt("FLOOR_NO"),
                        rs.getInt("SEAT_NO"),
                        rs.getString("EMP_ID")
                )
        );
    }

    public int clearEmployeeSeat(String empId) {
        String sql = sqlFileLoader.load("sql/queries/seats/assign/step1_clear_employee_old_seat.sql");
        return jdbcTemplate.update(sql, new MapSqlParameterSource("empId", empId));
    }

    public int clearTargetSeatOccupant(Integer newSeatSeq) {
        String sql = sqlFileLoader.load("sql/queries/seats/assign/step2_clear_target_seat_occupant.sql");
        return jdbcTemplate.update(sql, new MapSqlParameterSource("newSeatSeq", newSeatSeq));
    }

    public int assignEmployeeToSeat(String empId, Integer newSeatSeq) {
        String sql = sqlFileLoader.load("sql/queries/seats/assign/step3_assign_employee_to_new_seat.sql");
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("empId", empId)
                .addValue("newSeatSeq", newSeatSeq);
        return jdbcTemplate.update(sql, params);
    }
}
