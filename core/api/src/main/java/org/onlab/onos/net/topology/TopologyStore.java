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

import org.onlab.onos.event.Event;
import org.onlab.onos.net.ConnectPoint;
import org.onlab.onos.net.DeviceId;
import org.onlab.onos.net.Link;
import org.onlab.onos.net.Path;
import org.onlab.onos.net.provider.ProviderId;
import org.onlab.onos.store.Store;

import java.util.List;
import java.util.Set;

/**
 * Manages inventory of topology snapshots; not intended for direct use.
 */
public interface TopologyStore extends Store<TopologyEvent, TopologyStoreDelegate> {

    /**
     * Returns the current topology snapshot.
     *
     * @return current topology descriptor
     */
    Topology currentTopology();

    /**
     * Indicates whether the topology is the latest.
     *
     * @param topology topology descriptor
     * @return true if topology is the most recent one
     */
    boolean isLatest(Topology topology);

    /**
     * Returns the immutable graph view of the current topology.
     *
     * @param topology topology descriptor
     * @return graph view
     */
    TopologyGraph getGraph(Topology topology);

    /**
     * Returns the set of topology SCC clusters.
     *
     * @param topology topology descriptor
     * @return set of clusters
     */
    Set<TopologyCluster> getClusters(Topology topology);

    /**
     * Returns the cluster of the specified topology.
     *
     * @param topology  topology descriptor
     * @param clusterId cluster identity
     * @return topology cluster
     */
    TopologyCluster getCluster(Topology topology, ClusterId clusterId);

    /**
     * Returns the cluster of the specified topology.
     *
     * @param topology topology descriptor
     * @param cluster  topology cluster
     * @return set of cluster links
     */
    Set<DeviceId> getClusterDevices(Topology topology, TopologyCluster cluster);

    /**
     * Returns the cluster of the specified topology.
     *
     * @param topology topology descriptor
     * @param cluster  topology cluster
     * @return set of cluster links
     */
    Set<Link> getClusterLinks(Topology topology, TopologyCluster cluster);

    /**
     * Returns the set of pre-computed shortest paths between src and dest.
     *
     * @param topology topology descriptor
     * @param src      source device
     * @param dst      destination device
     * @return set of shortest paths
     */
    Set<Path> getPaths(Topology topology, DeviceId src, DeviceId dst);

    /**
     * Computes and returns the set of shortest paths between src and dest.
     *
     * @param topology topology descriptor
     * @param src      source device
     * @param dst      destination device
     * @param weight   link weight function
     * @return set of shortest paths
     */
    Set<Path> getPaths(Topology topology, DeviceId src, DeviceId dst,
                       LinkWeight weight);

    /**
     * Indicates whether the given connect point is part of the network fabric.
     *
     * @param topology     topology descriptor
     * @param connectPoint connection point
     * @return true if infrastructure; false otherwise
     */
    boolean isInfrastructure(Topology topology, ConnectPoint connectPoint);

    /**
     * Indicates whether broadcast is allowed for traffic received on the
     * given connection point.
     *
     * @param topology     topology descriptor
     * @param connectPoint connection point
     * @return true if broadcast allowed; false otherwise
     */
    boolean isBroadcastPoint(Topology topology, ConnectPoint connectPoint);

    /**
     * Generates a new topology snapshot from the specified description.
     *
     * @param providerId       provider identification
     * @param graphDescription topology graph description
     * @param reasons          list of events that triggered the update
     * @return topology update event or null if the description is old
     */
    TopologyEvent updateTopology(ProviderId providerId,
                                 GraphDescription graphDescription,
                                 List<Event> reasons);
}
