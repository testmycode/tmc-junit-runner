package fi.helsinki.cs.tmc.testscanner;

import fi.helsinki.cs.tmc.testrunner.Points;
import java.util.ArrayList;
import java.util.Arrays;
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
                List<String> points = pointsOfTestCase(elem);
                if (!points.isEmpty()) {
                    testMethods.add(new TestMethod(className, methodName, points.toArray(new String[0])));
                }
            }
        }
        return false;
    }
    
    private List<String> pointsOfTestCase(Element method) {
        ArrayList<String> pointNames = new ArrayList<String>();
        Points classAnnotation = method.getEnclosingElement().getAnnotation(Points.class);
        if (classAnnotation != null) {
            pointNames.addAll(Arrays.asList(classAnnotation.value().split(" +")));
        }
        Points methodAnnotation = method.getAnnotation(Points.class);
        if (methodAnnotation != null) {
            pointNames.addAll(Arrays.asList(methodAnnotation.value().split(" +")));
        }
        return pointNames;
    }

    public List<TestMethod> getTestMethods() {
        return Collections.unmodifiableList(testMethods);
    }
}
