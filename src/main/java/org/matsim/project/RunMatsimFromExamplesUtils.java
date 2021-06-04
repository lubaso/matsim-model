package org.matsim.project;

import org.apache.commons.io.FileUtils;
import org.matsim.api.core.v01.Scenario;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.controler.Controler;
import org.matsim.core.controler.OutputDirectoryHierarchy.OverwriteFileSetting;
import org.matsim.core.events.EventsUtils;
import org.matsim.core.events.MatsimEventsReader;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.core.utils.io.IOUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;

class RunMatsimFromExamplesUtils{

	public static final String outputDirectory = "output/siouxFalls";

	public static StringBuilder stringOfData = new StringBuilder();
	public static void main( String[] args ){

		URL context = org.matsim.examples.ExamplesUtils.getTestScenarioURL( "siouxfalls-2014" );
		URL url = IOUtils.extendUrl( context, "config_default.xml" );

		Config config = ConfigUtils.loadConfig( url );
		config.controler().setOutputDirectory(outputDirectory);
		config.controler().setOverwriteFileSetting( OverwriteFileSetting.deleteDirectoryIfExists );

		// possibly modify config here
		config.controler().setLastIteration(20);
		config.controler().setWriteEventsInterval(5);
		config.controler().setWritePlansInterval(5);

		Scenario scenario = ScenarioUtils.loadScenario( config );

		// possibly modify scenario here

		//

		Controler controler = new Controler( scenario );

		// ---

		//controler.run();

		// set path to event file
		String inputFile = "output/siouxFalls/output_events.xml.gz";

		// create an event object
		EventsManager events = EventsUtils.createEventsManager();

		//create the handler and add it
		TrajectoriesEventHandler trajectoriesEvent = new TrajectoriesEventHandler(scenario.getNetwork());
		events.addHandler(trajectoriesEvent);

		//create the reader and read the file
		events.initProcessing();
		MatsimEventsReader reader= new MatsimEventsReader(events);
		reader.readFile(inputFile);
		events.finishProcessing();


		// writing trajectory data to file
		File file = new File("output/trajectory/trajectoryData.csv");
		{
			try {
				FileUtils.write(file, stringOfData, Charset.defaultCharset());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}


	}



}
