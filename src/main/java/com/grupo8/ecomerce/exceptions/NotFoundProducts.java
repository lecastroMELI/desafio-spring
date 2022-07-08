package com.grupo8.ecomerce.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundProducts extends RuntimeException {
    public NotFoundProducts(String mensagem) {
        super(mensagem);
    }
}
