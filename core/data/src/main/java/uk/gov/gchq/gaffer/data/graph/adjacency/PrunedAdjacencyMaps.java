/*
 * Copyright 2017 Crown Copyright
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

package uk.gov.gchq.gaffer.data.graph.adjacency;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * A {@code PrunedAdjacencyMaps} object represents a collection of {@link
 * AdjacencyMap}s containing graph adjacency information.
 * <p>
 * As the {@link AdjacencyMap}s are added, a comparison is made between the
 * destination vertices of the previous map and the source vertices of the newly
 * added map. Any entries in the preceding map which do not join up with a
 * source vertex in the new map are deemed to be orphaned paths, and are
 * removed.
 *
 * @param <T> the type of object representing the vertices
 * @param <U> the type of object representing the edges
 */
public class PrunedAdjacencyMaps<T, U> implements AdjacencyMaps<T, U> {

    private final List<AdjacencyMap<T, U>> adjacencyMaps = new ArrayList<>();

    @Override
    public void add(final AdjacencyMap<T, U> adjacencyMap) {
        removeOrphans(adjacencyMaps, adjacencyMap);
        adjacencyMaps.add(adjacencyMap);
    }

    private void removeOrphans(final List<AdjacencyMap<T, U>> maps, final AdjacencyMap<T, U> adjacencyMap) {
        if (!maps.isEmpty()) {
            final AdjacencyMap<T, U> prev = maps.get(maps.size() - 1);

            final Set<T> prevDestinations = prev.getAllDestinations();

            final List<T> nodesToRemove = new ArrayList<>();

            for (final T dest : prevDestinations) {
                if (!adjacencyMap.getAllSources().contains(dest)) {
                    nodesToRemove.add(dest);
                }
            }

            for (final T dest : nodesToRemove) {
                prev.removeAllWithDestination(dest);
            }

            removeOrphans(maps.subList(0, maps.size() - 1), prev);
        }
    }

    @Override
    public List<AdjacencyMap<T, U>> asList() {
        return adjacencyMaps;
    }
}
