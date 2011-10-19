package org.pathplus.pathfinder;


import org.pathplus.utils.path.IPath;
import org.pathplus.utils.state.IState;




public interface IPathFinder {

	public <S> IPath findPath(IState<S> start, IState<S> end);
	
	public <S> IPath findPath(IState<S> start, IState<S> end, double weight);
	
	
	
}
