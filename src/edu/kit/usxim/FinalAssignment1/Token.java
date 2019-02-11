package edu.kit.usxim.FinalAssignment1;


public class Token {
    /** The minimum size for a mission control token */
    private static final int MIN_MISSION_CONTROL_SIZE = 2;

    /** The maximum size for a mission control token */
    private static final int MAX_MISSION_CONTROL_SIZE = 7;

    /** The exact size for a vesta or ceres token */
    private static final int VESTA_OR_CERES_SIZE = 1;

    /** The string representation for mission control tokens */
    private static final String MISSION_CONTROL_STRING_REPR = "+";


    public enum Type {
        /** The stone used by mission control */
        MISSION_CONTROL,
        /** The vesta playing token */
        VESTA,
        /** The ceres playing token */
        CERES
    };

    private Type type;
    private int size;

    /**
     * Generate a new playing token
     * @param type the type of the token
     * @param size the height in fields
     */
    public Token(Type type, int size) {
        this.type = type;
        this.size = size;

        checkDimensionValidity();
    }

    /**
     * @return the size in fields
     */
    public int getSize() {
        return size;
    }

    /**
     * @return the type of the token
     */
    public Type getType() {
        return type;
    }

    /**
     * Check if the token size matches the given type
     * @throws IllegalArgumentException if the size does not match our type
     */
    private void checkDimensionValidity() {
        switch (type) {
            case MISSION_CONTROL:
                if (size > MAX_MISSION_CONTROL_SIZE
                    || size < MIN_MISSION_CONTROL_SIZE) {
                    StringBuilder errorMessage = new StringBuilder();
                    errorMessage.append("size must be inbetween ")
                        .append(MIN_MISSION_CONTROL_SIZE)
                        .append(" and ")
                        .append(MIN_MISSION_CONTROL_SIZE)
                        .append(" for mission control tokens");
                    throw new IllegalArgumentException(errorMessage.toString());
                }
                break;
            case VESTA:
            case CERES:
                if (size != VESTA_OR_CERES_SIZE) {
                    StringBuilder errorMessage = new StringBuilder();
                    errorMessage.append("size must be exactly ")
                            .append(VESTA_OR_CERES_SIZE)
                            .append(" for vesta or ceres blocks");
                    throw new IllegalArgumentException(errorMessage.toString());
                }
                break;
            default:
                throw new IllegalArgumentException("Illegal type or size");
        }
    }

    /**
     * @return the string representation of the current token
     */
    public String toString() {
        if (type == Type.MISSION_CONTROL)
            return MISSION_CONTROL_STRING_REPR;

        return getFirstLetterOfType();
    }

    /**
     * @return V for Vega, C for Ceres, M for Mission Control
     */
    private String getFirstLetterOfType() {
        return type.toString().substring(0, 1);
    }



}
