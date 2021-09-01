package org.matsim.project;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.events.*;
import org.matsim.api.core.v01.events.handler.*;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.population.Leg;
import org.matsim.api.core.v01.population.Person;
import org.matsim.api.core.v01.population.Plan;
import org.matsim.api.core.v01.population.Population;
import org.matsim.core.router.TripStructureUtils;
import org.matsim.vehicles.Vehicle;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GenerateTrajectory implements LinkEnterEventHandler, LinkLeaveEventHandler, ActivityEndEventHandler, ActivityStartEventHandler, PersonDepartureEventHandler, PersonArrivalEventHandler, PersonLeavesVehicleEventHandler, PersonEntersVehicleEventHandler {

    private Scenario scenario;
    private Map<Id<Vehicle>,List<Id<Person>>> vehicles2Persons = new HashMap<>();
    List<Object[]> data = new ArrayList<>();
    public static final String CSV_FILE = "scenarios/siouxFalls/output/output_trajectory_raw.csv";
    //public static final String CSV_FILE = "scenarios/berlin/output/output_trajectory_raw.csv";

    public GenerateTrajectory(Scenario scenario) {
        this.scenario = scenario;
    }

    @Override
    public void reset(int iteration) {
        System.out.println("reset...");
    }

    @Override
    public void handleEvent(ActivityEndEvent event) {
        if (!event.getPersonId().toString().startsWith("pt")){
            Link link = scenario.getNetwork().getLinks().get( event.getLinkId());
            int age = (int) scenario.getPopulation().getPersons().get(event.getPersonId()).getAttributes().getAttribute("age");
            data.add(new Object[] {event.getPersonId().toString(), age, event.getTime(), event.getLinkId().toString(), link.getCoord().getX(), link.getCoord().getY(), event.getActType(), event.getEventType(), link.getLength(), "NA"});
        }
    }

    @Override
    public void handleEvent(PersonDepartureEvent event) {
        if (!event.getPersonId().toString().startsWith("pt")){
            Link link = scenario.getNetwork().getLinks().get( event.getLinkId());
            int age = (int) scenario.getPopulation().getPersons().get(event.getPersonId()).getAttributes().getAttribute("age");
            data.add(new Object[] {event.getPersonId().toString(), age, event.getTime(), event.getLinkId().toString(), link.getCoord().getX(), link.getCoord().getY(), "NA", event.getEventType(), link.getLength(), event.getLegMode()});
        }
    }

    @Override
    public void handleEvent(PersonEntersVehicleEvent event) {
        if (!event.getPersonId().toString().startsWith("pt")){
            if (vehicles2Persons.containsKey(event.getVehicleId())){
                vehicles2Persons.get(event.getVehicleId()).add(event.getPersonId());
            } else {
                List<Id<Person>> persons = new ArrayList<>();
                persons.add(event.getPersonId());
                vehicles2Persons.put(event.getVehicleId(),persons);
            }
            int age = (int) scenario.getPopulation().getPersons().get(event.getPersonId()).getAttributes().getAttribute("age");
            data.add(new Object[]{event.getPersonId().toString(), age, event.getTime(), "NA", "NA", "NA", "NA", event.getEventType(), "NA", "NA"});
        }
    }


    @Override
    public void handleEvent(LinkEnterEvent event) {
        if (vehicles2Persons.containsKey(event.getVehicleId())){
            for (Id<Person> person : vehicles2Persons.get(event.getVehicleId())){
                Link link = scenario.getNetwork().getLinks().get( event.getLinkId());
                int age = (int) scenario.getPopulation().getPersons().get(person).getAttributes().getAttribute("age");
                data.add(new Object[] {person.toString(), age, event.getTime(), event.getLinkId().toString(), link.getCoord().getX(), link.getCoord().getY(), "NA", event.getEventType(), link.getLength(), "NA"});
            }
        }
    }

    @Override
    public void handleEvent(LinkLeaveEvent event) {
        if (vehicles2Persons.containsKey(event.getVehicleId())){
            for (Id<Person> person : vehicles2Persons.get(event.getVehicleId())){
                Link link = scenario.getNetwork().getLinks().get( event.getLinkId());
                int age = (int) scenario.getPopulation().getPersons().get(person).getAttributes().getAttribute("age");
                data.add(new Object[] {person.toString(), age, event.getTime(), event.getLinkId().toString(), link.getCoord().getX(), link.getCoord().getY(), "NA", event.getEventType(), link.getLength(), "NA"});
            }
        }
    }

    @Override
    public void handleEvent(PersonLeavesVehicleEvent event) {
        if (!event.getPersonId().toString().startsWith("pt")){
            if (vehicles2Persons.containsKey(event.getPersonId())){
                vehicles2Persons.get(event.getVehicleId()).remove(event.getPersonId());
            }
            int age = (int) scenario.getPopulation().getPersons().get(event.getPersonId()).getAttributes().getAttribute("age");
            data.add(new Object[]{event.getPersonId().toString(), age, event.getTime(), "NA", "NA", "NA", "NA", event.getEventType(), "NA", "NA"});
        }
    }

    @Override
    public void handleEvent(PersonArrivalEvent event) {
        if (!event.getPersonId().toString().startsWith("pt")){
            Link link = scenario.getNetwork().getLinks().get( event.getLinkId());
            int age = (int) scenario.getPopulation().getPersons().get(event.getPersonId()).getAttributes().getAttribute("age");
            data.add(new Object[] {event.getPersonId().toString(), age, event.getTime(), event.getLinkId().toString(), link.getCoord().getX(), link.getCoord().getY(), "NA", event.getEventType(), link.getLength(), event.getLegMode()});
        }
    }

    @Override
    public void handleEvent(ActivityStartEvent event) {
        if (!event.getPersonId().toString().startsWith("pt")){
            Link link = scenario.getNetwork().getLinks().get( event.getLinkId());
            int age = (int) scenario.getPopulation().getPersons().get(event.getPersonId()).getAttributes().getAttribute("age");
            data.add(new Object[] {event.getPersonId().toString(), age, event.getTime(), event.getLinkId().toString(), link.getCoord().getX(), link.getCoord().getY(), event.getActType(), event.getEventType(), link.getLength(), "NA"});
        }
    }


    public void writeTrajectory(){
        try {
            BufferedWriter writer = Files.newBufferedWriter(Paths.get(CSV_FILE));
            CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader("person_id", "age", "time", "link_id", "x", "y", "act_type", "event_type", "length", "leg_mode"));
            csvPrinter.printRecords(data);
            csvPrinter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
