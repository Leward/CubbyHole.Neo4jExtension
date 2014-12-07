package com.leward.cubbyhole.graphaware;

import org.apache.http.HttpStatus;
import org.junit.Test;

import static com.graphaware.test.util.TestUtils.get;

import com.graphaware.test.integration.NeoServerIntegrationTest;

public class FileSpaceDiffServerIntegrationTest extends NeoServerIntegrationTest {

	@Test
    public void apiShouldBeMounted() {
		get(baseUrl() + "/graphaware/diff", HttpStatus.SC_OK);
    }
	
}
