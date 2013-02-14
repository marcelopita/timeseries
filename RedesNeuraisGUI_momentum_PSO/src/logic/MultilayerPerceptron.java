/*
 * with momentum
 * */

package logic;
import java.util.ArrayList;
import java.util.Random;
import java.awt.geom.QuadCurve2D;
import java.io.File;
import java.io.ObjectInputStream.GetField;
import java.math.*;

import java.util.*;


class Neuron {
	int layerId;
	double weightsPreviousLayer[][];
	double net;
	double fnet;
	double f_derivate_net;
	double sensibility;
	int previousLayerNeuronsQuantity;
	double localError;
	
	public Neuron(int layerID){
		this.layerId = layerID;
	}
	
	public Neuron(int layerID, int previousLayerNeuronsQuantity){
		this.weightsPreviousLayer = new double[2][previousLayerNeuronsQuantity+1]; // 'cause the BIAS
		this.previousLayerNeuronsQuantity = previousLayerNeuronsQuantity;
		this.layerId = layerID;
		this.setAleatoryWeights();
		
	}
	void setAleatoryWeights(){
	//	Random random = new Random(getSemente());
		for(int i=0; i<this.previousLayerNeuronsQuantity; i++){
			this.weightsPreviousLayer[0][i]=this.weightsPreviousLayer[1][i] = Math.random();
		}
	}
	
	public String toString(){
		String string = "";
		for (int i=0; i<this.previousLayerNeuronsQuantity; i++){
			string = string + " " + weightsPreviousLayer[1][i];
		}
		return string;
	}
	
	void resetParamaters(){
		this.sensibility=0;
		this.net=0;
		this.fnet=0;
		this.f_derivate_net=0;
		
	}
	void setSensibility(double s){
		this.sensibility = s;
	}
	void setNet(double net){
		this.net = net;
	}
	
	void setFNet (double fnet){
		this.fnet = fnet;
	}
	void setFDerNet (Double f_der_net){
		this.f_derivate_net  = f_der_net;
	}
	
	double getSensibily (){
		return this.sensibility;
	}
	double getNet(){
		return net;
	}
	double getFNet(){
		return this.fnet;
	}
	double getFDerNet(){
		return this.f_derivate_net;
	}

}

public class MultilayerPerceptron {
	int layerQuantity;
	int neuronQuantityPerLayer[];
	double learningRate;
	int inputNumber;
	int outputNumber;
	int numberExamples;
	int rateTrainning;
	int quantityIteration;
	int fail;
	int verificationQuantity;
	//Neuron neurons[][];
	ArrayList<ArrayList> neurons = new ArrayList<ArrayList>();
	double [][]dataBaseTrainningTest;
	double [][]dataBaseVerificationTest;
	ArrayList errors = new ArrayList();
	ArrayList errorsValidation = new ArrayList();
	ArrayList errorsVerification = new ArrayList();
	ArrayList errorsVerification_EMA = new ArrayList();
	ArrayList errorsVerification_EPMA = new ArrayList();
	ArrayList errorsVerification_EP = new ArrayList();
	Arquivo fileVerification;
	int qtdProgramRun;
	double momentum;
	int windowSize;
	
	public MultilayerPerceptron(Arquivo fileVerification, int layerQuantity, int []neuronQuantityPerLayer, double learningRate, int inputNumber, int outputNumber, int numberExamples, int rateTrainning, int quantifyIteration, int verificationQuantity, int fail, int qtdProgramRun, double momentum, int windowSize){
		this.fileVerification = fileVerification;
		this.layerQuantity = layerQuantity;
		this.qtdProgramRun = qtdProgramRun;
		this.neuronQuantityPerLayer = neuronQuantityPerLayer;
		this.learningRate = learningRate;
		this.inputNumber = inputNumber;
		this.outputNumber = outputNumber;
		this.numberExamples = numberExamples;
		this.rateTrainning = rateTrainning;
		this.dataBaseTrainningTest = new double [numberExamples][inputNumber+outputNumber];
		this.dataBaseVerificationTest=  new double [verificationQuantity][inputNumber+outputNumber];
		this.quantityIteration = quantifyIteration;
		this.fail = fail;
		this.momentum = momentum;
		this.windowSize = windowSize;
		
		this.verificationQuantity = verificationQuantity;
		for (int i=0; i<layerQuantity; i++){
			neurons.add(new ArrayList<Neuron>());
		}
		
		
		for(int i=0; i<layerQuantity; i++){
			for (int j=0; j< this.neuronQuantityPerLayer[i] + 1; j++){ // +1 cause de BIAS
				Neuron aux;
				if (i==0){
					aux = new Neuron(i);
				}else{
					aux = new Neuron(i, this.neuronQuantityPerLayer[i-1]);
				}
				neurons.get(i).add(aux);
			}
				
		}
		
		if (windowSize== 0){
			readFile();
			readFileVerification();
		}else{
			readFile_Time_Forescast();
			readFileVerification_Time_Forescast();
		}
		
	}
	
