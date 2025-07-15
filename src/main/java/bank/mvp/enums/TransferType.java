package bank.mvp.enums;

import lombok.Getter;

@Getter
public enum TransferType {
    RTGS("RTGS"),
    IMPS("IMPS"),
    NEFT("NEFT"),
    TYPE_120("120");
    private final  String type;

    private TransferType(String type) {
        this.type = type;
    }
}
