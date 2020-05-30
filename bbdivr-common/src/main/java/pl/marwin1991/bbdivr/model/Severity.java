package pl.marwin1991.bbdivr.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum Severity {

    DEFCON("Defcon1"),
    CRITICAL("Critical"),
    HIGH("High"),
    MEDIUM("Medium"),
    LOW("Low"),
    NEGLIGIBLE("Negligible"),
    UNKNOWN("Unknown");

    private String level;

}
