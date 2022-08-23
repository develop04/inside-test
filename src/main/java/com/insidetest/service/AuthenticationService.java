package com.insidetest.service;


import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    /**
     * Получение ID пользователя(user_id) из таблицы Users
     */
    public Optional<Integer> getUserId(String login, String password) {
        String sql = """
                SELECT id
                FROM  test_db.Users
                WHERE login = :login AND password = :password
                LIMIT 1
                """;
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, Map.of("login", login, "password", password), Integer.class));
    }


    /**
     * Генерация jwt-токена и занесение его в таблицу Tokens
     */
    public String generateToken(int userid) {
        String token = UUID.randomUUID().toString();
        String sql = """
                INSERT INTO test_db.Tokens(token, user_id) VALUES (:token, :userid)
                ON DUPLICATE KEY UPDATE token = :token
                """;

        jdbcTemplate.update(sql, Map.of("token", token,"userid", userid));
        return token;
    }

    /**
     * Получение jwt-токена из таблицы Tokens
     */
    public Optional<String> getToken(String userName) {
        String sql = """
            SELECT id
            FROM  test_db.Users
            WHERE login = :login
            LIMIT 1
            """;
        Optional<Integer> userId = Optional.ofNullable(jdbcTemplate.queryForObject(sql, Map.of("login", userName), Integer.class));
        if (userId.isEmpty()) {
            return Optional.empty();
        }

         sql = """
            SELECT token
            FROM  test_db.Tokens
            WHERE user_id = :user_id
            LIMIT 1
            """;
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, Map.of("user_id", userId.get()), String.class));
    }
}
