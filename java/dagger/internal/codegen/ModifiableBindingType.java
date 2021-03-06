/*
 * Copyright (C) 2018 The Dagger Authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dagger.internal.codegen;

import com.google.common.collect.ImmutableSet;

/**
 * A label for a binding indicating whether, and how, it may be redefined across implementations of
 * a subcomponent.
 *
 * <p>A subcomponent has multiple implementations only when generating ahead-of-time subcomponents.
 * Specifically, each subcomponent type in a component hierarchy is implemented as an abstract
 * class, and descendent components are implemented as abstract inner classes. A consequence of this
 * is that a given subcomponent has an implementation for each ancestor component. Each
 * implementation represents a different sub-binding-graph of the full subcomponent. A binding is
 * modifiable if it's definition may change depending on the characteristics of its ancestor
 * components.
 */
enum ModifiableBindingType {
  /** A binding that is not modifiable */
  NONE,

  /**
   * A binding that is missing when generating the abstract base class implementation of a
   * subcomponent.
   */
  MISSING,

  /**
   * A binding that requires an instance of a generated type. These binding are modifiable in the
   * sense that they are encapsulated in a method when they are first required, possibly in an
   * abstract implementation of a subcomponent, where, in general, no concrete instances of
   * generated types are available, and the method is satisfied in a final concrete implementation.
   */
  GENERATED_INSTANCE,

  /**
   * Multibindings may have contributions come from any ancestor component. Therefore, each
   * implementation of a subcomponent may have newly available contributions, and so the binding
   * method is reimplemented with each subcomponent implementation.
   */
  MULTIBINDING,

  /**
   * A Optional binding that may be empty when looking at a partial binding graph, but bound to a
   * value when considering the complete binding graph, thus modifiable across subcomponent
   * implementations.
   */
  OPTIONAL,

  /**
   * If a binding is defined according to an {@code @Inject} annotated constructor on the object it
   * is valid for that binding to be redefined a single time by an {@code @Provides} annotated
   * module method. It is possible that the {@code @Provides} binding isn't available in a partial
   * binding graph, but becomes available when considering a more complete binding graph, therefore
   * such bindings are modifiable across subcomponent implementations.
   */
  INJECTION,

  /**
   * If a binding requires an instance of a module then it is possible for that same module to be
   * re-instantiated with different state by an ancestor component and thereby bind to a different
   * instance of the same object. For this reason we reimplement the binding in the base
   * implementation of the subcomponent, but then allow for all modifiations by re-implementing the
   * binding when generating the root component. This allows for as much of the known binding graph
   * to be implemented as early as possible, even if the binding requiring a module must be
   * overridden later on.
   */
  MODULE_INSTANCE,

  /**
   * {@link dagger.producers.ProductionScope} is a unique scope that is allowed on multiple
   * components. In Ahead-of-Time mode, we don't actually know what component will end up owning the
   * binding because a parent could install the same module or also be an @ProductionScoped @Inject
   * constructor.
   *
   * <p>We don't apply the same logic to @Reusable, even though it can also be on multiple
   * components, because it is by definition ok to be reimplemented across multiple components.
   * Allowing @Reusable bindings to be redefined could only result in more code for subclass
   * implementations.
   *
   * <p>All production bindings are also treated as modifiable since they are implicitly {@link
   * dagger.producers.ProductionScope} in {@link dagger.producers.internal.AbstractProducer}. If an
   * ancestor component includes the same module as a descendant component, the descendant's
   * subclass implementation will need to be replaced with the ancestor's {@link
   * dagger.producers.Producer} instance. beder@ believes this to be a bug and that, because
   * {@code @Produces} methods are implicitly scoped, descendant components should not be allowed to
   * redefine the same module as an ancestor. If we disallow that, we can stop treating all
   * {@code @Produces} methods as modifiable.
   */
  PRODUCTION,
  ;

  private static final ImmutableSet<ModifiableBindingType> TYPES_WITH_BASE_CLASS_IMPLEMENTATIONS =
      ImmutableSet.of(NONE, INJECTION, MODULE_INSTANCE, MULTIBINDING, OPTIONAL, PRODUCTION);

  boolean isModifiable() {
    return !equals(NONE);
  }

  /**
   * Returns true if the method encapsulating the modifiable binding should have a concrete
   * implementation in the abstract base class for a subcomponent.
   */
  boolean hasBaseClassImplementation() {
    return TYPES_WITH_BASE_CLASS_IMPLEMENTATIONS.contains(this);
  }
}
