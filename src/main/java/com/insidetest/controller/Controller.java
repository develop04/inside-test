package com.insidetest.controller;

import com.insidetest.model.HistoryMsg;
import com.insidetest.model.SimpleMessage;
import com.insidetest.model.TokenMsg;
import com.insidetest.model.UserPassword;
import com.insidetest.service.AuthenticationService;
import com.insidetest.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;



@RestController("api/v1")
@RequiredArgsConstructor
public class Controller {

    private final AuthenticationService authenticationService;
    private final MessageService messageService;


    /**
     * HTTP POST эндпоинт "/auth", который получает данные в json вида:
     *      {
     *          name: "имя отправителя"
     *          password: "пароль"
     *      }
     * Проверяет пароль по БД и создает jwt токен и отправляет токен в ответ, тоже json вида:
     *      {
     *          token: "тут сгенерированный токен"
     *      }
     * Если пароль неверен - выкидывает исключение ResponseStatusException
     */
    @PostMapping("/auth")
    public TokenMsg authenticate(@RequestBody UserPassword userPassword) {

        if (userPassword.name() != null && userPassword.password() != null) {
            Optional<Integer> userId = authenticationService.getUserId(userPassword.name(), userPassword.password());
            if (userId.isPresent()) {
                return new TokenMsg(authenticationService.generateToken(userId.get(), userPassword.name(), userPassword.password()));
            }
        }

        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "entity not found"
        );
    }


    /**
     * HTTP POST эндпоинт "/msg", который получает данные в json вида:
     *      {
     *          bearer:	  jwt-token,
     *          name:       "имя отправителя",
     *          message:    "текст сообщение"
     *      }
     * Проверяет jwt-токен по БД таблицы Tokens. Если все в порядке - заносит сообщение в БД.
     * Если при этом текст сообщения - "history 10", отправляет в ответ последние 10 сообщений
     * Если jwt-токен неверен - выкидывает исключение ResponseStatusException
     */
    @PostMapping("/msg")
    public HistoryMsg message(@RequestBody SimpleMessage simpleMessage) {

        // Проверка jwt-токена по БД
        if (authenticationService.getToken(simpleMessage.name()).isPresent() &&
                authenticationService.getToken(simpleMessage.name()).get().equals(simpleMessage.bearer())) {

            // Проверка на сообщение "history 10"
            String historyMarker = "history 10";
            if (simpleMessage.message().equals(historyMarker)) {
                if (messageService.getLastTenMessages().isPresent()) {
                    return new HistoryMsg(messageService.getLastTenMessages().get().toArray());
                }
            } else {
                // Запись сообщения в БД
                messageService.insertMessage(simpleMessage.name(), simpleMessage.message());
                return new HistoryMsg(new Object[]{"Ok!"});
            }
        }

        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "entity not found"
        );

    }

}
