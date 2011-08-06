package fi.helsinki.cs.tmc.testscanner;

import fi.helsinki.cs.tmc.testrunner.Points;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import org.junit.Test;

/**
 * An annotation processor that records all <code>@Test</code>-annotated methods it sees.
 */
@SupportedSourceVersion(value = SourceVersion.RELEASE_6)
@SupportedAnnotationTypes(value = {"fi.helsinki.cs.tmc.testrunner.Exercise", "org.junit.Test"})
class TestMethodAnnotationProcessor extends AbstractProcessor {

    private ArrayList<TestMethod> testMethods = new ArrayList<TestMethod>();

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element elem : roundEnv.getElementsAnnotatedWith(Test.class)) {
            if (elem.getKind() == ElementKind.METHOD) {
                String methodName = elem.getSimpleName().toString();
                String className = elem.getEnclosingElement().getSimpleName().toString();
                Points exerciseAnnotation = elem.getAnnotation(Points.class);
                String[] exercises;
                if (exerciseAnnotation != null) {
                    exercises = exerciseAnnotation.value().split(" +");
                } else {
                    exercises = new String[0];
                }
                testMethods.add(new TestMethod(className, methodName, exercises));
            }
        }
        return false;
    }

    public List<TestMethod> getTestMethods() {
        return Collections.unmodifiableList(testMethods);
    }
}
