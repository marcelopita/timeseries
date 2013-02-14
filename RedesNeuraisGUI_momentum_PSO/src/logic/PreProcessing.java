package logic;

import java.util.ArrayList;

public class PreProcessing {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Arquivo file = new Arquivo ("iris.data.txt", "outputProcessedNetwork.txt");
		int sizeInput = 5;
		
		
		while (!file.isEndOfFile()){
			String a= file.readString();
			//System.out.println(a);
			String as [] = a.split(",");
			for (String string : as) {
				if (string.contains("Iris-setosa")){
					file.print("0 0 1"+'\n');
				}else if (string.contains("Iris-versicolor")){
					file.print("0 1 0"+'\n');
				}else if (string.contains("Iris-virginica")){
					file.print("1 0 0"+'\n');
				}else{
					file.print(string+" ");
				}
				System.out.println(string);
			}

		}
		
		file.close();
	}

}
