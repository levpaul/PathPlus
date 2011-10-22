package org.pathplus.algorithms;

import org.pathplus.utils.path.Result;
import org.pathplus.utils.state.BaseState;


public interface Algorithm {
	
	public Result search(BaseState start, BaseState goal);
	
}
