package dagger.reflect;

import dagger.reflect.Binding.LinkedBinding;
import javax.inject.Provider;

final class Scope {
  private final BindingGraph graph;

  public Scope(BindingGraph graph) {
    this.graph = graph;
  }

  Provider<?> getProvider(Key key) {
    Binding binding = graph.get(key);
    if (binding instanceof LinkedBinding<?>) {
      return (Provider<?>) binding;
    }
    if (binding == null) {
      throw new IllegalArgumentException("No provider available for " + key);
    }
    return Linker.getLinked(graph, key);
  }
}
