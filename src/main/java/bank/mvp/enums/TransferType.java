package bank.mvp.enums;

public enum TransferType {
    RTGS("RTGS"),
    IMPS("IMPS"),
    NEFT("NEFT"),
    TYPE_120("120");
    private String type;

    private TransferType(String type) {
        this.type = type;
    }
    public String getType() {
        return type;
    }
}
