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

public class AccountNumber {

    private Map<String, String> externalFormatAccount=new HashMap<>(); //key is 'value'

}
