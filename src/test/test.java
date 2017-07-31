package test;

import auxiliary.*;
import choiceModel.*;
import network.*;
import refCostFun.*;
public class test {

	public static void main(String[] args) {
		//Define some network directories to choose from
		String publicNetworkDirFromCalc = "//tsclient/O/Public/DMC/temp/Mads Paulsen (Network)/TransportationNetworks-master/";
		String publicNetworkDirFromPC = "O:/Public/DMC/temp/Mads Paulsen (Network)/TransportationNetworks-master/";
		String localNetworkDir = "C:/Projekter/Programming/Java/TransportationNetworks-master/";

		String networkDirectory = localNetworkDir; //Define directory of networks
		
//		String networkName = "SiouxFalls"; //Choose network
		String networkName = "EMA2"; //Choose network
		Network network = new Network(networkDirectory + networkName); //Initialize network
		network.minimumFlowToBeConsideredUsed = 0; // Allows flows of 0 to be considered too. Switch to a value higher than 0 to make cut-off.

		RefCostFun phi = new RefCostTauMin(1.3); //lower reference cost in RSUET
		RefCostFun omega = new RefCostTauMin(1.3); //upper reference cost in RSUET 

		 
//		phi = omega; // make phi = omega; this is computationally demanding. Only do it on small networks. 

		RUM rum = new TMNL(omega); //TMNL random utility model -- explicitly state to use ref cost omega 
		RSUET routeChoiceModel = new RSUET(rum, phi,omega); //set up the TMNL RSUET(min, min + 10)
		routeChoiceModel.maximumCostRatio = 50;
		routeChoiceModel.epsilon = 0.00005;


		
		ConvergencePattern conv = routeChoiceModel.solve(network); //get network to equilibrium

		boolean printToFile = true; //specify if you want the output printed
		String outputFolderPC = "O:/Public/DMC/temp/Mads Paulsen (Network)/Outputs";
		String outputFolderCalc = "C:/Projekter/Programming/Java/Outputs";
		String outputFolder = outputFolderCalc;
		if (printToFile){
			network.printOutput(outputFolder, routeChoiceModel, conv);
		}
	}
}
