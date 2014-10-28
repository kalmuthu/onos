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
package org.onlab.onos.provider.of.flow.impl;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.onlab.onos.net.flow.FlowRule;
import org.onlab.onos.net.flow.TrafficTreatment;
import org.onlab.onos.net.flow.instructions.Instruction;
import org.onlab.onos.net.flow.instructions.Instructions.OutputInstruction;
import org.onlab.onos.net.flow.instructions.L2ModificationInstruction;
import org.onlab.onos.net.flow.instructions.L2ModificationInstruction.ModEtherInstruction;
import org.onlab.onos.net.flow.instructions.L2ModificationInstruction.ModVlanIdInstruction;
import org.onlab.onos.net.flow.instructions.L2ModificationInstruction.ModVlanPcpInstruction;
import org.onlab.onos.net.flow.instructions.L3ModificationInstruction;
import org.onlab.onos.net.flow.instructions.L3ModificationInstruction.ModIPInstruction;
import org.projectfloodlight.openflow.protocol.OFFactory;
import org.projectfloodlight.openflow.protocol.OFFlowAdd;
import org.projectfloodlight.openflow.protocol.OFFlowDelete;
import org.projectfloodlight.openflow.protocol.OFFlowMod;
import org.projectfloodlight.openflow.protocol.OFFlowModFlags;
import org.projectfloodlight.openflow.protocol.action.OFAction;
import org.projectfloodlight.openflow.protocol.match.Match;
import org.projectfloodlight.openflow.types.IPv4Address;
import org.projectfloodlight.openflow.types.MacAddress;
import org.projectfloodlight.openflow.types.OFBufferId;
import org.projectfloodlight.openflow.types.OFPort;
import org.projectfloodlight.openflow.types.U64;
import org.projectfloodlight.openflow.types.VlanPcp;
import org.projectfloodlight.openflow.types.VlanVid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Flow mod builder for OpenFlow 1.0.
 */
public class FlowModBuilderVer10 extends FlowModBuilder {

    private static final Logger log = LoggerFactory.getLogger(FlowModBuilderVer10.class);

    private final TrafficTreatment treatment;

    /**
     * Constructor for a flow mod builder for OpenFlow 1.0.
     *
     * @param flowRule the flow rule to transform into a flow mod
     * @param factory the OpenFlow factory to use to build the flow mod
     */
    protected FlowModBuilderVer10(FlowRule flowRule, OFFactory factory) {
        super(flowRule, factory);

        this.treatment = flowRule.treatment();
    }

    @Override
    public OFFlowAdd buildFlowAdd() {
        Match match = buildMatch();
        List<OFAction> actions = buildActions();

        long cookie = flowRule().id().value();

        //TODO: what to do without bufferid? do we assume that there will be a pktout as well?
        OFFlowAdd fm = factory().buildFlowAdd()
                .setXid(cookie)
                .setCookie(U64.of(cookie))
                .setBufferId(OFBufferId.NO_BUFFER)
                .setActions(actions)
                .setMatch(match)
                .setFlags(Collections.singleton(OFFlowModFlags.SEND_FLOW_REM))
                .setPriority(flowRule().priority())
                .build();

        return fm;
    }

    @Override
    public OFFlowMod buildFlowMod() {
        Match match = buildMatch();
        List<OFAction> actions = buildActions();

        long cookie = flowRule().id().value();

        //TODO: what to do without bufferid? do we assume that there will be a pktout as well?
        OFFlowMod fm = factory().buildFlowModify()
                .setXid(cookie)
                .setCookie(U64.of(cookie))
                .setBufferId(OFBufferId.NO_BUFFER)
                .setActions(actions)
                .setMatch(match)
                .setFlags(Collections.singleton(OFFlowModFlags.SEND_FLOW_REM))
                .setPriority(flowRule().priority())
                .build();

        return fm;
    }

    @Override
    public OFFlowDelete buildFlowDel() {
        Match match = buildMatch();
        List<OFAction> actions = buildActions();

        long cookie = flowRule().id().value();

        OFFlowDelete fm = factory().buildFlowDelete()
                .setXid(cookie)
                .setCookie(U64.of(cookie))
                .setBufferId(OFBufferId.NO_BUFFER)
                .setActions(actions)
                .setMatch(match)
                .setFlags(Collections.singleton(OFFlowModFlags.SEND_FLOW_REM))
                .setPriority(flowRule().priority())
                .build();

        return fm;
    }

    private List<OFAction> buildActions() {
        List<OFAction> acts = new LinkedList<>();
        if (treatment == null) {
            return acts;
        }
        for (Instruction i : treatment.instructions()) {
            switch (i.type()) {
            case DROP:
                log.warn("Saw drop action; assigning drop action");
                return new LinkedList<>();
            case L2MODIFICATION:
                acts.add(buildL2Modification(i));
                break;
            case L3MODIFICATION:
                acts.add(buildL3Modification(i));
                break;
            case OUTPUT:
                OutputInstruction out = (OutputInstruction) i;
                acts.add(factory().actions().buildOutput().setPort(
                        OFPort.of((int) out.port().toLong())).build());
                break;
            case L0MODIFICATION:
            case GROUP:
                log.warn("Instruction type {} not supported with protocol version {}",
                        i.type(), factory().getVersion());
                break;
            default:
                log.warn("Instruction type {} not yet implemented.", i.type());
            }
        }

        return acts;
    }

    private OFAction buildL3Modification(Instruction i) {
        L3ModificationInstruction l3m = (L3ModificationInstruction) i;
        ModIPInstruction ip;
        switch (l3m.subtype()) {
        case IP_DST:
            ip = (ModIPInstruction) i;
            return factory().actions().setNwDst(IPv4Address.of(ip.ip().toInt()));
        case IP_SRC:
            ip = (ModIPInstruction) i;
            return factory().actions().setNwSrc(IPv4Address.of(ip.ip().toInt()));
        default:
            log.warn("Unimplemented action type {}.", l3m.subtype());
            break;
        }
        return null;
    }

    private OFAction buildL2Modification(Instruction i) {
        L2ModificationInstruction l2m = (L2ModificationInstruction) i;
        ModEtherInstruction eth;
        switch (l2m.subtype()) {
        case ETH_DST:
            eth = (ModEtherInstruction) l2m;
            return factory().actions().setDlDst(MacAddress.of(eth.mac().toLong()));
        case ETH_SRC:
            eth = (ModEtherInstruction) l2m;
            return factory().actions().setDlSrc(MacAddress.of(eth.mac().toLong()));
        case VLAN_ID:
            ModVlanIdInstruction vlanId = (ModVlanIdInstruction) l2m;
            return factory().actions().setVlanVid(VlanVid.ofVlan(vlanId.vlanId.toShort()));
        case VLAN_PCP:
            ModVlanPcpInstruction vlanPcp = (ModVlanPcpInstruction) l2m;
            return factory().actions().setVlanPcp(VlanPcp.of(vlanPcp.vlanPcp()));
        default:
            log.warn("Unimplemented action type {}.", l2m.subtype());
            break;
        }
        return null;
    }

}
