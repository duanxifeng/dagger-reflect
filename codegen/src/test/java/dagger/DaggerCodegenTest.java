/*
 * Copyright (C) 2018 Google Inc.
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
package dagger;

import org.junit.Test;
import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.fail;

public final class DaggerCodegenTest {
  @Test public void create() {
    NoBuilderComponent actual = Dagger.create(NoBuilderComponent.class);
    assertThat(actual).isInstanceOf(DaggerNoBuilderComponent.class);
  }

  @Test public void createNoAnnotation() {
    try {
      Dagger.create(NoBuilderComponentNoAnnotation.class);
      fail();
    } catch (IllegalStateException e) {
      assertThat(e).hasMessageThat()
          .isEqualTo("Unable to find generated component implementation "
              + "dagger.DaggerNoBuilderComponentNoAnnotation for component "
              + "dagger.NoBuilderComponentNoAnnotation");
    }
  }

  @Test public void builder() {
    BuilderComponent.Builder actual = Dagger.builder(BuilderComponent.Builder.class);
    assertThat(actual).isInstanceOf(DaggerBuilderComponent.builder().getClass());
  }

  @Test public void builderNoAnnotation() {
    try {
      Dagger.create(BuilderComponentNoAnnotation.class);
      fail();
    } catch (IllegalStateException e) {
      assertThat(e).hasMessageThat()
          .isEqualTo("Unable to find generated component implementation "
              + "dagger.DaggerBuilderComponentNoAnnotation for component "
              + "dagger.BuilderComponentNoAnnotation");
    }
  }

  @Test public void builderNotNested() {
    try {
      Dagger.builder(BuilderNotNested.class);
      fail();
    } catch (IllegalArgumentException e) {
      assertThat(e).hasMessageThat()
          .isEqualTo(
              "dagger.BuilderNotNested is not a nested type inside of a component interface");
    }
  }
}
