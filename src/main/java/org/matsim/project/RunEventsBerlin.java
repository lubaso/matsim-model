package org.matsim.project;

import org.apache.log4j.Logger;
import org.matsim.api.core.v01.Scenario;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.config.Config;
import org.matsim.core.controler.Controler;
import org.matsim.core.events.EventsReaderXMLv1;
import org.matsim.core.events.EventsUtils;
import org.matsim.core.events.MatsimEventsReader;
import org.matsim.run.RunBerlinScenario;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;


public class RunEventsBerlin {

    private static final Logger log = Logger.getLogger(RunEventsBerlin.class );
    private static final String eventsPath = "https://svn.vsp.tu-berlin.de/repos/public-svn/matsim/scenarios/countries/de/berlin/berlin-v5.5-1pct/output-berlin-v5.5-1pct/berlin-v5.5.3-1pct.output_events.xml.gz";
    private static final String CSV_FILE = "scenarios/berlin/output/output_trajectory_raw.csv";
    private static final String ZIP_FILE = "scenarios/berlin/output/output_trajectory_raw.csv.gz";

    public static void main(String[] args) {

        for (String arg : args) {
            log.info( arg );
        }

        if ( args.length==0 ) {
            args = new String[] {"https://svn.vsp.tu-berlin.de/repos/public-svn/matsim/scenarios/countries/de/berlin/berlin-v5.5-1pct/input/berlin-v5.5-1pct.config.xml"}  ;
        }

        Config config = RunBerlinScenario.prepareConfig( args );
        Scenario scenario = RunBerlinScenario.prepareScenario(config);
        //Controler controler = RunBerlinScenario.prepareControler(scenario);
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
        GenerateTrajectory trajectoryEvent = new GenerateTrajectory(scenario); //double check the instance name
        manager.addHandler(trajectoryEvent);

        //create the reader and read the file
        manager.initProcessing();
        MatsimEventsReader eventsReader = new MatsimEventsReader(manager);
        eventsReader.readFile(eventsPath);
        manager.finishProcessing();

        //write csv to file
        trajectoryEvent.writeTrajectory();
    }

    public static void zip() throws IOException {
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
