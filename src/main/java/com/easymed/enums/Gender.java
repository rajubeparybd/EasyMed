package com.easymed.enums;

/**
 * User gender enumeration
 *
 * @author Raju Bepary
 * @since 1.0.0
 */
public enum Gender {
    /**
     * User gender
     */
    MALE, FEMALE, OTHER;

    /**
     * Get the text representation of Gender
     *
     * @param gender gender to get text representation
     *
     * @return String representation
     */
    public static String getText(Gender gender) {
        return String.valueOf(gender);
    }

    /**
     * Get the text representation of the gender
     *
     * @param gender Gender to get text representation
     *
     * @return String representation of the gender
     */
    public static String getGenderText(Gender gender) {
        return switch (gender) {
            case MALE -> "Male";
            case FEMALE -> "Female";
            case OTHER -> "Other";
            default -> "Unknown";
        };
    }

    /**
     * Check if the gender is male
     *
     * @return Boolean
     */
    public static boolean isMale(String gender) {
        return gender.toUpperCase().equals(MALE.toString());
    }

    /**
     * Check if the gender is female
     *
     * @return Boolean
     */
    public static boolean isFemale(String gender) {
        return gender.toUpperCase().equals(FEMALE.toString());
    }

    /**
     * Check if the gender is other
     *
     * @return Boolean
     */
    public static boolean isOther(String gender) {
        return gender.toUpperCase().equals(OTHER.toString());
    }
}
