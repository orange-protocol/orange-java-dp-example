package io.ont.orangedpexample.entity;

import lombok.Data;

@Data
public class BalanceReq {
    String UserDID;
    String Address;
    String Chain;
    Boolean Encrypt;
}
