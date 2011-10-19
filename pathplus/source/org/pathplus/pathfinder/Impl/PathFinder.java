package org.pathplus.pathfinder.Impl;

import org.pathplus.pathfinder.Internal.IPathFinder;
import org.pathplus.utils.Impl.SearchAlgorithms;
import org.pathplus.utils.Internal.IPath;
import org.pathplus.utils.Internal.IState;

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
