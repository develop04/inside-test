package com.insidetest.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import java.util.Map;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class MessageServiceTest {

    @Mock
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Mock
    private JdbcTemplate simpleJdbcTemplate;

    @InjectMocks
    private MessageService messageService;


    @Test
    public void insertMessage() {
        messageService.insertMessage("String", "String");
        Mockito.verify(jdbcTemplate, times(1)).update(any(String.class), any(Map.class));
    }

    @Test
    public void getLastTenMessages() {
        messageService.getLastTenMessages();
        Mockito.verify(simpleJdbcTemplate, times(1)).queryForList(any(String.class), (Class<Object>) any());
    }


}
