package com.leward.cubbyhole.graphaware;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.QueryParam;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.neo4j.helpers.collection.Iterables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.graphaware.api.JsonNode;
import com.graphaware.module.algo.path.JsonPath;
import com.graphaware.module.algo.path.JsonPathFinderInput;
import com.leward.cubbyhole.graphaware.enums.Labels;
import com.leward.cubbyhole.graphaware.enums.RelTypes;
import com.leward.cubbyhole.graphaware.evaluators.NodeHasChangedSinceEvaluator;

@Controller
@RequestMapping("/diff")
public class FileSpaceDiffApi {

	private final GraphDatabaseService database;
	
	@Autowired
	public FileSpaceDiffApi(GraphDatabaseService database) {
		this.database = database;
	}

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public List<DiffResult> diff(@QueryParam("fileSpaceId") Long fileSpaceId, @QueryParam("since") Long since) {
		if(fileSpaceId == null && since == null) {
			return null;
		}
		List<DiffResult> results = new ArrayList<DiffResult>();
		Transaction tx = database.beginTx();
		try {
			Node startNode = database.getNodeById(fileSpaceId);
			TraversalDescription traversalDescription = database.traversalDescription()
					.depthFirst()
					.relationships(RelTypes.CONTAINS, Direction.OUTGOING)
					.evaluator(new NodeHasChangedSinceEvaluator(since));
			for(Path path : traversalDescription.traverse(startNode)) {
				DiffResult diffResult = new DiffResult();
				Node lastNode = Iterables.last(path.nodes());
				diffResult.setFileOrFolder(new JsonNode(lastNode));
				Node original = findOriginal(lastNode);
				diffResult.setOriginalContent((original == null) ? null : new JsonNode(original));
				diffResult.setPath(new JsonPath(path, new JsonPathFinderInput()));
				results.add(diffResult);
			}
			tx.success();
		}
		finally {
			tx.close();
		}
		return results;
	}
	
	/**
	 * Find the original of a file or a folder. Returns null if there is no original 
	 * (typically when the file is not a shared file (same for folders)
	 * Must be used in a DB transaction
	 * @param node
	 * @return
	 */
	private Node findOriginal(Node node) {
		if(!isNodeSharedItem(node)) {
			return null;
		}
		
		TraversalDescription traversalDescription = database.traversalDescription()
				.depthFirst()
				.relationships(RelTypes.SHARE);
		Path sharePath = null;
		for(Path path : traversalDescription.traverse(node)) {
			sharePath = path;
		}
		
		if(sharePath == null) {
			return null;
		}
		else {
			return sharePath.endNode();
		}
	}
	
	private boolean isNodeSharedItem(Node node) {
		for(Label label : node.getLabels()) {
			if(label.equals(Labels.SharedFile) || label.equals(Labels.SharedFolder)) {
				return true;
			}
		}
		return false;
	}
	
}
