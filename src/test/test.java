package test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import auxiliary.*;
import choiceModel.*;
import network.*;
import refCostFun.*;
public class test {

	public static void main(String[] args) throws IOException {
		String publicNetworkDirFromCalc = "O:/Public/DMC/temp/Mads Paulsen (Network)/TransportationNetworks-master/";
		String publicNetworkDirFromPC = "O:/Public/DMC/temp/Mads Paulsen (Network)/TransportationNetworks-master/";
		String localNetworkDir = "C:/Projekter/Programming/Java/TransportationNetworks-master/";

		String networkDirectory = publicNetworkDirFromCalc; //Define directory of networks
		
		Boolean useLocalStorage = true; //true doesn't work yet. madsp
		
		
		
	//	String networkName = "SiouxFalls"; //Choose network
		String networkName = "Anaheim";
//		String networkName = "Berlin-Friedrichshain"; //Choose network
		double maximumCostRatio = 1.4;
		double localMaximumCostRatio = 2.5;
		boolean isNetworkBidirectional = true;
		boolean printStatusOnTheGo = true;

		String localStorageDirectory = "C:/workAtHome/TKRA/storage/" + networkName + "/";
		new File(localStorageDirectory).mkdir();
		
		
		Network network = new Network(networkDirectory + networkName, isNetworkBidirectional); //Initialize network
		network.setMaximumCostRatio(maximumCostRatio);
		network.setLocalMaximumCostRatio(localMaximumCostRatio);
		network.minimumFlowToBeConsideredUsed = 0; // Allows flows of 0 to be considered too. Switch to a value higher than 0 to make cut-off.
		network.setLocalStorageDirectory(localStorageDirectory);
		network.setUseLocalStorage(useLocalStorage);
		network.printStatusOnTheGo = printStatusOnTheGo;

		
		RefCostFun phi = new RefCostTauMin(1.3); //lower reference cost in RSUET
		RefCostFun omega = new RefCostTauMin(1.3); //upper reference cost in RSUET 

		 
//		phi = omega; // make phi = omega; this is computationally demanding. Only do it on small networks. 

		RUM rum = new TMNL(omega); //TMNL random utility model -- explicitly state to use ref cost omega 
		RSUET routeChoiceModel = new RSUET(rum, phi,omega); //set up the TMNL RSUET(min, min + 10)
		routeChoiceModel.maximumCostRatio = maximumCostRatio;
		routeChoiceModel.epsilon = 0.00005;


		
		ConvergencePattern conv = routeChoiceModel.solve(network); //get network to equilibrium

		boolean printToFile = true; //specify if you want the output printed
		String outputFolderPC = "O:/Public/DMC/temp/Mads Paulsen (Network)/Outputs";
		String outputFolderCalc = "C:/Projekter/Programming/Java/Outputs";
		String outputFolder = outputFolderPC;
		if (printToFile){
			network.printOutput(outputFolder, routeChoiceModel, conv);
		}
	}
}
