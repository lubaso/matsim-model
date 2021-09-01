package org.matsim.project;

import org.matsim.api.core.v01.Scenario;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.events.EventsUtils;
import org.matsim.core.events.MatsimEventsReader;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.core.utils.io.IOUtils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.zip.GZIPOutputStream;

public class RunEventsSiouxFalls {

    public static final String eventsPath = "scenarios/siouxFalls/output/output_events.xml.gz";
    private static final String CSV_FILE = "scenarios/siouxFalls/output/output_trajectory_raw.csv";
    private static final String ZIP_FILE = "scenarios/siouxFalls/output/output_trajectory_raw.csv.gz";

    public static void main(String[] args){

        URL context = org.matsim.examples.ExamplesUtils.getTestScenarioURL( "siouxfalls-2014" );
        URL url = IOUtils.extendUrl( context, "config_default.xml" );

        Config config = ConfigUtils.loadConfig( url );

        // possibly modify config here

        //

        Scenario scenario = ScenarioUtils.loadScenario( config );

        // possibly modify scenario here

        //

        // call generateTrajectory
        generateTrajectory(scenario, eventsPath);
        try {
            zip();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void generateTrajectory(Scenario scenario, String eventsPath) {

        // create an event object
        EventsManager manager = EventsUtils.createEventsManager();

        //create the handler and add it
        GenerateTrajectory trajectoryEvent = new GenerateTrajectory(scenario);
        manager.addHandler(trajectoryEvent);

        //create the reader and read the file
        manager.initProcessing();
        MatsimEventsReader eventsReader = new MatsimEventsReader(manager);
        eventsReader.readFile(eventsPath);
        manager.finishProcessing();

        //write csv to file
        trajectoryEvent.writeTrajectory();

    }

    public static void  zip() throws IOException{
        byte[] buffer = new byte[2048];
        FileInputStream inputStream = new FileInputStream(CSV_FILE);
        FileOutputStream outputStream = new FileOutputStream(ZIP_FILE);
        GZIPOutputStream gzipOutputStream = new GZIPOutputStream(outputStream);
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            gzipOutputStream.write(buffer, 0, length);
        }
        inputStream.close();
        gzipOutputStream.close();
    }


}