	void readFile_Time_Forescast(){
		Arquivo file = new Arquivo("inputNetwork.txt", "vazio.txt");
		for(int i=0; i<this.numberExamples - (this.windowSize-1); i++){
			if (i==0){
				for(int j=0; j<(this.inputNumber + this.outputNumber); j++){
					this.dataBaseTrainningTest[i][j] = file.readDouble();
				}
			}else{
				int j;
				for(j=0; j<(this.inputNumber + this.outputNumber)-1; j++){
					this.dataBaseTrainningTest[i][j] = this.dataBaseTrainningTest[i-1][j+1]; 
				}
				this.dataBaseTrainningTest[i][j] = file.readDouble();
				
			}

		}
		file.close();
		
	}
	
	void readFile (){
		Arquivo file = new Arquivo("inputNetwork.txt", "vazio.txt");
		for(int i=0; i<this.numberExamples; i++){
			for(int j=0; j<(this.inputNumber + this.outputNumber); j++){
				this.dataBaseTrainningTest[i][j] = file.readDouble();
			}
		}
		file.close();
		
	}
	
	void readFileVerification(){
		Arquivo file = new Arquivo("inputNetworkVerification.txt", "vazio.txt");
		for(int i=0; i<this.verificationQuantity; i++){
			for(int j=0; j<(this.inputNumber + this.outputNumber); j++){
				this.dataBaseVerificationTest[i][j] = file.readDouble();
			}
		}
		file.close();
	}
	
	void readFileVerification_Time_Forescast(){
		Arquivo file = new Arquivo("inputNetworkVerification.txt", "vazio.txt");
		for(int i=0; i<this.verificationQuantity - (this.windowSize-1); i++){
			if (i==0){
				for(int j=0; j<(this.inputNumber + this.outputNumber); j++){
					this.dataBaseVerificationTest[i][j] = file.readDouble();
				}
			}else{
				int j;
				for(j=0; j<(this.inputNumber + this.outputNumber)-1; j++){
					this.dataBaseVerificationTest[i][j] = this.dataBaseVerificationTest[i-1][j+1]; 
				}
				this.dataBaseVerificationTest[i][j] = file.readDouble();
			}
		}
		file.close();
		
	}
	void resetParamaterMLP(){
		for(int i=0; i<this.layerQuantity; i++){
			for (int j=0; j<this.neuronQuantityPerLayer[i]; j++){
				Neuron n = (Neuron)neurons.get(i).get(j);
				n.resetParamaters();
			}
		}
	}
	double sigmoidFunction (double net){
		//System.out.println("net: "+net);
		double result = 1/( 1 + Math.pow(Math.E,(-1*net)));
		//System.out.println("resultado da funcao sigmoidal: "+result);
		return result;
	}
	
	double der_sigmoidFunction(double x){
		return (x)*(1-x);
	}
	
