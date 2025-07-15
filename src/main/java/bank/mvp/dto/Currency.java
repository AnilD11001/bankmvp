package bank.mvp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Currency {
    private String alphaCode;
    private Long numericCode;
    private String designation;
}
