package org.pathplus.algorithms;

import org.pathplus.utils.path.Result;
import org.pathplus.utils.state.State;


public interface Algorithm<T extends State<T>> {
	
	// TODO - fix this inheritence issue.
	public Result<T> search(T start, T goal);
	
}
