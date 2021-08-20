package org.matsim.project;

import org.apache.log4j.Logger;
import org.matsim.api.core.v01.Scenario;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.config.Config;
import org.matsim.core.controler.Controler;
import org.matsim.core.events.EventsReaderXMLv1;
import org.matsim.core.events.EventsUtils;
import org.matsim.run.RunBerlinScenario;

/**
 * TODO
 * convert eventsPath as "args" - see TUM course
 */


public class RunEventsBerlin {

    private static final Logger log = Logger.getLogger(RunEventsBerlin.class );
    private static final String eventsPath = "https://svn.vsp.tu-berlin.de/repos/public-svn/matsim/scenarios/countries/de/berlin/berlin-v5.5-1pct/output-berlin-v5.5-1pct/berlin-v5.5.3-1pct.output_events.xml.gz";


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

    }

    private static void generateTrajectory(Scenario scenario, String eventsPath) {

        // create an event object
        EventsManager manager = EventsUtils.createEventsManager();

        //create the handler and add it
        GenerateTrajectory trajectoryEvent = new GenerateTrajectory(scenario.getNetwork()); //double check the instance name
        manager.addHandler(trajectoryEvent);

        //create the reader and read the file
        manager.initProcessing();
        EventsReaderXMLv1 eventsReader = new EventsReaderXMLv1(manager);
        eventsReader.readFile(eventsPath);
        manager.finishProcessing();

        //write csv to file
        trajectoryEvent.writeTrajectory();
    }
}
