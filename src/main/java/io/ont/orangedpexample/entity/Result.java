package io.ont.orangedpexample.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Result {
    String provider_did;
    RespData data;
    String encrypted;
}
