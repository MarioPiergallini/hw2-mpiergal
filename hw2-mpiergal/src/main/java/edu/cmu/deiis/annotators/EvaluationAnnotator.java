/* AnswerAnnotator.java
 *  @author Mario Piergallini
 */

package edu.cmu.deiis.annotators;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.cas.FSIndex;
import org.apache.uima.jcas.JCas;
import edu.cmu.deiis.types.*;

//Annotator that ranks answers by score then calculates precision at N

public class EvaluationAnnotator extends JCasAnnotator_ImplBase {
  
 public void process(JCas aJCas) {
   
   System.out.println("Evaluation annotator called");
   
   Evaluation eval = new Evaluation(aJCas);
   
   //get answer iterator, initialize counts
   FSIndex ansIndex = aJCas.getAnnotationIndex(AnswerScore.type);
   Iterator ansIter = ansIndex.iterator();
   int numCorrect = 0;
   ArrayList<Boolean> goldStd = new ArrayList<Boolean>();
   ArrayList<Double> scores = new ArrayList<Double>();
   //iterate over answers, creating array of gold std annotation + our scores
   while (ansIter.hasNext()) {
     AnswerScore ans = (AnswerScore)ansIter.next();
     //increase count of numCorrect (N for precision@N) if answer is correct
     if (ans.getAnswer().getIsCorrect()) {numCorrect++;}
     goldStd.add(ans.getAnswer().getIsCorrect());
     scores.add(ans.getScore());
   }
   int n = numCorrect;
   System.out.println("There are " + n + " correct answers.");
   //grab top N scores, N=numCorrect
   double bestScore = -1;
   double secondBest = -1;
   int bestIndex = 0;
   int correctAtN = 0;
   for(int i=0;i<n;i++){
     //iterate through scores to grab best one
     for(int j=0;j<scores.size();j++){
       if (scores.get(j)>bestScore) {
         secondBest = bestScore;
         bestScore = scores.get(j);
         bestIndex = j;
       } else if (scores.get(j)>secondBest) {
         secondBest = scores.get(j);
       }
     }
     //add to count of top answers correct at N
     if (goldStd.get(bestIndex)) {correctAtN++;}
     System.out.println("Score " + (i+1) + ": " + scores.get(bestIndex) + " is " + goldStd.get(bestIndex));
     //second best is new best for next iteration
     bestScore = secondBest;
     secondBest = -1;
     //remove best answer from ArrayLists
     scores.remove(bestIndex);
     goldStd.remove(bestIndex);
     bestIndex = 0;
   }
   
   //calculate and set precision
   double precis = (double)correctAtN/(double)n;
   System.out.println("Precision at N=" + n + " is " + precis);
   eval.setPrecisionAtN(precis);
   eval.addToIndexes();
 }
}
