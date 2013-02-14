/*
VERSAO 12 abril 2012 :: 22:05

Global position pelo INDICE do global - reseting velocity;
 * 
 */

package PSO2;
import logic.Arquivo;

import java.util.ArrayList;
import java.util.Random;
import java.awt.geom.QuadCurve2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream.GetField;
import java.math.*;
import java.net.MalformedURLException;
import java.net.URL;

import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import gabor.*;
import gabor.Main.ImagePanel;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.applet.Applet;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.Random;


class Velocity{
	int id;
	int inputQuantity;
	int hiddenQuantity;
	int outputQuantity;
	double c1[][];
	double c2[][];
	double h[];
	double w1[][];
	double w2[][];
	double b1[];
	double b2[];


	public Velocity(int idArg, int inputQuantityArg, int hiddenQuantityArg, int outputQuantityArg) {
		this.id = idArg;
		this.inputQuantity = inputQuantityArg;
		this.hiddenQuantity = hiddenQuantityArg;
		this.outputQuantity = outputQuantityArg;

		c1 = new double[hiddenQuantityArg][inputQuantityArg];
		c2 = new double[outputQuantityArg][hiddenQuantityArg];
		h = new double[hiddenQuantity];

		w1 = new double[hiddenQuantityArg][inputQuantityArg];
		
		w2 = new double[outputQuantityArg][hiddenQuantityArg];
		b1 = new double[hiddenQuantityArg];
		b2 = new double[outputQuantityArg];

		int error;

	}

	public void clonar(Velocity v){
		//Velocity r = new Velocity(0, v.inputQuantity, v.hiddenQuantity, v.outputQuantity);
		this.w1 = v.w1.clone();
		this.w2 = v.w2.clone();
		this.c1 = v.c1.clone();
		this.c2 = v.c2.clone();
		this.h = v.h.clone();
		this.b1 = v.b1.clone();
		this.b2 =v.b2.clone();
		
		
	}


}

class Particle {
	int id;
	int inputQuantity;
	int hiddenQuantity;
	int outputQuantity;
	double c1[][];
	double c2[][];
	double h[];
	double w1[][];
	double w2[][];
	double b1[];
	double b2[];
	Velocity velocity;

	double [][]setValidation;
	int sizeSetValidation;
	double perfomanceError;
	double velocityMax;
	double velocityMin;
	Random random;

	double perfomanceErrorTest;
	Particle pi;
	
	public Particle (int id){
		this.id = id;
	}
	public Particle(int idArg, int inputQuantityArg, int hiddenQuantityArg,
			int outputQuantityArg, double setValidationArg[][],
			int sizeSetValidationArg, double velocityMax, double velocityMin, Random randomArg) {
		this.velocity = new Velocity(idArg, inputQuantityArg, hiddenQuantityArg, outputQuantityArg);
		this.random = randomArg;
		this.id = idArg;
		this.inputQuantity = inputQuantityArg;
		this.hiddenQuantity = hiddenQuantityArg;
		this.outputQuantity = outputQuantityArg;

		this.sizeSetValidation = sizeSetValidationArg;

		c1 = new double[hiddenQuantityArg][inputQuantityArg+1];
		c2 = new double[outputQuantityArg][hiddenQuantityArg+1];
		h = new double[hiddenQuantity];

		w1 = new double[hiddenQuantityArg ][inputQuantityArg+1];
		w2 = new double[outputQuantityArg ][hiddenQuantityArg+1];
		b1 = new double[hiddenQuantityArg];
		b2 = new double[outputQuantityArg];
		
		this.velocityMax = velocityMax;
		this.velocityMin = velocityMin;
		initializeMatrix();
		initalizePi();
		this.setValidation = setValidationArg;
		int error;
		

	}
	/*construtor do clonar*/
	public Particle(int idArg, int inputQuantityArg, int hiddenQuantityArg,
			int outputQuantityArg, double setValidationArg[][],
			int sizeSetValidationArg, double velocityMax, double velocityMin, Random randomArg, String s) {
		this.velocity = new Velocity(idArg, inputQuantityArg, hiddenQuantityArg, outputQuantityArg);
		this.random = randomArg;
		this.id = idArg;
		this.inputQuantity = inputQuantityArg;
		this.hiddenQuantity = hiddenQuantityArg;
		this.outputQuantity = outputQuantityArg;

		this.sizeSetValidation = sizeSetValidationArg;
/*
		c1 = new double[hiddenQuantityArg][inputQuantityArg+1];
		c2 = new double[outputQuantityArg][hiddenQuantityArg+1];
		h = new double[hiddenQuantity];

		w1 = new double[hiddenQuantityArg ][inputQuantityArg+1];
		w2 = new double[outputQuantityArg ][hiddenQuantityArg+1];
		b1 = new double[hiddenQuantityArg];
		b2 = new double[outputQuantityArg];
*/
		
		this.velocityMax = velocityMax;
		this.velocityMin = velocityMin;
		//initializeMatrix();
		//initalizePi();
		this.setValidation = setValidationArg;
		int error;
		

	}
	
	public void initalizePi() {
		this.pi = this.clonarPi();
		this.pi.perfomanceError = 9999999;
	}

	public Particle clonar(){
		/*o construtor nao vai inicializar as matrizes, nem o pi, pq eles ja sao inicializados abaixo*/
		Particle r = new Particle(this.id, this.inputQuantity, this.hiddenQuantity, this.outputQuantity, this.setValidation, this.sizeSetValidation, this.velocityMax, this.velocityMin, this.random, "a");
		
		r.perfomanceError = this.perfomanceError;
		r.b1 = this.b1.clone();
		r.b2 = this.b2.clone();
		r.h = this.h.clone();
		
		r.w1 = this.w1.clone();
		r.w2 = this.w2.clone();
		r.c1 = this.c1.clone();
		r.c2 = this.c2.clone();
		
		r.velocity.clonar(this.velocity);
		r.perfomanceError = this.perfomanceError;
		r.pi = this.pi.clonarPi();
		
		return r;
	}
	
		public Particle clonarPi(){
		 /*este construtor nao inicializa as matrizes nem o pi e nao inicializa o pi*/
		Particle r = new Particle(this.id, this.inputQuantity, this.hiddenQuantity, this.outputQuantity, this.setValidation, this.sizeSetValidation, this.velocityMax, this.velocityMin, this.random, "pi");
		
		r.perfomanceError = this.perfomanceError;
		r.b1 = this.b1.clone();
		r.b2 = this.b2.clone();
		r.h = this.h.clone();
		
		r.w1 = this.w1.clone();
		r.w2 = this.w2.clone();
		r.c1 = this.c1.clone();
		r.c2 = this.c2.clone();
		
	//	r.velocity.clonar(this.velocity);
		r.perfomanceError = this.perfomanceError;
		
		return r;
	}
	double sigmoidFunction(double w12) {
		// System.out.println("net: "+net);
		double result = 1 / (1 + Math.pow(Math.E, (-1 * w12)));
		// System.out.println("resultado da funcao sigmoidal: "+result);

		return result;
	}

	double der_sigmoidFunction(double x) {
		return (x) * (1 - x);
	}

	void resetParameters() {
		for (int i = 0; i < this.hiddenQuantity; i++) {
			
			w1[i][this.inputQuantity] = 0;
		}
		for (int i = 0; i < this.outputQuantity; i++) {
			w2[i][this.hiddenQuantity] = 0;
		}

	}

	void resetPerfomance() {
		this.perfomanceError = 0;
	}

