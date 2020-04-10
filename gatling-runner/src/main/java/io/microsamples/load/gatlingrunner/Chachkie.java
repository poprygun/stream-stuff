package io.microsamples.load.gatlingrunner;

import lombok.Data;
import lombok.ToString;

import java.time.Instant;

@Data
@ToString
public class Chachkie {
    private int age;
    private String id, name, description;
    private Instant when;
}