	void forward(){
		boolean finishNetwork = true;
		int trainning = (this.numberExamples * this.rateTrainning) / 100;
		int validation = this.numberExamples - trainning;
		//System.out.println("numero de ex: "+numberExamples+" treinamento"+trainning+" validacao: "+validation);
		for (int in=0, valid=0, total=0, count=0; finishNetwork; in++, valid++, total++){
		//	System.out.println("aqui");
			resetParamaterMLP();
			if (in==trainning){
				in=0;
			}
			if (valid == this.numberExamples){
				valid=0;
			}
			
			// propagation
			for(int i=1; i<this.layerQuantity; i++){
				for(int j=0; j<this.neuronQuantityPerLayer[i]; j++){
					if (i==1){ // the inner product is between the input of the file and weights
						Neuron aux = (Neuron)neurons.get(i).get(j);
						int k;
						for(k=0; k<this.inputNumber; k++){
							aux.net += this.dataBaseTrainningTest[in][k]* aux.weightsPreviousLayer[1][k];
						}
						// atention in the duplication code below
						aux.net += 1 * aux.weightsPreviousLayer[1][k]; // BIAS
						aux.fnet = sigmoidFunction(aux.net);
						
						aux.f_derivate_net = der_sigmoidFunction(aux.fnet);
						//System.out.println("fnet dos neuronios: "+aux.f_derivate_net+ " da camada: "+i);
					}else{
						Neuron aux = (Neuron)neurons.get(i).get(j);
						int k;
						for(k=0; k<aux.previousLayerNeuronsQuantity; k++){
							Neuron aux2 = (Neuron)neurons.get(i-1).get(k);
							aux.net += aux2.fnet* aux.weightsPreviousLayer[1][k];
						}
						
						aux.net += 1 * aux.weightsPreviousLayer[1][k]; // BIAS
						
						aux.fnet = sigmoidFunction(aux.net);
						aux.f_derivate_net = der_sigmoidFunction(aux.fnet);	
						//System.out.println("fnet dos neuronios: AQUI "+aux.f_derivate_net);
					}
					
				}
			}
			//verify the error of the network
			double localError=0;
			for(int i=0; i<this.outputNumber; i++ ){
				Neuron out = (Neuron)neurons.get(this.layerQuantity-1).get(i);
				out.localError = (this.dataBaseTrainningTest[in][this.inputNumber+i] - out.fnet);
				localError += Math.abs(out.localError);
				//System.out.println("erro dentro: "+localError);
			//	System.out.println("Erro salvo: "+out.localError);
			}
			localError = (float)localError/(float)this.outputNumber;
			this.errors.add(localError);
			
			
			///FEEDFORWARD
			//calculus of the output neurons sensibility
			for(int i=0; i<this.outputNumber; i++ ){
				Neuron out = (Neuron)neurons.get(layerQuantity-1).get(i);
				out.sensibility = out.localError * out.f_derivate_net;
				//System.out.println("local error: "+out.localError+" sensibilidade saida: "+out.sensibility + "f der"+ out.f_derivate_net);
			}
		
			
			//calculus of sensibility hidden layer neurons
			for (int i = this.layerQuantity-2 ; i>0; i-- ){
				//will not go until zero, because input layer don't need sensibility
				for (int j=0; j< this.neuronQuantityPerLayer[i]; j++){
					Neuron n = (Neuron)neurons.get(i).get(j);
					for (int w=0; w< this.neuronQuantityPerLayer[i+1]; w++){
						Neuron aux = (Neuron)neurons.get(i+1).get(w);
						n.sensibility += aux.weightsPreviousLayer[1][j] * aux.sensibility;
					}
					// sensibility = f'(net)        *   SUM (w(i,j) * sensibility(i))
					n.sensibility = n.f_derivate_net *  n.sensibility ;
					//System.out.println("sensib hidd: "+n.sensibility);
				}
			}
			
			//update of the weights
			for (int i = this.layerQuantity-1 ; i>0; i-- ){
				//will not go until zero, because input layer don't need to update the weights
				for (int j=0; j< this.neuronQuantityPerLayer[i]; j++){
					Neuron n = (Neuron)neurons.get(i).get(j);
					int w;
					
					for(w=0; w < n.previousLayerNeuronsQuantity; w++){
						double tempTbefore = n.weightsPreviousLayer[0][w];
						n.weightsPreviousLayer[0][w]= n.weightsPreviousLayer[1][w];
						double momentumRate = this.momentum * (n.weightsPreviousLayer[1][w] - tempTbefore);
						if (i==1){ // camada de entrada
							n.weightsPreviousLayer[1][w] += this.learningRate * n.sensibility * this.dataBaseTrainningTest[in][w] + momentumRate;
						}else{
							Neuron aux = (Neuron)neurons.get(i-1).get(w);
							n.weightsPreviousLayer[1][w] += this.learningRate * n.sensibility * aux.fnet + momentumRate;
						}
						//System.out.println("antes de mudar: "+n.weightsPreviousLayer[w]);
						//System.out.println("learning rate: "+ learningRate+ "sensib: "+n.sensibility+" fnet: "+aux.fnet+" camada: "+ i);
						//System.out.println("mudando peso: "+n.weightsPreviousLayer[w]);
					}
					double tempTbefore = n.weightsPreviousLayer[0][w];
					n.weightsPreviousLayer[0][w]= n.weightsPreviousLayer[1][w];
					double momentumRate = this.momentum * (n.weightsPreviousLayer[1][w] - tempTbefore);
					n.weightsPreviousLayer[1][w] += this.learningRate * n.sensibility * 1 + momentumRate;
				}
			}
			
			//validation
			resetParamaterMLP();
			for(int i=1; i<this.layerQuantity; i++){
				for(int j=0; j<this.neuronQuantityPerLayer[i]; j++){
					if (i==1){ // the inner product is between the layer input and weights
						Neuron aux = (Neuron)neurons.get(i).get(j);
						int k;
						for(k=0; k<this.inputNumber; k++){
							aux.net += this.dataBaseTrainningTest[valid][k]* aux.weightsPreviousLayer[1][k];
						}
						aux.net += 1 * aux.weightsPreviousLayer[1][k];
						
						aux.fnet = sigmoidFunction(aux.net);
						aux.f_derivate_net = der_sigmoidFunction(aux.fnet);
					}else{
						Neuron aux = (Neuron)neurons.get(i).get(j);
						int k;
						for(k=0; k<aux.previousLayerNeuronsQuantity; k++){
							Neuron aux2 = (Neuron)neurons.get(i-1).get(k);
							aux.net += aux2.fnet* aux.weightsPreviousLayer[1][k];
						}
						aux.net += 1 * aux.weightsPreviousLayer[1][k];
						aux.fnet = sigmoidFunction(aux.net);
						aux.f_derivate_net = der_sigmoidFunction(aux.fnet);						
					}
					
				}
			}
			//verify the error of the network
			double last=99999999;
			if (this.errorsValidation.size() > 0){
				last= (Double) this.errorsValidation.get(this.errorsValidation.size()-1);
			}
			double localErrorValidation=0;
			for(int i=0; i<this.outputNumber; i++ ){
				Neuron out = (Neuron)neurons.get(this.layerQuantity-1).get(i);
				localErrorValidation += Math.abs(this.dataBaseTrainningTest[valid][this.inputNumber+i] - out.fnet);
			}
			localErrorValidation = localErrorValidation/this.outputNumber;
			
			
			/*
			 TODO: TO VERIFY IT
			*/
			
			
			localErrorValidation = Math.abs(localErrorValidation);
			last = Math.abs(last);
			
			this.errorsValidation.add(localErrorValidation);
			
			if (localErrorValidation > last){
			//	System.out.println("SAIR1   " + last + " "+localErrorValidation);
				count++;
				if(count>this.fail){
					System.out.println("SAIR POR FAIL");	
					break;
				}
			}else{
				//TO VERIFY THIS
				if (count<this.fail/2){
					count=0;
				}
			}
			
			if(total >this.quantityIteration){
				System.out.println("SAIR POR QUANTIDADE DE ITERACAO");
				break;
			}
		//		System.out.println(this.neurons);
			
			
			
			
		}
		
		
	}
	
