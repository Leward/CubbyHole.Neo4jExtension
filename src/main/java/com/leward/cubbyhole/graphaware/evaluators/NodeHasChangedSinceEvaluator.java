package com.leward.cubbyhole.graphaware.evaluators;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.traversal.Evaluation;
import org.neo4j.graphdb.traversal.Evaluator;
import org.neo4j.helpers.collection.Iterables;

public class NodeHasChangedSinceEvaluator implements Evaluator {

	private Long since;
	
	public NodeHasChangedSinceEvaluator(Long since) {
		this.since = since;
	}
	
	public Evaluation evaluate(Path path) {
		Node lastNode = Iterables.last(path.nodes());
		Long updatedAt = (Long) lastNode.getProperty("updatedAt", null);
		Long cascadedUpdatedAt = (Long) lastNode.getProperty("cascadeUpdatedAt", null);
		Long mergedUpdatedAt = keepBigger(updatedAt, cascadedUpdatedAt);
		if (mergedUpdatedAt == null || mergedUpdatedAt < since) {
			return Evaluation.EXCLUDE_AND_PRUNE;
		}
		else {
			if(updatedAt == null || updatedAt < since) {
				return Evaluation.EXCLUDE_AND_CONTINUE;
			}
			else {
				return Evaluation.INCLUDE_AND_CONTINUE;
			}
		}
	}
	
	private Long keepBigger(Long long1, Long long2) {
		if(long1 == null) {
			return long2;
		}
		else if(long2 == null) {
			return long1;
		}
		else if(long1 > long2) {
			return long1;
		}
		else {
			return long2;
		}
	}
	
}
