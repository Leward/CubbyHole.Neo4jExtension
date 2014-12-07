package com.leward.cubbyhole.graphaware;

import java.io.Serializable;

import com.graphaware.api.JsonNode;
import com.graphaware.module.algo.path.JsonPath;

public class DiffResult implements Serializable {

	private static final long serialVersionUID = 3814431903035282984L;

	private JsonNode fileOrFolder;
	
	private JsonNode originalContent;
	
	private JsonPath path;
	
	public DiffResult() {
	}

	public DiffResult(JsonNode fileOrFolder, JsonNode originalContent, JsonPath path) {
		super();
		this.fileOrFolder = fileOrFolder;
		this.originalContent = originalContent;
		this.path = path;
	}

	public JsonNode getFileOrFolder() {
		return fileOrFolder;
	}

	public void setFileOrFolder(JsonNode fileOrFolder) {
		this.fileOrFolder = fileOrFolder;
	}

	public JsonNode getOriginalContent() {
		return originalContent;
	}

	public void setOriginalContent(JsonNode originalContent) {
		this.originalContent = originalContent;
	}

	public JsonPath getPath() {
		return path;
	}

	public void setPath(JsonPath path) {
		this.path = path;
	}
	
	
}
