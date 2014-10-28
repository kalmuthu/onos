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
package org.onlab.onos.store.serializers;

import org.onlab.onos.net.ConnectPoint;
import org.onlab.onos.net.DefaultLink;
import org.onlab.onos.net.Link.Type;
import org.onlab.onos.net.provider.ProviderId;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

/**
 * Kryo Serializer for {@link DefaultLink}.
 */
public class DefaultLinkSerializer extends Serializer<DefaultLink> {

    /**
     * Creates {@link DefaultLink} serializer instance.
     */
    public DefaultLinkSerializer() {
        // non-null, immutable
        super(false, true);
    }

    @Override
    public void write(Kryo kryo, Output output, DefaultLink object) {
        kryo.writeClassAndObject(output, object.providerId());
        kryo.writeClassAndObject(output, object.src());
        kryo.writeClassAndObject(output, object.dst());
        kryo.writeClassAndObject(output, object.type());
    }

    @Override
    public DefaultLink read(Kryo kryo, Input input, Class<DefaultLink> type) {
        ProviderId providerId = (ProviderId) kryo.readClassAndObject(input);
        ConnectPoint src = (ConnectPoint) kryo.readClassAndObject(input);
        ConnectPoint dst = (ConnectPoint) kryo.readClassAndObject(input);
        Type linkType = (Type) kryo.readClassAndObject(input);
        return new DefaultLink(providerId, src, dst, linkType);
    }
}
