package com.clase.tarea.model;


import lombok.*;


@Getter
@Setter
@ToString
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Request
{

    private String user;
    private String Password;
}
