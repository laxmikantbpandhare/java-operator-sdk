package io.javaoperatorsdk.operator.api.config;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import io.fabric8.kubernetes.api.model.HasMetadata;
import io.javaoperatorsdk.operator.api.config.dependent.DependentResourceSpec;
import io.javaoperatorsdk.operator.api.config.eventsource.EventSourceSpec;
import io.javaoperatorsdk.operator.processing.event.source.controller.ResourceEventFilter;
import io.javaoperatorsdk.operator.processing.retry.Retry;

@SuppressWarnings("rawtypes")
public class DefaultControllerConfiguration<R extends HasMetadata>
    extends DefaultResourceConfiguration<R>
    implements ControllerConfiguration<R> {

  private final String associatedControllerClassName;
  private final String name;
  private final String crdName;
  private final String finalizer;
  private final boolean generationAware;
  private final Retry retry;
  private final ResourceEventFilter<R> resourceEventFilter;
  private final List<EventSourceSpec> eventSourceSpecs;
  private final List<DependentResourceSpec> dependents;
  private final Duration reconciliationMaxInterval;

  // NOSONAR constructor is meant to provide all information
  public DefaultControllerConfiguration(
      String associatedControllerClassName,
      String name,
      String crdName,
      String finalizer,
      boolean generationAware,
      Set<String> namespaces,
      Retry retry,
      String labelSelector,
      ResourceEventFilter<R> resourceEventFilter,
      Class<R> resourceClass,
      Duration reconciliationMaxInterval,
      List<EventSourceSpec> eventSourceSpecs,
      List<DependentResourceSpec> dependents) {
    super(labelSelector, resourceClass, namespaces);
    this.associatedControllerClassName = associatedControllerClassName;
    this.name = name;
    this.crdName = crdName;
    this.finalizer = finalizer;
    this.generationAware = generationAware;
    this.eventSourceSpecs = eventSourceSpecs;
    this.reconciliationMaxInterval = reconciliationMaxInterval;
    this.retry =
        retry == null
            ? ControllerConfiguration.super.getRetry()
            : retry;
    this.resourceEventFilter = resourceEventFilter;

    this.dependents = dependents != null ? dependents : Collections.emptyList();
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getResourceTypeName() {
    return crdName;
  }

  @Override
  public String getFinalizerName() {
    return finalizer;
  }

  @Override
  public boolean isGenerationAware() {
    return generationAware;
  }

  @Override
  public String getAssociatedReconcilerClassName() {
    return associatedControllerClassName;
  }

  @Override
  public Retry getRetry() {
    return retry;
  }

  @Override
  public ResourceEventFilter<R> getEventFilter() {
    return resourceEventFilter;
  }

  @Override
  public List<DependentResourceSpec> getDependentResources() {
    return dependents;
  }

  @Override
  public Optional<Duration> reconciliationMaxInterval() {
    return Optional.ofNullable(reconciliationMaxInterval);
  }

  @Override
  public List<EventSourceSpec> getEventSources() {
    if (eventSourceSpecs == null) {
      return Collections.emptyList();
    }
    return eventSourceSpecs;
  }
}