	void updatePositionVelocity(Particle g, double w, double c1, double r1,
			double c2, double r2){
		
		for (int i = 0; i < this.hiddenQuantity; i++) {
			for (int j = 0; j < this.inputQuantity; j++) {
				this.velocity.w1[i][j] = w*this.velocity.w1[i][j]+ c1*r1*(this.pi.w1[i][j] - this.w1[i][j]) + c2*r2*(g.w1[i][j] - this.w1[i][j]);
				//condicao em que a velocidade deve estar entre os valores permitidos
				if (this.velocity.w1[i][j]>this.velocityMax){
					this.velocity.w1[i][j] = this.velocityMax;
				}else if (this.velocity.w1[i][j] < this.velocityMin){
					this.velocity.w1[i][j] = this.velocityMin;
				}
				// condicao em que a velocidade deve estar entre os valores permitidos
				this.velocity.c1[i][j] = w*this.velocity.c1[i][j] + c1*r1*(this.pi.c1[i][j] - this.c1[i][j]) + c2*r2*(g.c1[i][j] - this.c1[i][j]);
				if (this.velocity.c1[i][j]>this.velocityMax){
					this.velocity.c1[i][j] = this.velocityMax;
				}else if (this.velocity.c1[i][j] < this.velocityMin){
					this.velocity.c1[i][j] = this.velocityMin;
				}
				//updating position
				this.w1[i][j] += this.velocity.w1[i][j]; 
				this.c1[i][j] += this.velocity.c1[i][j];
				
			}
		}
		
		for(int i=0; i<outputQuantity; i++){
			for(int j=0; j<hiddenQuantity; j++){
				this.velocity.w2[i][j] = w*this.velocity.w2[i][j] + c1*r1*(this.pi.w2[i][j] - this.w2[i][j]) + c2*r2*(g.w2[i][j] - this.w2[i][j]); 
				// condicao em que a velocidade deve estar entre os valores permitidos
				if (this.velocity.w2[i][j]>this.velocityMax){
					this.velocity.w2[i][j] = this.velocityMax;
				}else if (this.velocity.w2[i][j] < this.velocityMin){
					this.velocity.w2[i][j] = this.velocityMin;
				}
				
				this.velocity.c2[i][j] = w*this.velocity.c2[i][j] +  c1*r1*(this.pi.c2[i][j] - this.c2[i][j]) + c2*r2*(g.c2[i][j] - this.c2[i][j]);
				// condicao em que a velocidade deve estar entre os valores permitidos
				if (this.velocity.c2[i][j]>this.velocityMax){
					this.velocity.c2[i][j] = this.velocityMax;
				}else if (this.velocity.c2[i][j] < this.velocityMin){
					this.velocity.c2[i][j] = this.velocityMin;
				}
				
				//updating position
				this.w2[i][j] += this.velocity.w2[i][j];
				this.c2[i][j] += this.velocity.c2[i][j];
			}
		}
		
		for(int i=0; i<hiddenQuantity; i++){
			this.velocity.h[i] = w*this.velocity.h[i] +  c1*r1*(this.pi.h[i] - this.h[i]) + c2*r2*(g.h[i] - this.h[i]);
			// condicao em que a velocidade deve estar entre os valores permitidos
			if (this.velocity.h[i]>this.velocityMax){
				this.velocity.h[i] = this.velocityMax;
			}else if (this.velocity.h[i] < this.velocityMin){
				this.velocity.h[i] = this.velocityMin;
			}
			
			this.velocity.b1[i] = w*this.velocity.b1[i] +  c1*r1*(this.pi.b1[i] - this.b1[i]) + c2*r2*(g.b1[i] - this.b1[i]); 
			// condicao em que a velocidade deve estar entre os valores permitidos
			if (this.velocity.b1[i]>this.velocityMax){
				this.velocity.b1[i] = this.velocityMax;
			}else if (this.velocity.b1[i] < this.velocityMin){
				this.velocity.b1[i] = this.velocityMin;
			}
			//updating position
			this.h[i] += this.velocity.h[i];
			this.b1[i] += this.velocity.b1[i]; 
		}
		
		for (int i=0; i<outputQuantity; i++){
			this.velocity.b2[i] = w*this.velocity.b2[i] +  c1*r1*(this.pi.b2[i] - this.b2[i]) + c2*r2*(g.b2[i] - this.b2[i]); 
			// condicao em que a velocidade deve estar entre os valores permitidos
			if (this.velocity.b2[i]>this.velocityMax){
				this.velocity.b2[i] = this.velocityMax;
			}else if (this.velocity.b2[i] < this.velocityMin){
				this.velocity.b2[i] = this.velocityMin;
			}
			
			//updating position
			this.b2[i] += this.velocity.b2[i]; 
			
		}
	}
	void updateVelocity(Particle g, double w, double c1, double r1,
			double c2, double r2) {
		
		for (int i = 0; i < this.hiddenQuantity; i++) {
			for (int j = 0; j < this.inputQuantity; j++) {
				this.velocity.w1[i][j] = w*this.velocity.w1[i][j]+ c1*r1*(this.pi.w1[i][j] - this.w1[i][j]) + c2*r2*(g.w1[i][j] - this.w1[i][j]);
				//condicao em que a velocidade deve estar entre os valores permitidos
				if (this.velocity.w1[i][j]>this.velocityMax){
					this.velocity.w1[i][j] = this.velocityMax;
				}else if (this.velocity.w1[i][j] < this.velocityMin){
					this.velocity.w1[i][j] = this.velocityMin;
				}
				// condicao em que a velocidade deve estar entre os valores permitidos
				this.velocity.c1[i][j] = w*this.velocity.c1[i][j] + c1*r1*(this.pi.c1[i][j] - this.c1[i][j]) + c2*r2*(g.c1[i][j] - this.c1[i][j]);
				if (this.velocity.c1[i][j]>this.velocityMax){
					this.velocity.c1[i][j] = this.velocityMax;
				}else if (this.velocity.c1[i][j] < this.velocityMin){
					this.velocity.c1[i][j] = this.velocityMin;
				}
				
			}
		}
		
		for(int i=0; i<outputQuantity; i++){
			for(int j=0; j<hiddenQuantity; j++){
				this.velocity.w2[i][j] = w*this.velocity.w2[i][j] + c1*r1*(this.pi.w2[i][j] - this.w2[i][j]) + c2*r2*(g.w2[i][j] - this.w2[i][j]); 
				// condicao em que a velocidade deve estar entre os valores permitidos
				if (this.velocity.w2[i][j]>this.velocityMax){
					this.velocity.w2[i][j] = this.velocityMax;
				}else if (this.velocity.w2[i][j] < this.velocityMin){
					this.velocity.w2[i][j] = this.velocityMin;
				}
				
				this.velocity.c2[i][j] = w*this.velocity.c2[i][j] +  c1*r1*(this.pi.c2[i][j] - this.c2[i][j]) + c2*r2*(g.c2[i][j] - this.c2[i][j]);
				// condicao em que a velocidade deve estar entre os valores permitidos
				if (this.velocity.c2[i][j]>this.velocityMax){
					this.velocity.c2[i][j] = this.velocityMax;
				}else if (this.velocity.c2[i][j] < this.velocityMin){
					this.velocity.c2[i][j] = this.velocityMin;
				}
			}
		}
		
		for(int i=0; i<hiddenQuantity; i++){
			this.velocity.h[i] = w*this.velocity.h[i] +  c1*r1*(this.pi.h[i] - this.h[i]) + c2*r2*(g.h[i] - this.h[i]);
			// condicao em que a velocidade deve estar entre os valores permitidos
			if (this.velocity.h[i]>this.velocityMax){
				this.velocity.h[i] = this.velocityMax;
			}else if (this.velocity.h[i] < this.velocityMin){
				this.velocity.h[i] = this.velocityMin;
			}
			
			this.velocity.b1[i] = w*this.velocity.b1[i] +  c1*r1*(this.pi.b1[i] - this.b1[i]) + c2*r2*(g.b1[i] - this.b1[i]); 
			// condicao em que a velocidade deve estar entre os valores permitidos
			if (this.velocity.b1[i]>this.velocityMax){
				this.velocity.b1[i] = this.velocityMax;
			}else if (this.velocity.b1[i] < this.velocityMin){
				this.velocity.b1[i] = this.velocityMin;
			}
		}
		
		for (int i=0; i<outputQuantity; i++){
			this.velocity.b2[i] = w*this.velocity.b2[i] +  c1*r1*(this.pi.b2[i] - this.b2[i]) + c2*r2*(g.b2[i] - this.b2[i]); 
			// condicao em que a velocidade deve estar entre os valores permitidos
			if (this.velocity.b2[i]>this.velocityMax){
				this.velocity.b2[i] = this.velocityMax;
			}else if (this.velocity.b2[i] < this.velocityMin){
				this.velocity.b2[i] = this.velocityMin;
			}
			
		}

	}

