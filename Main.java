/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject1;

/**
 *
 * @author Atta Noon
 */
import java.io.*;
import java.util.Scanner;
import java.util.Random;
public class Main {
    public static void main(String[] args) throws FileNotFoundException{
        
        
        ///solution does exist but takes time due to mutation, example of solution given
       File f= new File("C:\\Users\\Atta Noon\\Documents\\NetBeansProjects\\mavenproject1\\src\\main\\java\\files\\registration.data");
       Scanner sc = new Scanner(f);
       //classes
       int line;//take input from files
       int cse=1;//case for swapping in genetic algorithm
       int[][] classes=new int[223][3169];//Max 243 classes array with max 3169 students capacity
       int noclasses=0;
       int nostudents[]=new int[223]; //for storing number of studens per class
       int i=0,j=0,k=0,l=0,m=0,n=0;//counters used many times in this code         
       int sum=0;//total number of students per class
       while(sc.hasNextInt()){
           line=sc.nextInt();
           if(j==3169){
               nostudents[i]=sum;
               //System.out.println(i+" " +sum);
               sum=0;
               i++;
               j=0;
               //System.out.println(" ");
               
           }
           if(line==1){
           sum++;}
           classes[i][j]=line;    
           //System.out.print(classes[i][j]);
           j++;
            
       }
       //System.out.println(i+" " +sum);
       noclasses=i;//total number of classes
       sc.close();
       //general.info
       File f2=new File("C:\\Users\\Atta Noon\\Documents\\NetBeansProjects\\mavenproject1\\src\\main\\java\\files\\general.info");
       sc = new Scanner(f2);
       int numofexamdays=sc.nextInt();//maximum number of days
       int maxperroom=sc.nextInt();//number of maximum xams per room on a single day
       //System.out.println(numofexamdays+" "+maxperroom);
       sc.close();
       //capacity info.
       int numofrooms=0;//number of rooms
       int roomcapacity[]=new int[46];// capacity per room
       i=0;
       File f3=new File("C:\\Users\\Atta Noon\\Documents\\NetBeansProjects\\mavenproject1\\src\\main\\java\\files\\capacity.room");
       sc=new Scanner(f3);
       sum=0;//now sm will be used to calculate total capacity of all the rooms
       while(sc.hasNextInt()){
           roomcapacity[i]=sc.nextInt();
           sum=sum+roomcapacity[i];
           i++;
           //System.out.println(roomcapacity[i-1]);
       }
       
       numofrooms=i;    

       //System.out.print(numofrooms);
       sc.close();
       
       
       
        ////////////////////////////////////////////////////////
       ///From here we are going to try out the genetic work///
       int chromosome[][][]=new int[numofexamdays][maxperroom][numofrooms];//3 days with 46 rooms used 7 times per day
       //will store index of classroom in each room that it occupies
       //value=-1 means room empty
       for(i=0;i<numofexamdays;i++){
            for(j=0;j<maxperroom;j++){
                for(k=0;k<numofrooms;k++){
                    chromosome[i][j][k]=-1;
                }
            }
       }
       int temp=0;//used for various purposes
       //placing values
       //placing rooms in chromosome
       int currsub=0;//current subject
       int currstd=0;//current subjects students' number
       ///////////////placing all subjects's positions in classes for candidate chromosome ///////////////////////////// 
       for(i=0;i<numofexamdays;i++){
            for(j=0;j<maxperroom;j++){
                temp=sum;//temp holds sum of all room capacity
                currstd=nostudents[currsub];
                for(k=0;k<numofrooms;k++){
                    if(currsub<222){
                        if(currstd<temp){
                            while(currstd<=0&&currsub!=222){//while classes have 0 students, skip those classes
                                currsub++;
                                currstd=nostudents[currsub];
                            }
                            if(currstd>0  && currsub!=222 && currstd<temp){
                                chromosome[i][j][k]=currsub;
                                currstd=currstd-roomcapacity[k];
                                temp=temp-roomcapacity[k];
                            }
                        }
                    }
                    //System.out.print(chromosome[i][j][k]+" ");
                }
                if(currstd<temp&&currsub<222)
                    currsub++;
               // System.out.println("");
            }
           // System.out.println("");
        }
        //////////////////////////////////////////////////////////////////////
       /////////////////////checking for problems////////////////////////////
       int stdprb[][][]=new int[3169][maxperroom][3];//stdprb[x][] means student problems where x=std number index
       //[x][][0]=number of exams in one slot
       //[x][][1]=number of exams in consecutive slot
       //[x][][2]=total number of exams per day.
       int numofprob=1;//number of problems//used for fitness check
       int day[]=new int[numofexamdays];//for storing number of problems per day
       Random rand=new Random();
       int r,ri,rj,rk,r1,r2;//for random
       i=0;j=0;
       temp=0;
       sum=0;
       int maxprob=6000;//numofproblems for while loop
       int counter=0;//used for keeping tab on which day to swap
       int tempc;
       fill0(stdprb);
       for(i=0;i<numofexamdays;i++)
           day[0]=0;
       
       
       
       
       
       while(maxprob>4000){//just for checks, currently while loop continues endlessly untill cases are fixed
           //initial number of problems are 6338, but can come down to half
            //these for loops count number of problems
            maxprob=0;
            for(i=0;i<3;i++){
               System.out.println("number of problems on day" +(i+1)+": "+ day[i]/4);}
           for(m=0;m<numofexamdays;m++){
               numofprob=0;
               fill0(stdprb);
               for(i=0;i<3169;i++){
                   for(j=0;j<maxperroom;j++){
                       temp=0;
                       sum=0;
                       for(k=0;k<numofrooms;k++){
                           while(temp==k){
                               k++;
                           }
                           temp=k;
                           k--;
                           tempc=chromosome[m][j][k];
                            if(tempc!=-1){
                            if(classes[tempc][j]==1){//if student is taking class that occurs at that time of the day
                                stdprb[i][j][0]++;
                                if(j-1>=0){
                                    if(stdprb[i][j-1][0]>0)
                                        stdprb[i][j][1]=stdprb[i][j][1]+1;
                                }
                            }
                           }
                        }
                       for(n=0;n<j;n++){
                           sum=sum+stdprb[i][n][0];
                           if(stdprb[i][n][1]>2)
                               numofprob++;
                       }
                       if(sum>3)
                           numofprob++;
                       //System.out.println("Student no:"+i+" numofexams:"+sum+" time:"+j+" day:"+m);
                   }
               }
               if(maxprob<numofprob)
                   maxprob=numofprob;
               day[m]=numofprob;
               
           }
          // for(i=0;i<3;i++)
           //    System.out.println(day[i]);
           /////////////////////////////////////////////////////////////////////////

            //finding largest number or rooms oocupied by a single subject in the array with the most problems
            //case=1 means swap day 1 and 3
            //case=2 means swap day 2 and 3
            //case=3 means swap day 1 and 2
            //we will shift between 3 cases to reach optimal status
            //we will have a 5% chance for mutation 
            //
            if(maxprob<4000)
                break;//just in case we reach a solution
            r= rand.nextInt(100)+1;
            if(r<6){//if mutation possible, we remove only one subject from the day and place it somewhere else
                System.out.println("\nMutation occurred! Swapping 2 subjects");
                r1=rand.nextInt(noclasses+1);//get random class
                while(nostudents[r1]==0)
                    r1=rand.nextInt(noclasses+1);
                r2=rand.nextInt(noclasses+1);//get random class thats not r1
                while(nostudents[r2]==0||r1==r2){
                    System.out.println(r1+" "+r2);
                    r2=rand.nextInt(noclasses+1);
                    
                }
                for(i=0;i<numofexamdays;i++){//swapping locations of 2 classes
                    for(j=0;j<maxperroom;j++){
                        for(k=0;k<numofrooms;k++){
                            if(chromosome[i][j][k]==r1)
                                chromosome[i][j][k]=r2;
                            else if(chromosome[i][j][k]==r2)
                                chromosome[i][j][k]=r1;
                        }
                    }
                }
                    System.out.println("Swapping "+r1+" and "+r2);
            }else{
            
                switch (cse) {
                    case 1:
                        if(counter==maxperroom){
                            counter=0;
                        }   i=counter;
                        for(j=0;j<numofrooms;j++){
                            temp=chromosome[2][i][j];
                            chromosome[2][i][j]=chromosome[0][i][j];
                            chromosome[0][i][j]=temp;
                        }   counter++;
                        cse=2;
                        break;
                    case 2:
                        if(counter==maxperroom){
                            counter=0;
                        }   i=counter;
                        for(j=0;j<numofrooms;j++){
                            temp=chromosome[2][i][j];
                            chromosome[2][i][j]=chromosome[1][i][j];
                            chromosome[1][i][j]=temp;
                        }   counter++;
                        cse=3;
                        break;
                    case 3:
                        if(counter==maxperroom){
                            counter=0;
                        }   i=counter;
                        for(j=0;j<numofrooms;j++){
                            temp=chromosome[1][i][j];
                            chromosome[1][i][j]=chromosome[0][i][j];
                            chromosome[0][i][j]=temp;
                        }   counter++;
                        cse=1;
                        break;
                    default:
                        break;
                }
            }
            System.out.println("Maximum problems: "+ maxprob/4);
            
        
       }
       System.out.println("\n\nSolution found!");
       for(i=0;i<3;i++){
            System.out.println("number of problems on day" +(i+1)+": "+ day[i]/4);}
       System.out.println("The numbers represent the class numbers in the respective rooms, -1 is empty room.\n"
               + "The timeings per day are represented by the rows\n"
               + "The rooms are column wise.\n "
               + "The days are seperated by the extra line of space inbetween.\n ");
         
       
       for(i=0;i<numofexamdays;i++){
            for(j=0;j<maxperroom;j++){
                for(k=0;k<numofrooms;k++){
                    System.out.print(chromosome[i][j][k]+" ");
                }
                System.out.println("");
            }
            System.out.println("");
        }
       
       
    }
    
    static void fill0(int arr[][][]){
           for(int i=0;i<3169;i++){
               for(int j=0;j<7;j++){
                   for(int k=0;k<3;k++)
                       arr[i][j][k]=0;
               }
           }
    }
       
    
}
