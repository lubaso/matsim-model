package org.matsim.project;


import org.matsim.api.core.v01.Scenario;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.controler.Controler;
import org.matsim.core.controler.OutputDirectoryHierarchy.OverwriteFileSetting;;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.core.utils.io.IOUtils;

import java.net.URL;

class RunMatsimFromExamplesUtils{

	public static final String outputDirectory = "scenarios/siouxFalls/output";

	public static void main(String[] args) {

		URL context = org.matsim.examples.ExamplesUtils.getTestScenarioURL("siouxfalls-2014");
		URL url = IOUtils.extendUrl(context, "config_default.xml");

		Config config = ConfigUtils.loadConfig(url);
		config.controler().setLastIteration(500);
		config.controler().setOutputDirectory(outputDirectory);
		config.controler().setOverwriteFileSetting(OverwriteFileSetting.deleteDirectoryIfExists);

		// possibly modify config here

		// ---

		Scenario scenario = ScenarioUtils.loadScenario(config);

		// possibly modify scenario here

		// ---

		Controler controler = new Controler(scenario);

		// possibly modify controler here

		// ---

		controler.run();

	}

}