	void updatePosition() {
		for (int i = 0; i < this.hiddenQuantity; i++) {
			for (int j = 0; j < this.inputQuantity; j++) {
				this.w1[i][j] += this.velocity.w1[i][j]; 
				this.c1[i][j] += this.velocity.c1[i][j];
			}
		}
		
		for(int i=0; i<outputQuantity; i++){
			for(int j=0; j<hiddenQuantity; j++){
				this.w2[i][j] += this.velocity.w2[i][j];
				this.c2[i][j] += this.velocity.c2[i][j];
			}
		}
		
		for(int i=0; i<hiddenQuantity; i++){
			this.h[i] += this.velocity.h[i];
			this.b1[i] += this.velocity.b1[i]; 
		}
		
		for (int i=0; i<outputQuantity; i++){
			this.b2[i] += this.velocity.b2[i]; 
		}

	}

	
	private void initializeMatrix() {
	
		
		for (int i = 0; i < this.hiddenQuantity; i++) {
			for (int j = 0; j < this.inputQuantity; j++) {
				this.w1[i][j] =  -1 + 2*random.nextDouble() ;
				this.c1[i][j] = (double) random.nextDouble();
			}
		}
		
		for(int i=0; i<outputQuantity; i++){
			for(int j=0; j<hiddenQuantity; j++){
				this.w2[i][j] =  -1 + 2*random.nextDouble();
				this.c2[i][j] =(double) random.nextDouble();
			}
		}
		
		for(int i=0; i<hiddenQuantity; i++){
			this.h[i] = (double) random.nextDouble();
			this.b1[i] = -1+2*random.nextDouble(); 
		}
		
		for (int i=0; i<outputQuantity; i++){
			this.b2[i] = -1+2*random.nextDouble();
		}
		
	}

	void validation() {

	
		double localError=0;
		
		for (int a = 0; a < this.sizeSetValidation; a++) {
			resetParameters();

			// entrada com escondida
			for (int i = 0; i < this.hiddenQuantity; i++) {
				if (h[i] > 0) { // se esta unidade na camada escondida estiver
								// ativa
					int j;
					for (j = 0; j < this.inputQuantity; j++) {

						// TODO LEMBRAR DE ZERAR OS NETS//

						if (c1[i][j] > 0) { // verifica se aquela conex�o est�
											// ativa
							try{
								w1[i][inputQuantity] += this.setValidation[a][j] * w1[i][j]; // faz o produto interno
												// (peso*entrada)
							}catch(Exception e){
								System.out.println("i: "+ i);
								System.out.println("j: " +j);
								System.out.println("imp: ");
								System.out.println(this.sizeSetValidation);
								System.out.println(this.hiddenQuantity);
								System.out.println(this.inputQuantity);
							}
						
						}
					}
					// bias da camada escondida
					w1[i][inputQuantity] += b1[i] * 1;

					w1[i][inputQuantity] = (double) sigmoidFunction(w1[i][inputQuantity]);
				}
			}
			// escondida com sa�da;
			for (int i = 0; i < this.outputQuantity; i++) {

				for (int j = 0; j < hiddenQuantity; j++) {
					if (h[j] > 0 && c2[i][j] > 0) { // esta unidade da camada
													// escondida t� ativa?
						w2[i][hiddenQuantity] += w1[j][inputQuantity]* w2[i][j];				

					}
				}

				w2[i][hiddenQuantity] += b2[i] * 1;
						if (Double.isNaN(b2[i])){
								System.out.println("ERROR NO b2 "+i);
						}
				w2[i][hiddenQuantity] = (double) (sigmoidFunction((w2[i][hiddenQuantity])));

			}

			
			localError = 0;
			for (int i = 0; i < outputQuantity; i++) {
				localError += Math.pow(setValidation[a][inputQuantity + i]- w2[i][hiddenQuantity], 2);
				if (Double.isNaN(localError)){
					System.out.println("error");
					System.out.println(w2[i][hiddenQuantity]);
				}
			}
			
			this.perfomanceError += localError;

		}
		//System.out.println("termina validacao");
		
		this.perfomanceError = perfomanceError/ (sizeSetValidation * outputQuantity);
		//System.out.println(this.perfomanceError);
		/* Se a posicao guardada estiver melhor*/
		if (pi.perfomanceError > perfomanceError){
			this.pi = this.clonarPi();
		}
		
	//	System.out.println(this.perfomanceError);
			
	}

	void runTest(int sizeSetTest, double[][]setTest) {

		/*
		 * TODO APAGAR ESTA ATRIBUICAO ABAIXO
		 */
		
	//	this.sizeSetValidation = 10;
		//System.out.println("come�a validacao");
		double localError=0;
		//System.out.println(sizeSetValidation);
		//System.out.println(hiddenQuantity);
		//System.out.println(inputQuantity);
		for (int a = 0; a < sizeSetTest; a++) {
			resetParameters();

			// entrada com escondida
			for (int i = 0; i < this.hiddenQuantity; i++) {
				if (h[i] > 0) { // se esta unidade na camada escondida estiver
								// ativa
					int j;
					for (j = 0; j < this.inputQuantity; j++) {

						// TODO LEMBRAR DE ZERAR OS NETS//

						if (c1[i][j] > 0) { // verifica se aquela conex�o est�
											// ativa
							try{
								w1[i][inputQuantity] += setTest[a][j] * w1[i][j]; // faz o produto interno
												// (peso*entrada)
							}catch(Exception e){
								System.out.println("i: "+ i);
								System.out.println("j: " +j);
								System.out.println("imp: ");
								System.out.println(this.sizeSetValidation);
								System.out.println(this.hiddenQuantity);
								System.out.println(this.inputQuantity);
							}
						
						}
					}
					// bias da camada escondida
					w1[i][inputQuantity] += b1[i] * 1;

					w1[i][inputQuantity] = (double) sigmoidFunction(w1[i][inputQuantity]);
				}
			}
			// escondida com sa�da;
			for (int i = 0; i < this.outputQuantity; i++) {

				for (int j = 0; j < hiddenQuantity; j++) {
					if (h[j] > 0 && c2[i][j] > 0) { // esta unidade da camada
													// escondida t� ativa?
						w2[i][hiddenQuantity] += w1[j][inputQuantity]
								* w2[i][j];				

					}
				}

				w2[i][hiddenQuantity] += b2[i] * 1;
						if (Double.isNaN(b2[i])){
								System.out.println("ERROR NO b2 "+i);
						}
				w2[i][hiddenQuantity] = (double) (sigmoidFunction((w2[i][hiddenQuantity])));

			}

			
			localError = 0;
			for (int i = 0; i < outputQuantity; i++) {
				localError += Math.pow(setTest[a][inputQuantity + i]
						- w2[i][hiddenQuantity], 2);
				if (Double.isNaN(localError)){
					System.out.println("error");
					System.out.println(w2[i][hiddenQuantity]);
				}
			}
			
			this.perfomanceErrorTest += localError;

		}
		//System.out.println("termina validacao");
		
		this.perfomanceErrorTest = perfomanceErrorTest/ (sizeSetTest * outputQuantity);
		
	//	System.out.println(this.perfomanceError);
			
	}
	
