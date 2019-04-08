package be.witspirit.menuapigwlambda.api;

import org.springframework.util.NumberUtils;
import org.springframework.util.StringUtils;

/**
 * Web request invocation through the Serverless Java adapter with the Proxy integration from API GW seems
 * to behave slightly different from the normal Tomcat deployment.
 *
 * Haven't investigated the sources of these issues properly, as I expect they can be fixed properly in the
 * supporting infrastructure. In the meanwhile, this class acts as a collector of issues and their common
 * workarounds.
 */
public class LambdaWorkarounds {

    /**
     * When using int parameters as query parameters, the default value does not seem to be respected, causing
     * the following error message:
     * Failed to convert value of type 'java.lang.String[]' to required type 'int'; nested exception is java.lang.NumberFormatException: For input string: ""
     *
     * So the workaround is to keep the original String rep and use this helper method to convert it
     * @param stringRep The String representation of the integer value (or null or empty)
     * @param defaultValue The default value if the String representation is empty
     * @return The int value of the String rep if available, taking into account the default value.
     * @throws NumberFormatException if the String representation cannot be converted to an Integer.
     */
    public static int intParameter(String stringRep, int defaultValue) {
        if (StringUtils.isEmpty(stringRep)) {
            return defaultValue;
        }
        return NumberUtils.parseNumber(stringRep, Integer.class);
    }
}