	void verification(){
		
		for(int a=0; a<this.verificationQuantity; a++){
			resetParamaterMLP();
			for(int i=1; i<this.layerQuantity; i++){
				for(int j=0; j<this.neuronQuantityPerLayer[i]; j++){
					if (i==1){ // the inner product is between the layer input and weights
						Neuron aux = (Neuron)neurons.get(i).get(j);
						int k;
						for(k=0; k<this.inputNumber; k++){
							aux.net += this.dataBaseVerificationTest[a][k]* aux.weightsPreviousLayer[1][k];
						}
						aux.net += 1 * aux.weightsPreviousLayer[1][k];
						
						aux.fnet = sigmoidFunction(aux.net);
						aux.f_derivate_net = der_sigmoidFunction(aux.fnet);
					}else{
						Neuron aux = (Neuron)neurons.get(i).get(j);
						int k;
						for(k=0; k<aux.previousLayerNeuronsQuantity; k++){
							Neuron aux2 = (Neuron)neurons.get(i-1).get(k);
							aux.net += aux2.fnet* aux.weightsPreviousLayer[1][k];
						}
						aux.net += 1 * aux.weightsPreviousLayer[1][k];
						aux.fnet = sigmoidFunction(aux.net);
						aux.f_derivate_net = der_sigmoidFunction(aux.fnet);						
					}
					
				}
			}
			//verify the error of the network
			
			double localErrorVerification=0;
			double error_EPMA=0;
			double error_EP=0;
			
			for(int i=0; i<this.outputNumber; i++ ){
				Neuron out = (Neuron)neurons.get(this.layerQuantity-1).get(i);
				
				double dif  = Math.abs(this.dataBaseVerificationTest[a][this.inputNumber+i] - out.fnet);
				localErrorVerification += dif;
				error_EPMA += (dif/this.dataBaseVerificationTest[a][this.inputNumber+i]);
				
			}
			localErrorVerification = localErrorVerification/this.outputNumber;
			
			/*
			 TODO: TO VERIFY IT
			*/
			
			localErrorVerification = Math.abs(localErrorVerification);
			
			this.errorsVerification.add(localErrorVerification);
			
			this.errorsVerification_EMA.add(localErrorVerification);
			this.errorsVerification_EPMA.add(error_EPMA);
			this.errorsVerification_EP.add(Math.pow(localErrorVerification,2));
			
			
			
		}
	}
	