	private double tanHipFunction(double d) {
		double retorno = (Math.pow(Math.E,d) - Math.pow(Math.E, -d)) /(Math.pow(Math.E, d) + Math.pow(Math.E, -d)); 
		return retorno;
	}

	public void resetVelocity(double rangeVelocityMAX) {
		Random random = new Random(System.currentTimeMillis());  
		
		double probD1 = random.nextDouble();
		double probD2 = random.nextDouble();
		
		double r1 = (double)random.nextDouble();
		
		
		for (int i = 0; i < this.hiddenQuantity; i++) {
			for (int j = 0; j < this.inputQuantity; j++) {
				if (probD1 > 0.5){
					this.velocity.w1[i][j] +=  (2*r1-1)*rangeVelocityMAX;
					// condicao em que a velocidade deve estar entre os valores permitidos
					if (this.velocity.w1[i][j]>this.velocityMax){
						this.velocity.w1[i][j] = this.velocityMax;
					}else if (this.velocity.w1[i][j] < this.velocityMin){
						this.velocity.w1[i][j] = this.velocityMin;
					}
				}
				
				if (probD2 > 0.5){
					this.velocity.c1[i][j] += (2*r1-1)*rangeVelocityMAX;
					// condicao em que a velocidade deve estar entre os valores permitidos
					if (this.velocity.c1[i][j]>this.velocityMax){
						this.velocity.c1[i][j] = this.velocityMax;
					}else if (this.velocity.c1[i][j] < this.velocityMin){
						this.velocity.c1[i][j] = this.velocityMin;
					}
				}
			}
		}
		
		
		probD1 = random.nextDouble();
		probD2 = random.nextDouble();
		
		for(int i=0; i<outputQuantity; i++){
			for(int j=0; j<hiddenQuantity; j++){
				if (probD1>0.5){
					this.velocity.w2[i][j] += (2*r1-1)*rangeVelocityMAX; 
					// condicao em que a velocidade deve estar entre os valores permitidos
					if (this.velocity.w2[i][j]>this.velocityMax){
						this.velocity.w2[i][j] = this.velocityMax;
					}else if (this.velocity.w2[i][j] < this.velocityMin){
						this.velocity.w2[i][j] = this.velocityMin;
					}
				}
				if (probD2 >0.5){
					this.velocity.c2[i][j] += (2*r1-1)*rangeVelocityMAX;
					// condicao em que a velocidade deve estar entre os valores permitidos
					if (this.velocity.c2[i][j]>this.velocityMax){
						this.velocity.c2[i][j] = this.velocityMax;
					}else if (this.velocity.c2[i][j] < this.velocityMin){
						this.velocity.c2[i][j] = this.velocityMin;
					}
				}
			}
		}
		

		
		probD1 = random.nextDouble();
		probD2 = random.nextDouble();
		
		for(int i=0; i<hiddenQuantity; i++){
			if (probD1 >0.5){
				this.velocity.h[i] += (2*r1-1)*rangeVelocityMAX; 
				// condicao em que a velocidade deve estar entre os valores permitidos
				if (this.velocity.h[i]>this.velocityMax){
					this.velocity.h[i] = this.velocityMax;
				}else if (this.velocity.h[i] < this.velocityMin){
					this.velocity.h[i] = this.velocityMin;
				}
			}
			if (probD2 >0.5){
				this.velocity.b1[i] += (2*r1-1)*rangeVelocityMAX; 
				// condicao em que a velocidade deve estar entre os valores permitidos
				if (this.velocity.b1[i]>this.velocityMax){
					this.velocity.b1[i] = this.velocityMax;
				}else if (this.velocity.b1[i] < this.velocityMin){
					this.velocity.b1[i] = this.velocityMin;
				}
			}
		}
		
		
		probD1 = random.nextDouble();
		probD2 = random.nextDouble();
		
		for (int i=0; i<outputQuantity; i++){
			if (probD1 > 0.5){
				this.velocity.b2[i] += (2*r1-1)*rangeVelocityMAX; 
				// condicao em que a velocidade deve estar entre os valores permitidos
				if (this.velocity.b2[i]>this.velocityMax){
					this.velocity.b2[i] = this.velocityMax;
				}else if (this.velocity.b2[i] < this.velocityMin){
					this.velocity.b2[i] = this.velocityMin;
				}
			}
		}

	}
	

}



public class PSO_RNA {
	Particle []particles;
	double w;
	double w1, w2;
	double c1;
	double r1;
	double c2;
	double r2;
	int sizePopulation;
	double rangeVelocityMAX;
	double rangeVelocityMIN;
	
	double inertiaWeightMAX;
	double inertiaWeightMIN;
	double c1i;
	double c2i;
	double c1f;
	double c2f;
	int toleranceConsecutiveFailures;
	double alpha;
	double beta;
	double varianceGaussian;

	int allowableIteration;
	int inputQuantity;
	int hiddenQuantity;
	int outputQuantity;
	double[][] setValidation;
	int sizeSetValidation;
	Particle[] childs;
	Random rGaussian;
	Random random;
	int quantitySelectedForGeneration;
	Arquivo errorOut;
	private int sizeSetTest;
	String arqVal;
	String arqTest;
	private int countBest;
	
	
	public PSO_RNA() {

	}

	public PSO_RNA(int sizePopulationARG, double rangeVelocityMAXArg, double rangeVelocityMINArg,
			double inertiaWeightMAXArg, double inertiaWeightMINArg, double c1iArg,
			double c2iArg, int toleranceConsecutiveFailuresArg, double alphaArg,
			double betaArg, double varianceGaussianArg,
			int allowableIterationArg, int inputQuantityArg,
			int hiddenQuantityArg, int outputQuantityArg,
			double[][] setValidationArg, int sizeSetValidationArg, int quantitySelectedForGenerationArg, String arqVal, String arqTest, double c1fArg, double c2fArg) throws IOException {

		this.arqVal = arqVal;
		this.arqTest = arqTest;
		this.quantitySelectedForGeneration = quantitySelectedForGenerationArg;
		this.random = new Random(System.currentTimeMillis());
		this.sizePopulation = sizePopulationARG;
		this.rangeVelocityMAX = rangeVelocityMAXArg;
		this.rangeVelocityMIN = rangeVelocityMINArg;
		
		this.inertiaWeightMAX = inertiaWeightMAXArg;
		this.inertiaWeightMIN = inertiaWeightMINArg;
		
		this.w1 = inertiaWeightMIN;
		this.w2 = inertiaWeightMAX;
		this.c1i = c1iArg;
		this.c2i = c2iArg;
		
		this.c1f = c1fArg;
		this.c2f = c2fArg;

		this.toleranceConsecutiveFailures = toleranceConsecutiveFailuresArg;
		this.alpha = alphaArg;
		this.beta = betaArg;
		this.varianceGaussian = varianceGaussianArg;

		//this.inputQuantity = inputQuantityArg;
		this.hiddenQuantity = hiddenQuantityArg;
		this.outputQuantity = outputQuantityArg;
		//this.setValidation = setValidationArg;
		//this.sizeSetValidation = sizeSetValidationArg;

		this.rGaussian = new Random();
		// CARREGAR O ARQUIVO DE VALIDACAO
		readSetValidation();
		
		this.allowableIteration = allowableIterationArg;
		this.particles = new Particle[sizePopulationARG*10];
		System.out.println("Inicializando as part�culas");
		for (int i = 0; i < sizePopulationARG*2; i++) {
			//System.out.println(i);
			particles[i] = new Particle(i, this.inputQuantity, hiddenQuantity,
					outputQuantity, setValidation, this.sizeSetValidation, rangeVelocityMAXArg, rangeVelocityMINArg, this.random);
		}
		
		System.out.println("Part�culas inicializadas");
		errorOut = new Arquivo ("vazio", arqVal);

	}
	
	

