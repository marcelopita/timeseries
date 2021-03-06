/*
 * with momentum
 * */

package backup;
import java.util.ArrayList;
import java.util.Random;
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

public class MultilayerPerceptron_momentum {
	int layerQuantity;
	int neuronQuantityPerLayer[];
	double learningRate;
	int inputNumber;
	int outputNumber;
	int numberExamples;
	int rateTrainning;
	int quantifyIteration;
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
	int qtdeIteracao;
	double momentum;
	
	public MultilayerPerceptron_momentum(Arquivo fileVerification, int layerQuantity, int []neuronQuantityPerLayer, double learningRate, int inputNumber, int outputNumber, int numberExamples, int rateTrainning, int quantifyIteration, int verificationQuantity, int fail, int qtdeIteracao, double momentum){
		this.fileVerification = fileVerification;
		this.layerQuantity = layerQuantity;
		this.qtdeIteracao = qtdeIteracao;
		this.neuronQuantityPerLayer = neuronQuantityPerLayer;
		this.learningRate = learningRate;
		this.inputNumber = inputNumber;
		this.outputNumber = outputNumber;
		this.numberExamples = numberExamples;
		this.rateTrainning = rateTrainning;
		this.dataBaseTrainningTest = new double [numberExamples][inputNumber+outputNumber];
		this.dataBaseVerificationTest=  new double [verificationQuantity][inputNumber+outputNumber];
		this.quantifyIteration = quantifyIteration;
		this.fail = fail;
		this.momentum = momentum;
		
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
		
		readFile();
		
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
			
			if(total >this.quantifyIteration){
				System.out.println("SAIR POR QUANTIDADE DE ITERACAO");
				break;
			}
		//		System.out.println(this.neurons);
			
			
			
			
		}
		
		
	}
	
	void verification(){
		readFileVerification();
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
				localErrorVerification += Math.abs(this.dataBaseVerificationTest[a][this.inputNumber+i] - out.fnet);
				error_EPMA += (localErrorVerification/this.dataBaseVerificationTest[a][this.inputNumber+i]);
				
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
		this.fileVerification.println("Quantidade Iteracao: "+this.quantifyIteration);
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
		arq += this.neuronQuantityPerLayer[1]+"_"+this.qtdeIteracao;
		
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
		
		return error_EMA;
		
	}
	
	//Neuron n = new Neuron(2);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		
		int qtdeIterationProgram = 50;
		int []neuronsPerLayer = {44,3,1};
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
					for(int i=0; i< qtdeIterationProgram; i++ ){
						MultilayerPerceptron_momentum mlp = new MultilayerPerceptron_momentum(fileVerification, 3, neuronsPerLayer, learningRate, 44, 1, 186, 75, qtdeIteracao, 80, 20, qtdeIteracao, 0.3);
						mlp.forward();
						//System.out.println(mlp.neurons);
						mlp.verification();
						retornoA = mlp.finishSaveFile();
						retorno += retornoA;
						fileVerificationAUX.println(retornoA);
						
					}
					
					fileVerificationAUX.println("MEDIA: "+retorno/qtdeIterationProgram);
					
					
					
				}
			}
				
		}
		
		
		fileVerification.close();
		
	}

}
