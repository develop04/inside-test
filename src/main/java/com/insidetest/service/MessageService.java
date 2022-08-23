package com.insidetest.service;


import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final JdbcTemplate simpleJdbcTemplate;


    /**
     * Вставка сообщения в таблицу Logger
     */
    public void insertMessage(String userName, String message) {
        String sql = """
            INSERT INTO test_db.Logger(user_name, message) VALUES (:userName, :message)
            """;
        jdbcTemplate.update(sql, Map.of("userName", userName, "message", message));
    }

    /**
     * Получение последних десяти сообщений из таблицы Logger
     */
    public Optional<List<String>> getLastTenMessages() {
        String sql = """
            SELECT message FROM test_db.Logger ORDER BY messageid DESC LIMIT 10
            """;
        return Optional.ofNullable(simpleJdbcTemplate.queryForList(sql, String.class));
    }


}