	double finishSaveFile(){
		
		
		this.fileVerification.println(this.layerQuantity);
		this.fileVerification.println("Taxa de aprendizagem: "+this.learningRate);
		this.fileVerification.println("Quantidade Iteracao: "+this.quantityIteration);
		this.fileVerification.println("Quantos neuronios em cada camada: ");
		
		/*
		 qtd neurons of input layer
		 qtde neurons for each hidden layer
		 qtde neurons output layer
		 */
		
		
		for(int i=0; i<this.neuronQuantityPerLayer.length; i++){
			fileVerification.println(this.neuronQuantityPerLayer[i]);
		}
		/*
		 for each weight for each neuron
		 starting of the second layer (the first doesnt have weights)
		 */
		
	//	for(int i=1; i<this.layerQuantity; i++){
		//	for(int j=0; j<this.neuronQuantityPerLayer[i]; j++){
			//	Neuron n = (Neuron)this.neurons.get(i).get(j);
				//for(int k=0; k< n.previousLayerNeuronsQuantity; k++){
				//	out.println(n.weightsPreviousLayer[k]);
				//}
			//}
		//}
		String arq = "analise_erros_"+learningRate+"_";
		for(int i=0; i<this.layerQuantity; i++){
			arq += this.neuronQuantityPerLayer[i]+"_";
		}
		arq += this.neuronQuantityPerLayer[1]+"_"+this.qtdProgramRun;
		
		Arquivo fileVerificationAUX2 = new Arquivo("vazio.txt", arq);
		
		 //Errors of the Training
		 
		fileVerificationAUX2.println("\n\nTraining Errors ("+this.errors.size()+")");
		for (int i=0; i<this.errors.size(); i++){
			fileVerificationAUX2.println((Double)this.errors.get(i));
		}
		
		 //Errors of the Validation
		 
		fileVerificationAUX2.println("\n\nValidation Errors ("+this.errorsValidation.size()+")");
		for (int i=0; i<this.errorsValidation.size(); i++){
			fileVerificationAUX2.println((Double)this.errorsValidation.get(i));
		}
		
		/*
		 Errors of the Verification - EMA
		 */
		
		
		double error_EMA =0;
		double error_EPMA =0;
		double error_EP =0;
		//out.println("\n\n Verification Errors - EMA ("+this.errorsVerification.size()+")");
		for (int i=0; i<this.errorsVerification.size(); i++){
			//out.println((Double)this.errorsVerification.get(i));
			error_EMA += (Double)this.errorsVerification.get(i);
		}
		
		/*
		 Errors of the Verification - EPMA
		 */
		//out.println("\n\n Verification Errors - EPMA ("+this.errorsVerification_EPMA.size()+")");
		for (int i=0; i<this.errorsVerification_EPMA.size(); i++){
			//out.println((Double)this.errorsVerification_EPMA.get(i));
			error_EPMA += (Double)this.errorsVerification.get(i);
		}
		
		/*
		 Errors of the Verification - EP
		 */
		//out.println("\n\n Verification Errors - EP ("+this.errorsVerification_EP.size()+")");
		for (int i=0; i<this.errorsVerification_EP.size(); i++){
			//out.println((Double)this.errorsVerification_EP.get(i));
			error_EP += (Double)this.errorsVerification.get(i);
		}
		
		error_EMA = error_EMA / this.verificationQuantity;
		error_EPMA = 100 * error_EPMA / this.verificationQuantity;
		error_EP = Math.sqrt(error_EP /this.verificationQuantity);
		
		fileVerification.println("\nEMA: "+error_EMA);
		fileVerification.println("EPMA: "+error_EPMA);
		fileVerification.println("EP: "+error_EP+"\n");
		
		this.errors = new ArrayList();
		this.errorsValidation = new ArrayList();
		this.errorsVerification = new ArrayList();
		this.errorsVerification_EMA = new ArrayList();
		this.errorsVerification_EPMA = new ArrayList();
		this.errorsVerification_EP = new ArrayList();
		
		return error_EPMA;
		
	}
	
