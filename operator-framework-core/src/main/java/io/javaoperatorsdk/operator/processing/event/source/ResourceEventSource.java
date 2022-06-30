package io.javaoperatorsdk.operator.processing.event.source;

import java.util.Optional;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import io.fabric8.kubernetes.api.model.HasMetadata;
import io.javaoperatorsdk.operator.processing.ResourceOwner;

public interface ResourceEventSource<R, P extends HasMetadata> extends EventSource,
    ResourceOwner<R, P> {

  default Optional<R> getSecondaryResource(P primary) {
    var resources = getSecondaryResources(primary);
    if (resources.isEmpty()) {
      return Optional.empty();
    } else if (resources.size() == 1) {
      return Optional.of(resources.iterator().next());
    } else {
      throw new IllegalStateException("More than 1 secondary resource related to primary");
    }

  }

  Set<R> getSecondaryResources(P primary);

  void setOnAddFilter(Predicate<R> onAddFilter);

  void setOnUpdateFilter(BiPredicate<R, R> onUpdateFilter);

  void setOnDeleteFilter(BiPredicate<R, Boolean> onDeleteFilter);

  void setGenericFilter(Predicate<R> genericFilter);
}
