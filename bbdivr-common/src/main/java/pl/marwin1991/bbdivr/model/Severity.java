package pl.marwin1991.bbdivr.model;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum Severity {

    DEFCON("1"),
    CRITICAL("2"),
    HIGH("3"),
    MEDIUM("4"),
    LOW("5"),
    NEGLIGIBLE("6"),
    UNKNOWN("7");

    @JsonValue
    private String level;


    public static Severity of(String sev) {
        switch (sev) {
            case "Unknown":
                return UNKNOWN;
            case "Negligible":
                return NEGLIGIBLE;
            case "Medium":
                return MEDIUM;
            case "Low":
                return LOW;
            case "High":
                return HIGH;
            case "Critical":
                return CRITICAL;
            case "Defcon1":
                return DEFCON;
            default:
                return UNKNOWN;
        }

    }
}
