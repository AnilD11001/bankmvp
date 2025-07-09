package bank.mvp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class OrderingDto {

    private Map<String, InternalAccountFormat> accountNumber=new HashMap<>();
    private  Map<String, String> accountClass = new HashMap<>(); //2 keys :    value; comparisonOperator;
    private Customer customer;
}
