package org.pathplus.utils.state;

/**
 * 
 * @author levi.lovelock
 *
 */

public abstract class BaseState implements State<BaseState>{

	private double fValue, gValue, hValue;
	
	/**
	 * 
	 * @param parent
	 */
	public BaseState(BaseState parent){
		gValue = parent.gValue + 1;
		hValue = calcHValue();
		fValue = gValue + hValue;
	}
	
	
	public double getGVal(){
		return gValue;
	}
	
	public double getHVal(){
		return hValue;
	}
	
	public double getFVal(){
		return fValue;
	}
	
	public int compareTo(BaseState s){
		return (int) (this.fValue - s.fValue);
	}
	
}