	//Neuron n = new Neuron(2);

	/**
	 * @param args
	 */
	

class Particle{
	int id;
	int inputQuantity;
	int hiddenQuantity;
	int outputQuantity;
	float c1[][];
	float c2[][];
	float h[];
	float w1[][];
	float w2[][];
	float b1[];
	float b2[];
	
	
	
	float setValidation[][];
	int sizeSetValidation;
	float perfomanceError;
	
	double sigmoidFunction (float w12){
		//System.out.println("net: "+net);
		double result = 1/( 1 + Math.pow(Math.E,(-1*w12)));
		//System.out.println("resultado da funcao sigmoidal: "+result);
		return result;
	}
	
	double der_sigmoidFunction(double x){
		return (x)*(1-x);
	}
	
	void resetParameters(){
		for(int i=0; i<this.hiddenQuantity; i++){
			w1[i][this.inputQuantity]=0;
		}
		for(int i=0; i<this.outputQuantity; i++){
			w2[i][this.hiddenQuantity]=0;
		}
		
	}
	void resetPerfomance(){
		this.perfomanceError=0;
	}
	
	void updateVelocity (Particle p, Particle g, float w, float c1, float r1, float c2, float r2){
		for(int i=0; i<this.hiddenQuantity; i++){
			for (int j=0; j<this.inputQuantity; j++){
				//w1[i][j] = w1[i][j]*w + c1*(p.w[i][j] - w[i][j])
			}
		}
		
	}
	
	void updatePosition (){
		for(int i=0; i<this.hiddenQuantity; i++){
			for (int j=0; j<this.inputQuantity; j++){
				//w1[i][j] = w1[i][j]*w + c1*(p.w[i][j] - w[i][j])
			}
		}
		
	}
	
	public Particle(int idArg, int inputQuantityArg, int hiddenQuantityArg, int outputQuantityArg, float setValidationArg[][], int sizeSetValidationArg){
		this.id = idArg;
		this.inputQuantity = inputQuantityArg;
		this.hiddenQuantity = hiddenQuantityArg;
		this.outputQuantity = outputQuantityArg;
		
		this.sizeSetValidation = sizeSetValidationArg;
		
		c1 = new float[hiddenQuantityArg][inputQuantityArg];
		c2 = new float[outputQuantityArg][hiddenQuantityArg];
		h = new float[hiddenQuantity];
		
		w1 = new float[hiddenQuantityArg+1][inputQuantityArg];
		w2 = new float[outputQuantityArg+1][hiddenQuantityArg];
		b1 = new float[hiddenQuantityArg];
		b2 = new float [outputQuantityArg];
		
		this.setValidation = setValidationArg;
		int error;
		
	}
	
