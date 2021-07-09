package org.matsim.project;

import org.matsim.api.core.v01.Scenario;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.controler.OutputDirectoryHierarchy;
import org.matsim.core.events.EventsReaderXMLv1;
import org.matsim.core.events.EventsUtils;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.core.utils.io.IOUtils;

import java.net.URL;

public class RunEvents {

    public static final String outputDirectory = "output/siouxFalls";


    public static void main(String[] args){

        URL context = org.matsim.examples.ExamplesUtils.getTestScenarioURL( "siouxfalls-2014" );
        URL url = IOUtils.extendUrl( context, "config_default.xml" );

        Config config = ConfigUtils.loadConfig( url );
        config.controler().setOutputDirectory(outputDirectory);
        config.controler().setOverwriteFileSetting( OutputDirectoryHierarchy.OverwriteFileSetting.deleteDirectoryIfExists );

        // possibly modify config here

        //

        Scenario scenario = ScenarioUtils.loadScenario( config );

        // possibly modify scenario here

        //

        // set path to event file
        String eventsPath = "output/siouxFalls/output_events.xml.gz";
        generateTrajectory(scenario, eventsPath);

    }

    private static void generateTrajectory(Scenario scenario, String eventsPath) {

        // create an event object
        EventsManager manager = EventsUtils.createEventsManager();

        //create the handler and add it
        GenerateTrajectory trajectoryEvent = new GenerateTrajectory(scenario.getNetwork()); //double check the instance name
        manager.addHandler(trajectoryEvent);

        //create the reader and read the file
        manager.initProcessing();
        EventsReaderXMLv1 eventsReader= new EventsReaderXMLv1(manager);
        eventsReader.readFile(eventsPath);
        manager.finishProcessing();

        //write csv to file
        trajectoryEvent.writeTrajectory();

    }


}
