package logic;

import java.io.IOException;

import logic.PSO_RNA;
public class TestandoJava {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		int sizePopulation =2; 
		double rangeVelocityMAX=1; 
		double rangeVelocityMin=1; 
		double inertiaWeightsMax =(double) 0.9; 
		double inertiaWeightsMin = (double) 0.4; 
		double c1=(double) 2.5; 
		double c2=(double) 0.5; 
		int toleranceFail=50;  
		double alpha= (double) 0.7;
		double beta=(double) 0.1; 
		double varianceGaussian = (double) 0.7; 
		int allowIteration=1000; 
		int inputQ= 360;
		int hiddenQ = 50; 
		int outputQ=1; 
		double [][]setValidation=null; 
		int sizeSetValidation=0; 
		int quantitySelectedForGeneration = 50;
		
		PSO_RNA pso = new PSO_RNA(sizePopulation, rangeVelocityMAX, rangeVelocityMin, inertiaWeightsMax, inertiaWeightsMin, c1, c2, toleranceFail, alpha, beta, varianceGaussian, allowIteration, inputQ, hiddenQ, outputQ, setValidation, sizeSetValidation, quantitySelectedForGeneration);
		
		// TODO Auto-generated method stub
	//	Particle p1 = new Particle(1, 1,1,1,null,2,3,4,null);
		
		System.out.println("oi");
		Particle p = null;
		
		p = pso.particles[0].clonar();
		
		pso.particles[0].id=7777;
		
		System.out.println(p.id);

	}

}
