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
    UNKNOWN("0");

    @JsonValue
    private String level;

}
