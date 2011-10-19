package org.pathplus.pathfinder;

import org.pathplus.utils.algorithms.SearchAlgorithms;
import org.pathplus.utils.path.IPath;
import org.pathplus.utils.state.IState;


public class PathFinder implements IPathFinder{

	private int searchAlgorithm;
	private double weight = 1.0;
	
	// The default constructor will use A* as the search algorithm by default.
	public PathFinder(){
		searchAlgorithm = SearchAlgorithms.A_STAR;
	}
	
	public PathFinder(int searchType){
		this.searchAlgorithm = searchType;
	}
	
	
	


	public <S> IPath findPath(IState<S> start, IState<S> end) {
		// TODO Auto-generated method stub
		return null;
	}

	public <S> IPath findPath(IState<S> start, IState<S> end, double weight) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	
	public void setWeight(double weight){
		this.weight = weight;
	}
	
	public double getWeight(){
		return weight;
	}
	
}