	void validation(){

		for (int a =0; a<this.sizeSetValidation; a++){
			resetParameters();
			
			//entrada com escondida
			for (int i=0; i<this.hiddenQuantity; i++){
				if (h[i]>0){ // se esta unidade na camada escondida estiver ativa
					int j;
					for(j=0; j<this.inputQuantity; j++){
						
						//TODO LEMBRAR DE ZERAR OS NETS//
						
						if (c1[i][j] > 0){ //verifica se aquela conexão está ativa
							w1[i][inputNumber] += setValidation[a][j]*w1[i][j]; // faz o produto interno (peso*entrada)
						}
					}
					//bias da camada escondida
					w1[i][inputQuantity] += b1[i]*1; 
					
					w1[i][inputQuantity] = (float) sigmoidFunction(w1[i][inputQuantity]);
				}
			}
			//escondida com saída;
			for(int i=0; i<this.outputQuantity; i++){
				
				for (int j=0; j<hiddenQuantity; j++){
					if (h[j]>0 && c2[i][j]>0){ //esta unidade da camada escondida tá ativa?
						w2[i][hiddenQuantity] += w1[j][inputQuantity]*w2[i][j];
					}
				}
				
				w2[i][hiddenQuantity] += b2[i]*1;
				
				w2[i][hiddenQuantity] = (float)(sigmoidFunction(w2[i][hiddenQuantity]));
			}
			
			float localError =0;
			for(int i=0; i<outputNumber; i++){
				localError += Math.pow(setValidation[a][inputQuantity+i] - w2[i][hiddenQuantity],2);
			}
			
			perfomanceError += localError;
			
		}
		
		perfomanceError += perfomanceError/ (sizeSetValidation * outputNumber);
		
	}

	public void resetVelocity() {
		//vid = vid + (2*r-1)*vmax
		
	}
	
	
}

class RNA_PSO{
	Particle particles[];
	float w;
	float w1, w2;
	float c1;
	float r1;
	float c2;
	float r2;
	int sizePopulation;
	int[][] rangeVelocity;
	float inertiaWeightMAX;
	float inertiaWeightMIN;
	float c1i;
	float c2i;
	float c1f;
	float c2f;
	int toleranceConsecutiveFailures;
	float alpha;
	float beta;
	float varianceGaussian;

	int allowableIteration;
	int inputQuantity; 
	int hiddenQuantity; 
	int outputQuantity; 
	float [][]setValidation; 
	int sizeSetValidation;
	Particle []childs;
	
	void gabor(){
		
	}
	
	public RNA_PSO(int sizePopulationARG, int rangeVelocityARG[][], float inertiaWeightMAXArg, float inertiaWeightMINArg, float c1iArg, float c2iArg, int toleranceConsecutiveFailuresArg, float alphaArg, float betaArg, float varianceGaussianArg,int allowableIterationArg, int inputQuantityArg, int hiddenQuantityArg, int outputQuantityArg, float [][]setValidationArg,int sizeSetValidationArg, float w1Arg, float w2Arg){
		
		this.sizePopulation = sizePopulationARG;
		this.rangeVelocity = rangeVelocityARG;
		this.inertiaWeightMAX = inertiaWeightMAXArg;
		this.inertiaWeightMIN = inertiaWeightMINArg;
		this.c1i = c1iArg;
		this.c2i = c2iArg;
		this.w1 = w1Arg;
		this.w2 = w2Arg;
		this.toleranceConsecutiveFailures = toleranceConsecutiveFailuresArg;
		this.alpha = alphaArg;
		this.beta = betaArg;
		this.varianceGaussian = varianceGaussianArg;
		
		this.inputQuantity = inputQuantityArg; 
		this.hiddenQuantity = hiddenQuantityArg ; 
		this.outputQuantity = outputQuantityArg; 
		this.setValidation = setValidationArg; 
		this.sizeSetValidation = sizePopulationARG;
		
		//CARREGAR O ARQUIVO DE VALIDACAO
		
		this.allowableIteration = allowableIterationArg;
		this.particles = new Particle[sizePopulationARG];
		for(int i=0; i<sizePopulationARG; i++){
			particles[i] = new Particle(i, this.inputQuantity, hiddenQuantity, outputQuantity, setValidation, sizeSetValidation);
		}
		
	}
	
