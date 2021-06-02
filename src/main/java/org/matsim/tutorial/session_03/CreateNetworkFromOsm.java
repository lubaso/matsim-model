//package org.matsim.project.lecture3;
//
//import org.matsim.core.utils.geometry.CoordinateTransformation;
//import org.matsim.core.utils.geometry.transformations.TransformationFactory;
//import org.matsim.core.utils.io.OsmNetworkReader;
//
//public class CreateNetworkFromOsm {
//    public static void main(String[] args) {
//
//        // First, we need an new/empty network in which the OsmNetworkReader can store whatever it parses
//        // ...
//
//        CoordinateTransformation ct = TransformationFactory.getCoordinateTransformation(TransformationFactory.WGS84,
//                "DESIRED CRS as EPSG:XXXX");
//
//        new OsmNetworkReader(network, ct).parse("PATH TO .OSM/.OSM.GZ FILE");
//
//        // Lastly, store parsed network as .xml file
//        // ...
//    }
//}
