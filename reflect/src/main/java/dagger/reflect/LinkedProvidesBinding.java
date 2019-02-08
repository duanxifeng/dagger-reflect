package dagger.reflect;

import dagger.reflect.Binding.LinkedBinding;
import java.lang.reflect.Method;
import org.jetbrains.annotations.Nullable;

import static dagger.reflect.Reflection.tryInvoke;

public final class LinkedProvidesBinding<T> extends LinkedBinding<T> {
  private final @Nullable Object instance;
  private final Method method;
  private final LinkedBinding<?>[] dependencies;

  LinkedProvidesBinding(@Nullable Object instance, Method method, LinkedBinding<?>[] dependencies) {
    this.instance = instance;
    this.method = method;
    this.dependencies = dependencies;
  }

  @Override public @Nullable T get() {
    Object[] arguments = new Object[dependencies.length];
    for (int i = 0; i < arguments.length; i++) {
      arguments[i] = dependencies[i].get();
    }
    return (T) tryInvoke(instance, method, arguments);
  }

  @Override public String toString() {
    return "@Provides[" + method.getDeclaringClass().getName() + '.' + method.getName() + "(…)]";
  }
}
