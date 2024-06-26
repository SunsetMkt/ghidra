/* ###
 * IP: GHIDRA
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ghidra.jython;

import java.io.*;
import java.util.regex.Pattern;

import generic.jar.ResourceFile;
import ghidra.app.script.*;

public class JythonScriptProvider extends GhidraScriptProvider {

	private static final Pattern BLOCK_COMMENT = Pattern.compile("'''");

	@Override
	public void createNewScript(ResourceFile newScript, String category) throws IOException {
		PrintWriter writer = new PrintWriter(new FileWriter(newScript.getFile(false)));
		writeHeader(writer, category);
		writer.println("");
		writeBody(writer);
		writer.println("");
		writer.close();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * <p>
	 * In Jython this is a triple single quote sequence, "'''".
	 * 
	 * @return the Pattern for Jython block comment openings
	 */
	@Override
	public Pattern getBlockCommentStart() {
		return BLOCK_COMMENT;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * <p>
	 * In Jython this is a triple single quote sequence, "'''".
	 * 
	 * @return the Pattern for Jython block comment openings
	 */
	@Override
	public Pattern getBlockCommentEnd() {
		return BLOCK_COMMENT;
	}

	@Override
	public String getCommentCharacter() {
		return "#";
	}

	@Override
	protected String getCertifyHeaderStart() {
		return "## ###";
	}

	@Override
	protected String getCertificationBodyPrefix() {
		return "#";
	}

	@Override
	protected String getCertifyHeaderEnd() {
		return "##";
	}

	@Override
	public String getDescription() {
		return "Jython";
	}

	@Override
	public String getExtension() {
		return ".py";
	}

	@Override
	public GhidraScript getScriptInstance(ResourceFile sourceFile, PrintWriter writer)
			throws GhidraScriptLoadException {

		try {
			Class<?> clazz = Class.forName(JythonScript.class.getName());
			GhidraScript script = (GhidraScript) clazz.getConstructor().newInstance();
			script.setSourceFile(sourceFile);
			return script;
		}
		catch (Exception e) {
			throw new GhidraScriptLoadException(e);
		}
	}
}