	void run(){
		Particle pbest = null;
		Particle gbest = null;
		int countBest=0;
		
		
		for(int iter=0; iter<allowableIteration; iter++){
			Particle gbestAux = gbest; 
			for(int j=0; j<this.sizePopulation; j++){
				if (j==0){
					pbest = particles[iter];
				}
				if (iter==0){
					gbest = particles[iter];
					countBest++;
				}
				this.particles[iter].validation();
				if (particles[iter].perfomanceError < pbest.perfomanceError ){
					pbest = particles[iter];
				}
				if (particles[iter].perfomanceError < gbest.perfomanceError){
					gbest = particles[iter];
				}
			}
			if (gbestAux  == gbest){
				countBest++;
			}else{
				countBest=0;
			}
			
			for(int a=0; a<this.sizePopulation; a++){
				particles[a].updateVelocity(pbest, gbest, w, c1, r1, c2, r2);
				particles[a].updatePosition();
			}
			
			//Adaption of the Basic PSO
			
			this.w = (this.w1 - this.w2)*(allowableIteration - iter)/allowableIteration + this.w2;
			if (this.w>this.w1){
				this.w = this.w1;
			}else if (this.w2>this.w ){
				this.w = this.w2;
			}
			
			this.c1 = (this.c1f-this.c1i)*iter/allowableIteration + c1i;
			
			this.c2 = (this.c2f - this.c2i)*iter/allowableIteration + c2i;
			
			//to avoid the permature stagnation
			if(countBest == toleranceConsecutiveFailures){
				for(int i=0; i<this.sizePopulation; i++){
					float prob= (float) Math.random();
					if (prob>0.5){
						particles[i].resetVelocity();
					}
				}
			}
			
			
			
			//CROSSOVER AND MUTATION OPERATORS 
			int parent1 = (int) (50*Math.random());
			int parent2 = (int) (50*Math.random());
			
			crossoverMutation(particles[parent1], particles[parent2]);
			
			
			
		}
	}

	void crossoverMutation(Particle particle1, Particle particle2) {
		// TODO Auto-generated method stub
		
	}

}
	
public static void main(String[] args){
	System.out.println("oi");
}
	
}	
/*
	public static void main(String[] args) {
		
		
		int qtdProgramRun = 10;
		int []neuronsPerLayer = {1,3,1};
		//MultilayerPerceptron mlp = new MultilayerPerceptron(int layerQuantity=3,  neuronsPerLayer, learningRate= 0.3, inputNumber= 10, outputNumber = 1, numberExamples = 100, rateTrainning = 50, quantityIteration = 50, fail = 10);
		Arquivo fileVerification = new Arquivo("inputNetworkVerification.txt", "outputNetwork.txt");
		double learningRate=0.1;
		for(int a=0; a<3; a++){
			if (a==0){
				learningRate=0.1;
			}else if (a==1){
				learningRate=0.01;
			}else if (a==2){
				learningRate=0.001;
			}
			for(int b=0; b<3; b++){
				if (b==0){
					neuronsPerLayer[1]=1;
				}else if (b==1){
					neuronsPerLayer[1]=10;
				}else if (b==2){
					neuronsPerLayer[1]=30;
				}
				
				//neuronsPerLayer[1] =  (int) (Math.random()*100);
				
				
				for(int c=0; c<3; c++){
					int qtdeIteracao =0;
					if (c==0){
						qtdeIteracao=5000;
					}else if (c==1){
						qtdeIteracao= 10000;
					}else if (c==2){
						qtdeIteracao = 15000;
					}
					double retorno =0;
					double retornoA =0;

					Arquivo fileVerificationAUX = new Arquivo("vazio.txt", "erros_"+learningRate+"_"+neuronsPerLayer[1]+"_"+qtdeIteracao);
					for(int i=0; i< qtdProgramRun; i++ ){
						int numberExamples = 100;
						int rateTrainning = 60;
						int numberVerification = 41;
						int fail = 10;
						double momentum = 0.3;
						int windowSize = 2;
						
						MultilayerPerceptron mlp = new MultilayerPerceptron(fileVerification, 3, neuronsPerLayer, learningRate, neuronsPerLayer[0], neuronsPerLayer[neuronsPerLayer.length -1], numberExamples, rateTrainning, qtdeIteracao, numberVerification, fail, qtdProgramRun, momentum, windowSize);
						mlp.forward();
						//System.out.println(mlp.neurons);
						mlp.verification();
						retornoA = mlp.finishSaveFile();
						retorno += retornoA;
						fileVerificationAUX.println(retornoA);
						
					}
					
					fileVerificationAUX.println("MEDIA: "+retorno/qtdProgramRun);
					
					
					
				}
			}
				
		}
		
		
		fileVerification.close();
		
	}

}

*/
