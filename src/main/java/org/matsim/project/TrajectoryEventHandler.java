package org.matsim.project;

import org.apache.commons.io.FileUtils;
import org.matsim.api.core.v01.events.*;
import org.matsim.api.core.v01.events.handler.LinkEnterEventHandler;
import org.matsim.api.core.v01.events.handler.LinkLeaveEventHandler;
import org.matsim.api.core.v01.events.handler.PersonArrivalEventHandler;
import org.matsim.api.core.v01.events.handler.PersonDepartureEventHandler;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.core.events.handler.BasicEventHandler;
import org.matsim.core.events.handler.EventHandler;


import static org.matsim.project.RunMatsimFromExamplesUtils.stringOfData;

/**
 * This event handler prints some event information to the console.
 * @author: Lucas Soares
 *
 */

public class TrajectoryEventHandler implements LinkEnterEventHandler, LinkLeaveEventHandler, PersonArrivalEventHandler, PersonDepartureEventHandler {

    private Network network;


    public TrajectoryEventHandler(Network network) {
        this.network = network;
    }



    @Override
    public void reset(int iteration) {
        System.out.println("reset...");
    }

    @Override
    public void handleEvent(LinkEnterEvent event) {
        Link link = network.getLinks().get( event.getLinkId());
        stringOfData.append("LinkEnterEvent").append(",Time: ").append(event.getTime()).
                append(",LinkId: ").append(event.getLinkId()).append(",LinkCoord: ").
                append(link.getCoord()).append("\n");
        System.out.println("LinkEnterEvent");
        System.out.println("Time: " + event.getTime());
        System.out.println("LinkId: " + event.getLinkId());
        System.out.println("LinkCoord: " + link.getCoord());
    }

    @Override
    public void handleEvent(LinkLeaveEvent event) {
        Link link = network.getLinks().get( event.getLinkId());
        System.out.println("LinkLeaveEvent");
        System.out.println("Time: " + event.getTime());
        System.out.println("LinkId: " + event.getLinkId());
        System.out.println("LinkCoord: " + link.getCoord());
    }

    @Override
    public void handleEvent(PersonArrivalEvent event) {
        System.out.println("AgentArrivalEvent");
        System.out.println("Time: " + event.getTime());
        System.out.println("LinkId: " + event.getLinkId());
        System.out.println("PersonId: " + event.getPersonId());
    }

    @Override
    public void handleEvent(PersonDepartureEvent event) {
        System.out.println("AgentDepartureEvent");
        System.out.println("Time: " + event.getTime());
        System.out.println("LinkId: " + event.getLinkId());
        System.out.println("PersonId: " + event.getPersonId());
    }

}
