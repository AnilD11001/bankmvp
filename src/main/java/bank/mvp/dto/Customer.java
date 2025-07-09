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

public class Customer {

    private Map<String,String> customer = new HashMap<>(); //customerNumber, displayedName
    private String customerType;
    private String nationalIdentifier;
    private Map<String,String> customerOfficer=new HashMap<>(); ; //code, name
}
