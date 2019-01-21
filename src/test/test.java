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
		//		String networkDirectory = localNetworkDir; //Define directory of networks

		double bound=24.5;
		for (int j=1;j<=1;j++) {

			bound+= 0.5;

			

			RefCostFun phi = new RefCostMinPlusDelta(bound); //lower reference cost for DUE replication
			RefCostFun omega = new RefCostMinPlusDelta(bound); //upper reference cost for DUE replication

			//		phi = omega; // make phi = omega; this is computationally demanding. Only do it on small networks. 

			RUM rum = new TMNL(omega); 
			rum.theta=0.39;
			for (int k=1;k<=1;k++) {

				rum.theta+=0.01;	

				




				boolean useLocalStorage = false; //
				boolean useOnlyConsideredPaths = false;
				boolean exportConsideredPaths = false;
				boolean consEnumIte = false;

				for (int i=1;i<=1;i++) {
					if (i==2) {
						useOnlyConsideredPaths = true;
						exportConsideredPaths = false;
					}
					if (i==3) {
						useOnlyConsideredPaths = true;
						exportConsideredPaths = false;
						consEnumIte=true;
					}
					//String networkName = "SiouxFalls"; //Choose network
					//		String networkName = "SiouxFallsDUEMod"; //Choose network
							String networkName = "Anaheim";
					//	String networkName = "Berlin-Friedrichshain"; //Choose network
					//double maximumCostRatio = 1.5;


					//double maximumCostRatio = Double.POSITIVE_INFINITY;
					double maximumCostRatio = 1.8;
						double localMaximumCostRatio = 1.4;
					//double localMaximumCostRatio = Double.POSITIVE_INFINITY;
					boolean isNetworkBidirectional = false; //is the network defined as bidirectional or one-way streets?
					//boolean isNetworkBidirectional = true; //is the network defined as bidirectional or one-way streets?
					boolean printStatusOnTheGo = true; //Level of detail of println messages during calculation

					String localStorageDirectory = "C:/Projekter/Programming/Java/storage/" + networkName + "/";
					new File(localStorageDirectory).mkdir();
					rum.betaLength=0.0;
					rum.betaTime=1.0;
					double demandScale;
					demandScale = 1.0;
					Network network = new Network(networkDirectory + networkName, isNetworkBidirectional, demandScale); //Initialize network
					network.setMaximumCostRatio(maximumCostRatio);
					network.setLocalMaximumCostRatio(localMaximumCostRatio);
					network.minimumFlowToBeConsideredUsed = 0; // Allows flows of 0 to be considered too. Switch to a value higher than 0 to make cut-off.
					network.setLocalStorageDirectory(localStorageDirectory);
					network.setUseLocalStorage(useLocalStorage);
					network.printStatusOnTheGo = printStatusOnTheGo;


					//RefCostFun phi = new RefCostTauMin(1.3); //lower reference cost in RSUET
					//RefCostFun omega = new RefCostTauMin(1.3); //upper reference cost in RSUET

					//RefCostFun phi = new RefCostMinPlusDelta(0.00001); //lower reference cost for DUE replication
					//RefCostFun omega = new RefCostMinPlusDelta(0.00001); //upper reference cost for DUE replication
					//TMNL random utility model -- explicitly state to use ref cost omega
					//		RUM rum = new MNL(); //TMNL random utility model -- explicitly state to use ref cost omega
					//		rum.theta=0.2;


					RSUET routeChoiceModel = new RSUET(rum, phi,omega, demandScale, consEnumIte, bound,
							useOnlyConsideredPaths, exportConsideredPaths); //set up the TMNL RSUET(min, min + 10)
					routeChoiceModel.maximumCostRatio = maximumCostRatio;
					routeChoiceModel.epsilon = 0.00000005;



					ConvergencePattern conv = routeChoiceModel.solve(network); //get network to equilibrium

					boolean printToFile = true; //specify if you want the output printed
					String outputFolderPC = "O:/Public/DMC/temp/Mads Paulsen (Network)/Outputs";
					String outputFolderCalc = "C:/Projekter/Programming/Java/Outputs";
					String outputFolder = outputFolderCalc;
					if (printToFile){
						//network.printOutput(outputFolder, routeChoiceModel, conv);
						String outputNameHelp = networkName + "_"+ (double)Math.round(rum.theta* 1000d)/1000d + "_" + (double)Math.round(bound*10d)/10d + "_" + useOnlyConsideredPaths +"_"+consEnumIte;
						network.printOutput(outputFolder, routeChoiceModel, conv, outputNameHelp);
					}
					if(i==1 & !exportConsideredPaths) {
						break;
					}
				}
			}
		}
	}
}
