/*
 * Copyright (C) 2014 Google, Inc.
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

/**
 * The collection of error messages to be reported back to users.
 *
 * @author Gregory Kick
 * @since 2.0
 */
final class ErrorMessages {
  /*
   * JSR-330 errors
   *
   * These are errors that are explicitly outlined in the JSR-330 APIs
   */

  /* constructors */
  static final String MULTIPLE_INJECT_CONSTRUCTORS =
      "Types may only contain one @Inject constructor.";

  /* fields */
  static final String FINAL_INJECT_FIELD = "@Inject fields may not be final";

  /* methods */
  static final String ABSTRACT_INJECT_METHOD = "Methods with @Inject may not be abstract.";
  static final String GENERIC_INJECT_METHOD =
      "Methods with @Inject may not declare type parameters.";

  /* qualifiers */
  static final String MULTIPLE_QUALIFIERS =
      "A single injection site may not use more than one @Qualifier.";

  /* scope */
  static final String MULTIPLE_SCOPES = "A single binding may not declare more than one @Scope.";

  /*
   * Dagger errors
   *
   * These are errors that arise due to restrictions imposed by the dagger implementation.
   */

  /* constructors */
  static final String INJECT_ON_PRIVATE_CONSTRUCTOR =
      "Dagger does not support injection into private constructors";
  static final String INJECT_CONSTRUCTOR_ON_INNER_CLASS =
      "@Inject constructors are invalid on inner classes";
  static final String INJECT_CONSTRUCTOR_ON_GENERIC_CLASS =
      "Generic types may not use @Inject constructors. "
          + "Use a @Provides method to bind the type parameters.";
  static final String INJECT_CONSTRUCTOR_ON_ABSTRACT_CLASS =
      "@Inject is nonsense on the constructor of an abstract class";

  /* fields */
  static final String PRIVATE_INJECT_FIELD =
      "Dagger does not support injection into private fields";

  /* methods */
  static final String PRIVATE_INJECT_METHOD =
      "Dagger does not support injection into private methods";

  /* all */
  static final String INJECT_INTO_PRIVATE_CLASS =
      "Dagger does not support injection into private classes";

  private ErrorMessages() {}
}
