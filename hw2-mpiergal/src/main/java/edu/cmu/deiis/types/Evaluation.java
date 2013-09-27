

/* First created by JCasGen Thu Sep 26 20:10:15 EDT 2013 */
package edu.cmu.deiis.types;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** Evaluation of the answer scoring with precision at N.
 * Updated by JCasGen Thu Sep 26 20:10:15 EDT 2013
 * XML source: C:/Users/Mario/git/hw2-mpiergal/hw2-mpiergal/src/main/resources/descriptors/deiis_types.xml
 * @generated */
public class Evaluation extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(Evaluation.class);
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int type = typeIndexID;
  /** @generated  */
  @Override
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected Evaluation() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public Evaluation(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public Evaluation(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public Evaluation(JCas jcas, int begin, int end) {
    super(jcas);
    setBegin(begin);
    setEnd(end);
    readObject();
  }   

  /** <!-- begin-user-doc -->
    * Write your own initialization here
    * <!-- end-user-doc -->
  @generated modifiable */
  private void readObject() {/*default - does nothing empty block */}
     
 
    
  //*--------------*
  //* Feature: precisionAtN

  /** getter for precisionAtN - gets The precision at the top N ranked answers, where N is the number of correct answers in the gold standard annotation.
   * @generated */
  public double getPrecisionAtN() {
    if (Evaluation_Type.featOkTst && ((Evaluation_Type)jcasType).casFeat_precisionAtN == null)
      jcasType.jcas.throwFeatMissing("precisionAtN", "edu.cmu.deiis.types.Evaluation");
    return jcasType.ll_cas.ll_getDoubleValue(addr, ((Evaluation_Type)jcasType).casFeatCode_precisionAtN);}
    
  /** setter for precisionAtN - sets The precision at the top N ranked answers, where N is the number of correct answers in the gold standard annotation. 
   * @generated */
  public void setPrecisionAtN(double v) {
    if (Evaluation_Type.featOkTst && ((Evaluation_Type)jcasType).casFeat_precisionAtN == null)
      jcasType.jcas.throwFeatMissing("precisionAtN", "edu.cmu.deiis.types.Evaluation");
    jcasType.ll_cas.ll_setDoubleValue(addr, ((Evaluation_Type)jcasType).casFeatCode_precisionAtN, v);}    
  }

    