	/* 
	 * Ordena dois trechos ordenados e adjacente de vetores e ordena-os
	 * conjuntamente
	 */
	

	private void readSetValidation() throws IOException {
		// TODO Auto-generated method stub7
		System.out.println("Lendo o conjunto de teste");

		Arquivo in = new Arquivo("./faces/svm.train.normgrey", "vazio");
		int sizeTrain = in.readInt();
		int sizeDimension = in.readInt();
		this.sizeSetValidation = sizeTrain;
		this.inputQuantity = sizeDimension-1;
		
		System.out.println(sizeTrain + " "+sizeDimension );
		
		this.setValidation = new double[sizeTrain][sizeDimension];
		
		for(int i=0; i<sizeTrain; i++){
			for(int j=0; j<sizeDimension; j++){
				this.setValidation[i][j] = (double)in.readDouble();
				//System.out.println(setValidation[i][j]);
			}
		}
		for(int i=0; i<sizeTrain; i++){
			if (this.setValidation[i][sizeDimension-1] == -1){
				this.setValidation[i][sizeDimension-1] =0;
			}
		}
		in.close();
		System.out.println("Finalizando a leitura do conjunto de valida��o");
		
	}
	
	private double [][] readSetTest() throws IOException {
		// TODO Auto-generated method stub7
		System.out.println("Lendo o conjunto de teste");

		Arquivo in = new Arquivo("./faces/svm.test.normgrey", "vazio");
		int sizeTest = in.readInt();
		int sizeDimension = in.readInt();
		this.sizeSetTest = sizeTest;
		this.inputQuantity = sizeDimension-1;
		
		double [][]testValidation = new double[sizeTest][sizeDimension];
		
		System.out.println(sizeTest + " "+sizeDimension );
		
		//this.setValidation = new double[sizeTrain][sizeDimension];
		
		for(int i=0; i<sizeTest; i++){
			for(int j=0; j<sizeDimension; j++){
				testValidation[i][j] = (double)in.readDouble();
				//System.out.println(setValidation[i][j]);
			}
		}
		for(int i=0; i<sizeTest; i++){
			if (testValidation[i][sizeDimension-1] == -1){
				testValidation[i][sizeDimension-1] =0;
			}
		}
		in.close();
		System.out.println("Finalizando a leitura do conjunto de teste");
		
		return testValidation;
		
	}

	void run() {
	//	Particle pbest = null; // melhor de cada particula
		Particle gbest = null; // melhor de todas as iteracoes
		int globalPosition =0;
		this.countBest = 0;
		System.out.println("RUN!");
		//roda para cada part�cula o conjunto de validacao enquanto escolhe o melhor global e o melhor do enxame
		for (int iter = 0; iter < allowableIteration; iter++) {
			//printErrors(iter);
			System.out.println("Iteracao "+iter);

			if (iter==0){
		//		pbest = particles[0].clonar();
			//	pbest.perfomanceError=999999;
				gbest = particles[0].clonar();
				//globalPosition=0;
				gbest.perfomanceError = 9999;
			}			
			double globalPerfomance = gbest.perfomanceError;
			double globalPerfomanceAtual = globalPerfomance;
			double menorMSEIteracao =99999;
			int indiceGlobal=0;
			for (int j = 0; j < this.sizePopulation; j++) {

				this.particles[j].resetPerfomance();
				this.particles[j].validation();
				
				if (particles[j].perfomanceError < globalPerfomanceAtual) {
					globalPerfomanceAtual = particles[j].perfomanceError; //particles[j].clonar(); // melhor do enxame
					indiceGlobal = j;
				}
				if (particles[j].perfomanceError < menorMSEIteracao){
					menorMSEIteracao = particles[j].perfomanceError;
				}
			}
			if (globalPerfomance != globalPerfomanceAtual){
				gbest = particles[indiceGlobal].clonar();
			}

			if (gbest.perfomanceError >= globalPerfomance) {
				countBest++;
			} else if (gbest.perfomanceError < globalPerfomance){
				countBest = 0;
			}
			
			// to avoid the premature stagnation
			if (countBest >= this.toleranceConsecutiveFailures) {
				System.out.println("AVOIDING THE PREMATURE STAGNATION");
				System.out.println("tolerancia consecutiva alcancada");
				for (int i = 0; i < this.sizePopulation; i++) {
					double prob = (double) random.nextDouble();
					if (prob >= 0.5) {
						particles[i].resetVelocity(this.rangeVelocityMAX);
					}
				}
				countBest=0;
			}
			
			// Adaption of the Basic PSO

			this.w = (this.w1 - this.w2) * (allowableIteration - iter)/ allowableIteration + this.w2;

			this.c1 = (this.c1f - this.c1i) * iter / allowableIteration + c1i;
			this.c2 = (this.c2f - this.c2i) * iter / allowableIteration + c2i;
			
			//Atualiza a velocidade e a posi��o
			System.out.println("Atualizando a velocidade e posicao");
			this.r1 = random.nextDouble();
			this.r2 = random.nextDouble();
			System.out.println("w: "+this.w+" c1: "+this.c1+" c2: "+this.c2+" r2: "+this.r2);
			
			//atualizando o gbest
			
			
			for (int a = 0; a < this.sizePopulation; a++) {
				particles[a].updatePositionVelocity(gbest, this.w, this.c1, this.r1, this.c2, this.r2);
			}
			
			gbest.updatePositionVelocity(gbest, this.w, this.c1, this.r1, this.c2, this.r2);
		
			System.out.println("gbest: "+gbest.perfomanceError);
			System.out.println("menor da iteracao: "+menorMSEIteracao);
		
			
			printErrors(iter);
			
			this.particles = this.fitnessFunctionSelection();
			cross(iter);
			
		}
	}

