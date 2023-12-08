package vinci.be.backend.models;

public enum TourState {
    PREVUE("prévue"),
    COMMENCEE("commencée"),
    TERMINEE("terminée");

    private final String label;

    TourState(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
