package fi.helsinki.cs.tmc.testscanner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Name;
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
                String className = ((TypeElement)(elem.getEnclosingElement())).getQualifiedName().toString();
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
        String classAnnotation = getPointsAnnotationValueIfAny(method.getEnclosingElement());
        if (classAnnotation != null) {
            pointNames.addAll(Arrays.asList(classAnnotation.split(" +")));
        }
        String methodAnnotation = getPointsAnnotationValueIfAny(method);
        if (methodAnnotation != null) {
            pointNames.addAll(Arrays.asList(methodAnnotation.split(" +")));
        }
        return pointNames;
    }
    
    private String getPointsAnnotationValueIfAny(Element e) {
        for (AnnotationMirror am : e.getAnnotationMirrors()) {
            Name annotationName = am.getAnnotationType().asElement().getSimpleName();
            if (annotationName.contentEquals("Points")) { // Any "@Points" annotation (from any package) will do
                String value = getAnnotationValue(am);
                return value;
            }
        }
        return null;
    }
    
    private String getAnnotationValue(AnnotationMirror am) {
        for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> e : am.getElementValues().entrySet()) {
            if (e.getKey().getSimpleName().contentEquals("value")) {
                Object value = e.getValue().getValue();
                if (value instanceof String) {
                    return (String)value;
                }
            }
        }
        return "";
    }
    
    public List<TestMethod> getTestMethods() {
        return Collections.unmodifiableList(testMethods);
    }
}
