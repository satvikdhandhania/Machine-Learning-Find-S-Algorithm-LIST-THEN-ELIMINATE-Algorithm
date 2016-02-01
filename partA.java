/*
 * Author: Satvik Dhandhania
 * Andrew ID: sdhandha
 * Date : 01/31/2016
 * 
 * Implementation of the Find S Algorithm.
 * Prints 
 * Input Space
 * Digits required by Concept Space |C|
 * Size of Hypothesis Space
 * Stores the current hypothesis after every 30 records in a file partA4.txt
 * Misclassification Rate
 * The remaining data is classification of inputs passed through a file.  
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

public class partA {
	public static void main(String[] args) throws Exception{
		
		//Initializations 
		String[] Attributes = new String[20];
		String[] hypothesis = new String[10];
		//Calculating Input Space
		int inputSpace= (int)Math.pow(2,9); //2^x
		System.out.println(inputSpace);
		//Calculating digits required for Concept Space
		int digits = (int) Math.floor(Math.log10(Math.pow(2,inputSpace)) + 1);
		System.out.println(digits);
		//Calculating Hypothesis Space size
		int hypothesisSpace= 3*3*3*3*3*3*3*3*3+1;
		System.out.println(hypothesisSpace);
		
		int y=0;
		int count=0;
		boolean flag=true;
		String row;
		BufferedReader br = null;
		BufferedWriter bw = null;
		//Initializing null hypothesis as default
		for(int i=0;i<9;i++)
		{
			hypothesis[i]="null";
		}
		//Setting up the file readers
		try
		{
			bw = new BufferedWriter(new FileWriter("partA4.txt"));
			br = new BufferedReader(new FileReader("9Cat-Train.labeled"));
		}catch(Exception e)
		{
			e.printStackTrace();
			return;
		}	
		
		//TRAINING the hypothesis and generalizing it
		while((row=br.readLine())!=null)
		{	
			count++;
			if(!row.equals("\n"))
			{	// Variable to check the correct parameter in hypothesis 
				y=0;
				//Splitting based on tabs and spaces 
				Attributes = row.split("\\s+");
				//We only consider positive examples 
				if(Attributes[Attributes.length-1].equals("high"))
				{
					for(int i=1;i<Attributes.length-2;i=i+2)
					{	//If first high row, we store values to the hypothesis
						if(flag)
						{
							hypothesis[y] = Attributes[i];
						}
						else
						{	// If not equal we generalize it by using ?
							if(!hypothesis[y].equals(Attributes[i]))
							{
								hypothesis[y]="?";
							}
						}
						y++;
					}
					//After first high row we start generalizing
					flag=false;
				}
			}
			//Storing current hypothesis aS a row to file every 30 inputs 
			if(count%30==0)
			{	
				for(int i=0;i<9;i++)
				{
					if(i==0)
					{	
						bw.write(hypothesis[i]);
					}
					else
					{	
						bw.write("\t"+hypothesis[i]);
					}
				}
				bw.write("\n");
			}
		}
		// Closing buffers
		try{
			bw.close();
			br.close();
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
		// Testing on dev to calculate misclassification rate
		int correct=0,total=0;
		try {
			// Setting buffer for dev file
			br = new BufferedReader(new FileReader("9Cat-Dev.labeled"));
		}catch(Exception e)
		{
			e.printStackTrace();
		}		
		while((row=br.readLine())!=null)
		{	
			flag=true;
			if(!row.equals("\n"))
			{
				total++;
				y=0;
				//Splitting each input based on spaces and tabs
				Attributes = row.split("\\s+");
				for(int i=1;i<Attributes.length-2;i=i+2)
					{	//Checking if attribute is already generalized 
						if(!hypothesis[y].equals("?"))
						{
							/* If hypothesis is not already generalized
							 * we compare the exact input
							 */ 
							if(!hypothesis[y].equals(Attributes[i]))
								flag=false;			
						}
						y++;
					}
				/* Flag here stores result of hypothesis flag stays true if  
				 * every input matches or is generalized. If false it fails
				 * to match at least one attribute.Compare obtained results 
				 * to given results. 
				 */
				if(flag&&Attributes[Attributes.length-1].equals("high"))
					correct++;
				if((!flag)&&(Attributes[Attributes.length-1].equals("low")))
					correct++;
			}
		}
		// Calculate misclassification rate using formula.
		float misclassification= (float)(total-correct)/total;
		System.out.println(misclassification);
		
		try{
			br.close();
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
		// Independent Test
		try {
			// Open file passed through arguments
			br = new BufferedReader(new FileReader(args[0]));
		}catch(Exception e)
		{
			e.printStackTrace();
		}		
		while((row=br.readLine())!=null)
		{	
			flag=true;
			if(!row.equals("\n"))
			{
				total++;
				y=0;
				Attributes = row.split("\\s+");
				for(int i=1;i<Attributes.length-2;i=i+2)
					{
						//Comparing attributes with hypothesis
						if(!hypothesis[y].equals("?"))
						{
							if(!hypothesis[y].equals(Attributes[i]))
								flag=false;			
						}
						y++;
					}
				// If any failure in matches fla is set to false
				if(flag)
					System.out.println("high");
				else
					System.out.println("low");
			}
		}
		try{
			br.close();
		}catch(Exception e)
		{
			e.printStackTrace();
		}	
	}
}
