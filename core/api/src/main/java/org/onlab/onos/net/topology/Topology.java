/*
 * Copyright 2014 Open Networking Laboratory
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
package org.onlab.onos.net.topology;

import org.onlab.onos.net.Provided;

/**
 * Represents a network topology computation snapshot.
 */
public interface Topology extends Provided {

    // FIXME: Following is not true right now. It is actually System.nanoTime(),
    // which has no relation to epoch time, wall clock, etc.
    /**
     * Returns the time, specified in milliseconds since start of epoch,
     * when the topology became active and made available.
     *
     * @return time in milliseconds since start of epoch
     */
    long time();

    /**
     * Returns the number of SCCs (strongly connected components) in the
     * topology.
     *
     * @return number of clusters
     */
    int clusterCount();

    /**
     * Returns the number of infrastructure devices in the topology.
     *
     * @return number of devices
     */
    int deviceCount();


    /**
     * Returns the number of infrastructure links in the topology.
     *
     * @return number of links
     */
    int linkCount();

    /**
     * Returns the number of infrastructure paths computed between devices
     * in the topology. This means the number of all the shortest paths
     * (hop-count) between all device pairs.
     *
     * @return number of paths
     */
    int pathCount();

}