	private void cross(int iter) {
		Particle []parents = new Particle[quantitySelectedForGeneration];
		int []parentsIndex = new int [quantitySelectedForGeneration] ;
		int qtdeParents =0;
		for(int i=0; i<this.sizePopulation; i++){
			double prob = random.nextDouble();
			if (prob >= this.alpha){
				parentsIndex[qtdeParents]=i;
				qtdeParents++;
				
			}
		}
		
		if((qtdeParents %2 )!=0){
			int a = (int)(random.nextDouble() * sizePopulation);
			parentsIndex[qtdeParents-1] = a;
			qtdeParents++;
			
		}
		System.out.println("Pais para cruzamento: "+qtdeParents);
		int p1=0, p2=0;
		for(; qtdeParents>0;){
		//	System.out.println(qtdeParents);
			if (qtdeParents == 2){
				p1 =0;
				p2=1;
			}else{
				p1 = (int) (random.nextDouble() * (qtdeParents-1));
				p2=0;
				do{
					p2 =(int)( random.nextDouble() * (qtdeParents-1));
				}while (p1 == p2);
			}
		//	System.out.println("P1: "+p1 + " "+p2);
			int indexP1 = parentsIndex[p1];
			int indexP2 = parentsIndex[p2];
			Particle []children = crossoverMutation(particles[indexP1], particles[indexP2], iter, rGaussian);
			particles[indexP1] = children[0];
			particles[indexP2] = children[1];
			
			parentsIndex[p1] = parentsIndex[qtdeParents - 1];
			parentsIndex[p2] = parentsIndex[qtdeParents -2];
			qtdeParents -=2;
		}
		
		/*
		boolean parentsChoosed = false;
		while (!parentsChoosed){
			int p1 = (int) (Math.random()*sizePopulation);
			int p2 = (int) (Math.random()*sizePopulation);
			
			double probabilityCross = Math.random();
			if (probabilityCross >= this.alpha){
				parentsChoosed = true;
				Particle []children = crossoverMutation(particles[p1], particles[p2], iter, rGaussian);
				particles[this.sizePopulation] = children[0];
				sizePopulation++;
				particles[this.sizePopulation] = children[1];
				sizePopulation++;
			}

		}
		*/
		
		 
		/*
		 * !!
		 
		  
		Particle p1 = null;
		Particle p2 = null;
		for(int i=0; i<sizePopulation; i++){					
			if (i==0){
				p1 = particles[i];
				p2 = particles[i+1];
			}
			if (p1.perfomanceError > particles[i].perfomanceError){
				p2 = p1;
				p1 = particles[i];
				
			}else if (particles[i].perfomanceError < p2.perfomanceError){
				p2 = particles[i];
			}
		}
		
		Particle []children = crossoverMutation(p1, p2, iter, rGaussian);
		this.particles[this.sizePopulation] = children[0];
		this.sizePopulation++;
		this.particles[this.sizePopulation] = children[1];
		this.sizePopulation++;
		
		*/
		//System.out.println("Fitness");
		
		//System.out.println("Heapsort");
	//	heapSort(particles, this.sizePopulation);
		
	//	double percentsElitismo = 0.96;
	//	int elitismQ = 48;//(int) (this.quantitySelectedForGeneration*percentsElitismo);
		//Particle []offspring = new Particle[this.quantitySelectedForGeneration];
		//Particle []particles2 = fitnessFunctionSelectionN(elitismQ);
		
		//for(int i=0; i< elitismQ; i++){
		//	offspring[i] = particles[i];
		//}
		
	//	int [][]parents = new int [quantitySelectedForGeneration][2];
	//	int qtdeCross =0;
	//	Particle []children = crossoverMutation(particles[0], particles[1], iter, rGaussian);
		//offspring[48] = children[0];
		//offspring[49] = children[1];
		
		//particles = offspring;
	//	this.sizePopulation = this.quantitySelectedForGeneration; 
		
		
	//	System.out.println("Nova geracao");
	//	System.out.println("elitismo "+elitismQ);
	//	System.out.println("qtde de nova geracao: "+ (quantitySelectedForGeneration - elitismQ));
	/*	
		for (int newOff=0; newOff< (this.quantitySelectedForGeneration); newOff+=2){
		//	System.out.println("nova geracao: "+ newOff);
			//System.out.println(newOff);
			int p1, p2;
			boolean parentesNaoOk ;
			do{
				parentesNaoOk = false;
				p1 = (int) (this.random.nextDouble() *quantitySelectedForGeneration);
				p2 = (int) (this.random.nextDouble()*quantitySelectedForGeneration);
			//	System.out.println(p1+ " "+p2);
				//System.out.println(quantitySelectedForGeneration);
				//System.out.println(Math.random());
				for(int i=0; i<qtdeCross; i++){
					if (parents[i][0] == p1 && parents[i][1] == p2){
						parentesNaoOk= true;
					}else if (parents[i][0] == p2 && parents[i][1] == p1){
						parentesNaoOk = true;
					}
				}
			}while(parentesNaoOk);
			
			//System.out.println(newOff);
			parents[qtdeCross][0] = p1;
			parents[qtdeCross][1] = p2;
			qtdeCross++;
			
			Particle []children = crossoverMutation(particles[p1], particles[p2], iter, rGaussian);
			offspring[newOff] = children[0];
			if (newOff+1 < this.quantitySelectedForGeneration){
					offspring[newOff+1] = children[1];
			
			}
		
		}
		*/
			/*
			boolean parentesOK = true;
			int sum=0;
			for(int i=0; i<this.sizePopulation; i++){
				sum += (1-particles[i].perfomanceError);
			}
			double rnd = Math.random()*sum;
			double rndAux = rnd;
			int p1=0;
			int p2=0;
			for(int i=0; i<this.sizePopulation; i++){
				rndAux -= (1-particles[i].perfomanceError); 
				if (rndAux <0){
					p1=i;
					break;
				}
			}
			
			for(int i=0; i<this.sizePopulation; i++){
				rnd -= (1-particles[i].perfomanceError); 
				if (rnd <0){
					p2=i;
					break;
				}
			}
			//verify if the parents were crossed
			int trying =0;
			do{
				for(int i=0; i<qtdeCross; i++){
					if (parents[i][0] == p1 && parents[i][1] == p2){
						parentesOK= false;
					}else if (parents[i][0] == p2 && parents[i][1] == p1){
						parentesOK = false;
					}
				}
				if (!parentesOK){
					rnd = Math.random()*sum;
					for(int i=0; i<this.sizePopulation; i++){
						rnd -= (1-particles[i].perfomanceError); 
						if (rnd <0){
							p2=i;
							break;
						}
					}
				}
				trying+=1;
				if (trying > 1000){
					break;
				}
			}while (!parentesOK);
			
			if (parentesOK){
				System.out.println(newOff);
				parents[qtdeCross][0] = p1;
				parents[qtdeCross][1] = p2;
				qtdeCross++;
				
				Particle []children = crossoverMutation(particles[p1], particles[p2], iter, rGaussian);
				offspring[newOff] = children[0];
				offspring[newOff+1] = children[1];
		
			}else{
				newOff-=2;
			//	System.out.println("CROSS NEGADO");
			}
			*/
		//}
		//particles = null;
		

	//	this.sizePopulation = this.quantitySelectedForGeneration;
		//particles = null;
	//	particles = offspring;
		System.out.println("geracao finalizada");
		

		
	//	printErrors(iter);
		
	}

	private void printErrors(int j) {
	
		System.out.println("Print error: "+j+"");
		System.out.println("tamanho da populacao: "+this.sizePopulation);
		this.errorOut.print("Geracao_"+j+"\n");
		for(int i=0; i<this.sizePopulation; i++){
			//System.out.println("Erro ("+i+"): "+this.particles[i].perfomanceError);
			this.errorOut.print(this.particles[i].perfomanceError+"\n");
		}
		this.errorOut.flush();
	}

