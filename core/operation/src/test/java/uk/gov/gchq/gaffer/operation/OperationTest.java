/*
 * Copyright 2016-2021 Crown Copyright
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.gchq.gaffer.operation;

import com.google.common.collect.Maps;
import org.junit.jupiter.api.Test;

import uk.gov.gchq.gaffer.JSONSerialisationTest;
import uk.gov.gchq.koryphe.Since;
import uk.gov.gchq.koryphe.Summary;
import uk.gov.gchq.koryphe.ValidationResult;
import uk.gov.gchq.koryphe.util.SummaryUtil;
import uk.gov.gchq.koryphe.util.VersionUtil;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

public abstract class OperationTest<T extends Operation> extends JSONSerialisationTest<T> {
    protected Set<String> getRequiredFields() {
        return Collections.emptySet();
    }

    @Test
    public abstract void builderShouldCreatePopulatedOperation();

    @Test
    public abstract void shouldShallowCloneOperation();

    @Test
    public void shouldValidateRequiredFields() throws Exception {
        // Given
        final Operation op = getTestObject();

        // When
        final ValidationResult validationResult = op.validate();

        // Then
        final Set<String> requiredFields = getRequiredFields();
        final Set<String> requiredFieldsErrors = requiredFields.stream()
                .map(f -> f + " is required for: " + op.getClass().getSimpleName()).collect(Collectors.toSet());

        assertEquals(requiredFieldsErrors, validationResult.getErrors());
    }

    @Test
    public void shouldSetGetOption() throws Exception {
        final Operation testObject = getTestObject();
        final HashMap<String, String> expected = Maps.newHashMap();
        expected.put("one", "two");
        testObject.setOptions(expected);
        final Map<String, String> actual = testObject.getOptions();
        assertEquals(expected, actual);
        assertEquals("two", testObject.getOption("one"));

        testObject.addOption("three", "four");
        assertEquals("four", testObject.getOption("three"));
    }

    @Test
    public void shouldHaveSinceAnnotation() {
        // Given
        final T instance = getTestObject();

        // When
        final Since annotation = instance.getClass().getAnnotation(Since.class);

        // Then
        assertNotNull(annotation, "Missing Since annotation on class " + instance.getClass().getName());
        assertNotNull(annotation.value(), "Missing Since annotation on class " + instance.getClass().getName());
        assumeTrue(VersionUtil.validateVersionString(annotation.value()),
                annotation.value() + " is not a valid value string.");
    }

    @Test
    public void shouldHaveSummaryAnnotation() {
        // Given
        final T instance = getTestObject();

        // When
        final Summary annotation = instance.getClass().getAnnotation(Summary.class);

        assertNotNull(annotation, "Missing Since annotation on class " + instance.getClass().getName());
        assertNotNull(annotation.value(), "Missing Since annotation on class " + instance.getClass().getName());
        assumeTrue(SummaryUtil.validateSummaryString(annotation.value()),
                annotation.value() + " is not a valid value string.");
    }
}
