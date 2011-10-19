package org.pathplus.pathfinder.Internal;


import org.pathplus.utils.Internal.IPath;
import org.pathplus.utils.Internal.IState;



public interface IPathFinder {

	public <S> IPath findPath(IState<S> start, IState<S> end);
	
	public <S> IPath findPath(IState<S> start, IState<S> end, double weight);
	
	
	
}
