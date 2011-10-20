package org.pathplus.pathfinder;

import org.pathplus.utils.algorithmsindex.SearchAlgorithms;
import org.pathplus.utils.path.PathResult;
import org.pathplus.utils.state.State;

/**
 * 
 * @author levi.lovelock
 *
 */

public class PathFinderImpl implements PathFinder{

	private int searchAlgorithm;
	private double weight = 1.0;
	
	// The default constructor will use A* as the search algorithm by default.
	public PathFinderImpl(){
		searchAlgorithm = SearchAlgorithms.A_STAR;
	}
	
	public PathFinderImpl(int searchType){
		this.searchAlgorithm = searchType;
	}


	public <S> PathResult findPath(State<S> start, State<S> end) {
		// TODO Auto-generated method stub
		return null;
	}


	public <S> PathResult findPath(State<S> start, State<S> end, double weight) {
		
		return null;
	}
	
	
	
	
	public void setWeight(double weight){
		this.weight = weight;
	}
	
	public double getWeight(){
		return weight;
	}
	
}