	private Particle[] fitnessFunctionSelection() {
		//Particle p[] = new Particle[quantitySelectedForGeneration*3];
		int idW =0;
		double valueWorse = 0;
		int sizePopulationAux = sizePopulation;
	
		for(int i=0; i < this.sizePopulation - this.quantitySelectedForGeneration; i++){
			valueWorse = 0;
			for(int j=0; j<sizePopulationAux; j++){
				if (this.particles[j].perfomanceError > valueWorse){
					idW = j;
					valueWorse = particles[j].perfomanceError;
				}
			}
			
		
			particles[idW] = particles[sizePopulationAux-1];
			sizePopulationAux--;
		}
		
		this.sizePopulation = quantitySelectedForGeneration;
		return particles;
		
	}
	private Particle[] fitnessFunctionSelectionN(int qtde) {
		//Particle p[] = new Particle[quantitySelectedForGeneration*3];
		int idW =0;
		double bestV = 0;
		int sizePopulationAux = sizePopulation;
	
		Particle [] newParticles = new Particle[qtde];
		int []marked = new int [sizePopulation];
		
		for(int i=0; i < qtde; i++){
			bestV = 99999;
			for(int j=0; j<sizePopulationAux; j++){
				if (this.particles[j].perfomanceError < bestV && marked[j]==0){
					idW = j;
					bestV = particles[j].perfomanceError;
				}
			}
			
			marked[idW] = 1;
		
			newParticles[i] = particles[idW].clonar();
		}
		
		return newParticles;
		
	}
	Particle[] crossoverMutation(Particle p1, Particle p2, int iter, Random rGaussianArg) {
		
		double r1,r2,r3,r4;
		Particle []children = new Particle[2];
		
		children[0]= new Particle(this.sizePopulation, this.inputQuantity, this.hiddenQuantity, this.outputQuantity, setValidation, this.sizeSetValidation, this.rangeVelocityMAX, this.rangeVelocityMIN, this.random);
		children[1]= new Particle(this.sizePopulation+1, this.inputQuantity, this.hiddenQuantity, this.outputQuantity, setValidation, this.sizeSetValidation, this.rangeVelocityMAX, this.rangeVelocityMIN, this.random);
		
		
		//CROSSOVER
		
		r1 = (double)random.nextDouble();
		r2 = (double)random.nextDouble();
		r3 = (double)random.nextDouble();
		r4 = (double) random.nextDouble();
		for (int i = 0; i < this.hiddenQuantity; i++) {
			for (int j = 0; j < this.inputQuantity; j++) {
				children[0].w1[i][j] = (r1*p1.w1[i][j]) + (1-r1)*p2.w1[i][j];
				children[0].c1[i][j] = (r3*p1.c1[i][j]) + (1-r3)*p2.c1[i][j];
				
				children[1].w1[i][j] = (r2*p2.w1[i][j]) + (1-r2)*p1.w1[i][j];
				children[1].c1[i][j] = (r4*p2.c1[i][j]) + (1-r4)*p1.c1[i][j];
				
			//	if (Math.abs(p1.velocity.w1[i][j] + p2.velocity.w1[i][j]) !=0){
					children[0].velocity.w1[i][j] = (p1.velocity.w1[i][j] + p2.velocity.w1[i][j]) * Math.abs(p1.velocity.w1[i][j]) / (Math.sqrt(p1.velocity.w1[i][j]*p1.velocity.w1[i][j] + p2.velocity.w1[i][j]*p2.velocity.w1[i][j]));
					children[1].velocity.w1[i][j] = (p1.velocity.w1[i][j] + p2.velocity.w1[i][j]) * Math.abs(p2.velocity.w1[i][j]) / (Math.sqrt(p1.velocity.w1[i][j]*p1.velocity.w1[i][j] + p2.velocity.w1[i][j]*p2.velocity.w1[i][j]));
			//	}
				//if (Math.abs(p1.velocity.c1[i][j] + p2.velocity.c1[i][j]) !=0){
					children[0].velocity.c1[i][j] = (p1.velocity.c1[i][j] + p2.velocity.c1[i][j]) * Math.abs(p1.velocity.c1[i][j]) / (Math.sqrt(p1.velocity.c1[i][j]*p1.velocity.c1[i][j] + p2.velocity.c1[i][j]*p2.velocity.c1[i][j]));
					children[1].velocity.c1[i][j] = (p1.velocity.c1[i][j] + p2.velocity.c1[i][j]) * Math.abs(p2.velocity.c1[i][j]) / (Math.sqrt(p1.velocity.c1[i][j]*p1.velocity.c1[i][j] + p2.velocity.c1[i][j]*p2.velocity.c1[i][j]));
				//}
			
			}
		}
		
		r1 = (double)random.nextDouble();
		r2 = (double)random.nextDouble();
		r3 = (double)random.nextDouble();
		r4 = (double) random.nextDouble();
		for(int i=0; i<outputQuantity; i++){
			for(int j=0; j<hiddenQuantity; j++){				
				children[0].w2[i][j] = (r1*p1.w2[i][j]) + (1-r1)*p2.w2[i][j];
				children[0].c2[i][j] = (r3*p1.c2[i][j]) + (1-r3)*p2.c2[i][j];
				
				children[1].w2[i][j] = (r2*p2.w2[i][j]) + (1-r2)*p1.w2[i][j];
				children[1].c2[i][j] = (r4*p2.c2[i][j]) + (1-r4)*p1.c2[i][j];
		//		if (Math.abs(p1.velocity.w2[i][j] + p2.velocity.w2[i][j]) !=0){
					children[0].velocity.w2[i][j] = (p1.velocity.w2[i][j] + p2.velocity.w2[i][j]) * Math.abs(p1.velocity.w2[i][j]) / (Math.sqrt(p1.velocity.w2[i][j]*p1.velocity.w2[i][j] + p2.velocity.w2[i][j]*p2.velocity.w2[i][j]));
					children[1].velocity.w2[i][j] = (p1.velocity.w2[i][j] + p2.velocity.w2[i][j]) * Math.abs(p2.velocity.w2[i][j]) / (Math.sqrt(p1.velocity.w2[i][j]*p1.velocity.w2[i][j] + p2.velocity.w2[i][j]*p2.velocity.w2[i][j]));
			//	}
		//		if (Math.abs(p1.velocity.c2[i][j] + p2.velocity.c2[i][j]) !=0){
					children[0].velocity.c2[i][j] = (p1.velocity.c2[i][j] + p2.velocity.c2[i][j]) * Math.abs(p1.velocity.c2[i][j]) / (Math.sqrt(p1.velocity.c2[i][j]*p1.velocity.c2[i][j] + p2.velocity.c2[i][j]*p2.velocity.c2[i][j]));
					children[1].velocity.c2[i][j] = (p1.velocity.c2[i][j] + p2.velocity.c2[i][j]) * Math.abs(p2.velocity.c2[i][j]) / (Math.sqrt(p1.velocity.c2[i][j]*p1.velocity.c2[i][j] + p2.velocity.c2[i][j]*p2.velocity.c2[i][j]));
		//		}

			}
		}
		
		r1 = (double)random.nextDouble();
		r2 = (double)random.nextDouble();
		r3 = (double)random.nextDouble();
		r4 = (double) random.nextDouble();
		for(int i=0; i<hiddenQuantity; i++){			
			children[0].h[i] = (r1*p1.h[i]) + (1-r1)*p2.h[i];
			children[1].h[i] = (r2*p2.h[i]) + (1-r2)*p1.h[i];

		//	if (Math.abs(p1.velocity.h[i] + p2.velocity.h[i]) !=0){
			children[0].velocity.h[i] = (p1.velocity.h[i] + p2.velocity.h[i]) * Math.abs(p1.velocity.h[i]) / (Math.sqrt(p1.velocity.h[i]*p1.velocity.h[i] + p2.velocity.h[i]*p2.velocity.h[i]));
			children[1].velocity.h[i] = (p1.velocity.h[i] + p2.velocity.h[i]) * Math.abs(p2.velocity.h[i]) / (Math.sqrt(p1.velocity.h[i]*p1.velocity.h[i] + p2.velocity.h[i]*p2.velocity.h[i]));

		//	}
		}
		
		
		r1 = (double)random.nextDouble();
		r2 = (double)random.nextDouble();
		for (int i=0; i<hiddenQuantity; i++){ 
			children[0].b1[i] = (r1*p1.b1[i]) + (1-r1)*p2.b1[i];
			children[1].b1[i] = (r2*p2.b1[i]) + (1-r2)*p1.b1[i];

		//	if (Math.abs(p1.velocity.b1[i] + p2.velocity.b1[i]) !=0){
				children[0].velocity.b1[i] = (p1.velocity.b1[i] + p2.velocity.b1[i]) * Math.abs(p1.velocity.b1[i]) / (Math.sqrt(p1.velocity.b1[i]*p1.velocity.b1[i] + p2.velocity.b1[i]*p2.velocity.b1[i]));
				children[1].velocity.b1[i] = (p1.velocity.b1[i] + p2.velocity.b1[i]) * Math.abs(p2.velocity.b1[i]) / (Math.sqrt(p1.velocity.b1[i]*p1.velocity.b1[i] + p2.velocity.b1[i]*p2.velocity.b1[i]));
		//	}
			
		}
		
		
		
		r1 = (double)random.nextDouble();
		r2 = (double)random.nextDouble();
		for (int i=0; i<outputQuantity; i++){ 
			children[0].b2[i] = (r1*p1.b2[i]) + (1-r1)*p2.b2[i];
			children[1].b2[i] = (r2*p2.b2[i]) + (1-r2)*p1.b2[i];

		
				children[0].velocity.b2[i] = (p1.velocity.b2[i] + p2.velocity.b2[i]) * Math.abs(p1.velocity.b2[i]) / (Math.sqrt(p1.velocity.b2[i]*p1.velocity.b2[i] + p2.velocity.b2[i]*p2.velocity.b2[i]));
				children[1].velocity.b2[i] = (p1.velocity.b2[i] + p2.velocity.b2[i]) * Math.abs(p2.velocity.b2[i]) / (Math.sqrt(p1.velocity.b2[i]*p1.velocity.b2[i] + p2.velocity.b2[i]*p2.velocity.b2[i]));
		


		}
		
		
		
		
		//MUTATION
		
		//mutacao do primeiro
		double probability = (double)random.nextDouble();
		double probDad = (double)random.nextDouble();
		Particle parent;
		if (probDad > 0.5){
			parent = p1;
		}else{
			parent = p2;
		}
		if (this.beta >=probability){
			Random rGaussian = rGaussianArg;
			double gaussianDist = (double) rGaussian.nextGaussian() * this.varianceGaussian;
			
			for (int i = 0; i < this.hiddenQuantity; i++) {
				for (int j = 0; j < this.inputQuantity; j++) {
					children[0].w1[i][j] = parent.w1[i][j] + gaussianDist * (this.allowableIteration-iter)/allowableIteration;
					
				}
			}
			for(int i=0; i<outputQuantity; i++){
				for(int j=0; j<hiddenQuantity; j++){				
					children[0].w2[i][j] = parent.w2[i][j] + gaussianDist * (this.allowableIteration-iter)/allowableIteration;					

				}
			}
		
			for(int i=0; i<hiddenQuantity; i++){			
				children[0].h[i] = parent.h[i] + gaussianDist * (this.allowableIteration-iter)/allowableIteration;		
				children[0].b1[i] = parent.b1[i] + gaussianDist * (this.allowableIteration-iter)/allowableIteration;

			}
			
			for (int i=0; i<outputQuantity; i++){ 
				children[0].b2[i] = parent.b2[i] + gaussianDist * (this.allowableIteration-iter)/allowableIteration;		
				

			}
		}
		
		//mutacao do segundo filho
		probability = (double)random.nextDouble();
		probDad = (double)random.nextDouble();

		if (probDad > 0.5){
			parent = p1;
		}else{
			parent = p2;
		}
		if (this.beta >=probability){
			Random rGaussian = rGaussianArg;
			double gaussianDist = (double) rGaussian.nextGaussian();
			
			for (int i = 0; i < this.hiddenQuantity; i++) {
				for (int j = 0; j < this.inputQuantity; j++) {
					children[1].w1[i][j] = parent.w1[i][j] + gaussianDist * (this.allowableIteration-iter)/allowableIteration;
					
				}
			}
			for(int i=0; i<outputQuantity; i++){
				for(int j=0; j<hiddenQuantity; j++){				
					children[1].w2[i][j] = parent.w2[i][j] + gaussianDist * (this.allowableIteration-iter)/allowableIteration;					

				}
			}
		
			for(int i=0; i<hiddenQuantity; i++){			
				children[1].h[i] = parent.h[i] + gaussianDist * (this.allowableIteration-iter)/allowableIteration;		
				children[1].b1[i] = parent.b1[i] + gaussianDist * (this.allowableIteration-iter)/allowableIteration;

			}


			for (int i=0; i<outputQuantity; i++){ 
				children[1].b2[i] = parent.b2[i] + gaussianDist * (this.allowableIteration-iter)/allowableIteration;		
				

			}
		}
		
		
		return children;
	}
	
