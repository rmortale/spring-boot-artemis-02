package com.example.springbootartemis.util;

import lombok.Data;

@Data
public class VaiHeader {

    private String sourceSystem;
    private String sourceEnvironment;
    private String vaiServiceId;
    private String vaiServiceVersion;
}
