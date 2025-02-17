package ru.t1.java.demo.dto;

import lombok.Data;

import java.util.UUID;

/**
 * DTO for {@link ru.t1.java.demo.model.Client}
 */
@Data
public class ClientDto {

    private UUID clientId;
    private String firstName;
    private String lastName;
    private String middleName;

}