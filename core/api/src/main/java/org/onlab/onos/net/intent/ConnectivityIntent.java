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
package org.onlab.onos.net.intent;

import com.google.common.collect.ImmutableSet;
import org.onlab.onos.core.ApplicationId;
import org.onlab.onos.net.Link;
import org.onlab.onos.net.NetworkResource;
import org.onlab.onos.net.flow.TrafficSelector;
import org.onlab.onos.net.flow.TrafficTreatment;

import java.util.Collection;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Abstraction of connectivity intent for traffic matching some criteria.
 */
public abstract class ConnectivityIntent extends Intent {

    // TODO: other forms of intents should be considered for this family:
    //   point-to-point with constraints (waypoints/obstacles)
    //   multi-to-single point with constraints (waypoints/obstacles)
    //   single-to-multi point with constraints (waypoints/obstacles)
    //   concrete path (with alternate)
    //   ...

    private final TrafficSelector selector;
    private final TrafficTreatment treatment;

    /**
     * Creates a connectivity intent that matches on the specified selector
     * and applies the specified treatment.
     *
     * @param id        intent identifier
     * @param appId     application identifier
     * @param resources required network resources (optional)
     * @param selector  traffic selector
     * @param treatment treatment
     * @throws NullPointerException if the selector or treatement is null
     */
    protected ConnectivityIntent(IntentId id, ApplicationId appId,
                                 Collection<NetworkResource> resources,
                                 TrafficSelector selector,
                                 TrafficTreatment treatment) {
        super(id, appId, resources);
        this.selector = checkNotNull(selector);
        this.treatment = checkNotNull(treatment);
    }

    /**
     * Constructor for serializer.
     */
    protected ConnectivityIntent() {
        super();
        this.selector = null;
        this.treatment = null;
    }

    /**
     * Returns the match specifying the type of traffic.
     *
     * @return traffic match
     */
    public TrafficSelector selector() {
        return selector;
    }

    /**
     * Returns the action applied to the traffic.
     *
     * @return applied action
     */
    public TrafficTreatment treatment() {
        return treatment;
    }

    /**
     * Produces a collection of network resources from the given links.
     *
     * @param links collection of links
     * @return collection of link resources
     */
    protected static Collection<NetworkResource> resources(Collection<Link> links) {
        return ImmutableSet.<NetworkResource>copyOf(links);
    }

}
