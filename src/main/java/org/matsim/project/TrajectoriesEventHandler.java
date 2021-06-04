package org.matsim.project;

import org.matsim.api.core.v01.events.LinkEnterEvent;
import org.matsim.api.core.v01.events.LinkLeaveEvent;
import org.matsim.api.core.v01.events.handler.LinkEnterEventHandler;
import org.matsim.api.core.v01.events.handler.LinkLeaveEventHandler;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;

import static org.matsim.project.RunMatsimFromExamplesUtils.stringOfData;


public class TrajectoriesEventHandler implements LinkEnterEventHandler, LinkLeaveEventHandler {

    private Network network;


    public TrajectoriesEventHandler(Network network) {
        this.network = network;
    }

    @Override
    public void reset(int iteration) {
        System.out.println("reset...");
    }

    @Override
    public void handleEvent(LinkEnterEvent event) {
        Link link = network.getLinks().get( event.getLinkId());
        stringOfData.append("VehId: ").append(event.getVehicleId()).append(",Time: ").append(event.getTime()).append(",LinkId: ").append(event.getLinkId()).append(",LinkCoord: ").append(link.getCoord()).append(",EventType: ").append(event.getEventType()).append("\n");
/*        System.out.println("VehId: " + event.getVehicleId());
        System.out.println("Time: " + event.getTime());
        System.out.println("LinkId: " + event.getLinkId());
        System.out.println("LinkCoord: " + link.getCoord());
        System.out.println("EventType: " + event.getEventType());*/
    }

    @Override
    public void handleEvent(LinkLeaveEvent event) {
        Link link = network.getLinks().get( event.getLinkId());
        stringOfData.append("VehId: ").append(event.getVehicleId()).append(" ,Time: ").append(event.getTime()).append(",LinkId: ").append(event.getLinkId()).append(",LinkCoord: ").append(link.getCoord()).append(",EventType: ").append(event.getEventType()).append("\n");
/*        System.out.println("VehId: " + event.getVehicleId());
        System.out.println("Time: " + event.getTime());
        System.out.println("LinkId: " + event.getLinkId());
        System.out.println("LinkCoord: " + link.getCoord());
        System.out.println("EventType: " + event.getEventType());*/
    }

}