	void printTestError() throws IOException{
		System.out.println("printar test erros");
		System.out.println("tamanho da pop: "+this.sizePopulation);
		
		double [][] setTest = readSetTest();
		for (int i=0; i<this.sizePopulation; i++){
			particles[i].runTest(this.sizeSetTest, setTest);
		}
		
		System.out.println("Criando o arquvio de teste");
		Arquivo saida = new Arquivo("vazio2", this.arqTest);
		System.out.println("Criado o arquivo de teste");
		for(int i=0; i<this.sizePopulation; i++){
			saida.print(particles[i].perfomanceErrorTest + "\n");
		}
		saida.close();
		System.out.println("Finalizado a impressao dos testes");
	}

	public static void main(String[] args) throws MalformedURLException,
			IOException {
		//System.out.println("oi");
	
		//PSO_RNA p = new PSO_RNA(50, 1, -1, 0.9, 0.4, 2.5, 0.5, 50, 0.7, 0.1, 0.7, 1000, 361, 50, 1, null, sizeSetValidation, 50);
		int sizePopulation =500; 
		double rangeVelocityMAX=1; 
		double rangeVelocityMin=-1; 
		double inertiaWeightsMax =(double) 0.9; 
		double inertiaWeightsMin = (double) 0.4; 
		double c1i=(double) 2.5;
		double c1f =0.5;
		double c2i=(double) 0.5;
		double c2f = 2.5;
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
		String arqSaidaVal = "arq_val44.out";
		String arqSaidaTest = "arq_test44.out";
		PSO_RNA p = new PSO_RNA(sizePopulation, rangeVelocityMAX, rangeVelocityMin, inertiaWeightsMax, inertiaWeightsMin, c1i, c2i, toleranceFail, alpha, beta, varianceGaussian, allowIteration, inputQ, hiddenQ, outputQ, setValidation, sizeSetValidation, quantitySelectedForGeneration, arqSaidaVal, arqSaidaTest, c1f, c2f);
		
	//	p.gabor();
		p.run();
		p.printTestError();
		
	}


}
