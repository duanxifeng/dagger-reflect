package dagger.reflect;

import dagger.reflect.Binding.UnlinkedBinding;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import static dagger.reflect.Reflection.findQualifier;

public final class UnlinkedOptionalBinding extends UnlinkedBinding {
  private final Method method;

  UnlinkedOptionalBinding(Method method) {
    this.method = method;
  }

  @Override LinkedBinding<?> link(Linker linker) {
    Type[] parameterTypes = method.getGenericParameterTypes();
    if (parameterTypes.length != 0) {
      throw new IllegalArgumentException(
          "@BindsOptionalOf methods must not have parameters: " + method);
    }

    Annotation[] methodAnnotations = method.getDeclaredAnnotations();
    Annotation qualifier = findQualifier(methodAnnotations);
    Key key = Key.of(qualifier, method.getReturnType());

    LinkedBinding<?> dependency = linker.find(key);
    return new LinkedOptionalBinding<>(dependency);
  }
}
