package be.witspirit.common.test;

import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Class.forName;
import static java.lang.System.getenv;

/**
 * Small JUnit 5 extension inspired by some code from the System Rules library (see http://stefanbirkner.github.io/system-rules)
 */
public class SysEnvExtension implements BeforeTestExecutionCallback, AfterTestExecutionCallback, TestExecutionExceptionHandler {

    private Map<String, String> originalSysEnv;

    private void saveCurrentEnv() {
        originalSysEnv = new HashMap<>(getenv());
    }

    private void restoreEnv() {
        restoreVariables(getEditableMapOfVariables());
        Map<String, String> theCaseInsensitiveEnvironment
                = getTheCaseInsensitiveEnvironment();
        if (theCaseInsensitiveEnvironment != null)
            restoreVariables(theCaseInsensitiveEnvironment);
    }

    private void restoreVariables(Map<String, String> variables) {
        variables.clear();
        variables.putAll(originalSysEnv);
    }

    @Override
    public void beforeTestExecution(ExtensionContext context) throws Exception {
        saveCurrentEnv();

        WithSysEnv withSysEnv = context.getTestMethod().get().getAnnotation(WithSysEnv.class);
        if (withSysEnv != null) {
            Map<String, String> liveEnv = getEditableMapOfVariables();
            Map<String, String> windowsEnv = getTheCaseInsensitiveEnvironment();
            for (EnvValue envValue : withSysEnv.value()) {
                liveEnv.put(envValue.key(), envValue.val());
                if (windowsEnv != null) {
                    windowsEnv.put(envValue.key(), envValue.val());
                }
            }
        }
    }

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        restoreEnv();
    }

    @Override
    public void handleTestExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
        restoreEnv();
        throw throwable;
    }

    // Shameless rip of System Rules
    private static Map<String, String> getEditableMapOfVariables() {
        Class<?> classOfMap = getenv().getClass();
        try {
            return getFieldValue(classOfMap, getenv(), "m");
        } catch (IllegalAccessException e) {
            throw new RuntimeException("System Rules cannot access the field"
                    + " 'm' of the map System.getenv().", e);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("System Rules expects System.getenv() to"
                    + " have a field 'm' but it has not.", e);
        }
    }

    /*
	 * The names of environment variables are case-insensitive in Windows.
	 * Therefore it stores the variables in a TreeMap named
	 * theCaseInsensitiveEnvironment.
     */
    private static Map<String, String> getTheCaseInsensitiveEnvironment() {
        try {
            Class<?> processEnvironment = forName("java.lang.ProcessEnvironment");
            return getFieldValue(
                    processEnvironment, null, "theCaseInsensitiveEnvironment");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("System Rules expects the existence of"
                    + " the class java.lang.ProcessEnvironment but it does not"
                    + " exist.", e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("System Rules cannot access the static"
                    + " field 'theCaseInsensitiveEnvironment' of the class"
                    + " java.lang.ProcessEnvironment.", e);
        } catch (NoSuchFieldException e) {
            //this field is only available for Windows
            return null;
        }
    }

    private static Map<String, String> getFieldValue(Class<?> klass,
                                                     Object object, String name)
            throws NoSuchFieldException, IllegalAccessException {
        Field field = klass.getDeclaredField(name);
        field.setAccessible(true);
        return (Map<String, String>) field.get(object);
    }
}
