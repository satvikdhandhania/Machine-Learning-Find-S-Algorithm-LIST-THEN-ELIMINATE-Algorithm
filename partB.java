/*
 * Author: Satvik Dhandhania
 * Andrew ID: sdhandha
 * Date : 01/31/2016
 * 
 * Bias-free Learning, the Risk-4Cat task and the LIST-THEN-ELIMINATE 
 * algorithm
 * 
 * Prints 
 * a)Input Space
 * b)Size of Concept Space |C|
 * c)Forms version space from concept space using LIST-THEN-
 * ELIMINATE algorithm
 * d) Accepts test data file and then prints the number of positive 
 * and negative results from the set of version space previously created 
 */

import java.io.BufferedReader;
import java.io.FileReader;

public class partB {
	public static void main(String[] args) throws Exception{
		
		// Calculating input space 
		int inputSpace= (int)Math.pow(2,4); //2^x
		System.out.println(inputSpace);
		// Calculating hypothesis space
		int hypothesisSpace= (int)Math.pow(2,inputSpace);
		System.out.println(hypothesisSpace);
		// Initializations 
		int j,high,low;
		String row;
		String[] Attributes = new String[10];
		// Getting number of digits required for the array
		int num_digits=(int)(Math.log(hypothesisSpace)/Math.log(2));
		/* Initializing conceptSpace array for the problem. Adding a 
		 * column for flag to check for deleted rows.
		 */
		int[][] conceptSpace = new int[hypothesisSpace][num_digits+1];
		int[][] versionSpace;
		// Readers for reading input files 
		BufferedReader br = null;
		BufferedReader btest = null;
		/* Populating the conceptSpace array for all possible
		 * combinations. Using binary filling to represent all
		 * hypothesises in concept space.
		 */
		for(int i=0;i<hypothesisSpace;i++)
		{
			j=num_digits;
			int temp=i;
			while(temp>=0&&j>0)
			{
				j--;
				if(temp%2==1)
					conceptSpace[i][j]=1;
				else 
					conceptSpace[i][j]=0;
				temp=temp/2;
			}
			conceptSpace[i][num_digits]=1;
		}
		// Connecting the buffers to the actual file
		try
		{
			br = new BufferedReader(new FileReader("4Cat-Train.labeled"));
			btest = new BufferedReader(new FileReader(args[0]));
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return;
		}

		/* temp will store current attribute value, input stores the number
		 * to check in a specific column of the concept space. result stores
		 * the given output from the file.
		 */
		int temp=0,input,result;
		
		/*
		 *  Obtaining the version space from concept space by using
		 *  LIST-THEN-ELIMINATE algorithm.
		 */
		while((row=br.readLine())!=null)
		{	
			input=0;
			if(!row.equals("\n"))
			{
				// Splitting based on spaces and tabs 
				Attributes = row.split("\\s+");
				// Every alternate value is an attribute
				if(Attributes[1].equals("Male"))
					temp=1;
				else
					temp=0;
				input=temp;
				if(Attributes[3].equals("Old"))
					temp=1;
				else
					temp=0;
				input=input*2+temp;
				if(Attributes[5].equals("Yes"))
					temp= 1;
				else
					temp=0;
				input=(input*2)+temp;
				if(Attributes[7].equals("Yes"))
					temp= 1;
				else
					temp=0;
				input=input*2+temp;
				//Get actual result
				if(Attributes[7].equals("high"))
					result=1;
				else
					result=0;
				// Check for the same input in conceptSpace 
				for(int i=0;i<hypothesisSpace;i++)
				{	// Check only if it a valid row
					if(conceptSpace[i][num_digits]==1)
					{
						if(conceptSpace[i][input]!=result)
						{
							// If it does not match change the flag
							conceptSpace[i][num_digits]=0;
						}
					}
				}
			}
		}
		
		//Counting Valid Hypothesis
		int count=0;
		for(int i=0;i<hypothesisSpace;i++)
		{	// The rows that still have flag as 1 are valid
			if(conceptSpace[i][num_digits]==1)
			{
				count++;
			}
		}
		//CREATE Version Space
		System.out.println(count);
		versionSpace = new int[count][num_digits];
		temp=0;
		for(int i=0;i<hypothesisSpace;i++)
		{
			if(conceptSpace[i][num_digits]==1)
			{
				/* Copying from conceptSpace to version to increase performance
				 * Traversing an 65536 for a few valid hypothesis wastes a lot 
				 * of processing capability
				 */
				for(j=0;j<num_digits;j++)
					versionSpace[temp][j]=conceptSpace[i][j];
				temp++;
			}
		}

		//Testing on input set, file passed through argument to program
		while((row=btest.readLine())!=null)
		{
			input=0;
			if(!row.equals("\n"))
			{
				high=low=0;
				Attributes = row.split("\\s+");
				if(Attributes[1].equals("Male"))
					temp=1;
				else
					temp=0;
				input=temp;
				if(Attributes[3].equals("Old"))
					temp=1;
				else
					temp=0;
				input=input*2+temp;
				if(Attributes[5].equals("Yes"))
					temp= 1;
				else
					temp=0;
				input=(input*2)+temp;
				if(Attributes[7].equals("Yes"))
					temp= 1;
				else
					temp=0;
				input=input*2+temp;
				for(int i=0;i<count;i++)
				{
					//Checking the input in versionSpace
					if(versionSpace[i][input]==1)
						high++;
					else
						low++;		
				}
				//Print number of high and low votes
				System.out.println(high+" "+low);
			}
		}
		// Closing opened files
		try
		{
			br.close();
			btest.close();
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
