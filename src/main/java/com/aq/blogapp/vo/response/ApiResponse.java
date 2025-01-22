package com.aq.blogapp.vo.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;


@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class ApiResponse {

    // 2021.03.24.16.34.26
    private final SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");

    // 2021-03-24T16:44:39.083+08:00
    private final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

    // 2021-03-24 16:48:05
    private final SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private String timestamp = sdf2.format(new Timestamp(System.currentTimeMillis()));
    private String message;
    private Integer statusCode;

    public ApiResponse(String message, Integer statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }
}
