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
package org.onlab.onos.net.device;

import org.onlab.onos.net.AbstractDescription;
import org.onlab.onos.net.PortNumber;
import org.onlab.onos.net.SparseAnnotations;

import com.google.common.base.MoreObjects;

/**
 * Default implementation of immutable port description.
 */
public class DefaultPortDescription extends AbstractDescription
        implements PortDescription {

    private final PortNumber number;
    private final boolean isEnabled;

    /**
     * Creates a port description using the supplied information.
     *
     * @param number       port number
     * @param isEnabled    port enabled state
     * @param annotations  optional key/value annotations map
     */
    public DefaultPortDescription(PortNumber number, boolean isEnabled,
                SparseAnnotations... annotations) {
        super(annotations);
        this.number = number;
        this.isEnabled = isEnabled;
    }

    /**
     * Creates a port description using the supplied information.
     *
     * @param base         PortDescription to get basic information from
     * @param annotations  optional key/value annotations map
     */
    public DefaultPortDescription(PortDescription base,
            SparseAnnotations annotations) {
        this(base.portNumber(), base.isEnabled(), annotations);
    }

    @Override
    public PortNumber portNumber() {
        return number;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(getClass())
                .add("number", number)
                .add("isEnabled", isEnabled)
                .add("annotations", annotations())
                .toString();
    }

    // default constructor for serialization
    private DefaultPortDescription() {
        this.number = null;
        this.isEnabled = false;
    }
}
