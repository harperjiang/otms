package org.harper.otms.web.i18n;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.MessageFormat;
import java.util.Properties;

public class PropertiesFileProcessor {

	public static void main(String[] args) throws Exception {
		File folder = new File("src/main/java/org/harper/otms/calendar/i18n");
		File[] props = folder.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith("properties");
			}
		});

		for (File prop : props) {
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			PrintWriter bufWriter = new PrintWriter(buffer);
			BufferedReader input = new BufferedReader(new InputStreamReader(
					new FileInputStream(prop)));

			String line = null;

			boolean goThrough = true;
			String includeDir = null;
			Properties includeProp = null;
			while ((line = input.readLine()) != null) {
				if (line.startsWith("#")) {

					if (line.startsWith("#include")) {
						includeDir = line;
						String includeName = line.substring(9);

						includeProp = new Properties();
						includeProp.load(new FileInputStream(MessageFormat
								.format("{0}{1}{2}.properties",
										folder.getAbsolutePath(),
										File.separator, includeName)));
						goThrough = false;
						continue;
					}
					if (line.startsWith("#{")) {
						continue;
					}

					if (line.startsWith("#}")) {
						bufWriter.println(includeDir);
						bufWriter.println("#{");

						for (Object key : includeProp.keySet()) {
							bufWriter.println(MessageFormat.format("{0}={1}",
									key, encode(includeProp
											.getProperty((String) key))));
						}

						bufWriter.println("#}");

						goThrough = true;
						includeDir = null;
						includeProp = null;
						continue;
					}

				} else if (goThrough) {
					bufWriter.println(line);
				}
			}

			input.close();

			bufWriter.flush();
			bufWriter.close();

			FileOutputStream replace = new FileOutputStream(prop);

			replace.write(buffer.toByteArray());
			replace.close();
		}
	}

	protected static String encode(String input) {
		StringBuilder sb = new StringBuilder();
		for (char c : input.toCharArray()) {
			if (c < 256) {
				sb.append(c);
			} else {
				String hex = Integer.toString(c, 16);
				sb.append("\\u");
				for (int i = 0; i < 4 - hex.length(); i++) {
					sb.append("0");
				}
				sb.append(hex.toUpperCase());
			}
		}
		return sb.toString();

	}
}
