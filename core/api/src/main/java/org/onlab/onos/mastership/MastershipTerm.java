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
package org.onlab.onos.mastership;

import java.util.Objects;

import org.onlab.onos.cluster.NodeId;

import com.google.common.base.MoreObjects;

public final class MastershipTerm {

    private final NodeId master;
    private final int termNumber;

    private MastershipTerm(NodeId master, int term) {
        this.master = master;
        this.termNumber = term;
    }

    public static MastershipTerm of(NodeId master, int term) {
        return new MastershipTerm(master, term);
    }

    public NodeId master() {
        return master;
    }

    public int termNumber() {
        return termNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hash(master, termNumber);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other instanceof MastershipTerm) {
            MastershipTerm that = (MastershipTerm) other;
            if (!this.master.equals(that.master)) {
                return false;
            }
            if (this.termNumber != that.termNumber) {
                return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(getClass())
                .add("master", this.master)
                .add("termNumber", this.termNumber)
                .toString();
    }
}
