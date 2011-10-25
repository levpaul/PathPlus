package org.pathplus.utils.path;

import java.util.ArrayList;

import org.pathplus.utils.state.BaseState;
import org.pathplus.utils.state.State;

// TODO Sort this out as soon as possible.
public class PathResult<T extends State<T>> implements Result<T> {

	private T startState,  goalState;
	private ArrayList<T> pathArrayList;
	
	public PathResult(T goalState){
		generatePathArrayList(goalState);
		startState = pathArrayList.get(pathArrayList.size() - 1);
		goalState = pathArrayList.get(0);
	}
	
	private void generatePathArrayList(T goalState) {
		pathArrayList = new ArrayList<T>();
		T current = this.goalState;
		
		while(current.getParent() != null){
			pathArrayList.add(current.getParent());
			current = current.getParent();
		}
	}

	// TODO fix this immediately.
	@SuppressWarnings("unchecked")
	public T[] toArray() {
		return pathArrayList.toArray(new T[pathArrayList.size()]);
	}

	public double getLength() {
		return goalState.getGVal();
	}

	public T getGoalState() {
		return goalState;
	}

	public void setGoalState(T goalState) {
		this.goalState = goalState;
	}

	public T getStartState() {
		return startState;
	}

	public void setStartState(T startState) {
		this.startState = startState;
	}

	

}
