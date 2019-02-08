package dagger.reflect;

import dagger.reflect.Binding.LinkedBinding;
import dagger.reflect.Binding.UnlinkedBinding;
import java.util.LinkedHashMap;
import java.util.Map;
import org.jetbrains.annotations.Nullable;

final class Linker {
  static LinkedBinding<?> getLinked(BindingGraph graph, Key key) {
    return new Linker(graph).get(key);
  }

  private final BindingGraph graph;
  private final Map<Key, Binding> chain = new LinkedHashMap<>();

  private Linker(BindingGraph graph) {
    this.graph = graph;
  }

  LinkedBinding<?> get(Key key) {
    LinkedBinding<?> binding = find(key);
    if (binding != null) {
      return binding;
    }
    throw failure(key, "Missing binding", "was not found");
  }

  @Nullable LinkedBinding<?> find(Key key) {
    Binding binding = graph.get(key);
    if (binding instanceof UnlinkedBinding) {
      return performLinking(key, (UnlinkedBinding) binding);
    }
    return (LinkedBinding<?>) binding;
  }

  private LinkedBinding<?> performLinking(Key key, UnlinkedBinding binding) {
    if (chain.containsKey(key)) {
      throw failure(key, "Dependency cycle", "formed a cycle");
    }
    chain.put(key, binding);
    LinkedBinding<?> linkedBinding = binding.link(this);
    chain.remove(key);

    return graph.replaceLinked(key, binding, linkedBinding);
  }

  private RuntimeException failure(Key key, String reason, String cause) {
    StringBuilder builder = new StringBuilder(reason)
        .append(" for ")
        .append(key)
        .append('\n');
    for (Map.Entry<Key, Binding> entry : chain.entrySet()) {
      builder.append(" * Requested: ")
          .append(entry.getKey())
          .append("\n     from ")
          .append(entry.getValue())
          .append('\n');
    }
    builder.append(" * Requested: ")
        .append(key)
        .append("\n     which ")
        .append(cause)
        .append(".");
    throw new IllegalStateException(builder.toString());
  }
}
