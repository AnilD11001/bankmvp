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
public class InternalAccountFormat {
    private Map<String, String> branch =new HashMap<>();
    private Currency currency;
    private String account;
    private String suffix;
}
