package org.pathplus.algorithms;

import org.pathplus.utils.path.Result;
import org.pathplus.utils.state.State;


public interface Algorithm {
	
	public Result search(State start, State goal);
	
	
}
