package com.insidetest.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @Mock
    private NamedParameterJdbcTemplate jdbcTemplate;

    @InjectMocks
    private AuthenticationService authenticationService;

    @Test
    void getUserIdPositiveTest() {
        int id = 100;
        Mockito.when(jdbcTemplate.queryForObject(any(String.class), any(Map.class), (Class<Object>) any()))
                .thenReturn(id);
        Optional<Integer> answer = authenticationService.getUserId("String", "String");
        assertTrue(answer.isPresent());
        assertEquals(id, answer.get());
    }

    @Test
    void getUserIdNegativeTest() {
        Mockito.when(jdbcTemplate.queryForObject(any(String.class), any(Map.class), (Class<Object>) any()))
                .thenReturn(null);
        Optional<Integer> answer = authenticationService.getUserId("String", "String");
        assertTrue(answer.isEmpty());
    }


    @Test
    public void generateToken() {
        authenticationService.generateToken(0);
        Mockito.verify(jdbcTemplate, times(1)).update(any(String.class), any(Map.class));
    }

    @Test
    public void getToken() {
        Mockito.when(jdbcTemplate.queryForObject(any(String.class), any(Map.class), (Class<Object>) any()))
                .thenReturn(null);
        Optional<String> result = authenticationService.getToken("String");
        assertTrue(result.isEmpty());
    }

}
