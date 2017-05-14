package com.docgl;

import com.docgl.exceptions.ValidationException;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * Created by Martin on 26.4.2017.
 * Validation Exception Mapper
 */
public class ValidationExceptionMapper implements ExceptionMapper<ValidationException> {
    @Override
    public Response toResponse(final ValidationException ex) {

        return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON)
                .entity(new MyResponse(ex.getMessage())).build();
    }

    static final class MyResponse {
        @JsonProperty
        private String message;

        MyResponse(final String message) {
            this.message = message;
        }
    }
}
