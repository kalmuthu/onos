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
package org.onlab.onos.store.topology.impl;

import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;
import java.util.Set;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Service;
import org.onlab.onos.event.Event;
import org.onlab.onos.net.ConnectPoint;
import org.onlab.onos.net.DeviceId;
import org.onlab.onos.net.Link;
import org.onlab.onos.net.Path;
import org.onlab.onos.net.provider.ProviderId;
import org.onlab.onos.net.topology.ClusterId;
import org.onlab.onos.net.topology.GraphDescription;
import org.onlab.onos.net.topology.LinkWeight;
import org.onlab.onos.net.topology.Topology;
import org.onlab.onos.net.topology.TopologyCluster;
import org.onlab.onos.net.topology.TopologyEvent;
import org.onlab.onos.net.topology.TopologyGraph;
import org.onlab.onos.net.topology.TopologyStore;
import org.onlab.onos.net.topology.TopologyStoreDelegate;
import org.onlab.onos.store.AbstractStore;
import org.slf4j.Logger;

/**
 * Manages inventory of topology snapshots using trivial in-memory
 * structures implementation.
 */
//FIXME: I LIE I AM NOT DISTRIBUTED
@Component(immediate = true)
@Service
public class DistributedTopologyStore
extends AbstractStore<TopologyEvent, TopologyStoreDelegate>
implements TopologyStore {

    private final Logger log = getLogger(getClass());

    private volatile DefaultTopology current;

    @Activate
    public void activate() {
        log.info("Started");
    }

    @Deactivate
    public void deactivate() {
        log.info("Stopped");
    }
    @Override
    public Topology currentTopology() {
        return current;
    }

    @Override
    public boolean isLatest(Topology topology) {
        // Topology is current only if it is the same as our current topology
        return topology == current;
    }

    @Override
    public TopologyGraph getGraph(Topology topology) {
        return defaultTopology(topology).getGraph();
    }

    @Override
    public Set<TopologyCluster> getClusters(Topology topology) {
        return defaultTopology(topology).getClusters();
    }

    @Override
    public TopologyCluster getCluster(Topology topology, ClusterId clusterId) {
        return defaultTopology(topology).getCluster(clusterId);
    }

    @Override
    public Set<DeviceId> getClusterDevices(Topology topology, TopologyCluster cluster) {
        return defaultTopology(topology).getClusterDevices(cluster);
    }

    @Override
    public Set<Link> getClusterLinks(Topology topology, TopologyCluster cluster) {
        return defaultTopology(topology).getClusterLinks(cluster);
    }

    @Override
    public Set<Path> getPaths(Topology topology, DeviceId src, DeviceId dst) {
        return defaultTopology(topology).getPaths(src, dst);
    }

    @Override
    public Set<Path> getPaths(Topology topology, DeviceId src, DeviceId dst,
            LinkWeight weight) {
        return defaultTopology(topology).getPaths(src, dst, weight);
    }

    @Override
    public boolean isInfrastructure(Topology topology, ConnectPoint connectPoint) {
        return defaultTopology(topology).isInfrastructure(connectPoint);
    }

    @Override
    public boolean isBroadcastPoint(Topology topology, ConnectPoint connectPoint) {
        return defaultTopology(topology).isBroadcastPoint(connectPoint);
    }

    @Override
    public TopologyEvent updateTopology(ProviderId providerId,
            GraphDescription graphDescription,
            List<Event> reasons) {
        // First off, make sure that what we're given is indeed newer than
        // what we already have.
        if (current != null && graphDescription.timestamp() < current.time()) {
            return null;
        }

        // Have the default topology construct self from the description data.
        DefaultTopology newTopology =
                new DefaultTopology(providerId, graphDescription);

        // Promote the new topology to current and return a ready-to-send event.
        synchronized (this) {
            current = newTopology;
            return new TopologyEvent(TopologyEvent.Type.TOPOLOGY_CHANGED,
                                     current, reasons);
        }
    }

    // Validates the specified topology and returns it as a default
    private DefaultTopology defaultTopology(Topology topology) {
        if (topology instanceof DefaultTopology) {
            return (DefaultTopology) topology;
        }
        throw new IllegalArgumentException("Topology class " + topology.getClass() +
                " not supported");
    }

}
