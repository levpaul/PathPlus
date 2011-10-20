package org.pathplus.pathfinder;


import org.pathplus.utils.path.PathResult;
import org.pathplus.utils.state.State;




public interface PathFinder {

	public <S> PathResult findPath(State<S> start, State<S> end);
	
	public <S> PathResult findPath(State<S> start, State<S> end, double weight);
	
	
	
}
