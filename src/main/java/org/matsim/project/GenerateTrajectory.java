package org.matsim.project;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.matsim.api.core.v01.events.*;
import org.matsim.api.core.v01.events.handler.*;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 * initialize CSV_FILE as args
 */

public class GenerateTrajectory implements LinkEnterEventHandler, LinkLeaveEventHandler, ActivityEndEventHandler, ActivityStartEventHandler, PersonDepartureEventHandler, PersonArrivalEventHandler, PersonLeavesVehicleEventHandler, PersonEntersVehicleEventHandler {

    private Network network;
    List<Object[]> data = new ArrayList<>();
    public static final String CSV_FILE = "scenarios/berlin/output/trajectory_data.csv";

    public GenerateTrajectory(Network network) {
        this.network = network;
    }

    @Override
    public void reset(int iteration) {
        System.out.println("reset...");
    }

    @Override
    public void handleEvent(ActivityEndEvent event) {
        Link link = network.getLinks().get( event.getLinkId());
        data.add(new Object[] {event.getPersonId().toString(), "NA", event.getTime(), event.getLinkId().toString(), link.getCoord().getX(), link.getCoord().getY(), event.getActType(), event.getEventType(), link.getLength(), "NA"});
    }

    @Override
    public void handleEvent(PersonDepartureEvent event) {
        Link link = network.getLinks().get( event.getLinkId());
        data.add(new Object[] {event.getPersonId().toString(), "NA", event.getTime(), event.getLinkId().toString(), link.getCoord().getX(), link.getCoord().getY(), "NA", event.getEventType(), link.getLength(), event.getLegMode()});
    }

    @Override
    public void handleEvent(PersonEntersVehicleEvent event) {
        data.add(new Object[] {event.getPersonId().toString(), event.getVehicleId(), event.getTime(), "NA", "NA", "NA", "NA", event.getEventType(), "NA", "NA"});
    }

    @Override
    public void handleEvent(LinkEnterEvent event) {
        Link link = network.getLinks().get( event.getLinkId());
        data.add(new Object[] {"NA", event.getVehicleId(), event.getTime(), event.getLinkId().toString(), link.getCoord().getX(), link.getCoord().getY(), "NA", event.getEventType(), link.getLength(), "NA"});
    }

    @Override
    public void handleEvent(LinkLeaveEvent event) {
        Link link = network.getLinks().get( event.getLinkId());
        data.add(new Object[] {"NA", event.getVehicleId(), event.getTime(), event.getLinkId().toString(), link.getCoord().getX(), link.getCoord().getY(), "NA", event.getEventType(), link.getLength(), "NA"});
    }

    @Override
    public void handleEvent(PersonLeavesVehicleEvent event) {
        data.add(new Object[] {event.getPersonId().toString(), event.getVehicleId(), event.getTime(), "NA", "NA", "NA", "NA", event.getEventType(), "NA", "NA"});
    }

    @Override
    public void handleEvent(PersonArrivalEvent event) {
        Link link = network.getLinks().get( event.getLinkId());
        data.add(new Object[] {event.getPersonId().toString(), "NA", event.getTime(), event.getLinkId().toString(), link.getCoord().getX(), link.getCoord().getY(), "NA", event.getEventType(), link.getLength(), event.getLegMode()});
    }

    @Override
    public void handleEvent(ActivityStartEvent event) {
        Link link = network.getLinks().get( event.getLinkId());
        data.add(new Object[] {event.getPersonId().toString(), "NA", event.getTime(), event.getLinkId().toString(), link.getCoord().getX(), link.getCoord().getY(), event.getActType(), event.getEventType(), link.getLength(), "NA"});
    }


    public void writeTrajectory(){
        try {
            BufferedWriter writer = Files.newBufferedWriter(Paths.get(CSV_FILE));
            CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader("person_id", "veh_id", "time", "link_id", "x", "y", "act_type", "event_type", "length", "leg_mode"));
            csvPrinter.printRecords(data);
            csvPrinter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
