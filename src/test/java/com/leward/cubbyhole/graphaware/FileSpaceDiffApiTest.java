package com.leward.cubbyhole.graphaware;

import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

import com.graphaware.test.integration.GraphAwareApiTest;
import static com.graphaware.test.util.TestUtils.get;
import com.leward.cubbyhole.graphaware.enums.Labels;
import com.leward.cubbyhole.graphaware.enums.RelTypes;

public class FileSpaceDiffApiTest extends GraphAwareApiTest {

	private Node fileSpace;
	
	@Before
	public void prepareData() {
		Transaction tx = getDatabase().beginTx();
		try {
			Node user = getDatabase().createNode(Labels.User);
			Node fileSpace = getDatabase().createNode(Labels.FileSpace);
			
			user.setProperty("email", "marquise.douglas@yahoo.com");
			user.createRelationshipTo(fileSpace, RelTypes.FILESPACE);
			
			Node level1File1 = getDatabase().createNode(Labels.File);
			Node level1File2 = getDatabase().createNode(Labels.File);
			Node level1Folder1 = getDatabase().createNode(Labels.Folder);
			Node level2File1 = getDatabase().createNode(Labels.File);
			
			fileSpace.createRelationshipTo(level1File1, RelTypes.CONTAINS);
			fileSpace.createRelationshipTo(level1File2, RelTypes.CONTAINS);
			fileSpace.createRelationshipTo(level1Folder1, RelTypes.CONTAINS);
			level1Folder1.createRelationshipTo(level2File1, RelTypes.CONTAINS);
			
			// Set updates
			level1File1.setProperty("updatedAt", 5L);
			fileSpace.setProperty("cascadeUpdatedAt", 5L);
			
			this.fileSpace = fileSpace;
			tx.success();
		}
		finally {
		   tx.close();
		}
	}
	
	@Test
	public void simpleTest() {
		String returnValue = get(baseUrl() + "/diff?fileSpaceId="+ fileSpace.getId() +"&since=4", HttpStatus.SC_OK);
		System.out.println(returnValue);
	}
	
}
